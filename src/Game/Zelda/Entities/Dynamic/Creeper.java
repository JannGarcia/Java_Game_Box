package Game.Zelda.Entities.Dynamic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Game.GameStates.Zelda.ZeldaGameState;
import Game.Zelda.Entities.Statics.Item;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

public class Creeper extends Enemy {
	public final String CREEPER_HISS= "creeperHisses.wav";
	public final String CREEPER_EXPLOSION = "creeperExplosion.wav";

	private int hitBoxDistance = 40;
	public Rectangle explosionHitBox = new Rectangle(0,0,0,0);
	private Animation moveDown, moveUpLeft, moveUp, moveUpRight, moveLeft, moveRight, anim;
	private Animation moveDownDamage, moveUpDamage, moveLeftDamage, moveRightDamage, explodesLeft, explodesRight;
	private boolean exploding, exploded = false, lockExplosion = false;
	private int stayInPlaceCooldown, creeperHissTimer = 60, creeperExplosionTimer = 60;
	int xRect=0, yRect=0, wRect=0, hRect=0;
	int worldScale = ZeldaGameState.worldScale;
	public Creeper(int x, int y, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);	
		this.speed = 1;
		this.power = 5;
		this.health = 4;
		this.canHurtLink = false;
		anim = new Animation(80, Images.CreeperRight);
		moveDown = new Animation(80, Images.CreeperDown);
		moveUp = new Animation(80, Images.CreeperUp);
		moveUpRight = new Animation(80, Images.CreeperUpRight);
		moveUpLeft = new Animation(80, Images.CreeperUp);
		moveRight = new Animation(80, Images.CreeperRight);
		moveLeft = new Animation(80, Images.CreeperLeft);

		moveUpDamage = new Animation(120, Images.CreeperDamageUp);
		moveDownDamage = new Animation(120, Images.CreeperDamageDown);
		moveRightDamage = new Animation(120, Images.CreeperDamageRight);
		moveLeftDamage = new Animation(120, Images.CreeperDamageLeft);

		explodesLeft = new Animation(120, Images.CreeperExplosionLeft);
		explodesRight = new Animation(120, Images.CreeperExplosionRight);

		stayInPlaceCooldown = 0;
		damageAnim = moveRightDamage;
	}


	@Override
	public void tick() {
		if(exploded) {return;}
		if(creeperExplosionTimer > 0) {creeperExplosionTimer --;}
		if(creeperHissTimer > 0) {creeperHissTimer --;}

		xRect = this.x - hitBoxDistance;
		yRect = this.y - hitBoxDistance;
		wRect = this.width + hitBoxDistance * 2;
		hRect = this.height + hitBoxDistance * 2;
		this.explosionHitBox = new Rectangle(xRect,yRect,wRect,hRect);
		Link link = handler.getZeldaGameState().link;
		if(this.explosionHitBox.intersects(link.interactBounds) && !exploded) {
			if(this.direction == Direction.LEFT) {
				this.anim = explodesLeft;
			}
			else {
				this.anim= explodesRight;
			}
			if(creeperHissTimer <= 0) {
				handler.getMusicHandler().playEffect(CREEPER_HISS);
			}
			exploding = true;
			stayInPlaceCooldown = this.stayInPlaceCooldown();
		}
		else {
			if(!lockExplosion) {
				creeperHissTimer = 60 * 2;
				if(stayInPlaceCooldown>0) {stayInPlaceCooldown --;}
				exploding = false;
				if(!exploded) {
					explodesLeft.reset();
					explodesRight.reset();
				}
			}
		}
		if(explodesLeft.getCurrentFrame() == Images.CreeperExplosionLeft[4]|| explodesRight.getCurrentFrame() == Images.CreeperExplosionRight[4]) {
			exploding = true;
			lockExplosion = true;
			if(creeperExplosionTimer <= 0) {
				handler.getMusicHandler().playEffect(CREEPER_EXPLOSION);
				//handler.getMusicHandler().pauseMusic();
				creeperExplosionTimer = 60 * 2;

			}
		}
		if(explodesLeft.end || explodesRight.end) {
			exploded = true;
			this.damage(this.health);
			this.dead = true;
			hit = false;
			exploding = false;
			//handler.getMusicHandler().resumeMusic();		
			
			for (BaseMovingEntity enemy : handler.getZeldaGameState().entitiesToTick) {
				if (this.explosionHitBox.intersects(enemy.bounds)) {
					enemy.damage(this.power);
				}
			}
			//handler.getMusicHandler().resumeMusic();			
			if(this.explosionHitBox.intersects(link.interactBounds)) {
				link.damage(this.power);		
			}

		}
		super.tick();
		anim.tick();
		
		if(stayInPlaceCooldown > 0) {this.speed = 0;}
		else {
			this.speed = 1;
		}
		if (this.x < handler.getZeldaGameState().link.x) {
			this.direction = Direction.RIGHT;
			this.move(this.direction);
			moveUp = moveUpRight;
		}
		if (this.x > handler.getZeldaGameState().link.x) {
			this.direction = Direction.LEFT;
			this.move(this.direction);
			moveUp = moveUpLeft;
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
		}else if(!exploding){
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
		if(exploded) {return;}
		if (hit) {
			BufferedImage frame = this.damageAnim.getCurrentFrame();
			g.drawImage(frame,x , y , frame.getWidth() * ZeldaGameState.worldScale , frame.getHeight() * ZeldaGameState.worldScale, null);
		}
		else {
			BufferedImage frame = this.anim.getCurrentFrame();
			g.drawImage(frame,x , y , frame.getWidth() * ZeldaGameState.worldScale , frame.getHeight() * ZeldaGameState.worldScale, null);
		}

		if (ZeldaGameState.showHitboxes) { g.setColor(Color.YELLOW);g.drawRect(this.explosionHitBox.x, this.explosionHitBox.y, this.explosionHitBox.width, this.explosionHitBox.height);}
	}

	@Override
	protected Item getRandomItem(int x, int y) {
		if(!exploded) {
			return super.getRandomItem(x, y);
		}
		return null;
	}

	@Override
	public void damage(int amount) {
		if (health < 0) return;  	
		health-=amount;
		dead = health<=0;

		hit = true;
		damageCooldown = 5;
		this.damageCooldown = 20;
		if(!exploded) {
			handler.getMusicHandler().playEffect("enemyHit.wav");
		}
		if (this.dead && !exploded) {
			handler.getMusicHandler().playEffect("enemyDeath.wav");
			return;
		}
	}
	@Override
	public void moveAlgorithm() {}

	@Override
	protected int moveCooldown() {
		return 0;
	}

	@Override
	protected int stayInPlaceCooldown() {
		return 20;
	}

}
