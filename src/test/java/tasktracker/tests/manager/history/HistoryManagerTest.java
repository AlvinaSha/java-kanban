package tasktracker.tests.manager.history;

import tasktracker.manager.history.InMemoryHistoryManager;
import tasktracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void add_shouldHandleEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой при создании.");
    }

    @Test
    void add_shouldPreventDuplicateTasks() {
        Task task = new Task("Task", "Description", 1);

        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size(), "Дубликаты не должны добавляться в историю.");
    }

    @Test
    void remove_shouldDeleteTaskFromBeginning() {
        Task task1 = new Task("Task 1", "Description", 1);
        Task task2 = new Task("Task 2", "Description", 2);
        Task task3 = new Task("Task 3", "Description", 3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "Должно остаться 2 задачи после удаления.");
        assertEquals(task2, history.get(0), "Первой должна быть вторая задача.");
        assertEquals(task3, history.get(1), "Второй должна быть третья задача.");
    }

    @Test
    void remove_shouldDeleteTaskFromMiddle() {
        Task task1 = new Task("Task 1", "Description", 1);
        Task task2 = new Task("Task 2", "Description", 2);
        Task task3 = new Task("Task 3", "Description", 3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "Должно остаться 2 задачи после удаления.");
        assertEquals(task1, history.get(0), "Первой должна быть первая задача.");
        assertEquals(task3, history.get(1), "Второй должна быть третья задача.");
    }

    @Test
    void remove_shouldDeleteTaskFromEnd() {
        Task task1 = new Task("Task 1", "Description", 1);
        Task task2 = new Task("Task 2", "Description", 2);
        Task task3 = new Task("Task 3", "Description", 3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(3);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "Должно остаться 2 задачи после удаления.");
        assertEquals(task1, history.get(0), "Первой должна быть первая задача.");
        assertEquals(task2, history.get(1), "Второй должна быть вторая задача.");
    }

    @Test
    void remove_shouldHandleNonExistentTaskId() {
        Task task = new Task("Task", "Description", 1);
        historyManager.add(task);

        assertDoesNotThrow(() -> historyManager.remove(999),
                "Удаление несуществующей задачи не должно вызывать исключение.");
        assertEquals(1, historyManager.getHistory().size(),
                "История не должна измениться при удалении несуществующей задачи.");
    }

    @Test
    void getHistory_shouldReturnCopyNotReference() {
        Task task = new Task("Task", "Description", 1);
        historyManager.add(task);

        List<Task> history1 = historyManager.getHistory();
        List<Task> history2 = historyManager.getHistory();

        assertNotSame(history1, history2, "Метод должен возвращать копию истории, а не ссылку.");
        assertEquals(history1, history2, "Копии должны содержать одинаковые задачи.");
    }

    @Test
    void add_shouldMaintainInsertionOrder() {
        Task task1 = new Task("Task 1", "Description", 1);
        Task task2 = new Task("Task 2", "Description", 2);
        Task task3 = new Task("Task 3", "Description", 3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size(), "Должно быть 3 задачи в истории.");
        assertEquals(task1, history.get(0), "Первая задача должна быть первой в истории.");
        assertEquals(task2, history.get(1), "Вторая задача должна быть второй в истории.");
        assertEquals(task3, history.get(2), "Третья задача должна быть третьей в истории.");
    }

    @Test
    void add_shouldMoveDuplicateToEnd() {
        Task task1 = new Task("Task 1", "Description", 1);
        Task task2 = new Task("Task 2", "Description", 2);
        Task task3 = new Task("Task 3", "Description", 3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task1); // Добавляем дубликат первой задачи

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size(), "Дубликат не должен увеличивать размер истории.");
        assertEquals(task2, history.get(0), "Первой должна быть вторая задача.");
        assertEquals(task3, history.get(1), "Второй должна быть третья задача.");
        assertEquals(task1, history.get(2), "Дубликат должен переместиться в конец.");
    }

    @Test
    void shouldHandleNullTask() {
        assertDoesNotThrow(() -> historyManager.add(null),
                "Добавление null задачи не должно вызывать исключение.");
        assertTrue(historyManager.getHistory().isEmpty(),
                "История должна остаться пустой после добавления null.");
    }

}