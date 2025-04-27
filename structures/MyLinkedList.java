package structures;

public class MyLinkedList<T> {

    // Node class (inner class)
    private class Node {
        T data;        // Data of generic type T
        Node next;     // Pointer to next node

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    // Head (start) and tail (end) of the list
    private Node head;
    private Node tail;
    private int size;

    // Constructor
    public MyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // Add element at the end
    public void add(T data) {
        Node newNode = new Node(data);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    // Get element by index
    public T get(int index) {
        if (index < 0 || index >= size) {
            System.out.println("Index out of bounds.");
            return null;
        }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.data;
    }

    // Get size of list
    public int size() {
        return size;
    }

    // Check if list is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Remove element by index (optional, if needed)
    public void remove(int index) {
        if (index < 0 || index >= size) {
            System.out.println("Index out of bounds.");
            return;
        }

        if (index == 0) {
            head = head.next;
            if (head == null) tail = null;
        } else {
            Node current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
            if (current.next == null) tail = current;
        }

        size--;
    }

    // Display list (for testing purpose)
    public void display() {
        Node current = head;
        while (current != null) {
            System.out.println(current.data.toString());
            current = current.next;
        }
    }
}
