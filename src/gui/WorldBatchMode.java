package gui;

import realWorld.FitnessFunction;
import realWorld.World;

//This is entry point for experimentation, number of iterations is set via command line
public class WorldBatchMode {

	public static void main(String[] args) {
		int iterations = Integer.parseInt(args[0]);
		World world = new World(2004*3,3798*3);
		world.setFishPopulation(200);
		world.setWormPopulation(500);
		world.loadConfiguration();
		world.setFitnessType(FitnessFunction.Hungry);
		//world.load();
		
		for(int i = 0 ;i<iterations;i++){
			world.move(true);
			System.out.println(i+"similar fish"+ world.getSimilarFishCount());
		}
		world.save(); // this method saves the trained chromosomes to a file chromosomes.txt after training
	}
	
}
