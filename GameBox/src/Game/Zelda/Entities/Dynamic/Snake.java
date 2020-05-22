package Game.Zelda.Entities.Dynamic;

import java.awt.image.BufferedImage;
import Main.Handler;

public class Snake extends Enemy {

	public Snake(int x, int y, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
		this.health = 4;
		this.speed = 1;

	}

	@Override
	public void moveAlgorithm() {
		Link link = handler.getZeldaGameState().link;
		

		if (linkIsAboveBelow()) {
			
			speed = 2;
			
			// Above
			if (this.y < link.y) {move(Direction.DOWN);}
			
			// Below
			else if (this.y > link.y) {move(Direction.UP);}
			
		}
		
		else if (linkIsAhead()) {			
			speed = 2;
					
			// Left
			if (this.x < link.x) {move(Direction.RIGHT);}
			
			// Right
			else if (this.x > link.x) {move(Direction.LEFT);}
			
		}
		
		else {
			speed = 1;
			moveRandomly();
		}


	}

	@Override
	protected int moveCooldown() {
		return 30;
	}

	@Override
	protected int stayInPlaceCooldown() {
		return 0;
	}
	

}
