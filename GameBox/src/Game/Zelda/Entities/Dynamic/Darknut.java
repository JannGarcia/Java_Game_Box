package Game.Zelda.Entities.Dynamic;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Game.GameStates.Zelda.ZeldaGameState;
import Main.Handler;
import Resources.Animation;
import Resources.Images;
public class Darknut extends Enemy{
	private Animation moveDown, moveUp, moveLeft, moveRight, anim;
	private Animation moveDownDamage, moveUpDamage, moveLeftDamage, moveRightDamage;
	private Animation damageAnim = new Animation(120, Images.DarknutDamageRight);
	public Darknut(int x, int y, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
		this.speed = 1;
		this.power = 3;
		this.health = 20;
		anim = new Animation(120, Images.DarknutRight);
		moveDown = new Animation(120, Images.DarknutDown);
		moveUp = new Animation(120, Images.DarknutUp);
		moveRight = new Animation(120, Images.DarknutRight);
		moveLeft = new Animation(120, Images.DarknutLeft);

		moveUpDamage = new Animation(120, Images.DarknutDamageUp);
		moveDownDamage = new Animation(120, Images.DarknutDamageDown);
		moveRightDamage = new Animation(120, Images.DarknutDamageRight);
		moveLeftDamage = new Animation(120, Images.DarknutDamageLeft);
	}


	@Override
	public void tick() {
		anim.tick();
		super.tick();
		if (this.x < handler.getZeldaGameState().link.x) {
			this.direction = Direction.RIGHT;
			this.move(this.direction);
		}
		if (this.x > handler.getZeldaGameState().link.x) {
			this.direction = Direction.LEFT;
			this.move(this.direction);
		}
		if (this.y > handler.getZeldaGameState().link.y) {
			this.direction = Direction.UP;
			this.move(this.direction);
		}
		if (this.y < handler.getZeldaGameState().link.y) {
			this.direction = Direction.DOWN;
			this.move(this.direction);
		}
		
		if (hit) {
			damageAnim.tick();
			switch (this.direction) {
			case DOWN:
				damageAnim = moveDownDamage;
				break;
			case LEFT:
				damageAnim = moveLeftDamage;
				break;
			case RIGHT:
				damageAnim = moveRightDamage;
				break;
			case UP:
				damageAnim = moveUpDamage;
				break;  
			}
		}else{
			switch (direction) {
			case RIGHT:
				anim = moveRight;
				break;
			case LEFT:
				anim = moveLeft;
				break;
			case UP:
				anim = moveUp;
				break;
			case DOWN:
				anim = moveDown;
				break;
			}
		}
	}

	@Override
	public void render(Graphics g){
		if (hit) {
			BufferedImage frame = this.damageAnim.getCurrentFrame();
			g.drawImage(frame,x , y , frame.getWidth() * ZeldaGameState.worldScale , frame.getHeight() * ZeldaGameState.worldScale, null);
		}
		else {
			BufferedImage frame = this.anim.getCurrentFrame();
			g.drawImage(frame,x , y , frame.getWidth() * ZeldaGameState.worldScale , frame.getHeight() * ZeldaGameState.worldScale, null);
		}


	}


	@Override
	public void moveAlgorithm() {
	}

	@Override
	protected int moveCooldown() {return 0;}

	@Override
	protected int stayInPlaceCooldown() {return 0;}

}
