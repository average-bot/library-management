package com.company;

import java.sql.*;

public class Main {
    static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    static final String USER = "rooter";
    static final String PASS = "rooter";
    static final String QUERY = "SELECT * FROM category";

    public static void main(String[] args) {
        try{
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY);
            while (resultSet.next()) { // Retrieve by column name
                System.out.print("Id: " + resultSet.getInt("Id"));
                System.out.print(", Category: " + resultSet.getString("CategoryName"));
            }
            connection.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}