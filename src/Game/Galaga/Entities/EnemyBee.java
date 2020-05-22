package Game.Galaga.Entities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;


public class EnemyBee extends Enemy {

    boolean lockedOn = false;
    int waitCooldown;
    
    
    public EnemyBee(int x, int y, int width, int height, Handler handler,int row, int col) {
        super(x, y, width, height, Images.galagaEnemyBee[0], handler, row, col);
        BufferedImage[] idleAnimList= new BufferedImage[2];
        idleAnimList[0] = Images.galagaEnemyBee[0];
        idleAnimList[1] = Images.galagaEnemyBee[1];
        idle = new Animation(512,idleAnimList);
        turn90Left = new Animation(128,Images.galagaEnemyBee);
        spawn();
        
        
        // [5, 10] seconds on hard, [5, 18] otherwise
        waitCooldown = handler.getGalagaState().difficulty.equals("hard") ? (random.nextInt(6 * 60)) + 5 * 60 : (random.nextInt(13 * 60)) + 5 * 60;

    }


    public void tick() {
    	super.tick();
    	
    	if (!justSpawned) {
    		if (positioned){
            	// Use the randomly generated cooldown above (when enemy was created) to attack the player
            	if (waitCooldown == 0) {
            		attacking = true;
                	positioned = false;
            	}
            	
            	else {waitCooldown--;}

            }
    		else if (attacking){attack();}
    	}
    }
    



    
    public void attack() {
    	
    	// Dummy variables only used when He-Man calls this function
    	attacking = true; 
    	positioned = false;


    	// Keep getting the x coordinates of the ship
        
        int shipX = handler.getGalagaState().entityManager.playerShip.x;
        shipX += handler.getGalagaState().entityManager.playerShip.width / 2;
        // int shipY = handler.getGalagaState().entityManager.playerShip.y;

        if (lockedOn) { 
        	
            y += speed;  // Enemy will only move down
        

            // If enemy moves offscreen
        	if (this.y > handler.getHeight() + 50) {
        		playerDamage = false;  // Player never damaged it
        		lockedOn = false;  // It's not locked on anymore
        		attacking = false;  // It's not attacking anymore
        		playSound = false;  // Sound gets annoying
            	damage(new PlayerLaser(0,0,0,0, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));
            }
        
        }
        
        // Move left/right depending on ship's position
        else if (Point2D.distance(x,y,shipX,y)>speed/2) {
            if (x > shipX) {
                x -= speed;  // Left
            } 
            
            else {
                x += speed;  // Right
            }
        }
        
        else {
        	lockedOn = true;
        }


    
    }
}
