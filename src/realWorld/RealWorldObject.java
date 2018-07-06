package realWorld;

public interface RealWorldObject {

	double[] getLocation();
	void move(double[] visibleArea);
	int getValue();
	RealWorldObjectType getType();
	void setLocation(double[] location);
	int getSize();
}
