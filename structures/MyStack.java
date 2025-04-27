package structures;

// Custom Stack class (LIFO: Last In First Out)
public class MyStack<T> {

    // Node class inside the Stack
    private class Node {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node top;   // The top item of the stack
    private int size;   // How many items are in the stack

    // Constructor: When the stack is created, it's empty
    public MyStack() {
        this.top = null;
        this.size = 0;
    }

    // Push: Add item onto the top of the stack
    public void push(T data) {
        Node newNode = new Node(data);
        newNode.next = top;  // New node points to the current top
        top = newNode;       // New node becomes the new top
        size++;
    }

    // Pop: Remove and return the top item
    public T pop() {
        if (isEmpty()) {
            System.out.println("❗ Stack is empty. Cannot pop.");
            return null;
        }

        T data = top.data;
        top = top.next;  // Move top to next node
        size--;
        return data;
    }

    // Peek: Just look at the top item without removing it
    public T peek() {
        if (isEmpty()) {
            System.out.println("❗ Stack is empty. Nothing to peek.");
            return null;
        }

        return top.data;
    }

    // Check if the stack is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Get the number of items in the stack
    public int size() {
        return size;
    }

    // Display all items in stack (from top to bottom)
    public void display() {
        Node current = top;
        while (current != null) {
            System.out.println(current.data.toString());
            current = current.next;
        }
    }
}
 