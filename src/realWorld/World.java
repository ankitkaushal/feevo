package realWorld;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


import org.apache.commons.io.FileUtils;

import genetic.FishEvolver;
import user.selection.model.FitnessPredictor;
//This class contains most of the functional logic for the project
public class World {
	
	
	private List<RealWorldObject> objects = new ArrayList<RealWorldObject>();
	
	 int height;
	 int width;
	 int fishPopulation =200;
	 int wormPopulation= 500;
	 int evolutionCount = 1000;
	 int count = 0;
	 FishEvolver evolver = new FishEvolver(fishPopulation);
	 boolean training= true;
	 PrintWriter fitnessWriter =null;
	 FitnessFunction fitnessType = FitnessFunction.Manual;
	 FitnessPredictor predictor;
	 WorldHelper helper = new WorldHelper(this);
	 // boolean evolution 
	 public void move(boolean evolve) {
		
		
		long startTime = System.currentTimeMillis();
		ArrayList<RealWorldObject> objectsMoved = new ArrayList<RealWorldObject>();
		
		for(RealWorldObject object: objects){
			object.move(getVisibleArea(object.getLocation(),object));
			//System.out.println(object.getLocation());
			if(object.getLocation()[0]>width)
				object.setLocation(new double[]{object.getLocation()[0]-width,object.getLocation()[1]});
			else if(object.getLocation()[0]<0)
				object.setLocation(new double[]{object.getLocation()[0]+width,object.getLocation()[1]});
			
			
			if(object.getLocation()[1]>height)
				object.setLocation(new double[]{object.getLocation()[0],object.getLocation()[1]-height});
			else if(object.getLocation()[1]<0)
				object.setLocation(new double[]{object.getLocation()[0],object.getLocation()[1]+height});
			
			for(RealWorldObject objectMoved:objectsMoved){
				if(helper.areColliding(object, objectMoved)){
					//System.out.println("collision");
					helper.resolveCollision(object, objectMoved, height, width);
				}
				
			}
			objectsMoved.add(object);
			
		}
		long endTime = System.currentTimeMillis();
		//System.out.println(endTime-startTime+" time to move");
		
		if(evolve){
			count++;
		
		if(count>=evolutionCount){
			evolve();
			count =0;
		}
		}
		
		//System.out.println(count);
	}
	
	public void setPredictor(FitnessPredictor predictor) {
		this.predictor = predictor;
	}

	public  double[] getVisibleArea(double location[],RealWorldObject selfObject){
		double[] visibleArea = new double[25];
		for(RealWorldObject object: objects){
			if(object==selfObject)
				continue;
			if( Math.abs(object.getLocation()[0]-location[0])<150 && Math.abs(object.getLocation()[1]-location[1])<150){
				int x = (int) (object.getLocation()[0]-location[0] +150);
				int y = (int) (object.getLocation()[1]-location[1] +150);
				
				x =x/60;
				y=y/60;
				int pixelNumber = x + y*5;
				
				//System.out.println(x+" " +y + pixelNumber);
				visibleArea[pixelNumber] = object.getValue();
				
			}
		}
		return visibleArea;
	}
	
	public void addObject(RealWorldObject object){
		objects.add(object);
	}

	public List<RealWorldObject> getObjects() {
		return objects;
	}

	public World(int height,int width) {
		super();
		//System.out.println("init world" + Thread.currentThread().getName());
		this.height = height;
		this.width = width;
		String fileName = "fitness.txt";
		 
		try {
			fitnessWriter = new PrintWriter(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void loadConfiguration(){
		Random rand = new Random();
		for(int i =0;i<fishPopulation;i++){
		Fish fish = new Fish();
		
		
		
		fish.generateRandomChromosome();
		fish.loadBrain();
		fish.setLocation(new double[]{rand.nextInt(width),rand.nextInt(height)});
		addObject(fish);
		
		}
		
		
		
		for(int i =0;i<wormPopulation;i++){
		Worm worm = new Worm();
		
	
		worm.setLocation(new double[]{rand.nextInt(width),rand.nextInt(height)});
		addObject(worm);
		}
	}
	
	private void evolve() {
		String chromosomes[] = new String[fishPopulation];
		int[] fitness = new int[fishPopulation];
		
		
		double averageFood=0;
		double averageHawk =0;
		double averageRewards=0;
		double averageSize=0;
		double averageFitness=0;
		double maxFood=0;
		for(int i =0;i<fishPopulation;i++){
			chromosomes[i] = ((Fish)objects.get(i)).getChromosome();
			double distance =  ((Fish)objects.get(i)).getDistanceMoved();
			distance = Math.max(2000, distance);
			if(fitnessType.equals(FitnessFunction.Hungry)){
			//fitness[i] =  (int) (  ((Fish)objects.get(i)).getFoodEaten() *1000.0/distance  ) ;
				fitness[i] =  (int) (  ((Fish)objects.get(i)).getFoodEaten()   ) ;
			System.out.println("hungry");
			}
			else if(fitnessType.equals(FitnessFunction.Manual)){
			//String chromosome = ((Fish)objects.get(i)).getChromosome();
			fitness[i]=  (int) predictor.getFitness(((Fish)objects.get(i)));
			System.out.println("manual");
			
			}
			else if(fitnessType.equals(FitnessFunction.Natural)){
				fitness[i] =  (int) (  ((Fish)objects.get(i)).getFoodEaten() + ((Fish)objects.get(i)).getRewards());
			}
			
			if(((Fish)objects.get(i)).getFoodEaten()>maxFood){
				maxFood = ((Fish)objects.get(i)).getFoodEaten();
			}
			averageSize+=((Fish)objects.get(i)).getSize();
			averageFood += ((Fish)objects.get(i)).getFoodEaten();
			averageRewards+=((Fish)objects.get(i)).getRewards();
			averageHawk +=((Fish)objects.get(i)).getHawkRatio();
			averageFitness+=fitness[i];
			}
		
		averageFood /= fishPopulation+0.0;
		averageHawk /= fishPopulation+0.0;
		averageRewards/=fishPopulation+0.0;
		averageSize/=fishPopulation+0.0;
		averageFitness/=fishPopulation+0.0;

		//saves the statisitcs to a file fitness.txt after each call to this method
		fitnessWriter.println("food average"+ averageFood +"food max"+maxFood+"averageHawk"+ averageHawk+"averageRewards"+averageRewards +"averagesize"+averageSize+ "averageFitness" + averageFitness);
		fixFitness(fitness);
		fitnessWriter.flush();
		
		evolver.setChromosomes(chromosomes);
		evolver.setFitness(fitness);
		
		evolver.evolvePopulation();
		chromosomes = evolver.getChromosomes();
		
		
		for(int i =0;i<fishPopulation;i++){
			((Fish)objects.get(i)).setChromosome(chromosomes[i]);
			
			((Fish)objects.get(i)).loadBrain();
			((Fish)objects.get(i)).reset();
			((Fish)objects.get(i)).setDistanceMoved(0);
		//	((Fish)objects.get(i)).save();
			}
	}
	
	public int getSimilarFishCount(){
		HashSet<String> set = new HashSet<String>();
		for(int i =0;i<fishPopulation;i++){
			
			set.add(   ((Fish)objects.get(i)).getChromosome());
		
			}
		return set.size();
	}
	
	

	public void setWormPopulation(int wormPopulation) {
		ArrayList<RealWorldObject> objectsToDelete = new ArrayList<RealWorldObject>();
		this.wormPopulation = wormPopulation;
		int count = 0;
		for(RealWorldObject object:objects){
			if(object instanceof Worm){
				count+=1;
				if(count>wormPopulation){
					objectsToDelete.add(object);
				}
			}
		}
		
		for(RealWorldObject object:objectsToDelete){
			objects.remove(object);
		}
	}

	public void setFishPopulation(int fishPopulation) {
		ArrayList<RealWorldObject> objectsToDelete = new ArrayList<RealWorldObject>();
		this.fishPopulation = fishPopulation;
		int count = 0;
		for(RealWorldObject object:objects){
			if(object instanceof Fish){
				count+=1;
				if(count>fishPopulation){
					objectsToDelete.add(object);
				}
			}
		}
		
		for(RealWorldObject object:objectsToDelete){
			objects.remove(object);
		}
		
		evolver = new FishEvolver(fishPopulation);
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public void fixFitness(int[] fitness){
		
		int min = Integer.MAX_VALUE;
		
		for(int i:fitness){
			if(i<min)
				min = i;
		}
		
		if(min<0){
			for(int i=0;i<fitness.length;i++){
				fitness[i]-= min;
			}
		}
			
	}
	
	//Saves the chromosomes to a file chromosomes.txt
	public void save(){
		
		String fileName = "chromosomes.txt";
		PrintWriter writer = null;
		try {
			 writer = new PrintWriter(new File(fileName));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(RealWorldObject object:objects){
			if(object instanceof Fish){
			Fish fish = (Fish)object;
			writer.println(fish.getChromosome());
			}
		}
		writer.close();
		fitnessWriter.close();
	}

	//Loads the configuration from file chromosomes.txt
	public void load(){
		String fileString =null;
		try {
			fileString = FileUtils.readFileToString(new File("chromosomes.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] lines = fileString.split(System.getProperty("line.separator")); 
		
		int i=0;
		for(RealWorldObject object:objects){
			if(object instanceof Fish){
			Fish fish = (Fish)object;
			fish.setChromosome(lines[i++]);
			}
		}
	}

	public FitnessPredictor getPredictor() {
		return predictor;
	}

	public void setFitnessType(FitnessFunction fitnessType) {
		this.fitnessType = fitnessType;
	}

	public FitnessFunction getFitnessType() {
		return fitnessType;
	}
	
	
}
