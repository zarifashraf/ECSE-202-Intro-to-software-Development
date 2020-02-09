import java.awt.Color;
import acm.graphics.*;

/**
 * This class provides the single instance of a ball launch from the center of the ground 
 * at an initial angle. Because it is an extension of the thread class, each instance
 * will run concurrently, with animations on the screen as a side effect.
 * @author zedzeeden
 *
 */

public class aBall extends Thread {

	// Parameters for simulation
	double Xi; // The initial X position of the center of the ball
	double Yi; // The initial Y position of the center of the ball
	double Vo; // The initial velocity of the ball at launch
	double theta; // launch angle (with the horizontal plane
	double bSize; // The radius of the ball in simulation units
	Color bColor; // The initial color of the ball
	double bLoss; // Fraction [0,1] of the energy lost on each bounce
	
	GOval myBall;
	GOval BallZ;

	// Preset parameters
	private static final int HEIGHT = 600; //// distance from top of screen to ground plane
	private static final int SCALE = HEIGHT/100; // Pixels/meter
	private static final double Pi = 3.141592654; //To convert degrees to radians
	private static final double k = 0.0001; // constant to be factored in during air resistance and terminal velocity calculations 
	private static final double g = 9.8; // MKS gravitational constant 9.8 m/s^2
	private static final double TICK = 0.1; // Clock tick duration (sec)
	private static final double ETHR = 0.01; // If (KEx+ KEy < ETHR), STOP!
	
	public aBall (double Xi, double Yi, double Vo, double theta, double bSize, Color bColor, double bLoss) {

		//Get Simulation parameters
		this.Xi = Xi; 
		this.Yi = Yi; 
		this.Vo = Vo; 
		this.theta = theta; 
		this.bSize = bSize; 
		this.bColor = bColor; 
		this.bLoss = bLoss;
		
		//creates an instance of the ball
		myBall = new GOval(Xi*SCALE, HEIGHT - Yi*SCALE + SCALE*bSize, SCALE*bSize*2, SCALE*bSize*2); 
		myBall.setFilled(true);	
		myBall.setColor(this.bColor);
		
		this.BallZ = myBall;
		
	}	
	
	// creates the ball 'getter'
	public GOval getBall() { 

		return this.BallZ;

	}

	/**
	* The run method executes the simulation of a ball which launches with an initial velocity at an angle, and bounces 
	* upon impact with the ground. The ball loses energy upon each impact, and during its range of motion due to air 
	* resistance. As soon as the kinetic energy of the ball depletes to zero, the ball stops bouncing. 
	* Once the start method is called on the aBall instance, the code in the run method is executed concurrently with the main program.
	* @param void
	* @return void
	*/
	public void run() {
		
		// Initialize variables
		double t = 0;
		double Vt = g/(4*Pi*this.bSize*this.bSize*k); // Terminal velocity
		double Vox = this.Vo*Math.cos(this.theta*Pi/180); // X component of initial velocity
		double Voy = this.Vo*Math.sin(this.theta*Pi/180); // Y component of initial velocity
		double X = this.Xi;
		double Y = this.Yi;
		double Xlast, Ylast;
		double Xo = 0;
		double Vx = Vox;
		double Vy = Voy;
		double KEx = 0.5*Vx*Vx; // Kinetic energy in X direction 
		double KEy = 0.5*Vy*Vy; // Kinetic energy in Y direction 
		double KEt = KEx + KEy; // Total energy
		double KEp = KEx + KEy+1; // Previous total energy
		double ScrX, ScrY;
		
		// Initial position (in m)
		double Xinit = X;
		double Yinit = Y;
		
		// Simulation loop
		while ((KEt > ETHR) && (KEt<KEp)) {
		
			
			Xlast = X;
			Ylast = Y;
			
			
			X = Vox*Vt/g*(1-Math.exp(-g*t/Vt)); // X position
			Y = Yinit + this.bSize + Vt/g*(Voy+Vt)*(1-Math.exp(-g*t/Vt))-Vt*t; // Y position
			
			
			Vx = (X-Xlast)/TICK;	//Estimate Vx from difference
			Vy = (Y-Ylast)/TICK;	//Estimate Vy from difference
			
			// Check to see if we have hit ground
			//If yes, inject energy loss
			if ((Vy<0) && (Y<=bSize)) {
				
				// Offset for next parabola
				Xo += X;
				
				// Lose energy in collision with ground
				KEp = KEx + KEy;
				KEx = 0.5*Vx*Vx*(1-this.bLoss); // Kinetic energy in X direction after collision
				KEy = 0.5*Vy*Vy*(1-this.bLoss); // Kinetic energy in Y direction after collision
				KEt = KEx + KEy;
				
				
				Vox = Math.sqrt(2*KEx); // Resulting horizontal velocity
				Voy = Math.sqrt(2*KEy); // Resulting vertical velocity
				
				// If the ball is going backward (right to left), keep it that way
				if (Vx < 0) {
					Vox*=-1;
				}
				
				// Restarting for new parabola
				X = 0;
				Y = bSize;
				t = 0;
			
				
				Vx = (X-Xlast)/TICK;	//estimate Vx from difference
				Vy = (Y-Ylast)/TICK;	//estimate Vy from difference

			} 
			
			// Display Update
			ScrX = (int) (X*SCALE + this.Xi*SCALE + Xo*SCALE);
			ScrY = (int) (HEIGHT-SCALE*(Y+this.bSize));
			this.BallZ.setLocation(ScrX, ScrY);	
			
			t += TICK;
			
			try {
				Thread.sleep(50); // pause for 50 milliseconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} 
		
	} 

} 