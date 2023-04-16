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
import org.example.handler.UrlRedirectHandler;
import org.example.util.DatabaseConnection;

/**
 * Hello world!
 */
public class App {

    public App() {
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        DatabaseConnection.init();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Create handlers
//        HealthHandler healthHandler = new HealthHandler();
//        HelloHandler helloHandler = new HelloHandler();
        ShortenUrlHandler shortenUrlHandler = new ShortenUrlHandler();
        UrlRedirectHandler urlRedirectHandler = new UrlRedirectHandler();

        // Register handlers
//        server.createContext("/health", healthHandler);
//        server.createContext("/hello", helloHandler);
        server.createContext("/shorten", shortenUrlHandler);
        server.createContext("/", urlRedirectHandler);

        // Start the server
        server.setExecutor(null);
        server.start();
        System.out.println("Server started.");
    }

}


