import acm.program.*;

import java.awt.Color;

import acm.graphics.*;

public class Bounce extends GraphicsProgram {
// Code to read simulation parameters from user.
// constant numbers that will be used	
	private static final int WIDTH = 600;// defines the width of the screen in pixels
	private static final int HEIGHT = 600; // distance from top of screen to ground plane
	private static final int OFFSET = 200; // distance from bottom of screen to ground plane
	private static final double g = 9.8;// MKS gravitational constant 9.8 m/s^2
	private static final double Pi = 3.141592654;// To convert degrees to radians
	private static final double Xinit = 5.0; // initial ball location (X)	
	private static final double TICK = 0.1; // Clock tick duration (sec)
	private static final double ETHER = 0.01; // If either Vx or Vy<ETHER STOP
	private static final double XMAX = 100.0; // Maximum value of X
    //private static final double YMAX = 100.0; // Maximum value of Y, provided but not used
	private static final double PD = 1.0; //Trace point diameter
	private static final double SCALE = HEIGHT/XMAX; // Pixel/meter
	private static final double m = 1.0; // given m = 1.0 kg
	private static final double k = 0.0016; // given k = 0.0016
// define variables that will be used	
	double time = 0.0; // set time start at 0
	private double bSize; // define the double bSize so that it can be used in the next line
	double Yinit = bSize; // initial ball location (Y)
	private static final boolean TEST = true; // print if test true
	
	public void run () {
		this.resize(WIDTH,HEIGHT+OFFSET);//create windows of a specific size 600*800
		
	// add the ground that the ball will collide with
		GRect Ground = new GRect (0,600,600,3);
		Ground.setFilled(true);
		add(Ground);
		
	//read parameters for user
		double Vo = readDouble ("Enter the initial velocity of the ball in meters/second [0,100]: ");
		double theta = readDouble ("Enter the launch angle in degree [0,90]: ");
    	double loss = readDouble ("Enter energy loss parameter [0,1]: ");
		bSize = readDouble ("Enter the radius of the ball in meters [0.1,5.0]: ");
		
	//draw and place ball at initial position
		GOval myBall = new GOval ((Xinit-bSize)*SCALE,HEIGHT-2*bSize*SCALE,2*bSize*SCALE,2*bSize*SCALE);
		myBall.setFilled(true);
		myBall.setFillColor(Color.RED);
		add(myBall);

	// initialize Variables
		double Vt = g / (4*Pi*bSize*bSize*k);
		double Vox=Vo*Math.cos(theta*Pi/180);
		double Voy=Vo*Math.sin(theta*Pi/180);
		double X,Y;
	    double Xlast = Xinit, Ylast = Yinit;
	    double Vx, Vy;
	    double KEx = 0.5*m*Vox*Vox, KEy = 0.5*m*Voy*Voy;
	    double XcollisionAdjust = 0; // time will be zero after each collision since I set time=0 below, to make the ball jump continuously, initialize a new variable
	    double t = 0; // another variable to calculate total time
//Simulation loop
	while(true) {
		
		X = Vox*Vt/g*(1-Math.exp(-g*time/Vt)) + XcollisionAdjust; // after adding XcollisionAdjust, next jumping will start from where it lands
		Y = bSize + Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time;
		Vx = (X-Xlast)/TICK;
        Vy = (Y-Ylast)/TICK; 
        Xlast = X;//give Xlast new X position
        Ylast = Y;//give Ylast new Y position
        if (Y<0) Y=0; // prevent the ball from being below Ground at the first contact with Ground
        int ScrX = (int) ((Xinit+X-bSize)*SCALE);
        int ScrY = (int) (HEIGHT-(Y+bSize)*SCALE); // have to be int type, or the program does not work
        myBall.setLocation(ScrX,ScrY); // renew myBall location for each past 0.1s
        time = time+TICK;
        
    // this if statement only happens when ball touches the ground
        if  (Vy<0 && Y<=bSize) {
        	KEx = 0.5*Vx*Vx*(1-loss); 
        	KEy = 0.5*Vy*Vy*(1-loss); 
        	Vox = Math.sqrt(2*KEx); 
        	Voy = Math.sqrt(2*KEy);
        	time=0; // to prevent the next ball movement from starting below the ground
        	XcollisionAdjust = Xlast; // give XcollisionAjust the X position of the landing ball, so that next while loop start from the last X position
        }
    
    // draw dots every 0.1s    
        GOval dot = new GOval(ScrX+bSize*SCALE,ScrY+bSize*SCALE,PD,PD);     
        add(dot);
    // signify where to stop    
        if ((KEx <= ETHER) | (KEy <= ETHER)) break;
    // slow down the calculation process
        pause (100);

    // print datas every 0.1s    
    // print text
        if (time == 0.1) {
        	Vx = Vox;
        	Vy = Voy;
        } //eliminate the problem that at time == 0.1, the data is not compatible
        if (TEST)
         System.out.printf("t: %.2f   X: %.2f   Y: %.2f   Vx: %.2f   Vy:%.2f\n", t , Xinit+X , Y , Vx , Vy );
        t += TICK; //add after println so that data at t==0 can be printed
		}



	}
}