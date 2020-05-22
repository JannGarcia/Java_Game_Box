package Game.Zelda.Entities.Dynamic;

import static Game.Zelda.Entities.Dynamic.Direction.UP;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Game.GameStates.Zelda.ZeldaGameState;
import Game.Zelda.Entities.BaseEntity;
import Game.Zelda.Entities.Statics.Item;
import Game.Zelda.Entities.Statics.SectionDoor;
import Game.Zelda.Entities.Statics.SolidStaticEntities;
import Main.Handler;
import Resources.Animation;

/**
 * Created by AlexVR on 3/15/2020
 */
public class BaseMovingEntity extends BaseEntity {

    int speed;
    Direction direction;
    public Animation animation;
    BufferedImage[] sprites;
    public boolean moving = false;
    boolean hit = false;
    boolean dead = false;
    int damageCooldown = 0;
	Direction shoveDir = UP;


    Rectangle interactBounds;
    public int health = 1;
    public int power = 1;
    
    // How fast the enemy should be knockbacked
    public int knockBack;

    public BaseMovingEntity(int x, int y, BufferedImage[] sprite, Handler handler) {
        super(x, y, sprite[0], handler);
        animation = new Animation(256,sprite);
        bounds = new Rectangle((x * (ZeldaGameState.stageWidth/16)) + ZeldaGameState.xOffset,(y * (ZeldaGameState.stageHeight/11)) + ZeldaGameState.yOffset,width,height);
        speed=2;
        knockBack = speed;
        direction = UP;
        sprites = sprite;
        interactBounds = (Rectangle) bounds.clone();
        interactBounds.y+=(height/2);
        interactBounds.height/=2;
    }

    @Override
    public void tick() {
        if (isStuck()) {unStuck();}
    }

    @Override
    public void render(Graphics g) {
        if (!dead) {
            super.render(g);
        }
    }

    public void move(Direction direction){
        moving = true;
    }
    
    public void push(Direction direction) {
    	// Failsafe in case the entity never changed their knockback
    	if (knockBack == 2 && this.speed != 2) {knockBack = this.speed;}
    	int oldSpeed = this.speed;
    	this.speed = knockBack;
        changeIntersectingBounds();
        
        for (SolidStaticEntities objects : handler.getZeldaGameState().toTick) {
        	if (objects.bounds.intersects(interactBounds)) { 
            	if (objects instanceof Item) {
            		if (!((Item)objects).getName().contains("locked")) {
                		continue;
            		}

            	}
        		
            	this.speed = oldSpeed; return;}
        }

        
        switch (direction) {
        case RIGHT:
            x += speed;
            break;
        case LEFT:
            x -= speed;
            break;
        case UP:
            y -= speed;
            break;
        case DOWN:
            y += speed;
            break;
        }
        bounds.x = x;
        bounds.y = y;
        this.speed = oldSpeed;
    }

    public void changeIntersectingBounds() {
        interactBounds = (Rectangle) bounds.clone();
        interactBounds.y+=(height/2);
        interactBounds.height/=2;
        switch (direction){
            case DOWN:
                interactBounds.y+=speed;
                break;
            case UP:
                interactBounds.y-=speed;
                break;
            case LEFT:
                interactBounds.x-=speed;
                break;
            case RIGHT:
                interactBounds.x+=speed;
                break;
        }
    }

    public Rectangle getInteractBounds() {
        return interactBounds;
    }

    public void damage(int amount){
    	if (health < 0) return;
    	
        health-=amount;
        dead = health<=0;
        hit = true;
        damageCooldown = 5;
        
    }

    /**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health the health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}
	
	public void setDirection(Direction d) {
		this.direction = d;
		this.changeIntersectingBounds();
	}
	
	public void addHealth(int health) {
		this.health += health;
	}
    
    public boolean isInvulnerable() {
    	return this.hit || this.damageCooldown > 0;
    }
    
    public boolean isStuck() {
    	Direction oldDir = this.direction;
    	
    	for (Direction d : Direction.values()) {
    		boolean collides = false;
    		this.setDirection(d);
            for (SolidStaticEntities objects : handler.getZeldaGameState().toTick) {
            	if ( !((objects instanceof SectionDoor)|| (objects instanceof Item)) && objects.bounds.intersects(interactBounds)) {collides = true;}
            }
            
            if (!collides) {this.direction = oldDir; return false;}
            
    	}
    	
    	this.setDirection(oldDir);
    	return true;
    	
    }
    
    public void unStuck() {
    	int mult = 1;
    	int oldX = this.x, oldY = this.y;
    	do{
    		for (Direction d: Direction.values()) {
        		this.x = oldX; this.y = oldY;
    			switch(d) {
				case DOWN:
					this.y += this.speed + mult;
					break;
				case LEFT:
					this.x -= this.speed + mult;
					break;
				case RIGHT:
					this.x += this.speed + mult;
					break;
				case UP:
					this.y -= this.speed + mult;
					break;
    			}
    			
    			this.bounds.x = x;
    			this.bounds.y = y;
    			
    			if (!isStuck()) {break;}
    		}
    		mult++;
    	} while(isStuck() && mult < 1000);
    	
    }

}
