package tasktracker.manager.api.handler;

import com.sun.net.httpserver.HttpExchange;
import tasktracker.manager.exception.NotFoundException;
import tasktracker.manager.exception.TimeConflictException;
import tasktracker.manager.task.TaskManager;
import tasktracker.model.Subtask;

import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public SubtasksHandler(TaskManager taskManager) {
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
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (TimeConflictException e) {
            sendNotAcceptable(exchange);
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException, NotFoundException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/subtasks")) {
            List<Subtask> subtasks = taskManager.getAllSubtasks();
            sendSuccess(exchange, subtasks);
        } else {
            int id = extractIdFromPath(path);
            Subtask subtask = taskManager.getSubtaskById(id);
            sendSuccess(exchange, subtask);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException, NotFoundException, TimeConflictException {
        Subtask subtask = readJson(exchange, Subtask.class);

        if (subtask.getId() == 0) {
            Subtask created = taskManager.createSubtask(subtask);
            sendSuccess(exchange, created);
        } else {
            Subtask updated = taskManager.updateSubtask(subtask);
            sendSuccess(exchange, updated);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException, NotFoundException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/subtasks")) {
            taskManager.deleteAllSubtasks();
            sendCreated(exchange);
        } else {
            int id = extractIdFromPath(path);
            taskManager.deleteSubtaskById(id);
            sendCreated(exchange);
        }
    }

    private int extractIdFromPath(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[2]);
    }
}
