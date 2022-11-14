package com.example.hangman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class HangmanApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HangmanApplication.class.getResource("loginview.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 330);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon.png")));
        stage.getIcons().add(image);
        stage.setResizable(false);
        stage.setTitle("Wisielec - Logowanie");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}