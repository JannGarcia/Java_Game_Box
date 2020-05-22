package Game.PacMan.entities.Dynamics;

import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BoundBlock;
import Game.PacMan.entities.Statics.SpawnerGate;
import Game.PacMan.entities.Statics.TransportBlock;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PacMan extends BaseDynamic{
	
	// We use velX and velY to stop Pac-Man when he hits a wall
    protected double velX,velY,speed = 1.8;
    public int originX, originY;
    public String facing = "Left";
    public boolean moving = true,turnFlag = false;
    public Animation leftAnim,rightAnim,upAnim,downAnim, deathAnimation;
    public int turnCooldown = 1;
    public int health = 3;
    public boolean hit = false;
    private boolean playedSound;
    private int teleportCooldown, invulnerableCooldown;


    public PacMan(int x, int y, int width, int height, double speedMultiplier, Handler handler) {
        super(x, y, width, height, handler, Images.pacmanRight[0]);
        leftAnim = new Animation(128,Images.pacmanLeft);
        rightAnim = new Animation(128,Images.pacmanRight);
        upAnim = new Animation(128,Images.pacmanUp);
        downAnim = new Animation(128,Images.pacmanDown);
        deathAnimation = new Animation(128, Images.pacmanDeath);
        this.speed *= speedMultiplier;
    }

    @Override
    public void tick(){
    	
    	
        if (invulnerableCooldown > 0) {
            invulnerableCooldown--;
        }

        // if dead or outside
        if (hit || isOutOfBounds()){
            if (!playedSound) {
                handler.getMusicHandler().playEffect("pacman_death.wav");
                playedSound = true;
            }

            if (deathAnimation.end){
            	health--;
            	x = originX;
            	y = originY;
                hit=false;
                deathAnimation.reset();
                handler.getPacManState().respawnCooldown = 60 * 2;
                invulnerableCooldown = 60 * 3;  // 1 second of cooldown
                
                // Game Over
                if (health <= 0) {
                	handler.getPacManState().Mode = "GameOver";
                }
            }
            
            else{
                deathAnimation.tick();
            }
        }
       

        // if not dead
        else {
        	updateMapCoords();
            playedSound = false;
            
            if (teleportCooldown > 0) {teleportCooldown--;}

            switch (facing){
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
                    break;
            }

            if (turnCooldown<=0){
                turnFlag= false;
            }
            if (turnFlag){
                turnCooldown--;
            }

            if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)  || handler.getKeyManager().keyJustPressed(KeyEvent.VK_D)) && !turnFlag && checkPreHorizontalCollision("Right")){
                facing = "Right";
                turnFlag = true;
                turnCooldown = 1;
            }else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_A)) && !turnFlag && checkPreHorizontalCollision("Left")){
                facing = "Left";
                turnFlag = true;
                turnCooldown = 1;
            }else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)  ||handler.getKeyManager().keyJustPressed(KeyEvent.VK_W)) && !turnFlag && checkPreVerticalCollision("Up")){
                facing = "Up";
                turnFlag = true;
                turnCooldown = 1;
            }else if ((handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)  || handler.getKeyManager().keyJustPressed(KeyEvent.VK_S)) && !turnFlag && checkPreVerticalCollision("Down")){
                facing = "Down";
                turnFlag = true;
                turnCooldown = 1;
            }

            // some extra debug commands

            // kills ghosts
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_Z) && Handler.DEBUG) {
                for (BaseDynamic entity:handler.getMap().getEnemiesOnMap()) {
                    if (entity instanceof Ghost) {
                        ((Ghost) entity).dead = true;
                        ((Ghost) entity).setEdible(false);
                        ((Ghost) entity).resetEdibleCooldown();
                    }
                }
            }
            // makes ghosts edible
            else if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_X) && Handler.DEBUG) {
                for (BaseDynamic entity:handler.getMap().getEnemiesOnMap()) {
                    if (entity instanceof Ghost) {
                        ((Ghost) entity).dead = false;
                        ((Ghost) entity).setEdible(true);
                        ((Ghost) entity).resetEdibleCooldown();
                    }
                }
            }
            // makes ghosts not edible
            else if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_V) && Handler.DEBUG) {
                for (BaseDynamic entity:handler.getMap().getEnemiesOnMap()) {
                    if (entity instanceof Ghost) {
                        ((Ghost) entity).dead = false;
                        ((Ghost) entity).setEdible(false);
                        ((Ghost) entity).resetEdibleCooldown();
                    }
                }
            }
            if (facing.equals("Right") || facing.equals("Left")){
                checkHorizontalCollision();
            } else{
                checkVerticalCollision();
            }
        }
    }


    public void checkVerticalCollision() {
        // Checks if pacman is colliding with another object above or below

        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamic> enemies = handler.getMap().getEnemiesOnMap();
        ArrayList<TransportBlock> transBlocks = handler.getMap().getTransportBlocks();
        velY = speed;
        boolean toUp = moving && facing.equals("Up");

        // Rectangle pacmanBounds = toUp ? pacman.getTopBounds() : pacman.getBottomBounds();
        Rectangle pacmanBounds = this.getBounds();

        // checks if pacman is colliding with bricks
        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock || brick instanceof SpawnerGate) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (pacmanBounds.intersects(brickBounds)) {
                    velY = 0;
                    if (toUp)
                        this.setY(brick.getY() + this.getDimension().height);
                    else
                        this.setY(brick.getY() - brick.getDimension().height);
                }
            }
        }
        // checks if pacman is colliding with an enemy
        for(BaseDynamic enemy : enemies){
            // Rectangle enemyBounds = !toUp ? enemy.getTopBounds() : enemy.getBottomBounds();
            Rectangle enemyBounds = enemy.getBounds();
            if (enemy instanceof Ghost) {
                if (pacmanBounds.intersects(enemyBounds) && !((Ghost)enemy).isEdible() && !hit && invulnerableCooldown <= 0) {
                    hit = true;
                    break;
                }
            }
        }
        for (TransportBlock trans : transBlocks) {
        	Rectangle blockBounds = !toUp ? trans.getTopBounds() : trans.getBottomBounds();
        	if (pacmanBounds.intersects(blockBounds) && teleportCooldown <= 0) {
        		teleportFrom(trans);
        		teleportCooldown = 120;
        	}
        }
    }


    public void checkHorizontalCollision(){
        // Checks if pacman is colliding with another object to the left or right

        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        ArrayList<BaseDynamic> enemies = handler.getMap().getEnemiesOnMap();
        ArrayList<TransportBlock> transBlocks = handler.getMap().getTransportBlocks();
        velX = speed;
        boolean toRight = moving && facing.equals("Right");

        // Rectangle pacmanBounds = toRight ? pacman.getRightBounds() : pacman.getLeftBounds();
        Rectangle pacmanBounds = this.getBounds();

        // checks if pacman is colliding with bricks
        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock || brick instanceof SpawnerGate) {
                Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
                if (pacmanBounds.intersects(brickBounds)) {
                    velX = 0;
                    if (toRight)
                        this.setX(brick.getX() - this.getDimension().width);
                    else
                        this.setX(brick.getX() + brick.getDimension().width);
                }
            }
        }
        // checks if pacman is colliding with an enemy
        for(BaseDynamic enemy : enemies){
            // Rectangle enemyBounds = !toRight ? enemy.getRightBounds() : enemy.getLeftBounds();
        	Rectangle enemyBounds = enemy.getBounds();
            if (enemy instanceof Ghost) {
                if (pacmanBounds.intersects(enemyBounds) && !((Ghost)enemy).isEdible() && !hit && invulnerableCooldown <= 0) {
                    hit = true;
                    break;
                }
            }
        }
        for (TransportBlock trans : transBlocks) {
            Rectangle blockBounds = !toRight ? trans.getRightBounds() : trans.getLeftBounds();

            if (pacmanBounds.intersects(blockBounds) && teleportCooldown <= 0) {
                teleportFrom(trans);
                teleportCooldown = 120;
            }
        }
    }


    public boolean checkPreVerticalCollision(String facing) {
        // Returns true if the given direction (Up or Down) is free and false otherwise

        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        boolean toUp = moving && facing.equals("Up");
        velY = speed;
        Rectangle pacmanBounds = toUp ? this.getTopBounds() : this.getBottomBounds();

        for (BaseStatic brick : bricks) {
            if (brick instanceof BoundBlock || brick instanceof SpawnerGate) {
                Rectangle brickBounds = !toUp ? brick.getTopBounds() : brick.getBottomBounds();
                if (pacmanBounds.intersects(brickBounds)) {
                    return false;
                }
            }
        }
        return true;
    }


    public boolean checkPreHorizontalCollision(String facing){
        // Returns true if the given direction (Left or Right) is free and false otherwise

        ArrayList<BaseStatic> bricks = handler.getMap().getBlocksOnMap();
        boolean toRight = moving && facing.equals("Right");
        velX = speed;
        Rectangle pacmanBounds = toRight ? this.getRightBounds() : this.getLeftBounds();

            for (BaseStatic brick : bricks) {
                if (brick instanceof BoundBlock || brick instanceof SpawnerGate) {
                    Rectangle brickBounds = !toRight ? brick.getRightBounds() : brick.getLeftBounds();
                    if (pacmanBounds.intersects(brickBounds)) {
                        return false;
                    }
                }
            }
        return true;
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
    
    
    public void updateMapCoords() {
        // TODO: implement or remove
    	Point2D.Double pacman = new Point2D.Double(this.x, this.y);
    	BufferedImage map = handler.getMap().currentMap;
    	int pixelMultiplier = handler.getMap().getPixelMultiplier();
    	int closestX=0, closestY=0;
    	double smallestDist = 0;
    	
		for (int i = 0; i < map.getWidth(); i++) {
			for (int j = 0; j < map.getHeight(); j++) {
				
    	
				int xPos = (i*pixelMultiplier)+((handler.getWidth()-(map.getWidth()*pixelMultiplier)))/2;
				int yPos = (j*pixelMultiplier)+((handler.getHeight()-(map.getHeight()*pixelMultiplier)))/2;
				
				Point2D.Double dot = new Point2D.Double(xPos, yPos);
				double dist = dot.distance(pacman);
				
				if (smallestDist == 0 || dist < smallestDist) {
					closestX = i;
					closestY = j;
					smallestDist = dist;
				}
		
			}
		}
		
		this.xOnMap = closestX; this.yOnMap = closestY;

    }


    public double getVelX() {
        return velX;
    }
    public double getVelY() {
        return velY;
    }
    
    public boolean isOutOfBounds(){
    	// Checks if pac man broke the game :)
    	
    	boolean outOfBounds = true;

    	// Right Side
    	for (BaseStatic brick : handler.getMap().getBlocksOnMap()) {
    		if (brick.x > this.x) {
    			outOfBounds = false;
    		}
    	}
    	
    	if (outOfBounds) {
    		return outOfBounds;
    	}
    	
    	// Left Side
    	outOfBounds = true;
    	for (BaseStatic brick : handler.getMap().getBlocksOnMap()) {
    		if (brick.x < this.x) {
    			outOfBounds = false;
    		}
    	}
    	
    	if (outOfBounds) {
    		return outOfBounds;
    	}
    	
    	// Down
    	outOfBounds = true;
    	for (BaseStatic brick : handler.getMap().getBlocksOnMap()) {
    		if (brick.y > this.y) {
    			outOfBounds = false;
    		}
    	}
    	
    	if (outOfBounds) {
    		return outOfBounds;
    	}
    	
    	// Up
    	outOfBounds = true;
    	for (BaseStatic brick : handler.getMap().getBlocksOnMap()) {
    		if (brick.y < this.y) {
    			outOfBounds = false;
    		}
    	}

    	return outOfBounds;
    }

    public boolean isInvulnerable() {
        // returns true when pacman is invulnerable
        return invulnerableCooldown > 0;
    }
}
