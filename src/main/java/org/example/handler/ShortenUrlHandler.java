package org.example.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.util.DatabaseConnection;
import org.example.util.UniqueIDGenerator;
import org.json.JSONObject;

import static org.example.util.DatabaseConnection.LONG_URL_COLUMN;
import static org.example.util.DatabaseConnection.SHORT_CODE_COLUMN;
import static org.example.util.DatabaseConnection.URL_TABLE;

public class ShortenUrlHandler implements HttpHandler {

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
            shortUrl = UniqueIDGenerator.generateUniqueID();

            // Insert the new short URL into the database
            insertShortUrlIntoDatabase(longUrl, shortUrl);
        }

        // Create the response
        JSONObject response = new JSONObject();
        response.put("shortUrl", SHORT_URL_PREFIX + shortUrl);

        // Set the response headers
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.toString().getBytes().length);

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(response.toString().getBytes());
        responseBody.close();
    }

    private String getShortUrlFromDatabase(String longUrl) {
        String shortUrl = null;
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + SHORT_CODE_COLUMN + " FROM " + URL_TABLE + " WHERE " + LONG_URL_COLUMN + " = ?");
            preparedStatement.setString(1, longUrl);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                shortUrl = resultSet.getString(SHORT_CODE_COLUMN);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shortUrl;
    }

    private void insertShortUrlIntoDatabase(String longUrl, String shortUrl) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + URL_TABLE + " (" + LONG_URL_COLUMN + ", " + SHORT_CODE_COLUMN + ") VALUES (?, ?)");
            preparedStatement.setString(1, longUrl);
            preparedStatement.setString(2, shortUrl);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
