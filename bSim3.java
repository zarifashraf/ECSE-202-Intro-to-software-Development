import java.awt.Color;
import java.awt.Font;
import acm.program.*;
import acm.graphics.*;
import java.awt.event.MouseEvent;
import acm.util.RandomGenerator;

/**
 * This is the main class from where the applet viewer and 
 * balls are created and the resulting simulation runs its course.
 * @author zedzeeden
 * 
*/

public class bSim extends GraphicsProgram {

	// n.b screen coordinates
	private static final int WIDTH = 1200;
	public static final int HEIGHT = 600;
	private static final int OFFSET = 200;
	
	public static final int SCALE = HEIGHT/100; // pixels per meter
	private static final int NUMBALLS = 60; // #balls to simulate
	private static final double MINSIZE = 1.0; // Minimum ball radius (meters)
	private static final double MAXSIZE = 7.0; // Maximum ball radius (meters)
	private static final double EMIN = 0.2; // Minimum loss coefficient
	private static final double EMAX = 0.6; // Maximum loss coefficient
	private static final double VoMIN = 40.0; // Minimum velocity (meters/s)
	private static final double VoMAX = 50.0; // Maximum velocity (meters/s)
	private static final double ThetaMIN = 80.0; // Minimum launch angle (degrees)
	private static final double ThetaMAX = 100.0; // Maximum launch angle (degrees)
	 
	private static final int XMID = WIDTH/SCALE/2; // X coordinate of midpoint 
	
	// Set up random number generator and B-tree
	private RandomGenerator rgen = RandomGenerator.getInstance();
	bTree myTree = new bTree();
	
	private static final int LABELXPOS = 500;
	private static final int LABELYPOS = 150;
	
	GLabel Message;
	GLabel CompletionMessage;
	
	public void run() {
		
		// Initialize applet viewer
		this.resize(WIDTH, HEIGHT + OFFSET);

		// Creates the ground plane for the ball
		GRect base = new GRect(0, HEIGHT , 2000, 2);	//sets the location of the ground plane on screen
		base.setFilled(true);							
		base.setColor(Color.DARK_GRAY);	//sets the color of the ground place to dark gray
		add(base);	//displays the ground plane on the applet viewer
		
		// produces the same sequence of psuedo-random numbers for each program run, terminating at exactly the same state
		rgen.setSeed((long) 424242);
		
			
		for (int i=0; i<NUMBALLS; i++) {
		
			// Generate random parameters
			double iSize = rgen.nextDouble(MINSIZE,MAXSIZE); // Current Size
	        Color iColor = rgen.nextColor(); // Current Color
	        double iLoss = rgen.nextDouble(EMIN,EMAX); // Current loss coefficient 
	        double iVel = rgen.nextDouble(VoMIN,VoMAX);  // current velocity
	        double iTheta = rgen.nextDouble(ThetaMIN,ThetaMAX); //current angle
	        
	        
	        aBall redBall = new aBall(XMID, iSize,iVel,iTheta,iSize,iColor,iLoss); 
			add(redBall.BallZ);
			myTree.addNode(redBall);
			redBall.start();
		}
		
		// Block until termination
		while(myTree.isRunning()) {
		}
		
		//Code to add a GLabel to the display
		Message = (new GLabel("CR to continue", LABELXPOS, LABELYPOS));
		Message.setFont(new Font("Monospaced", Font.BOLD, 30));
		Message.setColor(Color.ORANGE);
		add(Message);
		waitForClick(); // Wait
}

		public void init()	{
			addMouseListeners();
}

		public void mouseClicked(MouseEvent e) {
				remove(Message);
				myTree.stackBalls(); // Lay out balls in order
				CompletionMessage = new GLabel("All stacked", LABELXPOS, LABELYPOS);
				CompletionMessage.setFont(new Font("Monospaced", Font.BOLD, 30));
				CompletionMessage.setColor(Color.ORANGE);
				add(CompletionMessage);
}
}

