package com.company;

import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class UserControls {
    boolean sortingByCat = true;
    Statement statement;

    public UserControls(Statement statement){
        this.statement = statement;
    }

    public void changeSorting(){
        if (sortingByCat) sortingByCat=false; else sortingByCat=true;
    }

    public void viewItems() throws SQLException { // TODO: Add acronym
        final String VIEW_QUERY_CAT = "SELECT libraryitem.Id, category.Id AS category_id, category.CategoryName, libraryitem.Title, libraryitem.Author, libraryitem.Pages, libraryitem.RunTimeMinutes, libraryitem.isBorrowable, libraryitem.Borrower, libraryitem.BorrowDate, libraryitem.ItemType FROM libraryitem LEFT JOIN category ON Category.Id= libraryitem.CategoryId ORDER BY category.CategoryName ASC;";
        final String VIEW_QUERY_TYPE = "SELECT libraryitem.Id, category.Id AS category_id, category.CategoryName, libraryitem.Title, libraryitem.Author, libraryitem.Pages, libraryitem.RunTimeMinutes, libraryitem.isBorrowable, libraryitem.Borrower, libraryitem.BorrowDate, libraryitem.ItemType FROM libraryitem LEFT JOIN category ON Category.Id= libraryitem.CategoryId ORDER BY libraryitem.ItemType ASC;";

        String query = sortingByCat? VIEW_QUERY_CAT: VIEW_QUERY_TYPE; // check the sorting type before printing, choose query accordingly
        ResultSet resultSet = this.statement.executeQuery(query);
        String[] columnNames = {"ItemID", "CategoryID", "CategoryName", "Title", "Author", "Pages", "Length", "Borrowable", "Borrower", "BorrowDate", "ItemType", "Acronym"};
        while (resultSet.next()) {
            // print all the column names
            for (int i = 0; i < columnNames.length; i++){
                System.out.print(String.format("-%-16s",columnNames[i]));
            }
            System.out.println("");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            // print all the information per row
            do{
                for (int i = 1; i <= columnsNumber; i++) {
                    System.out.print(String.format("|%-16s",resultSet.getString(i)));
                }
                System.out.print(String.format("|%-16s",resultSet.getString("Title").replaceAll("\\B.|\\P{L}", "").toUpperCase())); // The acronyms of the titles
                System.out.println("");
            }while(resultSet.next());
        }
        resultSet.close();
    }

    public void showCategories() throws SQLException {
        final String SHOW_CATS_QUERY ="SELECT * FROM category";
        ResultSet resultSet = statement.executeQuery(SHOW_CATS_QUERY);

        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        // print all the information per row
        System.out.println("Here are the existing categories with their id's: ");
        while(resultSet.next()){
            for (int i = 1; i <= columnsNumber; i++) {
                System.out.print(resultSet.getString(i)+ " ");
            }
            System.out.println(" ");
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

        int id = getId("item", "check out");
        // Check if the item is available or borrowed to someone
        final String CHECKOUT_CHECK_QUERY = "SELECT * FROM libraryitem WHERE id="+ id +" AND Borrower IS NULL AND isBorrowable=1;";
        ResultSet resultSet = this.statement.executeQuery(CHECKOUT_CHECK_QUERY);
        if (resultSet.next()) {
            // If available run the query for borrowing
            String username = getString("username");
            final String CHECKOUT_QUERY= "UPDATE libraryitem SET Borrower = '" + username +"', BorrowDate = '"+ sqlDate +"'WHERE id="+ id +" AND isBorrowable=1 AND Borrower IS NULL;";
            this.statement.execute(CHECKOUT_QUERY);
            System.out.println("Borrow success!");
        } else System.out.println("Error on borrowing, please check the info again!");
        resultSet.close();

    }// ask for item id, check if its borrowable, check if its borrowed by someone else ? add username and time to it : show error

    public void checkIn() throws SQLException{
        // Check if the item is borrowed out to a user with the same username
        String username = getString("username");
        int id = getId("item", "check in");
        final String RETURN_CHECK_QUERY = "SELECT * FROM libraryitem WHERE id="+ id +" AND Borrower = '"+ username +"';";
        ResultSet resultSet = this.statement.executeQuery(RETURN_CHECK_QUERY);
        if (resultSet.next()) {
            // If its the correct user, proceed return
            final String RETURN_QUERY= "UPDATE libraryitem SET Borrower = NULL, BorrowDate = NULL WHERE id="+ id +";";
            this.statement.execute(RETURN_QUERY);
            System.out.println("Return success!");
        } else System.out.println("Error on returning, please check the info again!");
        resultSet.close();
    }// Ask for the item id, check if its borrowed by that user ? return : show error

    public int getId(String thing, String operation){ // getting a valid input for id
        System.out.print("Please enter the "+ thing +"'s id here to "+operation+": ");
        int id;
        Scanner scanner;
        do{
            scanner = new Scanner(System.in);
            if(scanner.hasNextInt()) {
                id = scanner.nextInt();
                break;
            }
            System.out.print("Invalid input. Please input a number.");
        }while(true);
        return id;
    }
    public String getString(String field) { // getting a valid input for a text input (in this case we want it to not be empty)
        System.out.print("Please enter "+ field +": ");
        String fieldValue = "";
        Scanner scanner;
        do{
            scanner = new Scanner(System.in);
            if(scanner.hasNext()) {
                fieldValue = scanner.nextLine();
                fieldValue = fieldValue.toLowerCase();
                break;
            }
            System.out.print("Invalid input. Please input a characters: ");
        }while(fieldValue.equals(""));
        return fieldValue;
    }
}