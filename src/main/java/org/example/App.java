package org.example;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.example.handler.HealthHandler;
import org.example.handler.HelloHandler;
import org.example.handler.ShortenUrlHandler;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        /**
         * A context is used to define a specific part of a web application that can be accessed via a specific URI path.
         */
        server.createContext("/health", new HealthHandler());
        server.createContext("/hello", new HelloHandler());
        server.createContext("/url-service/shorten", new ShortenUrlHandler());
        /**
         * Setting the executor to null effectively disables any additional thread management and uses the default thread pool provided by the HttpServer
         */
        server.setExecutor(null);
        server.start();
        System.out.println("Server start");
    }
}


