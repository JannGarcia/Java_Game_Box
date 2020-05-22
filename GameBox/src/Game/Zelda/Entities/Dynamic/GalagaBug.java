package Game.Zelda.Entities.Dynamic;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import Game.GameStates.Zelda.ZeldaGameState;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

public class GalagaBug extends Enemy {
	
	private int targetX, targetY;
	private int shootCooldown = 0;
	private boolean positioned = false;

	public GalagaBug(int x, int y, int targetX, int targetY, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
		this.targetX = targetX;
		this.targetY = targetY;
		this.speed = 2;
		this.health = 2;
		this.power = 2;
		this.knockBack = 0;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (positioned) {
			if (shootCooldown > 0) {shootCooldown--;}
			else {shootLaser(); shootCooldown = (new Random().nextInt(10) + 1) * 60;}
		}
	}

	@Override
	public void moveAlgorithm() {
		if (this.x > targetX) {move(Direction.LEFT);}
		else if (this.x < targetX) {move(Direction.RIGHT);}
		else if (this.y > targetY) {move(Direction.UP);}
		else if (this.y < targetY) {move(Direction.DOWN);}	
		
		else {positioned = true;}
	}
	
	@Override
	public void move(Direction direction) {
		this.bounds = new Rectangle(x,y,width,height);
		switch (direction) {
			case DOWN:
				this.y += speed;
				break;
			case LEFT:
				this.x -= speed;
				break;
			case RIGHT:
				this.x += speed;
				break;
			case UP:
				this.y -= speed;
				break;
		}

		this.changeIntersectingBounds();
		bounds.x = x;
		bounds.y = y;
		
	}

	@Override
	protected int moveCooldown() {
		return 0;
	}

	@Override
	protected int stayInPlaceCooldown() {
		return 0;
	}
	
	@Override
	public boolean isStuck() {
		return false;
	}
	
	public void setAnimation(BufferedImage[] s) {
		this.animation = new Animation(256, s);
	}
	
	public void setTargetX(int x) {
		this.targetX = x;
	}
	
	public void setTargetY(int y) {
		this.targetY = y;
	}
	
	public void shootLaser() {
		BufferedImage[] fire = {Images.galagaPlayerLaser};
		Projectile laser = new Projectile(this.x, this.y, fire, 4, 4, this.direction, this, handler);
		laser.setDeflectable(true);
		laser.setBlockable(true);
		
		// Change to blue laser
		laser.setReflectAnimation(Images.reflectedLaser);
		handler.getZeldaGameState().entitiesToAdd.add(laser);
		handler.getMusicHandler().playEffect("laser.wav");
	}


}
