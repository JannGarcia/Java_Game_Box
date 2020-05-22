package Game.Galaga.Entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by AlexVR on 1/25/2020
 */
public class EntityManager {
	
	Random random = new Random();
	
	
    public ArrayList<BaseEntity> entities;
    public PlayerShip playerShip;
    public ArrayList<BaseEntity> shoot;

    public EntityManager(PlayerShip playerShip) {
        entities = new ArrayList<>();
        this.playerShip = playerShip;
        shoot = new ArrayList<>();
    }

    public void tick(){
        playerShip.tick();
        ArrayList<BaseEntity> toRemove = new ArrayList<>();
        
        for ( BaseEntity shot : shoot) {
        	
        	if (!entities.contains(shot)) {
        		this.entities.add(shot);
        	}
        
        	
        }
        
        for (BaseEntity entity: entities){
            if (entity.remove){
                toRemove.add(entity);
                continue;
            }
            entity.tick();
            if (entity.bounds.intersects(playerShip.bounds) && !playerShip.isDestroyed()){
                playerShip.damage(entity);
            }
        }
        for (BaseEntity toErase:toRemove){
            entities.remove(toErase);
        }

    }

    public void render(Graphics g){
        for (BaseEntity entity: entities){
            entity.render(g);
        }
        playerShip.render(g);

    }

}
