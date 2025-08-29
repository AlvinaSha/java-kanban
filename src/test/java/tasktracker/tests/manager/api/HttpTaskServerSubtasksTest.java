package tasktracker.tests.manager.api;

import tasktracker.model.Epic;
import tasktracker.model.Subtask;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerSubtasksTest extends HttpTaskServerTestBase {

    @Test
    void testCreateSubtask() throws Exception {
        Epic epic = new Epic("Epic", "Desc", 1);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Desc", 0, 1);
        String subtaskJson = gson.toJson(subtask.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(1, taskManager.getAllSubtasks().size());
    }
}
