/*This is the HuffmanSubmit class that doing the decode and encode methods.
 * In the encode method, I created freqFile to store the huffmancode and frequency. Then, I created huffman tree and huffman
 * table. After going through the table, I can write out the output file. 
 * In the decode method, I read the freqFile first, then built up the huffman tree again using the freqFile. After the 
 * creation of the tree, go through the tree to get the character back, which is going to the left node if the code is 0,
 * going to the right node if the code is 1 until the node is a leaf. 
 * I created a Node class below the HuffmanSubmit class for the priority queue node.
*/

import java.io.*;  

public class HuffmanSubmit implements Huffman {
	
	public void encode(String inputFile, String outputFile, String freqFile){
		//Use BinaryIn class to read the input file.
		BinaryIn input = new BinaryIn(inputFile);
		//Create the freq array to store the characters in the input file. Each array index represent a corresponding to a 
		// character, then store the frequency into this array.
		int[] freq = new int[256];
		while(!input.isEmpty()) {
			freq[input.readChar()]++;
		}
		
		//Write out the frequency table in the freq.txt file.
		// The frequency table in format like 11001010:40
		BinaryOut freqF = new BinaryOut(freqFile);
		
		for(int i = 0; i<256;i++) {
			if(freq[i] != 0) {
				String str = Integer.toString(i,2);
				for(int j=0;j<8-str.length();j++) {
					freqF.write('0');
				}
				freqF.write(str);
				freqF.write(':');
				freqF.write(Integer.toString(freq[i]));
				freqF.write('\n');
			}
		}
		freqF.close();	//save the file, also do the flushing when close().
		
		//Create priority queue. Each node has character and corresponding frequency.
		PriorityQueue<Node> nodes = new PriorityQueue<Node>(256);
		for(char i=0;i<256;i++) {
			if(freq[i] != 0) {
				nodes.add(new Node(null,null,i,freq[i]));
			}
		}
		
		//Building huffman tree: Each time, adding two lowest frequency nodes into the huffman tree.
		while(nodes.size()>1) {
			Node min = nodes.poll();
			Node min2 = nodes.poll();
			nodes.add(new Node(min,min2,min.character,min.frequency+min2.frequency));
		}
		Node root = nodes.peek();
		
		//Build up the huffman table:
		String[] huffmanTable = new String[256];
		buildTable(root,"",huffmanTable);
		
		//Test code:
		 /*for(int i=0;i<256;i++) {
			if(huffmanTable[i] != null) {
				System.out.print(i);
				System.out.println(" "+huffmanTable[i]);
			}
		}
		*/
		
		
		//Use the huffman table to change the input file into output file and write out the output file as boolean.
		input = new BinaryIn(inputFile);
		BinaryOut output = new BinaryOut(outputFile);
		while(!input.isEmpty()) {
			String huffmanCode = huffmanTable[input.readChar()];
			for(int i=0;i<huffmanCode.length();i++) {
				if(huffmanCode.charAt(i) == '0') {
					output.write(false);
				}
				else {
					output.write(true);
				}
			}
		}
		output.close();
		
		
   }
	
	//This method can help to assign the huffmancode to each character and become a huffman table.
	private void buildTable(Node n,String label,String[] huffmanTable) {
		if(n.isLeaf()) {
			huffmanTable[n.character] = label;
		}
		else {
			buildTable(n.left,label+"0",huffmanTable);
			buildTable(n.right,label+"1",huffmanTable);

		}
	}

	
	
	
	public void decode(String inputFile, String outputFile, String freqFile){
		
		BinaryIn input = new BinaryIn(inputFile);
		
		//Create priority queue. Each node has character and corresponding frequency.
		PriorityQueue<Node> nodes = new PriorityQueue<Node>(256);
		//I used BufferedReader to read the freqFile line by line.
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(freqFile));
		    String line;
		    
		    while ((line = br.readLine()) != null) {
		    	String[] l = line.split(":"); //split by the :, and make them as array
		    	String character2=l[0]; 	// The string before : becomes the character2 string to store the character
			    String frequency = l[1];	//The string after : becomes the frequency string to store the frequency
		    	
		    	char character3 = (char) Integer.parseInt(character2,2);
				int freqInt = Integer.parseInt(frequency);
				nodes.add(new Node(null,null,character3,freqInt));
				
				
		    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//I created the huffman tree here.
		while(nodes.size()>1) {
			Node min = nodes.poll();
			Node min2 = nodes.poll();
			nodes.add(new Node(min,min2,min.character,min.frequency+min2.frequency));
		}
		Node root = nodes.peek();
		// only for testing purpose: (print out the node)
		//printPreOrder(root);
		
		BinaryOut output = new BinaryOut(outputFile);
		
		/*If the readBoolean from the input file is true, which also means it is 0, go to the leftchild of the current node.
		 *  If the readBoolean from the input file is false, which means it is 1, go to the rightchild of the current node.
		 *  Continue the process until the node is leaf node and write out the character into the output file. 
		 */
		Node current = root;
		while(!input.isEmpty()){
			if(input.readBoolean()) {
				current = current.right;
			}	
			else {
				current=current.left;
			}
			if(current.isLeaf()) {
				output.write(current.character); 
				current = root;
			}
		
		}
		output.close();
		
   }
  
	
	//only for testing and debug process:
	/*
	public static void printPreOrder(Node root) {
		System.out.println(root.character+":" + root.frequency);
		if(root.left != null) {
			printPreOrder(root.left);
		}
		if(root.right != null) {
			printPreOrder(root.right);
		}
	}
	*/
	


	public static void main(String[] args) {
      Huffman  huffman = new HuffmanSubmit();
		huffman.encode("alice30.txt", "alice30.enc", "freq.txt");
		huffman.decode("alice30.enc", "alice30_dec.txt", "freq.txt");
		huffman.encode("ur.jpg", "ur.enc", "freq2.txt"); 
		huffman.decode("ur.enc", "ur_dec.jpg", "freq2.txt");
		// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
		// On linux and mac, you can use `diff' command to check if they are the same. 
   }

}


class Node implements Comparable<Node>{
	Node left;
	Node right;
	char character;
	int frequency;
	
	public Node(Node l, Node r, char c, int f){
		left = l;
		right = r;
		character = c;
		frequency = f;
	}
	
	public boolean isLeaf() {
		return (this.left == null && this.right == null) ;
	}
	
	@Override
	public int compareTo(Node o) {
		if(frequency < o.frequency) {
			return -1;
		}
		else if(frequency > o.frequency){
			return 1;
		}
		else {
			if(character < o.character) {
				return -1;
			}
			else if(character >o.character) {
				return 1;
			}
		}
		return 0;
	}
	
}
