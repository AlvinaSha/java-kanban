package taskTracker.tests;

import taskTracker.model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskTest {

    @Test
    void equals_shouldReturnTrueIfIdsAreEqual() {
        Task task1 = new Task("Task 1", "Description", 1);
        Task task2 = new Task("Task 2", "Another Description", 1);
        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны.");
    }

    @Test
    void equals_shouldReturnFalseIfIdsAreDifferent() {
        Task task1 = new Task("Task 1", "Description", 1);
        Task task2 = new Task("Task 2", "Description", 2);
        assertNotEquals(task1, task2, "Задачи с разным id не должны быть равны.");
    }

    @Test
    void hashCode_shouldBeEqualIfIdsAreEqual() {
        Task task1 = new Task("Task 1", "Description", 1);
        Task task2 = new Task("Task 2", "Another Description", 1);
        assertEquals(task1.hashCode(), task2.hashCode(), "HashCode должен зависеть только от id.");
    }
}