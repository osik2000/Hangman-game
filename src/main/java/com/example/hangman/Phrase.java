package com.example.hangman;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Phrase {

    private final SimpleIntegerProperty id = new SimpleIntegerProperty(0);
    private final SimpleStringProperty title = new SimpleStringProperty("");
    private final SimpleStringProperty category = new SimpleStringProperty("");

    public Phrase(int id, String title, String category) {
        this.id.set(id);
        this.title.set(title);
        this.category.set(category);
    }

    public Phrase() {}

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getCategory() {
        return category.get();
    }
    public void setCategory(String category) {
        this.category.set(category);
    }
}
