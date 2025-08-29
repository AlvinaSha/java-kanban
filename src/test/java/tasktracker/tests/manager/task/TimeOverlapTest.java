package tasktracker.tests.manager.task;

import tasktracker.manager.task.InMemoryTaskManager;
import tasktracker.model.Epic;
import tasktracker.model.Status;
import tasktracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TimeOverlapTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }
    @Test
    void shouldDetectExactOverlap() {
        LocalDateTime time = LocalDateTime.of(2023, 1, 1, 10, 0);
        Task task1 = createTask(1, time, Duration.ofHours(2));
        Task task2 = createTask(2, time, Duration.ofHours(2));

        taskManager.createTask(task1);
        assertThrows(Exception.class, () -> taskManager.createTask(task2));
    }

    @Test
    void shouldDetectPartialOverlap() {
        Task task1 = createTask(1, LocalDateTime.of(2023, 1, 1, 10, 0), Duration.ofHours(2));
        Task task2 = createTask(2, LocalDateTime.of(2023, 1, 1, 11, 0), Duration.ofHours(2));

        taskManager.createTask(task1);
        assertThrows(Exception.class, () -> taskManager.createTask(task2));
    }

    @Test
    void shouldAllowNonOverlappingTasks() {
        Task task1 = createTask(1, LocalDateTime.of(2023, 1, 1, 10, 0), Duration.ofHours(1));
        Task task2 = createTask(2, LocalDateTime.of(2023, 1, 1, 12, 0), Duration.ofHours(1));

        taskManager.createTask(task1);
        assertDoesNotThrow(() -> taskManager.createTask(task2));
    }

    private Task createTask(int id, LocalDateTime startTime, Duration duration) {
        return new Task(id, "Task " + id, "Description", Status.NEW, duration, startTime);
    }
}