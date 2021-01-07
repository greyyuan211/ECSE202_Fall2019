import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import acm.graphics.GLabel;
import acm.graphics.GRect;
import acm.gui.DoubleField;
import acm.gui.IntField;
import acm.gui.TableLayout;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
/**
 *This class is the main body to generate the random numbers for the simulation
 *detect whether buttons is pressed or clicked and send signal to other class to implement corresponding functions
 *functions include run,stack,clear,stop,trace,quit
 *add menu which includes JComboboxes; 
 *add JTooggleButton Trace; 
 *add JPanel which includes parameter entry sliders and number fields
 * 
 * @author Linwei Yuan
 *
 */
public class bSim extends GraphicsProgram {
//parameters for windows	
	private static final int WIDTH = 1200;         
	private static final int HEIGHT = 600; 
	private static final int OFFSET = 200; 	
//parameters for In/Double field
	private IntField numField;
	private DoubleField minSizeField,maxSizeField,eminField,emaxField,minVoField,maxVoField,minThetaField,maxThetaField;
//parameters for JSliders
	private JSlider num,minSize,maxSize,lossMin,lossMax,minVel,maxVel,minTh,maxTh;
	private JPanel paraEntry;
//parameters for random numbers
	private int numBalls = 60;
	private int minsize = 1;
	private int maxsize = 7;
	private int emin = 2 ;
	private double emin2 = 0.2;
	private int emax = 6;
	private double emax2 = 0.6;
	private int minVo = 40;
	private int maxVo = 50;
	private int minTheta = 80;
	private int maxTheta = 100;
	double Xi = 100.0;
	private RandomGenerator rgen = new RandomGenerator(); 	//Generate random number,but set seed in the init()
//parameters for Labels	
	private GLabel words=new GLabel("Simulation complete!");
	GLabel words2 = new GLabel("All Stacked!");	//"ALl Stacked" message
//parameters for functions implemented in other classes	
	public volatile Boolean isTracing = false;	
	bTree myTree = new bTree();

	public void init() {
		rgen.setSeed((long)424242);

		createTrace(); // create method that implement JToggleButton that used for tracing
		createMenu(); // create method that implement Menu JomboBox
		createJSliders(); // put in method that create sliders in JPanel
		
	// set the size of the display window
		this.resize(WIDTH+300, HEIGHT+OFFSET);
		
	//draw the horizon
		GRect myGround = new GRect(0, HEIGHT, WIDTH, 3);  // ground coordinates
		myGround.setFilled(true);
		add(myGround);
		
	}

	public void doSim(){
		for(int i = 0;i<numBalls; i++) {
		//generate random numbers
			double bSize = rgen.nextDouble(minsize, maxsize);
			Color bColor = rgen.nextColor();
			double bLoss = rgen.nextDouble(emin2, emax2);
			double Vo = rgen.nextDouble(minVo, maxVo);
			double theta = rgen.nextDouble(minTheta, maxTheta);
		//call iBall restored in aBall class	
			aBall iBall = new aBall(Xi,bSize,Vo,theta,bSize,bColor,bLoss,this,isTracing);
			add(iBall.getBall());
			myTree.addNode(iBall);
			iBall.start();
		//add "simulation complete!"	
			remove(words2);
			words.setColor(Color.RED);
			add(words,1030,580);
			}
		
	}
	
	public void doStack() {
		//call stackBalls() in bTree class
			myTree.stackBalls();			
		//add "all stacked!" line
			remove(words);
			words2.setColor(Color.RED);
			add(words2,1030,580);
	}
	
	public void clear() {
		removeAll(); // clear all the stuff in the screen currently
		myTree.clearbTree(); // empty myTree
	// add ground after cleaning all	
		GRect myGround = new GRect(0, HEIGHT, WIDTH, 3);  
		myGround.setFilled(true);
		add(myGround);
	}

	//implement a method to make sure the interaction between sliders and number field
	public void actionPerformed(ActionEvent e) {
	//this method basically implement that getting values from the in/double field, then pass them to the sliders	
		if(e.getSource() == numField) {
			IntField source1= (IntField)e.getSource();
			numBalls = (int)source1.getValue();
			num.setValue(numBalls);
		}
		else if(e.getSource()==minSizeField) {
			DoubleField source2 = (DoubleField)e.getSource();
			minsize = (int)source2.getValue();
			minSize.setValue(minsize);
		}
		else if(e.getSource()==maxSizeField) {
			DoubleField source3 = (DoubleField)e.getSource();
			maxsize = (int)source3.getValue();
			maxSize.setValue(maxsize);
		}
		else if(e.getSource()==eminField) {
			DoubleField source4 = (DoubleField)e.getSource();
			emin2 = source4.getValue();
			emin = (int)(emin2*10);
			lossMin.setValue(emin);
		}
		else if(e.getSource()==emaxField) {
			DoubleField source5 = (DoubleField)e.getSource();
			emax2 = source5.getValue();
			emax = (int)(emax2*10);
			lossMax.setValue(emax);
		}
		else if(e.getSource()==minVoField) {
			DoubleField source6 = (DoubleField)e.getSource();
			minVo = (int)source6.getValue();
			minVel.setValue(minVo);
		}
		else if(e.getSource()==maxVoField) {
			DoubleField source7 = (DoubleField)e.getSource();
			maxVo = (int)source7.getValue();
			maxVel.setValue(maxVo);
		}
		else if(e.getSource()==minThetaField) {
			DoubleField source8 = (DoubleField)e.getSource();
			minTheta = (int)source8.getValue();
			minTh.setValue(minTheta);
		}
		else if(e.getSource()==maxThetaField) {
			DoubleField source9 = (DoubleField)e.getSource();
			maxTheta = (int)source9.getValue();
			maxTh.setValue(maxTheta);
		}
		
		
	}
	
	public void createJSliders () {
		//set up listeners for Int/Double field which will be used in junction with JSliders
		//so that the sliders and the value entered will interact
			numField = new IntField(60);
			minSizeField = new DoubleField(1.0);
			maxSizeField = new DoubleField(7.0);
			eminField = new DoubleField(0.2);
			emaxField = new DoubleField(0.6);
			minVoField = new DoubleField(40.0);
			maxVoField = new DoubleField(50.0);
			minThetaField = new DoubleField(80.0);
			maxThetaField = new DoubleField(100.0);
				
		//add action listener for the JSliders
			numField.addActionListener(this);
			minSizeField.addActionListener(this);
			maxSizeField.addActionListener(this);
			eminField.addActionListener(this);
			emaxField.addActionListener(this);
			minVoField.addActionListener(this);
			maxVoField.addActionListener(this);
			minThetaField.addActionListener(this);
			maxThetaField.addActionListener(this);
				
		//add title of the JPanel
			add(new JLabel("General Simulation Parameters"),EAST);
			add(new JLabel("                             "),EAST);
			
		//create JPanel to put the JSliders
			paraEntry = new JPanel(new TableLayout(9,5));//we need 9 line
		//add 1st Line NUMBALLS		
			paraEntry.add(new JLabel("NUMBALLS:"));     	//(1,1) 
			paraEntry.add(new JLabel("1"));					//(1,2)
			num = new JSlider(1,255,60);
			paraEntry.add(num);								//(1,3)
			paraEntry.add(new JLabel("255"));				//(1,4)
			paraEntry.add(numField);						//(1,5)
			numField.setForeground(Color.blue);				//set the number to blue
				num.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						numBalls = num.getValue();
						numField.setValue(numBalls);
					}
					
				});
		//add 2nd Line MIN SIZE	(same form as the first line)
			paraEntry.add(new JLabel("MIN SIZE:"));			
			paraEntry.add(new JLabel("1.0"));
			minSize = new JSlider(1,25,1);
			paraEntry.add(minSize);
			paraEntry.add(new JLabel("25.0"));
			paraEntry.add(minSizeField);
			minSizeField.setForeground(Color.blue);
				minSize.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						minsize = minSize.getValue();
						minSizeField.setValue(minsize);
					}
					
				});
		//add 3rd Line MAX SIZE
			paraEntry.add(new JLabel("MAX SIZE:"));		
			paraEntry.add(new JLabel("1.0"));
			maxSize = new JSlider(1,25,7);
			paraEntry.add(maxSize);
			paraEntry.add(new JLabel("25.0"));
			paraEntry.add(maxSizeField);
			maxSizeField.setForeground(Color.blue);
				maxSize.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						maxsize = maxSize.getValue();
						maxSizeField.setValue(maxsize);
					}
					
				});
		//add 4th Line LOSS MIN		
			paraEntry.add(new JLabel("LOSS MIN:"));		
			paraEntry.add(new JLabel("0.0"));
			lossMin = new JSlider(0,10,2);
			paraEntry.add(lossMin);
			paraEntry.add(new JLabel("1.0"));
			paraEntry.add(eminField);
			eminField.setForeground(Color.blue);
				lossMin.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						emin = lossMin.getValue();
						emin2 = (double) emin/10;
						eminField.setValue(emin2);
					}
					
				});
		//add 5th Line LOSS MAX
			paraEntry.add(new JLabel("LOSS MAX:  "));	
			paraEntry.add(new JLabel("0.0"));
			lossMax = new JSlider(0,10,6);
			paraEntry.add(lossMax);
			paraEntry.add(new JLabel("1.0"));
			paraEntry.add(emaxField);
			emaxField.setForeground(Color.blue);
				lossMax.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						emax = lossMax.getValue();
						emax2 = (double) emax/10;
						emaxField.setValue(emax2);
					}
					
				});
		//add 6th Line MIN VEL	
			paraEntry.add(new JLabel("MIN VEL:"));		
			paraEntry.add(new JLabel("1.0"));
			minVel = new JSlider(1,200,40);
			paraEntry.add(minVel);
			paraEntry.add(new JLabel("200.0"));
			paraEntry.add(minVoField);
			minVoField.setForeground(Color.blue);
				minVel.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						minVo = minVel.getValue();
						minVoField.setValue(minVo);
					}
					
				});
		//add 7th Line MAX VEL	
			paraEntry.add(new JLabel("MAX VEL:"));		
			paraEntry.add(new JLabel("1.0"));
			maxVel = new JSlider(1,200,50);
			paraEntry.add(maxVel);
			paraEntry.add(new JLabel("200.0"));
			paraEntry.add(maxVoField);
			maxVoField.setForeground(Color.blue);
				maxVel.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						maxVo = maxVel.getValue();
						maxVoField.setValue(maxVo);
					}
					
				});
		//add 8th Line TH MIN		
			paraEntry.add(new JLabel("TH MIN:"));		
			paraEntry.add(new JLabel("1.0"));
			minTh = new JSlider(1,180,80);
			paraEntry.add(minTh);
			paraEntry.add(new JLabel("180.0"));
			paraEntry.add(minThetaField);
			minThetaField.setForeground(Color.blue);
				minTh.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						minTheta = minTh.getValue();
						minThetaField.setValue(minTheta);
					}
					
				});
		//add 9th Line TH MAX	
			paraEntry.add(new JLabel("TH MAX:"));		
			paraEntry.add(new JLabel("1.0"));
			maxTh = new JSlider(1,180,100);
			paraEntry.add(maxTh);
			paraEntry.add(new JLabel("180.0"));
			paraEntry.add(maxThetaField);						
			maxThetaField.setForeground(Color.blue);
				maxTh.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						maxTheta = maxTh.getValue();
						maxThetaField.setValue(maxTheta);
					}
					
				});

			add(paraEntry,EAST); // put JPanel paraEntry to the right
		}
	
	public void createMenu () {
		addActionListeners();
		//create Jcombo box that will display "bSim" "Run" "Stack" "Clear" "Stop" "Quit"
			final JComboBox bSimC = new JComboBox();
			bSimC.addItem("bSim");
			bSimC.addItem("Run");		
			bSimC.addItem("Stack");
			bSimC.addItem("Clear");
			bSimC.addItem("Stop");
			bSimC.addItem("Quit");
			add(bSimC, NORTH);//put menu on the top
				
		//add action to each selection
			bSimC.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					
					JComboBox comboBox = (JComboBox) e.getSource();
					if(comboBox==bSimC) {
						if(bSimC.getSelectedIndex()==0) {
							;
						}
						else if(bSimC.getSelectedIndex()==1) {
							doSim();//method to run the simulation
							System.out.println("Starting simulation");
							bSimC.setSelectedIndex(-1);//command that enable us to reselect "run"
						}
						else if(bSimC.getSelectedIndex()==2){
							doStack();//method to call stackBalls() from bTree
							System.out.println("All stacked");
							bSimC.setSelectedIndex(-1);
						}
						else if(bSimC.getSelectedIndex()==3) {
							clear();//method to clear,empty,and add
							System.out.println("All cleared");
							bSimC.setSelectedIndex(-1);
						}
						else if(bSimC.getSelectedIndex()==4) {
							myTree.stop();//implemented in bTree that will stuck root in a recursion
							System.out.println("All stopped");
							bSimC.setSelectedIndex(-1);
						}
						else if(bSimC.getSelectedIndex()==5) {
							System.out.println("Closing");
							System.exit(0);
						}
					}
				}
			});

	//add the rest three useless JComboBox to the menu
		JComboBox File = new JComboBox();
		File.addItem("File");
		add(File,NORTH);
		JComboBox Edit = new JComboBox();
		Edit.addItem("Edit");
		add(Edit,NORTH);
		JComboBox Help = new JComboBox();
		Help.addItem("Help");
		add(Help,NORTH);
	}
	
	public void createTrace () {
		//create the Trace button in the south
			final JToggleButton trace = new JToggleButton ("Trace");
			add(trace, SOUTH);
		//add trace action listener
			trace.addChangeListener(new ChangeListener() {
				public void stateChanged (ChangeEvent e) {
					isTracing = trace.isSelected();
					}
				});
		}
}
