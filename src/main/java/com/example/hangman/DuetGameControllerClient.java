package com.example.hangman;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class DuetGameControllerClient implements Initializable {

    private static String playerlogin;
    private static String serverHostname;
    private String actualPhrase;
    private String[] phrase;
    private String teammateLogin;

    private Socket socket;
    private int phraseId;
    private List<Character> usedLetters = new ArrayList<>();
    private int howManyMissed = 0;
    private String lastLetter;
    @FXML private ImageView exitImage, logoutImage, hangmanImage;
    @FXML private Label phraseLabel, categoryLabel, errorLabel, correctLettersLabel , incorrectUsedLettersLabel, winLabel, playerInfoLabel;
    @FXML private TextField letterField;
    @FXML private AnchorPane letterBox;
    @FXML private Button menuReturnButton;

    public DuetGameControllerClient() {}
    public DuetGameControllerClient(String playerlogin, String ServerHostname){
        this.playerlogin = playerlogin;
        this.serverHostname = ServerHostname;
    }

    public void openWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HangmanApplication.class.getResource("duetnormalgameclient-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 735, 500);
        Stage stage = new Stage();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon.png")));
        stage.setOnCloseRequest(windowEvent -> {
            try {
                disconnect();
                if(socket != null) if(!socket.isClosed()) socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);
        });
        stage.getIcons().add(image);
        stage.setResizable(false);
        stage.setTitle("Wisielec - Gra w duecie");
        stage.setScene(scene);
        stage.show();
    }

    public boolean checkLetter() throws SQLException {
        boolean isGood = false;
        errorLabel.setText("");

        if(letterField.getText().length() == 0) { errorLabel.setText("Wpisz literę!"); }
        else if(letterField.getText().length()>1) { errorLabel.setText("Za dużo liter!"); }
        else {
            char letter = letterField.getText().charAt(0);

            if(!Character.isLetter(letter)) { errorLabel.setText("Wpisz literę!"); }
            else{
                boolean isUnique = true;
                for (char c : usedLetters) {
                    if (c == letter) {
                        isUnique = false;
                        break;
                    }
                }
                if(!isUnique){ errorLabel.setText("Już użyta!"); }
                else{
                    usedLetters.add(letter);
                    incorrectUsedLettersLabel.setText("");
                    correctLettersLabel.setText("");
                    howManyMissed = 0;
                    // sprawdzenie które z użytych liter znajdują się w haśle, a które nie
                    for (char c: usedLetters) {
                        if(phrase[0].indexOf(c) == -1) {  // 'phrase[0].indexOf(c)' zwraca '-1' jeżeli 'c' nie występuje w stringu 'phrase[0]'
                            incorrectUsedLettersLabel.setText(incorrectUsedLettersLabel.getText() + " " + c);

                            if((howManyMissed++) < 8){
                                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/s" + howManyMissed + ".gif")));
                                hangmanImage.setImage(image);
                            }
                        }
                        else { correctLettersLabel.setText(correctLettersLabel.getText() + " " + c); }
                    }

                    phraseLabel.setText(hidePhrase(phrase[0])); // wstawienie odgadniętych liter w "puste pola"
                    actualPhrase = hidePhrase(phrase[0]);
                    checkWin();
                    lastLetter = String.valueOf(letter);
                    isGood = true;
                }
            }
        }
        letterField.setText("");
        return isGood;
    }

    public void updateGame(String teammateLetter) throws SQLException {

        char letter = teammateLetter.charAt(0);
        usedLetters.add(letter);

        Platform.runLater(() -> {
            incorrectUsedLettersLabel.setText("");
            correctLettersLabel.setText("");
        });

        howManyMissed = 0;
        // sprawdzenie które z użytych liter znajdują się w haśle, a które nie
        for (char c : usedLetters) {
            if (phrase[0].indexOf(c) == -1) {  // 'phrase[0].indexOf(c)' zwraca '-1' jeżeli 'c' nie występuje w stringu 'phrase[0]'

                Platform.runLater(() -> incorrectUsedLettersLabel.setText(incorrectUsedLettersLabel.getText() + " " + c));

                if ((howManyMissed++) < 8) {
                    Platform.runLater(() -> {
                        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/s" + howManyMissed + ".gif")));
                        hangmanImage.setImage(image);
                    });
                }
            } else {
                Platform.runLater(() -> correctLettersLabel.setText(correctLettersLabel.getText() + " " + c));
            }
        }
        Platform.runLater(() -> phraseLabel.setText(hidePhrase(phrase[0]))); // wstawienie odgadniętych liter w "puste pola"
        actualPhrase = hidePhrase(phrase[0]);
        checkWin();
    }

    @FXML
    public void sendMove() throws SQLException {
        if(checkLetter()){

            Thread t = new Thread(() -> {
                try {
                    System.out.println("[KLIENT]: Wysylam ruch.");

                    JSONObject odp = new JSONObject();
                    odp.put("letter", lastLetter);
                    System.out.println("[KLIENT]: lastLetter = '"+lastLetter+"'");

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bw.write(odp.toString());
                    bw.newLine();
                    bw.flush();

                    Platform.runLater(() -> {
                        if (!menuReturnButton.isVisible())
                            playerInfoLabel.setText("Oczekiwanie na ruch gracza...");
                        else playerInfoLabel.setText("");
                    });
                    Platform.runLater(() -> letterField.setDisable(true));

                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String t1 = br.readLine();
                    JSONObject json = new JSONObject(t1);
                    String teammateLetter = json.optString("letter");

                    if (teammateLetter.equals("<disconnected>")){
                        Platform.runLater(() -> {
                            phraseLabel.setTextFill(Color.web("#ff0000"));
                            phraseLabel.setText("Gracz opuścił rozgrywkę!\n Wróć do menu i spróbuj ponownie.");
                            playerInfoLabel.setVisible(false);
                            categoryLabel.setVisible(false);
                            winLabel.setVisible(false);
                            hangmanImage.setVisible(false);
                            letterBox.setVisible(false);
                            menuReturnButton.setVisible(true);
                        });
                        socket.close();
                    }
                    else{
                        System.out.println("[KLIENT]: Odebrano ruch");

                        updateGame(teammateLetter);
                        checkWin();

                        Platform.runLater(() -> letterField.setDisable(false));
                        System.out.println("Serwer wybrał literę '" + teammateLetter + "'.");
                        Platform.runLater(() -> playerInfoLabel.setText("Gracz " + teammateLogin + " wybrał literę '" + teammateLetter + "'."));
                    }


                } catch (IOException | SQLException e){
                    System.out.println("[KLIENT]: Zakończono połączenie!");
                }
            });
            t.start();
        }
    }

    public void checkWin() throws SQLException {

        // sprawdzenie czy hasło zawiera jeszcze jakieś niezgadnięte litery
        Platform.runLater(() -> actualPhrase = phraseLabel.getText());

        if (!actualPhrase.contains("-")) { // jeżeli nie ma niezgadniętych liter
            Platform.runLater(() -> {
                winLabel.setText("Rozwiązano hasło!");
                winLabel.setVisible(true);
                letterBox.setVisible(false);
                menuReturnButton.setVisible(true);
            });
        }
        else if (howManyMissed == 8) {
            Platform.runLater(() -> {
                winLabel.setTextFill(Color.web("#ff0000"));
                winLabel.setText("Przegrana!\nKoniec gry :(");
                winLabel.setVisible(true);
                letterBox.setVisible(false);
                menuReturnButton.setVisible(true);
                phraseLabel.setText(phrase[0]);
            });
        }
    }

    public String hidePhrase(String phrase){
        String hidden = "";
        for(int i=0; i<phrase.length(); i++){
            if (usedLetters.contains(phrase.charAt(i))) hidden += phrase.charAt(i);
            else if(Character.isLetter(phrase.charAt(i))) hidden += '-';
            else hidden += phrase.charAt(i);
        }
        return hidden;
    }

    public void disconnect() throws IOException {
        if(socket != null) {
            if(!socket.isClosed()){
                JSONObject odp = new JSONObject();
                odp.put("letter", "<disconnected>");
                System.out.println("[KLIENT]: rozłączono");

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write(odp.toString());
                bw.newLine();
                bw.flush();
            }

        }
    }

    @FXML
    public void onHomeImageClick() throws IOException {
        disconnect();
        if(socket != null) socket.close();
        MainMenuController mmc = new MainMenuController(playerlogin);
        mmc.openWindow();
        Stage stage = (Stage) exitImage.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onExitImageClick() throws IOException {
        disconnect();
        if(socket != null) socket.close();
        Stage stage = (Stage) exitImage.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onLogoutImageClick() throws IOException {
        disconnect();
        if(socket != null) socket.close();
        LoginController lc = new LoginController();
        lc.reopenWindow();
        Stage stage = (Stage) logoutImage.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Thread t = new Thread(() -> {
            try {

                Platform.runLater(() -> {
                    phraseLabel.setText("Łączenie z hostem...");
                    categoryLabel.setVisible(false);
                    winLabel.setVisible(false);
                    hangmanImage.setVisible(false);
                    letterBox.setVisible(false);
                });

                System.out.println("[KLIENT]: Lacze z ip: '" + serverHostname + "'...");
                socket = new Socket(serverHostname, 8080);
                System.out.println("[KLIENT]: Połączenie udane!");

                Platform.runLater(() -> {
                    playerInfoLabel.setText("Dołączono do gry!");
                    categoryLabel.setVisible(true);
                    hangmanImage.setVisible(true);
                    letterBox.setVisible(true);
                });

                //wysyłanie naszego loginu hostowi
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write(playerlogin);
                bw.newLine();
                bw.flush();

                //odbieranie informacji o grze (id Hasła itp.)
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String s = br.readLine();
                JSONObject gameInfo = new JSONObject(s);
                phraseId = gameInfo.optInt("phraseId");
                phrase = new String[3];
                phrase[0] = gameInfo.getString("phraseTitle");
                phrase[1] = gameInfo.getString("phraseCategory");
                phrase[2] = Integer.toString(gameInfo.getInt("phraseId"));
                teammateLogin = gameInfo.getString("hostLogin");

                Platform.runLater(() -> {
                    phraseLabel.setText(hidePhrase(phrase[0]));
                    actualPhrase = hidePhrase(phrase[0]);
                    categoryLabel.setText("Kategoria: " + phrase[1]);
                });

            } catch (IOException e) {
                System.out.println("[KLIENT]: Zakończono połączenie!");
                Platform.runLater(() -> {
                    phraseLabel.setTextFill(Color.web("#ff0000"));
                    phraseLabel.setText("Połączenie nieudane!\n Wróć do menu i spróbuj ponownie.");
                    categoryLabel.setVisible(false);
                    winLabel.setVisible(false);
                    hangmanImage.setVisible(false);
                    letterBox.setVisible(false);
                    menuReturnButton.setVisible(true);
                });

            }
        });
        t.start();

        // aby w fieldzie byly same wielkie litery
        letterField.textProperty().addListener((ov, oldValue, newValue) -> letterField.setText(newValue.toUpperCase()));
    }
}
