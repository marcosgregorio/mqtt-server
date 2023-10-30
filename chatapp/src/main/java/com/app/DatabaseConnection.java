package com.app;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private Connection connection = null;

    public DatabaseConnection() {
        try {
            connection = (Connection) DriverManager.getConnection("jdbc:sqlite:base.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS terminalroot");
            statement.executeUpdate("CREATE TABLE terminalroot (id INTEGER, name STRING)");
            statement.executeUpdate("INSERT INTO terminalroot VALUES(1, 'Marcos Oliveira')");
            statement.executeUpdate("INSERT INTO terminalroot VALUES(2, 'James Gosling')");
            ResultSet rs = statement.executeQuery("SELECT * FROM terminalroot");
            while (rs.next()) {
                // Ler os dados inseridos
                System.out.println("NOME DO CARA  : " + rs.getString("name"));
                System.out.println("IDENTIFICAÇÃO : " + rs.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
