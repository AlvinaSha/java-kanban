package taskTracker.tests.model;

import taskTracker.model.Epic;
import taskTracker.model.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {

    @Test
    void equals_shouldReturnTrue_IfIdsAreEqual() {
        Epic epic = new Epic("Epic", "Description", 1);
        Subtask subtask1 = new Subtask("Subtask 1", "Description", 2, 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Another Description", 2, 1);
        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым id должны быть равны.");
    }

    @Test
    void equals_shouldReturnFalseIfIdsAreDifferent() {
        Epic epic = new Epic("Epic", "Description", 1);
        Subtask subtask1 = new Subtask("Subtask 1", "Description", 2, 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", 3, 1);
        assertNotEquals(subtask1, subtask2, "Подзадачи с разным id не должны быть равны.");
    }

    @Test
    void setId_notSetId_idSameWithEpicId() {

        Epic epic = new Epic("Epic", "Description", 1);
        Subtask subtask = new Subtask("Subtask", "Description", 2, 1); // epicId = 1

        subtask.setId(1);

        assertNotEquals(1, subtask.getId(), "Подзадача не должна принимать id, равный id её эпика.");
        assertEquals(2, subtask.getId(), "Подзадача должна сохранить исходный id.");
    }
}