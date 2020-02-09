This program executes the the projectile motion of a ball which launches with an initial velocity at an angle,
 * and bounces upon impact with the ground. The ball loses energy upon each impact, and during its range of motion
 * due to air resistance. As soon as the kinetic energy of the ball depletes to zero, the ball stops bouncing.
 *
 * @author zedzeeden
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>

	#define m 1 			//mass of the ball
	#define k 0.0016  		// constant to be factored in during air resistance and terminal velocity calculations
	#define g 9.8			//MKS gravitational constant 9.8 m/s^2
	#define pi 3.141592654 	//To convert degrees to radians
	#define TICK 0.1 		// Clock tick duration (sec)
	#define ETHR 0.01 		// If either KEx or KEy< ETHR, STOP

	double Vo;				// initial velocity in m/s
	double theta;			//launch angle in degrees
	double loss;			//Energy loss due to collision with the ground
	double bSize;			//radius of the ball
	double XLast=5;
	double YLast=0;
	double Vox;				//horizontal component of initial velocity
	double Voy;				//vertical component of initial velocity
	double Vt;				// Terminal velocity
	double t=0.0; 			//time variable(to be incremented at the end of each iteration)
	double X;  				//x at time t (real)+initialization
	double Y;				//y at time t (real)+initialization
	double Vx;				// horizontal component of the velocity
	double Vy;  			// vertical component of the velocity
	double KEy=0;			// Kinetic energy in Y direction after collision
	double KEx=0;			// Kinetic energy in X direction after collision
	double Xo=0.0;			//value for shifting x
	double Total_Time=0; 	//printing the t(not setting t to 0 when ball touch ground)
	double thetaRad;		//launch angle in radians




int main(void){


	printf("Enter the initial velocity of the ball in meters/s [0,100]:	");
	scanf("%lf",&Vo);
	printf("\n");

	printf("Enter the launch angle in degrees[0,90]: ");
	scanf("%lf",&theta);
	printf("\n");

	printf("Enter the energy loss parameter [0,1]: ");
	scanf("%lf",&loss);
	printf("\n");

	printf("Enter the radius of the ball in meters [0.1,5.0]: ");
	scanf("%lf",&bSize);
	printf("\n");

	thetaRad=theta*pi/180;
	Vox=Vo*cos(thetaRad);
	Voy=Vo*sin(thetaRad);
	Vt=(m*g/(4*pi*bSize*bSize*k));

	X=XLast;
	YLast=bSize;
	Y=YLast;
	Vx=Vox;
	Vy=Voy;
	KEx=0.5*Vx*Vx;
	KEy=0.5*Vy*Vy;

	printf("t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy: %.2f\n",Total_Time, XLast,YLast,Vx,Vy);

	//Simulation Loop
			while(1)
			{
				X=Xo+(Vox*Vt/g)*(1-(exp((-g*t)/Vt)));						//X position
				Y=bSize+((Vt/g)*(Voy+Vt)*(1-(exp((-g*t)/Vt))))-(Vt*t); 		//Y position

				Vx=(X-XLast)/TICK;											//Estimate Vx from difference
				Vy=(Y-YLast)/TICK;											//Estimate Vy from difference

				// Check to see if we've hit the ground
				// If yes, inject energy loss

				 if ((Vy < 0) && (Y < bSize))	{
					KEy=0.5*m*Vy*Vy*(1-loss);
					KEx=0.5*m*Vx*Vx*(1-loss);

					if ((KEx<=ETHR) || (KEy<=ETHR)) {
						break;
					}

					Vox=sqrt(2*KEx);										// Resulting horizontal velocity
					Voy=sqrt(2*KEy);										// Resulting vertical velocity

					// Offset for next parabola
					Xo=X;
					YLast=bSize;
					t=0;
				}

				// Print the location of the ball
				if(Total_Time>0)
				{
					printf("t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy: %.2f\n",Total_Time, X,Y,Vx,Vy);
				}


				t+=TICK;	// time increment
				Total_Time+=TICK;		//total_time increment

				// Creates new position of the ball on the ground
				XLast=X;
				YLast=Y;

				// Break statement to break out of the while(true) loop
				if ((KEx<=ETHR) || (KEy<=ETHR)) {
					break;
				}
			}

			return 0;
}
 