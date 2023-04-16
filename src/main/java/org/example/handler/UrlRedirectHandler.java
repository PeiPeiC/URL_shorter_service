package org.example.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

public class UrlRedirectHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            // Get the request URI from the exchange object
            String path = exchange.getRequestURI().getPath();
            // remove leading "/"
            String shortCode = path.substring(1);

            //TODO: use shortCode to find the long url
            String longUrl = "";

            exchange.getResponseHeaders().set("Location", longUrl);
            exchange.sendResponseHeaders(301, -1);
            exchange.close();
        }
    }
}
