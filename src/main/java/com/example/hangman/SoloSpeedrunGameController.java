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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SoloSpeedrunGameController implements Initializable {

    private static String playerlogin;

    // scheduler do odliczania czasu w grze
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private String[] phrase;
    private int phraseId;
    private List<Integer> usedPhrases = new ArrayList<>();
    private List<Character> usedLetters = new ArrayList<>();
    private int howManyMissed = 0;
    private int scoreCounter = 0;

    @FXML private ImageView exitImage, logoutImage, hangmanImage;
    @FXML private Label phraseLabel, categoryLabel, errorLabel, correctLettersLabel , incorrectUsedLettersLabel, winLabel, timerLabel, scoreCounterLabel;
    @FXML private TextField letterField;
    @FXML private AnchorPane letterBox;
    @FXML private Button menuReturnButton;

    public SoloSpeedrunGameController() {}

    public SoloSpeedrunGameController(String login){
        playerlogin = login;
    }

    public void openWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HangmanApplication.class.getResource("solospeedrungame-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 735, 500);
        Stage stage = new Stage();

        //zakończenie działania schedulera podczas zamknięcia okna
        stage.setOnCloseRequest(windowEvent -> {
            scheduler.shutdown();
            System.exit(0);
        });

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon.png")));
        stage.getIcons().add(image);
        stage.setResizable(false);
        stage.setTitle("Wisielec - Szybka gra solo");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void checkLetter() throws SQLException {
        errorLabel.setVisible(true);
        errorLabel.setText("");
        if(letterField.getText().length() == 0){ errorLabel.setText("Wpisz literę!"); }
        else if(letterField.getText().length()>1){ errorLabel.setText("Za dużo liter!"); }
        else {
            char letter = letterField.getText().charAt(0);

            if(!Character.isLetter(letter)){ errorLabel.setText("Wpisz literę!"); }
            else{
                boolean isUnique = true;
                for (char c : usedLetters) { if(c == letter) isUnique = false; }
                if(!isUnique){ errorLabel.setText("Już użyta!"); }
                else{
                    usedLetters.add(letter);
                    incorrectUsedLettersLabel.setText("");
                    correctLettersLabel.setText("");
                    howManyMissed = 0;
                    // sprawdzenie które z użytych liter znajdują się w haśle, a które nie
                    for (char c: usedLetters) {
                        if(phrase[0].indexOf(c) == -1 ){  // 'phrase[0].indexOf(c)' zwraca '-1' jeżeli 'c' nie występuje w stringu 'phrase[0]'
                            incorrectUsedLettersLabel.setText(incorrectUsedLettersLabel.getText() + " " + c);

                            if((howManyMissed++) < 8){
                                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/s" + howManyMissed + ".gif")));
                                hangmanImage.setImage(image);
                            }
                        }
                        else {
                            correctLettersLabel.setText(correctLettersLabel.getText() + " " + c);
                        }
                    }
                    phraseLabel.setText(hidePhrase(phrase[0])); // wstawienie odgadniętych liter w "puste pola"
                    checkWin();
                }
            }
        }
        letterField.setText("");
    }

    public void checkWin() throws SQLException {

        // sprawdzenie czy hasło zawiera jeszcze jakieś niezgadnięte litery
        if(!phraseLabel.getText().contains("-")){   // jeżeli nie ma niezgadniętych liter
            usedLetters.clear();
            correctLettersLabel.setText("");
            incorrectUsedLettersLabel.setText("");
            howManyMissed = 0;
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/s" + howManyMissed + ".gif")));
            hangmanImage.setImage(image);

            randomizePhrase();

            scoreCounter++;
            scoreCounterLabel.setText("Punkty: " + scoreCounter);
        }
        else if(howManyMissed == 8){
            phraseLabel.setText(phrase[0]);
            endRound();
            scheduler.shutdown();
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

    public void endRound() throws SQLException {

        DbConnection dbc = new DbConnection();
        boolean isHighscoreBeated = dbc.updateSpeedrunScore(playerlogin, scoreCounter);
        dbc.closeConnection();

        if(isHighscoreBeated){
            winLabel.setTextFill(Color.web("#00ae06"));
            Platform.runLater(() -> winLabel.setText("Koniec gry, Pobito rekord!"));
        }
        else{
            winLabel.setTextFill(Color.web("#e1b800"));
            Platform.runLater(() -> winLabel.setText("Koniec gry, Nie udało Ci się pobić rekordu :("));
        }

        winLabel.setVisible(true);
        letterBox.setVisible(false);
        menuReturnButton.setVisible(true);
    }

    @FXML
    public void onHomeImageClick() throws IOException {
        MainMenuController mmc = new MainMenuController(playerlogin);
        mmc.openWindow();
        Stage stage = (Stage) exitImage.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onExitImageClick(){
        Stage stage = (Stage) exitImage.getScene().getWindow();
        stage.close();
        scheduler.shutdown();
        System.exit(0);
    }

    @FXML
    public void onLogoutImageClick() throws IOException {
        LoginController lc = new LoginController();
        lc.reopenWindow();
        Stage stage = (Stage) logoutImage.getScene().getWindow();
        stage.close();
    }

    public String timeConverter(int seconds){
        int min = seconds / 60;
        int sec = seconds % 60;
        return ("0"+ min + ":" + (sec<10 ? ("0"+ sec) : sec));
    }

    public void randomizePhrase() throws SQLException {
        DbConnection dbc = new DbConnection();
        phrase = dbc.getSpeedrunPhrase(usedPhrases);

        phraseId = Integer.parseInt(phrase[2]);
        usedPhrases.add(Integer.parseInt(phrase[2]));
        phraseLabel.setText(hidePhrase(phrase[0]));
        categoryLabel.setText("Kategoria: " + phrase[1]);

        dbc.closeConnection();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // aby w fieldzie byly same wielkie litery
        letterField.textProperty().addListener((ov, oldValue, newValue) -> {
            letterField.setText(newValue.toUpperCase());
        });

        /*** Obsługa działania odliczania czasu w grze ***/
        final Runnable r = new Runnable() {
            int i = 121;
            @Override
            public void run() {
                Platform.runLater(() -> timerLabel.setText("Czas: " + timeConverter(i)));
                i--;
                if(i==0){
                    Platform.runLater(() -> timerLabel.setText("Czas: 00:00"));
                    scheduler.shutdown();
                    try {
                        endRound();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        scheduler.scheduleAtFixedRate(r, 0, 1, TimeUnit.SECONDS);
        /*** ========================================= ***/

        try {
            randomizePhrase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
