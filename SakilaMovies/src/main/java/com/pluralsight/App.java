package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

    public class App {
        // This lets us type things into the computer

        static Scanner input = new Scanner(System.in);
        // Make sure we have a username and password before starting

        public static void main(String[] args) {

            if (args.length != 2) {
                System.out.println(
                        "Application needs two arguments to run: " +
                                "java com.pluralsight.UsingDriverManager <username> <password>"
                );
                System.exit(1);}// Stop the program if info is missing

            String username = args[0];
            String password = args[1];
            // Setup the connection to the movie database on our computer

            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
            dataSource.setUsername(username);
            dataSource.setPassword(password);



            try {
                // Let the user search for actors with a certain last name

                System.out.println("Enter last name to display matches of actors.");
                System.out.print("Enter actor's last name: ");
                String lastName = input.nextLine();
                // Connect to the database and search for the actor

                try (Connection connection = dataSource.getConnection();
                     PreparedStatement stmt = connection.prepareStatement(
                             "SELECT actor_id, first_name, last_name FROM actor WHERE last_name = ?"
                     )) {
                    stmt.setString(1, lastName); // Fill in the ? with the user's input

                    try (ResultSet results = stmt.executeQuery()) {
                        if (results.next()) {
                            System.out.println("\nActors with last name '" + lastName + "':");
                            System.out.println("ID   First Name       Last Name");
                            System.out.println("-----------------------------------");
                            do {
                                // Print each actor's details

                                System.out.printf("%-4d %-15s %-15s%n",
                                        results.getInt("actor_id"),
                                        results.getString("first_name"),
                                        results.getString("last_name"));
                            } while (results.next());
                        } else {
                            System.out.println("No actors found with last name '" + lastName + "'.");
                        }
                    }
                }
                System.out.println("-----------------------------------");
                // Ask the user to enter full name to look up movies they've acted in

                System.out.println("\nEnter full name to display movies where actor participated.");
                System.out.print("Enter actor's first name: ");
                String firstName = input.nextLine();
                System.out.print("Enter actor's last name: ");
                String fullLastName = input.nextLine();

                // SQL query to find all movies featuring the given actor

                String movieQuery = """
                SELECT film.title, film.description, film.release_year
                FROM film
                JOIN film_actor ON film.film_id = film_actor.film_id
                JOIN actor ON film_actor.actor_id = actor.actor_id
                WHERE actor.first_name = ? AND actor.last_name = ?
                ORDER BY film.title;
                """;
                // Execute the movie query and display results

                try (Connection connection = dataSource.getConnection();
                     PreparedStatement stmt = connection.prepareStatement(movieQuery)) {
                    stmt.setString(1, firstName);
                    stmt.setString(2, fullLastName);

                    try (ResultSet results = stmt.executeQuery()) {
                        if (results.next()) {
                            System.out.println("\nMovies featuring " + firstName + " " + fullLastName + ":");
                            System.out.println("Title                          Year           Description");
                            System.out.println("-------------------------------------------------------------");
                            do {
                                //show each movie title , year , and what it is about
                                // Display movie title, release year, and description

                                String title = results.getString("title");
                                String description = results.getString("description");
                                String year = results.getString("release_year");

                                System.out.printf("%-30s %-5s     %s%n", title, year, description);
                            } while (results.next());
                        } else {
                            // No movies found for the entered actor

                            System.out.println("No movies found for " + firstName + " " + fullLastName + ".");
                        }
                    }
                    System.out.println("-------------------------------------------------------------");
                }

            } catch (SQLException e) {
                // If any database error occurs, print the error details
                e.printStackTrace();
            }
        }
    }



