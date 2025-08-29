package tasktracker.manager.api.handler;

import com.sun.net.httpserver.HttpExchange;
import tasktracker.manager.task.TaskManager;
import tasktracker.model.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            List<Task> prioritized = taskManager.getPrioritizedTasks();
            sendSuccess(exchange, prioritized);
        } else {
            sendNotFound(exchange);
        }
    }
}