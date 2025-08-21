package tasktracker.tests.manager.history;

import tasktracker.manager.history.InMemoryHistoryManager;
import tasktracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryManagerTest {
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void add_shouldSaveTaskInHistory() {
        Task task = new Task("Task", "Description", 1);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Задача должна добавляться в историю.");
    }

}