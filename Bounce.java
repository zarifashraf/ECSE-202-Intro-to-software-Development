import acm.program.*;
import acm.graphics.*;
import java.awt.color.*;
import java.awt.Color;

/**This program executes the the projectile motion of a ball which launches with an initial velocity at an angle,
 * and bounces upon impact with the ground. The ball loses energy upon each impact, and during its range of motion
 * due to air resistance. As soon as the kinetic energy of the ball depletes to zero, the ball stops bouncing.
 * 
 * @author zedzeeden
 *
 */
public class Bounce extends GraphicsProgram {

	// Parameters for the applet viewer
	private static final int WIDTH = 600; // defines the width of the screen in pixels
	private static final int HEIGHT = 600; // distance from top of screen to ground plane
	private static final int OFFSET = 200; // distance from bottom of screen to ground plane

	// Parameters for simulation
	private static final double g = 9.8; 				//MKS gravitational constant 9.8 m/s^2
	private static final double pi = 3.141592654; 		//To convert degrees to radians
	private static final double TICK = 0.1; 			// Clock tick duration (sec)
	private static final double ETHR = 0.01; 			// If either KEx or KEy< ETHR, STOP
	private static final double PD = 1; 				// Trace point diameter
	private static final double SCALE = HEIGHT/100; 	//Pixels/Meter
	private static final double k = 0.0016; 			// constant to be factored in during air resistance and terminal velocity calculations  
	private static final boolean TEST= true;			//print if TEST true

	public void run() {

		
		this.resize(WIDTH,HEIGHT + OFFSET);				//initialize applet viewer

		// Code to read simulation parameters from user
		double Vo = readDouble("Enter the initial velocity of the ball in m/s [0, 100]: "); 	// initial velocity in m/s
		double theta = readDouble("Enter the launch angle in degrees [0, 90]: "); 				// launch angle in degrees
		double loss = readDouble("Enter the energy loss parameter [0, 1]: "); 					//Energy loss due to collision with the ground
		double bSize = SCALE*readDouble("Enter the radius of the ball [0.1, 5.0]: "); 			// radius of the ball 

		println("Vo: " + Vo + ", theta: " + theta + ", loss: " + loss + ", BallSize: " + bSize +""); // displays variables with corresponding assigned values on the console window.
		
		pause(1000);
		
		// Setting (0,0) in pixel coordinates 
		double Xinit = 5*SCALE;
		double Yinit = HEIGHT - 2*bSize;

		// Screen coordinates initialized to initial position 
		double ScrX = Xinit;
		double ScrY = Yinit;
		
		
		//Creates the ground plane for the ball
		GLine base = new GLine(0, 600, 600, 600);			//sets the location of the ground plane on the screen
		base.setColor(Color.BLACK);							//sets the color of the ground plane to black
		add(base);											//displays the ground plane on the applet viewer
								
		// Creates an instance of the ball
		GOval BallZ = new GOval(ScrX, ScrY, 2*bSize, 2*bSize);		//sets the initial location of the ball
		BallZ.setColor(Color.PINK);									//sets the color of the ball to pink
		BallZ.setFilled(true);											//fills the ball with the assigned color
		add(BallZ);													//adds the ball to the applet viewer
	

		// Initialize variables
		double t = 0;
		double Vt = g/(4*pi*bSize*bSize/SCALE/SCALE*k); 				// Terminal velocity
		double Vox = Vo*Math.cos(theta*pi/180); 						// X component of initial velocity
		double Voy = Vo*Math.sin(theta*pi/180); 						// Y component of initial velocity
		double X = 0;													//horizontal component of displacement
		double Y = bSize/SCALE;											//vertical component of displacement
		double Vx = Vox;												// horizontal component of the velocity
		double Vy = Voy;												// vertical component of the velocity
		double XLast;												
		double YLast;
		double Xo = 0;
		
		pause(500);

		
		// Simulation loop
		while (true) {

			XLast = X;
			YLast = Y;

			X = Vox*Vt/g*(1-Math.exp(-g*t/Vt)); 									// X position
			Y = bSize/SCALE + Vt/g*(Voy+Vt)*(1-Math.exp(-g*t/Vt))-Vt*t; 			// Y position

			
			Vx = (X-XLast)/TICK;								// Estimate Vx from difference
			Vy = (Y-YLast)/TICK;								//Estimate Vy from difference

			// check to see if we've hit the ground
			// If yes, inject energy loss
			
			if ((Vy<0) && (Y<=bSize/SCALE)) {
				
				// Offset for next parabola
				Xo += X;

			
				double KEx = 0.5*Vx*Vx*(1-loss); 				// Kinetic energy in X direction after collision
				double KEy = 0.5*Vy*Vy*(1-loss); 				// Kinetic energy in Y direction after collision
				
				// Termination condition - energy depletes to zero
				if ((KEx <= ETHR) | (KEy <= ETHR)) {
					break;
				}

				Vox = Math.sqrt(2*KEx); 						// Resulting horizontal velocity
				Voy = Math.sqrt(2*KEy); 						// Resulting vertical velocity

				// Restarting for new parabola
				X = 0;
				Y = bSize/SCALE;
				t = 0;

				Vx = (X-XLast)/TICK;							//Estimate Vx from difference
				Vy = (Y-YLast)/TICK;							//Estimate Vy from difference
			
			}
			// Creating the trace points
			GOval points = new GOval(ScrX+bSize, ScrY+bSize, PD, PD);		//initialize the location of the trace points
			points.setColor(Color.PINK);									//sets the color of the trace points to pink	
			points.setFilled(true);											//fills the trace point with the assigned color
			add(points);													//displays the trace points on the applet viewer


			// Display Update
			ScrX = (int) (X*SCALE + Xinit + Xo*SCALE);
			ScrY = (int) (HEIGHT-(Y*SCALE+bSize));
			BallZ.setLocation(ScrX, ScrY);					//Screen Units
			
			pause(50);

			
			if(TEST)
			System.out.printf("t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy:%.2f\n", t, Xo+X, Y, Vx, Vy);
			t += TICK;
		
		} 

	}

} 

  