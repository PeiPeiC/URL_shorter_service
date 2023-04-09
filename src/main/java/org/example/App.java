package org.example;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import org.example.handler.HelloHandler;
import org.example.handler.HealthHandler;
/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/health", new HealthHandler());//A context is used to define a specific part of a web application that can be accessed via a specific URI path.
        server.createContext("/hello",new HelloHandler());
        server.setExecutor(null); //Setting the executor to null effectively disables any additional thread management and uses the default thread pool provided by the HttpServer
        server.start();
        System.out.println("Server start");
    }
}


