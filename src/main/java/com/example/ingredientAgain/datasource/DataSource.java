package com.example.ingredientAgain.datasource;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String JDBC_URL = dotenv.get("JDBC_URL");
    private static final String USERNAME = dotenv.get("USERNAME");
    private static final String PASSWORD = dotenv.get("PASSWORD");

    public Connection getConnection() throws SQLException {
        if (JDBC_URL == null || USERNAME == null || PASSWORD == null) {
            throw new RuntimeException("""
                JDBC_URL, USERNAME et PASSWORD sont obligatoires.
                """);
        }
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}