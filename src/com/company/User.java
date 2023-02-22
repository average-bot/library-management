package com.company;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class User {

    private String username;

    static final String VIEW_QUERY_CAT = "SELECT libraryitem.Id, category.Id AS category_id, category.CategoryName, libraryitem.Title, libraryitem.Author, libraryitem.Pages, libraryitem.RunTimeMinutes, libraryitem.isBorrowable, libraryitem.Borrower, libraryitem.BorrowDate, libraryitem.ItemType FROM libraryitem LEFT JOIN category ON Category.Id= libraryitem.CategoryId ORDER BY category.CategoryName ASC;";
    static final String VIEW_QUERY_TYPE = "SELECT libraryitem.Id, category.Id AS category_id, category.CategoryName, libraryitem.Title, libraryitem.Author, libraryitem.Pages, libraryitem.RunTimeMinutes, libraryitem.isBorrowable, libraryitem.Borrower, libraryitem.BorrowDate, libraryitem.ItemType FROM libraryitem LEFT JOIN category ON Category.Id= libraryitem.CategoryId ORDER BY libraryitem.ItemType ASC;";
    boolean sortingByCat = false;

    public User(String username) {
        this.username = username;
    }

    public void viewItems(Statement statement) throws SQLException { // TODO: Add acronym
        String query = sortingByCat? VIEW_QUERY_CAT: VIEW_QUERY_TYPE;
        ResultSet resultSet = statement.executeQuery(query);
        String[] columnNames = {"ItemID", "Category", "Title", "Author", "Pages", "Length", "Borrowable", "Borrower", "BorrowDate", "ItemType"};
        while (resultSet.next()) {
            for (int i = 0; i < columnNames.length; i++){
                System.out.print(String.format("-%-16s",columnNames[i]));
            }
            System.out.println("");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            do{
                for (int i = 1; i <= columnsNumber; i++) {
                    System.out.print(String.format("|%-16s",resultSet.getString(i)));
                }
                System.out.println("");
            }while(resultSet.next());
        }
        resultSet.close();
    }

    public void checkOut(Statement statement) throws SQLException{}

    public void checkIn(Statement statement) throws SQLException{}// TODO: check with the username if can return
}
