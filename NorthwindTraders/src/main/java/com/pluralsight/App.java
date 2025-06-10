package com.pluralsight;

import java.sql.*;

public class App {
    public static void main(String[] args) {

        try {

            // 1. open a connection to the database
            // use the database URL to point to the correct database

            //this is like opening MySQL workbench and clicking localhost
            Connection connection;
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", "root", "Ethiopia18@");


            // create statement
            // the statement is tied to the open connection

            //like me opening a new query window
            Statement statement = connection.createStatement();

            // define your query

            //like me typing the query in the new query windows
            String query = "SELECT ProductName,UnitPrice, UnitsInStock  FROM Products;";

            // 2. Execute your query

            //this is like me clicking the lightning bolt
            ResultSet results = statement.executeQuery(query);

            // process the results
            //this is a way to view the result set but java doesnt have a spreadsheet view for us
            while (results.next()) {
                int id = results.getInt("ProductID");
                String name = results.getString("ProductName");
                double price = results.getDouble("UnitPrice");
                int stock = results.getInt("UnitsInStock");

              System.out.print("...........................");
              System.out.print("Product Id:%d%nName: %s%nPrice: %.2f%nStock: %d%n");
              System.out.println(".................................");

            }

            // 3. Close the connection

            //closing mysql workbench
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


}