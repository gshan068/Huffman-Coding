# Huffman-Coding

I created a HuffmanSubmit class that doing the decode and encode. In the HuffmanSubmit class, I used binaryIn to read the input file in the encode method but used bufferReader to read the frequency file in the decode method. 

In the encode method, I created freqFile to store the huffmancode and frequency. Then, I created huffman tree and huffman table. After going through the table, I can write out the output file. In the decode method, I read the freqFile first, then built up the huffman tree again using the freqFile. After the creation of the tree, go through the tree to get the character back, which is going to the left node if the code is 0, going to the right node if the code is 1 until the node is a leaf. I created a Node class below the HuffmanSubmit class for the priority queue node. I also created a node method in the HuffmanSubmit class, this can be used to build Huffman tree.

In the priority queue class, I created all methods that I need to used in the HuffmanSubmit, including constructor, size(), isLeaf(), leftchild(), rightchild(), add(), swap(), peek(), poll(), and siftdown().
