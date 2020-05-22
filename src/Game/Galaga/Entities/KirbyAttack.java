package Game.Galaga.Entities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.image.BufferedImage;

/**
 * Created by AlexVR on 1/25/2020
 */
public class KirbyAttack extends BaseEntity {

    EntityManager enemies;
    Animation idle;
    int speed = 6;
    int direction;

    
    public KirbyAttack(int x, int y, int width, int height, BufferedImage sprite, Handler handler,EntityManager enemies) {
        super(x, y, width, height, sprite, handler);
        this.enemies=enemies;
        BufferedImage[] idleAnimList= new BufferedImage[2];
        idleAnimList[0] = Images.KirbyPowerUp[0];
        idleAnimList[1] = Images.KirbyPowerUp[1];
        idle = new Animation(512,idleAnimList);
        
        direction = handler.getWidth() / 2 < this.x ? 1 : 0;
        
        
    }
    
    @Override
    public void tick() {
        if (!remove) {
        	idle.tick();
            super.tick();
            
            if (direction == 0) { // Come from left
            	x+=speed;
            	if (handler.getWidth() + 50 <= bounds.x) {
            		remove = true;
                
            	}
            	
            }
             
            else if (direction == 1) {  // Come from right
            	x-=speed;
            	if ( 50 >= bounds.x) {
            		remove = true;

            	}
            	
            }
            

            	
            }
            
            for (BaseEntity enemy : enemies.entities) {
                if (enemy instanceof PlayerShip || enemy instanceof PlayerLaser) {
                    continue;
                }
                
                
                if (enemy.bounds.intersects(bounds)) {
                	if (enemy instanceof EnemyBee) {
                		((EnemyBee) enemy).damage(this);
                	}
                	
                	else if (enemy instanceof NewEnemy) {
                		((NewEnemy)enemy).damage(this);
                	}

                }
              }
   
        bounds.x=x;
        bounds.y=y;
    }
}
   
