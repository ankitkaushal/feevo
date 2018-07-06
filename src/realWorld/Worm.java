package realWorld;

public class Worm implements RealWorldObject {

	double[] location;
	
	@Override
	public double[] getLocation() {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public void move(double[] visibleArea) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public RealWorldObjectType getType() {
		// TODO Auto-generated method stub
		return RealWorldObjectType.WORM;
	}

	@Override
	public void setLocation(double[] location) {
		this.location = location;
		
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 20;
	}

}
