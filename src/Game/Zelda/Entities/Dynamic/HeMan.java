package Game.Zelda.Entities.Dynamic;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import Game.GameStates.Zelda.BossBattleState;
import Game.GameStates.Zelda.ZeldaGameState;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

public class HeMan extends Enemy {
	
	private BufferedImage[] frames = {Images.heman[2], Images.heman[3], Images.heman[4]};
	private Animation attackAnim = new Animation(256, frames);
	private int phase = 1;
	private int swordHeight = 30;
	private boolean invulnerable = false, positioned = false;
	public int maxHealth;
	public boolean playAnimation = false, attacking = false;
	
	// PHASE 2 VARIABLES
	private boolean canHurtLink = true;
	private boolean staggering = false, strafing = false, returning = false, dashing = false;
	private boolean offscreen = false;
	private Direction dashDir;
	private int attackCooldown = 10*60, staggerCooldown = 5*60, dashCooldown = -1;

	public HeMan(int x, int y, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
		BufferedImage[] newSprite =  {Images.heman[0], Images.heman[1]};
		this.speed = 1;
		this.animation = new Animation(256,newSprite);
		this.width = 60;
		this.height = 80;
		this.power = 5;
		this.health = 100;
		this.maxHealth = this.health;
		this.knockBack = 0;
		this.bounds = new Rectangle(this.x, this.y, this.width, this.height);
	}
	
	@Override
	public void tick() {
		
		if (this.health <= 0) {
			return;
		}
		
		if (playAnimation || attacking) {
			attackAnim.tick();
		}
		else{super.tick();}
		
		if (phase == 2) {
			
			// Continue staggering
			if (staggerCooldown > 0 && staggering) {
				moving = false;
				staggerCooldown--;
				
				
				// Unstagger after the cooldown ends
				if (staggerCooldown == 0) {
					moving = true;
					returning = true;
					staggering = false;
					this.knockBack = 0;
				}

				return;	
			}
			
			// Begin to dash in a random direction
			if (dashCooldown == 0) {
				setDashing(true);
				dashDir = Direction.values()[new Random().nextInt(Direction.values().length)];
				dashCooldown = -1;
			}
			
			
			// Attack instead
			if (!dashing) {
				
				// Play the sound
				if (attackCooldown == 5 * 60) {
					handler.getMusicHandler().playEffect("hemanattack.wav");
					playAnimation = true;
				}

				if (dashCooldown > 0) {dashCooldown--;}
				
				else if (attackCooldown == 0) {
					
					playAnimation = false;
					
					switch(new Random().nextInt(2)) {
					case 0: // Summon Ghosts
						if (!handler.getBossState().ghostsOnMap()) {
							addEnemy(new Random().nextInt(BossBattleState.roomWidth-4)+2, new Random().nextInt(BossBattleState.roomHeight-4)+2, handler.getZeldaGameState().createBlinky());
							addEnemy(new Random().nextInt(BossBattleState.roomWidth-4)+2, new Random().nextInt(BossBattleState.roomHeight-4)+2, handler.getZeldaGameState().createInky());
							addEnemy(new Random().nextInt(BossBattleState.roomWidth-4)+2, new Random().nextInt(BossBattleState.roomHeight-4)+2, handler.getZeldaGameState().createPinky());
							addEnemy(new Random().nextInt(BossBattleState.roomWidth-4)+2, new Random().nextInt(BossBattleState.roomHeight-4)+2, handler.getZeldaGameState().createClyde());
							dashCooldown = 10 * 60;
							attackCooldown = 10 * 60;
						}
						
						else {return;}

						break;
					case 1: // Summon Galaga
						setStrafing(true);
						
						for (BaseMovingEntity bug : handler.getZeldaGameState().entitiesToTick) {
							if (bug instanceof GalagaBug) {
								handler.getZeldaGameState().toRemove2.add(bug);
							}
						}
						
						switch(Direction.values()[new Random().nextInt(Direction.values().length)]) {
						case DOWN:
							for (int i = BossBattleState.leftX+2; i < BossBattleState.rightX; i++) {
//								if (new Random().nextInt(100) < 30) {continue;}
								GalagaBug bug = handler.getZeldaGameState().createGalagaBug();
								bug.setTargetX(ZeldaGameState.itemXToOverworldX(i));
								bug.setTargetY(ZeldaGameState.itemYToOverworldY(BossBattleState.bottomY + 3));
								bug.setDirection(Direction.UP);
								addEnemy(BossBattleState.roomWidth / 2, BossBattleState.topY - 3-i, bug);
							}
							break;
						case LEFT:
							for (int i = BossBattleState.topY+2; i < BossBattleState.bottomY; i++) {
//								if (new Random().nextInt(100) < 30) {continue;}
								GalagaBug bug = handler.getZeldaGameState().createGalagaBug();
								bug.setTargetX(ZeldaGameState.itemXToOverworldX(BossBattleState.leftX - 1));
								bug.setTargetY(ZeldaGameState.itemYToOverworldY(i));
								bug.setDirection(Direction.RIGHT);
								addEnemy(BossBattleState.roomWidth / 2, BossBattleState.topY - 3-i, bug);
							}
							break;
						case RIGHT:
							for (int i = BossBattleState.topY+2; i < BossBattleState.bottomY; i++) {
//								if (new Random().nextInt(100) < 30) {continue;}
								GalagaBug bug = handler.getZeldaGameState().createGalagaBug();
								bug.setTargetX(ZeldaGameState.itemXToOverworldX(BossBattleState.rightX + 3));
								bug.setTargetY(ZeldaGameState.itemYToOverworldY(i));
								bug.setDirection(Direction.LEFT);
								addEnemy(BossBattleState.roomWidth / 2, BossBattleState.topY - 3 - i, bug);
							}
							break;
						case UP:
							for (int i = BossBattleState.leftX+2; i < BossBattleState.rightX; i++) {
//								if (new Random().nextInt(100) < 30) {continue;}
								GalagaBug bug = handler.getZeldaGameState().createGalagaBug();
								bug.setTargetX(ZeldaGameState.itemXToOverworldX(i));
								bug.setTargetY(ZeldaGameState.itemYToOverworldY(BossBattleState.topY -1));
								bug.setDirection(Direction.DOWN);
								addEnemy(BossBattleState.roomWidth / 2, BossBattleState.topY - 3-i, bug);
							}
							break;
						
						}
						

						dashCooldown = 10 * 60;
						attackCooldown = 10 * 60;
						break;
					}
					
				}
				
				else {attackCooldown--;}
			}

		}
		
	}
	
	
	@Override
	public void render(Graphics g) {
		if (playAnimation) {
			g.drawImage(attackAnim.getCurrentFrame(), x, y, width, height, null);
		}
		else {super.render(g);}
		
	}

	@Override
	public void moveAlgorithm() {
		switch(phase) {
		case 1:
			if (moving) {
				if (this.x < handler.getZeldaGameState().link.x) {this.move(Direction.RIGHT);}
				if (this.x > handler.getZeldaGameState().link.x) {this.move(Direction.LEFT);}
				if (this.y + swordHeight > handler.getZeldaGameState().link.y) {this.move(Direction.UP);}
				if (this.y + swordHeight < handler.getZeldaGameState().link.y) {this.move(Direction.DOWN);}
			}
			break;
			
		case 2:
			if(returning) {
				this.setInvulnerable(true);
				canHurtLink = false;
				speed = 4;
				if (this.x < handler.getWidth()/2 - 10) {this.move(Direction.RIGHT);}
				else if (this.x > handler.getWidth()/2 + 10) {this.move(Direction.LEFT);}
				else if (this.y + swordHeight > 60) {this.move(Direction.UP);}
				else if (this.y + swordHeight < 40) {this.move(Direction.DOWN);}
				
				else {returning = false; moving = false; offscreen = false; attackAnim.reset();}
			}
			
			else if (dashing) {
				canHurtLink = true;
				
				if (!offscreen) {
					move(Direction.UP);
					offscreen = y + height < -200;
					
					if (offscreen) {
						// Set He-Man into position
						switch(dashDir) {
						case DOWN:
							this.x = handler.getZeldaGameState().link.x;
							this.y = 0 - height;
							break;
						case LEFT:
							this.x = handler.getWidth();
							this.y = handler.getZeldaGameState().link.y;
							break;
						case RIGHT:
							this.x = 0;
							this.y = handler.getZeldaGameState().link.y;
							break;
						case UP:
							this.x = handler.getZeldaGameState().link.x;
							this.y = handler.getHeight();
							break;
						
						}
					}
				}
				
				else {
					// Dash
					this.setInvulnerable(false);
					speed = 4;
					switch(dashDir) {
					case DOWN:
						move(Direction.DOWN);
						if (this.y > handler.getHeight()) {dashing = false;returning = true; dashCooldown = -1;}
						break;
					case LEFT:
						move(Direction.LEFT);
						if (this.x < 0) {dashing = false; returning = true; dashCooldown = -1;}
						break;
					case RIGHT:
						move(Direction.RIGHT);
						if (this.x > handler.getWidth()) {dashing = false; returning = true; dashCooldown = -1;}
						break;
					case UP:
						move(Direction.UP);
						if (this.y < 0) {dashing = false;returning = true; dashCooldown = -1;}
						break;
				
					}
					
				}
				
			}
			
			else if (strafing) {
				this.setInvulnerable(false);
			
				if (!this.direction.equals(Direction.LEFT) && !this.direction.equals(Direction.RIGHT)) {
					this.direction = new Random().nextBoolean() ? Direction.LEFT : Direction.RIGHT;
				}
				
				else if (this.direction.equals(Direction.LEFT) && this.x < 100) {
					this.direction = Direction.RIGHT;
				}
				
				else if (this.direction.equals(Direction.RIGHT) && this.x > handler.getWidth() - 100) {
					this.direction = Direction.LEFT;
				}
				
				move(this.direction);
				
			}

			
			break;
		 
		case 5: // Move to top center
			if (moving) {
				speed = 4;
				if (this.x < handler.getWidth()/2 - 10) {this.move(Direction.RIGHT);}
				else if (this.x > handler.getWidth()/2 + 10) {this.move(Direction.LEFT);}
				else if (this.y + swordHeight > 60) {this.move(Direction.UP);}
				else if (this.y + swordHeight < 40) {this.move(Direction.DOWN);}
				
				else {
					setPositioned(true);
				}
			}
			
			break;
		}


	}
	
	@Override
	public void damage(int amount) {
		
		if (invulnerable) {return;}
		
		switch (phase) {
		case 1:
			super.damage(amount);
			break;
			
		case 2:
			
			if (dashing || strafing) {
				stagger();
				super.damage(amount);
			}
			
			else if (staggering) {
				super.damage(amount);
			}

			break;
			
		case 5:
			break;
		}
		
		if (dead) {
			handler.getMusicHandler().playEffect("explosion.wav");
		}
	}
	
	
	public void addEnemy(int x, int y, Enemy enemy) {		
		enemy.x = ZeldaGameState.itemXToOverworldX(x);
		enemy.y = ZeldaGameState.itemYToOverworldY(y);
		handler.getZeldaGameState().entitiesToAdd.add(enemy);
	}

	public boolean isStaggering() {
		return this.staggering;
	}
	
	public void stagger() {
		this.staggering = true;
		this.invulnerable = false;
		this.moving = false;
		this.dashing = false;
		this.staggerCooldown = 5 * 60;
		this.canHurtLink = false;
		this.strafing = false;
		this.knockBack = 1;
		this.speed = 0;
		
	}
	
	public void setStrafing(boolean bool) {
		this.strafing = bool;
		this.dashing = !bool;
	}
	
	public void setDashing(boolean bool) {
		this.strafing = !bool;
		this.dashing = bool;
	}
	
	public boolean isDashing() {
		return this.dashing;
	}
	
	@Override
	public boolean isInvulnerable() {
		return super.isInvulnerable() || this.invulnerable;
	}
	
	public void setInvulnerable(boolean i) {
		this.invulnerable = i;
	}
	
	public int getPhase() {return this.phase;}
	public void setPhase(int phase) {this.phase = phase;}
	
	
	@Override
	public boolean isStuck() {
		return false;
	}
	
	
	public boolean isPositioned() {
		return positioned;
	}

	public void setPositioned(boolean positioned) {
		this.positioned = positioned;
	}

	public boolean canHurtLink() {
		return canHurtLink;
	}

	public void setCanHurtLink(boolean canHurtLink) {
		this.canHurtLink = canHurtLink;
	}

	@Override
	public void move (Direction direction) {
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
	protected int moveCooldown() {
		return 0;
	}

	@Override
	protected int stayInPlaceCooldown() {
		return 0;
	}

}
