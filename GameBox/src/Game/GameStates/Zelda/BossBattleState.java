package Game.GameStates.Zelda;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import Game.GameStates.State;
import Game.Zelda.Entities.Dynamic.BaseMovingEntity;
import Game.Zelda.Entities.Dynamic.Direction;
import Game.Zelda.Entities.Dynamic.Ghost;
import Game.Zelda.Entities.Dynamic.HeMan;
import Game.Zelda.Entities.Dynamic.Link;
import Game.Zelda.Entities.Statics.DungeonDoor;
import Game.Zelda.Entities.Statics.Item;
import Game.Zelda.Entities.Statics.SolidStaticEntities;
import Game.Zelda.World.Room;
import Main.Handler;
import Resources.Images;

public class BossBattleState extends State {
	
	// ALWAYS MAKE SURE THAT ROOM WIDTH AND ROOM HEIGHT ARE EVEN NUMBERS
	public static int leftX = 0, rightX = 16, topY = -1, bottomY = 9;
	public static int roomWidth = rightX - leftX, roomHeight = bottomY - topY;
	
	private int roomX = 0, roomY = 0;
	private int droningCooldown = 20;
	
	private HeMan boss;
	private Link link;
	private ArrayList<BaseMovingEntity> enemies;
	private ArrayList<SolidStaticEntities> blocks;
	private ZeldaGameState zeldaState;
	private BufferedImage leftDoor = Images.zeldaTiles.get(5);
	private BufferedImage rightDoor = Images.zeldaTiles.get(11);
	private BufferedImage topDoor = Images.zeldaTiles.get(1);
	private BufferedImage bottomDoor = Images.zeldaTiles.get(16);
	private BufferedImage leftWall = Images.zeldaTiles.get(6);
	private BufferedImage rightWall = Images.zeldaTiles.get(10);
	private BufferedImage topWall = Images.zeldaTiles.get(0);
	private BufferedImage bottomWall = Images.zeldaTiles.get(15);
	private BufferedImage cornerTile = Images.zeldaTiles.get(26);
	
	public static String[] themes = {"megalosuck.wav", "final.wav"};
	public int bossTheme = 1;
	
	public final String ENTRANCE_MUSIC = "wind.wav";
	public final String DUNGEON_MUSIC = "dungeon.wav";
	public final String BOSS_MUSIC = "hemantheme.wav";
	public final String FINAL_MUSIC = themes[bossTheme];
	public boolean inEntrance = false, keyUsed = false;
	
	private boolean movingRoom = false, defeated = false;
	public boolean inHeManRoom = false;
	private boolean transitionToSpace;
	private int doorLocation = 0;
	
	// VARIABLES FOR WHEN LINK ENTERS THE ROOM
	private int pause1Duration = 2, pause2Duration = 3, pause3Duration = 6, soundCooldown;
	private int phase1Wait, phase2Wait, phase3Wait=-1;
	private int songDrop = bossTheme==0 ? 13*60 + 15 : bossTheme==1 ?  5*60 : 0;
	private int defeatCD = 10* 60;


	public BossBattleState(Handler handler) {
		super(handler);
		refresh();
	}

	@Override
	public void tick() {
		// Refresh Room
		if (movingRoom) {loadRoom();movingRoom = false;}
		
		if (inHeManRoom && boss!=null) {
			
			// BEFORE BATTLE (Maybe play an animation here?)
			if (phase1Wait > 0) {phase1Wait--; boss.tick(); return;}
			else if (phase1Wait == 0) {handler.getMusicHandler().playEffect("heman.wav"); soundCooldown = 4 * 60; phase1Wait = -1;}
			else if (phase1Wait == -1 && soundCooldown >= 0) {
				soundCooldown--;
				boss.tick();
				if (soundCooldown==2*60) {handler.getMusicHandler().changeMusic(BOSS_MUSIC);}
				if (soundCooldown == 0) {boss.moving = true;}
				return;
			}
			
			// BEGIN BATTLE
			
			// SECOND PHASE COMMENCE
			if (boss.health < boss.maxHealth/2 && boss.getPhase() != 2) {
				boss.setCanHurtLink(false);
				boss.setInvulnerable(true);
				boss.moving = false;
				boss.setPhase(5);
				
				// Small Delay
				if (phase2Wait > 0) { handler.getMusicHandler().stopMusic(); phase2Wait--; boss.tick(); return;}
				
				// Start to move to center
				else if (phase2Wait == 0) {
					boss.moving = true;
				
					// Once he is in position, do the rest
					if (boss.isPositioned()) {
						
						boss.tick();
						
						// Play his clip
						if(phase3Wait == -1) { phase3Wait = pause3Duration * 60; 							
						boss.playAnimation = true; handler.getMusicHandler().playEffect("hemanattack.wav");}
				 	
						// Midway
						if(phase3Wait == (int) (pause3Duration/2) * 60) {if (bossTheme != 1) handler.getMusicHandler().changeMusic(FINAL_MUSIC);}
						if(phase3Wait > 0) {phase3Wait--; return;}
						else {
							
							if (songDrop>0) {songDrop--; }
							else {
								boss.playAnimation = false;
								boss.setPhase(2);
								boss.setHealth(150);
								transitionToSpace = true;
								
								if (bossTheme==1) {
									handler.getMusicHandler().changeMusic(FINAL_MUSIC);
								}
								
								// Remove Door
								for (SolidStaticEntities block: blocks) {
									if (block instanceof DungeonDoor) {
										this.doorLocation = blocks.indexOf(block);
										blocks.set(doorLocation, new SolidStaticEntities(block.x,block.y,this.bottomWall, handler));
									}
								}
							}
						}
					}
				}			
			}	
			
			if (boss.health <= 0) {
				
				if (!defeated) {
					defeated = true;
					handler.getMusicHandler().stopMusic();
					zeldaState.bossDefeated = true;
					transitionToSpace = false;
					enemies.clear();
					enemies.add(boss);
				}
				
				
				if (defeatCD == -30) {
					enemies.clear();
					handler.getMusicHandler().playEffect("explosion.wav");
				}
				
				else if (defeatCD == 30){
					handler.getMusicHandler().playEffect("explosion.wav");
				}
				
				else if (defeatCD == 60) {
					handler.getMusicHandler().playEffect("explosion.wav");
				}
				
				else if (defeatCD == 90) {
						handler.getMusicHandler().playEffect("explosion.wav");
				}
				
				else if (defeatCD == -240) {
					handler.getCreditsState().refresh();
		        	handler.getMusicHandler().changeMusic("intro.wav");
					handler.changeState(handler.getCreditsState());
				}
				
				
				if (defeatCD % 5 == 0) {
					boss.x = new Random().nextBoolean() ? boss.x + 1 : boss.x - 1;
					boss.y = new Random().nextBoolean() ? boss.y + 1 : boss.y - 1;
				}
				
				defeatCD--;
				
				
				// TODO: Have an animation when He-Man dies

			}
			
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_6)) {
			this.roomX = 3;
			this.roomY = 2;
			this.loadRoom();
		}
		
		if (ghostsOnMap()) {
	        if (droningCooldown <= 0) {
	            handler.getMusicHandler().playEffect("pacman_droning.wav");
	            droningCooldown = 20;
	        } 
	        else {droningCooldown--;}
		}
		
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		zeldaState.tick();
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (transitionToSpace) {
			g.drawImage(Images.space, 0, 0, handler.getWidth(), handler.getHeight(), null);
		}
			
		// Drawing Floor
		for (int x = leftX; x <= rightX+1; x++) {
			for (int y = topY; y <= bottomY+1; y++) {
				int x1 = (x * ZeldaGameState.stageWidth/16) + ZeldaGameState.xOffset;
				int y1 = (y * ZeldaGameState.stageHeight/11) + ZeldaGameState.yOffset;
			
				BufferedImage floor = Images.zeldaTiles.get(26);
				g.drawImage(floor, x1, y1, (floor.getWidth()+2) * ZeldaGameState.worldScale, (floor.getHeight()+1) * ZeldaGameState.worldScale, null);
			}
		}
		
		
		// BACKGROUND
		switch(roomX) {
		case 0:
			switch(roomY) {
			case 0: // [0,0]
				break;
			case 1: // [0,1]
				break;
			case 2: // [0,2]
				break;
			case 3: // [0,3]
				break;
			case 4: // [0,4]
				break;
			default:
				break;
			}
			break;

		case 1: 
			switch(roomY) {
			case 0: // [1,0]
				break;
			case 1: // [1,1]
				break;
			case 2: // [1,2]
				break;
			case 3: // [1,3]
				break;
			case 4: // [1,4]
				break;
			default:
				break;
			}
			break;
			
		case 2:
			switch(roomY) {
			case 0: // [2,0]
				break;
			case 1: // [2,1]
				break;
			case 2: // [2,2]
				break;
			case 3: // [2,3]
				break;
			case 4: // [2,4]
				break;
			default:
				break;
			}
			break;
			
		case 3:
			switch(roomY) {
			case 0: // [3,0]
				break;
			case 1: // [3,1]
				break;
			case 2: // [3,2]
				break;
			case 3: // [3,3]
				break;
			case 4: // [3,4]
				break;
			default:
				break;
			}
			break;
			
		case 4:
			switch(roomY) {
			case 0: // [4,0]
				break;
			case 1: // [4,1]
				break;
			case 2: // [4,2]
				break;
			case 3: // [4,3]
				break;
			case 4: // [4,4]
				break;
			default:
				break;
			}
			break;
			
		case 5:
			switch(roomY) {
			case 0: // [5,0]
				break;
			case 1: // [5,1]
				break;
			case 2: // [5,2]
				break;
			case 3: // [5,3]
				break;
			case 4: // [5,4]
				break;
			default:
				break;
			}
			break;
			
		case 6:
			switch(roomY) {
			case 0: // [6,0]
				break;
			case 1: // [6,1]
				break;
			case 2: // [6,2]
				break;
			case 3: // [6,3]
				break;
			case 4: // [6,4]
				break;
			default:
				break;
			}
			break;
			
		default:
			break;
		}
		
		zeldaState.render(g);
//		g.drawString("X: " + roomX, 100, 100);
//		g.drawString("Y: " + roomY, 100, 150);
		
	}

	
	public void moveLeft() {this.roomX--; movingRoom = true;}
	public void moveRight(){this.roomX++; movingRoom = true;}
	public void moveDown() {this.roomY--; movingRoom = true;}
	public void moveUp() {this.roomY++; movingRoom = true;}

	@Override
	public void refresh() {
		zeldaState = handler.getZeldaGameState();
		enemies = zeldaState.cave2Entities;
		blocks = zeldaState.cave2Objects;
		defeatCD = 10* 60;
		enemies.clear();
		blocks.clear();
		this.roomX = 0;
		this.roomY = 0;
		loadRoom();
		link = zeldaState.link;
		keyUsed = false;
	}
	
	
	public void loadRoom() {
		enemies.clear();
		blocks.clear();
		if (inHeManRoom) {
			this.inHeManRoom = false;
			this.transitionToSpace = false;
			handler.getMusicHandler().changeMusic(DUNGEON_MUSIC);
		}
		
		else if (handler.getState()!=null && handler.getState().equals(this) && roomX == 0 && roomY == 0) {
			handler.getMusicHandler().changeMusic(ENTRANCE_MUSIC);
			inEntrance = true;
		}
		
		// Leaving Entrance
		else if (inEntrance) {
			handler.getMusicHandler().changeMusic(DUNGEON_MUSIC);
			inEntrance = false;
		}

		
		
		/*********************************************************************************************************************
    	 * 
    	 * 												CREATING ROOMS  
    	 * 
    	 *  					TO ADD AN ENEMY TO A ROOM: enemies.add(new whateveryouwant();
    	 *  					TO ADD A BLOCK TO A ROOM: blocks.add(new whateveryouwant();
    	 * 
    	 *********************************************************************************************************************/
		
		switch(this.roomX) {
		case 0:
			switch(this.roomY) {
			case 0: // ENTRANCE (0,0)
			
				// Top Wall / Bottom Wall
				for (int x = leftX ; x < rightX; x+=2) {
					if (!(roomWidth%2==0 && x == (int) (roomWidth/2)) && !(roomWidth%2==1 && (x == (int) (roomWidth/2) -1  || x == (int) (roomWidth/2)))) {
						blocks.add(new SolidStaticEntities(x,topY,Images.zeldaTiles.get(0), handler));
						blocks.add(new SolidStaticEntities(x,bottomY,Images.zeldaTiles.get(0), handler));
					}
					else {
						// Closed Door
						if (!keyUsed) {
							blocks.add(new Item(x,topY, "topdoor",Images.zeldaTiles.get(2), handler));
							keyUsed = true;
						}
						
						else {
							DungeonDoor door = new DungeonDoor(x,topY,16*ZeldaGameState.worldScale*2,16*ZeldaGameState.worldScale * 2, Direction.UP,"movingUp" , handler, ZeldaGameState.itemXToOverworldX(x), ZeldaGameState.itemYToOverworldY(bottomY-2));
							door.sprite = this.topDoor;
							blocks.add(door);
						}
						
						blocks.add(new DungeonDoor(x,bottomY,16*ZeldaGameState.worldScale*2,16*ZeldaGameState.worldScale * 2,Direction.DOWN,"heManCaveLeave",handler,(4 * (ZeldaGameState.stageWidth/10)) + ZeldaGameState.xOffset,(2 * (ZeldaGameState.stageHeight/11)) + ZeldaGameState.yOffset));

					}

				}

				// Left Wall / Right Wall
				for (int y = topY ; y <= bottomY; y+=2) {
					blocks.add(new SolidStaticEntities(leftX,y,Images.zeldaTiles.get(0), handler));
					blocks.add(new SolidStaticEntities(rightX,y,Images.zeldaTiles.get(0), handler));
				}
				
				break;
				
			case 1: // (0,1)
				createRoom(false,true,false,true);
				addEnemyToRoom(6, 3, zeldaState.createTektite());
				addEnemyToRoom(2, 6, zeldaState.createTektite());
				addEnemyToRoom(1, 2, zeldaState.createSuperTektite());
				break;
				
			case 2: // (0,2)
				createRoom(true,false,false,true);
				addEnemyToRoom(6,6,zeldaState.createSnake());
				break;
				
			case 3: // (0,3)
				createRoom(false,true,false,true);
				break;
				
			case 4: // (0,4)
				createRoom(false,false,false,true);
				break;
				
			default:
				if (roomY<0) {createRoom(true,false,false,false);}
				if (roomY>0) {createRoom(false,true,false,false);}

			}
			
			break;
			
		case 1:
			switch(roomY) {
			case 0: // (1,0)
				createRoom(true,false,false,false);
				break;
				
			case 1: // (1,1)
				createRoom(true,true,true,true);
				addEnemyToRoom(6, 6, zeldaState.createBlinky());
				addEnemyToRoom(6, 6, zeldaState.createInky());
				addEnemyToRoom(6, 6, zeldaState.createPinky());
				addEnemyToRoom(6, 6, zeldaState.createClyde());
				break;
				
			case 2: // (1,2)
				createRoom(false,true,true,false);
				break;
				
			case 3: // (1,3)
				createRoom(false,false,true,false);
				break;
				
			case 4: // (1,4)
				createRoom(false,false,true,true);
				break;
				
			default:
				if (roomY<0) {createRoom(true,false,false,false);}
				if (roomY>0) {createRoom(false,true,false,false);}
			}
			break;
			
		case 2:
			switch(roomY) {
			case 0: // (2,0)
				createRoom(true,false,false,true);
				break;
				
			case 1:// (2,1)
				createRoom(true,true,true,false);
				break;
				
			case 2:// (2,2)
				createRoom(true,true,false,false);
				break;
				
			case 3: // (2,3)
				createRoom(true,true,false,false);
				break;
				
			case 4: // (2,4)
				createRoom(false,true,true,false);
				break;
				
			default:
				if (roomY<0) {createRoom(true,false,false,false);}
				if (roomY>0) {createRoom(false,true,false,false);}
			}
			break;
			
		case 3:
			switch(roomY) {
			case 0: // (3,0)
				createRoom(true,false,true,true);
				break;
				
			case 1: // (3,1)
				createRoom(false,true,false,true);
				break;
				
			case 2: // (3,2)
				createRoom(true,false,false,true);
				break;
				
			case 3: // (3,3)
				createRoom(false,true,false,false);
				this.boss = zeldaState.createHeMan();
				addEnemyToRoom(leftX + roomWidth/2,topY + 2,this.boss);
				inHeManRoom = true;
				handler.getMusicHandler().stopMusic();
				phase1Wait = this.pause1Duration*60; phase2Wait = this.pause2Duration*60;
				phase3Wait=-1;
				songDrop = bossTheme==0 ? 13*60 + 15 : bossTheme==1 ?  0 : 0;	
				break;
			case 4: // (3,4)
				createRoom(false,false,false,true);
				break;
				
			default:
				if (roomY<0) {createRoom(true,false,false,false);}
				if (roomY>0) {createRoom(false,true,false,false);}
			}
			break;
			
		case 4:
			switch(roomY) {
			case 0: // (4,0)
				createRoom(false,false,true,true);
				break;
				
			case 1: // (4,1)
				createRoom(true,false,true,false);
				break;
				
			case 2: // (4,2)
				createRoom(true,true,true,false);
				break;
				
			case 3: // (4,3)
				createRoom(true,true,false,false);
				break;
				
			case 4: // (4,4)
				createRoom(false,true,true,true);
				break;
				
			default:
				if (roomY<0) {createRoom(true,false,false,false);}
				if (roomY>0) {createRoom(false,true,false,false);}
			}
			break;
			
		case 5:
			switch(roomY) {
			case 0: // (5,0)
				createRoom(true,false,true,false);
				break;
				
			case 1: // (5,1)
				createRoom(false,true,false,true);
				break;
				
			case 2: // (5,2)
				createRoom(false,false,false,true);
				break;
				
			case 3: // (5,3)
				createRoom(true,false,false,true);
				break;
				
			case 4: // (5,4)
				createRoom(false,true,true,false);
				break;
				
			default:
				if (roomY<0) {createRoom(true,false,false,false);}
				if (roomY>0) {createRoom(false,true,false,false);}
			}
			break;
			
		case 6:
			switch(roomY) {
			case 0: // (6,0)
				createRoom(true,false,false,false);
				break;
				
			case 1: // (6,1)
				createRoom(true,true,true,false);
				break;
				
			case 2: // (6,2)
				createRoom(false,true,true,false);
				break;
				
			case 3: // (6,3)
				createRoom(true,false,true,false);
				break;
				
			case 4: // (6,4)
				createRoom(false,true,false,false);
				break;
				
			default:
				if (roomY<0) {createRoom(true,false,false,false);}
				if (roomY>0) {createRoom(false,true,false,false);}
			}
			break;
			
		default:
			if (roomX < 0) {createRoom(false,false,false,true);}
			if (roomX > 0) {createRoom(false,false,true,false);}
		}
	}
	
	// LEFT:6 RIGHT:10 UP:0 DOWN:15
	// LEFTD: 5 RIGHTD: 11 UPD: 1 DOWND 16:
	
	private void addEnemyToRoom(int x, int y, BaseMovingEntity entity) {
		if (x < leftX || x > rightX) {x = x < leftX ? (leftX + 2) : (leftX - 2);}
		if (y < topY || y > bottomY) {y = y < topY ? (topY + 2) : (bottomY - 2);}
		
		entity.x = ZeldaGameState.itemXToOverworldX(x);
		entity.y = ZeldaGameState.itemYToOverworldY(y);
		
		this.enemies.add(entity);
	}
	
	public boolean ghostsOnMap() {
		for (BaseMovingEntity enem : enemies) {
			if (enem instanceof Ghost) {
				return true;
			}
		}
		
		return false;
	}
	
	private void createRoom(boolean up, boolean down, boolean left, boolean right) {
		// Top Wall / Bottom Wall
		for (int x = leftX, xBlock = 0 ; x <= rightX && xBlock <= roomWidth; x+=2, xBlock+=2) {
			boolean bottomDoor = false; boolean topDoor = false;
			if (up) {
				if ((xBlock == (int) (roomWidth/2) -1  || xBlock == (int) (roomWidth/2))) {
					DungeonDoor door = new DungeonDoor(x,topY,16*ZeldaGameState.worldScale*2,16*ZeldaGameState.worldScale * 2, Direction.UP,"movingUp" , handler, ZeldaGameState.itemXToOverworldX(x), ZeldaGameState.itemYToOverworldY(bottomY-2));
					door.sprite = this.topDoor;
					blocks.add(door);
					topDoor = true;
				}
			}
			
			if (down) {
				if ((xBlock == (int) (roomWidth/2) -1  || xBlock == (int) (roomWidth/2))) {
					DungeonDoor door = new DungeonDoor(x,bottomY,16*ZeldaGameState.worldScale*2,16*ZeldaGameState.worldScale * 2, Direction.DOWN,"movingDown" , handler, ZeldaGameState.itemXToOverworldX(x), ZeldaGameState.itemYToOverworldY(topY+2));
					door.sprite = this.bottomDoor;
					blocks.add(door);
					bottomDoor = true;
				}
			}
			
			if (!topDoor) {blocks.add(new SolidStaticEntities(x,topY,this.topWall, handler));}
			if (!bottomDoor) {blocks.add(new SolidStaticEntities(x,bottomY,this.bottomWall, handler));}
		}

		// Left Wall / Right Wall
		for (int y = topY, yBlock = 0 ; y <= bottomY && yBlock <= roomHeight; y+=2, yBlock+=2) {
			
			// DRAWING CORNERS			
			if (y == bottomY || y == topY) {
				blocks.add(new SolidStaticEntities(leftX,y,this.cornerTile, handler));
				blocks.add(new SolidStaticEntities(rightX,y,this.cornerTile, handler));
				blocks.add(new SolidStaticEntities(leftX,y+1,this.cornerTile, handler));
				blocks.add(new SolidStaticEntities(rightX,y+1,this.cornerTile, handler));
				blocks.add(new SolidStaticEntities(leftX+1,y,this.cornerTile, handler));
				blocks.add(new SolidStaticEntities(rightX+1,y,this.cornerTile, handler));
				blocks.add(new SolidStaticEntities(leftX+1,y+1,this.cornerTile, handler));
				blocks.add(new SolidStaticEntities(rightX+1,y+1,this.cornerTile, handler));
				continue;
			}
			boolean rightDoor = false; boolean leftDoor = false;
						
			if (left) {
				if ((yBlock == (int) (roomHeight/2) -1  || yBlock == (int) (roomHeight/2))) {
					DungeonDoor door = new DungeonDoor(leftX,y,16*ZeldaGameState.worldScale*2,16*ZeldaGameState.worldScale * 2, Direction.LEFT,"movingLeft" , handler, ZeldaGameState.itemXToOverworldX(rightX-2), ZeldaGameState.itemYToOverworldY(y));
					door.sprite = this.leftDoor;
					blocks.add(door);					
					leftDoor = true;
				}
			}
			
			if (right) {
				if ((yBlock == (int) (roomHeight/2) -1  || yBlock == (int) (roomHeight/2))) {
					DungeonDoor door = new DungeonDoor(rightX,y,16*ZeldaGameState.worldScale*2,16*ZeldaGameState.worldScale * 2, Direction.RIGHT,"movingRight" , handler, ZeldaGameState.itemXToOverworldX(leftX+2), ZeldaGameState.itemYToOverworldY(y));
					door.sprite = this.rightDoor;
					blocks.add(door);
					rightDoor = true;
				}
			}
			
			if (!leftDoor) {blocks.add(new SolidStaticEntities(leftX,y,this.leftWall, handler));}
			if (!rightDoor) {blocks.add(new SolidStaticEntities(rightX,y,this.rightWall, handler));}
		}

		
	}


}
