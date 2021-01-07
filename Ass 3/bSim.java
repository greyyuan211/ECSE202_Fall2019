import java.awt.Color;

import acm.graphics.GLabel;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

public class bSim extends GraphicsProgram {
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 600;
	private static final int OFFSET = 200;	
	
	private static final int NUMBALLS = 60;//# balls
	private static final double MINSIZE = 1.0;//Min ball radius (meters)
	private static final double MAXSIZE = 7.0;//Max ball radius (meters)
	private static final double EMIN = 0.2;//min loss coefficient 
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
		rgen.setSeed( (long) 424242 );
		bTree myTree = new bTree();

		//for loop that show set up the random number interval,and call the method from aBall class
		for(int i = 0;i < NUMBALLS; i++) {
			double bSize = rgen.nextDouble(MINSIZE,MAXSIZE);
			Color bColor = rgen.nextColor();
			double bLoss = rgen.nextDouble(EMIN,EMAX);
			double Vo = rgen.nextDouble(VoMIN,VoMAX);
			double theta = rgen.nextDouble(ThetaMIN,ThetaMAX);
			
			aBall iBall = new aBall(Xi,bSize,Vo,theta,bSize,bColor,bLoss); 
			add(iBall.getBall());
			myTree.addNode(iBall);
			iBall.start();
		}
		
		while (myTree.isRunning());//block until termination
		
		//add "click right to continue" line
		GLabel words = new GLabel ("Click mouse to continue");
		words.setColor(Color.RED);
		add(words,1030, 580);
		
		waitForClick();
		myTree.stackBalls();
		remove(words);
		
		//add "all stacked!" line
		GLabel words2 = new GLabel ("All stacked!");
		words2.setColor(Color.RED);
		add(words2,1030, 580);
	}
	
}


