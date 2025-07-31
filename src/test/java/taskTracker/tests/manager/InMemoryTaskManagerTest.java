package tasktracker.tests.manager;

import tasktracker.manager.task.InMemoryTaskManager;
import tasktracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp(){
        taskManager = new InMemoryTaskManager();
    }

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
        Task task2 = new Task("Task 2", "Description", 1);
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