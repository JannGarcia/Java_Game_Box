package Game.Zelda.Entities.Statics;

import java.awt.image.BufferedImage;

import Game.GameStates.Zelda.ZeldaGameState;
import Game.Zelda.Entities.Dynamic.Direction;
import Main.Handler;

public class Item extends SolidStaticEntities {
	
	// Value to purchase, 0 by default
	private int value = 0;
	private String name;

	public Item(int x, int y, String name, BufferedImage sprite, Handler handler) {
		super(x, y, sprite, handler);
		this.name = name.toLowerCase();
	}
	
	public Item(int x, int y, int value, String name, BufferedImage sprite, Handler handler) {
		super(x, y, sprite, handler);
		this.name = name.toLowerCase();
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

    public boolean isStuck() {
    	
    	boolean collides = false;
        for (SolidStaticEntities objects : handler.getZeldaGameState().toTick) {
          	if (objects.bounds.intersects(this.bounds)) {collides = true;}
        }
            
        return collides;

    }
    

}
