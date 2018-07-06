package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import realWorld.Behavior;
import realWorld.Fish;
import realWorld.FitnessFunction;
import realWorld.RealWorldObject;
import realWorld.RealWorldObjectType;
import realWorld.World;
import realWorld.Worm;
import user.selection.model.FitnessPredictor;
import utilities.IntegerUtilities;


//This is the entry point to play the game via GUI mode
public class WorldGUI extends JFrame implements Runnable,MouseListener,MouseMotionListener {

	World world ;
	int frameLength = 33;
	Fish selectedFish = null;
	Graphics movingGraphics;
	int animationHeight;
	int animationWidth;
	JLabel fishDetails;
	int leftPanelWidth = 150;
	HashMap<Fish,Integer> rating = new HashMap<Fish,Integer>();
	JTextField score;
	GUIHelper helper;
	FitnessPredictor predictor;
	boolean evolution= false;
	boolean freeze =false;
	public WorldGUI() throws HeadlessException {
		super();
		
		helper = new GUIHelper(world,this);
		predictor = new FitnessPredictor();
		world.setPredictor(predictor);
	}
	

	
	
	@Override
	protected void frameInit() {
		// TODO Auto-generated method stub
		leftPanelWidth =150;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize( (int)screenSize.getWidth()-100,(int)screenSize.getHeight()-100);
		
		world = new World(getHeight(),getWidth());
		world.setWidth(getWidth()-leftPanelWidth);
		world.setHeight(getHeight());
		world.loadConfiguration();
		world.load();   //comment this to use the random initialisation instead of previously saved fish in chromosomes.txt
		world.setWormPopulation(50);
		world.setFishPopulation(20);
	     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     
	   
		for(int i = 0 ;i<500;i++){
			world.move(false);
		}
		
		addMouseListener(this);
	     
	    JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(Box.createRigidArea( new Dimension(0,10)));
		fishDetails = new JLabel("<html>Click a fish <br><br>to see <br><br>details</html>");
		fishDetails.setSize(leftPanelWidth, 250);
		BufferedImage bi = new BufferedImage( getWidth()-leftPanelWidth,getHeight(), BufferedImage.TYPE_INT_RGB); 
		
        //Create the graphics object from the BufferedImage height
		leftPanel.setSize(leftPanelWidth, getHeight());
		
		leftPanel.add(fishDetails);
		 score = new JTextField("");
		JButton scoreSubmit = new  JButton("Score");
		scoreSubmit.addActionListener(new ActionListener(){
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				int i = IntegerUtilities.getInt(score.getText());
				
				if(i==-1 ||i>100)
					JOptionPane.showMessageDialog(null, "Please enter a value between 1 and 100");
				else if(selectedFish==null)
					JOptionPane.showMessageDialog(null, "Please select a fish");
				else	{
				rating.put(selectedFish, i);
				JOptionPane.showMessageDialog(null, rating.size() + " fishes rated");
				}
			}});
		
		JButton reset = new JButton("reset");
		reset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				rating.clear();
				JOptionPane.showMessageDialog(null,  "map cleared");
			}});
		
		
		DefaultListModel model = new DefaultListModel();
		final JList fitnessFunction = new JList(model);
		model.addElement("Nautral");
		model.addElement("Hungry");
		model.addElement("Manual");
		fitnessFunction.setSelectedIndex(0);
		JButton evolve = new JButton("evolve");
		evolve.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				evolution = true;
				int selectedIndex = fitnessFunction.getSelectedIndex();
				if(selectedIndex==0)
					world.setFitnessType(FitnessFunction.Natural);
				else if(selectedIndex==1)
					world.setFitnessType(FitnessFunction.Hungry);
				else
					world.setFitnessType(FitnessFunction.Manual);
			}
			
		});
		
		
		leftPanel.add(Box.createRigidArea( new Dimension(0,50)));
		leftPanel.add(score);
		leftPanel.add(scoreSubmit);
		leftPanel.add(reset);
		leftPanel.add(Box.createRigidArea( new Dimension(0,50)));
		leftPanel.add(new JLabel("<html>Please select <br>evolution type</html>"));
		leftPanel.add(Box.createRigidArea( new Dimension(0,20)));
		leftPanel.add(fitnessFunction);
		leftPanel.add(Box.createRigidArea( new Dimension(0,50)));
		leftPanel.add(evolve);
		leftPanel.add(Box.createRigidArea( new Dimension(0,getHeight()/4)));
		
		
		
		
		
		JLabel gui = new JLabel(new ImageIcon(bi));
		add(gui,BorderLayout.EAST);
		add(leftPanel,BorderLayout.WEST);
		animationHeight = getHeight();
		animationWidth = getWidth()-leftPanelWidth;
		setVisible(true);
		setResizable(false);
	   movingGraphics=  bi.createGraphics(); 
	}




	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		
		while(true){
			long timest = System.currentTimeMillis();
		world.move(false);
		long timeend = System.currentTimeMillis();
		try {
			Thread.sleep(frameLength);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(!evolution)
		repaint();
		else
		{
			
			evolve();
			evolution = false;
			
		}
		 timeend = System.currentTimeMillis();
		
		}
	}




	@Override
	public void update(Graphics g) {
		// TODO Auto-generated method stub
		super.update(g);
		
	}

	@Override
	public void paint(Graphics g){

		if(helper==null)
			return;
		
		
		Image img = helper.creatImage(animationWidth, animationHeight, selectedFish);
		
		if(selectedFish!=null){
		double foodRatio = selectedFish.getFoodEaten()*1000/selectedFish.getDistanceMoved();
		
		String fishDetailString = "<html>Hawk "+ Math.round(selectedFish.getHawkRatio()*100)/100.0+" <br>Food/Distance<br> " + Math.round(foodRatio*100)/100.0 ;
		fishDetailString+="<br>";
		fishDetailString +=" Distance<br> "+ Math.round(selectedFish.getDistanceMoved()*100)/100.0 +"<br></html>";
	
		fishDetails.setText(fishDetailString);
		
		}
		else{
			fishDetails.setText("<html>Click a fish <br>to see <br>details</html>");
		}
		
		if(!evolution)
		movingGraphics.drawImage(img, 0, 0, this);
		
		 super.paint(g);
		
	}
	public static void main(String[] args) {
		WorldGUI gui = new WorldGUI();
		new Thread(gui).start();
		
	}
	
	public void findFish(int x,int y){
		selectedFish = null;
		for(RealWorldObject object:world.getObjects()){
			if(object instanceof Fish){
				Fish fish = (Fish)object;
				if( Math.abs(fish.getLocation()[0]-x) <20 && Math.abs(fish.getLocation()[1]-y)<20)
					selectedFish = fish;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		findFish(e.getX()-leftPanelWidth,e.getY());
		System.out.println(e.getX() + " "+e.getY());
	}




	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	

	public void evolve(){
		
		
		
		if(world.getFitnessType().equals(FitnessFunction.Manual)){
		for(Fish fish:rating.keySet()){
			predictor.add(fish, rating.get(fish));
		}
		predictor.train();
		}
		System.out.println("trained");
		for(int i=0;i<10000;i++){
			world.move(true);
			System.out.println(i);
		}
	}
	
	
	
}
