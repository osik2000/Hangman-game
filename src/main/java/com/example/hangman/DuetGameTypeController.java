package com.example.hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Objects;


public class DuetGameTypeController {

    private static String playerlogin;

    @FXML private ImageView exitImage;
    @FXML private ImageView logoutImage;
    @FXML private ImageView hostGameImage;
    @FXML private ImageView joinGameImage;
    @FXML private HBox hostnameBar;
    @FXML private TextField hostnameField;
    @FXML private Label hostnameErrorLabel;

    public DuetGameTypeController(String login){
        playerlogin = login;
    }

    public DuetGameTypeController() {}

    public void openWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("duetgametype-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 735, 680);
        Stage stage = new Stage();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon.png")));
        stage.getIcons().add(image);
        stage.setResizable(false);
        stage.setTitle("Wisielec - Gra Duet");
        stage.setScene(scene);
        stage.show();
        System.out.println("Wybor trybu duet dla uzytkownika \"" + playerlogin + "\"");
    }

    @FXML
    public void hostDuet() throws IOException {
        System.out.println("Hostuje gre duet. Host: \"" + playerlogin + "\"");
        DuetGameControllerHost dgc = new DuetGameControllerHost(playerlogin);
        dgc.openWindow();
        Stage stage = (Stage) hostGameImage.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void joinDuet() {
        if(!hostnameBar.isVisible()) hostnameBar.setVisible(true);
        else hostnameField.setText("");
    }

    @FXML
    public void joinDuetConnection() throws IOException {
        hostnameErrorLabel.setText("");

        if(hostnameField.getText().length() == 0){
            hostnameErrorLabel.setVisible(true);
            hostnameErrorLabel.setText("Wprowadź nazwę hosta!");
        }
        else{
            System.out.println("Zaczynam gre duet. Dolaczajacy: \"" + playerlogin + "\". Hostname: \"" + hostnameField.getText() + "\"");
            DuetGameControllerClient dgcc = new DuetGameControllerClient(playerlogin, hostnameField.getText());
            dgcc.openWindow();
            Stage stage = (Stage) joinGameImage.getScene().getWindow();
            stage.close();
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
}
