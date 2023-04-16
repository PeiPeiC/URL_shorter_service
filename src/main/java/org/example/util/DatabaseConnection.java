package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {

    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/url_shorter_service";
    public static final String URL_TABLE = "urls";
    public static final String DATABASE_USERNAME = "root";
    public static final String DATABASE_PASSWORD = "password";
    public static final String LONG_URL_COLUMN = "long_url";
    public static final String SHORT_CODE_COLUMN = "short_code";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
    }

    public static void init() throws ClassNotFoundException, SQLException {
// Connect to MySQL and insert data
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

        // SQL syntax
        String createTable = "CREATE TABLE IF NOT EXISTS " + URL_TABLE + " (id int PRIMARY KEY AUTO_INCREMENT, " + LONG_URL_COLUMN + " varchar(100) NOT NULL, " + SHORT_CODE_COLUMN
                + " varchar(100) NOT NULL UNIQUE);";
        PreparedStatement createStatement = connection.prepareStatement(createTable);
        createStatement.execute();
    }
}
