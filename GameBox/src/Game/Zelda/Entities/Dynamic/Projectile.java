package Game.Zelda.Entities.Dynamic;

import static Game.GameStates.Zelda.ZeldaGameState.worldScale;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Game.GameStates.Zelda.ZeldaGameState;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

public class Projectile extends BaseMovingEntity {

	private BaseMovingEntity owner;
	protected Rectangle hitbox;
	private int hitBoxSize = 10, hitBoxDistance;
	private boolean blockable = true, deflectable = false;
	private Animation reflectAnim;

	public Projectile(int x, int y, BufferedImage[] sprite, int speed, int power, Direction d, BaseMovingEntity owner,
			Handler handler) {
		super(x, y, sprite, handler);
		this.speed = speed;
		this.direction = d;
		this.owner = owner;
		this.power = power;
		
		// TODO: Modify
		this.hitBoxDistance = this.width;
		this.hitBoxSize = this.width;
	}

	@Override
	public void tick() {
		if (this.x < 0 || this.x > handler.getWidth() || this.y < 0 || this.y > handler.getHeight()) {
			delete();
			return;
		}

		move(this.direction);
		
		for (BaseMovingEntity entity : handler.getZeldaGameState().entitiesToTick) {
			if (entity.bounds.intersects(this.hitbox) && !(entity instanceof Projectile)) {

				// Rogue
				if (this.owner == null) {
					impact(entity);
					return;
				}

				// Came from link
				else if (this.owner instanceof Link && !(entity instanceof Link)) {
					impact(entity);
					return;
				}

			}
		}
		
		// Came from someone that isn't Link
		if (!(this.owner instanceof Link) && handler.getZeldaGameState().link.bounds.intersects(this.hitbox) && !handler.getZeldaGameState().link.isInvulnerable()) {

			if (isBlocked(handler.getZeldaGameState().link)) {
				handler.getMusicHandler().playEffect("block.wav");
				
				// Swap projectiles
				if (reflectAnim != null) {
					Animation oldAnim = this.animation;
					setAnimation(reflectAnim);
					this.reflectAnim = oldAnim;
				}

				// If the projectile would have killed link, push him back a bit
				if (this.power >= handler.getZeldaGameState().link.health || handler.getZeldaGameState().link.health <= 2) {
					int oldKB = handler.getZeldaGameState().link.knockBack;
					handler.getZeldaGameState().link.knockBack = this.power;
					handler.getZeldaGameState().link.push(this.direction);
					handler.getZeldaGameState().link.knockBack = oldKB;
				}
				
				// Deflect a Projectile :)
				if (isDeflectable()) {
					this.owner = handler.getZeldaGameState().link;
					this.power *= 1.2;
					reverseDirection();
					return;
				}
				
				
			}

			else {
				handler.getZeldaGameState().link.shoveDir = this.direction;
				impact(handler.getZeldaGameState().link);
			}

			delete();
		}

	}

	@Override
	public void render(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		BufferedImage frame = this.animation.getCurrentFrame();
		
		switch(this.direction) {
		case DOWN:
			frame = Images.flipVertical(frame);
			break;
		case LEFT:
			frame = Images.flipHorizontal(Images.rotate(frame, 90));
			break;
		case RIGHT:
			frame = Images.rotate(frame, 90);
			break;
		case UP:
			break;
		default:
			this.animation = new Animation(100, this.sprites);
			this.direction = Direction.UP;
			break;
		
		}

		g.drawImage(frame, this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height, null);

		if (ZeldaGameState.showHitboxes && this.hitbox!=null) {
			g.drawRect(this.hitbox.x, this.hitbox.y, this.hitbox.width, this.hitbox.height);
		}
	}

	@Override
	public void move(Direction direction) {
		this.bounds = new Rectangle(x,y,width,height);

		switch (direction) {
			case DOWN:
				this.y += speed;
				break;
			case LEFT:
				this.bounds = new Rectangle(x,y,height,width);
				this.x -= speed;
				break;
			case RIGHT:
				this.bounds = new Rectangle(x,y,height,width);
				this.x += speed;
				break;
			case UP:
				this.y -= speed;
				break;
		}
		
		// Hitbox modification
		int xRect=0, yRect=0, wRect=0, hRect=0;
		switch(direction) {
		case UP:
			xRect = this.bounds.x - hitBoxSize/2;
			wRect = this.bounds.width + hitBoxSize;
			yRect = this.bounds.y + this.bounds.height / 2 - hitBoxDistance;
			hRect = hitBoxDistance;
			break;
		case DOWN:
			xRect = this.bounds.x - hitBoxSize/2;
			wRect = this.bounds.width + hitBoxSize;
			yRect = this.bounds.y + this.bounds.height/2;
			hRect = hitBoxDistance;
			break;
		case LEFT:
			xRect = this.bounds.x + this.bounds.width / 2 - hitBoxDistance;
			wRect = hitBoxDistance;
			yRect = this.bounds.y - hitBoxSize / 2;
			hRect = this.bounds.height + hitBoxSize;
			break;
		case RIGHT:
			xRect = this.bounds.x + this.bounds.width/2;
			wRect = hitBoxDistance;
			yRect = this.bounds.y - hitBoxSize / 2;
			hRect =	this.bounds.height + hitBoxSize;
			break;
		}

		this.hitbox = new Rectangle(xRect,yRect,wRect,hRect);

		this.changeIntersectingBounds();
		bounds.x = x;
		bounds.y = y;


	}

	public void delete() {
		handler.getZeldaGameState().toRemove2.add(this);
	}

	@Override
	public void damage(int amount) {
		// Projectiles can't be damaged
	}

	public BaseMovingEntity getOwner() {
		return this.owner;
	}

	public void setOwner(BaseMovingEntity owner) {
		this.owner = owner;
	}

	public boolean isBlockable() {
		return blockable;
	}

	public void setBlockable(boolean blockable) {
		this.blockable = blockable;
	}

	public boolean isDeflectable() {
		return deflectable;
	}

	public void setDeflectable(boolean deflectable) {
		this.deflectable = deflectable;
	}

	private boolean isBlocked(BaseMovingEntity entity) {
		if (!isBlockable() || (entity instanceof Link && ((Link)entity).attacking)) {return false;}
		switch (this.direction) {
		case DOWN:
			return entity.direction == Direction.UP;
		case LEFT:
			return entity.direction == Direction.RIGHT;
		case RIGHT:
			return entity.direction == Direction.LEFT;
		case UP:
			return entity.direction == Direction.DOWN;
		default:
			return false;
		}
	}

	private void impact(BaseMovingEntity entity) {
		if (!entity.isInvulnerable()) {
			entity.shoveDir = this.direction;
			entity.damage(this.power);
			delete();
		}

	}
	
	private void reverseDirection() {
		switch(this.direction) {
		case DOWN:
			this.direction = Direction.UP;
			break;
		case LEFT:
			this.direction = Direction.RIGHT;
			break;
		case RIGHT:
			this.direction = Direction.LEFT;
			break;
		case UP:
			this.direction = Direction.DOWN;
			break;
		default:
			break;
		
		}
	}

	public Animation getAnimation() {
		return this.animation;
	}
	
	public Animation getReflectAnimation() {
		return this.reflectAnim;
	}

	public void setAnimation(BufferedImage...frames) {
		this.animation = new Animation(100, frames);
	}
	
	public void setAnimation(Animation an) {
		this.animation = an;
	}
	
	public void setReflectAnimation(BufferedImage...frames) {
		this.reflectAnim = new Animation(100, frames);
	}
	
	public void setReflectAnimation(Animation an) {
		this.reflectAnim = an;
	}



}
