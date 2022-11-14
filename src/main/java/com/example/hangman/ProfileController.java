package com.example.hangman;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;


public class ProfileController implements Initializable {

    private static String playerlogin;

    @FXML private ImageView exitImage;
    @FXML private ImageView logoutImage;

    @FXML private Label nicknameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label normalPointsLabel;
    @FXML private Label speedrunPointsLabel;
    @FXML private Label changeErrorLabel;

    @FXML private HBox changePasswordBox;
    @FXML private HBox changeDescriptionBox;
    @FXML private PasswordField newPasswordField, newPasswordConfirmField;
    @FXML private TextArea newDescriptionTextArea;

    public ProfileController() {}
    public ProfileController(String login){
        playerlogin = login;
    }

    public void openWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 735, 680);
        Stage stage = new Stage();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon.png")));
        stage.getIcons().add(image);
        stage.setResizable(false);
        stage.setTitle("Wisielec - Mój profil");
        stage.setScene(scene);
        stage.show();
        System.out.println("Profil dla uzytkownika \"" + playerlogin + "\"");
    }

    @FXML
    public void onChangePasswordButtonClick(){
        changePasswordBox.setVisible(true);
        changeDescriptionBox.setVisible(false);
        changeErrorLabel.setText("");
        newPasswordField.clear();
        newPasswordConfirmField.clear();
    }

    @FXML
    public void onChangeDescriptionButtonClick(){
        changeDescriptionBox.setVisible(true);
        changePasswordBox.setVisible(false);
        changeErrorLabel.setText("");
        newDescriptionTextArea.clear();
    }

    @FXML
    public void changePassword() throws SQLException {
        changeErrorLabel.setText("");
        changeErrorLabel.setTextFill(Color.web("#ab0303"));
        if(newPasswordField.getText().length() == 0){
            changeErrorLabel.setText("Hasło nie może być puste!");
        }
        else {
            if(!newPasswordField.getText().equals(newPasswordConfirmField.getText())){
                changeErrorLabel.setText("Hasła muszą się zgadzać!");
            }
            else{
                DbConnection dbc = new DbConnection();
                dbc.changeUserPassword(playerlogin,newPasswordField.getText());
                changeErrorLabel.setTextFill(Color.web("#007506"));
                changeErrorLabel.setText("Zmieniono hasło!");
                changePasswordBox.setVisible(false);
            }
        }
    }

    @FXML
    public void changeDescription() throws SQLException {
        changeErrorLabel.setText("");
        changeErrorLabel.setTextFill(Color.web("#ab0303"));
        if(newDescriptionTextArea.getText().length() == 0){
            changeErrorLabel.setText("Opis nie może być pusty!");
        }
        else {
            if(newDescriptionTextArea.getText().length() > 420){
                changeErrorLabel.setText("Opis jest za długi!");
            }
            else {
                DbConnection dbc = new DbConnection();
                dbc.changeUserDescription(playerlogin, newDescriptionTextArea.getText());
                descriptionLabel.setText(newDescriptionTextArea.getText());
                changeErrorLabel.setTextFill(Color.web("#007506"));
                changeErrorLabel.setText("Zmieniono opis!");
                changeDescriptionBox.setVisible(false);
            }
        }
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
        try {
            DbConnection dbc = new DbConnection();
            nicknameLabel.setText(playerlogin);
            descriptionLabel.setText(dbc.getUserDescription(playerlogin));
            normalPointsLabel.setText(Integer.toString(dbc.countNormalPoints(playerlogin)));
            speedrunPointsLabel.setText(Integer.toString(dbc.getSpeedrunHighScore(playerlogin)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
