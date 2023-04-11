package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.handler.HealthHandler;
import org.example.handler.HelloHandler;
import org.example.handler.ShortenUrlHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import java.sql.SQLException;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws IOException, SQLException {
        // Connect to MySQL and insert data
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/url_shorter_service", "root", "default");
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO url (idURL) VALUES (1)");
        stmt.setString(1, "test");
        int rowsInserted = stmt.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new record has been inserted successfully!");
        }
        conn.close();
        stmt.close();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        /*
         * A context is used to define a specific part of a web application that can be accessed via a specific URI path.
         */
        server.createContext("/health", new HealthHandler());
        server.createContext("/hello", new HelloHandler());
        server.createContext("/url-service/shorten", new ShortenUrlHandler());
        /*
         * Setting the executor to null effectively disables any additional thread management and uses the default thread pool provided by the HttpServer
         */
        server.setExecutor(null);
        server.start();
        System.out.println("Server start");
    }
}


