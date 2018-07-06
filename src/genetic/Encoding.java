package genetic;

public enum Encoding {
Real,Binary,Genitor;

public double parseWeight(String weight){
	
	if(this.equals(Real)){
		return Double.parseDouble(weight)-50;
	}
	else if(this.equals(Binary))
	{
		return Integer.parseInt(weight,2)-63;
	}
	else if(this.equals(Genitor)){
		if(weight.substring(0).equals("0"))
			return 0;
		else
			return Integer.parseInt(weight.substring(1, 8),2)-63;
	}
	else{
		throw new IllegalArgumentException();
	}
}

public int parseWeightAndScale(String weight,int scaleFactor){
	double weightCalculated = parseWeight(weight);
	double maxValue =0;
	
	if(this.equals(Real)){
		maxValue = 50.0;
	}
	else if(this.equals(Binary))
	{
		maxValue=64;
	}
	else if(this.equals(Genitor)){
		weightCalculated = Binary.parseWeight(weight.substring(1,8));
		maxValue = 64;
	}
	else{
		throw new IllegalArgumentException();
	}
	
	return (int)((Math.abs(weightCalculated)/maxValue)*scaleFactor);
}
public static void main(String[] args) {
	Encoding encoding = Encoding.Real;
	System.out.println(encoding.parseWeightAndScale("00", 5));
}
}
