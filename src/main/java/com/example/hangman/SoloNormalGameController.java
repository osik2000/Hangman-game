package com.example.hangman;

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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class SoloNormalGameController implements Initializable {

    private static String playerlogin;
    private String[] phrase;
    private int phraseId;
    private List<Character> usedLetters = new ArrayList<>();
    private int howManyMissed = 0;

    @FXML private ImageView exitImage, logoutImage, hangmanImage;
    @FXML private Label phraseLabel, categoryLabel, errorLabel, correctLettersLabel , incorrectUsedLettersLabel, winLabel;
    @FXML private TextField letterField;
    @FXML private AnchorPane letterBox;
    @FXML private Button menuReturnButton;

    public SoloNormalGameController() {}

    public SoloNormalGameController(String login){
        playerlogin = login;
    }

    public void openWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HangmanApplication.class.getResource("solonormalgame-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 735, 500);
        Stage stage = new Stage();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon.png")));
        stage.getIcons().add(image);
        stage.setResizable(false);
        stage.setTitle("Wisielec - Normalna gra solo");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void checkLetter() throws SQLException {
        errorLabel.setText("");
        if(letterField.getText().length() == 0){ errorLabel.setText("Wpisz literę!"); }
        else if(letterField.getText().length()>1){ errorLabel.setText("Za dużo liter!"); }
        else {
            char letter = letterField.getText().charAt(0);

            if(!Character.isLetter(letter)){ errorLabel.setText("Wpisz literę!"); }
            else{
                boolean isUnique = true;
                for (char c : usedLetters) { if(c == letter) isUnique = false; }
                if(!isUnique) { errorLabel.setText("Już użyta!"); }
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

        if(!phraseLabel.getText().contains("-")){ // jeżeli nie ma niezgadniętych liter
            DbConnection dbc = new DbConnection();
            dbc.addNormalPoint(playerlogin, phraseId);
            dbc.closeConnection();

            winLabel.setText("Rozwiązano hasło! Zdobywasz 1 punkt!");
            winLabel.setVisible(true);
            letterBox.setVisible(false);
            menuReturnButton.setVisible(true);
        }
        else if(howManyMissed == 8){
            winLabel.setTextFill(Color.web("#ff0000"));
            winLabel.setText("Przegrana!\nKoniec gry :(");
            winLabel.setVisible(true);
            letterBox.setVisible(false);
            menuReturnButton.setVisible(true);
            phraseLabel.setText(phrase[0]);
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
    }

    @FXML
    public void onLogoutImageClick() throws IOException {
        LoginController lc = new LoginController();
        lc.reopenWindow();
        Stage stage = (Stage) logoutImage.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // aby w fieldzie byly same wielkie litery
        letterField.textProperty().addListener((ov, oldValue, newValue) -> {
            letterField.setText(newValue.toUpperCase());
        });

        try {
            DbConnection dbc = new DbConnection();
            phrase = dbc.getNormalPhrase(playerlogin);
            if(phrase[1].equals("")){ // przypadek kiedy wszystkie hasła zostały już rozwiązane
                phraseLabel.setText("Rozwiązano już wszystkie hasła!!!\nWróć do menu i sprawdź się w trybie Wyścig z Czasem");
                categoryLabel.setText("");
                hangmanImage.setVisible(false);
                letterBox.setVisible(false);
                menuReturnButton.setVisible(true);
            }
            else{ // przypadek kiedy istnieją nierozwiązane jeszcze hasła
                phraseId = Integer.parseInt(phrase[2]);
                phraseLabel.setText(hidePhrase(phrase[0]));
                categoryLabel.setText("Kategoria: " + phrase[1]);
            }

            dbc.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
