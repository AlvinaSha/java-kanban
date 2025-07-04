package taskTracker.tests.manager;

import taskTracker.manager.history.InMemoryHistoryManager;
import taskTracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class HistoryManagerTest {
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp(){
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void add_shouldSaveTaskInHistory() {
        Task task = new Task("Task", "Description", 1);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Задача должна добавляться в историю.");
    }

    @Test
    void add_shouldPreserveTaskData() {
        Task task = new Task("Task", "Description", 1);
        historyManager.add(task);
        task.setName("Task2");
        Task savedTask = historyManager.getHistory().getFirst();
        assertNotEquals(task.getName(), savedTask.getName());
    }

    @Test
    void add_shouldNotExceedMaxSize() {
        for (int i = 1; i <= 15; i++) {
            Task task = new Task("Task " + i, "Description", i);
            historyManager.add(task);
        }
        assertEquals(10, historyManager.getHistory().size(), "История не должна превышать 10 элементов.");
    }

    @Test
    void add_changeTask_shouldNot() {
        for (int i = 1; i <= 15; i++) {
            Task task = new Task("Task " + i, "Description", i);
            historyManager.add(task);
        }
        assertEquals(10, historyManager.getHistory().size(), "История не должна превышать 10 элементов.");
    }
}