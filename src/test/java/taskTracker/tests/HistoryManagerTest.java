package taskTracker.tests;

import taskTracker.model.Task;
import taskTracker.manager.history.HistoryManager;
import taskTracker.manager.Managers;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    private final HistoryManager historyManager = Managers.getDefaultHistory();

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
        Task savedTask = historyManager.getHistory().get(0);
        assertEquals(task.getName(), savedTask.getName(), "Название задачи должно сохраняться.");
        assertEquals(task.getDescription(), savedTask.getDescription(), "Описание задачи должно сохраняться.");
    }

    @Test
    void add_shouldNotExceedMaxSize() {
        for (int i = 1; i <= 15; i++) {
            Task task = new Task("Task " + i, "Description", i);
            historyManager.add(task);
        }
        assertEquals(10, historyManager.getHistory().size(), "История не должна превышать 10 элементов.");
    }
}