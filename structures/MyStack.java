package structures;

public class MyStack<T> {

    private class Node {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node top;   
    private int size;   

    public MyStack() {
        this.top = null;
        this.size = 0;
    }

    public void push(T data) {
        Node newNode = new Node(data);
        newNode.next = top;  
        top = newNode;      
        size++;
    }

    public T pop() {
        if (isEmpty()) {
            System.out.println("❗ Stack is empty. Cannot pop.");
            return null;
        }

        T data = top.data;
        top = top.next; 
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            System.out.println("❗ Stack is empty. Nothing to peek.");
            return null;
        }

        return top.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void display() {
        Node current = top;
        while (current != null) {
            System.out.println(current.data.toString());
            current = current.next;
        }
    }
}
 