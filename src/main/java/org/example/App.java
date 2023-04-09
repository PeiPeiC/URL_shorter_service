package org.example;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/health", new HealthHandler());//A context is used to define a specific part of a web application that can be accessed via a specific URI path.
        server.setExecutor(null); //Setting the executor to null effectively disables any additional thread management and uses the default thread pool provided by the HttpServer
        server.start();
        System.out.println("Server start");
    }
}
class HealthHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        String response = "{\"status\":\"OK\"}"; //the "response" string is created as a JSON message with a "status" field set to "OK" because the HTTP response is intended to be consumed by a client application, and JSON is a commonly used data format for transmitting data between web applications and client applications.
        exchange.sendResponseHeaders(200, response.length()); //sends an HTTP response header with a status code of 200 (OK) and a content length equal to the length of the "response" string.
        exchange.getResponseBody().write(response.getBytes()); //write() method expects an array of bytes as its argument, not a string. By calling response.getBytes(), we are converting the string into an array of bytes, which can then be written to the response body using the write() method.
        exchange.getResponseBody().close();
    }
}
