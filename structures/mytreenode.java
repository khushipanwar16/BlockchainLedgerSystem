package structures;

public class MyTreeNode<T> {
    public T data;
    public MyTreeNode<T> left, right;

  
    public MyTreeNode(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    // Link two nodes together
    public void linkNodes(MyTreeNode<T> left, MyTreeNode<T> right) {
        this.left = left;
        this.right = right;
    }

    // Display the node and its children (optional)
    public void display(String indent) {
        System.out.println(indent + "Node Data: " + data);
        if (left != null) {
            left.display(indent + "  ");
        }
        if (right != null) {
            right.display(indent + "  ");
        }
    }
}
