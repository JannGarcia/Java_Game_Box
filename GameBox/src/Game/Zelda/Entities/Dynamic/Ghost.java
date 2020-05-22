package Game.Zelda.Entities.Dynamic;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Game.GameStates.Zelda.ZeldaGameState;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

public class Ghost extends Enemy {
	
	private final int animSpeed = 120;
	private final int damageAnimSpeedMult = 2;
	private String name;
	private Animation up, down, left, right;

	public Ghost(int x, int y, String name,  BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
		BufferedImage[] frames = {sprite[0], Images.turnToBlue[0], Images.turnToBlue[2], Images.turnToBlue[1], Images.turnToBlue[3] };
		damageAnim = new Animation(animSpeed/damageAnimSpeedMult, frames);
		this.name = name.toLowerCase();
		
		switch(name) {
		case "inky":
			this.up = new Animation(animSpeed,Images.inkyUp);
			this.down = new Animation(animSpeed,Images.inkyDown);
			this.left = new Animation(animSpeed,Images.inkyLeft);
			this.right = new Animation(animSpeed,Images.inkyRight);
			this.power = 4;
			this.speed = 2;
			this.health = 10;
			break;
			
		case "pinky":
			this.up = new Animation(animSpeed,Images.pinkyUp);
			this.down = new Animation(animSpeed,Images.pinkyDown);
			this.left = new Animation(animSpeed,Images.pinkyLeft);
			this.right = new Animation(animSpeed,Images.pinkyRight);
			this.power = 3;
			this.speed = 2;
			this.health = 8;
			break;
			
		case "clyde":
			this.up = new Animation(animSpeed,Images.clydeUp);
			this.down = new Animation(animSpeed,Images.clydeDown);
			this.left = new Animation(animSpeed,Images.clydeLeft);
			this.right = new Animation(animSpeed,Images.clydeRight);
			this.power = 5;
			this.speed = 1;
			this.health = 12;
			break;
			
		default:
			this.up = new Animation(animSpeed,Images.blinkyUp);
			this.down = new Animation(animSpeed,Images.blinkyDown);
			this.left = new Animation(animSpeed,Images.blinkyLeft);
			this.right = new Animation(animSpeed,Images.blinkyRight);
			this.power = 3;
			this.speed = 2;
			this.health = 8;
			break;
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		switch (this.direction) {
		case DOWN:
			down.tick();
			break;
		case LEFT:
			left.tick();
			break;
		case RIGHT:
			right.tick();
			break;
		case UP:
			up.tick();
			break;

		}
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		
		if (!hit) {
			BufferedImage frame = animation.getCurrentFrame();
			switch(this.direction) {
			case DOWN:
				frame = down.getCurrentFrame();
				break;
			case LEFT:
				frame = left.getCurrentFrame();
				break;
			case RIGHT:
				frame = right.getCurrentFrame();
				break;
			case UP:
				frame = up.getCurrentFrame();
				break;
			
			}
			g.drawImage(frame, this.x, this.y, this.width, this.height, null);
		}
	}
	
//	@Override
//	public void move(Direction direction) {
//		// If they moved for this.moveCooldown() ticks, reset their variables (and stay in place)
//		if (moveCooldown == 0) {resetMovement();}
//		else{moveCooldown--;}
//        switch (direction) {
//        case RIGHT:
//            x += speed;
//            break;
//        case LEFT:
//            x -= speed;
//            break;
//        case UP:
//            y -= speed;
//            break;
//        case DOWN:
//            y += speed;
//            break;
//    }
//    
//    bounds.x = x;
//    bounds.y = y;
//	}

	@Override
	public void moveAlgorithm() {
		switch(this.name) {
		case "inky":
			blinkyAlgorithm();
			break;
			
		case "pinky":
			blinkyAlgorithm();
			break;
			
		case "clyde":
			blinkyAlgorithm();
			break;
			
		default:
			blinkyAlgorithm();


		
		}
	}
	
	@Override
	public void damage(int amount) {
		super.damage(amount);
		if (this.dead) {
			handler.getMusicHandler().playEffect("pacman_eatghost.wav");
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
	
	private void blinkyAlgorithm() {
		Link link = handler.getZeldaGameState().link;

		if (linkIsAboveBelow()) {
							
			// Above
			if (this.y < link.y) {this.direction = Direction.DOWN; move(Direction.DOWN);}
			
			// Below
			else if (this.y > link.y) {this.direction = Direction.UP;move(Direction.UP);}
			
		}
		
		else if (linkIsAhead()) {			
					
			// Left
			if (this.x < link.x) {this.direction = Direction.RIGHT;move(Direction.RIGHT);}
			
			// Right
			else if (this.x > link.x) {this.direction = Direction.LEFT;move(Direction.LEFT);}
			
		}
		
		else {
			moveRandomly();
		}
	}
	

}
