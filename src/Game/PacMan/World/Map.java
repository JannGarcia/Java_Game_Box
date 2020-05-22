package Game.PacMan.World;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Game.PacMan.entities.BaseEntity;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Game.PacMan.entities.Dynamics.HeMan;
import Game.PacMan.entities.Dynamics.PacMan;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Game.PacMan.entities.Statics.BoundBlock;
import Game.PacMan.entities.Statics.Dot;
import Game.PacMan.entities.Statics.Fruit;
import Game.PacMan.entities.Statics.GhostSpawner;
import Game.PacMan.entities.Statics.TransportBlock;
import Main.Handler;
import Resources.Images;

public class Map {

    ArrayList<BaseStatic> blocksOnMap;
    ArrayList<BaseDynamic> enemiesOnMap;
    ArrayList<TransportBlock> transportBlocks;
    Handler handler;

    private double bottomBorder;
    public BufferedImage currentMap;
    private int pixelMultiplier;

    
    
    // The variables below are for heman to control
    public boolean heManOnMap = false;
    public boolean invisibleGhosts, invisibleDots;

    public Map(Handler handler) {
        this.handler=handler;

        this.blocksOnMap = new ArrayList<>();
        this.enemiesOnMap = new ArrayList<>();
        this.transportBlocks = new ArrayList<>();
        bottomBorder=handler.getHeight();
    }


    public void addBlock(BaseStatic block){
        blocksOnMap.add(block);
    }

    public void addEnemy(BaseDynamic entity){
        enemiesOnMap.add(entity);
    }
    
    public void addTransportBlock(TransportBlock e) {
    	transportBlocks.add(e);
    }

    public void drawMap(Graphics2D g2) {
        for (BaseStatic block:blocksOnMap) {
        	
        	if ((block instanceof BigDot || block instanceof Fruit || block instanceof Dot) && invisibleDots) {
        		// The dots are invisible! D:
        	}
        	
        	// Blinking BigDots!
        	else if (block instanceof BigDot) {
        		// If the cooldown has reached 20 or lower, stop drawing it
        		if (((BigDot) block).blinkCooldown < 20) {
        			
        			// Reset counter to blink
        			if (((BigDot) block).blinkCooldown == 0) {
        				((BigDot) block).blinkCooldown = 60;
        			}
        			
        			// Tick until 0.
        			else {
        				((BigDot) block).blinkCooldown--;
        			}
        		}
        		else {
        			((BigDot) block).blinkCooldown--;
        			g2.drawImage(block.sprite, block.x, block.y, block.width, block.height, null);
        		}
        	}
        	
        	else if (block instanceof GhostSpawner) {
        		// Don't draw ghostspawners
        	}
        	
        	else {
        		g2.drawImage(block.sprite, block.x, block.y, block.width, block.height, null);
        	}

        }
        for (BaseDynamic entity:enemiesOnMap) {
        	// Pac-Man is in the enemy list. Keep that in mind
            if (entity instanceof PacMan) {
            	
            	if (((PacMan) entity).hit) {
                    g2.drawImage(((PacMan) entity).deathAnimation.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
            	}
            	
            	else {
            	
                switch (((PacMan) entity).facing){
                    case "Right":
                        g2.drawImage(((PacMan) entity).rightAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Left":
                        g2.drawImage(((PacMan) entity).leftAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Up":
                        g2.drawImage(((PacMan) entity).upAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Down":
                        g2.drawImage(((PacMan) entity).downAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                        break;
                	}
            	}
            }
            
            else if (entity instanceof Ghost) {
            	
            	if (invisibleGhosts) {
            		g2.setColor(Color.BLACK);
            		g2.fillRect(entity.x, entity.y, entity.width, entity.height);
            	}
            	
            	// Eyes
            	else if (((Ghost) entity).dead) {
                    switch (((Ghost) entity).facing){
                    case "Right":
                        g2.drawImage(((Ghost) entity).eyesRight, entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Left":
                        g2.drawImage(((Ghost) entity).eyesLeft, entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Up":
                        g2.drawImage(((Ghost) entity).eyesUp, entity.x, entity.y, entity.width, entity.height, null);
                        break;
                    case "Down":
                        g2.drawImage(((Ghost) entity).eyesDown, entity.x, entity.y, entity.width, entity.height, null);
                        break;
                	}
            	}
            	
            	// Turn_to_blue
            	else if (((Ghost) entity).isEdible()) {
                        g2.drawImage(((Ghost) entity).turnToBlue.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                	}

            	else {
            		switch (((Ghost) entity).facing){
                    	case "Right":
                    		g2.drawImage(((Ghost) entity).rightAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                    		break;
                    	case "Left":
                    		g2.drawImage(((Ghost) entity).leftAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                    		break;
                    	case "Up":
                    		g2.drawImage(((Ghost) entity).upAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                    		break;
                    	case "Down":
                    		g2.drawImage(((Ghost) entity).downAnim.getCurrentFrame(), entity.x, entity.y, entity.width, entity.height, null);
                    		break;
                	}
            	}
            }
            
            
            else if (entity instanceof HeMan) {
            	
            	BufferedImage image = ((HeMan) entity).isAttacking() ? 
            			((HeMan) entity).attackAnim.getCurrentFrame() : ((HeMan) entity).idleAnim.getCurrentFrame();
            	g2.drawImage(image, entity.x, entity.y, entity.width, entity.height, null);

            }
            
            
            else {
                g2.drawImage(entity.sprite, entity.x, entity.y, entity.width, entity.height, null);
            }
        }
        
        for (TransportBlock block : handler.getMap().getTransportBlocks()) {
        	if (block.animationTick < 15) {
        			
        		// Reset counter to blink
        		if (block.animationTick == 0) {
        			block.animationTick = 30;
        		}
        		
        		// Tick until 0.
        		else {
            		g2.drawImage(Images.transportBlock[1], block.x, block.y, block.width, block.height, null);
        			block.animationTick--;
        		}
        	}
        	else {
        		block.animationTick--;
        		g2.drawImage(Images.transportBlock[0], block.x, block.y, block.width, block.height, null);
        	}
        	
        }

    }
    
    public boolean blockBetweenBlocks(BaseEntity b, BaseEntity b2) {

		 int x1 = b.getX(); int x2 = b2.getX();
		 int y1 = b.getY(); int y2 = b2.getY();
		 
		 boolean b2IsAbove = y2 < y1; boolean b2IsRight = x2 > x1;
		 
		 // Diagonal blocks are a big no
		 if (x1 != x2 && y1 != y2) {return true;}
		 
		 else if (x1 == x2 && y1 == y2) {return false;}
		 
		 if (x1 == x2) {
			 for (BaseStatic block : getBlocksOnMap()) {
				 
				 // Same X Coords only
				 if (block.getX() != x1) {continue;}

				 
				 // If y2 < y3 < y1
				 if (b2IsAbove && (y2 < block.y && block.y < y1)) {
					 if (block instanceof BoundBlock) {return true;}
				 }
				 
				 // If y1 < y3 < y2
				 else if (!b2IsAbove && (y1 < block.y && block.y < y2)) {
					 if (block instanceof BoundBlock) {return true;}					 
				 }
			 }
		 }
		 
		 else if (y1 == y2) {
			 for (BaseStatic block : getBlocksOnMap()) {
				 
				 // Same Y Coords only
				 if (block.getY() != y1) {continue;}
				 
				 
				 // If x2 < x3 < x1
				 if (!b2IsRight && (x2 < block.x && block.x < x1)) {
					 if (block instanceof BoundBlock) {return true;}
				 }
				 
				 // If x1 < x3 < x2
				 else if (b2IsRight && (x1 < block.x && block.x < x2)) {
					 if (block instanceof BoundBlock) {return true;}					 
				 }
			 }
		 }

    	return false;
    }
    
    public boolean blocksAhead (BaseDynamic b, int distance, String dir) {
    	dir = dir.toLowerCase();
    	int x = b.getxOnMap(), y = b.getyOnMap();
    	boolean blockFound = false;
    	int boundBlock = MapBuilder.boundBlock;
    	
    	switch(dir) {
    		case "up":
    			while(!blockFound || distance > 0 || y - distance >= 0) {
    				int currentPixel = currentMap.getRGB(x, y-distance);
    				if (currentPixel == boundBlock) {blockFound = true;}
    				distance--;	
    			}
    			break;
        	case "down":
    			while(!blockFound || distance > 0 || y + distance <= this.currentMap.getHeight()) {
    				int currentPixel = currentMap.getRGB(x, y+distance);
    				if (currentPixel == boundBlock) {blockFound = true;}
    				distance--;	
    			}
        		break;
        	case "left":
    			while(!blockFound || distance > 0  || x - distance >= 0) {
    				int currentPixel = currentMap.getRGB(x-distance, y);
    				if (currentPixel == boundBlock) {blockFound = true;}
    				distance--;	
    			}
        		break;
        	case "right":
    			while(!blockFound || distance > 0 || x + distance <= this.currentMap.getWidth()) {
    				int currentPixel = currentMap.getRGB(x+distance, y);
    				if (currentPixel == boundBlock) {blockFound = true;}
    				distance--;	
    			}
        		break;
    	}
    	
    	return blockFound;

    }
    

    public ArrayList<BaseStatic> getBlocksOnMap() {
        return blocksOnMap;
    }

    public ArrayList<BaseDynamic> getEnemiesOnMap() {
        return enemiesOnMap;
    }
    
    public ArrayList<TransportBlock> getTransportBlocks() {
        return transportBlocks;
    }

    // This is useless :D
    public double getBottomBorder() {
        return bottomBorder;
    }

    public void reset() {
    	handler.setMap(MapBuilder.createMap(this.currentMap, handler));
    }


	public int getPixelMultiplier() {
		return pixelMultiplier;
	}


	public void setPixelMultiplier(int pixelMultiplier) {
		this.pixelMultiplier = pixelMultiplier;
	}
}
