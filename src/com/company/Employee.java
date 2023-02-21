package com.company;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Employee {
    static final String VIEW_QUERY_CAT = "SELECT libraryitem.Id, category.Id AS category_id, category.CategoryName, libraryitem.Title, libraryitem.Author, libraryitem.Pages, libraryitem.RunTimeMinutes, libraryitem.isBorrowable, libraryitem.Borrower, libraryitem.BorrowDate, libraryitem.ItemType FROM libraryitem LEFT JOIN category ON Category.Id= libraryitem.CategoryId ORDER BY category.CategoryName ASC;";
    static final String VIEW_QUERY_TYPE = "SELECT libraryitem.Id, category.Id AS category_id, category.CategoryName, libraryitem.Title, libraryitem.Author, libraryitem.Pages, libraryitem.RunTimeMinutes, libraryitem.isBorrowable, libraryitem.Borrower, libraryitem.BorrowDate, libraryitem.ItemType FROM libraryitem LEFT JOIN category ON Category.Id= libraryitem.CategoryId ORDER BY libraryitem.ItemType ASC;";
    boolean sortingByCat = false;

    public void adminMenu(Statement statement) throws SQLException {
        System.out.print("Please choose an option:\n" +
                "1. View items \n" +
                "2. Create item \n" +
                "3. Edit item \n" +
                "4. Delete item \n" +
                "-------------- \n" +
                "5. Create category \n" +
                "6. Edit category \n" +
                "7. Delete category \n" +
                "-------------- \n" +
                "8. Log out\n" +
                "Enter number here: ");
        switch(new Scanner(System.in).nextInt()) {
            case 1:
                viewItems(statement);
                break;
            case 2:
                createItem(statement);
                break;
            case 3:
                editItem(statement);
                break;
            case 4:
                deleteItem(statement);
                break;
            case 5:
                createCategory(statement);
                break;
            case 6:
                editCategory(statement);
                break;
            case 7:
                deleteCategory(statement);
                break;
            case 8:
                break; // TODO: LOGOUT
            default:
                System.out.println("Invalid option. Try again selecting a number.");
                break;
        }
    }

    private void viewItems(Statement statement) throws SQLException { // TODO: Add acronym
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

    private void deleteCategory(Statement statement) {
        // TODO: Check if the cat is not connected to anything + get the delete id from user + check if it exists
        int id = 3;
        String DELETE_CAT_QUERY = "DELETE FROM category WHERE Id="+id+";";
    }

    private void editCategory(Statement statement) { //TODO: Check if categoryName exists already + ask user for id and new name
        int id = 3;
        String changedName = "\"yolo\"" ;
        String EDIT_CAT_QUERY = "UPDATE category SET CategoryName = "+ changedName +" WHERE id = "+ id +";";
    }

    private void createCategory(Statement statement) { //TODO: Check if categoryName exists already + ask user for new categorys name
        String newCat = "family";
        String CREATE_CAR_QUERY = "INSERT INTO category (CategoryName) VALUES (\""+ newCat +"\");";
    }

    private void deleteItem(Statement statement) {
        // TODO: Get the delete id from user + check if it exists
        int id = 3;
        String DELETE_ITEM_QUERY = "DELETE FROM libraryitem WHERE Id="+id+";";
    }

    private void editItem(Statement statement) {
        //TODO: Ask user for new info
        String EDIT_ITEM_QUERY = "";

    }
    private void createItem(Statement statement) {
        //TODO: Ask user for new items info
        String CREATE_ITEM_QUERY = "";
    }
}
