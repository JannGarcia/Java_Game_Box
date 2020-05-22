package Game.PacMan.entities.Dynamics;

import Game.PacMan.World.MapBuilder;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BoundBlock;
import Game.PacMan.entities.Statics.SpawnerGate;
import Game.PacMan.entities.Statics.TransportBlock;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Ghost extends BaseDynamic{

    protected double velX,velY,speed, originalSpeed;
    private boolean speedIsNormal = true;
    public int spawnerX, spawnerY;
    public String ghostName = "Blinky";
    public String facing = "Left";
    public boolean moving = true,dead = false, leftHome = false;
    public boolean up = false,down = false,left = false, right = false;
    public Animation leftAnim,rightAnim,upAnim,downAnim,turnToBlue;
    public BufferedImage eyesRight, eyesLeft, eyesUp, eyesDown;
    private boolean edible = false;
    private final int EDIBLE_COOLDOWN_TIME = 60*10;
    private int edibleCooldown = 0;
    private int teleportCooldown;
    private double sizeFactor = 1.6; // used to determine rectangle size when checking for intersections
    private int ghostHome = new Color(253,236,166).getRGB();
    private int boundBlock = MapBuilder.boundBlock;
    private int gate = MapBuilder.gate;
    private int spawner = MapBuilder.ghostC;
    
    Random random = new Random();
    // [3, 8] seconds
    int deadCooldown = random.nextInt(5*60) + 3 * 60;

    // Each ghost should be random
    public Ghost(int x, int y, int width, int height, double speedMultiplier, Handler handler, String ghostName, Boolean galagaSprites) {
        super(x, y, width, height, handler, Images.ghost);
        this.ghostName = ghostName;
        eyesRight = Images.eyesRight;
        eyesLeft = Images.eyesLeft;
        eyesUp = Images.eyesUp;
        eyesDown = Images.eyesDown;
        turnToBlue = new Animation(128, Images.turnToBlue);


        if (galagaSprites) {
            BufferedImage[] anim;
            switch (random.nextInt(3)) {
                case 0:
                    anim = Images.galagaEnemyBee;
                    break;
                case 1:
                    anim = Images.galagaEnemyButterfly;
                    break;
                default:
                    anim = Images.galagaEnemyBomber;
            }
            leftAnim = new Animation(512,anim);
            rightAnim = new Animation(512,anim);
            upAnim = new Animation(512,anim);
            downAnim = new Animation(512,anim);
            this.speed = 2 * speedMultiplier;
            this.originalSpeed = 2 * speedMultiplier;
        } else {
            switch(ghostName){
                case "Pinky":
                    this.speed = 1.5 * speedMultiplier;
                    this.originalSpeed = 1.5 * speedMultiplier;
                    leftAnim = new Animation(128,Images.pinkyLeft);
                    rightAnim = new Animation(128,Images.pinkyRight);
                    upAnim = new Animation(128,Images.pinkyUp);
                    downAnim = new Animation(128,Images.pinkyDown);
                    break;
                case "Inky":
                    this.speed = 1.6 * speedMultiplier;
                    this.originalSpeed = 1.6 * speedMultiplier;
                    leftAnim = new Animation(128,Images.inkyLeft);
                    rightAnim = new Animation(128,Images.inkyRight);
                    upAnim = new Animation(128,Images.inkyUp);
                    downAnim = new Animation(128,Images.inkyDown);
                    break;
                case "Clyde":
                    this.speed = 1.4 * speedMultiplier;
                    this.originalSpeed = 1.4 * speedMultiplier;
                    leftAnim = new Animation(128,Images.clydeLeft);
                    rightAnim = new Animation(128,Images.clydeRight);
                    upAnim = new Animation(128,Images.clydeUp);
                    downAnim = new Animation(128,Images.clydeDown);
                    break;
                default:
                    this.speed = 1.7 * speedMultiplier;
                    this.originalSpeed = 1.7 * speedMultiplier;
                    leftAnim = new Animation(128,Images.blinkyLeft);
                    rightAnim = new Animation(128,Images.blinkyRight);
                    upAnim = new Animation(128,Images.blinkyUp);
                    downAnim = new Animation(128,Images.blinkyDown);
            }
        }
    }

    @Override
    public void tick(){
    	
    	if (speed < 1) {this.speed = this.originalSpeed;}
    	
        if (teleportCooldown > 0) {teleportCooldown--;}
    	
    	if (dead) {
            if (deadCooldown <= 0) {
            	dead = false;
            	edible = false;
            	resetEdibleCooldown();
                deadCooldown = random.nextInt(5*60) + 3 * 60;
        		this.x = spawnerX;
        		this.y = spawnerY;
        		this.facing = "Up";
        		this.leftHome = false;
            }
            else {
            	deadCooldown--;
            }
    	}

    	if (edibleCooldown < 0) {
            resetEdibleCooldown();
            edible = false;
        }
    	

    	if (!edible) {
            if (!speedIsNormal) {
                this.speed = this.originalSpeed;
                speedIsNormal = true;
            }
            switch (facing) {
                case "Right":
                    x = (int) Math.ceil(x + velX);
                    rightAnim.tick();
                    break;
                case "Left":
                    x = (int) Math.floor(x - velX);
                    leftAnim.tick();
                    break;
                case "Up":
                    y = (int) Math.floor(y - velY);
                    upAnim.tick();
                    break;
                case "Down":
                    y = (int) Math.ceil(y + velY);
                    downAnim.tick();
            }
        }
    	// if edible
    	else {
    	    if (speedIsNormal) {
                speed = speed / 2;
                if (speed <= 1) {speed = 1;}
                speedIsNormal = false;
            }
            edibleCooldown--;
            turnToBlue.tick();
            switch (facing){
                case "Right":
                    x+=velX;
                    break;
                case "Left":
                    x-=velX;
                    break;
                case "Up":
                    y-=velY;
                    break;
                case "Down":
                    y+=velY;
            }
        }

        BGMA();
        if (!handler.getPacman().hit) {
    	    AGMA();
    	}
    }


    public void checkVerticalCollision() {
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamic> enemies = handler.getMap().getEnemiesOnMap();
        ArrayList<TransportBlock> transBlocks = handler.getMap().getTransportBlocks();
        velY = this.speed;
        boolean toUp = moving && facing.equals("Up");

        Rectangle ghostBounds = this.getBounds();

        for(BaseDynamic enemy : enemies){
            Rectangle enemyBounds = enemy.getBounds();
            // Rectangle enemyBounds = !toUp ? enemy.getTopBounds() : enemy.getBottomBounds();
            if (enemy instanceof PacMan) {
                if (ghostBounds.intersects(enemyBounds) && this.edible) {
                    damage();
                    return;
                }
            }
        }
        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock || (brick instanceof SpawnerGate && dead) || (brick instanceof SpawnerGate && leftHome)) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (ghostBounds.intersects(brickBounds)) {
                    velY = 0;
                    if (toUp)
                        this.setY(brick.getY() + this.getDimension().height);
                    else
                        this.setY(brick.getY() - brick.getDimension().height);
                }
            }
        }
        for (TransportBlock trans : transBlocks) {
            Rectangle blockBounds = !toUp ? trans.getRightBounds() : trans.getLeftBounds();

            if (ghostBounds.intersects(blockBounds) && teleportCooldown <= 0) {
                teleportFrom(trans);
                teleportCooldown = 120;
            }
        }
    }


    public boolean checkPreVerticalCollision(String facing) {
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        velY = this.speed;
        boolean toUp = moving && facing.equals("Up");

        Rectangle ghostBounds = toUp ? this.getTopBounds() : this.getBottomBounds();
        ghostBounds.setSize((int) (ghostBounds.width / sizeFactor) , (int) (ghostBounds.height / sizeFactor));

        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock || (brick instanceof SpawnerGate && dead) || (brick instanceof SpawnerGate && leftHome)) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (ghostBounds.intersects(brickBounds)) {
                    return false;
                }
            }
        }
        return true;
    }


    public void checkHorizontalCollision(){
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamic> enemies = handler.getMap().getEnemiesOnMap();
        ArrayList<TransportBlock> transBlocks = handler.getMap().getTransportBlocks();
        velX = this.speed;
        boolean toRight = moving && facing.equals("Right");

        Rectangle ghostBounds = this.getBounds();

        for(BaseDynamic enemy : enemies){
            Rectangle enemyBounds = enemy.getBounds();
            if (enemy instanceof PacMan) {
                if (ghostBounds.intersects(enemyBounds) && this.edible) {
                    damage();
                    return;
                }
            }
        }
        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock || (brick instanceof SpawnerGate && dead) || (brick instanceof SpawnerGate && leftHome)) {
                Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
                if (ghostBounds.intersects(brickBounds)) {
                    velX = 0;
                    if (toRight)
                        this.setX(brick.getX() - this.getDimension().width);
                    else
                        this.setX(brick.getX() + brick.getDimension().width);
                }
            }
        }
        for (TransportBlock trans : transBlocks) {
            Rectangle blockBounds = !toRight ? trans.getRightBounds() : trans.getLeftBounds();

            if (ghostBounds.intersects(blockBounds) && teleportCooldown <= 0) {
                teleportFrom(trans);
                teleportCooldown = 120;
            }
        }
    }


    public boolean checkPreHorizontalCollision(String facing){
        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        velX = this.speed;
        boolean toRight = moving && facing.equals("Right");

        Rectangle ghostBounds = toRight ? this.getRightBounds() : this.getLeftBounds();
        ghostBounds.setSize((int) (ghostBounds.width / sizeFactor) , (int) (ghostBounds.height / sizeFactor));

        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock || (brick instanceof SpawnerGate && dead) || (brick instanceof SpawnerGate && leftHome)) {
                Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
                if (ghostBounds.intersects(brickBounds)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Basic Ghost Moving Algorithm!
    private void BGMA() {
        if (facing.equals("Right") || facing.equals("Left")){
            checkHorizontalCollision();
            if (velX == 0) {
            	facing = new Random().nextBoolean() ? "Up" : "Down" ;
            }
        } else{
            checkVerticalCollision();
            if (velY == 0) {
            	facing = new Random().nextBoolean() ? "Left" : "Right" ;
            }
        }
    }
    
    // Advanced Ghost Moving Algorithm!
    private void AGMA() {

        if (random.nextInt(15) > 0)
            return;

        updateMapCoords();
        int currentLocation = handler.getMap().currentMap.getRGB(this.getxOnMap(), this.getyOnMap());
        if (currentLocation == this.ghostHome || currentLocation == this.gate || currentLocation == this.spawner || currentLocation == boundBlock)
            return;
        else
            leftHome = true;

        int pacmanX = handler.getPacman().getX(), pacmanY = handler.getPacman().getY();

        switch (random.nextInt(4)) {
            case 0: // goes left
                if(checkPreHorizontalCollision("Left") && !facing.equals("Left") && !facing.equals("Right") && (edible ? pacmanX >= this.x : pacmanX < this.x))
                    facing = "Left";
                break;
            case 1: // goes right
                if(checkPreHorizontalCollision("Right") && !facing.equals("Right") && !facing.equals("Left") && (edible ? pacmanX < this.x : pacmanX >= this.x))
                    facing = "Right";
                break;
            case 2: // goes up
                if(checkPreVerticalCollision("Up") && !facing.equals("Up") && !facing.equals("Down") && (edible ? pacmanY >= this.y : pacmanY < this.y))
                    facing = "Up";
                break;
            case 3: // goes down
                if(checkPreVerticalCollision("Down") && !facing.equals("Down") && !facing.equals("Up") && (edible ? pacmanY < this.y : pacmanY >= this.y))
                    facing = "Down";
        }
    }

    
    public void updateMapCoords() {
    	Point2D.Double ghost = new Point2D.Double(this.x, this.y);
    	BufferedImage map = handler.getMap().currentMap;
    	int pixelMultiplier = handler.getMap().getPixelMultiplier();
    	int closestX=0, closestY=0;
    	double smallestDist = 0;
    	
		for (int i = 0; i < map.getWidth(); i++) {
			for (int j = 0; j < map.getHeight(); j++) {
				
    	
				int xPos = (i*pixelMultiplier)+((handler.getWidth()-(map.getWidth()*pixelMultiplier)))/2;
				int yPos = (j*pixelMultiplier)+((handler.getHeight()-(map.getHeight()*pixelMultiplier)))/2;
				
				Point2D.Double dot = new Point2D.Double(xPos, yPos);
				double dist = dot.distance(ghost);
				
				if (smallestDist == 0 || dist < smallestDist) {
					closestX = i;
					closestY = j;
					smallestDist = dist;
				}
		
			}
		}
		
		this.xOnMap = closestX; this.yOnMap = closestY;
		
		this.up = yOnMap-1 > 0 && map.getRGB(xOnMap, yOnMap-1) != boundBlock && map.getRGB(xOnMap, yOnMap-1) != gate;                         
		this.down = yOnMap+1 < map.getHeight() && map.getRGB(xOnMap, yOnMap+1) != boundBlock && map.getRGB(xOnMap, yOnMap+1) != gate;
		this.left = xOnMap-1 > 0 && map.getRGB(xOnMap-1, yOnMap) != boundBlock && map.getRGB(xOnMap-1, yOnMap) != gate;
		this.right = xOnMap+1 < map.getHeight() && map.getRGB(xOnMap+1, yOnMap) != boundBlock && map.getRGB(xOnMap+1, yOnMap) != gate;
       

    }


    public boolean blockBetweenPacMan(String dir) {
    	int x = this.getxOnMap(), y = this.getyOnMap();
    	int x1 = handler.getPacman().getxOnMap(), y1 = handler.getPacman().getyOnMap();
    	dir = dir.toLowerCase();
    	int distance = 0;
    	int blocksAway = 0;
    	boolean blockFound = false;
    	
    	switch(dir) {

		case "up":
			blocksAway = y - y1;
			while(!blockFound && (distance <= blocksAway)) {
				int currentPixel = handler.getMap().currentMap.getRGB(x, y-distance);
				if (currentPixel == boundBlock) {blockFound = true;}
				distance++;	
			}
			break;
    	case "down":
    		blocksAway = y1-y;
			while(!blockFound && (distance <= blocksAway)) {
				int currentPixel = handler.getMap().currentMap.getRGB(x, y+distance);
				if (currentPixel == boundBlock) {blockFound = true;}
				distance++;	
			}
    		break;
    	case "left":
    		blocksAway = x - x1;
			while(!blockFound && (distance <= blocksAway)) {
				int currentPixel = handler.getMap().currentMap.getRGB(x-distance, y);
				if (currentPixel == boundBlock) {blockFound = true;}
				distance++;	
			}
    		break;
    	case "right":
    		blocksAway = x1 - x;
			while(!blockFound && (distance <= blocksAway)) {
				int currentPixel = handler.getMap().currentMap.getRGB(x+distance, y);
				if (currentPixel == boundBlock) {blockFound = true;}
				distance++;	
			}
    		break;
	
    	}
    	return blockFound;
    }
    

    private void teleportFrom(TransportBlock e) {
    	// If the block has no destination, don't teleport
    	if (e.getDestination() == null) {
    		return;
    	}
    	
    	// Set Pacman's coordinates to the destination's x and y
    	this.x = e.getDestination().x;
    	this.y = e.getDestination().y;
    }

    public double getVelX() {
        return velX;
    }
    public double getVelY() {
        return velY;
    }
    
    public void damage() {
        // [3, 8] seconds
        deadCooldown = random.nextInt(5*60) + 3 * 60;
        leftHome = false;
        handler.getMusicHandler().playEffect("pacman_eatGhost.wav");
        edible = false;
        resetEdibleCooldown();
        dead = true;
		x = spawnerX;
		y = spawnerY;
		handler.getScoreManager().addPacmanCurrentScore(500);
    }

    public boolean isEdible() {
        return edible;
    }

    public void setEdible(boolean edible) {
        this.edible = edible;
    }

    public void resetEdibleCooldown() {
        this.edibleCooldown = EDIBLE_COOLDOWN_TIME;
    }

    public int getEdibleCooldown() {
        return edibleCooldown;
    }
}
