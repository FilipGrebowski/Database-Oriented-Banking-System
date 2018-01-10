package com.filipgrebowski;

/* The main class that allows a connection to the Sky Banking database. */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    static Connection connection = null;

    public static Connection getConnection(String database_name, String username, String password) {

        // Connecting to the database.
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database_name, username, password);
            connection.setAutoCommit(true);

        }
        catch (SQLException e) {
            System.out.println("Whoops, something went wrong with the connection.");
            e.printStackTrace();
        }
        return connection;
    }
}