package com.example.hangman;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    private static String adminLogin;

    public AdminController(String login){
        adminLogin = login;
    }

    public AdminController() {}

    @FXML private ImageView exitImage;
    @FXML private ImageView logoutImage;

    @FXML private Button adminAddUserConfirmButton;
    @FXML private Button adminAddPhraseConfirmButton;

    @FXML private ToggleButton usersToggleButton;
    @FXML private ToggleButton phrasesToggleButton;

    @FXML private HBox addUserHBox;
    @FXML private HBox addPhraseHBox;

    @FXML private TextField newLoginField;
    @FXML private TextField newPasswordField;
    @FXML private TextArea newDescriptionField;
    @FXML private TextField newTitleField;
    @FXML private TextField newCategoryField;
    @FXML private TableColumn passwordColumn;
    @FXML private TableColumn descriptionColumn;
    @FXML private TableColumn speedrunScoreColumn;
    @FXML private TableColumn titleColumn;
    @FXML private TableColumn categoryColumn;

    @FXML private TableView<User> usersTable;
    @FXML private TableView<Phrase> phrasesTable;

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ObservableList<Phrase> phraseList = FXCollections.observableArrayList();

    public void openWindow() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 735, 680);
        Stage stage = new Stage();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon.png")));
        stage.getIcons().add(image);
        stage.setResizable(false);
        stage.setTitle("Wisielec - Panel Administratora");
        stage.setScene(scene);
        stage.show();
        System.out.println("Panel administratora - login: \"" + adminLogin + "\"");
    }

    public void showUsersTable(){
        phrasesTable.setVisible(false);
        usersTable.setVisible(true);
        addUserHBox.setVisible(false);
        addPhraseHBox.setVisible(false);
    }

    public void showPhrasesTable(){
        usersTable.setVisible(false);
        phrasesTable.setVisible(true);
        addUserHBox.setVisible(false);
        addPhraseHBox.setVisible(false);
    }

    @FXML
    public void editPassword(TableColumn.CellEditEvent cellEditEvent) throws SQLException {
        int index = usersTable.getSelectionModel().getSelectedIndex();
        userList.get(index).setPassword((String) cellEditEvent.getNewValue());

        String login = userList.get(index).getLogin();
        DbConnection dbc = new DbConnection();
        dbc.changeUserPassword(login,userList.get(index).getPassword());

    }

    @FXML
    public void editDescription(TableColumn.CellEditEvent cellEditEvent) throws SQLException {
        int index = usersTable.getSelectionModel().getSelectedIndex();
        userList.get(index).setDescription((String) cellEditEvent.getNewValue());

        String login = userList.get(index).getLogin();
        DbConnection dbc = new DbConnection();
        dbc.changeUserDescription(login,userList.get(index).getDescription());
    }

    @FXML
    public void editSpeedrunScore(TableColumn.CellEditEvent cellEditEvent) throws SQLException {
        int index = usersTable.getSelectionModel().getSelectedIndex();
        userList.get(index).setSpeedrunScore((Integer) cellEditEvent.getNewValue());

        String login = userList.get(index).getLogin();

        DbConnection dbc = new DbConnection();
        dbc.changeUserSpeedrunScore(login,userList.get(index).getSpeedrunScore());
    }

    @FXML
    public void editTitle(TableColumn.CellEditEvent cellEditEvent) throws SQLException {
        int index = phrasesTable.getSelectionModel().getSelectedIndex();
        phraseList.get(index).setTitle((String) cellEditEvent.getNewValue());

        int id = phraseList.get(index).getId();
        DbConnection dbc = new DbConnection();
        dbc.changePhraseTitle(id,phraseList.get(index).getTitle());
    }

    @FXML
    public void editCategory(TableColumn.CellEditEvent cellEditEvent) throws SQLException {
        int index = phrasesTable.getSelectionModel().getSelectedIndex();
        phraseList.get(index).setCategory((String) cellEditEvent.getNewValue());

        int id = phraseList.get(index).getId();

        DbConnection dbc = new DbConnection();
        dbc.changePhraseCategory(id,phraseList.get(index).getCategory());
    }

    @FXML
    public void refreshTables() throws SQLException {

        addPhraseHBox.setVisible(false);
        addUserHBox.setVisible(false);

        DbConnection dbc = new DbConnection();
        userList = dbc.getUsersList();
        usersTable.setItems(userList);
        phraseList = dbc.getPhrasesList();
        phrasesTable.setItems(phraseList);
    }

    @FXML
    public void adminAddUser() throws SQLException {
        // wprowadzenie danych do bazy
        //blokada wprowadzenia danych z pustym rekordem
        if(newLoginField.getText().length() != 0 && newPasswordField.getText().length() != 0 && newDescriptionField.getText().length() != 0){
            DbConnection dbc = new DbConnection();
            dbc.addUser(newLoginField.getText(),newPasswordField.getText(),newDescriptionField.getText());

            userList = dbc.getUsersList();
            usersTable.setItems(userList);

            dbc.closeConnection();
            newLoginField.setText("");
            newPasswordField.setText("");
            newDescriptionField.setText("");
        }
    }

    @FXML
    public void adminAddPhrase() throws SQLException {
        // wprowadzenie danych do bazy
        //blokada wprowadzenia danych z pustym rekordem
        if(newTitleField.getText().length() != 0 && newCategoryField.getText().length() != 0){
            DbConnection dbc = new DbConnection();
            dbc.addPhrase(newTitleField.getText(),newCategoryField.getText());

            phraseList = dbc.getPhrasesList();
            phrasesTable.setItems(phraseList);

            dbc.closeConnection();
            newTitleField.setText("");
            newCategoryField.setText("");
        }
    }

    @FXML
    public void addRecord() {
        //dodanie użytkownika
        if(usersToggleButton.isSelected()){
            if(!addUserHBox.isVisible()){
                addUserHBox.setVisible(true);
                addPhraseHBox.setVisible(false);
            }
            else {
                newLoginField.setText("");
                newPasswordField.setText("");
                newDescriptionField.setText("");
            }
        }
        //dodanie hasła do gry
        else{
           if(!addPhraseHBox.isVisible()){
               addPhraseHBox.setVisible(true);
               addUserHBox.setVisible(false);
           }
           else {
               newTitleField.setText("");
               newCategoryField.setText("");
           }
        }
    }

    @FXML
    public void removeRecord() throws SQLException {
        //usunięcie hasła do gry
        if(phrasesToggleButton.isSelected()){
            int index = phrasesTable.getSelectionModel().getSelectedIndex();
            int id = phraseList.get(index).getId();

            DbConnection dbc = new DbConnection();
            dbc.removePhrase(id);

            phraseList = dbc.getPhrasesList();
            phrasesTable.setItems(phraseList);

            dbc.closeConnection();
        }
        // usuniecie uzytkownika
        else {
            int index = usersTable.getSelectionModel().getSelectedIndex();
            String login = userList.get(index).getLogin();

            DbConnection dbc = new DbConnection();
            dbc.removeUser(login);

            userList = dbc.getUsersList();
            usersTable.setItems(userList);

            dbc.closeConnection();

        }

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

        usersTable.setEditable(true);
        phrasesTable.setEditable(true);

        // mozliwość edycji poszczególnych rekordów
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        speedrunScoreColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        categoryColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        try {
            DbConnection dbc = new DbConnection();
            userList = dbc.getUsersList();
            usersTable.setItems(userList);
            phraseList = dbc.getPhrasesList();
            phrasesTable.setItems(phraseList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Automatyczne wypelnianie danych pobranych z bazy dla administratora: \"" + adminLogin + "\"");
    }
}
