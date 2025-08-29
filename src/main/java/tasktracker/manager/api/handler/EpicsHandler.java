package tasktracker.manager.api.handler;

import com.sun.net.httpserver.HttpExchange;
import tasktracker.manager.exception.NotFoundException;
import tasktracker.manager.task.TaskManager;
import tasktracker.model.Epic;
import tasktracker.model.Subtask;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
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
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException, NotFoundException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/epics")) {
            List<Epic> epics = taskManager.getAllEpics();
            sendSuccess(exchange, epics);
        } else if (path.contains("/subtasks")) {
            handleGetEpicSubtasks(exchange);
        } else {
            int id = extractIdFromPath(path);
            Epic epic = taskManager.getEpicById(id);
            sendSuccess(exchange, epic);
        }
    }

    private void handleGetEpicSubtasks(HttpExchange exchange) throws IOException, NotFoundException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");

        if (parts.length == 4 && "subtasks".equals(parts[3])) {
            int epicId = Integer.parseInt(parts[2]);
            List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epicId);
            sendSuccess(exchange, subtasks);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException, NotFoundException {
        Epic epic = readJson(exchange, Epic.class);

        if (epic.getId() == 0) {
            Epic created = taskManager.createEpic(epic);
            sendSuccess(exchange, created);
        } else {
            Epic updated = taskManager.updateEpic(epic);
            sendSuccess(exchange, updated);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException, NotFoundException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/epics")) {
            taskManager.deleteAllEpics();
            sendCreated(exchange);
        } else {
            int id = extractIdFromPath(path);
            taskManager.deleteEpicById(id);
            sendCreated(exchange);
        }
    }

    private int extractIdFromPath(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[2]);
    }
}