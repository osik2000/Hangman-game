package com.example.hangman;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {

    private SimpleStringProperty login = new SimpleStringProperty("");
    private SimpleStringProperty password = new SimpleStringProperty("");
    private SimpleStringProperty description = new SimpleStringProperty("");
    private SimpleIntegerProperty speedrunScore = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty normalScore = new SimpleIntegerProperty(0);

    public User() {}

    public User(String login, String password, String description, int normalScore, int speedrunScore) {
        this.login.set(login);
        this.password.set(password);
        this.description.set(description);
        this.speedrunScore.set(speedrunScore);
        this.normalScore.set(normalScore);
    }

    public String getLogin() {
        return login.get();
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public int getSpeedrunScore() {
        return speedrunScore.get();
    }

    public SimpleIntegerProperty speedrunScoreProperty() {
        return speedrunScore;
    }

    public void setSpeedrunScore(int speedrunScore) {
        this.speedrunScore.set(speedrunScore);
    }

    public int getNormalScore() {
        return normalScore.get();
    }

    public SimpleIntegerProperty normalScoreProperty() {
        return normalScore;
    }

    public void setNormalScore(int normalScore) {
        this.normalScore.set(normalScore);
    }
}
