
package Game.Zelda.Entities.Dynamic;

import static Game.GameStates.Zelda.ZeldaGameState.worldScale;
import static Game.Zelda.Entities.Dynamic.Direction.DOWN;
import static Game.Zelda.Entities.Dynamic.Direction.LEFT;
import static Game.Zelda.Entities.Dynamic.Direction.RIGHT;
import static Game.Zelda.Entities.Dynamic.Direction.UP;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import Game.GameStates.Zelda.BossBattleState;
import Game.GameStates.Zelda.ZeldaGameState;
import Game.Zelda.Entities.Statics.DungeonDoor;
import Game.Zelda.Entities.Statics.Item;
import Game.Zelda.Entities.Statics.SectionDoor;
import Game.Zelda.Entities.Statics.SolidStaticEntities;
import Game.Zelda.World.Dungeon;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

/**
 * Created by AlexVR on 3/15/2020
 */
public class Link extends BaseMovingEntity {

	private final int animSpeed = 120;
	int newMapX=0,newMapY=0,xExtraCounter=0,yExtraCounter=0, attackCooldown = 0, bowAttackCooldown = 0, projectileCooldown=0;
	private Animation attackDown, attackUp, attackLeft, attackRight, damageAnim;
	public Animation deathAnim;
	private final int damageAnimSpeedMult = 2;
	public boolean canAttack = true;

	// ORIGINAL VALUES: Distance = 35, Size = 10;
	private int hitBoxDistance = 30, hitBoxSize = 5;
	public Rectangle hitBox = new Rectangle(0,0,0,0);
	public int waitCooldown = 0;
 
	// CHANGE ME TO CHANGE CAMSPEED
	int camSpeed = 6;

	// MAX HEALTH
	public int maxHealth = 6;
	public boolean hasKey = true;
	public boolean movingMap = false,  attacking = false, attackingBow = false;
	public boolean hasSword = false, hasSecondSword = false, hasFinalSword = false;
	Direction movingTo;

	public int potions = 3, rupees = 300, arrows = 20, keys = 2;

	public Link(int x, int y, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
		speed = 4;
		health = 6;
		BufferedImage[] animList = new BufferedImage[2];
		BufferedImage[] deathFrames = {
				Images.zeldaLinkFrames[0], Images.zeldaLinkFrames[2], Images.zeldaLinkFrames[4],
				Images.flipHorizontal(Images.zeldaLinkFrames[2]), Images.zeldaLinkFrames[0], 
				Images.zeldaLinkFrames[2], Images.zeldaLinkFrames[4],
				Images.flipHorizontal(Images.zeldaLinkFrames[2]), Images.zeldaLinkFrames[0]
		};
		deathAnim = new Animation(animSpeed, deathFrames);
		animList[0] = sprite[4];
		animList[1] = sprite[5];
		damageAnim = new Animation(animSpeed / damageAnimSpeedMult, Images.linkDamage);
		animation = new Animation(animSpeed,animList);
		this.bounds = new Rectangle(x,y,width,height);
		this.direction = DOWN;
	}

	@Override
	public void tick() {

		if (this.isDead()) {
			deathAnim.tick();
			return;
		}

		if (attackCooldown > 0) {attackCooldown--;}
		if (bowAttackCooldown > 0) {bowAttackCooldown--;}
		if (!hit) {if (damageCooldown > 0) {damageCooldown--;}}
		if (projectileCooldown > 0) {projectileCooldown--;}
		if (waitCooldown > 0) {waitCooldown--; return;}
		if (bowAttackCooldown <= 0) { attackingBow = false;}
		if (movingMap){
			switch (movingTo) {
			case RIGHT:
				handler.getZeldaGameState().cameraOffsetX+=camSpeed;
				newMapX = newMapX + camSpeed > 0 ? 0 : newMapX + camSpeed;
				if (xExtraCounter>0){
					x++;
					xExtraCounter--;
					animation.tick();
				}
				else{x-= camSpeed;}
				break;
			case LEFT:
				handler.getZeldaGameState().cameraOffsetX-=camSpeed;
				newMapX = newMapX - camSpeed < 0 ? 0 : newMapX - camSpeed;
				if (xExtraCounter>0){
					x--;
					xExtraCounter--;
					animation.tick();
				}
				else{x+= camSpeed;}

				break;
			case UP:
				handler.getZeldaGameState().cameraOffsetY-=camSpeed;
				newMapY = newMapY + camSpeed > 0 ? 0 : newMapY + camSpeed;
				if (yExtraCounter>0){
					y--;
					yExtraCounter--;
					animation.tick();
				}
				else{y+= camSpeed;}
				break;
			case DOWN:
				handler.getZeldaGameState().cameraOffsetY+=camSpeed;
				newMapY = newMapY - camSpeed < 0 ? 0 : newMapY - camSpeed;
				if (yExtraCounter>0){
					y++;
					yExtraCounter--;
					animation.tick();
				}
				else{y-= camSpeed;}
				break;
			}
			bounds = new Rectangle(x,y,width,height);
			changeIntersectingBounds();
			if (newMapX == 0 && newMapY == 0){
				movingMap = false;
				movingTo = null;

				// Enemies move back to their original place
				handler.getZeldaGameState().refreshWorld();
			}

		}

		else if (hit) {
			if (damageAnim.end) {refreshDamageAnim(); hit = false; return;}
			damageAnim.tick();
			switch (this.direction) {
			case DOWN:
				push(UP);
				break;
			case LEFT:
				push(RIGHT);
				break;
			case RIGHT:
				push(LEFT);
				break;
			case UP:
				push(DOWN);
				break;  
			}
		}

		else if (attacking) {
			this.power = this.hasFinalSword ? (4) : this.hasSecondSword ? (2) : this.hasSword ? (1) : 0;
			switch(this.direction) {
			case UP:
				attackUp.tick();
				if (attackUp.end) {attackUp.reset(); attacking = false;}
				break;
			case DOWN:
				attackDown.tick();
				if (attackDown.end) {attackDown.reset(); attacking = false;}
				break;
			case LEFT:
				attackLeft.tick();
				if (attackLeft.end) {attackLeft.reset(); attacking = false;}
				break;
			case RIGHT:
				attackRight.tick();
				if (attackRight.end) {attackRight.reset(); attacking = false;}
				break;

    		}
    		
    		// Check if Enemy collides with hitbox
    		for (BaseMovingEntity entity : handler.getZeldaGameState().entitiesToTick) {
    			if (entity.bounds.intersects(this.hitBox)) {

    				if (!entity.isInvulnerable() && !entity.dead) {
        				entity.damage(this.power);
        				entity.shoveDir = this.direction;
        			}
    			}

    		}
    		
    	}

		else {
			super.tick();
			if (handler.getKeyManager().up) {
				if (direction != UP) {
					BufferedImage[] animList = new BufferedImage[2];
					animList[0] = sprites[4];
					animList[1] = sprites[5];
					animation = new Animation(animSpeed, animList);
					direction = UP;
					sprite = sprites[4];
				}
				animation.tick();
				move(direction);

			} else if (handler.getKeyManager().down) {
				if (direction != DOWN) {
					BufferedImage[] animList = new BufferedImage[2];
					animList[0] = sprites[0];
					animList[1] = sprites[1];
					animation = new Animation(animSpeed, animList);
					direction = DOWN;
					sprite = sprites[0];
				}
				animation.tick();
				move(direction);
			} else if (handler.getKeyManager().left) {
				if (direction != Direction.LEFT) {
					BufferedImage[] animList = new BufferedImage[2];
					animList[0] = Images.flipHorizontal(sprites[2]);
					animList[1] = Images.flipHorizontal(sprites[3]);
					animation = new Animation(animSpeed, animList);
					direction = Direction.LEFT;
					sprite = Images.flipHorizontal(sprites[3]);
				}
				animation.tick();
				move(direction);
			} else if (handler.getKeyManager().right) {
				if (direction != Direction.RIGHT) {
					BufferedImage[] animList = new BufferedImage[2];
					animList[0] = (sprites[2]);
					animList[1] = (sprites[3]);
					animation = new Animation(animSpeed, animList);
					direction = Direction.RIGHT;
					sprite = (sprites[3]);
				}
				animation.tick();
				move(direction);
			} else {
				moving = false;
			}


			if (this.canAttack() && (handler.getKeyManager().keyJustPressed(KeyEvent.VK_B)&& bowAttackCooldown <= 0 && this.arrows >0)) {
				attackingBow = true;
				handler.getMusicHandler().playEffect("arrow_shot.wav");

				if (projectileCooldown <= 0) {
					BufferedImage[] arrowImage = new BufferedImage[1];
					arrowImage[0] = Images.linkArrow[0];
					int x = 0; int y = 0;
					switch(this.direction) {
					case DOWN:
//						arrowImage[0] = Images.linkArrow[3];
						x = this.x + arrowImage[0].getWidth()*worldScale/2; y = this.y+this.height;
						break;
					case LEFT:
//						arrowImage[0] = Images.linkArrow[2];
						x = this.x; y = this.y + this.height/3;
						break;
					case RIGHT:
//						arrowImage[0] = Images.linkArrow[1];
						x = this.x + this.width; y = this.y + this.height/3;
						break;
					case UP:
//						arrowImage[0] = Images.linkArrow[0];
						x = this.x + arrowImage[0].getWidth()*worldScale/2; y = this.y;
						break;
					default:
						x = this.x ; y = this.y;
//						arrowImage[0] = Images.LinkBow[1];
						break;

					}


					Projectile arrow = new Projectile(x,y,arrowImage, 4, 3, this.direction, this, handler);
					projectileCooldown = 35;
					bowAttackCooldown = 35;
					handler.getZeldaGameState().entitiesToTick.add(arrow);
					this.arrows --;

				}
			}
			
			// NO ARROWS LEFT
			else if(this.canAttack() && (handler.getKeyManager().keyJustPressed(KeyEvent.VK_B)&& bowAttackCooldown <= 0 && this.arrows <=0)) {
				handler.getMusicHandler().playEffect("noArrows.wav");
			}
				
				
			else if ( this.canAttack() && (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_SPACE))&& attackCooldown <= 0) {
				attacking = true;
				handler.getMusicHandler().playEffect("sword_attack.wav");
				attackCooldown = 5;
				if (this.health == this.maxHealth  && projectileCooldown <= 0 && !this.projectileIsAlive()) {
					BufferedImage[] swordImage = new BufferedImage[1];
					swordImage[0] = this.hasSword ? Images.linkSword : this.hasSecondSword ? Images.secondSword : this.hasFinalSword ? Images.masterSword : null;
					int x = 0, y = 0;
					switch(this.direction) {
					case DOWN:
						x = this.x + swordImage[0].getWidth()*worldScale/2; y = this.y+this.height;
						break;
					case LEFT:
						x = this.x; y = this.y + swordImage[0].getHeight()*worldScale/2;
						break;
					case RIGHT:
						x = this.x + this.width; y = this.y + swordImage[0].getHeight()*worldScale/2;
						break;
					case UP:
						x = this.x + swordImage[0].getWidth()*worldScale/2; y = this.y;
						break;
					default:
						x = this.x ; y = this.y;
						break;

					}

					Projectile sword = new Projectile(x,y,swordImage, 6, this.power, this.direction, this, handler);
					projectileCooldown = 60;
					attackingBow = false;
					bowAttackCooldown = 35;
					handler.getZeldaGameState().entitiesToTick.add(sword);
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {

		if (isDead() && !this.deathAnim.end) {
			g.drawImage(this.deathAnim.getCurrentFrame(), this.x, this.y, width, height, null);
			return;
		}

		if (handler.getZeldaGameState().itemObtained) {
			g.drawImage(Images.linkHoldingItem2,x, y, width,height,null);
		}

		else if (hit) {
			BufferedImage frame = this.damageAnim.getCurrentFrame();
			g.drawImage(frame,x , y , frame.getWidth() * worldScale , frame.getHeight() * worldScale, null);
		}

		else if (attacking) {
			BufferedImage frame;
			// For left/up purposes
			int xShift = 0, yShift = 0;
			int xRect=0, yRect=0, wRect=0, hRect=0;
			switch(this.direction) {	
			case UP:
				frame = this.attackUp.getCurrentFrame();
				yShift = this.height - frame.getHeight() * worldScale;
				xRect = this.x - hitBoxSize/2;
				wRect = frame.getWidth() * worldScale + hitBoxSize;
				yRect = this.y - hitBoxDistance;
				hRect = hitBoxDistance;
				break;
			case DOWN:
				frame = this.attackDown.getCurrentFrame();
				xRect = this.x - hitBoxSize/2;
				wRect = frame.getWidth() * worldScale + hitBoxSize;
				yRect = this.y + this.height;
				hRect = hitBoxDistance;
				break;
			case LEFT:
				frame = this.attackLeft.getCurrentFrame();
				xShift = this.width - frame.getWidth() * worldScale;
				xRect = this.x - hitBoxDistance;
				wRect = hitBoxDistance;
				yRect = this.y - hitBoxSize / 2;
				hRect = frame.getHeight() * worldScale + hitBoxSize;
				break;
			case RIGHT:
				frame = this.attackRight.getCurrentFrame();
				xRect = this.x + this.width;
				wRect = hitBoxDistance;
				yRect = this.y - hitBoxSize / 2;
				hRect = frame.getHeight() * worldScale + hitBoxSize;
				break;
			default:
				frame = animation.getCurrentFrame();
			}

			this.hitBox = new Rectangle(xRect,yRect,wRect,hRect);
			g.drawImage(frame,x + xShift, y + yShift, frame.getWidth() * worldScale , frame.getHeight() * worldScale, null);
			if (ZeldaGameState.showHitboxes) { g.setColor(Color.YELLOW);g.drawRect(this.hitBox.x, this.hitBox.y, this.hitBox.width, this.hitBox.height);}

		}
		else if (attackingBow) {
			// For left/up purposes
			switch(this.direction) {	
			case UP:
				g.drawImage(animation.getCurrentFrame(),x , y, width, height  , null);
				break;
			case DOWN:
				g.drawImage(Images.LinkBow[2], x, y, width, height, null);
				break;
			case LEFT:
				g.drawImage(Images.LinkBow[1],  x, y, width, height, null);
				break;
			case RIGHT:
				g.drawImage(Images.LinkBow[0],  x, y, width, height, null);
				break;
			default:
				g.drawImage(Images.LinkBow[0],  x, y, width, height, null);
			}
			
			if (ZeldaGameState.showHitboxes) { g.setColor(Color.YELLOW);g.drawRect(this.hitBox.x, this.hitBox.y, this.hitBox.width, this.hitBox.height);}
			}
		else if (moving) {
			g.drawImage(animation.getCurrentFrame(),x , y, width , height  , null);
		} 
		else {
			if (movingMap){
				g.drawImage(animation.getCurrentFrame(),x , y, width, height  , null);
			}
			g.drawImage(sprite, x , y, width , height , null);
		}
	}

	@Override
	public void move(Direction direction) {
		moving = true;
		changeIntersectingBounds();
		//chack for collisions
		for (SolidStaticEntities objects : handler.getZeldaGameState().toTick) {


			if ((objects instanceof SectionDoor) && objects.bounds.intersects(bounds) && direction == ((SectionDoor) objects).direction) {
				if (!(objects instanceof DungeonDoor)) {

					/*********************************************************************************************************************
					 * 
					 * 										SECTION DOOR INTERACTION
					 * 
					 *********************************************************************************************************************/

					movingMap = true;
					movingTo = ((SectionDoor) objects).direction;
					switch (((SectionDoor) objects).direction) {
					case RIGHT:
						newMapX = -(((handler.getZeldaGameState().mapWidth) + 1) * worldScale);
						newMapY = 0;
						handler.getZeldaGameState().mapX++;
						xExtraCounter = getExtraCounter();
						break;
					case LEFT:
						newMapX = (((handler.getZeldaGameState().mapWidth) + 1) * worldScale);
						newMapY = 0;
						handler.getZeldaGameState().mapX--;
						xExtraCounter = getExtraCounter();
						break;
					case UP:
						newMapX = 0;
						newMapY = -(((handler.getZeldaGameState().mapHeight) + 1) * worldScale);
						handler.getZeldaGameState().mapY--;
						yExtraCounter = getExtraCounter();
						break;
					case DOWN:
						newMapX = 0;
						newMapY = (((handler.getZeldaGameState().mapHeight) + 1) * worldScale);
						handler.getZeldaGameState().mapY++;
						yExtraCounter = getExtraCounter();
						break;
					}
					return;
				}
				else {


					/*********************************************************************************************************************
					 * 
					 * 										DUNGEON DOOR ENTRANCES/EXITS
					 * 
					 *********************************************************************************************************************/

					if (handler.getState() instanceof Dungeon) {
						Dungeon dungeon = (Dungeon) handler.getState();
						if (((DungeonDoor) objects).name.equals("dungeon" + dungeon.getName() + "Exit")) {
							handler.getMusicHandler().changeMusic("overworld.wav");
							handler.changeState(handler.getZeldaGameState());
						}
						
						if (((DungeonDoor) objects).name.equals("movingUp")) {
							dungeon.moveUp();
						}
						if (((DungeonDoor) objects).name.equals("movingRight")) {
							dungeon.moveRight();
						}
						if (((DungeonDoor) objects).name.equals("movingLeft")) {
							dungeon.moveLeft();
						}
						if (((DungeonDoor) objects).name.equals("movingDown")) {
							dungeon.moveDown();
						}
						
						// Invulnerable after walking out of a door
						this.damageCooldown = 60;
						
						
					}
					
					else {
						// Enter cave
						if (((DungeonDoor) objects).name.equals("caveStartEnter")) {
							ZeldaGameState.inCave = true;
						}

						// Leave Cave
						if (((DungeonDoor) objects).name.equals("caveStartLeave")) {
							ZeldaGameState.inCave = false;
						}

						// Enter HeMan Cave
						if (((DungeonDoor) objects).name.equals("heManCaveEnter")) {
							ZeldaGameState.inHeManCave = true;    
	                        handler.getBossState().inEntrance = true;
	                        handler.getMusicHandler().changeMusic(handler.getBossState().ENTRANCE_MUSIC);
						}


						// Leave HeMan Cave
						if (((DungeonDoor) objects).name.equals("heManCaveLeave")) {
							ZeldaGameState.inHeManCave = false;
							handler.getMusicHandler().changeMusic("overworld.wav");
							handler.changeState(handler.getZeldaGameState());
						}

						// Enter cave
						if (((DungeonDoor) objects).name.equals("caveThreeEnter")) {
							ZeldaGameState.inPokemonCave = true;
							handler.getMusicHandler().changeMusic("pokemonLab.wav");
						}

						// Leave Cave
						if (((DungeonDoor) objects).name.equals("caveThreeLeave")) {
							ZeldaGameState.inPokemonCave = false;
							handler.getMusicHandler().changeMusic("overworld.wav");
						}

						if (((DungeonDoor) objects).name.equals("shopEnter")) {
							x = ((DungeonDoor) objects).nLX;
							y = ((DungeonDoor) objects).nLY;
							direction = ((DungeonDoor)objects).direction;
							handler.getStoreState().refresh();
							handler.getMusicHandler().changeMusic("shopTheme.wav");
							handler.changeState(handler.getStoreState());
						}
						
						if (((DungeonDoor) objects).name.equals("dungeon1Enter")) {
							handler.getMusicHandler().changeMusic(handler.getDungeon1().getSong());
							handler.changeState(handler.getDungeon1());
						}


						/*********************************************************************************************************************
						 * 
						 * 										DUNGEON DOOR INTERATCION
						 * 
						 *********************************************************************************************************************/


						if (((DungeonDoor) objects).name.equals("movingUp")) {
							handler.getBossState().moveUp();
						}
						if (((DungeonDoor) objects).name.equals("movingRight")) {
							handler.getBossState().moveRight();
						}
						if (((DungeonDoor) objects).name.equals("movingLeft")) {
							handler.getBossState().moveLeft();
						}
						if (((DungeonDoor) objects).name.equals("movingDown")) {
							handler.getBossState().moveDown();
						}

					}
					
					// Move Link to the X/Y coordinates specified by the door
					x = ((DungeonDoor) objects).nLX;
					y = ((DungeonDoor) objects).nLY;
					direction = ((DungeonDoor)objects).direction;

				}
			}

			/*********************************************************************************************************************
			 * 
			 * 										ITEM INTERACTION
			 * 
			 *********************************************************************************************************************/


			else if (objects.bounds.intersects(interactBounds)) {

				if (objects instanceof Item) {
					Item item = (Item) objects;
					// CAN'T AFFORD IT
					if ((item.getValue() < 0) && (Math.abs(item.getValue()) > this.rupees)) {return;}
					
					
					if (handler.getState() instanceof Dungeon && item.getName().contains("locked") && keys>0) {
						
						Dungeon dungeon = (Dungeon) handler.getState();
						
						if (item.getName().equals("lockedup")) {
							dungeon.getCurrentRoom().openTopDoor();
						}
						
						else if (item.getName().equals("lockedbottom")) {
							dungeon.getCurrentRoom().openBottomDoor();
						}
						
						else if (item.getName().equals("lockedleft")) {
							dungeon.getCurrentRoom().openLeftDoor();

						}
						
						else if (item.getName().equals("lockedright")) {
							dungeon.getCurrentRoom().openRightDoor();
						}
						

						keys--;
						
					}


					// HEARTS
					if (item.getName().equals("heart")) {
						handler.getZeldaGameState().pickUp(objects);
						handler.getMusicHandler().playEffect("heart.wav");
						this.addHealth(2);
					}

					// HEART CONTAINERS
					else if (item.getName().equals("heartcontainer")) {
						this.increaseMaxHealth();
						handler.getZeldaGameState().itemGet(objects);
					}

					// RUPEES
					else if (item.getName().equals("rupee")) {
						handler.getZeldaGameState().pickUp(objects);
						handler.getMusicHandler().playEffect("rupee.wav");
					}

					// MASTER SWORD
					else if (item.getName().equals("mastersword")) {
						this.hasFinalSword = true; this.hasSecondSword = false; this.hasSword = false;
						this.attackUp = new Animation(animSpeed, Images.linkAttackUpMaster);
						this.attackDown = new Animation(animSpeed, Images.linkAttackDownMaster);
						this.attackRight = new Animation(animSpeed, Images.linkAttackRightMaster);
						this.attackLeft = new Animation(animSpeed, Images.linkAttackLeftMaster);
						handler.getZeldaGameState().itemGet(objects);
					}

					// SECOND SWORD
					else if (item.getName().equals("secondsword")) {
						this.hasFinalSword = false; this.hasSecondSword = true; this.hasSword = false;
						this.attackUp = new Animation(animSpeed, Images.linkAttackUpSecond);
						this.attackDown = new Animation(animSpeed, Images.linkAttackDownSecond);
						this.attackRight = new Animation(animSpeed, Images.linkAttackRightSecond);
						this.attackLeft = new Animation(animSpeed, Images.linkAttackLeftSecond);
						handler.getZeldaGameState().itemGet(objects);
					}

					// SWORD
					else if (item.getName().equals("sword")) {
						this.hasFinalSword = false; this.hasSecondSword = false; this.hasSword = true;
						this.attackUp = new Animation(animSpeed, Images.linkAttackUp);
						this.attackDown = new Animation(animSpeed, Images.linkAttackDown);
						this.attackRight = new Animation(animSpeed, Images.linkAttackRight);
						this.attackLeft = new Animation(animSpeed, Images.linkAttackLeft);
						handler.getZeldaGameState().itemGet(objects);
					}

					else if (item.getName().equals("hemankey")) {
						this.hasKey = true;
						handler.getZeldaGameState().itemGet(objects);
					}
					
					else if(item.getName().contentEquals("arrow")) {
						this.arrows += 5;
						handler.getZeldaGameState().pickUp(objects);
					}
					
					else if(item.getName().contentEquals("potion")) {
						this.potions ++;
						handler.getMusicHandler().playEffect("heart.wav");
						handler.getZeldaGameState().pickUp(objects);
					}
					
					else if (item.getName().equals("key")) {
						this.keys++;
						handler.getZeldaGameState().itemGet(objects);
					}

					else if (item.getName().equals("topdoor") && this.hasKey) {
						int index = handler.getZeldaGameState().toTick.indexOf(item);
						SolidStaticEntities door = new DungeonDoor(item.x, item.y,item.width, item.height, Direction.UP,"movingUp" , handler, ZeldaGameState.itemXToOverworldX(item.x), ZeldaGameState.itemYToOverworldY(BossBattleState.bottomY) );
						door.sprite = Images.zeldaTiles.get(1);
						handler.getZeldaGameState().toTick.set(index, door);
                		handler.getBossState().keyUsed = true;
						pause(60);
					}


					/*********************************************************************************************************************
					 * 
					 * 							OTHERWISE, IF NOT AN ITEM, DO NOT MOVE
					 * 						*/	else if (!(objects instanceof SectionDoor)){return;}	/*
					 * 
					 *********************************************************************************************************************/

					this.rupees += item.getValue();
				}



				/*********************************************************************************************************************
				 * 
				 * 							OTHERWISE, IF NOT A SECTION DOOR, DO NOT MOVE
				 * 						*/	else if (!(objects instanceof SectionDoor)){return;}	/*
				 * 
				 *********************************************************************************************************************/



			}

		}

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

	//    private boolean noHeMan() {
	//		for (BaseMovingEntity e : handler.getZeldaGameState().cave2Entities) {
	//			if (e instanceof HeMan) {return false;}
	//		}
	//		return true;
	//	}

	private int getExtraCounter() {return ((8 * worldScale + (2 * worldScale)) / 2 );}

	@Override
	public void damage(int amount) {
		super.damage(amount);
		handler.getMusicHandler().playEffect("damage.wav");
		if (this.health > 0) {
			hit = true;
			damageCooldown = 30;
		}

		else {
			handler.getZeldaGameState().soundCooldown = 160;
		}

	}

	public void pause(int amount) {
		this.waitCooldown = amount;
	}

	public boolean isDead() {return this.dead || this.health <= 0;}
	public void setDead(boolean a) {this.dead = a;}


	@Override
	public void addHealth(int amount) {
		this.health += amount;

		if (this.health > this.maxHealth) {
			this.health = this.maxHealth;
		}
	}

	public void increaseMaxHealth() {
		this.maxHealth+=2;
		this.health = this.maxHealth;
	}

	public boolean canAttack() {
		return !(this.attackDown==null || this.attackUp==null || this.attackLeft==null || this.attackRight==null)  && canAttack;
	}

	private boolean projectileIsAlive() {
		for (BaseMovingEntity entity : handler.getZeldaGameState().entitiesToTick) {
			if (entity instanceof Projectile) {
				if (((Projectile) entity).getOwner() instanceof Link) {
					return true;
				}
			}
		}

		return false;
	}

	private void refreshDamageAnim() {
		BufferedImage[] temp = new BufferedImage[Images.linkDamage.length];
		temp[0] = Images.linkDamage[0];
		temp[1] = Images.linkDamagePhase2[new Random().nextInt(Images.linkDamagePhase2.length)];
		temp[2] = Images.linkDamage[2];
		temp[3] = Images.linkDamage[3];
		temp[4] = Images.linkDamage[4];
		this.damageAnim = new Animation(animSpeed / damageAnimSpeedMult, temp);
	}


}