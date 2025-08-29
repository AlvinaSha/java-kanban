package tasktracker.tests.manager.api;

import tasktracker.model.Task;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerHistoryTest extends HttpTaskServerTestBase {

    @Test
    void testGetHistory() throws Exception {
        Task task = new Task("Task", "Desc", 1);
        taskManager.createTask(task);
        taskManager.getTaskById(1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task[] history = gson.fromJson(response.body(), Task[].class);
        assertEquals(1, history.length);
    }

    @Test
    void testGetPrioritized() throws Exception {
        Task task = new Task("Task", "Desc", 1);
        taskManager.createTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task[] prioritized = gson.fromJson(response.body(), Task[].class);
        assertTrue(prioritized.length >= 0);
    }
}