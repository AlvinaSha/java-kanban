package tasktracker.manager.api.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {

    protected static final Gson GSON = new Gson();

    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    protected void sendSuccess(HttpExchange exchange, Object responseObject) throws IOException {
        String response = GSON.toJson(responseObject);
        sendText(exchange, response, 200);
    }

    protected void sendCreated(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(201, -1);
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, -1);
    }

    protected void sendNotAcceptable(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(406, -1);
    }

    protected void sendInternalError(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(500, -1);
    }

    protected String readText(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    protected <T> T readJson(HttpExchange exchange, Class<T> classOfT) throws IOException {
        String body = readText(exchange);
        return GSON.fromJson(body, classOfT);
    }
}