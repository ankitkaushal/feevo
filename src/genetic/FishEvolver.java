package genetic;


import java.util.Arrays;
import java.util.Random;

import realWorld.Fish;

//This is the main class used for the genetic algorithms, mutation rate, selection and crossover
//are configurable for different experiments

public class FishEvolver {

private String[] chromosomes;
private int population;
private int[] fitness;	
private double mutationRate = .005;
private Selection selection = new RouletteSelection();
private Crossover crossover = new SinglePointCrossover();



public void evolvePopulation() {

	
	String[] newPopulation =selection.select(chromosomes, fitness);
	Random rand = new Random();

	
	for(int i =0;i<population;i++){
		int parent1 = rand.nextInt(population);
		int parent2 = rand.nextInt(population);
		
		
		
		String chrom1 = newPopulation[parent1];
		
		String chrom2 = newPopulation[parent2];
		
		
		
		chromosomes[i] = crossover.join(chrom1, chrom2);
		
		
		int mutatedChars = (int)(mutationRate*Fish.getChromosomeLength());
		
		int randomNumber=0;
		for (int ri=0;ri<mutatedChars;ri++){
			
			int randomIndex = rand.nextInt(chromosomes[i].length());
			if(Fish.encoding.equals(Encoding.Binary) || Fish.encoding.equals(Encoding.Genitor)){
			 if(chromosomes[i].charAt(randomIndex)=='0')
				 randomNumber=1;
			 else
				 randomNumber=0;
			}
			else if(Fish.encoding.equals(Encoding.Real)){
				randomNumber = rand.nextInt(10);
			}
			chromosomes[i]= chromosomes[i].substring(0, randomIndex)+randomNumber+ chromosomes[i].substring(randomIndex+1, chromosomes[i].length());  
			
		}
			
		
		
	}
	

	
}

public FishEvolver(int population) {
	super();
	this.population = population;
	chromosomes = new String[population];
	fitness = new int[population];
}



public String[] getChromosomes() {
	return chromosomes;
}

public void setChromosomes(String[] chromosomes) {
	this.chromosomes = chromosomes;
}

public void setFitness(int[] fitness) {
	this.fitness = fitness;
}


}
