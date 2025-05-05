package structures;

public class MyMerkleTree<T> {
    private MyTreeNode<T> root;

    public MyMerkleTree(T[] elements) {
        MyTreeNode<T>[] nodes = new MyTreeNode[elements.length];

        for (int i = 0; i < elements.length; i++) {
            nodes[i] = new MyTreeNode<>(elements[i]);
        }

        while (nodes.length > 1) {
            int newLength = (nodes.length + 1) / 2;
            MyTreeNode<T>[] newLevel = new MyTreeNode[newLength];
            for (int i = 0; i < newLength; i++) {
                int leftIndex = 2 * i;
                int rightIndex = leftIndex + 1;

                MyTreeNode<T> left = nodes[leftIndex];
                MyTreeNode<T> right = (rightIndex < nodes.length) ? nodes[rightIndex] : left;

                T combinedData = combine(left.data, right.data);
                MyTreeNode<T> parent = new MyTreeNode<>(combinedData);
                parent.linkNodes(left, right);
                newLevel[i] = parent;
            }
            nodes = newLevel;
        }

        if (nodes.length > 0) {
            root = nodes[0];
        }
    }

    private T combine(T leftData, T rightData) {
        return (T) (leftData.toString() + rightData.toString());
    }

    public T getMerkleRoot() {
        if (root != null) {
            return root.data;
        }
        return null;
    }

    public void displayTree(MyTreeNode<T> node, String indent) {
        if (node != null) {
            System.out.println(indent + node.data);
            displayTree(node.left, indent + "  ");
            displayTree(node.right, indent + "  ");
        }
    }

    public void display() {
        displayTree(root, "");
    }
}
