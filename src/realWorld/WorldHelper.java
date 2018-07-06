package realWorld;

import java.util.Arrays;
import java.util.Random;

public class WorldHelper {
	World world;
	
	public WorldHelper(World world) {
		super();
		this.world = world;
	}

	public  boolean areColliding(RealWorldObject object1,RealWorldObject object2){
		
		if( Math.abs(object1.getLocation()[0] - object2.getLocation()[0]) < object1.getSize()/2+object2.getSize()/2 ){
			if( Math.abs(object1.getLocation()[1] - object2.getLocation()[1]) < object1.getSize()/2+object2.getSize()/2 ){
			return true;
			
			
		}
		}
		
		return false;
		
	}
	
	public void resolveCollision(RealWorldObject object1,RealWorldObject object2,int height,int width){
		if(object1.getType().equals(RealWorldObjectType.FISH) && object2.getType().equals(RealWorldObjectType.WORM)  ){
			Fish fish = (Fish)object1;
			Worm worm = (Worm)object2;
			
			fish.eat();
			Random rand = new Random();
			worm.setLocation(new double[]{rand.nextInt(width),rand.nextInt(height)});
			
		}
		
		
		else if(object2.getType().equals(RealWorldObjectType.FISH) && object1.getType().equals(RealWorldObjectType.WORM)  ){
			Fish fish = (Fish)object2;
			Worm worm = (Worm)object1;
			
			fish.eat();
			Random rand = new Random();
			worm.setLocation(new double[]{rand.nextInt(width),rand.nextInt(height)});
			//System.out.println("collision 2");
		}
		
		else if(object1.getType().equals(RealWorldObjectType.FISH) && object2.getType().equals(RealWorldObjectType.FISH)){
			Fish fish1 = (Fish)object1;
			Fish fish2 = (Fish)object2;
			fish1.contest(fish2.getSize());
			fish2.contest(fish1.getSize());			
			if(fish1.getBehavior().equals(Behavior.HAWK) && fish2.getBehavior().equals(Behavior.HAWK)){
				int totalSize = fish1.getSize() + fish2.getSize();
				int rand = new Random().nextInt(totalSize);
				
				if(rand>=fish1.getSize()){
					fish2.addRewards(1);
					fish1.addRewards(-4);
				}
				else{
					fish1.addRewards(1);
					fish2.addRewards(-4);
				}
				
			}
			else if(fish1.getBehavior().equals(Behavior.HAWK) && fish2.getBehavior().equals(Behavior.DOVE)){
				fish1.addRewards(1);
				fish2.addRewards(-1);
			}
			else if(fish1.getBehavior().equals(Behavior.DOVE) && fish2.getBehavior().equals(Behavior.HAWK)){
				fish2.addRewards(1);
				fish1.addRewards(-1);
				
			}
			
			
		}
		
	}
	
}
