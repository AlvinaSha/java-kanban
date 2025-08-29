package tasktracker.manager.api.handler;

import com.sun.net.httpserver.HttpExchange;
import tasktracker.manager.task.TaskManager;
import tasktracker.model.Task;

import java.io.IOException;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/tasks")) {
            List<Task> tasks = taskManager.getAllTasks();
            sendSuccess(exchange, tasks);
        } else {
            String[] parts = path.split("/");
            if (parts.length == 3) {
                int id = Integer.parseInt(parts[2]);
                Task task = taskManager.getTaskById(id);
                if (task != null) {
                    sendSuccess(exchange, task);
                } else {
                    sendNotFound(exchange);
                }
            } else {
                sendNotFound(exchange);
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        Task task = readJson(exchange, Task.class);
        if (task.getId() == 0) {
            Task created = taskManager.createTask(task);
            sendSuccess(exchange, created);
        } else {
            Task updated = taskManager.updateTask(task);
            if (updated != null) {
                sendSuccess(exchange, updated);
            } else {
                sendNotFound(exchange);
            }
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/tasks")) {
            taskManager.deleteAllTasks();
            sendCreated(exchange);
        } else {
            String[] parts = path.split("/");
            if (parts.length == 3) {
                int id = Integer.parseInt(parts[2]);
                taskManager.deleteTaskById(id);
                sendCreated(exchange);
            } else {
                sendNotFound(exchange);
            }
        }
    }
}
