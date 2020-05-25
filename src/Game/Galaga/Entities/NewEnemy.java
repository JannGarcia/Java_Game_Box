package Game.Galaga.Entities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.image.BufferedImage;


public class NewEnemy extends Enemy {

    int shootCooldown;
    
    public NewEnemy(int x, int y, int width, int height, Handler handler,int row, int col) {
        super(x, y, width, height, Images.galagaNewEnemy[0], handler, row, col);
        BufferedImage[] idleAnimList= new BufferedImage[2];
        idleAnimList[0] = Images.galagaNewEnemy[0];
        idleAnimList[1] = Images.galagaNewEnemy[1];
        idle = new Animation(512,idleAnimList);
        turn90Left = new Animation(128,Images.galagaNewEnemy);
        spawn();
        
        shootCooldown = handler.getGalagaState().difficulty.equals("hard") ? (random.nextInt(2 * 60)) + 5 * 60 : (random.nextInt(6 * 60)) + 5 * 60;
    }
 

    @Override
    public void tick() {
    	super.tick();
    	if (!justSpawned) {
    		if (positioned){
            	// Attack     
            	if (shootCooldown == 0) {
            	    attacking = true;
            	    positioned = false;
            	}
            	shootCooldown-- ;
            }
            
            else if (attacking) {
            	attack();
            	// Baby Mode: [5, 15] seconds, Hard Mode: [5, 7] seconds, Easy Mode: [5, 10] seconds
                shootCooldown = handler.getGalagaState().difficulty.equals("baby") ? random.nextInt(11) * 60  + 5 * 60 
                		: handler.getGalagaState().difficulty.equals("hard") ? random.nextInt(3) * 60 + 5 * 60 
                		: random.nextInt(6) * 60 + 5 * 60;
            	attacking = false;
        	    positioned = true;
            }
    	}
    	
    }


    
    public void attack() {
        handler.getGalagaState().entityManager.shoot.add(new EnemyLaser(this.x + (width / 2), this.y + 3, width / 5, height / 2, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));
    }
}

