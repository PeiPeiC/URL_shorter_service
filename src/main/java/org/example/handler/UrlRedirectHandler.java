package org.example.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.sql.*;

public class UrlRedirectHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            // Get the request URI from the exchange object
            String path = exchange.getRequestURI().getPath();
            // remove leading "/"
            String short_url = path.substring(1);

            //TODO: use shortCode to find the long url
            String longUrl = "";
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/url_shorter_service", "root", "password");
                String query = "SELECT long_url FROM urls WHERE short_url = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, short_url);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    longUrl = resultSet.getString("long_url");
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            exchange.getResponseHeaders().set("Location", longUrl);
            exchange.sendResponseHeaders(301, -1);
            exchange.close();
        }
    }
}
