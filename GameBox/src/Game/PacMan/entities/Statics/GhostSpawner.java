package Game.PacMan.entities.Statics;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import Game.PacMan.entities.Dynamics.Ghost;
import Main.Handler;
import Resources.Images;

public class GhostSpawner extends BaseStatic {
	
	public boolean blinky, pinky, inky, clyde, full;
	private Random random = new Random();
	public static ArrayList<Ghost> ghosts = new ArrayList<>();
	private double speedMultiplier;

    public GhostSpawner(int x, int y, int width, int height, double speedMultiplier, Handler handler) {
        super(x, y, width, height, handler, Images.bound[0]);
        this.speedMultiplier = speedMultiplier;
    }
    
    public void spawnGhost(boolean override) {
    	
    	if (override) {
        	int ghost = random.nextInt(4);
        	String ghostType = "Blinky";
    		
			switch(ghost) {
				case 0:
					ghostType = "Blinky";
					blinky = true;
					break;
				case 1:
					ghostType = "Pinky";
					pinky = true;
					break;
				case 2:
					ghostType = "Inky";
					inky = true;
					break;
				case 3:
					ghostType = "Clyde";
					clyde = true;
					break;
			}
			Ghost ghostToSpawn = new Ghost(this.x, this.y, this.width, this.height, this.speedMultiplier, this.handler, ghostType, handler.getPacManState().isUsingGalagaSprites());
			ghostToSpawn.facing = determineDirection();
			ghostToSpawn.spawnerX = this.x;
			ghostToSpawn.spawnerY = this.y;
			handler.getMap().getEnemiesOnMap().add(ghostToSpawn);
			handler.getPacManState().ghostCount++;
			return;
    	}
    	
    	full = blinky && inky && pinky && clyde;
    	
    	if (full) {return;}
    	
    	int ghost = random.nextInt(4);
    	boolean invalidGhost = true;
    	String ghostType = "Blinky";


        while(invalidGhost) {
            ghost = random.nextInt(4);
		switch (ghost) {
			case 0:
				if (blinky) {continue;}
				ghostType = "Blinky";
				invalidGhost = false;
				blinky = true;
				break;
			case 1:
				if (pinky) {continue;}
				ghostType = "Pinky";
				invalidGhost = false;
				pinky = true;
				break;
			case 2:
				if (inky) {continue;}
				ghostType = "Inky";
				invalidGhost = false;
				inky = true;
				break;
			case 3:
				if (clyde) {continue;}
				ghostType = "Clyde";
				invalidGhost = false;
				clyde = true;
				break;
			}
        }
    	
    	if (blinky && pinky && inky && clyde) {full = true;}



		Ghost ghostToSpawn = new Ghost(this.x, this.y, this.width, this.height, this.speedMultiplier, this.handler, ghostType, handler.getPacManState().isUsingGalagaSprites());
    	ghostToSpawn.facing = determineDirection();
    	ghostToSpawn.spawnerX = this.x;
    	ghostToSpawn.spawnerY = this.y;
    	handler.getMap().getEnemiesOnMap().add(ghostToSpawn);
    	handler.getPacManState().ghostCount++;
    }
    
    
    private String determineDirection() {
    	/* Determines the direction a ghost should face when spawned
    	 * based on the NEAREST SpawnerGate
    	 * 
    	 * Default Value for Direction: Up
    	 *
    	 * NOTES:
    	 * - We use Point2D.Double's distance method to determine the CLOSEST spawner gate
    	 */
    	
    	
    	String dir = "Up";
    	Point2D.Double loc = new Point2D.Double(this.x, this.y);
    	double smallestDist = 0;
    	
    	for (BaseStatic b : handler.getMap().getBlocksOnMap()) {
    		if (b instanceof SpawnerGate) {
    			SpawnerGate gate = (SpawnerGate) b;
    			Point2D.Double otherLoc = new Point2D.Double(gate.x, gate.y);
    			boolean blocksBetween = handler.getMap().blockBetweenBlocks(this, b);
    			double dist = loc.distance(otherLoc);
    			if (smallestDist == 0 || dist < smallestDist) {
    				
    				if (gate.x == this.x && !blocksBetween) {dir = ((gate.y < this.y) ? "Up" : "Down"); smallestDist = dist;}
    			
    				else if (gate.y == this.y && !blocksBetween) {dir = ((gate.x < this.x) ? "Left" : "Right"); smallestDist = dist;}
    			
    			}
    			
    		}
    	}
    	
    	return dir;
    }

}