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

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        // Connect to MySQL and insert data
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/url_shorter_service", "root", "password");

        //SQL syntax
        String drop = "DROP TABLE IF EXISTS urlTable;";
        String create = "CREATE TABLE urlTable (id int PRIMARY KEY," +
                "  longUrl varchar(100) NOT NULL);";
        String insert = "INSERT INTO urlTable(id,longUrl) " + "VALUES(?,?)";

        //clear table
        PreparedStatement statement = connection.prepareStatement(drop);
        statement.execute();

        //create table
        statement = connection.prepareStatement(create);
        statement.execute();

        //insert data
        statement = connection.prepareStatement(insert);
        statement.setInt(1, 1);
        statement.setString(2, "http://localhost:8080");
        statement.execute();

        statement.close();
        connection.close();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        /*
         * A context is used to define a specific part of a web application that can be accessed
         * via a specific URI path.
         */
        server.createContext("/health", new HealthHandler());
        server.createContext("/hello", new HelloHandler());
        server.createContext("/url-service/shorten", new ShortenUrlHandler());
        /*
         * Setting the executor to null effectively disables any additional thread management and
         *  uses the default thread pool provided by the HttpServer
         */
        server.setExecutor(null);
        server.start();
        System.out.println("Server start");
    }
}


