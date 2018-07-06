package realWorld;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

import genetic.Encoding;

//This is the biggest class of the project, It contains the code related to Fish agents, the two brains of the Fish are instance variables
public class Fish implements RealWorldObject{

	static int inputLayer =5;
	static int middleLayer = 10;
	static int visibleAreas = 25;
	static int outputLayer = 2;
	public static Encoding encoding = Encoding.Real;
	MultiLayerPerceptron locationBrain = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,visibleAreas,inputLayer,middleLayer,outputLayer);
	
	static int battleBrainInput=1;
	static int battleBrainMiddleLayer = 10; 
	static int battleBrainOutput = 1;
	MultiLayerPerceptron battleBrain = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,battleBrainInput,battleBrainMiddleLayer,battleBrainOutput);
	
	String chromosome;
	double x = 100;
	double y = 100;
	double dx = 0;
	double dy = 0;
	double currentRotation=0;
	boolean inBattle;
	int foodEaten =10;
	double visibleArea[] = new double[1];
	double[] brainOutput = new double[1];
	double distanceMoved = 0;
	boolean keypressed = false;
	Behavior behavior = Behavior.DOVE;
	int size = 10;
	int speed= 10;
	int rewards=0;
	int hawkBehavior =0;
	int doveBehavior=0;
	int stepsMoved =0;
	
	boolean varySpeedAndSize =true;
	
	public int getRewards() {
		return rewards;
	}


	public void addRewards(int rewards) {
		this.rewards += rewards;
	}


	public int getFoodEaten(){
		return foodEaten;
	}
	
	public void eat(){
		foodEaten = foodEaten +10;
	}
	
	
	
	@Override
	public double[] getLocation() {
		// TODO Auto-generated method stub
		return new double[]{x,y};
	}

	
	public boolean flipVertical(){
		return dx<0;
	}
	
	public double getRotation(){
		double newRotation = Math.atan((dy+0.00001)/(dx+0.00001));
		double rotation = currentRotation + (newRotation-currentRotation)/10000;
		currentRotation = newRotation;
		return rotation;
	}
	
	@Override
	public void move(double[] visibleArea) {
		// TODO Auto-generated method stub
		

	
		locationBrain.setInput(visibleArea);
		locationBrain.calculate();
		
		brainOutput = locationBrain.getOutput();
		 
		dx = brainOutput[0]-0.5;
		dy = brainOutput[1]-0.5;
		

		dx=dx*speed/3;
		dy=dy*speed/3;
		x = x+dx;
		y = y+dy;
		
		//System.out.println(dx+" "+dy);
		distanceMoved += Math.abs(dx)+ Math.abs(dy);
		stepsMoved++;
		this.visibleArea = visibleArea;
	}

	public double getDistanceMoved() {
		return distanceMoved;
	}

	public void setDistanceMoved(double distanceMoved) {
		this.distanceMoved = distanceMoved;
	}

	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return 1 ;
	}

	@Override
	public RealWorldObjectType getType() {
		// TODO Auto-generated method stub
		return RealWorldObjectType.FISH;
	}


	@Override
	public void setLocation(double[] location) {
		// TODO Auto-generated method stub
		x =location[0];
		y = location[1];
	}


	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 2 * size;
	}

	//this function loads the random generated encoded string into the two brains of fish
	public void loadBrain(){
		int weightLength=0;
		if(encoding.equals(Encoding.Real)){
			weightLength=2;
		}
		else if(encoding.equals(Encoding.Binary)){
			weightLength=7;
		}
		else if(encoding.equals(Encoding.Genitor)){
			weightLength=8;
		}
		for(int i =0;i<inputLayer;i++ ){
			for(int j=0;j<visibleAreas+1;j++){
				String weightString = chromosome.substring(weightLength*(i*(visibleAreas+1)+j), weightLength*(i*(visibleAreas+1)+j+1));
				double weight = encoding.parseWeight(weightString);
				locationBrain.getLayerAt(1).getNeurons().get(i).getWeights()[j].setValue(weight);
				//locationBrain.getLayerAt(1).getNeurons().get(i).
			}
		}
		int middleLayerStart = inputLayer*visibleAreas;
			//System.out.println(middleLayerStart);
			
		for(int im =0;im<middleLayer;im++ ){
			for(int jm=0;jm<inputLayer+1;jm++){
				String weightString = chromosome.substring(weightLength*(im*(inputLayer+1)+jm +middleLayerStart), weightLength*(im*(inputLayer+1)+jm+1+middleLayerStart));
				double weight = encoding.parseWeight(weightString);
				locationBrain.getLayerAt(2).getNeurons().get(im).getWeights()[jm].setValue(weight);
			}
			
		
		}
		
		int outputLayerStart = middleLayerStart + inputLayer*middleLayer;
		
		for(int io =0;io<outputLayer;io++ ){
			for(int jo=0;jo<middleLayer+1;jo++){
				String weightString = chromosome.substring(weightLength*(io*(middleLayer+1)+jo +outputLayerStart), weightLength*(io*(middleLayer+1)+jo+1+outputLayerStart));
				double weight = encoding.parseWeight(weightString);
				locationBrain.getLayerAt(3).getNeurons().get(io).getWeights()[jo].setValue(weight/50);
			}
			
		
		}
		
		
		int battleBrainStart = outputLayerStart + outputLayer*middleLayer;
		
		for(int ib =0;ib<battleBrainMiddleLayer;ib++ ){
			for(int jb=0;jb<battleBrainInput+1;jb++){
				String weightString = chromosome.substring(weightLength*(ib*(battleBrainInput+1)+jb +battleBrainStart), weightLength*(ib*(battleBrainInput+1)+jb+1+battleBrainStart));
				double weight = encoding.parseWeight(weightString);
				battleBrain.getLayerAt(1).getNeurons().get(ib).getWeights()[jb].setValue(weight);
			}
			//
		
		}//
		
		
		for(int ibm =0;ibm<battleBrainOutput;ibm++ ){
			for(int jbm=0;jbm<battleBrainMiddleLayer+1;jbm++){
				String weightString = chromosome.substring(weightLength*(ibm*(battleBrainMiddleLayer+1)+jbm +battleBrainStart), weightLength*(ibm*(battleBrainMiddleLayer+1)+jbm+1+battleBrainStart));
				double weight = encoding.parseWeight(weightString);
				battleBrain.getLayerAt(2).getNeurons().get(ibm).getWeights()[jbm].setValue(weight);
			}
			//
		
		}//
		
		
		
		String sizeGeneString = chromosome.substring(chromosome.length() - weightLength) ;
	
		if(varySpeedAndSize){
		int sizeGene = encoding.parseWeightAndScale(sizeGeneString, 5);
		size = sizeGene+8;
		speed = 20 -size;
		}
		
		
	}
	
	
	public void generateRandomChromosome(){
		
		int chromLength = inputLayer*(visibleAreas+1) + middleLayer*(inputLayer+1) + outputLayer*(middleLayer+1)+1;
		chromLength+= battleBrainMiddleLayer*(battleBrainInput+1) + battleBrainOutput*(battleBrainMiddleLayer+1);
		
		int max =0;
		
		if(encoding.equals(Encoding.Real)){
			chromLength*=2;
			max=10;
		}
		else if(encoding.equals(Encoding.Binary)){
		chromLength*=7;
		max =2;
		}
		else if(encoding.equals(Encoding.Genitor)){
		chromLength*=8;
		max =2;
		}
		
			
			
		chromosome="";
		
		
		Random rand= new Random();
		for (int i =0;i<chromLength;i++){
			chromosome = chromosome+ rand.nextInt(max); 
	}
	
	
	}
	
	public static int getChromosomeLength(){
		int chromLength = inputLayer*(visibleAreas+1) + middleLayer*(inputLayer+1) + outputLayer*(middleLayer+1) +1;
		chromLength+= battleBrainMiddleLayer*(battleBrainInput+1) + battleBrainOutput*(battleBrainMiddleLayer+1);
		if(encoding.equals(Encoding.Real)){
			chromLength*=2;
		}
		else if(encoding.equals(Encoding.Binary)){
		chromLength*=7;
		}
		else if(encoding.equals(Encoding.Genitor)){
		chromLength*=8;
		}
		
		return chromLength;
	}
	


	public String getChromosome() {
		return chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}
	
	public void reset(){
		foodEaten =10;
		hawkBehavior=0;
		doveBehavior=0;
		rewards=0;
		stepsMoved=0;
	}
	
	
	
	
	



	
	public void contest(int sizeOfOther){
		
		battleBrain.setInput(sizeOfOther);
		battleBrain.calculate();
		double[] brainOutput = battleBrain.getOutput();
		
		
		double beh = brainOutput[0]-0.5;
		
		if(beh>=0){
			behavior = Behavior.HAWK;
			hawkBehavior++;
		}
		if(beh<0){
			behavior = Behavior.DOVE;
			doveBehavior++;
		}
	}
	
	public Behavior getBehavior() {
		return behavior;
	}
	
	
	
	
	public double getHawkRatio(){
		return (hawkBehavior+1.0)/(hawkBehavior+doveBehavior+2.0);
	}
	
	public int getSteps(){
		return stepsMoved;
	}
	
}
