package com.example.hangman;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class LoginController {

    @FXML private HBox descriptionBox;
    @FXML private TextArea descriptionField;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    public LoginController() {}

    public void reopenWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HangmanApplication.class.getResource("loginview.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 330);
        Stage stage = new Stage();
        stage.setResizable(false);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon.png")));
        stage.getIcons().add(image);
        stage.setTitle("Wisielec - Logowanie");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onRegisterButtonClick(){

        // sprawdzanie poprawnosci danych
        String errorMessages = "";
        errorLabel.setTextFill(Color.web("#ff0000"));

        if(loginField.getText().length() == 0){ errorMessages += "Wprowadź login!   "; }
        if(passwordField.getText().length() == 0){errorMessages += "Wprowadź hasło!   "; }

        if(!descriptionBox.isVisible()){ descriptionBox.setVisible(true); }
        else {
            if(descriptionField.getText().length() == 0){ errorMessages += "Wprowadź opis!"; }

            // wyswietlanie komunikatu o blednych danych uzytkownikowi
            errorLabel.setText(errorMessages);

            if(errorMessages.length() == 0){

                // łączenie z bazą i dodanie użytkownika
                try {
                    DbConnection dbc = new DbConnection();
                    if (dbc.checkIfUserExists(loginField.getText())) {
                        errorLabel.setText("Login \"" + loginField.getText() + "\" jest zajęty!");
                    } else {
                        dbc.addUser(loginField.getText(), passwordField.getText(), descriptionField.getText());
                        errorLabel.setText("Rejestracja ukończona, możesz się teraz zalogować!");
                        errorLabel.setTextFill(Color.web("#00a316"));
                        descriptionBox.setVisible(false);
                        loginField.setText("");
                        passwordField.setText("");
                        dbc.closeConnection();
                    }
                } catch(SQLException e){
                    e.printStackTrace();
                    errorLabel.setTextFill(Color.web("#ff0000"));
                    errorLabel.setText("Nie udało się połączyć z bazą danych!");
                }
            }
        }
    }

    @FXML
    protected void onLoginButtonClick() throws IOException {

        // sprawdzanie poprawnosci danych
        String errorMessages = "";
        errorLabel.setTextFill(Color.web("#ff0000"));

        if(loginField.getText().length() == 0){ errorMessages += "Wprowadź login!   "; }
        if(passwordField.getText().length() == 0){ errorMessages += "Wprowadź hasło!   "; }

        // wyswietlanie komunikatu o blednych danych uzytkownikowi
        errorLabel.setText(errorMessages);

        if(errorMessages.length() == 0){

            // łączenie z bazą i dodanie użytkownika
            try {
                DbConnection dbc = new DbConnection();
                if(dbc.checkIfUserExists(loginField.getText())){
                    //sprawdzanie czy wprowadzone haslo jest poprawne
                    if(dbc.getUserPassword(loginField.getText()).equals(passwordField.getText())){
                        String currentUser = dbc.getUserLogin(loginField.getText());

                        // panel dla administratora
                        if(currentUser.equalsIgnoreCase("admin")){
                            AdminController ac = new AdminController(currentUser);
                            ac.openWindow();
                        }
                        // menu w przypadku zwyklego uzytkownika
                        else {
                            MainMenuController mmc = new MainMenuController(currentUser);
                            mmc.openWindow();
                        }

                        // zamykanie okna logowania
                        Stage stage = (Stage) errorLabel.getScene().getWindow();
                        stage.close();
                    }
                    else {
                        errorLabel.setTextFill(Color.web("#ff0000"));
                        errorLabel.setText("Bledny login lub haslo!");
                    }
                }
                else{
                    errorLabel.setTextFill(Color.web("#ff0000"));
                    errorLabel.setText("Podany login nie istnieje, zarejestruj się!");
                }
            } catch(SQLException e) {
                e.printStackTrace();
                errorLabel.setTextFill(Color.web("#ff0000"));
                errorLabel.setText("Nie udało się połączyć z bazą danych!");
            }
        }
    }
}