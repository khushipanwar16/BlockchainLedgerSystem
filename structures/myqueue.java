package structures;

public class MyQueue<T> {
    private T[] elements;
    private int front, rear, size, capacity;

    // Constructor to initialize the queue with a given capacity
    public MyQueue(int capacity) {
        this.capacity = capacity;
        elements = (T[]) new Object[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    // Check if the queue is full
    public boolean isFull() {
        return size == capacity;
    }

    // Check if the queue is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Add an element to the queue
    public void enqueue(T element) {
        if (isFull()) {
            System.out.println("Queue is full, cannot add more elements.");
            return;
        }
        rear = (rear + 1) % capacity;
        elements[rear] = element;
        size++;
    }

    // Remove and return the first element from the queue
    public T dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty, no element to dequeue.");
            return null;
        }
        T element = elements[front];
        front = (front + 1) % capacity;
        size--;
        return element;
    }

    // Get the current size of the queue
    public int size() {
        return size;
    }

    // View the front element without removing it
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return elements[front];
    }

    // Clear the entire queue
    public void clear() {
        front = 0;
        rear = -1;
        size = 0;
    }

    // Display all elements in the queue for debugging purposes
    public void display() {
        if (isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }
        for (int i = 0; i < size; i++) {
            int index = (front + i) % capacity;
            System.out.println(elements[index]);
        }
    }
}
