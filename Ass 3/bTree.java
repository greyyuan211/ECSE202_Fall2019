import acm.program.GraphicsProgram;

public class bTree extends GraphicsProgram{
	
	private static final int HEIGHT = 600; 
	private double lastSize,lastSize2;
	private double currentSize=0;
	private double X = 0;
	private double Y = HEIGHT;
	private static final double SCALE = HEIGHT/100;
	private static final double DELTASIZE = 0.1;
	bNode root=null;
	volatile Boolean running;
	
	class bNode {
		aBall data;
		aBall iBall;
		bNode left;
		bNode right;
	}
	
	//create a new method based on the in-order traversal routine that scans the B-tree and checks the status of each aBall
	public boolean isRunning() {
		running = false;
		checkRunning(root) ;
		return running;
	}
	
	//use recursing to check if left and right nodes are running
	public void checkRunning (bNode root) {
		if (root.data.b) running = true;
		if (root.left!=null)  checkRunning (root.left);
		if (root.right!=null)  checkRunning (root.right);
	}
	
	//based on the in-order traversal routine to move a ball to its sort order position instead of printing
	public void stackBalls() {
		traverse_inorder(root);
	}
	
	
	//modify the addNode method to accommodate the aBall object	
		public void addNode(aBall data) {
			
			bNode current;
	// Empty tree		
			if (root == null) {
				root = makeNode(data);
			}		
	// If not empty, descend to the leaf node according to
	// the input data.  		
			else {
				current = root;
				while (true) {
					if (data.getbSize() < current.data.getbSize()) {					
	// New data < data at node, branch left					
						if (current.left == null) {				// leaf node
							current.left = makeNode(data);		// attach new node here
							break;
						}
						else {									// otherwise
							current = current.left;				// keep traversing
						}
					}
					else {
	// New data >= data at node, branch right
						
						if (current.right == null) {			// leaf node	
							current.right = makeNode(data);		// attach
							break;
						}
						else {									// otherwise 
							current = current.right;			// keep traversing
						}
					}
				}
			}
			
		}
		
	
		
		bNode makeNode(aBall data) {
			bNode node = new bNode();							// create new object
			node.data = data;									// initialize data field
			node.left = null;									// set both successors
			node.right = null;									// to null
			return node;										// return handle to new object
		}
		
	//traverse_inorder method - recursively traverses tree in order (LEFT-Root-RIGHT) and prints each node.
		
		private void traverse_inorder(bNode root) {
			if (root.left != null) traverse_inorder(root.left);
			aBall oneBall=root.data;
			currentSize = root.data.getbSize();
			//stack balls according to Algorithm 1
			if(currentSize - lastSize > DELTASIZE) {

				X += SCALE*(lastSize2 + lastSize);//change currentSize to lastSize2 to make balls close to each other
				Y = HEIGHT - 2*SCALE*currentSize;
				oneBall.moveTo(X,Y);
				lastSize2 = currentSize;
			}else {
				Y -=SCALE*2*currentSize;
				oneBall.moveTo(X,Y);
			}
			lastSize = currentSize;
			if (root.right != null) traverse_inorder(root.right);
			
		}
	
}

