# Huffman Coding Compression

> Class Project · CSC 172, University of Rochester

Compresses files with Huffman coding — the classic lossless compression algorithm that assigns shorter bit codes to more frequent characters.

## What It Does

- Builds a frequency table from the input text.
- Constructs a Huffman tree from the frequencies.
- Encodes the text into a compact bit stream, and decodes it back to the original.

## Tech Stack

Java · Huffman coding · Binary trees · Priority queues

## How to Run

```bash
javac HuffmanSubmit.java
java HuffmanSubmit
```

## Files

| File | What it is |
| --- | --- |
| `HuffmanSubmit.java` | The full Huffman encoder and decoder |
