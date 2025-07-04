package taskTracker.manager;

import taskTracker.manager.history.HistoryManager;
import taskTracker.manager.history.InMemoryHistoryManager;
import taskTracker.manager.task.InMemoryTaskManager;
import taskTracker.manager.task.TaskManager;

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
