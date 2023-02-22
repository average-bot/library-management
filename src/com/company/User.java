package com.company;

import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class User {

    private String username;

    static final String VIEW_QUERY_CAT = "SELECT libraryitem.Id, category.Id AS category_id, category.CategoryName, libraryitem.Title, libraryitem.Author, libraryitem.Pages, libraryitem.RunTimeMinutes, libraryitem.isBorrowable, libraryitem.Borrower, libraryitem.BorrowDate, libraryitem.ItemType FROM libraryitem LEFT JOIN category ON Category.Id= libraryitem.CategoryId ORDER BY category.CategoryName ASC;";
    static final String VIEW_QUERY_TYPE = "SELECT libraryitem.Id, category.Id AS category_id, category.CategoryName, libraryitem.Title, libraryitem.Author, libraryitem.Pages, libraryitem.RunTimeMinutes, libraryitem.isBorrowable, libraryitem.Borrower, libraryitem.BorrowDate, libraryitem.ItemType FROM libraryitem LEFT JOIN category ON Category.Id= libraryitem.CategoryId ORDER BY libraryitem.ItemType ASC;";
    boolean sortingByCat = false;

    public User(){
        setUsername();
    }

    public void setUsername() {
        System.out.print("Please enter your username: ");
        String username;
        do {
            username = new Scanner(System.in).next(); // No security
        }while(username.equals("")); // TODO: other reqs for username
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

    public void checkOut(Statement statement) throws SQLException {
        // Get Date&Time and convert to correct format for the db
        java.util.Calendar cal = Calendar.getInstance();
        java.util.Date utilDate = new java.util.Date();
        cal.setTime(utilDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());

        System.out.print("Please enter the items id you want to borrow: ");
        int id = new Scanner(System.in).nextInt(); // No security

        // Check if the item is available or borrowed to someone
        String CHECKOUT_CHECK_QUERY = "SELECT * FROM libraryitem WHERE id="+ id +" AND Borrower IS NULL AND isBorrowable=1;";
        boolean resultSetExists = statement.execute(CHECKOUT_CHECK_QUERY);

        // If available run the query for borrowing
        String CHECKOUT_QUERY= "UPDATE libraryitem SET Borrower = '" + username +"', BorrowDate = '"+ sqlDate +"'WHERE id="+ id +" AND isBorrowable=1 AND Borrower IS NULL;";
        if (resultSetExists){
            statement.execute(CHECKOUT_QUERY);
            System.out.println("Borrow success!");
        } else System.out.println("Error on borrowing, please check the info again!");

    }// ask for item id, check if its borrowable, check if its borrowed by someone else ? add username and time to it : show error

    public void checkIn(Statement statement) throws SQLException{
        System.out.print("Please enter the items id you want to return: ");
        int id = new Scanner(System.in).nextInt(); // No security

        // Check if the item is borrowed out to a user with the same username
        String RETURN_CHECK_QUERY = "SELECT * FROM libraryitem WHERE id="+ id +" AND Borrower = '"+ this.username +"';";
        boolean resultSetExists = statement.execute(RETURN_CHECK_QUERY);

        // If its the correct user, proceed return
        String RETURN_QUERY= "UPDATE libraryitem SET Borrower = NULL, BorrowDate = NULL WHERE id="+ id +";";
        if (resultSetExists){
            statement.execute(CHECKOUT_QUERY);
            System.out.println("Return success!");
        } else System.out.println("Error on returning, please check the info again!");
    }// Ask for the item id, check if its borrowed by that user ? return : show error
}
