package genetic;

import java.util.Random;

public class DoublePointCrossover implements Crossover {

	@Override
	public String join(String chromosome1, String chromosome2) {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int cutAt = rand.nextInt(chromosome1.length()-2)+1;
		int cutAt2 =cutAt + 1 + rand.nextInt(chromosome1.length()-1 -cutAt);
		
		System.out.println(cutAt+" "+cutAt2);
		return chromosome1.substring(0, cutAt) + chromosome2.substring(cutAt,cutAt2) +chromosome1.substring(cutAt2, chromosome1.length()) ;
		
	}
	
	public static void main(String[] args) {
		System.out.println(new DoublePointCrossover().join("111111", "222222"));
	}

}
