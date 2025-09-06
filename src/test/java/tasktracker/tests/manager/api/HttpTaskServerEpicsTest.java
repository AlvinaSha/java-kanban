package tasktracker.tests.manager.api;

import tasktracker.model.Epic;
import tasktracker.model.Subtask;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerEpicsTest extends HttpTaskServerTestBase {

    @Test
    void testCreateEpic() throws Exception {
        Epic epic = new Epic("Test Epic", "Description", 0);
        String epicJson = gson.toJson(epic.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    void testGetEpicSubtasks() throws Exception {
        Epic epic = new Epic("Epic", "Desc", 1);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Desc", 2, 1);
        taskManager.createSubtask(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Subtask[] subtasks = gson.fromJson(response.body(), Subtask[].class);
        assertEquals(1, subtasks.length);
    }
}
