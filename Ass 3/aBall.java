import java.awt.Color;

import acm.graphics.GOval;

public class aBall extends Thread {
	
	private static final int HEIGHT = 600;
	private static final double SCALE = HEIGHT/100; // Pixel/meter
	private static final double g = 9.8;// MKS gravitational constant 9.8 m/s^2
	private static final double Pi = 3.141592654;// To convert degrees to radians
	private static final double TICK = 0.1; // Clock tick duration (sec)
	private static final double ETHER = 0.01; // If either Vx or Vy<ETHER STOP
	private static final double m = 1.0; // given m = 1.0 kg
	private static final double k = 0.0001; // given k = 0.0001
	double time = 0.0; // set time start at 0
	public boolean b = true;//define to check if the while loop following is executed
	
	double Vo;
	double theta;
	double bSize;
	double bLoss;
	Color bColor;
	private double Xi;
	private double Yi;
	private GOval iBall;
	
	public aBall (double Xi, double Yi, double Vo, double theta, double bSize,
		Color bColor, double bLoss) {
	
		this.Xi = Xi; 
		this.Yi = Yi;
		this.Vo = Vo;
		this.theta = theta;
		this.bSize = bSize;
		this.bColor = bColor;
		this.bLoss = bLoss;
		iBall = new GOval ((Xi-bSize)*SCALE,HEIGHT-2*bSize*SCALE,2*bSize*SCALE,2*bSize*SCALE);
		iBall.setFilled(true);
		iBall.setFillColor(bColor);
	}
		
	public void run () {

		// initialize Variables
			double Vt = g / (4*Pi*bSize*bSize*k);
			double Vox=Vo*Math.cos(theta*Pi/180);
			double Voy=Vo*Math.sin(theta*Pi/180);
			double X,Y;
		    double Xlast = Xi, Ylast = Yi;
			double Vx = Vox;
			double Vy = Voy;
		    double KEx = 0.5*m*Vox*Vox, KEy = 0.5*m*Voy*Voy;
		    double KExlast=100000, KEylast=100000;//required to restate the stopping condition (different from A1)
		    double XcollisionAdjust = 0; // time will be zero after each collision since I set time=0 below, to make the ball jump continuously, initialize a new variable
	
		//Simulation loop
		    
		while(true) {
				X = Vox*Vt/g*(1-Math.exp(-g*time/Vt)) + XcollisionAdjust; // after adding XcollisionAdjust, next jumping will start from where it lands
				Y = Yi+bSize + Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time;
				Vx = (X-Xlast)/TICK;
		        Vy = (Y-Ylast)/TICK;
		        
	    // this if statement only happens when ball touches the ground 
	        if  (Vy<0 && Y<=bSize) {
	        	KExlast=0.5*Vx*Vx;
	        	KEylast=0.5*Vy*Vy;
	        	KEx = 0.5*Vx*Vx*(1-bLoss); 
	        	KEy = 0.5*Vy*Vy*(1-bLoss); 
	        	Vox = Math.sqrt(2*KEx); 	        	
	        	Yi=0;
	        	Y=bSize;
	        	if (theta > 90) Vox=-Vox;//when theta is larger than 90, the ball will move from right to left
	        	Voy = Math.sqrt(2*KEy);
	        	time=0; // to prevent the next ball movement from starting below the ground
	        	XcollisionAdjust = Xlast; // give XcollisionAjust the X position of the landing ball, so that next while loop start from the last X position
	        	X=Vox*Vt/g*(1-Math.exp(-g*time/Vt)) + XcollisionAdjust;
	        }

	        Xlast = X;//give Xlast new X position
	        Ylast = Y;//give Ylast new Y position
	        int ScrX = (int) ((Xi+X-bSize)*SCALE);
	        int ScrY = (int) (HEIGHT-(Y+bSize)*SCALE); // have to be int type, or the program does not work
	        iBall.setLocation(ScrX,ScrY); // renew myBall location for each past 0.1s
	        time +=TICK;
	        
	    // signify where to stop    
	        if ((KExlast-KEx <= Math.pow(ETHER,3)) | ((KEylast-KEy) <= Math.pow( ETHER,3))) break;
	  

	        if (time == 0.1) {
	        	Vx = Vox;
	        	Vy = Voy;
	        } //eliminate the problem that at time == 0.1, the data is not compatible
	        			
	        
				try {
	        	// pause for 50 milliseconds
	        	Thread.sleep(50);
	        	} catch (InterruptedException e) {
	        	e.printStackTrace(); }
		}
		b = false;//check while loop is running to check isRunning()
	}
	
	//method to return bSize
	public double getbSize() {
		return bSize;
	}
	
	//method to return iBall
	public GOval getBall() {
		return iBall;
	}
	
	//define the moveTo() to reset location and get feed data to traverse_inorder()
	public void moveTo (double x,double y) {
		iBall.setLocation(x,y);
	}
}

