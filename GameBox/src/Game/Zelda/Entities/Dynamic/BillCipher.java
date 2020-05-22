package Game.Zelda.Entities.Dynamic;

import static Game.GameStates.Zelda.ZeldaGameState.worldScale;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import Game.GameStates.Zelda.ZeldaGameState;
import Game.Zelda.Entities.Statics.Item;
import Game.Zelda.World.Dungeon;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

public class BillCipher extends Enemy {

	/*
	 * TODO: ADD DEATH QUOTE
	 * 
	 * 
	 */

	// Starting phase variables
	private int teleCooldown = 60;
	private boolean /*drawSymbol,*/ start, disguised = true;

	// Battle Variables
	protected int phase = -1, teleportations = 0, maxHealth, hoverTick = 0;
	protected int maxTeleportations, teleportSpeed, attacks, maxAttacks, attackDelay;
	protected boolean idle, summoning, teleporting, returning, staggering, attacking, finalMove;
	// Phase 3 Attacks
	protected int targetX, targetY, clonesOut = 2, fireAttacks = 0, defeatedCD = 10 * 60;
	protected boolean cloning;
	protected boolean invulnerable = false, defeated = false, shotFire = false;
	protected BufferedImage frame;
	protected int animSpeed = 200;
	protected Animation fireAttack, eyeAnim, growthAnim, triforce = new Animation(animSpeed, Images.triForce);
	protected boolean noMoreClones = false;

	// Cooldowns
	protected int idleCD, summonCD, teleportCD, staggerCD, cloneCD, fireCD;

	public BillCipher(int x, int y, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
		this.speed = 4;
		this.health = 200;
		this.maxHealth = health;
	}

	@Override
	public void tick() {
		hoverTick++;
		if (defeated) {
			canHurtLink = false;
			if (defeatedCD > 0) {

				if (defeatedCD > 3 * 60) {
					if (defeatedCD % 5 == 0) {
						this.x = new Random().nextBoolean() ? x + 1 : x - 1;
						this.y = new Random().nextBoolean() ? y + 1 : y - 1;
					}
				}

				else {

				}

				defeatedCD--;

			}

			else {
				handler.getZeldaGameState().toRemove2.add(this);
				handler.getMusicHandler().playEffect("explosion.wav");
			}
			return;
		}

		super.tick();

		if (phase == -1) {
			if (disguised) {
				triforce.tick();
				
				if (handler.getZeldaGameState().link.bounds.intersects(this.bounds)) {
					this.damage(0);
				}
			
			}
			if (start) {
				if (teleCooldown == -20) {
					handler.getMusicHandler().playEffect("laugh2.wav");
				}

				else if (teleCooldown < -100) {

					if (handler.getState() instanceof Dungeon) {
						((Dungeon) handler.getState()).getCurrentRoom().setDoors(true, true, true, true);
					}

					//drawSymbol = true;
					handler.getMusicHandler().changeMusic("weirdmaggaedon.wav");
					this.changePhase(0);
				}

				else if (teleCooldown < 0) {
					this.x = handler.getWidth() / 2;
					this.y = 50;

				}

				else {
					this.x = -100;
					this.y = -100;
				}

				teleCooldown--;

			}

			return;
		}

		if (health <= maxHealth / 3) {
			if (phase != 2)
				changePhase(2);
		} else if (health <= maxHealth * 2 / 3) {
			if (phase != 1)
				changePhase(1);
		} else if (health <= maxHealth) {
			if (phase != 0)
				changePhase(0);
		}

		if (hit) {
			damageAnim.tick();
		} else if (!hit && damageAnim.end) {
			damageAnim.reset();
		}

		if (staggering) {
			invulnerable = false;
			if (staggerCD > 0) {
				staggerCD--;
			} else {
				returnBack();
			}
		}

		else if (returning) {

		}

		else if (cloning) {
			if (cloneCD > 0) {
				cloneCD--;
			} else if (clonesOut < 16 && !noMoreClones) {
				for (int i = 0; i < 2; i++) {
					// Skip Cypher
					if (clonesOut == targetX) {
						clonesOut += 2;
					}
					CipherClone clone1 = new CipherClone(this, ZeldaGameState.itemXToOverworldX(clonesOut), this.y);
					handler.getZeldaGameState().entitiesToAdd.add(clone1);
				}
				String track = new Random().nextBoolean() ? "laugh1.wav" : "laugh2.wav";
				handler.getMusicHandler().playEffect(track);
				cloneCD = 20;
				fireCD = 6 * 60;
			} else {

				if (this.allClonesArePositioned()) {
					invulnerable = false;
					noMoreClones = true;
					if (fireCD > 0) {
						fireCD--;
					} else {

						for (BaseMovingEntity clone : handler.getZeldaGameState().entitiesToTick) {
							if (clone instanceof CipherClone || clone instanceof BillCipher) {
								((BillCipher) clone).attacking = true;
								((BillCipher) clone).fireAttacks = phase + 1;
//								System.out.println(((BillCipher) clone).attacking);
//								System.out.println(((BillCipher) clone).shotFire);
//								System.out.println(((BillCipher) clone).fireAttacks);
//								System.out.println("===");
							}
						}
						cloning = false;
						noMoreClones = false;
					}

				}
			}
		}

		// Do stuff related to summoning
		else if (summoning) {
			if (summonCD > 0) {
				summonCD--;
			} else {
				summon();
				returnBack();
			}
		}

		else if (attacking) {
			if (fireAttack.end) {
				if (fireAttacks > 0) {
					fireAttacks--;
				} else {
					returnBack();
				}
				fireAttack.reset();
				shotFire = false;
			} else if (fireAttack.getIndex() == 8 && !shotFire) {
				shootFire();
				shotFire = true;
			}

			fireAttack.tick();
		}

		// Teleport randomly
		else if (teleporting) {
			invulnerable = false;
			if (teleportations >= maxTeleportations && teleportCD <= 0) {
				returnBack();
			} else if (teleportCD > 0) {
				teleportCD--;
			} else {
				teleportations++;
				teleport();
				teleportCD = teleportations >= maxTeleportations ? 60 : teleportSpeed;

			}
		}

		else if (finalMove) {
			if (this.eyeAnim.end) {
				// Finished Throwing Eye

				if (eyeIsDead()) {
					finalMove = false;
					growthAnim.reset();
					eyeAnim.reset();
					returnBack();
				}

				if (growthAnim.end) {
					fireAttacks = 1 + phase;
					attacking = true;
				}
				growthAnim.tick();
			}

			else {
				eyeAnim.tick();

				if (eyeAnim.getIndex() == 6 && eyeIsDead()) {
					this.shootEye();
				}

			}
		}

		// He's not moving
		else if (idle) {
			invulnerable = true;
			if (idleCD > 0) {
				idleCD--;
			} else if (attacks >= maxAttacks) {
				teleporting = true;
				attacks = 0;
			}

			else {
				idle = false;
				attack();
			}
		}

	}

	private boolean eyeIsDead() {
		for (BaseMovingEntity b : handler.getZeldaGameState().entitiesToTick) {
			if (b instanceof Eye) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void render(Graphics g) {
		if (phase == -1 && disguised) {
			BufferedImage frame = triforce.getCurrentFrame();
			g.drawImage(frame, this.x, this.y, frame.getWidth() * ZeldaGameState.worldScale, frame.getHeight() * ZeldaGameState.worldScale, null);
			return;
		}
		int hoverShift = 0;
		if (hoverTick > 60) {
			hoverTick = 0;
		} else if (hoverTick > 30) {
			hoverShift = 2;
		}

		if (defeated) {
			if (this.defeatedCD % 5 == 0) {
				phase = new Random().nextInt(4);
			}
			frame = Images.billDown[phase];
		}

		else if (phase == -1) {
			switch (this.direction) {
			case DOWN:
				if (handler.getZeldaGameState().link.x < this.x - 20) {
					frame = Images.billLookingLeft[0];
				} else if (handler.getZeldaGameState().link.x > this.x + 20) {
					frame = Images.billLookingRight[0];
				} else {
					frame = Images.billDown[0];
				}
				break;
			case LEFT:
				frame = Images.billLeft[0];
				break;
			case RIGHT:
				frame = Images.billRight[0];
				break;
			default:
				frame = Images.billUp[0];
				break;
			}
		}

		else if (hit) {
			frame = this.damageAnim.getCurrentFrame();
		}

		else if (summoning) {
			switch (this.direction) {
			case UP:
				frame = Images.billSummonBack[phase];
				break;

			default:
				frame = Images.billSummon[phase];
				break;
			}
		}

		else if (attacking) {
			this.frame = this.fireAttack.getCurrentFrame();
		}

		else if (finalMove) {
			if (this.eyeAnim.end) {

				if (this.growthAnim.end) {

					switch (this.direction) {
					case DOWN:
						if (handler.getZeldaGameState().link.x < this.x - 20) {
							frame = Images.billLookingLeft[phase];
						} else if (handler.getZeldaGameState().link.x > this.x + 20) {
							frame = Images.billLookingRight[phase];
						} else {
							frame = Images.billDown[phase];
						}
						break;
					case LEFT:
						frame = Images.billLeft[phase];
						break;
					case RIGHT:
						frame = Images.billRight[phase];
						break;
					default:
						frame = Images.billUp[phase];
						break;
					}

				}

				else {
					frame = this.growthAnim.getCurrentFrame();
				}
			}

			else {
				frame = this.eyeAnim.getCurrentFrame();
			}
		} else {
			switch (this.direction) {
			case DOWN:
				if (handler.getZeldaGameState().link.x < this.x - 20) {
					frame = Images.billLookingLeft[phase];
				} else if (handler.getZeldaGameState().link.x > this.x + 20) {
					frame = Images.billLookingRight[phase];
				} else {
					frame = Images.billDown[phase];
				}
				break;
			case LEFT:
				frame = Images.billLeft[phase];
				break;
			case RIGHT:
				frame = Images.billRight[phase];
				break;
			default:
				frame = Images.billUp[phase];
				break;
			}
		}

//		if (drawSymbol) {
//			if (handler.getState() instanceof Dungeon) {
//				Room room = ((Dungeon) handler.getState()).getCurrentRoom();
//				g.drawImage(Images.billSymbol, ZeldaGameState.itemXToOverworldX(room.getleftX()+2), 
//						ZeldaGameState.itemYToOverworldY(room.gettopY()+2), 400,400, null);
//			}
//		}

		if (frame != null) {
			g.drawImage(frame, x, y + hoverShift, frame.getWidth() * ZeldaGameState.worldScale,
					frame.getHeight() * ZeldaGameState.worldScale, null);
		}

	}

	@Override
	public void moveAlgorithm() {

		if (returning) {
			invulnerable = true;
			canHurtLink = false;
			idle = false;
			speed = 4;
			if (this.y > 60) {
				this.direction = Direction.UP;
				move(direction);
			} else if (this.y < 40) {
				this.direction = Direction.DOWN;
				move(direction);
			} else if (this.x < handler.getWidth() / 2 - 10) {
				this.direction = Direction.RIGHT;
				move(direction);
			} else if (this.x > handler.getWidth() / 2 + 10) {
				this.direction = Direction.LEFT;
				move(direction);
			} else {
				returning = false;
				idle = true;
				canHurtLink = true;
				idleCD = attackDelay * 60;
				this.direction = Direction.DOWN;
			}
		}

		else if (attacking && !clonesExist()) {
			if (this.x < handler.getZeldaGameState().link.x - 10) {
				this.direction = Direction.RIGHT;
				move(direction);
			} else if (this.x > handler.getZeldaGameState().link.x + 10) {
				this.direction = Direction.LEFT;
				move(direction);
			} else {
				this.x = handler.getZeldaGameState().link.x;
				bounds.x = x;
			}
		}

		else if (cloning) {
			if (this.x > targetX + 10) {
				this.direction = Direction.LEFT;
				move(direction);
			} else if (this.x < targetX - 10) {
				this.direction = Direction.RIGHT;
				move(direction);
			} else {
				this.direction = Direction.DOWN;
			}
		}

	}

	@Override
	public void move(Direction direction) {
		moving = true;
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
	public boolean isStuck() {
		// Can phase through walls
		return false;
	}

	public void summon() {

		int enemies = new Random().nextInt(2) + phase + 1;

		do {
			BaseMovingEntity enemy;
			switch (new Random().nextInt(2 + phase)) {
			case 0:
				enemy = new Snake(0, 0, Images.snake, handler) {
					@Override
					protected Item getRandomItem(int x, int y) {
						if (new Random().nextInt(8) == 0 ) {
							return new Item(x,y, "heart", Images.linkHeart, handler );
						}
						return null;
					}
				};
				break;
			case 1:
				enemy = new Tektite(0, 0, Images.bouncyEnemyFrames, handler) {
					@Override
					protected Item getRandomItem(int x, int y) {
						if (new Random().nextInt(8) == 0 ) {
							return new Item(x,y, "heart", Images.linkHeart, handler );
						}
						return null;
					}
				};
				break;

			case 2:
				enemy = new Tektite(0, 0, Images.blueTektike, handler) {
					@Override
					protected Item getRandomItem(int x, int y) {
						if (new Random().nextInt(8) == 0 ) {
							return new Item(x,y, "heart", Images.linkHeart, handler );
						}
						return null;
					}
				};		
				enemy.power = 3;
				enemy.health = 10;
				break;

			default:
				enemy = new Darknut(0, 0, Images.DarknutUp, handler) {
					@Override
					protected Item getRandomItem(int x, int y) {
						if (new Random().nextInt(8) == 0) {
							return new Item(x,y, "heart", Images.linkHeart, handler );
						}
						return null;
					}
				};						
				break;
			}

			enemy.x = ZeldaGameState.itemXToOverworldX(new Random().nextInt(10) + 2);
			enemy.y = ZeldaGameState.itemYToOverworldY(new Random().nextInt(6) + 2);
			handler.getZeldaGameState().entitiesToAdd.add(enemy);

			enemies--;
		} while (enemies > 0);

	}

	public void teleport() {
		// Make sure not all his teleportations are bull
		if (teleportations >= 2) {

			if (handler.getState() instanceof Dungeon) {
				this.x = ZeldaGameState.itemXToOverworldX(new Random().nextInt(Dungeon.roomWidth - 2));
				this.y = ZeldaGameState.itemYToOverworldY(new Random().nextInt(Dungeon.roomHeight - 2));
			}

			// If for some reason Bill is out of a dungeon
			else {
				this.x = ZeldaGameState.itemXToOverworldX(new Random().nextInt(14));
				this.y = ZeldaGameState.itemYToOverworldY(new Random().nextInt(8));
			}

		}

		// Randomly teleport
		else {
			this.x = new Random().nextInt(handler.getWidth());
			this.y = new Random().nextInt(handler.getHeight());
		}

	}

	private void changePhase(int phase) {
		this.phase = phase;

		switch (phase) {
		case 0:
			maxTeleportations = 4;
			teleportSpeed = 60;
			maxAttacks = 3;
			attackDelay = 5;
			break;

		case 1:
			teleportSpeed = 45;
			maxAttacks = 4;
			attackDelay = 4;
			break;

		case 2:
			maxTeleportations = 8;
			teleportSpeed = 20;
			maxAttacks = 5;
			attackDelay = 2;
			break;
		}

		BufferedImage[] frames = { Images.billDown[phase], Images.billDown[3], Images.billDown[phase],
				Images.billDown[3] };

		returnBack();
		this.fireAttack = new Animation(animSpeed, Images.billFireAttack[phase]);
		this.eyeAnim = new Animation(animSpeed, Images.billEyeThrow[phase]);
		this.growthAnim = new Animation(animSpeed, Images.billEyeGrowth[phase]);
		this.damageAnim = new Animation(animSpeed, frames);
		attacks = 0;

	}

	public boolean defeated() {
		return this.defeated && this.defeatedCD <= 0;
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
	public boolean isInvulnerable() {
		return (this.invulnerable || damageCooldown > 0) && !this.defeated;
	}

	@Override
	public void damage(int amount) {
		if (phase == -1) {
			if (!start) {
				handler.getMusicHandler().playEffect("laugh1.wav");
				disguised = false;
			}
			start = true;
			return;
		}
		if (health < 0 || defeated)
			return;
		health -= amount;
		defeated = health <= 0;

		if (defeated) {
			health = 1;
			handler.getMusicHandler().stopMusic();
			handler.getMusicHandler().playEffect("billDeath.wav");
			removeAll();
			return;
		}

		hit = true;
		if (!staggering) {
			stagger();
			staggerCD = 5 * 60;
		}

		damageCooldown = 20;
	}

	private void stagger() {
		returnBack();
		this.returning = false;
		invulnerable = false;
		canHurtLink = false;
		noMoreClones = false;
		this.staggering = true;
		staggerCD = 5 * 60;
		removeClones();
	}

	private void attack() {
		int number = new Random().nextInt(2 + phase);
		switch (number) {
		case 0: // Default Attack 1: Summon other Enemies
			summoning = true;
			summonCD = 5 * 60;
			break;
		case 1: // Default Attack 2: Fireball
			attacking = true;
			fireAttacks = phase + 1;
			break;
		case 2: // Phase 2 Attack: Clone Self
			cloning = true;
			int tarX = new Random().nextInt(6) + 2;
			if (tarX % 2 != 0) {
				tarX++;
			}
			targetX = ZeldaGameState.itemXToOverworldX(tarX);
			break;
			
		case 3: // Final Attack
			finalMove = true;
			break;
//		case 4: // Phase 4 Attack:
//			idle = true;
//			return;
		default: // Retry
			idle = true;
			return;
		}

		attacks++;

	}

	private void returnBack() {
		teleportations = 0;
		returning = true;
		invulnerable = true;
		canHurtLink = true;
		staggering = false;
		attacking = false;
		summoning = false;
		teleporting = false;
		cloning = false;

	}

	private boolean clonesExist() {
		for (BaseMovingEntity enemy : handler.getZeldaGameState().entitiesToTick) {
			if (enemy instanceof CipherClone) {
				return true;
			}
		}

		return false;
	}

	private boolean eyeExists() {
		for (BaseMovingEntity enemy : handler.getZeldaGameState().entitiesToTick) {
			if (enemy instanceof Eye) {
				return true;
			}
		}

		return false;
	}

	private boolean allClonesArePositioned() {
		if (!clonesExist()) {
			return false;
		}
		for (BaseMovingEntity enemy : handler.getZeldaGameState().entitiesToTick) {
			if (enemy instanceof CipherClone) {
				if (!((CipherClone) enemy).isPositioned()) {
					return false;
				}
			}
		}

		return true;
	}

	protected void shootFire() {
		Projectile ball = new Projectile(this.x, this.y + this.height, Images.billFire, 10, 4, Direction.DOWN, this,
				handler);
		ball.setBlockable(eyeExists());
		handler.getZeldaGameState().entitiesToAdd.add(ball);
	}

	protected void shootEye() {
		BufferedImage[] arr = new BufferedImage[1];
		arr[0] = phase == 0 ? Images.billEye : Images.billEye2;
		Projectile ball = new Eye(this.x, this.y + this.height, handler.getWidth() / 2, handler.getHeight() / 2, arr, 5,
				2, Direction.DOWN, this, handler);
		ball.setBlockable(false);
		handler.getZeldaGameState().entitiesToAdd.add(ball);
	}

	private void removeClones() {
		for (BaseMovingEntity clone : handler.getZeldaGameState().entitiesToTick) {
			if (clone instanceof CipherClone) {
				clone.damage(100);
			}
		}
	}
	
	private void removeAll() {
		for (BaseMovingEntity b : handler.getZeldaGameState().entitiesToTick) {
			if (!(b instanceof BillCipher  || b instanceof Projectile)) {
				handler.getZeldaGameState().toRemove2.add(b);
			}
		}
	}
	
	@Override
	protected Item getRandomItem(int x, int y) {
		return new Item(ZeldaGameState.OverworldXToItemX(handler.getZeldaGameState().link.x), ZeldaGameState.OverworldYToItemY(handler.getZeldaGameState().link.y), "heManKey", Images.linkKey2, handler);
	}

}

class CipherClone extends BillCipher {

	private boolean positioned = true;
	private BillCipher cloner;

	public CipherClone(BillCipher cloner, int targetX, int targetY) {
		super(cloner.x, cloner.y, Images.billUp, cloner.handler);
		this.health = cloner.health;
		this.targetX = targetX;
		this.targetY = targetY;
		this.cloner = cloner;
		this.phase = cloner.phase;
		this.fireAttack = cloner.fireAttack;
		this.damageAnim = cloner.damageAnim;
		cloner.clonesOut += 2;
	}

	@Override
	public void moveAlgorithm() {
		if (this.x > targetX + 10) {
			this.direction = Direction.LEFT;
			move(direction);
		} else if (this.x < targetX - 10) {
			this.direction = Direction.RIGHT;
			move(direction);
		} else if (this.y > targetY + 10) {
			this.direction = Direction.UP;
			move(direction);
		} else if (this.y < targetY - 10) {
			this.direction = Direction.DOWN;
			move(direction);
		} else {
			positioned = true;
			this.direction = Direction.DOWN;
		}
	}

	public boolean isPositioned() {
		return this.positioned;
	}

	@Override
	public Item getRandomItem(int x, int y) {
		return null;
	}

	@Override
	public void damage(int amount) {
		handler.getZeldaGameState().toRemove2.add(this);
		cloner.clonesOut -= 2;
	}

	@Override
	public void shootFire() {
		super.shootFire();
		this.damage(1);
	}

}

class Eye extends Projectile {

	boolean positioned = false;
	int hitboxSize = 20;
	int suckSpeed = 3;
	int targetX, targetY, windboxSize = 300, windboxDistance = 300, delay = 45, lifeTime = 5 * 60;
	Rectangle up, down, left, right;

	public Eye(int x, int y, int targetX, int targetY, BufferedImage[] sprite, int speed, int power, Direction d,
			BaseMovingEntity owner, Handler handler) {
		super(x, y, sprite, speed, power, d, owner, handler);
		this.targetX = targetX;
		this.targetY = targetY;
	}

	@Override
	public void tick() {
		super.tick();
		if (positioned) {

			if (delay > 0) {
				delay--;
				return;
			}

			for (BaseMovingEntity entity : handler.getZeldaGameState().entitiesToTick) {
				if (!entity.equals(this.getOwner()) && !(entity instanceof Projectile)) {
					int oldKB = entity.knockBack;
					entity.knockBack = suckSpeed;

					if (entity.bounds.intersects(left)) {
						entity.push(Direction.RIGHT);
					}
					if (entity.bounds.intersects(right)) {
						entity.push(Direction.LEFT);
					}
					if (entity.bounds.intersects(up)) {
						entity.push(Direction.DOWN);
					}
					if (entity.bounds.intersects(down)) {
						entity.push(Direction.UP);
					}

					entity.knockBack = oldKB;

				}
			}

			Link link = handler.getZeldaGameState().link;
			int oldKB = link.knockBack;
			link.knockBack = suckSpeed;
			if (link.bounds.intersects(left)) {
				link.push(Direction.RIGHT);
			}
			if (link.bounds.intersects(right)) {
				link.push(Direction.LEFT);
			}
			if (link.bounds.intersects(up)) {
				link.push(Direction.DOWN);
			}
			if (link.bounds.intersects(down)) {
				link.push(Direction.UP);
			}

			link.knockBack = oldKB;

			if (lifeTime < 0) {
				handler.getZeldaGameState().toRemove2.add(this);
			}

			else {
				if (lifeTime % 5 == 0) {
					hitboxSize++;
				}
				lifeTime--;
			}

			move(this.direction);
			this.hitbox = new Rectangle(this.x - hitboxSize, this.y - hitboxSize, hitboxSize * 2 + this.width,
					hitboxSize * 2 + this.height);

		}
	}

	@Override
	public void render(Graphics g) {
		super.render(g);

		if (ZeldaGameState.showHitboxes && this.delay <= 0) {
			g.setColor(Color.GREEN);
			if (up != null) {
				g.drawRect(up.x, up.y, up.width, up.height);
			}
			g.setColor(Color.RED);
			if (down != null) {
				g.drawRect(down.x, down.y, down.width, down.height);
			}
			g.setColor(Color.BLUE);
			if (left != null) {
				g.drawRect(left.x, left.y, left.width, left.height);
			}
			g.setColor(Color.MAGENTA);
			if (right != null) {
				g.drawRect(right.x, right.y, right.width, right.height);
			}
			g.setColor(Color.YELLOW);

		}

		if (positioned && !(delay > 0)) {
			g.drawImage(Images.flipVertical(this.animation.getCurrentFrame()), hitbox.x, hitbox.y, hitbox.width,
					hitbox.height, null);
		}
	}

	@Override
	public void move(Direction d) {

		if (positioned) {
			return;
		}

		super.move(d);

		if (this.x > targetX + speed * 2) {

		} else if (this.x < targetX - speed * 2) {

		} else if (this.y > targetY + speed * 2) {

		} else if (this.y < targetY - speed * 2) {

		} else {
			positioned = true;
			// Add windboxes
			int xRect = 0, yRect = 0, wRect = 0, hRect = 0;
			BufferedImage frame = this.animation.getCurrentFrame();

			xRect = this.x - windboxSize / 2;
			wRect = frame.getWidth() * worldScale + windboxSize;
			yRect = this.y - windboxDistance;
			hRect = windboxDistance;
			up = new Rectangle(xRect, yRect, wRect, hRect);

			xRect = this.x - windboxSize / 2;
			wRect = frame.getWidth() * worldScale + windboxSize;
			yRect = this.y + this.height;
			hRect = windboxDistance;
			down = new Rectangle(xRect, yRect, wRect, hRect);

			xRect = this.x - windboxDistance;
			wRect = windboxDistance;
			yRect = this.y - windboxSize / 2;
			hRect = frame.getHeight() * worldScale + windboxSize;
			left = new Rectangle(xRect, yRect, wRect, hRect);

			xRect = this.x + this.width;
			wRect = windboxDistance;
			yRect = this.y - windboxSize / 2;
			hRect = frame.getHeight() * worldScale + windboxSize;
			right = new Rectangle(xRect, yRect, wRect, hRect);

		}
	}

	@Override
	public void delete() {

	}

}
