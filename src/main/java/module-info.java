module com.example.hangman {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;


    opens com.example.hangman to javafx.fxml;
    exports com.example.hangman;
}