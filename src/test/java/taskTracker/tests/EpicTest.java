package taskTracker.tests;

import taskTracker.model.Epic;
import taskTracker.model.Status;
import taskTracker.model.Subtask;
import org.junit.jupiter.api.Test;

public class EpicTest {

    @Test
    void shouldUpdateStatusBasedOnSubtasks() {
        Epic epic = new Epic("Epic", "Description", 1);
        Subtask subtask1 = new Subtask("Subtask 1", "Description", 2, 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", 3, 1);

        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);

    }
}