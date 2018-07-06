package user.selection.model;

import java.util.ArrayList;
import java.util.Arrays;

import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

import realWorld.Fish;

public class FitnessPredictor {

	ArrayList<Fish> fishes =  new ArrayList<Fish>();
	ArrayList<Double> rating =  new ArrayList<Double>();
	int input =4 ;
	int first = 10;
	int middle = 15;
	int output=1;
	
	MultiLayerPerceptron predictor = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, input, first, middle,output); ;
	
	
	public void train(){
		System.out.println("1");
			predictor.setLearningRule(new BackPropagation());
		  DataSet trainingSet = new DataSet(input, 1);
		for(int i=0;i<fishes.size();i++){
			//System.out.println(fishes.get(i).length()+" "+input);
			double[] dInput = getInput(fishes.get(i));
			System.out.println(dInput.length+" "+predictor.getInputsCount());
			trainingSet.addRow(dInput,new double[]{rating.get(i)/100.0});	
			
		}
		System.out.println(trainingSet);
		predictor.learn(trainingSet);
		System.out.println("3");
	}
	
	public double[] getInput(Fish fish){
		
		
		double[] result = new double[]{fish.getFoodEaten()/(fish.getSteps()+1.0),fish.getHawkRatio(),fish.getDistanceMoved()/(fish.getSteps()+1),fish.getSize()/10.0};
		return result;
		
		
	}

	public static void main(String[] args) {
//	FitnessPredictor predictor = new FitnessPredictor();
//	double[] op = predictor.parseInput("1122");
//	System.out.println(Arrays.toString(op));
	}

	
	
	public void add(Fish fish,int score){
		fishes.add(fish);
		rating.add((double)score);
	}

	
	public double getFitness(Fish fish){
		predictor.setInput(getInput(fish));
		predictor.calculate();
		//System.out.println(predictor.getOutput()[0]*100+1);
		return predictor.getOutput()[0]*100+1;
		
	}
}

