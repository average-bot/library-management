package com.company;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

public class Main {
    static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    // If the security was important the different roles would have different users in the db
    static final String USER = "rooter";
    static final String PASS = "rooter";
    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = connection.createStatement();

        new Console(statement);

        connection.close();
        statement.close();
    }
}