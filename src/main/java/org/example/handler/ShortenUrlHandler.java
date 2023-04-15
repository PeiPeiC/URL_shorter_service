package org.example.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.until.DatabaseConnection;
import org.example.until.UniqueIDGenerator;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortenUrlHandler implements HttpHandler {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/url_shorter_service";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "password";
    private static final String LONG_URL_COLUMN_NAME = "long_url";
    private static final String SHORT_URL_COLUMN_NAME = "short_url";
    private static final String SHORT_URL_PREFIX = "http://localhost:8080/";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Get the request body
        InputStream requestBody = exchange.getRequestBody();
        String requestBodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

        // Parse the JSON payload
        JSONObject payload = new JSONObject(requestBodyString);
        String longUrl = payload.getString("longUrl");

        // Check if the long URL is already in the database
        String shortUrl = getShortUrlFromDatabase(longUrl);
        if (shortUrl == null) {
            // Generate a unique short code using the UniqueIDGenerator class
            String shortCode = UniqueIDGenerator.generateUniqueID();
            shortUrl = SHORT_URL_PREFIX + shortCode;

            // Insert the new short URL into the database
            insertShortUrlIntoDatabase(longUrl, shortUrl);
        }

        // Create the response
        JSONObject response = new JSONObject();
        response.put("shortUrl", shortUrl);

        // Set the response headers
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.toString().getBytes().length);

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(response.toString().getBytes());
        responseBody.close();
    }

    private String getShortUrlFromDatabase(String longUrl) {
        String shortUrl = null;
        try (Connection connection = DatabaseConnection.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + SHORT_URL_COLUMN_NAME + " FROM urls WHERE " + LONG_URL_COLUMN_NAME + " = ?");
            preparedStatement.setString(1, longUrl);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                shortUrl = resultSet.getString(SHORT_URL_COLUMN_NAME);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shortUrl;
    }

    private void insertShortUrlIntoDatabase(String longUrl, String shortUrl) {
        try (Connection connection = DatabaseConnection.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO urls (" + LONG_URL_COLUMN_NAME + ", " + SHORT_URL_COLUMN_NAME + ") VALUES (?, ?)");
            preparedStatement.setString(1, longUrl);
            preparedStatement.setString(2, shortUrl);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
