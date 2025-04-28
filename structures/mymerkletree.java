package structures;

public class MyMerkleTree<T> {
    private MyTreeNode<T> root;

    // Build the Merkle Tree from a list of data elements (e.g., transaction hashes)
    public MyMerkleTree(T[] elements) {
        MyTreeNode<T>[] nodes = new MyTreeNode[elements.length];

        // Create leaf nodes for each element
        for (int i = 0; i < elements.length; i++) {
            nodes[i] = new MyTreeNode<>(elements[i]);
        }

        // Build the tree by combining pairs of nodes until a single root is formed
        while (nodes.length > 1) {
            int newLength = (nodes.length + 1) / 2;
            MyTreeNode<T>[] newLevel = new MyTreeNode[newLength];
            for (int i = 0; i < newLength; i++) {
                int leftIndex = 2 * i;
                int rightIndex = leftIndex + 1;

                // If there's an odd number of nodes, the last node gets duplicated
                MyTreeNode<T> left = nodes[leftIndex];
                MyTreeNode<T> right = (rightIndex < nodes.length) ? nodes[rightIndex] : left;

                // Combine nodes by concatenating their data (this could be modified to apply a real hash function)
                T combinedData = combine(left.data, right.data);
                MyTreeNode<T> parent = new MyTreeNode<>(combinedData);
                parent.linkNodes(left, right);
                newLevel[i] = parent;
            }
            nodes = newLevel;
        }

        // The root node will be the only remaining node in the list
        if (nodes.length > 0) {
            root = nodes[0];
        }
    }

    // Combine two elements into one (simple concatenation or hashing method)
    private T combine(T leftData, T rightData) {
        // For simplicity, this example concatenates the string representations of the data
        // Replace this logic with real hashing for cryptographic applications
        return (T) (leftData.toString() + rightData.toString());
    }

    // Get the root data of the Merkle Tree (Merkle Root)
    public T getMerkleRoot() {
        if (root != null) {
            return root.data;
        }
        return null;
    }

    // Recursively display the tree for debugging
    public void displayTree(MyTreeNode<T> node, String indent) {
        if (node != null) {
            System.out.println(indent + node.data);
            displayTree(node.left, indent + "  ");
            displayTree(node.right, indent + "  ");
        }
    }

    // Method to display the entire tree from root to leaf
    public void display() {
        displayTree(root, "");
    }
}
