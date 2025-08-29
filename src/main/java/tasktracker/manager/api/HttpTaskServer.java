package tasktracker.manager.api;

import com.sun.net.httpserver.HttpServer;
import tasktracker.manager.Managers;
import tasktracker.manager.api.handler.*;
import tasktracker.manager.task.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this.taskManager = Managers.getDefault();
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        configureHandlers();
    }

    private void configureHandlers() {
        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void start() {
        System.out.println("Starting server on port " + PORT);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped");
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(taskServer::stop));
    }
}