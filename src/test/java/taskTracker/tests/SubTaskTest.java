package taskTracker.tests;

import taskTracker.model.Epic;
import taskTracker.model.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {

    @Test
    void equals_shouldReturnTrueIfIdsAreEqual() {
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

}