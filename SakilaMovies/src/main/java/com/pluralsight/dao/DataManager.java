package com.pluralsight.dao;

import com.pluralsight.models.Actor;
import com.pluralsight.models.Film;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

        private BasicDataSource dataSource;

        public DataManager(String username, String password) {
            dataSource = new BasicDataSource();
            dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
            dataSource.setUsername(username);
            dataSource.setPassword(password);
        }

        public List<Actor> findActorsByLastName(String lastName) {
            List<Actor> actors = new ArrayList<>();
            String query = "SELECT actor_id, first_name, last_name FROM actor WHERE last_name = ?";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, lastName);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Actor actor = new Actor(
                                rs.getInt("actor_id"),
                                rs.getString("first_name"),
                                rs.getString("last_name")
                        );
                        actors.add(actor);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return actors;
        }

        public List<Film> findFilmsByActorId(int actorId) {
            List<Film> films = new ArrayList<>();
            String query = """
                SELECT film.film_id, film.title, film.description, film.release_year, film.length
                FROM film
                JOIN film_actor ON film.film_id = film_actor.film_id
                WHERE film_actor.actor_id = ?
                ORDER BY film.title;
                """;

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, actorId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Film film = new Film(
                                rs.getInt("film_id"),
                                rs.getString("title"),
                                rs.getString("description"),
                                rs.getInt("release_year"),
                                rs.getInt("length")
                        );
                        films.add(film);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return films;
        }
    }

