package tasktracker.tests.model;

import taskTracker.model.Epic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EpicTest {

    @Test
    void equals_returnTrue_idAreSame() {
        Epic epic1 = new Epic("Epic 1", "Description 1", 1);
        Epic epic2 = new Epic("Epic 2", "Description 2", 1); // Такой же id, как у epic1

        assertEquals(epic1, epic2, "Эпики с одинаковым id должны быть равны.");
        assertEquals(epic1.hashCode(), epic2.hashCode(), "HashCode эпиков с одинаковым id должен совпадать.");
    }

    @Test
    void addSubtaskId_notAddId_IdSameWithEpicId() {
        Epic epic = new Epic("Epic", "Description", 1);
        epic.addSubtaskId(1);

        assertFalse(epic.getSubtaskIds().contains(1), "Эпик не должен содержать подзадачу с id, равным своему.");
    }
}