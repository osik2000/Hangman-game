package com.example.hangman;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;


public class RankController implements Initializable {

    private static String playerlogin;

    @FXML private ImageView exitImage;
    @FXML private ImageView logoutImage;
    @FXML private ImageView homeImage;

    @FXML private Label firstNickname;
    @FXML private Label secondNickname;
    @FXML private Label thirdNickname;
    @FXML private Label fourthNickname;
    @FXML private Label fifthNickname;
    @FXML private Label descriptionLabel;

    public RankController() {}
    public RankController(String login){
        playerlogin = login;
    }

    public void openWindow() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rank-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 735, 680);
        Stage stage = new Stage();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon.png")));
        stage.getIcons().add(image);
        stage.setResizable(false);
        stage.setTitle("Wisielec - Ranking");
        stage.setScene(scene);
        stage.show();
        System.out.println("Ranking (zalogowany: \"" + playerlogin + "\")");
    }

    @FXML
    public void onHomeImageClick() throws IOException {
        MainMenuController mmc = new MainMenuController(playerlogin);
        mmc.openWindow();
        Stage stage = (Stage) homeImage.getScene().getWindow();
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


    public void openNormalRanking() throws SQLException {
        DbConnection dbc = new DbConnection();
        String[] scores;
        scores = dbc.getTopNormalScore();
        firstNickname.setText(scores[0]);
        secondNickname.setText(scores[1]);
        thirdNickname.setText(scores[2]);
        fourthNickname.setText(scores[3]);
        fifthNickname.setText(scores[4]);

        dbc.closeConnection();
    }

    public void openSpeedrunRanking() throws SQLException {
        DbConnection dbc = new DbConnection();
        String[] scores;
        scores = dbc.getTopSpeedrunScore();
        firstNickname.setText(scores[0]);
        secondNickname.setText(scores[1]);
        thirdNickname.setText(scores[2]);
        fourthNickname.setText(scores[3]);
        fifthNickname.setText(scores[4]);

        dbc.closeConnection();
    }

    public void showNormalDescription(){
        descriptionLabel.setText("Top 5 graczy z rozwiązaną największą liczbą haseł w trybie normalnym");
    }

    public void showSpeedrunDescription(){
        descriptionLabel.setText("Top 5 graczy z najwyższym wynikiem uzyskanym w trybie szybkim");
    }

    public void hideDescription(){
        descriptionLabel.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            openNormalRanking();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
