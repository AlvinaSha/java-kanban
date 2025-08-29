package tasktracker.tests.manager.api;

import tasktracker.manager.api.HttpTaskServer;
import tasktracker.manager.task.InMemoryTaskManager;
import tasktracker.manager.task.TaskManager;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.http.HttpClient;

public abstract class HttpTaskServerTestBase {
    protected TaskManager taskManager;
    protected HttpTaskServer taskServer;
    protected Gson gson;
    protected HttpClient client;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer();
        gson = new Gson();
        client = HttpClient.newHttpClient();
        taskServer.start();

        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }
}