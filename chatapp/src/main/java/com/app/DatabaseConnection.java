package com.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private Connection connection = null;

    public DatabaseConnection() {
        try {
            connection = (Connection) DriverManager.getConnection("jdbc:sqlite:base.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS usuarios");
            statement.executeUpdate("" +
                    "CREATE TABLE usuarios ( " +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "user_id STRING UNIQUE, " +
                        "password STRING " +
                    ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
