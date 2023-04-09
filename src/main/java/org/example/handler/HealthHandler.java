package org.example.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

public class HealthHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        /**
         * the "response" string is created as a JSON message with a "status" field set to "OK" because the HTTP response is intended to be consumed by a client application, and JSON is a commonly used data format for transmitting data between web applications and client applications.
         */
        String response = "{\"status\":\"OK\"}";
        /**
         * sends an HTTP response header with a status code of 200 (OK) and a content length equal to the length of the "response" string.
         */
        exchange.sendResponseHeaders(200, response.length());
        /**
         * sends an HTTP response header with a status code of 200 (OK) and a content length equal to the length of the "response" string.
         */
        exchange.getResponseBody().write(response.getBytes());
        /**
         * write() method expects an array of bytes as its argument, not a string. By calling response.getBytes(), we are converting the string into an array of bytes, which can then be written to the response body using write() method.
         */
        exchange.getResponseBody().close();
    }
}
