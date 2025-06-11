package com.pluralsight;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
import javax.sql.DataSource;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What do you want to do");
        System.out.println("1) Display all product");
        System.out.print("2) Display all customers");
        System.out.print("0) Exit");
        System.out.println("Select an option");
        int option = scanner.nextInt();
        scanner.nextLine();

        if (args.length !=2) {
            System.out.println(
                    "Application needs two arguments to run: " +
                            "java com.pluralsight.UsingDriverManager <username> <password>"
            );
            System.exit(1);
        }
        //get the username and password
        String userName = args[0];
        String password = args[1];

// Declare variables for database connection, statement, and results
        Connection connection = null;
        PreparedStatement preparedstatement = null;
        ResultSet results = null;
        Scanner scanner1 = new Scanner(System.in); // Scanner for user input

        String query; // SQL query to be executed
        int choice;  // User's menu choice
        try {
            // Display menu options to user
            System.out.println("What do you want to do?");
            System.out.println("1) Display all products");
            System.out.println("2) Display all customers");
            System.out.println("0) Exit");
            System.out.println("Enter choice: ");

            choice = scanner.nextInt(); // Read user's choice

            // Exit if user chooses 0
            if (choice == 0) {
                System.out.println("You chose to exit");
                return;
            }

            // Set query based on user's choice
            if (choice == 1) {
                // Query to select product details
                query = "SELECT productId, productName, unitPrice, unitsInStock  FROM products";
            } else if (choice == 2) {
                // Query to select customer details, ordered by country
                query = "SELECT contactName, companyName, city,country, phone FROM customers ORDER BY country";
            } else{
                // Invalid choice entered
                System.out.println("Invalid choice");
                return; // Exit program for invalid choice
            }

            //1. open a connection to the database
            // use the database URL to point the correct database

            //this is like MySQL workbench and clicking localhost

            // Establish connection to MySQL database 'northwind' running on localhost
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", userName, password);
            // Prepare the SQL statement
            preparedstatement = connection.prepareStatement(query);
            // Execute the query and get results
            results = preparedstatement.executeQuery();

            // Process results based on user's choice
            if (choice == 1) {
                // Loop through product results
                while (results.next()) {

                    int productId = results.getInt("productId");
                    String productName = results.getString("ProductName");
                    double unitPrice = results.getDouble("unitPrice");
                    int unitInStock = results.getInt("unitsInStock");

                    System.out.println("Product ID: " + productId + ", Name: " + productName + ", Price: $"
                            + unitPrice + ", Stock: " + unitInStock + "  ------------------");

                }
            } else if (choice == 2) {
                while (results.next()) {
                    String contactName = results.getString("contactName");
                    String companyName = results.getString("companyName");
                    String city = results.getString("city");
                    String country = results.getString("country");
                    String phone = results.getString("phone");

                    System.out.println("Contact: " + contactName + ", Company: " + companyName +
                            ", City: " + city + ", Country: " + country + ", Phone: " + phone + "  ------------------");

                }

            }
        } catch (SQLException e){
            // Handle any SQL exceptions by printing error message
            System.out.println("Error :" + e.getMessage());
        }finally{
            // Close the ResultSet if it was opened
            try{
                if (results != null) results.close();
            }catch (SQLException e){
                System.out.println("Error closing resultSet: " + e.getMessage());
            }
            // Close the PreparedStatement if it was opened
            try{
                if (preparedstatement !=null) preparedstatement.close();
            }catch (SQLException e) {
                System.out.println("Error closing PreparedStatement: " + e.getMessage());
            }
            // Close the Connection if it was opened
            try{
                if(connection != null) connection.close();
            }catch (SQLException e){
                System.out.println("Error closing Connection: " + e.getMessage());
            }
            scanner.close();
        }
    }
}
