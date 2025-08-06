package tasktracker.manager.history.list;

import tasktracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskHistoryLinkedList {

    private final Map<Integer, Node<Task>> table = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    public void remove(int id) {
        removeNode(table.get(id));
    }

    public void linkLast(Task task) {
        Node<Task> newNode = new Node<>(null, tail, task);
        removeNode(table.get(task.getId()));
        if (tail == null) {
            head = newNode;
        } else {
            tail.setNext(newNode);
        }

        tail = newNode;
        table.put(task.getId(),newNode);
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> prev = node.getPrev();
            final Node<Task> next = node.getNext();
            table.remove(node.getData().getId());
            if (prev == null && next == null) {
                head = null;
                tail = null;
            } else if (next == null) {
                tail = prev;
                tail.setNext(null);
            } else if (prev == null) {
                head = next;
                head.setPrev(null);
            } else {
                prev.setNext(next);
                next.setPrev(prev);
            }
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>(table.size());
        Node<Task> node = head;
        while (node != null) {
            tasks.add(node.getData());
            node = node.getNext();
        }
        return tasks;
    }

    public Node<Task> getNode(int id) {
        return table.get(id);
    }
}
