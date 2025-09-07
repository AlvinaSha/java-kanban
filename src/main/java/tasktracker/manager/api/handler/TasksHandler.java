package tasktracker.manager.api.handler;

import com.sun.net.httpserver.HttpExchange;
import tasktracker.manager.exception.NotFoundException;
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
            String path = exchange.getRequestURI().getPath();

            if (!isPathSupported(path)) {
                sendNotFound(exchange);
                return;
            }

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
                    sendNotAllowed(exchange);
            }
        } catch (NotFoundException | NumberFormatException e) {
            sendNotFound(exchange);
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private boolean isPathSupported(String path) {
        return path.equals("/tasks") || path.matches("/tasks/\\d+");
    }

    private void handleGet(HttpExchange exchange) throws IOException, NotFoundException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/tasks")) {
            List<Task> tasks = taskManager.getAllTasks();
            sendSuccess(exchange, tasks);
        } else {
            int id = extractIdFromPath(path);
            Task task = taskManager.getTaskById(id);
            if (task != null) {
                sendSuccess(exchange, task);
            } else {
                throw new NotFoundException("Task not found with id: " + id);
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
                throw new NotFoundException("Task not found for update with id: " + task.getId());
            }
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException, NotFoundException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/tasks")) {
            taskManager.deleteAllTasks();
            sendCreated(exchange);
        } else {
            int id = extractIdFromPath(path);
            Task task = taskManager.getTaskById(id);
            if (task != null) {
                taskManager.deleteTaskById(id);
                sendCreated(exchange);
            } else {
                throw new NotFoundException("Task not found for deletion with id: " + id);
            }
        }
    }

    private int extractIdFromPath(String path) throws NumberFormatException {
        String[] parts = path.split("/");
        if (parts.length != 3) {
            throw new NumberFormatException("Invalid path format");
        }
        return Integer.parseInt(parts[2]);
    }
}