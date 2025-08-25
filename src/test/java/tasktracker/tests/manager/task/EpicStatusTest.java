package tasktracker.tests.manager.task;

import tasktracker.manager.task.InMemoryTaskManager;
import tasktracker.model.Epic;
import tasktracker.model.Status;
import tasktracker.model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EpicStatusTest extends TaskManagerTest<InMemoryTaskManager> {
    private Epic epic;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        epic = new Epic(1, "Test Epic", "Description");
        taskManager.createEpic(epic);
    }

    @Test
    void epicStatus_shouldBeNewWhenNoSubtasks() {
        assertEquals(Status.NEW, epic.getStatus(),
                "Эпик без подзадач должен иметь статус NEW");
    }

    @Test
    void epicStatus_shouldBeNewWhenAllSubtasksNew() {
        Subtask subtask1 = new Subtask(2, "Subtask 1", "Desc", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Desc", Status.NEW, epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.NEW, epic.getStatus(),
                "Эпик со всеми подзадачами NEW должен иметь статус NEW");
    }

    @Test
    void epicStatus_shouldBeDoneWhenAllSubtasksDone() {
        Subtask subtask1 = new Subtask(2, "Subtask 1", "Desc", Status.DONE, epic.getId());
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Desc", Status.DONE, epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.DONE, epic.getStatus(),
                "Эпик со всеми подзадачами DONE должен иметь статус DONE");
    }

    @Test
    void epicStatus_shouldBeInProgressWhenMixedNewAndDoneSubtasks() {
        Subtask subtask1 = new Subtask(2, "Subtask 1", "Desc", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Desc", Status.DONE, epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(),
                "Эпик с подзадачами NEW и DONE должен иметь статус IN_PROGRESS");
    }

    @Test
    void epicStatus_shouldBeInProgressWhenAnySubtaskInProgress() {
        Subtask subtask1 = new Subtask(2, "Subtask 1", "Desc", Status.IN_PROGRESS, epic.getId());
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Desc", Status.NEW, epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(),
                "Эпик с хотя бы одной подзадачей IN_PROGRESS должен иметь статус IN_PROGRESS");
    }

    @Test
    void epicStatus_shouldBeInProgressWhenAllSubtasksInProgress() {
        Subtask subtask1 = new Subtask(2, "Subtask 1", "Desc", Status.IN_PROGRESS, epic.getId());
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Desc", Status.IN_PROGRESS, epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(),
                "Эпик со всеми подзадачами IN_PROGRESS должен иметь статус IN_PROGRESS");
    }

    @Test
    void epicStatus_shouldUpdateWhenSubtaskStatusChanges() {
        Subtask subtask = new Subtask(2, "Subtask", "Desc", Status.NEW, epic.getId());
        taskManager.createSubtask(subtask);

        assertEquals(Status.NEW, epic.getStatus(),
                "Эпик должен иметь статус NEW после создания подзадачи");

        // Меняем статус подзадачи
        Subtask updatedSubtask = new Subtask(2, "Subtask", "Desc", Status.DONE, epic.getId());
        taskManager.updateSubtask(updatedSubtask);

        assertEquals(Status.DONE, epic.getStatus(),
                "Эпик должен обновить статус после изменения статуса подзадачи");
    }

    @Test
    void epicStatus_shouldUpdateWhenSubtaskRemoved() {
        Subtask subtask1 = new Subtask(2, "Subtask 1", "Desc", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask(3, "Subtask 2", "Desc", Status.DONE, epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(),
                "Эпик должен иметь статус IN_PROGRESS при разных статусах подзадач");

        // Удаляем одну подзадачу
        taskManager.deleteSubtaskById(2);

        assertEquals(Status.DONE, epic.getStatus(),
                "Эпик должен обновить статус после удаления подзадачи");
    }

    @Test
    void epicStatus_shouldBeNewWhenLastSubtaskRemoved() {
        Subtask subtask = new Subtask(2, "Subtask", "Desc", Status.DONE, epic.getId());
        taskManager.createSubtask(subtask);

        assertEquals(Status.DONE, epic.getStatus(),
                "Эпик должен иметь статус DONE с выполненной подзадачей");

        taskManager.deleteSubtaskById(2);

        assertEquals(Status.NEW, epic.getStatus(),
                "Эпик должен вернуться к статусу NEW после удаления всех подзадач");
    }
}