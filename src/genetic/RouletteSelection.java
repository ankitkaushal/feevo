package genetic;

import java.util.Arrays;
import java.util.Random;

public class RouletteSelection implements Selection {

	@Override
	public String[] select(String[] chromosomes,int[] fitness) {
		// TODO Auto-generated method stub
		int population = chromosomes.length;
		int sumFitness=0;
		String[] newPopulation = new String[population];
		Random rand = new Random();
		
		
		for(int i =0;i<population;i++){
				sumFitness+=fitness[i];
			}
		
		for(int i=0;i<population;i++){
			System.out.println(sumFitness + Arrays.toString(fitness));
			int randomNumber =  rand.nextInt(sumFitness);
			newPopulation[i] =chromosomes[findChromosome(randomNumber,population,fitness)];
		}
		return newPopulation;
	}

	public int findChromosome(int randomNumber,int population,int[] fitness){
		
		for(int i=0;i<population;i++ ){
			randomNumber = randomNumber-fitness[i];
			if(randomNumber<=0){
				return i;
			}
		}
		return population;
	}
}
