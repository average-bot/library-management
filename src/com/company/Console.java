package com.company;

import java.sql.*;
import java.text.ParseException;
import java.util.Scanner;

public class Console {

    Console(Statement statement) throws SQLException, ParseException {
        while (true) {
            System.out.print("Please choose a role to login:\n" +
                    "1. ADMIN \n" +
                    "2. USER \n" +
                    "3. EXIT \n" +
                    "Enter number here: ");
            switch (new Scanner(System.in).nextInt()) {
                case 1:
                    Employee employee = new Employee();
                    adminMenu(employee, statement);
                    break;
                case 2:
                    User user = new User();
                    userMenu(user, statement);
                    break;
                case 3:
                    System.exit(1);
                default:
                    System.out.println("Invalid role. Try again selecting a number.");
                    break;
            }
        }
    }

    public void adminMenu(Employee employee, Statement statement) throws SQLException {
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
                employee.viewItems(statement);
                break;
            case 2:
                employee.createItem(statement);
                break;
            case 3:
                employee.editItem(statement);
                break;
            case 4:
                employee.deleteItem(statement);
                break;
            case 5:
                employee.createCategory(statement);
                break;
            case 6:
                employee.editCategory(statement);
                break;
            case 7:
                employee.deleteCategory(statement);
                break;
            case 8:
                System.out.println("You are now logged out!");
                break;
            default:
                System.out.println("Invalid option. Try again selecting a number.");
                break;
        }
    }
    public void userMenu(User user, Statement statement) throws SQLException, ParseException {
        System.out.print("Please choose an option:\n" +
                "1. View items \n" +
                "2. Check out a book \n" +
                "3. Check in a book\n" +
                "4. Log out\n" +
                "Enter number here: ");
        switch(new Scanner(System.in).nextInt()) {
            case 1:
                user.viewItems(statement);
                break;
            case 2:
                user.checkOut(statement);
                break;
            case 3:
                user.checkIn(statement);
                break;
            case 4:
                System.out.println("logout");
                break;
            default:
                System.out.println("Invalid option. Try again selecting a number.");
                break;
        }
    }
}


