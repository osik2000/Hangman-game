package com.example.hangman;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.fxml.FXML;


public class MainMenuController implements Initializable {

    private static String playerlogin; // zmienna przechowująca nick gracza logującego się do gry
    private static boolean userLabelAntiBug = false; // okienko info wykorzystuje ten sam controller

    @FXML private Label userLabel;
    @FXML private ImageView exitImage;
    @FXML private ImageView logoutImage;
    @FXML private ImageView soloGameImage;

    private Stage infoStage = new Stage(); // do zamknięcia poprzedniego okienka 'info' przy ponownej próbie jego otwarcia

    public MainMenuController() {}

    public MainMenuController(String login){
        playerlogin = login;
    }

    public void openWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainmenu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 735, 680);
        Stage stage = new Stage();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon.png")));
        stage.getIcons().add(image);
        stage.setResizable(false);
        stage.setTitle("Wisielec - Menu Główne");
        stage.setScene(scene);
        stage.show();
        System.out.println("Menu glowne (zalogowany: \"" + playerlogin + "\")");
    }

    @FXML
    public void soloGameStart() throws IOException {
        System.out.println("zaczynam nowa gre solo!");
        SoloGameStartController sgsc = new SoloGameStartController(playerlogin);
        sgsc.openWindow();
        Stage stage = (Stage) soloGameImage.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void duetGameStart() throws IOException {
        System.out.println("zaczynam nowa gre duet!");
        DuetGameTypeController dgtc = new DuetGameTypeController(playerlogin);
        dgtc.openWindow();
        Stage stage = (Stage) soloGameImage.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void rankView() throws IOException {
        System.out.println("wyswietlam ranking!");
        RankController rnc = new RankController(playerlogin);
        rnc.openWindow();
        Stage stage = (Stage) soloGameImage.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void profileView() throws IOException {
        System.out.println("wyswietlam profil gracza " + playerlogin + "!");
        ProfileController pc = new ProfileController(playerlogin);
        pc.openWindow();
        Stage stage = (Stage) soloGameImage.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onInfoImageClick() throws IOException {
        if(infoStage.isShowing()) infoStage.close();
        userLabelAntiBug = true;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("info-view.fxml"));
        Scene infoScene = new Scene(fxmlLoader.load(), 600, 340);
        infoStage = new Stage();
        infoStage.setResizable(false);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon.png")));
        infoStage.getIcons().add(image);
        infoStage.setTitle("Wisielec - Twórcy");
        infoStage.setScene(infoScene);
        infoStage.show();
        userLabelAntiBug = false;
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
        // przy wyświetlaniu menu pokazuje się informacja o zalogowanym użytkowniku
        // następnie może zostać wyświetlone okno info-view z tego samego kontrolera
        // infoview używa więc tej funkcji przy starcie stąd zabezpieczenie
        if(!userLabelAntiBug) userLabel.setText("Witaj, " + playerlogin + "!");
    }
}
