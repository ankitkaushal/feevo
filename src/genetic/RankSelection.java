package genetic;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class RankSelection implements Selection {

	@Override
	public String[] select(String[] chromosomes,int[] fitness) {
		// TODO Auto-generated method stub
		int population = chromosomes.length;
		quicksort(0,population-1,fitness,chromosomes);
		int sumFitness=0;
		String[] newPopulation = new String[population];
		Random rand = new Random();
		
		
		for(int i =0;i<population;i++){
				sumFitness+=i+1;
				
			}
		for(int i =0;i<population;i++){
			fitness[i] =  i+1;
			
		}
		
		System.out.println(Arrays.toString(fitness)+" "+ Arrays.toString(chromosomes) + sumFitness);
		for(int i=0;i<population;i++){
			
			int randomNumber =  rand.nextInt(sumFitness);
			newPopulation[i] =chromosomes[findChromosome(randomNumber,population,fitness)];
		}
		return newPopulation;
}
	
	private void quicksort(int low, int high,int[] fitness,String[] chromosomes) {
        int i = low, j = high;
        // Get the pivot element from the middle of the list
        int pivot = fitness[low + (high-low)/2];

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller than the pivot
            // element then get the next element from the left list
            while (fitness[i] < pivot) {
                i++;
            }
            // If the current value from the right list is larger than the pivot
            // element then get the next element from the right list
            while (fitness[j] > pivot) {
                j--;
            }

            // If we have found a value in the left list which is larger than
            // the pivot element and if we have found a value in the right list
            // which is smaller than the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i <= j) {
                exchange(i, j,fitness,chromosomes);
                i++;
                j--;
            }
        }
        // Recursion
        if (low < j)
            quicksort(low, j,fitness,chromosomes);
        if (i < high)
            quicksort(i, high,fitness,chromosomes);
    }

    private void exchange(int i, int j,int[] fitness,String[] chromosomes) {
        int temp = fitness[i];
        String cTemp = chromosomes[i];
        fitness[i] = fitness[j];
        chromosomes[i] = chromosomes[j];
        fitness[j] = temp;
        chromosomes[j] = cTemp;
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
    public static void main(String[] args) {
		RankSelection selection = new RankSelection();
		String[] chromosomes = new String[]{"5","13","2"};
		int[] fitness = new int[]{5,13,2};
		selection.select(  chromosomes,fitness);
	//	System.out.println(Arrays.toString(chromosomes));
	}
}
