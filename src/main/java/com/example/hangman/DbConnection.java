package com.example.hangman;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DbConnection {
    Connection c;
    Statement stmt;
    ResultSet rset;

    public DbConnection() throws SQLException {
        c = null;
        rset = null;
        c = DriverManager
            .getConnection("jdbc:mysql://remotemysql.com/aeCwkdZjzZ", "aeCwkdZjzZ", "jaCfRCsMNb");
        stmt = c.createStatement();
    }

    public boolean checkIfUserExists(String login) throws SQLException {
        rset = stmt.executeQuery("SELECT COUNT(loginUzytk) FROM Uzytkownicy WHERE loginUzytk = '" + login + "';");
        rset.next();
        return (rset.getInt(1) != 0);
    }

    public String getUserPassword(String login) throws SQLException {
        rset = stmt.executeQuery("SELECT hasloUzytk FROM Uzytkownicy WHERE loginUzytk = '" + login + "';");
        rset.next();
        return rset.getString(1);
    }

    // otrzymanie struktury loginu z wyróżnionymi małymi oraz wielkimi znakami
    public String getUserLogin(String login) throws SQLException {
        rset = stmt.executeQuery("SELECT loginUzytk FROM Uzytkownicy WHERE loginUzytk = '" + login + "';");
        rset.next();
        return rset.getString(1);
    }

    public void addUser(String login, String password, String description) throws SQLException {
        stmt.executeUpdate("INSERT INTO Uzytkownicy(loginUzytk, hasloUzytk, opisUzytk) VALUES ('" +
                login +"','" +
                password + "','" +
                description + "');");

        // zerowanie najwyższej punktacji trybu speedrun nowego uzytkownika
        stmt.executeUpdate("INSERT INTO Ranking(loginUzytk, liczbaPkt) VALUES ('" + login + "', 0);");
    }

    public void changeUserPassword(String login, String password) throws SQLException {
        stmt.executeUpdate("UPDATE Uzytkownicy SET hasloUzytk='" + password + "' WHERE loginUzytk='" + login + "';");
    }

    public void changeUserDescription(String login, String description) throws SQLException {
        stmt.executeUpdate("UPDATE Uzytkownicy SET opisUzytk='" + description + "' WHERE loginUzytk='" + login + "';");
    }

    public void changeUserSpeedrunScore(String login, int score) throws SQLException {
        stmt.executeUpdate("UPDATE Ranking SET liczbaPkt=" + score + " WHERE loginUzytk='" + login + "';");
    }

    public void changePhraseTitle(int id, String title) throws SQLException {
        stmt.executeUpdate("UPDATE Hasla SET trescHasla='" + title + "' WHERE idHasla=" + id + ";");
    }

    public void changePhraseCategory(int id, String category) throws SQLException {
        stmt.executeUpdate("UPDATE Hasla SET kategoriaHasla='" + category + "' WHERE idHasla=" + id + ";");
    }

    public void addPhrase(String title, String category) throws SQLException {
        stmt.executeUpdate("INSERT INTO Hasla(trescHasla, kategoriaHasla) VALUES ('" + title + "', '" + category + "');");
    }

    public void removePhrase(int id) throws SQLException {
        stmt.executeUpdate("DELETE FROM RozwiazaneHasla WHERE idHasla=" + id + ";");
        stmt.executeUpdate("DELETE FROM Hasla WHERE idHasla=" + id + ";");
    }

    public void removeUser(String login) throws SQLException {
        stmt.executeUpdate("DELETE FROM Ranking WHERE loginUzytk='" + login + "';");
        stmt.executeUpdate("DELETE FROM RozwiazaneHasla WHERE loginUzytk='" + login + "';");
        stmt.executeUpdate("DELETE FROM Uzytkownicy WHERE loginUzytk='" + login + "';");
    }

    public String getUserDescription(String login) throws SQLException {
        rset = stmt.executeQuery("SELECT opisUzytk FROM Uzytkownicy WHERE loginUzytk = '" + login + "';");
        rset.next();
        return rset.getString(1);
    }

    public int countNormalPoints(String login) throws SQLException {
        rset = stmt.executeQuery("SELECT COUNT(idHasla) FROM RozwiazaneHasla WHERE loginUzytk = '" + login + "';");
        rset.next();
        return rset.getInt(1);
    }

    public int getSpeedrunHighScore(String login) throws SQLException {
        rset = stmt.executeQuery("SELECT liczbaPkt FROM Ranking WHERE loginUzytk = '" + login + "';");
        rset.next();
        return rset.getInt(1);
    }

    public String[] getTopNormalScore() throws SQLException {
        String[] score = new String[5];

        rset = stmt.executeQuery("SELECT loginUzytk, COUNT(idHasla) AS pkt FROM RozwiazaneHasla GROUP BY loginUzytk ORDER BY pkt DESC LIMIT 5;");

        //zliczanie wyniku według argumentu (n-te miejsce)
        for(int i=0; i<5; i++){
            rset.next();
            score[i] = rset.getString(1) + " (" + rset.getString(2) + ")";
        }
        return score;
    }

    public String[] getTopSpeedrunScore() throws SQLException {
        String[] score = new String[5];
        rset = stmt.executeQuery("SELECT loginUzytk, liczbaPkt FROM Ranking ORDER BY liczbaPkt DESC LIMIT 5;");

        //zliczanie wyniku według argumentu (n-te miejsce)
        for(int i=0; i<5; i++){
            rset.next();
            score[i] = rset.getString(1) + " (" + rset.getString(2) + ")";
        }
        return score;
    }

    public ObservableList<User> getUsersList() throws SQLException {
        ObservableList<User> output = FXCollections.observableArrayList();

        // zapytanie zwracające dane potrzebne do uzupełnienia tabeli
        rset = stmt.executeQuery("SELECT loginUzytk as mainLogin, hasloUzytk, opisUzytk, (SELECT COUNT(idHasla) FROM RozwiazaneHasla WHERE loginUzytk = mainLogin), (SELECT liczbaPkt FROM Ranking Where loginUzytk = mainLogin) FROM Uzytkownicy WHERE loginUzytk NOT LIKE 'admin';");
        while(rset.next()){
            output.add(new User(rset.getString(1),rset.getString(2),rset.getString(3),rset.getInt(4),rset.getInt(5)));
        }
        return output;
    }

    public ObservableList<Phrase> getPhrasesList() throws SQLException {
        ObservableList<Phrase> output = FXCollections.observableArrayList();

        // zapytanie zwracające dane potrzebne do uzupełnienia tabeli
        rset = stmt.executeQuery("SELECT idHasla, trescHasla, kategoriaHasla FROM Hasla;");
        while(rset.next()){
            output.add(new Phrase(rset.getInt(1),rset.getString(2),rset.getString(3)));
        }
        return output;
    }

    public String[] getNormalPhrase(String login) throws SQLException {

        String[] res = new String[3];   // res[0] - tresc, res[1] - kategoria, res[2] - id
        List<Integer> unusedPhrases = new ArrayList<>();

        rset = stmt.executeQuery("SELECT idHasla FROM Hasla WHERE idHasla NOT IN (SELECT idHasla FROM RozwiazaneHasla WHERE loginUzytk='"+ login +"');");
        while(rset.next()){
            unusedPhrases.add(rset.getInt(1));
        }

        if(unusedPhrases.size()>0){
            Random r = new Random();
            int id = r.nextInt(0, unusedPhrases.size()); // losowanie nierozwiązanego hasła dla użytkownika
            rset = stmt.executeQuery("SELECT trescHasla, kategoriaHasla FROM Hasla WHERE idHasla=" + unusedPhrases.get(id) + ";");
            rset.next();
            res[0] = rset.getString(1).toUpperCase();
            res[1] = rset.getString(2).toUpperCase();
            res[2] = Integer.toString(unusedPhrases.get(id));
        }
        else {
            res[0] = "";
            res[1] = "";
            res[2] = "";
        }
        return res;
    }

    public String[] getDuetPhrase() throws SQLException {

        String[] res = new String[3];   // res[0] - tresc, res[1] - kategoria, res[2] - idHasla
        List<Integer> phrases = new ArrayList<>();

        rset = stmt.executeQuery("SELECT idHasla FROM Hasla WHERE idHasla;");
        while(rset.next()){
            phrases.add(rset.getInt(1));
        }

        if(phrases.size()>0){
            Random r = new Random();
            int id = r.nextInt(0, phrases.size()); // losowanie nierozwiązanego hasła dla użytkownika
            rset = stmt.executeQuery("SELECT trescHasla, kategoriaHasla FROM Hasla WHERE idHasla="+ phrases.get(id) +";");
            rset.next();
            res[0] = rset.getString(1).toUpperCase();
            res[1] = rset.getString(2).toUpperCase();
            res[2] = Integer.toString(phrases.get(id));
        }
        else {
            res[0] = "";
            res[1] = "";
            res[2] = "";
        }
        return res;
    }

    public String[] getSpeedrunPhrase(List<Integer> usedPhrases) throws SQLException {

        String[] res = new String[3];   // res[0] - tresc, res[1] - kategoria
        List<Integer> phrases = new ArrayList<>();
        List<Integer> unusedPhrases = new ArrayList<>();

        rset = stmt.executeQuery("SELECT idHasla FROM Hasla WHERE idHasla;");
        while(rset.next()){
            phrases.add(rset.getInt(1));
        }

        for (Integer id: phrases) {
            if(!usedPhrases.contains(id)){
                unusedPhrases.add(id);
            }
        }

        if(phrases.size()>0){
            Random r = new Random();
            int id = r.nextInt(0, unusedPhrases.size()); // losowanie nierozwiązanego hasła dla użytkownika
            rset = stmt.executeQuery("SELECT trescHasla, kategoriaHasla FROM Hasla WHERE idHasla="+ unusedPhrases.get(id) +";");
            rset.next();
            res[0] = rset.getString(1).toUpperCase();
            res[1] = rset.getString(2).toUpperCase();
            res[2] = Integer.toString(unusedPhrases.get(id));
        }
        else {
            res[0] = "";
            res[1] = "";
            res[2] = "";
        }
        return res;
    }

    public void addNormalPoint(String login, int id) throws SQLException {
        stmt.executeUpdate("INSERT INTO RozwiazaneHasla(loginUzytk, idHasla) VALUES ('" + login + "',"+id+");");
    }

    public boolean updateSpeedrunScore(String login, int score) throws SQLException {
        rset = stmt.executeQuery("SELECT liczbaPkt FROM Ranking WHERE loginUzytk='"+ login +"';");
        rset.next();
        int oldScore = rset.getInt(1);

        if (score > oldScore){
            stmt.executeUpdate("UPDATE Ranking SET liczbaPkt=" + score + " WHERE loginUzytk='" + login + "';");
            return true;
        }
        else return false;
    }

    public void closeConnection() throws SQLException {
        stmt.close();
        c.close();
    }
}