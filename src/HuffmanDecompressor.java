import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class HuffmanDecompressor {

    public static void decompress(
            String inputFile,
            String outputFile) throws IOException {

        try (DataInputStream input =
                     new DataInputStream(
                             new BufferedInputStream(
                                     new FileInputStream(inputFile)))) {

            // Check file identifier
            String identifier = input.readUTF();

            if (!"HUFFMAN".equals(identifier)) {
                throw new IOException(
                        "Invalid Huffman file."
                );
            }

            // Read number of unique characters
            int uniqueCharacters =
                    input.readInt();

            if (uniqueCharacters <= 0) {
                throw new IOException(
                        "Invalid Huffman frequency table."
                );
            }

            // Rebuild frequency map
            Map<Character, Integer> frequencyMap =
                    new HashMap<>();

            for (int i = 0;
                 i < uniqueCharacters;
                 i++) {

                char character =
                        input.readChar();

                int frequency =
                        input.readInt();

                frequencyMap.put(
                        character,
                        frequency
                );
            }

            // Read number of valid bits
            int validBits =
                    input.readInt();

            if (validBits < 0) {
                throw new IOException(
                        "Invalid bit count."
                );
            }

            // Rebuild Huffman tree
            HuffmanNode root =
                    buildTree(frequencyMap);

            if (root == null) {
                throw new IOException(
                        "Unable to rebuild Huffman tree."
                );
            }

            // Read encoded bytes
            StringBuilder encodedData =
                    new StringBuilder();

            while (input.available() > 0) {

                int value =
                        input.readUnsignedByte();

                String bits =
                        String.format(
                                "%8s",
                                Integer.toBinaryString(value)
                        ).replace(' ', '0');

                encodedData.append(bits);
            }

            // Make sure we don't process padding bits
            if (encodedData.length() > validBits) {

                encodedData.setLength(
                        validBits
                );
            }

            StringBuilder decodedText =
                    new StringBuilder();

            // Special case:
            // only one unique character
            if (root.isLeaf()) {

                for (int i = 0;
                     i < validBits;
                     i++) {

                    decodedText.append(
                            root.character
                    );
                }

            } else {

                HuffmanNode current =
                        root;

                for (int i = 0;
                     i < encodedData.length();
                     i++) {

                    char bit =
                            encodedData.charAt(i);

                    if (bit == '0') {
                        current =
                                current.left;
                    } else if (bit == '1') {
                        current =
                                current.right;
                    } else {
                        throw new IOException(
                                "Invalid encoded bit."
                        );
                    }

                    if (current == null) {
                        throw new IOException(
                                "Invalid Huffman data."
                        );
                    }

                    // Reached a character
                    if (current.isLeaf()) {

                        decodedText.append(
                                current.character
                        );

                        current = root;
                    }
                }
            }

            // Write decoded text
            Files.writeString(
                    Path.of(outputFile),
                    decodedText.toString()
            );
        }

        System.out.println(
                "Decompression completed!"
        );

        System.out.println(
                "Output file: " + outputFile
        );
    }

    /**
     * Rebuild the Huffman tree from the
     * stored frequency table.
     */
    private static HuffmanNode buildTree(
            Map<Character, Integer> frequencyMap) {

        java.util.PriorityQueue<HuffmanNode>
                priorityQueue =
                new java.util.PriorityQueue<>(
                        (a, b) -> Integer.compare(
                                a.frequency,
                                b.frequency
                        )
                );

        for (Map.Entry<Character, Integer> entry
                : frequencyMap.entrySet()) {

            priorityQueue.add(
                    new HuffmanNode(
                            entry.getKey(),
                            entry.getValue()
                    )
            );
        }

        while (priorityQueue.size() > 1) {

            HuffmanNode left =
                    priorityQueue.poll();

            HuffmanNode right =
                    priorityQueue.poll();

            HuffmanNode parent =
                    new HuffmanNode(
                            left.frequency
                                    + right.frequency,
                            left,
                            right
                    );

            priorityQueue.add(parent);
        }

        return priorityQueue.poll();
    }

    /**
     * Compare the original file with the
     * decompressed file.
     *
     * Returns true when both files contain
     * exactly the same bytes.
     */
    public static boolean verifyDecompression(
            String originalFile,
            String decompressedFile)
            throws IOException {

        byte[] original =
                Files.readAllBytes(
                        Path.of(originalFile)
                );

        byte[] decompressed =
                Files.readAllBytes(
                        Path.of(decompressedFile)
                );

        return java.util.Arrays.equals(
                original,
                decompressed
        );
    }
}