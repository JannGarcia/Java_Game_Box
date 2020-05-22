package Game.Zelda.Entities.Dynamic;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

import Game.GameStates.Zelda.ZeldaGameState;
import Game.Zelda.Entities.Statics.Item;
import Game.Zelda.Entities.Statics.SectionDoor;
import Game.Zelda.Entities.Statics.SolidStaticEntities;
import Game.Zelda.World.Dungeon;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

public abstract class Enemy extends BaseMovingEntity {
	
	// How long the enemy should move in one direction
	protected int moveCooldown = moveCooldown();
	
	// How long the enemy should stay in one place
	protected int stayInPlaceCooldown = stayInPlaceCooldown();
	
	protected Animation damageAnim;
	
	public boolean canHurtLink = true;

	public Enemy(int x, int y, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
	}

	@Override
	public void tick() {
		// Unstuck if needed
		super.tick();

		
		// If it's hit, push it backwards
		if (hit) {
					
			// If link is not attacking and the damageCooldown has settled, you aren't hit anymore
			if (damageCooldown == 0 && !handler.getZeldaGameState().link.attacking) {damageCooldown--; hit = false; return;}
			
			// The enemy is still hit otherwise, and should be pushed
			else if (damageCooldown > 0) {damageCooldown--; hit = true;}
			push(this.shoveDir); // Push the enemy in the direction link was facing
		
		}
    	
		// THE ENEMY SHOULD MOVE HERE (otherwise they stay stuck D:<)
		else {
			
			// Once the enemy stayed in place for this.stayInPlaceCooldown() ticks, move
			if (stayInPlaceCooldown == 0) {moveAlgorithm();}
			else{stayInPlaceCooldown--; moving = false;}
		}
		
        bounds = new Rectangle(x,y,width,height);
        changeIntersectingBounds();
        
		if (damageAnim != null && hit) {
			damageAnim.tick();
			if(damageAnim.end) {damageAnim.reset();}
		}
		
		else {animation.tick();}
	
	}
	
	@Override
	public void render(Graphics g) {
		
		if (this.damageAnim != null && hit) {
			BufferedImage frame = this.damageAnim.getCurrentFrame();
			g.drawImage(frame,x , y , width , height, null);
		}
		else {
			BufferedImage frame = this.animation.getCurrentFrame();
			g.drawImage(frame, x, y,width, height, null);
		}
	}
	
	@Override
	public void move(Direction direction) {
		
		// If they moved for this.moveCooldown() ticks, reset their variables (and stay in place)
		if (moveCooldown == 0) {resetMovement();}
		else{moveCooldown--;}

        moving = true;
        changeIntersectingBounds();
        //check for collisions
        for (SolidStaticEntities objects : handler.getZeldaGameState().toTick) {
            if (objects.bounds.intersects(interactBounds)) {
            	if (objects instanceof Item) {
            		if (!((Item)objects).getName().contains("locked")) {
                		continue;
            		}

            	}
            	
                return;

            }

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
        changeIntersectingBounds();

	}

	
    @Override
    public void damage(int amount) {
    	super.damage(amount);
    	this.damageCooldown = 20;
		handler.getMusicHandler().playEffect("enemyHit.wav");
    	if (this.dead) {
    		handler.getMusicHandler().playEffect("enemyDeath.wav");
    		return;
    	}

    }
    
	protected Direction[] getAvailableDirections() {
		Direction[] available = new Direction[Direction.values().length];
		Direction oldDir = this.direction;
		int index = 0;
		
    	for (Direction d : Direction.values()) {
    		boolean collides = false;
    		this.direction = d;
    		this.changeIntersectingBounds();
    		
            for (SolidStaticEntities objects : handler.getZeldaGameState().toTick) {
            	if (!(objects instanceof SectionDoor) && objects.bounds.intersects(interactBounds)) {collides = true;}
            }
            
            if (!collides) {
            	available[index] = d;
            	index++;
            }

            
    	}
		
		this.direction = oldDir;
		return Arrays.copyOfRange(available, 0, index);
	}
    
    public void resetMovement() {
    	this.moveCooldown = moveCooldown(); this.stayInPlaceCooldown = stayInPlaceCooldown();
    }
    
    
    protected boolean linkIsAhead() {
		for (int i = this.y ; i < this.y+this.height; i++) {
			if (i == handler.getZeldaGameState().link.y) {
				return true;
			}
		}
		
		return false;
	}
    
    protected boolean linkIsAboveBelow() {
		for (int i = this.x ; i < this.x+this.width; i++) {
			if (i == handler.getZeldaGameState().link.x) {
				return true;
			}
		}
		
		return false;
	}
    
	protected void moveRandomly() {
		// Don't change direction until your ticks are up
		Direction[] availableDirections = Direction.values();
		this.direction = this.moveCooldown==0? availableDirections[new Random().nextInt(availableDirections.length)] : this.direction;
		move(this.direction);
	}
	
	public void dropItem() {
		
		Item dropped = null;
		int x, y;
		int offSetX=0, offSetY=0;
		int linkX = handler.getZeldaGameState().link.x, linkY = handler.getZeldaGameState().link.y;

		if (this.x > linkX) {offSetX = -1;}
		else if (this.x < linkX) {offSetX = 1;}
		if (this.y > linkY) {offSetY = -1;}
		else if (this.y < linkY) {offSetY = 1;}
		
		x = ZeldaGameState.OverworldXToItemX(this.x) + offSetX; y = ZeldaGameState.OverworldYToItemY(this.y) + offSetY;
		dropped = getRandomItem(x,y);
		
		if (dropped !=null) {handler.getZeldaGameState().toTick.add(dropped);}

	}
	
	protected Item getRandomItem(int x, int y) {
		if (new Random().nextInt(2) == 0) {return new Item(x,y,1, "rupee", Images.rupee, handler);}
		
		// Blue Rupee
		else if (new Random().nextInt(2) == 0) {return new Item(x,y,5, "rupee", Images.blueRupee, handler);}
		
		// Drop Heart
		else if (new Random().nextInt(5) == 0) {return new Item(x,y, "heart", Images.linkHeart, handler );}
		
		// Drop Arrows
		else if (new Random().nextInt(5) == 1){return new Item(x,y, "arrow" ,Images.linkArrow[0],handler);}
		
		// Drop Potion
		else if (new Random().nextInt(7) == 4){
			return new Item(x,y, "potion" ,Images.linkPotion,handler);
		}
		
		// Drop Key
		else if (handler.getState() instanceof Dungeon && new Random().nextInt(4) == 0) {
			return new Item(x,y, "key", Images.linkKey, handler);
		}
		
		// Drop HeartContainer
		else if (new Random().nextInt(5) == 0){return new Item(x,y, "HeartContainer" ,Images.heartContainer,handler);}
	
		return null;
	}
	
    /**
     * Implement how the enemy will move here!
     * 
     */
	public abstract void moveAlgorithm();
	
	/**
	 * @return how long the enemy should take moving (IN TICKS)
	 */
	protected abstract int moveCooldown();
	
	/**
	 * @return how long the enemy should stay in place before moving (IN TICKS)
	 */
	protected abstract int stayInPlaceCooldown();
	
	/**
	 * 	
	 * @return how fast the enemy should get knockbacked
	 */

}
