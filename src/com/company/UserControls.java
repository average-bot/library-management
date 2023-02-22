package com.company;

import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class UserControls {
    private String username;
    boolean sortingByCat = false;
    Statement statement;

    public UserControls(Statement statement){
        this.statement = statement;
    }

    public void setUsername() {
        System.out.print("Please enter your username: ");
        String username;
        do {
            username = new Scanner(System.in).next(); // No security
            username = username.toLowerCase();
        }while(username.equals("")); // TODO: other reqs for username
        this.username = username;
    }

    public void changeSorting(){
        if (sortingByCat) sortingByCat=false; else sortingByCat=true;
    }

    public void viewItems() throws SQLException { // TODO: Add acronym
        final String VIEW_QUERY_CAT = "SELECT libraryitem.Id, category.Id AS category_id, category.CategoryName, libraryitem.Title, libraryitem.Author, libraryitem.Pages, libraryitem.RunTimeMinutes, libraryitem.isBorrowable, libraryitem.Borrower, libraryitem.BorrowDate, libraryitem.ItemType FROM libraryitem LEFT JOIN category ON Category.Id= libraryitem.CategoryId ORDER BY category.CategoryName ASC;";
        final String VIEW_QUERY_TYPE = "SELECT libraryitem.Id, category.Id AS category_id, category.CategoryName, libraryitem.Title, libraryitem.Author, libraryitem.Pages, libraryitem.RunTimeMinutes, libraryitem.isBorrowable, libraryitem.Borrower, libraryitem.BorrowDate, libraryitem.ItemType FROM libraryitem LEFT JOIN category ON Category.Id= libraryitem.CategoryId ORDER BY libraryitem.ItemType ASC;";

        String query = sortingByCat? VIEW_QUERY_CAT: VIEW_QUERY_TYPE;
        ResultSet resultSet = this.statement.executeQuery(query);
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

    public void checkOut() throws SQLException {
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
        final String CHECKOUT_CHECK_QUERY = "SELECT * FROM libraryitem WHERE id="+ id +" AND Borrower IS NULL AND isBorrowable=1;";
        boolean resultSetExists = this.statement.execute(CHECKOUT_CHECK_QUERY);

        // If available run the query for borrowing
        final String CHECKOUT_QUERY= "UPDATE libraryitem SET Borrower = '" + username +"', BorrowDate = '"+ sqlDate +"'WHERE id="+ id +" AND isBorrowable=1 AND Borrower IS NULL;";
        if (resultSetExists){
            this.statement.execute(CHECKOUT_QUERY);
            System.out.println("Borrow success!");
        } else System.out.println("Error on borrowing, please check the info again!");

    }// ask for item id, check if its borrowable, check if its borrowed by someone else ? add username and time to it : show error

    public void checkIn() throws SQLException{
        System.out.print("Please enter the items id you want to return: ");
        int id = new Scanner(System.in).nextInt(); // No security

        // Check if the item is borrowed out to a user with the same username
        final String RETURN_CHECK_QUERY = "SELECT * FROM libraryitem WHERE id="+ id +" AND Borrower = '"+ this.username +"';";
        boolean resultSetExists = this.statement.execute(RETURN_CHECK_QUERY);

        // If its the correct user, proceed return
        final String RETURN_QUERY= "UPDATE libraryitem SET Borrower = NULL, BorrowDate = NULL WHERE id="+ id +";";
        if (resultSetExists){
            this.statement.execute(RETURN_QUERY);
            System.out.println("Return success!");
        } else System.out.println("Error on returning, please check the info again!");
    }// Ask for the item id, check if its borrowed by that user ? return : show error
}
//TODO: CHECKS FOR EXIST DONT WORK FOR CHECK OUT