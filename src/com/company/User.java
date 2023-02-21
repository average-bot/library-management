package com.company;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class User {

    static final String VIEW_QUERY_CAT = "SELECT libraryitem.Id, category.Id AS category_id, category.CategoryName, libraryitem.Title, libraryitem.Author, libraryitem.Pages, libraryitem.RunTimeMinutes, libraryitem.isBorrowable, libraryitem.Borrower, libraryitem.BorrowDate, libraryitem.ItemType FROM libraryitem LEFT JOIN category ON Category.Id= libraryitem.CategoryId ORDER BY category.CategoryName ASC;";
    static final String VIEW_QUERY_TYPE = "SELECT libraryitem.Id, category.Id AS category_id, category.CategoryName, libraryitem.Title, libraryitem.Author, libraryitem.Pages, libraryitem.RunTimeMinutes, libraryitem.isBorrowable, libraryitem.Borrower, libraryitem.BorrowDate, libraryitem.ItemType FROM libraryitem LEFT JOIN category ON Category.Id= libraryitem.CategoryId ORDER BY libraryitem.ItemType ASC;";
    boolean sortingByCat = false;

    public void userMenu(Statement statement) throws SQLException {
        System.out.print("Please choose an option:\n" +
                "1. View items \n" +
                "2. Check out a book \n" +
                "3. Check in a book\n" +
                "4. Log out\n" +
                "Enter number here: ");
        switch(new Scanner(System.in).nextInt()) {
            case 1:
                viewItems(statement);
                break;
            case 2:
                checkOut(statement);
                break;
            case 3:
                checkIn(statement);
                break;
            case 4:
                System.out.println("logout");
                break;
            default:
                System.out.println("Invalid option. Try again selecting a number.");
                break;
        }
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

    public void checkIn(Statement statement) throws SQLException{}
}
