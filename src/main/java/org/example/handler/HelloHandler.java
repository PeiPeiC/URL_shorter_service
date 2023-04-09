package org.example.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

public class HelloHandler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        String response = "world";
        exchange.sendResponseHeaders(200,
                response.length()); //sends an HTTP response header with a status code of 200 (OK) and a content length equal to the length of the "response" string.
        exchange.getResponseBody()
                .write(response.getBytes()); //write() method expects an array of bytes as its argument, not a string. By calling response.getBytes(), we are converting the string into an array of bytes, which can then be written to the response body using the write() method.
        exchange.getResponseBody().close();
    }
}
