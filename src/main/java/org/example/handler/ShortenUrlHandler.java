package org.example.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class ShortenUrlHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Get the request body
        InputStream requestBody = exchange.getRequestBody();
        String requestBodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

        // Parse the JSON payload
        JSONObject payload = new JSONObject(requestBodyString);
        String longUrl = payload.getString("longUrl");

        // Create the response
        JSONObject response = new JSONObject();
        response.put("longUrl", longUrl);

        // Set the response headers
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.toString().getBytes().length);

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(response.toString().getBytes());
        responseBody.close();
    }
}
