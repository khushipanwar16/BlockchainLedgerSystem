package structures;

public class MyQueue<T> {
    private MyArray<T> elements;
    private int front, rear;

    // Constructor to initialize the queue
    public MyQueue() {
        elements = new MyArray<>(); // Using custom MyArray with default capacity
        front = 0;
        rear = -1;
    }

    // Check if the queue is empty
    public boolean isEmpty() {
        return elements.size() == 0;
    }

    // Add an element to the queue (enqueue)
    public void enqueue(T element) {
        elements.add(element); // Add to the end of the list
        rear++;
    }

    // Remove and return the first element from the queue (dequeue)
    public T dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty, no element to dequeue.");
            return null;
        }
        T element = elements.get(front);  // Get the front element
        // Shift the elements to the left
        for (int i = 0; i < elements.size() - 1; i++) {
            elements.add(elements.get(i + 1));  // Shift elements left
        }
        elements.clear();  // Remove the last element (shifted)
        rear--;
        return element;
    }

    // Get the current size of the queue
    public int size() {
        return elements.size();
    }

    // View the front element without removing it
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return elements.get(front);
    }

    // Clear the entire queue
    public void clear() {
        elements.clear();
        front = 0;
        rear = -1;
    }

    // Display all elements in the queue for debugging purposes
    public void display() {
        if (isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }
        for (int i = front; i < elements.size(); i++) {
            System.out.println(elements.get(i));
        }
    }
}
