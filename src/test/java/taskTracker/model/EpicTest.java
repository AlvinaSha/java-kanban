package taskTracker.model;

import taskTracker.InMemoryTaskManager;
import taskTracker.Managers;
import taskTracker.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private TaskManager manager;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
    }

    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task("Task 1", "Description", 1);
        Task task2 = new Task("Task 2", "Different description", 1);
        assertEquals(task1, task2, "Задачи с одинаковым ID должны быть равны");
    }

    @Test
    void subtasksWithSameIdShouldBeEqual() {
        Epic epic = new Epic("Epic", "Description", 1);
        Subtask subtask1 = new Subtask("Sub 1", "Desc", 2, 1);
        Subtask subtask2 = new Subtask("Sub 2", "Different desc", 2, 1);
        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым ID должны быть равны");
    }

    @Test
    void epicsWithSameIdShouldBeEqual() {
        Epic epic1 = new Epic("Epic 1", "Desc", 1);
        Epic epic2 = new Epic("Epic 2", "Different desc", 1);
        assertEquals(epic1, epic2, "Эпики с одинаковым ID должны быть равны");
    }

    @Test
    void epicCannotAddItselfAsSubtask() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Subtask("Sub", "Desc", 1, 1);
        }, "Эпик не должен быть подзадачей самого себя");
    }

    @Test
    void managerInstanceShouldBeInitialized() {
        assertNotNull(manager, "Менеджер должен быть проинициализирован");
        assertTrue(manager instanceof InMemoryTaskManager, "Должна возвращаться конкретная реализация");
    }

    // 6. Проверка добавления и поиска задач
    @Test
    void shouldAddAndFindDifferentTaskTypes() {
        Task task = new Task("Task", "Description", 0);
        Epic epic = new Epic("Epic", "Description", 0);
        Subtask subtask = new Subtask("Sub", "Desc", 0, 2);

        Task createdTask = manager.createTask(task);
        Epic createdEpic = manager.createEpic(epic);
        Subtask createdSubtask = manager.createSubtask(subtask);

        assertEquals(task, manager.getTaskById(createdTask.getId()));
        assertEquals(epic, manager.getEpicById(createdEpic.getId()));
        assertEquals(subtask, manager.getSubtaskById(createdSubtask.getId()));
    }

    @Test
    void generatedAndManualIdsShouldNotConflict() {
        Task taskWithManualId = new Task("Manual", "Desc", 100);
        Task taskWithGeneratedId = new Task("Generated", "Desc", 0);

        manager.createTask(taskWithManualId);
        manager.createTask(taskWithGeneratedId);

        assertNotEquals(taskWithManualId.getId(), taskWithGeneratedId.getId(),
                "ID не должны конфликтовать");
        assertNotNull(manager.getTaskById(taskWithManualId.getId()));
        assertNotNull(manager.getTaskById(taskWithGeneratedId.getId()));
    }

    @Test
    void taskShouldRemainUnchangedWhenAddedToManager() {
        Task original = new Task("Original", "Desc", 0);
        original.setStatus(Status.IN_PROGRESS);

        Task created = manager.createTask(original);

        assertEquals("Original", created.getName());
        assertEquals("Desc", created.getDescription());
        assertEquals(Status.IN_PROGRESS, created.getStatus());
    }

    @Test
    void historyManagerShouldPreserveTaskState() {
        Task task = new Task("Task", "Desc", 0);
        Task created = manager.createTask(task);
        created.setStatus(Status.DONE);
        manager.updateTask(created);

        Task fromManager = manager.getTaskById(created.getId());
        List<Task> history = manager.getHistory();

        assertEquals(1, history.size());
        Task fromHistory = history.get(0);

        assertEquals(created.getId(), fromHistory.getId());
        assertEquals("Task", fromHistory.getName());
        assertEquals("Desc", fromHistory.getDescription());
        assertEquals(Status.DONE, fromHistory.getStatus());
    }
}