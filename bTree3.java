// author: zedzeeden

public class bTree {
	
	bNode root = null; 
	private double DELTASIZE= 0.1; // Range for stacking balls of approximately the same size on top of one another 
	private boolean running;	
	private double xCenter;
	private double yCenter;
	private double PreviousSize;
	private double bSizeDifference;
	
	// Descends to the leaf node to add a new node, using a control statement 
	 
	public void addNode(aBall ball) { // the argument is of type aBall
		
		bNode current;

		// Empty B-tree		
		if (root == null) {
			root = makeNode(ball);
		}
		
		// If the B-tree is not empty, descend to the leaf node based on the input data  
		else {
			current = root;
			while (true) {
				
				// If the new data is less than the data held at node, branch left
				if (ball.getSize() < current.size) {
					
					// If the left node is empty, establish a new node here
					if (current.left == null) {			// leaf node
						current.left = makeNode(ball);	// attach new node here	
						break;
					}
					// If the left node is not empty, keep traversing
					else {									
						current = current.left;				
					}
				} 
				
				// If the new data is greater than or equal to the data held at node, branch right
				else {
					
					// If the right node is empty, establish a new node here
					if (current.right == null) {		// leaf node		
						current.right = makeNode(ball);	// attach new node here
						break;
					}
					
					// If the right node is not empty, keep traversing
					else {									
						current = current.right;			
					}
					
				} 
			} 
		} 
		
	} 
	
	// Creates a single instance of a bNode
	bNode makeNode(aBall ball) {
		bNode node = new bNode();	// create new object					
		node.size = ball.getSize();		// initialize data field					
		node.ball = ball;							
		// set both successors to null
		node.left = null;									
		node.right = null;									
		// return handle to new object 
		return node;										
	} 
	
	public void isRunningTraversal(bNode root) {

		if (root.left != null) isRunningTraversal(root.left);
		if (root.ball.running) running = true;
		if (root.right != null) isRunningTraversal(root.right);
		
	} 
	
	// Returns true if simulation still running
	public boolean isRunning() {	
		running = false;
		isRunningTraversal(root);
		return running;
	}
	
	// This method will move selected aBalls to their sorted position
	private void stackBallsTraversal(bNode root) {
		
		if (root.left != null) stackBallsTraversal(root.left);
		
		bSizeDifference = root.size - PreviousSize;
		
		// If the difference in ball size is more than DELTASIZE, move to the next stack
		if (bSizeDifference > DELTASIZE) {
			xCenter += PreviousSize*2;
			yCenter = bSim.HEIGHT;
		} else {
			
			// If the difference in ball size is less than or equal to DELTASIZE, go on top of the current stack
			yCenter -= bSim.SCALE*2*PreviousSize;
		}
		
		(root.ball).moveTo(bSim.SCALE*xCenter, (yCenter -bSim.SCALE*2*root.size));
		
		PreviousSize = root.size;

		if (root.right != null) stackBallsTraversal(root.right);
		
	} 
	
	void stackBalls() {	
		xCenter = 0;
		yCenter = bSim.HEIGHT;
		stackBallsTraversal(root);
	} 
	
} 

 // A simple bNode class for use by the B-Tree
 
	class bNode {
		aBall ball;
		double size;
		bNode left;
		bNode right;
} 