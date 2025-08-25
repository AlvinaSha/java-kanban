package tasktracker.tests.manager.task;

import tasktracker.manager.task.TaskManager;
import tasktracker.model.Epic;
import tasktracker.model.Status;
import tasktracker.model.Subtask;
import tasktracker.model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @Test
    void createTask_shouldSaveAndFindTaskById() {
        Task task = new Task(1, "Task", "Description", Status.NEW);
        taskManager.createTask(task);
        Task savedTask = taskManager.getTaskById(1);
        assertEquals(task, savedTask);
    }

    @Test
    void updateTask_shouldChangeFields() {
        Task task = new Task(1, "Original", "Description", Status.NEW);
        taskManager.createTask(task);

        Task updated = new Task(1, "Updated", "New description", Status.DONE);
        taskManager.updateTask(updated);

        assertEquals("Updated", taskManager.getTaskById(1).getName());
        assertEquals(Status.DONE, taskManager.getTaskById(1).getStatus());
    }

    @Test
    void deleteTask_shouldRemoveFromManager() {
        Task task = new Task(1, "Task", "Description", Status.NEW);
        taskManager.createTask(task);
        taskManager.deleteTaskById(1);
        assertNull(taskManager.getTaskById(1));
    }

    // Тесты для Epic и Subtask
    @Test
    void createSubtask_shouldLinkToEpic() {
        Epic epic = new Epic(1, "Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask(2, "Subtask", "Description", 1);
        taskManager.createSubtask(subtask);

        assertTrue(epic.getSubtaskIds().contains(subtask.getId()));
    }

    @Test
    void epicStatus_shouldUpdateBasedOnSubtasks() {
        Epic epic = new Epic(1, "Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask(2, "Subtask 1", "Description", Status.NEW, 1);
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Description", Status.DONE, 1);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void deleteEpic_shouldRemoveSubtasks() {
        Epic epic = new Epic(1, "Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask(2, "Subtask", "Description", 1);
        taskManager.createSubtask(subtask);

        taskManager.deleteEpicById(1);

        assertNull(taskManager.getEpicById(1));
        assertNull(taskManager.getSubtaskById(2));
    }

    @Test
    void tasks_shouldPreventTimeOverlaps() {
        Task task1 = new Task(1, "Task 1", "Description", Status.NEW,
                Duration.ofHours(2), LocalDateTime.of(2023, 1, 1, 10, 0));

        Task task2 = new Task(2, "Task 2", "Description", Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2023, 1, 1, 11, 0));

        taskManager.createTask(task1);
        assertThrows(Exception.class, () -> taskManager.createTask(task2));
    }

}