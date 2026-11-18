//Chengxiang Zhang
//czhang88@u.rochester.edu

// Import any package as required
import java.io.*;
import java.util.*;

class Node implements Comparable<Node> {
    char Cap1;
    int freq;
    Node left, right;

    Node(char Cap1, int freq, Node left, Node right) {
        this.Cap1 = Cap1;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }

    public boolean isLeaf() {
        return (left == null && right == null);
    }

    @Override
    public int compareTo(Node other) {
        return this.freq - other.freq;
    }
}

public class HuffmanSubmit implements Huffman {

    // Feel free to add more methods and variables as required.

    private final Map<Character, String> huffmanCodes = new HashMap<>();

    @Override
    public void encode(String inputFile, String outputFile, String freqFile) {
        try {
            Map<Character, Integer> frequencyMap = new HashMap<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                int Cap1;
                while ((Cap1 = reader.read()) != -1) {
                    frequencyMap.put((char) Cap1, frequencyMap.getOrDefault((char) Cap1, 0) + 1);
                }
            }

            PriorityQueue<Node> pq = new PriorityQueue<>();
            for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
                pq.add(new Node(entry.getKey(), entry.getValue(), null, null));
            }
            while (pq.size() > 1) {
                Node left = pq.poll();
                Node right = pq.poll();
                pq.add(new Node('\0', left.freq + right.freq, left, right));
            }
            Node root = pq.poll();

            buildHuffmanCodes(root, "");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(freqFile))) {
                for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
                    writer.write(String.format("%8s:%d%n", Integer.toBinaryString(entry.getKey()), entry.getValue()));
                }
            }

            BinaryOut binaryOut = new BinaryOut(outputFile);
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            try {
                int Cap1;
                while ((Cap1 = reader.read()) != -1) {
                    String code = huffmanCodes.get((char) Cap1);
                    for (char bit : code.toCharArray()) {
                        binaryOut.write(bit == '1');
                    }
                }
            } finally {
                binaryOut.flush();
                binaryOut.close();
                reader.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void decode(String inputFile, String outputFile, String freqFile) {
        try {
            Map<Character, Integer> frequencyMap = new HashMap<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(freqFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    char Cap1 = (char) Integer.parseInt(parts[0].trim(), 2);
                    int freq = Integer.parseInt(parts[1]);
                    frequencyMap.put(Cap1, freq);
                }
            }

            PriorityQueue<Node> pq = new PriorityQueue<>();
            for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
                pq.add(new Node(entry.getKey(), entry.getValue(), null, null));
            }
            while (pq.size() > 1) {
                Node left = pq.poll();
                Node right = pq.poll();
                pq.add(new Node('\0', left.freq + right.freq, left, right));
            }
            Node root = pq.poll();
            BinaryIn binaryIn = new BinaryIn(inputFile);
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            try {
                Node current = root;
                while (!binaryIn.isEmpty()) {
                    boolean bit = binaryIn.readBoolean();
                    current = bit ? current.right : current.left;

                    if (current.isLeaf()) {
                        writer.write(current.Cap1);
                        current = root;
                    }
                }
            } finally {
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildHuffmanCodes(Node node, String code) {
        if (node.isLeaf()) {
            huffmanCodes.put(node.Cap1, code);
            return;
        }
        buildHuffmanCodes(node.left, code + '0');
        buildHuffmanCodes(node.right, code + '1');
    }

    public static void main(String[] args) {
        Huffman huffman = new HuffmanSubmit();
        huffman.encode("alice30.txt", "alice30.enc", "freq.txt");
        huffman.decode("alice30.enc", "alice30_decoded.txt", "freq.txt");
        // After decoding, both ur.jpg and ur_dec.jpg should be the same. 
        // On linux and mac, you can use `diff' command to check if they are the same. 
    }
}
