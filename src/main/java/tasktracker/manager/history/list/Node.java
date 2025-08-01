package tasktracker.manager.history.list;

public class Node<T> {

    Node<T> next;
    Node<T> prev;
    T data;

    public Node(Node<T> next, Node<T> prev, T data) {
        this.next = next;
        this.prev = prev;
        this.data = data;
    }
}
