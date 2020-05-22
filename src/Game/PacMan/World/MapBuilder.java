package Game.PacMan.World;

import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.HeMan;
import Game.PacMan.entities.Dynamics.PacMan;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Game.PacMan.entities.Statics.BoundBlock;
import Game.PacMan.entities.Statics.Dot;
import Game.PacMan.entities.Statics.Fruit;
import Game.PacMan.entities.Statics.GhostSpawner;
import Game.PacMan.entities.Statics.SpawnerGate;
import Game.PacMan.entities.Statics.TransportBlock;
import Main.Handler;
import Resources.Images;

import java.util.Random;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MapBuilder {


	// Change pixelMultiplier to make the pixels bigger (Default: 18)

	public static int pixelMultiplier;
	public static int boundBlock = new Color(0,0,0).getRGB();
	public static int pacman = new Color(255, 255,0).getRGB();
	public static int ghostC = new Color(25, 255,0).getRGB();
	public static int dotC = new Color(255, 10, 0).getRGB();
	public static int bigDotC = new Color(167, 0, 150).getRGB();
	public static int gate = new Color(0, 10, 255).getRGB();
	public static int transport = new Color(250, 80, 15).getRGB();
	public static int heman = new Color(210, 105, 30).getRGB();
	// fruit spawning
	public static Random random = new Random();
	public static int fruitsSpawned = 0, MINIMUM_FRUITS = 2;

	public static Map createMap(BufferedImage mapImage, Handler handler){
		
		// For He-Man Purposes
		int heManx = (int) (random.nextBoolean() ? handler.getWidth() / 8 : handler.getWidth() * .80);
		HeMan heMan = new HeMan(heManx,-100,60,80, handler);
		
		// Reset Count
		fruitsSpawned = 0;
		
		Map mapInCreation = new Map(handler);
		mapInCreation.currentMap = mapImage;

		// map scaling
		if (mapImage.getWidth() <= mapImage.getHeight())
			pixelMultiplier = (handler.getHeight()-100)/mapImage.getHeight();
		else
			pixelMultiplier = (handler.getWidth()-100)/mapImage.getWidth();

		mapInCreation.setPixelMultiplier(pixelMultiplier);
		double speedMultiplier = pixelMultiplier / 30.0;
		for (int i = 0; i < mapImage.getWidth(); i++) {
			for (int j = 0; j < mapImage.getHeight(); j++) {
				int currentPixel = mapImage.getRGB(i, j);
				// centralizes map
				int xPos = (i*pixelMultiplier)+((handler.getWidth()-(mapInCreation.currentMap.getWidth()*pixelMultiplier)))/2;
				int yPos = (j*pixelMultiplier)+((handler.getHeight()-(mapInCreation.currentMap.getHeight()*pixelMultiplier)))/2;
				if(currentPixel == boundBlock){
					BaseStatic BoundBlock = new BoundBlock(xPos,yPos,pixelMultiplier,pixelMultiplier,handler,getSprite(mapImage,i,j));
					mapInCreation.addBlock(BoundBlock);
				}else if(currentPixel == pacman){
					BaseDynamic PacMan = new PacMan(xPos,yPos,pixelMultiplier,pixelMultiplier,speedMultiplier,handler);
					mapInCreation.addEnemy(PacMan);
					((PacMan) PacMan).originX = xPos;
					((PacMan) PacMan).originY = yPos;
					handler.setPacman((PacMan) PacMan);

				}else if(currentPixel == ghostC){
					// Block to spawn ghosts
					BaseStatic ghostSpawner = new GhostSpawner(xPos,yPos,pixelMultiplier,pixelMultiplier,speedMultiplier,handler);
					mapInCreation.addBlock(ghostSpawner);
				}else if(currentPixel == dotC){
					if (random.nextInt(30) == 0) {
						fruitsSpawned++;
						BaseStatic fruit = new Fruit(xPos,yPos,pixelMultiplier,pixelMultiplier,handler, random.nextInt(Images.pacmanFruits.length));
						mapInCreation.addBlock(fruit);
					} else {
						BaseStatic dot = new Dot(xPos,yPos,pixelMultiplier,pixelMultiplier,handler);
						mapInCreation.addBlock(dot);
					}
				}else if(currentPixel == bigDotC){
					BaseStatic bigDot = new BigDot(xPos,yPos,pixelMultiplier,pixelMultiplier,handler);
					mapInCreation.addBlock(bigDot);
				}
				
				// The gate that prevents pacman from entering
				else if (currentPixel == gate) {
					int up = mapImage.getRGB(i, j-1);
					int down = mapImage.getRGB(i, j+1);
					int left = mapImage.getRGB(i+1, j);
					int right = mapImage.getRGB(i-1, j);
					BufferedImage gateImg = Images.bound[0];
					
					boolean verSurround = up == boundBlock && down == boundBlock;
					boolean horSurround = left == boundBlock && right == boundBlock;
					
					if (verSurround && !horSurround) {gateImg = Images.spawnerGateVer;}
					else if (!verSurround && horSurround) {gateImg = Images.spawnerGate;}

					
					BaseStatic gate = new SpawnerGate(xPos,yPos,pixelMultiplier,pixelMultiplier,handler, gateImg);
					mapInCreation.addBlock(gate);
				}
				
				else if (currentPixel == transport) {
					TransportBlock block = new TransportBlock(xPos,yPos,pixelMultiplier,pixelMultiplier,handler);
					mapInCreation.addTransportBlock(block);
					connectToOtherBlock(block, mapInCreation);
				}
				
				else if (currentPixel == heman) {
					mapInCreation.addEnemy(heMan);
				}
			}

		}
		
		// Fruit failsafe
		if (fruitsSpawned < MINIMUM_FRUITS && dotsOnMap(mapInCreation) > MINIMUM_FRUITS - 1) {
			while (fruitsSpawned < MINIMUM_FRUITS) {
				int i = random.nextInt(mapInCreation.getBlocksOnMap().size());
				BaseStatic b = mapInCreation.getBlocksOnMap().get(i);
				
				// Replace dot with fruit
				if (b instanceof Dot) {
					fruitsSpawned++;
					Fruit fruit = new Fruit(b.x,b.y,pixelMultiplier,pixelMultiplier,handler, random.nextInt(Images.pacmanFruits.length));
					mapInCreation.getBlocksOnMap().set(i, fruit);
				}
			}
		}
		
		return mapInCreation;
	}
	
	private static void connectToOtherBlock(Object e, Map inCreation) {
		
		// If there are no transport blocks added yet
		if (inCreation.getTransportBlocks() == null) {
			return;
		}
		
		if (e instanceof TransportBlock) {
			TransportBlock block = (TransportBlock) e;
			
			// If it already has a destination, return
			if (block.getDestination() != null) {
				return;
			}
			
			// Check every block and find one that does not have a destination
			for (int i = 0; i < inCreation.getTransportBlocks().size(); i++ ) {
				TransportBlock block1 = inCreation.getTransportBlocks().get(i);
				
				// If the block is the one we are checking, don't connect to itself
				if (block1.equals(block)) {continue;}
				
				// If the block is available, connect!
				if (block1.getDestination() == null) {
					inCreation.getTransportBlocks().get(i).setDestination(block);
					block.setDestination(inCreation.getTransportBlocks().get(i));
				}
				
			}
		}
	}
	
	private static int dotsOnMap(Map m) {
		int count = 0;
		
		for (BaseStatic b : m.getBlocksOnMap()) {
			if (b instanceof Dot) {
				count++;
			}
		}
		
		return count;
	}

	private static BufferedImage getSprite(BufferedImage mapImage, int i, int j) {
		// Don't touch this :) 
		// This "connects" blocks
		int currentPixel = boundBlock;
		int leftPixel;
		int rightPixel;
		int upPixel;
		int downPixel;
		if (i>0) {
			 leftPixel = mapImage.getRGB(i - 1, j);
		}else{
			 leftPixel = pacman;

		}
		if (i<mapImage.getWidth()-1) {
			 rightPixel = mapImage.getRGB(i + 1, j);
		}else{
			 rightPixel= pacman;

		}
		if (j>0) {
			 upPixel = mapImage.getRGB(i, j - 1);
		}else{
			 upPixel = pacman;

		}
		if (j<mapImage.getHeight()-1) {
			 downPixel = mapImage.getRGB(i, j + 1);
		}else{
			 downPixel = pacman;

		}

		if (currentPixel != leftPixel && currentPixel != upPixel && currentPixel != downPixel && currentPixel == rightPixel){

			return Images.bound[1];
		}else if (currentPixel != leftPixel && currentPixel != upPixel && currentPixel == downPixel && currentPixel != rightPixel){

			return Images.bound[2];
		}else if (currentPixel == leftPixel && currentPixel != upPixel && currentPixel != downPixel && currentPixel != rightPixel){

			return Images.bound[3];
		}else if (currentPixel != leftPixel && currentPixel == upPixel && currentPixel != downPixel && currentPixel != rightPixel){

			return Images.bound[4];
		}else if (currentPixel != leftPixel && currentPixel == upPixel && currentPixel == downPixel && currentPixel != rightPixel){

			return Images.bound[5];
		}else if (currentPixel == leftPixel && currentPixel != upPixel && currentPixel != downPixel && currentPixel == rightPixel){

			return Images.bound[6];
		}else if (currentPixel != leftPixel && currentPixel == upPixel && currentPixel != downPixel && currentPixel == rightPixel){

			return Images.bound[7];
		}else if (currentPixel == leftPixel && currentPixel == upPixel && currentPixel != downPixel && currentPixel != rightPixel){

			return Images.bound[8];
		}else if (currentPixel != leftPixel && currentPixel != upPixel && currentPixel == downPixel && currentPixel == rightPixel){

			return Images.bound[9];
		}else if (currentPixel == leftPixel && currentPixel != upPixel && currentPixel == downPixel && currentPixel != rightPixel){

			return Images.bound[10];
		}else if (currentPixel == leftPixel && currentPixel == upPixel && currentPixel == downPixel && currentPixel == rightPixel){

			return  Images.bound[11];
		}else if (currentPixel != leftPixel && currentPixel == upPixel && currentPixel == downPixel && currentPixel == rightPixel){

			return Images.bound[12];
		}else if (currentPixel == leftPixel && currentPixel == upPixel && currentPixel == downPixel && currentPixel != rightPixel){

			return Images.bound[13];
		}else if (currentPixel == leftPixel && currentPixel != upPixel && currentPixel == downPixel && currentPixel == rightPixel){

			return Images.bound[14];
		}else if (currentPixel == leftPixel && currentPixel == upPixel && currentPixel != downPixel && currentPixel == rightPixel){

			return Images.bound[15];
		}else{

			return  Images.bound[0];
		}


	}


}
