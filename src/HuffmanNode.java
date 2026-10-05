public class HuffmanNode {

    char character;
    int frequency;

    HuffmanNode left;
    HuffmanNode right;

    // Constructor for a leaf node
    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    // Constructor for an internal node
    public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
        this.character = '\0';
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    // Check whether this node is a leaf
    public boolean isLeaf() {
        return left == null && right == null;
    }
}