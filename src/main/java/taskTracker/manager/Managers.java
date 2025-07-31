package tasktracker.manager;

import tasktracker.manager.history.HistoryManager;
import tasktracker.manager.history.InMemoryHistoryManager;
import tasktracker.manager.task.InMemoryTaskManager;
import tasktracker.manager.task.TaskManager;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
