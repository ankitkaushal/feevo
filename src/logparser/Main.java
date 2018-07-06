package logparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//This is to parse the fitness.txt which contains the statistics of training, this class is not part of project
public class Main {

	public static void main(String[] args) throws IOException {
		FileReader reader = new FileReader(new File("fitness.txt"));
		BufferedReader bReader = new BufferedReader(reader);
		
		String line;
		while((line = bReader.readLine())!=null){
			if(line.contains("fitness"))
				;//System.out.println(line.replace("average fitness","").replace(" maximum fitness ",","));
			if(line.contains("food average"))
				System.out.println(line.replace("food average","").replace("averageHawk",",").replace("averageRewards", ",").replace("averagesize", ",").replace("averageFitness", ",").replace("food max",","));
		}
		
		//System.out.println("ankit".substring(2,-1));
	}
	
}
