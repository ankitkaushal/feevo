package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import realWorld.Behavior;
import realWorld.Fish;
import realWorld.RealWorldObject;
import realWorld.RealWorldObjectType;
import realWorld.World;
import realWorld.Worm;

public class GUIHelper {

	World world;
	JFrame frame;
	public  Image creatImage(int width,int height,Fish selectedFish){
		 Image img = frame.createImage(width,height);
		Graphics offscr = img.getGraphics();
		Image background = null;
		try {
			background = ImageIO.read(new File("background.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		background = background.getScaledInstance((int)img.getWidth(null), (int)img.getHeight(null), 0);
		offscr.drawImage(background,0,0,frame);
		
	
		for(RealWorldObject object:world.getObjects()){
		
			if(object.getType().equals(RealWorldObjectType.FISH)){
				Fish fishObject = (Fish)object;
				Image fish = null;
				Image hawkFish = null;
				try {
					
					fish = ImageIO.read(new File("fish2.jpg"));
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				double rotationRequired = fishObject.getRotation();
				//System.out.println(rotationRequired);
				double scale = fishObject.getSize()/20.0;
				//System.out.println(scale);
				AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, fish.getWidth(frame)/2.0, fish.getHeight(frame)/2.0);
				
				if(scale!=0)
				tx.scale(scale,scale);
				
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				AffineTransform rv = AffineTransform.getScaleInstance(-1,1);
				 rv.translate(-fish.getWidth(null),0);
				 AffineTransformOp rvop = new AffineTransformOp(rv,
					        AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				
				 if(fishObject.flipVertical())
				 fish = rvop.filter((BufferedImage) fish, null);
				 
				 fish = op.filter((BufferedImage) fish, null);
				
				
				fish = makeColorTransparent((BufferedImage) fish,fishObject.getHawkRatio());
				
				 
				
				
				
				 
				if(fishObject== selectedFish)
					offscr.drawRect((int)fishObject.getLocation()[0]-75, (int)fishObject.getLocation()[1]-75, 150, 150);
					
			
				
				offscr.drawImage(fish,(int)fishObject.getLocation()[0]- (int)(50*scale),(int)fishObject.getLocation()[1]-(int)(50*scale), frame);
				
			}
			
			else if(object.getType().equals(RealWorldObjectType.WORM)){
				Worm wormObject = (Worm)object;
				Image worm = null;
				try {
					worm = ImageIO.read(new File("worm.jpg"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
				worm = makeColorTransparent((BufferedImage) worm,0);
				offscr.drawImage(worm,(int)wormObject.getLocation()[0] -wormObject.getSize()/2,(int)wormObject.getLocation()[1]-wormObject.getSize()/2, frame);	
			}
				
		}
		return img;
	}
	
	
	public static Image makeColorTransparent(BufferedImage im,final double darkness) {
        ImageFilter filter = new RGBImageFilter() {

            // the color we are looking for... Alpha bits are set to opaque
          
            public final int filterRGB(int x, int y, int rgb) {
            	
            	int r = (rgb & 0x00FF0000) >>16;
            	int g = (rgb & 0x0000FF00)>> 8;
            	int b = rgb & 0x000000FF;
            	int o=  (rgb & 0xFF000000);
            
            	
            
                if ((rgb | 0xFF000000) == 0xFFFFFFFF ||(r > 15  && r <23 && g >19  && g <28 && b>190 && b<215)) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                	r -=r*darkness*0.5;
                	g-=g*darkness*0.5;
                	b -=b*darkness*0.5;
                    return o+(r<<16)+(g<<8)+b;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }


	public GUIHelper(World world, JFrame frame) {
		super();
		this.world = world;
		this.frame = frame;
	}
	
	
}
