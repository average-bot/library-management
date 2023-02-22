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
        showCategories();
        int id = getId("delete", "category");

        final String CHECK_IF_CAT_ITEMS = "SELECT libraryitem.id FROM category LEFT JOIN libraryitem ON libraryitem.CategoryId = category.Id WHERE category.Id="+ id +" AND libraryitem.id IS NOT NULL;";
        ResultSet resultSet = this.statement.executeQuery(CHECK_IF_CAT_ITEMS);
        if (resultSet.next()) {
            System.out.println("Error on deleting, please check that the category is not connected to any items!");
        } else {
            final String DELETE_CAT_QUERY = "DELETE FROM category WHERE Id="+id+";";
            this.statement.execute(DELETE_CAT_QUERY);
            System.out.println("Delete success!");
        }
        resultSet.close();
    } // get the delete cat id from user + check if exists + check if the cat is not connected to anything

    void editCategory() throws SQLException {
        showCategories();
        int id = getId("edit", "category");

        final String CHECK_CAT_QUERY = "SELECT * FROM category WHERE Id ="+ id +";";
        ResultSet resultSet = this.statement.executeQuery(CHECK_CAT_QUERY);
        if (resultSet.next()) {
            String name = getString("new name");

            final String EDIT_CAT_QUERY = "UPDATE category SET CategoryName = '"+ name +"' WHERE id = "+ id +";";
            this.statement.execute(EDIT_CAT_QUERY);
            System.out.println("Edit category success!");
        } else {
            System.out.println("Editing category failed. Please check if its the correct id.");
        }
        resultSet.close();
    }// ask user for category id + check if exists + ask for new category name + update category

    void createCategory() throws SQLException {
        showCategories();
        String newCatName = getString("new category name");

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
        int id = getId("delete", "item");

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
        int id = getId("edit", "item");

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

    void createItem() throws SQLException {
        showCategories();
        String type = getType("create");

        String[] columnNames = getColumns(type);
        String[] queryPart = inertQuery(columnNames, type); // [columns, values]
        String CREATE_ITEM_QUERY ="INSERT INTO libraryitem (" +queryPart[0]+ ") VALUES ("+ queryPart[1] +");";
        statement.execute(CREATE_ITEM_QUERY);
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
            String value = getString(reqColumn);
            try {
                int intValue = Integer.parseInt(value); // if its a number
                queryPart += reqColumn + "=" + intValue + ",";
            } catch (NumberFormatException e) { // if not a number
                queryPart += reqColumn + "='" + value + "',";
            }
        }
        if (type == "ref book") {
            queryPart += "isBorrowable=" + 0 + "";
        } else{
            queryPart += "isBorrowable=" + 1 + "";
        }
        return queryPart;
    } // create a query from all the columns

    private String[] inertQuery(String[] columnNames, String type){
        String columns = "";
        String values = "";
        for(String reqColumn : columnNames){
            columns += reqColumn+ ", ";
            String value = getString(reqColumn);
            try {
                int intValue = Integer.parseInt(value); // if its a number
                values += intValue + ", ";
            } catch (NumberFormatException e) { // if not a number
                values += "'" + value + "', ";
            }
        }
        columns += "isBorrowable, ";
        if (type == "ref book") {
            values += 0 +", ";

        } else{
            values += 1 + ", ";
        }
        columns += "ItemType";
        values += "'"+type+"'";
        return new String[]{columns, values};
    }

    private String getType(String operation){
        System.out.print("Please enter the type of item you want to "+ operation +": ");
        String fieldValue = "";
        Scanner scanner;
        do{
            scanner = new Scanner(System.in);
            if(scanner.hasNext()) {
                fieldValue = scanner.nextLine();
                fieldValue = fieldValue.toLowerCase();
                if(fieldValue.equals("book") || fieldValue.equals("dvd") || fieldValue.equals("audio book") || fieldValue.equals("ref book")){
                    break;
                }
                System.out.println("Invalid input. Please choose between book, dvd, audio book and ref book.");
            }
            System.out.print("Invalid input. Please input a characters: ");
        }while(true);
        return fieldValue;
    }
}