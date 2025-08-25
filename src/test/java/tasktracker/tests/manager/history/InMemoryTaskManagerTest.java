package tasktracker.tests.manager.history;

import tasktracker.manager.task.InMemoryTaskManager;
import tasktracker.model.Epic;
import tasktracker.model.Status;
import tasktracker.model.Subtask;
import tasktracker.model.Task;
import tasktracker.tests.manager.task.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void inMemoryManager_shouldHandleLargeNumberOfTasks() {
        for (int i = 1; i <= 1000; i++) {
            Task task = new Task(i, "Task " + i, "Description", Status.NEW);
            taskManager.createTask(task);
        }
        assertEquals(1000, taskManager.getAllTasks().size());
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

    @Test
    void createEpic_shouldCalculateStatus() {
        Epic epic = new Epic("Epic", "Description", 1);
        taskManager.createEpic(epic);
        assertEquals(Status.NEW, epic.getStatus(), "Новый эпик должен иметь статус NEW");
    }

    @Test
    void epicShouldNotContainItsOwnIdAsSubtask() {
        Epic epic = new Epic("Epic", "Description", 1);
        taskManager.createEpic(epic);

        epic.addSubtaskId(epic.getId());
        assertTrue(epic.getSubtaskIds().isEmpty(), "Эпик не должен содержать свой собственный ID как подзадачу");
    }

    @Test
    void createSubtask_shouldLinkToEpic() {
        Epic epic = new Epic("Epic", "Description", 1);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Description", 2, epic.getId());
        taskManager.createSubtask(subtask);

        assertTrue(epic.getSubtaskIds().contains(subtask.getId()));
    }

    @Test
    void subtaskShouldHaveValidEpicId() {
        Epic epic = new Epic("Epic", "Description", 1);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Description", 2, epic.getId());
        assertEquals(epic.getId(), subtask.getEpicId(), "Подзадача должна ссылаться на существующий эпик");
    }

    @Test
    void updateTaskStatus_shouldWorkCorrectly() {
        Task task = new Task("Task", "Description", 1);
        taskManager.createTask(task);

        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);

        assertEquals(Status.IN_PROGRESS, taskManager.getTaskById(task.getId()).getStatus());
    }

    @Test
    void updateSubtaskStatus_shouldUpdateEpicStatus() {
        Epic epic = new Epic("Epic", "Description", 1);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Description", 2, epic.getId());
        taskManager.createSubtask(subtask);

        subtask.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void tasksWithSameId_shouldBeEqual() {
        Task task1 = new Task("Task 1", "Description", 1);
        Task task2 = new Task(task1);
        assertEquals(task1, task2, "Задачи с одинаковым ID должны быть равны");
    }

    @Test
    void tasksWithDifferentIds_shouldNotBeEqual() {
        Task task1 = new Task("Task 1", "Description", 1);
        Task task2 = new Task("Task 2", "Description", 2);
        assertNotEquals(task1, task2, "Задачи с разными ID не должны быть равны");
    }
}