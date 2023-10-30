package com.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connection {
    private Connection connection = null;

    public Connection() {
        try {
            connection = (Connection) DriverManager.getConnection("jdbc:sqlite:base.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
