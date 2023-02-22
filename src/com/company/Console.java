package com.company;

import java.sql.*;
import java.util.Scanner;

public class Console {
    private Statement statement;

    Console(Statement statement) throws SQLException {
        this.statement = statement;
        while (true) {
            System.out.println("----------------------------");
            System.out.print("Please choose a role to login:\n" +
                    "1. ADMIN \n" +
                    "2. USER \n" +
                    "3. EXIT \n" +
                    "Enter option number here: ");
            switch (new Scanner(System.in).nextInt()) {
                case 1:
                    EmployeeControls employeeControls = new EmployeeControls(this.statement);
                    adminMenu(employeeControls);
                    break;
                case 2:
                    UserControls userControls = new UserControls(this.statement);
                    userMenu(userControls);
                    break;
                case 3:
                    System.exit(1);
                default:
                    System.out.println("Invalid role. Try again selecting a number.");
                    break;
            }
        }
    }

    public void adminMenu(EmployeeControls employeeControls) throws SQLException {
        boolean flag = true;
        do{
            System.out.println("----------------------------");
            System.out.print("Please choose an option:\n" +
                    "1. View items \n" +
                    "2. View Categories\n" +
                    "3. Create item \n" +
                    "4. Edit item \n" +
                    "5. Delete item \n" +
                    "-------------- \n" +
                    "6. Create category \n" +
                    "7. Edit category \n" +
                    "8. Delete category \n" +
                    "-------------- \n" +
                    "9. Log out\n" +
                    "Enter number here: ");
            switch(new Scanner(System.in).nextInt()) {
                case 1:
                    employeeControls.viewItems();
                    break;
                case 2:
                    employeeControls.showCategories();
                    break;
                case 3:
                    employeeControls.createItem();
                    break;
                case 4:
                    employeeControls.editItem();
                    break;
                case 5:
                    employeeControls.deleteItem();
                    break;
                case 6:
                    employeeControls.createCategory();
                    break;
                case 7:
                    employeeControls.editCategory();
                    break;
                case 8:
                    employeeControls.deleteCategory();
                    break;
                case 9:
                    System.out.println("You are now logged out!");
                    flag = false;
                    System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
                    break;
                default:
                    System.out.println("Invalid option. Try again selecting a number.");
                    break;
            }
        }while(flag);
    }
    public void userMenu(UserControls userControls) throws SQLException {
        boolean flag = true;
        do{
            System.out.println("----------------------------");
            System.out.print("Please choose an option:\n" +
                    "1. View items \n" +
                    "2. View categories \n" +
                    "3. Check out a book \n" +
                    "4. Check in a book\n" +
                    "5. Change sorting\n" +
                    "6. Log out\n" +
                    "Enter option number here: ");
            switch (new Scanner(System.in).nextInt()) {
                case 1:
                    userControls.viewItems();
                    break;
                case 2:
                    userControls.showCategories();
                    break;
                case 3:
                    userControls.checkOut();
                    break;
                case 4:
                    userControls.checkIn();
                    break;
                case 5:
                    userControls.changeSorting();
                    break;
                case 6:
                    System.out.println("logout");
                    flag = false;
                    System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
                    break;
                default:
                    System.out.println("Invalid option. Try again selecting a number.");
                    break;
            }
        }while(flag);
    }
}
