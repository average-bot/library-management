package com.company;

import java.sql.*;
import java.util.Scanner;

public class Console {

        Console(Statement statement) throws SQLException {
            roleMenu(statement);
        }

        public void roleMenu(Statement statement) throws SQLException {
            while (true) {
                System.out.print("Please choose a role to login:\n" +
                        "1. ADMIN \n" +
                        "2. USER \n" +
                        "3. EXIT \n" +
                        "Enter number here: ");
                switch (new Scanner(System.in).nextInt()) {
                    case 1:
                        Employee employee = new Employee();
                        employee.adminMenu(statement);
                        break;
                    case 2:
                        System.out.print("Please enter your username: ");
                        String username = new Scanner(System.in).next();
                        User user = new User(username);
                        user.userMenu(statement);
                        break;
                    case 3:
                        System.exit(1);
                    default:
                        System.out.println("Invalid role. Try again selecting a number.");
                        break;
                }
            }
        }
}


