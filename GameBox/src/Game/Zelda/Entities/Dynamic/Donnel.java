package Game.Zelda.Entities.Dynamic;

import static Game.GameStates.Zelda.ZeldaGameState.worldScale;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Game.GameStates.Zelda.ZeldaGameState;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

public class Donnel extends Enemy {
	
	private Animation up,down,left,right,idle, attackAnim;
	private Rectangle hitBox;
	private int hitBoxSize = 10, hitBoxDistance = 30;
	private int animSpeed = 255;
	private boolean attacking = false;

	public Donnel(int x, int y, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
		this.canHurtLink = false;
		idle = new Animation(animSpeed, Images.donnelIdle);
		attackAnim = new Animation(animSpeed, Images.donnelAttackLeft);
		up = new Animation(animSpeed, Images.donnelUp);
		down = new Animation(animSpeed, Images.donnelDown);
		left = new Animation(animSpeed, Images.donnelLeft);
		right = new Animation(animSpeed, Images.donnelRight);
		this.power = 10;
		this.health = 20;

	}
	
	@Override
	public void tick() {
		if (!attacking) {super.tick();}
		
		if (moving) {
			switch(this.direction) {
			case DOWN:
				this.animation = down;
				break;
			case LEFT:
				this.animation = left;
				break;
			case RIGHT:
				this.animation = right;
				break;
			case UP:
				this.animation = up;
				break;
			}
		}
		
		else if (attacking) {
			animation.tick();
			if (animation.end) {animation.reset(); attacking = false; animation = idle;}
			if (this.hitBox!=null && this.hitBox.intersects(handler.getZeldaGameState().link.bounds) && animation.getIndex() == 2) {
				if(!handler.getZeldaGameState().link.isInvulnerable()) {
					handler.getZeldaGameState().link.damage(this.power);
				}
			}
		}
		
		else if (linkIsNear()) {
			animation = attackAnim;
			attacking = true;
		}
		
		else {this.animation = idle;}
		


	}
	
	private boolean linkIsNear() {
		Direction oldDir = this.direction;
		int oldSpeed = this.speed;
		boolean found = false;
		for (Direction dir : Direction.values()) {
			
			this.direction = dir;
			this.speed = 20;
			this.changeIntersectingBounds();
			
			if (this.interactBounds.intersects(handler.getZeldaGameState().link.bounds)){
				found = true;
				if (dir.equals(Direction.RIGHT)) {attackAnim = new Animation(animSpeed, Images.donnelAttackRight);}
				else{attackAnim = new Animation(animSpeed,Images.donnelAttackLeft);}
				this.direction = dir;
				break;
			}
			
			else {this.direction = oldDir;}
		}

		this.speed = oldSpeed;
		this.changeIntersectingBounds();
		return found;
	}

	@Override
	public void render(Graphics g) {
		if (!attacking) {super.render(g);}
		
		else if (attacking){
			BufferedImage frame = this.animation.getCurrentFrame();
			// For left/up purposes
			int xShift = 0, yShift = 0;
			int xRect=0, yRect=0, wRect=0, hRect=0;
			switch(this.direction) {	
			case UP:
				xShift = this.width - frame.getWidth() * worldScale;
				xRect = this.x - hitBoxSize/2;
				wRect = frame.getWidth() * worldScale + hitBoxSize;
				yRect = this.y - hitBoxDistance;
				hRect = hitBoxDistance;
				break;
			case DOWN:
				xShift = this.width - frame.getWidth() * worldScale;
				xRect = this.x - hitBoxSize/2;
				wRect = frame.getWidth() * worldScale + hitBoxSize;
				yRect = this.y + this.height;
				hRect = hitBoxDistance;
				break;
			case LEFT:
				xShift = this.width - frame.getWidth() * worldScale;
				xRect = this.x - hitBoxDistance;
				wRect = hitBoxDistance;
				yRect = this.y - hitBoxSize / 2;
				hRect = frame.getHeight() * worldScale + hitBoxSize;
				break;
			case RIGHT:
				xRect = this.x + this.width;
				wRect = hitBoxDistance;
				yRect = this.y - hitBoxSize / 2;
				hRect = frame.getHeight() * worldScale + hitBoxSize;
				break;
			default:
				frame = animation.getCurrentFrame();
			}
			if (animation.getIndex()==2) {this.hitBox = new Rectangle(xRect,yRect,wRect,hRect);}
			else {this.hitBox = new Rectangle(0,0,0,0);}
			g.drawImage(frame,x + xShift, y + yShift, frame.getWidth() * worldScale , frame.getHeight() * worldScale, null);
			if (ZeldaGameState.showHitboxes) { g.setColor(Color.YELLOW);g.drawRect(this.hitBox.x, this.hitBox.y, this.hitBox.width, this.hitBox.height);}

		}
	}

	@Override
	public void moveAlgorithm() {
		moveRandomly();
	}

	@Override
	protected int moveCooldown() {
		return 30;
	}

	@Override
	protected int stayInPlaceCooldown() {
		return 180;
	}

}
