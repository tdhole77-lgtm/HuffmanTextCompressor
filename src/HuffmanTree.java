import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTree {

    private HuffmanNode root;
    private final Map<Character, String> huffmanCodes;

    public HuffmanTree(String text) {

        huffmanCodes = new HashMap<>();

        if (text == null || text.isEmpty()) {
            return;
        }

        buildTree(text);
        generateCodes(root, "");
    }

    private void buildTree(String text) {

        // Count character frequencies
        Map<Character, Integer> frequencyMap =
                new HashMap<>();

        for (char character : text.toCharArray()) {

            frequencyMap.put(
                    character,
                    frequencyMap.getOrDefault(character, 0) + 1
            );
        }

        // Priority Queue:
        // lowest frequency comes first
        PriorityQueue<HuffmanNode> priorityQueue =
                new PriorityQueue<>(
                        (a, b) -> Integer.compare(
                                a.frequency,
                                b.frequency
                        )
                );

        // Create one node for each character
        for (Map.Entry<Character, Integer> entry
                : frequencyMap.entrySet()) {

            priorityQueue.add(
                    new HuffmanNode(
                            entry.getKey(),
                            entry.getValue()
                    )
            );
        }

        // Build the Huffman tree
        while (priorityQueue.size() > 1) {

            HuffmanNode left =
                    priorityQueue.poll();

            HuffmanNode right =
                    priorityQueue.poll();

            HuffmanNode parent =
                    new HuffmanNode(
                            left.frequency + right.frequency,
                            left,
                            right
                    );

            priorityQueue.add(parent);
        }

        root = priorityQueue.poll();
    }

    private void generateCodes(
            HuffmanNode node,
            String code) {

        if (node == null) {
            return;
        }

        // Leaf node
        if (node.isLeaf()) {

            // Special case:
            // If there is only one unique character,
            // give it code "0".
            if (code.isEmpty()) {
                huffmanCodes.put(
                        node.character,
                        "0"
                );
            } else {
                huffmanCodes.put(
                        node.character,
                        code
                );
            }

            return;
        }

        // Left = 0
        generateCodes(
                node.left,
                code + "0"
        );

        // Right = 1
        generateCodes(
                node.right,
                code + "1"
        );
    }

    public Map<Character, String> getHuffmanCodes() {
        return huffmanCodes;
    }

    public HuffmanNode getRoot() {
        return root;
    }
}