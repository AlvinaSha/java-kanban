package taskTracker.manager.history;

import taskTracker.manager.history.list.TaskHistoryLinkedList;
import taskTracker.model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final TaskHistoryLinkedList taskHistoryLinkedList = new TaskHistoryLinkedList();

    @Override
    public void add(Task task) {
        if (task != null) {
            taskHistoryLinkedList.linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        taskHistoryLinkedList.removeNode(taskHistoryLinkedList.getNode(id));
    }


    @Override
    public List<Task> getHistory() {
        return taskHistoryLinkedList.getTasks();
    }

}
