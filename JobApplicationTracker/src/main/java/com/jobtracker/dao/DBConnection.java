package com.jobtracker.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton JDBC connection manager.
 * Supports environment variables for Railway deployment.
 */
public class DBConnection {

    // Default values for localhost
    private static String URL      = "jdbc:mysql://localhost:3306/task_manager?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static String USER     = "root";
    private static String PASSWORD = "root";

    private static Connection connection = null;

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        // If Railway environment variables are present, use them
        String envUrl  = System.getenv("MYSQL_URL"); // Often provided by Railway MySQL plugin
        String envUser = System.getenv("MYSQLUSER");
        String envPass = System.getenv("MYSQLPASSWORD");
        String envHost = System.getenv("MYSQLHOST");
        String envPort = System.getenv("MYSQLPORT");
        String envDb   = System.getenv("MYSQLDATABASE");

        if (envUrl != null) {
            URL = envUrl;
        } else if (envHost != null) {
            URL = "jdbc:mysql://" + envHost + ":" + envPort + "/" + envDb + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            USER = envUser;
            PASSWORD = envPass;
        }

        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found: " + e.getMessage(), e);
            }
        }
        return connection;
    }
}
