package tasktracker.tests.manager.api;

import tasktracker.model.Task;
import tasktracker.model.Status;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTasksTest extends HttpTaskServerTestBase  {

    @Test
    void testCreateTask() throws Exception {
        Task task = new Task("Test Task", "Description", 0);
        task.setStatus(Status.NEW);
        String taskJson = gson.toJson(task.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals("Test Task", taskManager.getAllTasks().get(0).getName());
    }

    @Test
    void testGetTaskById() throws Exception {
        Task task = new Task("Test Task", "Description", 1);
        taskManager.createTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task responseTask = gson.fromJson(response.body(), Task.class);
        assertEquals("Test Task", responseTask.getName());
    }

    @Test
    void testGetAllTasks() throws Exception {
        taskManager.createTask(new Task("Task 1", "Desc", 1));
        taskManager.createTask(new Task("Task 2", "Desc", 2));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(2, tasks.length);
    }

    @Test
    void testDeleteTask() throws Exception {
        taskManager.createTask(new Task("Task", "Desc", 1));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void testTaskWithTime() throws Exception {
        Task task = new Task(0, "Timed Task", "Description", Status.NEW,
                Duration.ofHours(2), LocalDateTime.of(2023, 1, 1, 10, 0));
        String taskJson = gson.toJson(task.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task createdTask = taskManager.getAllTasks().get(0);
        assertNotNull(createdTask.getStartTime());
        assertNotNull(createdTask.getDuration());
    }
}
