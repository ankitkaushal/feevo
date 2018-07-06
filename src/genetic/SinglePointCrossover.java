package genetic;

import java.util.Random;

public class SinglePointCrossover implements Crossover {

	@Override
	public String join(String chromosome1, String chromosome2) {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int cutAt = rand.nextInt(chromosome1.length()-1)+1;
		return chromosome1.substring(0, cutAt) + chromosome2.substring(cutAt,chromosome1.length());
		
	}

	public static void main(String[] args) {
		System.out.println(new SinglePointCrossover().join("111111", "222222"));
	}

}
