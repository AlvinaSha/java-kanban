package taskTracker.manager.history.list;

import taskTracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskHistoryLinkedList {

    private final Map<Integer, Node<Task>> table = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    public void linkLast(Task task) {
        Node<Task> newNode = new Node<>(null, tail, task);
        removeNode(table.get(task.getId()));
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }

        tail = newNode;
        table.put(task.getId(),newNode);
    }

    public void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> prev = node.prev;
            final Node<Task> next = node.next;
            table.remove(node.data.getId());
            if (prev == null && next == null) {
                head = null;
                tail = null;
            } else if (next == null) {
                tail = prev;
                tail.next = null;
            } else if (prev == null) {
                head = next;
                head.prev = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>(table.size());
        Node<Task> node = head;
        while (node != null) {
            tasks.add(node.data);
            node = node.next;
        }
        return tasks;
    }

    public Node<Task> getNode(int id) {
        return table.get(id);
    }
}
