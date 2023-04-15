package org.example;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.example.handler.HealthHandler;
import org.example.handler.HelloHandler;
import org.example.handler.ShortenUrlHandler;

/**
 * Hello world!
 */
public class App {

    public App() {
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        // Connect to MySQL and insert data
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/url_shorter_service", "root", "password");

        // SQL syntax
        String createTable = "CREATE TABLE IF NOT EXISTS urlTable (id int PRIMARY KEY AUTO_INCREMENT, longUrl varchar(100) NOT NULL, shortCode varchar(100) NOT NULL UNIQUE);";
        PreparedStatement createStatement = connection.prepareStatement(createTable);
        createStatement.execute();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Create handlers
        HealthHandler healthHandler = new HealthHandler();
        HelloHandler helloHandler = new HelloHandler();
        ShortenUrlHandler shortenUrlHandler = new ShortenUrlHandler();

        // Register handlers
        server.createContext("/health", healthHandler);
        server.createContext("/hello", helloHandler);
        server.createContext("/shorten", shortenUrlHandler);

        // Start the server
        server.setExecutor(null);
        server.start();
        System.out.println("Server started.");
    }

}


