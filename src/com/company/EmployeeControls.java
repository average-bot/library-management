package com.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class EmployeeControls extends UserControls {
    public EmployeeControls(Statement statement) {
        super(statement);
    }
    // To access the viewItems method, grants the access to other methods too if needed

    void deleteCategory() throws SQLException {
        System.out.print("Please enter the category's id you want to delete: ");
        int id = new Scanner(System.in).nextInt();

        final String CHECK_IF_CAT_ITEMS = "SELECT * FROM category LEFT JOIN libraryitem ON libraryitem.CategoryId = category.Id WHERE category.Id="+ id +";";
        ResultSet resultSet = this.statement.executeQuery(CHECK_IF_CAT_ITEMS);
        if (resultSet.next()) {
            System.out.println("Error on deleting, please check that the category is not connected to any items!");
        } else {
            final String DELETE_CAT_QUERY = "DELETE FROM category WHERE Id="+id+";";
            this.statement.execute(DELETE_CAT_QUERY);
            System.out.println("Return success!");
        }
        resultSet.close();
    } // get the delete cat id from user + check if exists + check if the cat is not connected to anything

    void editCategory() throws SQLException { //TODO: Check if categoryName exists already + ask user for id and new name
        System.out.print("Please enter the category's id you want to edit: ");
        int id = new Scanner(System.in).nextInt();

        final String CHECK_CAT_QUERY = "SELECT * FROM category WHERE Id ="+ id +";";
        ResultSet resultSet = this.statement.executeQuery(CHECK_CAT_QUERY);
        if (resultSet.next()) {
            System.out.print("Please enter the new name for the category: ");
            String name = new Scanner(System.in).next();

            final String EDIT_CAT_QUERY = "UPDATE category SET CategoryName = '"+ name +"' WHERE id = "+ id +";";
            this.statement.execute(EDIT_CAT_QUERY);
            System.out.println("Edit category success!");
        } else {
            System.out.println("Editing category failed. Please check if its the correct id.");
        }
        resultSet.close();
    }// ask user for category id + check if exists + ask for new category name + update category

    void createCategory() throws SQLException {
        System.out.print("Please enter the new category name: ");
        String newCatName = new Scanner(System.in).next();

        final String CHECK_CAT_QUERY = "SELECT * FROM category WHERE categoryName ='"+ newCatName +"';";
        ResultSet resultSet = this.statement.executeQuery(CHECK_CAT_QUERY);
        if (resultSet.next()) {
            System.out.println("Error on creating category, this category seems to exist. Please choose a new name!");
        } else {
            final String CREATE_CAT_QUERY = "INSERT INTO category (CategoryName) VALUES ('"+ newCatName +"');";
            this.statement.execute(CREATE_CAT_QUERY);
            System.out.println("Create category success!");
        }
        resultSet.close();
    }// ask user for new category's name + check if categoryName exists already + create category

    void deleteItem() throws SQLException {
        System.out.print("Please enter the item's id you want to delete: ");
        int id = new Scanner(System.in).nextInt();

        final String CHECK_ITEM_QUERY = "SELECT * FROM libraryitem WHERE id="+id+";";
        ResultSet resultSet = this.statement.executeQuery(CHECK_ITEM_QUERY);
        if (resultSet.next()) {
            String DELETE_ITEM_QUERY = "DELETE FROM libraryitem WHERE Id="+id+";";
            this.statement.execute(DELETE_ITEM_QUERY);
            System.out.println("Create category success!");
        } else {System.out.println("Error on deleting an item, this item does not exist. Please choose the correct id!"); }
        resultSet.close();
    } // ask user for id + check if exists + delete item

    void editItem() throws SQLException {
        System.out.print("Please enter the item's id you want to edit: ");
        int id = new Scanner(System.in).nextInt();

        final String CHECK_ITEM_QUERY = "SELECT * FROM libraryitem WHERE id="+id+";";
        ResultSet resultSet = this.statement.executeQuery(CHECK_ITEM_QUERY);
        if (resultSet.next()) {
            ResultSet resultsSet = this.statement.executeQuery(CHECK_ITEM_QUERY);

            String type = resultsSet.getString("ItemType");

            String[] reqColumnNames = getColumns(type);
            String EDIT_ITEM_QUERY ="UPDATE libraryitem SET "+ itemsQuery(reqColumnNames, type) +" WHERE id="+ id +";";
            this.statement.execute(EDIT_ITEM_QUERY);

            System.out.println("Edit item success!");
            resultSet.close();
            resultsSet.close();
        } else {System.out.println("Error on editing an item, this item does not exist. Please choose the correct id!"); }
    } // ask user for id + check if exists + edit item

    void createItem() {
        System.out.print("Please enter the type of item you want to create: ");
        String type = new Scanner(System.in).next();

        String[] columnNames = getColumns(type); // everything but isBorrowable
        String CREATE_ITEM_QUERY = itemsQuery(columnNames, type);


        System.out.println("Create item success!");
    }// ask user for type (act accordingly) + create item

    private String[] getColumns(String type){
        String[] reqColumnNames = null;
        if ("book".equals(type) || "ref book".equals(type)) {
            reqColumnNames = new String[]{"Title", "Author", "Pages", "CategoryId"};
        } else if ("dvd".equals(type) || "audio book".equals(type)) {
            reqColumnNames = new String[]{"Title", "RunTimeMinutes", "CategoryId"};
        }
        return reqColumnNames;
    }// according to type create update item query

    private String itemsQuery(String[] reqColumnNames, String type){
        String queryPart = "";
        for(String reqColumn : reqColumnNames){
            System.out.println("Please enter new item's " + reqColumn);
            String value = new Scanner(System.in).next();
            try {
                int intValue = Integer.parseInt(value); // if its a number
                queryPart += reqColumn + "=" + intValue + ",";
            } catch (NumberFormatException e) { // if not a number
                queryPart += reqColumn + "='" + value + "',";
            }
        }
        if (type == "ref book") {
            queryPart += "isBorrowable='" + 0 + "'";
        } else{
            queryPart += "isBorrowable='" + 1 + "'";
        }
        return queryPart;
    } // create a query from all the columns
}
// TODO : CLOSE RESULTSET