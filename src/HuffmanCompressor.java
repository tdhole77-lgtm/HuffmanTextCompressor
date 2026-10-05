import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class HuffmanCompressor {

    public static void compress(
            String inputFile,
            String outputFile) throws IOException {

        // Read input file
        String text =
                Files.readString(
                        Path.of(inputFile),
                        StandardCharsets.UTF_8
                );

        if (text.isEmpty()) {

            throw new IOException(
                    "The input file is empty."
            );
        }


        // -----------------------------
        // COUNT FREQUENCIES
        // -----------------------------

        Map<Character, Integer>
                frequencyMap =
                new HashMap<>();

        for (char character :
                text.toCharArray()) {

            frequencyMap.put(
                    character,
                    frequencyMap.getOrDefault(
                            character,
                            0
                    ) + 1
            );
        }


        // -----------------------------
        // BUILD HUFFMAN TREE
        // -----------------------------

        HuffmanTree tree =
                new HuffmanTree(text);


        Map<Character, String>
                codes =
                tree.getHuffmanCodes();


        // -----------------------------
        // ENCODE TEXT
        // -----------------------------

        StringBuilder encodedData =
                new StringBuilder();

        for (char character :
                text.toCharArray()) {

            String code =
                    codes.get(character);

            if (code == null) {

                throw new IOException(
                        "No Huffman code found for character."
                );
            }

            encodedData.append(code);
        }


        // -----------------------------
        // WRITE COMPRESSED FILE
        // -----------------------------

        try (DataOutputStream output =
                     new DataOutputStream(
                             new BufferedOutputStream(
                                     new FileOutputStream(
                                             outputFile
                                     )
                             )
                     )) {


            // File identifier
            output.writeUTF(
                    "HUFFMAN"
            );


            // Number of unique characters
            output.writeInt(
                    frequencyMap.size()
            );


            // Store frequencies
            for (Map.Entry<Character, Integer>
                    entry :
                    frequencyMap.entrySet()) {

                output.writeChar(
                        entry.getKey()
                );

                output.writeInt(
                        entry.getValue()
                );
            }


            // Store number of valid bits
            output.writeInt(
                    encodedData.length()
            );


            // Convert bits to bytes
            for (int i = 0;
                 i < encodedData.length();
                 i += 8) {

                int end =
                        Math.min(
                                i + 8,
                                encodedData.length()
                        );

                String byteString =
                        encodedData.substring(
                                i,
                                end
                        );


                // Pad final byte with zeroes
                while (byteString.length() < 8) {

                    byteString += "0";
                }


                int value =
                        Integer.parseInt(
                                byteString,
                                2
                        );

                output.writeByte(
                        value
                );
            }
        }


        System.out.println(
                "Compression completed!"
        );

        System.out.println(
                "Output file: "
                        + outputFile
        );
    }
}