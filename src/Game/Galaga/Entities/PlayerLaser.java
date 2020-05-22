package Game.Galaga.Entities;

import Main.Handler;

import java.awt.image.BufferedImage;

/**
 * Created by AlexVR on 1/25/2020
 */
public class PlayerLaser extends BaseEntity {

    EntityManager enemies;
    int speed = 6;

    public PlayerLaser(int x, int y, int width, int height, BufferedImage sprite, Handler handler,EntityManager enemies) {
        super(x, y, width, height, sprite, handler);
        this.enemies=enemies;
    }

    @Override
    public void tick() {
        if (!remove) {
            super.tick();
            y -= speed;
            bounds.y = y;
            for (BaseEntity enemy : enemies.entities) {
                if (enemy instanceof PlayerShip || enemy instanceof PlayerLaser || enemy instanceof EnemyLaser) {
                    continue;
                }          
                
                // The following 3 if statements prevent your bullets from getting eaten by dead enemies
                if (enemy instanceof EnemyBee) {
                	if (((EnemyBee) enemy ).hit == true ) {
                		continue;
                	}
                }
                
                else if (enemy instanceof NewEnemy) {
                	if ( ((NewEnemy) enemy ).hit == true ) {
                		continue;
                	}
                }
                
                else if (enemy instanceof HeMan) {
                	if ( ((HeMan) enemy ).hit == true ) {
                		continue;
                	}
                }
                
                if (enemy.bounds.intersects(bounds)) {
                    enemy.damage(this);
                    this.remove = true;
                    break;
                }
            }
        }
    }
}
