package structures;

public class MyQueue<T> {
    private MyArray<T> elements;
    private int front, rear;

    public MyQueue() {
        elements = new MyArray<>(); 
        front = 0;
        rear = -1;
    }

    public boolean isEmpty() {
        return elements.size() == 0;
    }

    public void enqueue(T element) {
        elements.add(element); 
        rear++;
    }

    public T dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty, no element to dequeue.");
            return null;
        }
        T element = elements.get(front);  
       
        for (int i = 0; i < elements.size() - 1; i++) {
            elements.add(elements.get(i + 1)); 
        }
        elements.clear(); 
        rear--;
        return element;
    }

   
    public int size() {
        return elements.size();
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return elements.get(front);
    }

    public void clear() {
        elements.clear();
        front = 0;
        rear = -1;
    }

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
