package Game.Galaga.Entities;

import Main.Handler;

import java.awt.image.BufferedImage;

/**
 * Created by AlexVR on 1/25/2020
 */
public class EnemyLaser extends BaseEntity {

    PlayerShip ship = handler.getGalagaState().entityManager.playerShip;
    // EntityManager enemies; 
    int speed = 4;


    public EnemyLaser(int x, int y, int width, int height, BufferedImage sprite, Handler handler,EntityManager enemies) {
        super(x, y, width, height, sprite, handler);

    }

    @Override
    public void tick() {
    	// Hard Mode: *1.5, Easy Mode: * 1
    	speed = handler.getGalagaState().difficulty.equals("hard") ? 6 : 4;
    	
        if (!remove) {
            super.tick();
            y += speed;
            bounds.y = y;
            
            if (ship.bounds.intersects(bounds) && !ship.isDestroyed()) {
            	ship.damage(this);
            	remove = true;
            }
            
        }
    }
}
