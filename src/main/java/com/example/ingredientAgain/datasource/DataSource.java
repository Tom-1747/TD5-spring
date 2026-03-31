package com.example.ingredientAgain.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    private static final String JDBC_URL  = "jdbc:postgresql://localhost:5432/mini_dish_db";
    private static final String USERNAME  = "mini_dish_db_manager";
    private static final String PASSWORD  = "123456";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}