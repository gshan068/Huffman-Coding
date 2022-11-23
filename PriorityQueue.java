//Name: Lishan Gao
//NetID: lgao14
// This is the class for priority queue, which I will used in the HuffmanSubmit class.

class PriorityQueue<E extends Comparable<E>> {
	E[] a;
	int length;
	
	// Constructors
	@SuppressWarnings("unchecked")
	public PriorityQueue(int capacity) { 
		a = (E[]) new Comparable[capacity];
		length = 0;
	}
		
	/*Return the comparator used to order the elements. 
	 * Return null if this queue is sorted.
	 I ordered them from the least to the biggest.
	 */
	public int size() {
		return length;
	}
	
	//if it is leaf, the index of the node is greater than or equal to length/2 and index need to be less than length.
	public boolean isLeaf(int index) {
		return index>=length/2 && index<length;
	}
	
	//Find out the lefthcild of the current node
	// If it is leftchild, the index for leftchild will be current index*2+1
	public int leftchild(int current) {
		return current*2+1;
		//need to smaller than length
	}
	
	//Find out rightchild of the current node.
	//If it is rightchild, the index for rightchild will be current index*2+2
	public int rightchild(int current) {
		return current*2+2;
	}
	
	//Find out the parent of the current node.
	public int parent(int current) {
		return (current-1)/2;
		//current need to greater than 0.
	}
	
	//Inserts the specified element into this priority queue.
	public boolean add(E e) {
		if(length==a.length) {
			return false;
		}
		int current = length++;
		a[current]=e;
		
		while(current>0 && a[current].compareTo(a[parent(current)]) < 0) {
			swap(current,parent(current));
			current = parent(current);
		}
		return true;
		
	}
	
	//this method is used to swap.
	public void swap(int in1,int in2) {
		E temp = a[in1];
		a[in1] = a[in2];
		a[in2] = temp;
	}
	
	// Return but does not remove item at top of stack
	public E peek() {
		return a[0];
	}
	
	//Retrieves and removes the head of the queue, or returns null if queue is empty
	public E poll() {
		swap(0,length-1);
		length--;
		if(length>0) {
			siftdown(0);
		}
		return a[length];
	}
		
	//Go through the nodes and swap nodes if they did not meet the priority queue rule, (from least to largest).
	private void siftdown(int current) {
		// While loop check if this is leaf. If it is leaf, cannot do siftdown. If not, go down and swap.
		while(!isLeaf(current)) { 	
			int c = leftchild(current);
			//If leftchild is greater than rightchild, c becomes rightchild. 
			if(c+1<length && a[c].compareTo(a[c+1])>0) { 
				c++;	
			}
			//If the current node is less than or equal to the leftchild of the current node, stop the loop.
			if(a[current].compareTo(a[c])<=0) {
				return;
			}
			//After checking all situations, swap the current node and the leftchild of current node. 
			//And the current index now becomes c.
			swap(current,c);
			current = c;
		}
	}
		
}

