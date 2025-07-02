package taskTracker.tests;

import taskTracker.model.*;
import taskTracker.manager.task.TaskManager;
import taskTracker.manager.Managers;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private final TaskManager taskManager = Managers.getDefault();

    @Test
    void addTask_shouldSaveAndFindTaskById() {
        Task task = new Task("Task", "Description", 1);
        taskManager.createTask(task);
        Task savedTask = taskManager.getTaskById(1);
        assertEquals(task, savedTask, "Задача должна сохраняться и находиться по id.");
    }

    @Test
    void addTaskWithGeneratedId_shouldNotConflictWithManualId() {
        Task task1 = new Task("Task 1", "Description", 1);
        Task task2 = new Task("Task 2", "Description", 0); // id сгенерируется автоматически
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertNotEquals(task1.getId(), task2.getId(), "Автоматический и ручной id не должны конфликтовать.");
    }

    @Test
    void addTask_shouldNotModifyOriginalTask() {
        Task task = new Task("Original", "Description", 1);
        taskManager.createTask(task);
        Task savedTask = taskManager.getTaskById(1);
        assertEquals("Original", savedTask.getName(), "Название задачи не должно изменяться.");
        assertEquals("Description", savedTask.getDescription(), "Описание задачи не должно изменяться.");
    }
}