import java.awt.Color;

import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

public class bSim extends GraphicsProgram {
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 600;
	private static final int OFFSET = 200;	
	
	private static final int NUMBALLS = 100;
	private static final double MINSIZE = 1.0;//Min ball radius (meters)
	private static final double MAXSIZE = 10.0;//Max ball radius (meters)
	private static final double EMIN = 0.1;//min loss coefficient 
	private static final double EMAX = 0.6;//max loss coefficient
	private static final double VoMIN = 40.0;//min velocity (meters/sec)
	private static final double VoMAX = 50.0;//max velocity (meters/sec)
	private static final double ThetaMIN = 80.0;//min lauch angle (degrees)
	private static final double ThetaMAX = 100.0;//max lauch angle(degree)
	double Xi = 100.0;
	
	

	public void run() {
		this.resize(WIDTH,HEIGHT+OFFSET);//set up the window
		
		//create ground
		GRect Ground = new GRect (0,HEIGHT,WIDTH,3);
		Ground.setFilled(true);
		add(Ground);			
		
		//generate the random number
		RandomGenerator rgen = RandomGenerator.getInstance ();
		rgen.setSeed( (long) 0.12345 );
		
		//for loop that show set up the random number interval,and call the method from aBall class
		for(int i = 0;i < NUMBALLS; i++) {
			double bSize = rgen.nextDouble(MINSIZE,MAXSIZE);
			Color bColor = rgen.nextColor();
			double bLoss = rgen.nextDouble(EMIN,EMAX);
			double Vo = rgen.nextDouble(VoMIN,VoMAX);
			double theta = rgen.nextDouble(ThetaMIN,ThetaMAX);
			
			aBall myBall = new aBall(Xi,bSize,Vo,theta,bSize,bColor,bLoss); 
			add(myBall.getBall());
			myBall.start();
		}
	}
	
}


