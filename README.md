# Huffman Text Compressor

A Java-based desktop application that performs **lossless text compression and decompression using Huffman Coding**.

The project demonstrates the use of a **Greedy Algorithm**, **Priority Queue**, **Binary Tree**, and **file handling** through a polished graphical user interface.

---

## 📌 Project Overview

Huffman Coding is a lossless data compression algorithm that assigns shorter binary codes to frequently occurring characters and longer codes to less frequent characters.

This project provides a graphical interface where users can:

- Select a `.txt` file for compression
- Compress text using Huffman Coding
- Generate a `.huff` compressed file
- Select a `.huff` file for decompression
- Restore the original text
- View character frequencies
- View generated Huffman codes
- Visualize the Huffman Binary Tree
- View compression statistics
- View compression ratio
- View space saved
- Zoom and fit the Huffman Tree
- Reset the application
- View information about how the algorithm works
- View information about the project

---

## ✨ Features

### 1. Lossless Compression

The application compresses text without losing any information.

The decompressed output is identical to the original input.

### 2. Huffman Coding

The application creates a Huffman Tree based on character frequencies and generates a unique binary code for each character.

Example:

```text
Character     Frequency     Huffman Code

a             13            0111
e             19            000
t             12            0101

