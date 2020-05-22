package Game.GameStates.Zelda;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import Game.GameStates.State;
import Game.Zelda.Entities.Dynamic.*;
import Game.Zelda.Entities.Statics.*;
import Game.Zelda.World.*;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

/**
 * Created by AlexVR on 3/14/2020
 */
public class ZeldaGameState extends State {


	public static int xOffset,yOffset,stageWidth,stageHeight,worldScale;
	public static boolean bronze,silver,gold;
	public int cameraOffsetX,cameraOffsetY;
	//map is 16 by 7 squares, you start at x=7,y=7 starts counting at 0
	public int mapX,mapY,mapWidth,mapHeight;

	public ArrayList<ArrayList<ArrayList<SolidStaticEntities>>> objects;
	public ArrayList<ArrayList<ArrayList<BaseMovingEntity>>> enemies;
	public Link link;
	public static boolean inCave = false, inHeManCave = false, inPokemonCave = false, usedDEBUG = false;
	public ArrayList<SolidStaticEntities> caveObjects;
	public ArrayList<BaseMovingEntity> caveEntities;
	public ArrayList<SolidStaticEntities> cave2Objects;
	public ArrayList<BaseMovingEntity> cave2Entities;
	public ArrayList<SolidStaticEntities> cave3Objects;
	public ArrayList<BaseMovingEntity> cave3Entities;

	public ArrayList<SolidStaticEntities> toRemove = new ArrayList<SolidStaticEntities>();
	public ArrayList<BaseMovingEntity> toRemove2 = new ArrayList<BaseMovingEntity>();

	public ArrayList<SolidStaticEntities> toTick = new ArrayList<>();
	public ArrayList<BaseMovingEntity> entitiesToTick = new ArrayList<>();

	public ArrayList<BaseMovingEntity> entitiesToAdd = new ArrayList<>();

	public int soundCooldown = 0, beepCooldown=5;
	public boolean itemObtained = false;
	public BufferedImage itemToDraw = null;
	public Animation rupeeAnim, oak;
	public static boolean showHitboxes = false;
	public boolean bossDefeated = false;

	public boolean healing = false;
	public int healingCooldown = 5;
	public static int timesDied = 0, rupeesSpent = 0, potionsUsed = 0;


	public ZeldaGameState(Handler handler) {
		super(handler);
		refresh();

	}



	@Override
	public void tick() {
		addExtraEntities();
		if (Handler.DEBUG) {
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_NUMPAD2) && objects.get(mapX).size() > mapY+1) {mapY++; cameraOffsetY+=177 * worldScale;}
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_NUMPAD8) && 0 <= mapY-1) {mapY--; cameraOffsetY-=177 * worldScale;}
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_NUMPAD4) && 0 <= mapX-1) {mapX--; cameraOffsetX-=257 * worldScale;}
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_NUMPAD6) && objects.size() > mapX+1) {mapX++; cameraOffsetX+=257 * worldScale;}

			//Add Health
			if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_H) && link.maxHealth > link.health) {
				link.addHealth(1);
				ZeldaGameState.usedDEBUG = true;
				if(link.isDead()) {
					link.setDead(false);
				}
			}

			// Increase Max Health
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_1)) {link.increaseMaxHealth(); ZeldaGameState.usedDEBUG = true;}

			// Show hitboxes
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_T)) {showHitboxes = !showHitboxes;}

			// Fireball
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_5)) {summonFireball(); ZeldaGameState.usedDEBUG = true;}
			
			// Teleport to Burgerpants
			if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_0)) {
				mapX = 5;
				mapY = 7;
				cameraOffsetX =  ((mapWidth*mapX) + mapX + 1)*ZeldaGameState.worldScale;
				cameraOffsetY = ((mapHeight*mapY) + mapY + 1)*ZeldaGameState.worldScale;
				link.setDirection(Direction.UP);
				link.x = 2 * (ZeldaGameState.stageWidth/16) + ZeldaGameState.xOffset;
				link.y = (ZeldaGameState.stageHeight/11) + ZeldaGameState.yOffset;
				ZeldaGameState.usedDEBUG = true;
			}
		}


		if (link.isDead()) {
			link.tick();
			if (soundCooldown == 160) {
				link.deathAnim.reset();
				handler.getMusicHandler().stopMusic();

				// TODO: MAKE A SOUND FOR WHEN YOU LOST AGAINST HE MAN
				if (handler.getState().equals(handler.getBossState()) && handler.getBossState().ghostsOnMap()) {
					handler.getMusicHandler().playEffect("pacman_death.wav");
				}

				else {
					handler.getMusicHandler().playEffect("gameover.wav");
				}

				soundCooldown--;
			}
			else{soundCooldown--;}


			if (soundCooldown == 0) {
				timesDied++;
				handler.getMusicHandler().startMusic("lose.wav");
				handler.getGameOverState().refresh();
				handler.changeState(handler.getGameOverState());
			}

			return;
		}

		if (healing) {
			if (link.health == link.maxHealth) {healing = false;}

			else {
				if (healingCooldown <= 0) {
					handler.getMusicHandler().playEffect("healing.wav");
					link.health++;
					healingCooldown = 5;
				}
				else {healingCooldown--;}
			}

			return;
		}

		// Use Potion (H with DEBUG Off, G with DEBUG on)
		if(((handler.getKeyManager().keyJustPressed(KeyEvent.VK_H) && !Handler.DEBUG) || (handler.getKeyManager().keyJustPressed(KeyEvent.VK_G) && Handler.DEBUG))
				&& link.maxHealth > link.health && link.potions > 0) {
			healing = true;
			potionsUsed++;
			link.potions--;
		}
		
		if (beepCooldown <= 0 && (link.health <=2) && !this.itemObtained && !link.movingMap) {
			handler.getMusicHandler().playEffect("lowHealth.wav");
			beepCooldown = 17;
		}
		else if (link.health <=2) {beepCooldown--;}

		// For Item Purposes
		if (soundCooldown > 0) {soundCooldown--; return;}
		else if(soundCooldown == 0) {soundCooldown--; if (!handler.isMute()) {handler.getMusicHandler().resumeMusic();} itemObtained = false; this.itemToDraw=null;}



		/*********************************************************************************************************************
		 * 
		 * 												DUNGEON TICKING
		 * 
		 *********************************************************************************************************************/


		if (handler.getState() instanceof Dungeon) {
			Dungeon dungeon = (Dungeon) handler.getState();
			Room currentRoom = dungeon.getCurrentRoom();
			toTick = currentRoom.getBlocks();
			entitiesToTick = currentRoom.getEnemies();
		}

		/*********************************************************************************************************************
		 * 
		 * 										BLOCKS/ENTITIES TO TICK LISTS GO HERE
		 * 
		 *********************************************************************************************************************/

		else if (handler.getState().equals(this)) {
			if (inCave){
				toTick = caveObjects;
				entitiesToTick = caveEntities;
			}

			else if (inHeManCave) {
				toTick = cave2Objects;
				entitiesToTick = cave2Entities;
				handler.changeState(handler.getBossState());
			}

			else if (inPokemonCave) {
				toTick = cave3Objects;
				entitiesToTick = cave3Entities;
			}

			else {
				toTick = objects.get(mapX).get(mapY);
				entitiesToTick = enemies.get(mapX).get(mapY);
			}	
		}

		/*********************************************************************************************************************
		 * 
		 * 										INTERACTING WITH LINK 
		 * 
		 *********************************************************************************************************************/

		link.tick();
		if (!link.movingMap) {
			for (SolidStaticEntities entity : toTick) {
				entity.tick();
			}
			for (BaseMovingEntity entity : entitiesToTick) {
				entity.tick();
				if (entity.getInteractBounds().intersects(link.getInteractBounds()) && !link.isInvulnerable() && !link.isDead() && !(entity instanceof Projectile)){
					if(link.getHealth()>0) {

						if (entity instanceof HeMan) {
							if (!((HeMan) entity).canHurtLink() || ((HeMan)entity).isStaggering()) {return;}
						}

						else if (entity instanceof Enemy && !((Enemy)entity).canHurtLink) {
							return;
						}

						link.damage(entity.power);
						handler.getMusicHandler().playEffect("link_hit.wav");
					}
				}
				if(entity.health<=0 && !(entity instanceof HeMan)) {toRemove2.add(entity);}
			}

		}

		/*********************************************************************************************************************
		 * 
		 * 										REMOVING ENTITIES/DROPPING ITEMS
		 * 											*/ clearEntities(); /*
		 * 
		 *********************************************************************************************************************/


		 /*********************************************************************************************************************
		  * 
		  * 											  REMOVING ITEMS
		  * 											*/ clearItems(); /*
		  * 
		  *********************************************************************************************************************/

	}


	@Override
	public void render(Graphics g) {

		if (handler.getState() instanceof Dungeon) {
			// IF LINK IS IN A DUNGEON
		}

		else if (inCave){

			g.setColor(Color.WHITE);
			g.setFont(new Font("Arcade Classic Regular", Font.BOLD, 24));
			if (!link.canAttack()) {
				g.drawString("  IT ' S  DANGEROUS  TO  GO",(3 * (ZeldaGameState.stageWidth/16)) + ZeldaGameState.xOffset,(2 * (ZeldaGameState.stageHeight/12)) + ZeldaGameState.yOffset+ ((16*worldScale)));
				g.drawString("  ALONE !   TAKE  THIS.",(4 * (ZeldaGameState.stageWidth/16)) + ZeldaGameState.xOffset,(4 * (ZeldaGameState.stageHeight/11)) + ZeldaGameState.yOffset- ((16*worldScale)/2));
			}
			g.drawImage(Images.oldMan, (3 * (ZeldaGameState.stageWidth/6))  - (ZeldaGameState.stageWidth/ 26)+ ZeldaGameState.xOffset, (2 * (ZeldaGameState.stageHeight/6)) + ZeldaGameState.yOffset+ ((16*worldScale)), 32, 32, null);
			g.drawImage(Images.zeldaFire,(3 * (ZeldaGameState.stageWidth/9))  - (ZeldaGameState.stageWidth/ 26)+ ZeldaGameState.xOffset, (2 * (ZeldaGameState.stageHeight/6)) + ZeldaGameState.yOffset+ ((16*worldScale)), 32, 32, null);
			g.drawImage(Images.zeldaFire, (3 * (ZeldaGameState.stageWidth/4))  - (ZeldaGameState.stageWidth/ 10)+ ZeldaGameState.xOffset, (2 * (ZeldaGameState.stageHeight/6)) + ZeldaGameState.yOffset+ ((16*worldScale)), 32, 32, null);

		}

		else if (inHeManCave) {

		}

		else if (inPokemonCave) {
			g.drawImage(Images.oakRoom, xOffset, yOffset, this.mapWidth * worldScale, this.mapHeight * worldScale, null);
			g.drawImage(oak.getCurrentFrame(), (4 * (ZeldaGameState.stageWidth/6))  - (ZeldaGameState.stageWidth/ 100)+ ZeldaGameState.xOffset, ((ZeldaGameState.stageHeight/10)) + ZeldaGameState.yOffset+ ((16*worldScale)), oak.getCurrentFrame().getWidth() * worldScale, oak.getCurrentFrame().getHeight() * worldScale, null);
			oak.tick();
			if (!this.linkChoseAnItem()) {
				g.setColor(Color.WHITE);
				g.setFont(new Font("Arcade Classic Regular", Font.BOLD, 24));
				g.drawString("CHOOSE WISELY...",(4 * (ZeldaGameState.stageWidth/16)) + ZeldaGameState.xOffset, 100);

			}

			else {
				for (SolidStaticEntities item : cave3Objects) {
					if (item instanceof Item && (((Item)item).getName().equals("heartcontainer") || ((Item)item).getName().equals("secondsword"))) {
						toRemove.add(item);
					}
				}
				clearItems();
			}



		}

		else {
			g.drawImage(Images.zeldaMap, -cameraOffsetX + xOffset, -cameraOffsetY + yOffset, Images.zeldaMap.getWidth() * worldScale, Images.zeldaMap.getHeight() * worldScale, null);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, xOffset, handler.getHeight());
			g.fillRect(xOffset + stageWidth, 0, handler.getWidth(), handler.getHeight());
			g.fillRect(0, 0, handler.getWidth(), yOffset);
			g.fillRect(0, yOffset + stageHeight, handler.getWidth(), handler.getHeight());
		}


		if (!link.movingMap) {
			for (SolidStaticEntities entity : toTick) {
				entity.render(g);
				if (showHitboxes) {g.drawRect(entity.bounds.x, entity.bounds.y, entity.bounds.width, entity.bounds.height);}
			}

			for (BaseMovingEntity entity : entitiesToTick) {
				entity.render(g);
				if (showHitboxes) {g.drawRect(entity.bounds.x, entity.bounds.y, entity.bounds.width, entity.bounds.height);}
			}

			if (showHitboxes) {g.drawRect(link.bounds.x, link.bounds.y, link.bounds.width, link.bounds.height);}
		}




		link.render(g);


		/*********************************************************************************************************************
		 * 
		 * 											DRAWING ITEMS
		 * 
		 *********************************************************************************************************************/	
		g.setColor(Color.WHITE);	
		g.setFont(new Font("Arcade Classic Regular", Font.BOLD, 24));

		// HEALTH POTIONS
		g.drawImage(Images.linkPotion,64, ZeldaGameState.stageHeight/2+50, 32, 50, null);
		g.drawString("X",100, ZeldaGameState.stageHeight/2+100);
		g.drawString(Integer.toString(link.potions),120, ZeldaGameState.stageHeight/2+100);

		// RUPEES
		rupeeAnim.tick();
		g.drawImage(rupeeAnim.getCurrentFrame(),64, ZeldaGameState.stageHeight/2+110, 32, 50, null);
		g.drawString("X",100, ZeldaGameState.stageHeight/2+160);
		g.drawString(Integer.toString(link.rupees),120, ZeldaGameState.stageHeight/2+160);


		// ARROWs
		g.drawImage(Images.linkArrow[0],64, ZeldaGameState.stageHeight/2+170, 32, 50, null);
		g.drawString("X",100, ZeldaGameState.stageHeight/2+220);
		g.drawString(Integer.toString(link.arrows),120, ZeldaGameState.stageHeight/2+220);


		// KEYS
		g.drawImage(Images.linkKey,64, ZeldaGameState.stageHeight/2+230, 32, 50, null);
		g.drawString("X",100, ZeldaGameState.stageHeight/2+280);
		g.drawString(Integer.toString(link.keys),120, ZeldaGameState.stageHeight/2+280);	
		
		if (gold) {
			g.drawImage(Images.gold, stageWidth + xOffset + 10, handler.getHeight()/2, handler.getWidth()/6, handler.getHeight()/4, null);
		}
		
		else if (silver) {
			g.drawImage(Images.silver, stageWidth + xOffset + 10, handler.getHeight()/2, handler.getWidth()/6, handler.getHeight()/4, null);
		}
		
		else if (bronze) {
			g.drawImage(Images.bronze, stageWidth + xOffset + 20, handler.getHeight()/2, handler.getWidth()/6, handler.getHeight()/4, null);
		}




		/*********************************************************************************************************************
		 * 
		 * 											DRAWING HEALTH
		 * 
		 *********************************************************************************************************************/

		for(int i = 2; i<= link.maxHealth ;i+=2) {
			g.drawImage(Images.linkEmptyHeart, i*32, ZeldaGameState.stageHeight/2-50, 32, 32, null);
		}

		for(int i = 1; i<= link.getHealth();i++) {
			if(i %2 == 0) {
				g.drawImage(Images.linkHeart, i*32, ZeldaGameState.stageHeight/2-50, 32, 32, null);
			}
			else {
				g.drawImage(Images.linkHalfHeart, (i+1)*32, ZeldaGameState.stageHeight/2-50, 32, 32, null);
			}
		}





		if (this.itemToDraw != null) {
			g.drawImage(this.itemToDraw, link.x, link.y - this.itemToDraw.getHeight() * worldScale, this.itemToDraw.getWidth() * worldScale, this.itemToDraw.getHeight() * worldScale, null);
		}

	}

	private void addWorldObjects() {
		/*********************************************************************************************************************
		 * 
		 * 											BEGINNING CAVE
		 * 
		 *********************************************************************************************************************/
		for (int i = 0;i < 16;i++){
			for (int j = 0;j < 11;j++) {
				if (i>=2 && i<=13 && j>=2 && j< 9 ) {
					continue;
				}else{
					if (j>=9){
						if (i>1 && i<14) {
							if ((i == 7 || i==8 )){
								continue;
							}else {
								caveObjects.add(new SolidStaticEntities(i, j, Images.caveTiles.get(2), handler));
							}
						}else{
							caveObjects.add(new SolidStaticEntities(i,j,Images.caveTiles.get(5),handler));
						}
					}else{
						caveObjects.add(new SolidStaticEntities(i,j,Images.caveTiles.get(5),handler));
					}
				}
			}
		}
		caveObjects.add(new DungeonDoor(7,9,16*worldScale*2,16*worldScale * 2,Direction.DOWN,"caveStartLeave",handler,(4 * (ZeldaGameState.stageWidth/16)) + ZeldaGameState.xOffset,(2 * (ZeldaGameState.stageHeight/11)) + ZeldaGameState.yOffset));
		caveObjects.add(new Item(7, 6,"Sword", Images.linkSword, handler));

		/*********************************************************************************************************************
		 * 
		 * 											HE-MAN'S CAVE
		 * 
		 *********************************************************************************************************************/
		//		for (int x = 0;x < 16;x++){
		//			for (int y = 0;y < 11;y++) {
		//				
		//				// Boundaries
		//				if (x>=1 && x<=14 && y>=1 && y< 10 ) {
		//					continue;
		//				}else{
		//					if (y>=9){
		//						if (x>1 && x<14) {
		//							if ((x == 7 || x==8 )){
		//								continue;
		//							}else {
		//								cave2Objects.add(new SolidStaticEntities(x, y, Images.zeldaTiles.get(21), handler));
		//							}
		//						}else{
		//							cave2Objects.add(new SolidStaticEntities(x,y,Images.zeldaTiles.get(20),handler));
		//						}
		//					}else{
		//						cave2Objects.add(new SolidStaticEntities(x,y,Images.zeldaTiles.get(20),handler));
		//					}
		//				}
		//			}
		//		}
		//		cave2Objects.add(new DungeonDoor(7,9,16*worldScale*2,16*worldScale * 2,Direction.DOWN,"heManCaveLeave",handler,(4 * (ZeldaGameState.stageWidth/10)) + ZeldaGameState.xOffset,(2 * (ZeldaGameState.stageHeight/11)) + ZeldaGameState.yOffset));
		//		
		/*********************************************************************************************************************
		 * 
		 * 													OAK'S ROOM
		 * 
		 *********************************************************************************************************************/
		for (int x = -1;x < 17;x++){
			for (int y = -1;y < 12;y++) {

				// Boundaries
				if (x>=0 && x<=14 && y>=2 && y< 10 ) {
					continue;
				}else{
					if (y>=9){
						if (x>1 && x<14) {
							if ((x == 7 || x==8 )){
								continue;
							}else {
								cave3Objects.add(new SolidStaticEntities(x, y, Images.invisibleBlock, handler));
							}
						}else{
							cave3Objects.add(new SolidStaticEntities(x,y,Images.invisibleBlock,handler));
						}
					}else{
						cave3Objects.add(new SolidStaticEntities(x,y,Images.invisibleBlock,handler));
					}
				}
			}
		}


		// OBJECTS
		for (int i = 0; i < 17; i++) {
			if (i > 5 && i < 10) {continue;}
			cave3Objects.add(new SolidStaticEntities(i,6,Images.invisibleBlock,handler));
			cave3Objects.add(new SolidStaticEntities(i,7,Images.invisibleBlock,handler));
		}

		for (int i=1;i<4;i++) {
			cave3Objects.add(new SolidStaticEntities(i,3,Images.invisibleBlock,handler));
			cave3Objects.add(new SolidStaticEntities(i,4,Images.invisibleBlock,handler));
		}

		for (int i = 9; i<13; i++) {
			cave3Objects.add(new SolidStaticEntities(i,3,Images.invisibleBlock,handler));
			cave3Objects.add(new SolidStaticEntities(i,2,Images.invisibleBlock,handler));
		}


		cave3Objects.add(new DungeonDoor(7,9,16*worldScale*2,16*worldScale * 2,Direction.DOWN,"caveThreeLeave",handler,(4 * (ZeldaGameState.stageWidth/10)) + ZeldaGameState.xOffset,(2 * (ZeldaGameState.stageHeight/11)) + ZeldaGameState.yOffset));
		cave3Objects.add(new Item(10, 4, "secondsword", Images.pokeBall, handler));
		cave3Objects.add(new Item(11, 4,"HeartContainer", Images.loveBall, handler));


		/*********************************************************************************************************************
		 * 
		 * 											THE REST OF THE WORLD
		 * 
		 *********************************************************************************************************************/

		refreshWorld();

	}

	/**
	 * Function that should be called when Link picks up an object. It will
	 * play the <i>itemGet.wav</i> file, and set <code>soundCooldown</code> to 2 seconds (the length
	 * of the sound). <code>itemObtained</code> will also become <code>true</code>
	 */

	public void itemGet(SolidStaticEntities item) {
		soundCooldown = 60 * 2;
		itemObtained = true;
		if (!handler.isMute()) {
			handler.getMusicHandler().pauseMusic();
			handler.setMute(false);
			if (inPokemonCave) {handler.getMusicHandler().playEffect("itemGetPokemon.wav");}
			else {handler.getMusicHandler().playEffect("itemGet.wav");}
		}
		this.itemToDraw = item.sprite;
		this.toRemove.add(item);
	}

	public void pickUp(SolidStaticEntities item) {
		this.toRemove.add(item);
	}

	public void summonFireball() {
		BufferedImage[] fire = {Images.zeldaFire};
		this.entitiesToAdd.add(new Projectile(link.x, 0, fire, 4, 4, Direction.DOWN, null, handler));
	}

	public void addExtraEntities() {
		if (entitiesToAdd.size() > 0) {
			for (int i = 0; i < entitiesToAdd.size(); i++) {
				entitiesToTick.add(entitiesToAdd.get(i));
			}
			entitiesToAdd.clear();
		}

	}



	/*********************************************************************************************************************
	 * 
	 * 											METHODS TO CREATE ENEMIES
	 * 
	 *********************************************************************************************************************/

	public HeMan createHeMan() {
		HeMan heman = new HeMan(0,0,Images.heman, handler);
		// TODO: Add more stuff to he-man here
		return heman;
	}

	public Tektite createTektite() {
		return new Tektite(0,0,Images.bouncyEnemyFrames, handler);
	}

	public Tektite createSuperTektite() {
		Tektite oof = createTektite();
		oof.animation = new Animation(256,Images.blueTektike);
		oof.power = 3;
		oof.health = 10;
		return oof;
	}

	public Snake createSnake() {
		return new Snake(0,0,Images.snake, handler);
	}

	public Darknut createDarknut() {
		return new Darknut(0,0,Images.DarknutRight, handler);
	}
	public Creeper createCreeper() {
		return new Creeper(0, 0, Images.CreeperRight, handler);
	}

	public Ghost createBlinky() {
		return new Ghost(0,0,"blinky", Images.blinkyRight, handler);
	}
	public Ghost createInky() {
		return new Ghost(0,0, "inky",Images.inkyRight, handler);
	}
	public Ghost createPinky() {
		return new Ghost(0,0, "pinky",Images.pinkyRight, handler);
	}
	public Ghost createClyde() {
		return new Ghost(0,0, "clyde",Images.clydeRight, handler);
	}

	public GalagaBug createGalagaBug() {
		BufferedImage[][] sprites = {Images.galagaEnemyBee, Images.galagaNewEnemy, Images.galagaEnemyBomber, Images.galagaEnemyButterfly};
		BufferedImage[] anim = sprites[new Random().nextInt(sprites.length)];
		anim = Arrays.copyOfRange(anim, 0, 2);
		GalagaBug b = new GalagaBug(0,0,0,0,anim,handler);
		return b;
	}

	public Donnel createDonnel() {
		return new Donnel(0,0, Images.donnelIdle, handler);
	}
	
	public BillCipher createBill() {
		return new BillCipher(0,0, Images.billUp, handler);
	}




	public void refreshWorld() {

		/*************************************************************************************************************************

			HOW TO MAKE YOUR OWN ROOM

			1) Put a comment stating the location of the room you're working on 
			// maxX,mapY (location) (Link starts at 7,7 for reference)

			2) Create new lists to store the monsters and solids
			monster = new ArrayList<>();
			solids = new ArrayList<>();

		 ***********************************************************************************************************

			ADD YOUR BLOCKS, DUNGEON DOORS, SECTION DOORS, ENEMIES, ETC, TO THE LIST HERE

		 ***********************************************************************************************************

			3) Add everything you made to the lists you initialized
			objects.get(mapX).set(mapY,solids);
			enemies.get(mapY).set(mapY, monster);

		 ************************************************
			(just copy paste from step 2, to the line above)

			NOTES:

				How to make a Section Door:
				solids.add(new SectionDoor(X,Y,16*worldScale * n,16*worldScale * n,Direction.RIGHT,handler));

				X = 0 means Left side of Map
				X = 15 means Right Side of Map
				Y = 0 means Top side of Map
				Y = 10 means Bottom Side of Map
				worldScale * n = how wide/tall the section door is IN BLOCKS


				FOR SECTION DOORS:
					if X == 0, the direction should be Direction.LEFT
					if X == 15, the direction should be Direction.RIGHT
					if Y == 0, the direction should be Direction.UP
					if Y == 10 , the direction should be Direction.DOWN


				FOR ENEMIES:
					TODO: Add this later :)


		 *************************************************************************************************************************/

		//7,7
		ArrayList<SolidStaticEntities> solids = new ArrayList<>();
		ArrayList<BaseMovingEntity> monster = new ArrayList<>();
		solids.add(new SectionDoor( 0,5,16*worldScale,16*worldScale, Direction.LEFT,handler));
		solids.add(new SectionDoor( 7,0,16*worldScale * 2,16*worldScale,Direction.UP,handler));
		solids.add(new DungeonDoor( 4,1,16*worldScale,16*worldScale,Direction.UP,"caveStartEnter",handler,(7 * (ZeldaGameState.stageWidth/16)) + ZeldaGameState.xOffset,(9 * (ZeldaGameState.stageHeight/11)) + ZeldaGameState.yOffset));
		solids.add(new SectionDoor( 15,5,16*worldScale,16*worldScale,Direction.RIGHT,handler));
		solids.add(new SolidStaticEntities(6,0,Images.forestTiles.get(2),handler));
		solids.add(new SolidStaticEntities(5,1,Images.forestTiles.get(5),handler));
		solids.add(new SolidStaticEntities(6,1,Images.forestTiles.get(6),handler));
		solids.add(new SolidStaticEntities(3,2,Images.forestTiles.get(6),handler));
		solids.add(new SolidStaticEntities(2,3,Images.forestTiles.get(6),handler));
		solids.add(new SolidStaticEntities(1,4,Images.forestTiles.get(6),handler));
		solids.add(new SolidStaticEntities(1,6,Images.forestTiles.get(3),handler));
		solids.add(new SolidStaticEntities(1,7,Images.forestTiles.get(5),handler));
		solids.add(new SolidStaticEntities(1,8,Images.forestTiles.get(5),handler));
		solids.add(new SolidStaticEntities(2,9,Images.forestTiles.get(2),handler));
		solids.add(new SolidStaticEntities(3,9,Images.forestTiles.get(2),handler));
		solids.add(new SolidStaticEntities(4,9,Images.forestTiles.get(2),handler));
		solids.add(new SolidStaticEntities(5,9,Images.forestTiles.get(2),handler));
		solids.add(new SolidStaticEntities(6,9,Images.forestTiles.get(2),handler));
		solids.add(new SolidStaticEntities(7,9,Images.forestTiles.get(2),handler));
		solids.add(new SolidStaticEntities(8,9,Images.forestTiles.get(2),handler));
		solids.add(new SolidStaticEntities(9,9,Images.forestTiles.get(2),handler));
		solids.add(new SolidStaticEntities(10,9,Images.forestTiles.get(2),handler));
		solids.add(new SolidStaticEntities(11,9,Images.forestTiles.get(2),handler));
		solids.add(new SolidStaticEntities(12,9,Images.forestTiles.get(2),handler));
		solids.add(new SolidStaticEntities(13,9,Images.forestTiles.get(2),handler));
		solids.add(new SolidStaticEntities(14,8,Images.forestTiles.get(5),handler));
		solids.add(new SolidStaticEntities(14,7,Images.forestTiles.get(5),handler));
		solids.add(new SolidStaticEntities(14,6,Images.forestTiles.get(2),handler));
		solids.add(new SolidStaticEntities(14,4,Images.forestTiles.get(5),handler));
		solids.add(new SolidStaticEntities(13,4,Images.forestTiles.get(5),handler));
		solids.add(new SolidStaticEntities(12,4,Images.forestTiles.get(5),handler));
		solids.add(new SolidStaticEntities(11,4,Images.forestTiles.get(5),handler));
		solids.add(new SolidStaticEntities(10,4,Images.forestTiles.get(5),handler));
		solids.add(new SolidStaticEntities(9,4,Images.forestTiles.get(4),handler));
		solids.add(new SolidStaticEntities(9,3,Images.forestTiles.get(5),handler));
		solids.add(new SolidStaticEntities(9,2,Images.forestTiles.get(5),handler));
		solids.add(new SolidStaticEntities(9,1,Images.forestTiles.get(5),handler));
		solids.add(new SolidStaticEntities(9,0,Images.forestTiles.get(5),handler));
		monster.add(new Tektite(xOffset+(stageWidth/2) - 100,yOffset + (stageHeight/2) - 100,Images.bouncyEnemyFrames,handler));
		objects.get(7).set(7,solids);
		enemies.get(7).set(7, monster);



		//6,7
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		solids.add(new SectionDoor( 0,2,16*worldScale,16*worldScale*7, Direction.LEFT,handler));
		solids.add(new SectionDoor( 12,0,16*worldScale * 2,16*worldScale,Direction.UP,handler));
		solids.add(new SectionDoor( 15,5,16*worldScale,16*worldScale,Direction.RIGHT,handler));

		for (int i = 0; i < 11; i++) {solids.add(new SolidStaticEntities(i,1,Images.forestTiles.get(5), handler));}
		for (int i = 0; i < 14; i++) {solids.add(new SolidStaticEntities(i,9,Images.forestTiles.get(2), handler));}
		//for (int i = 2; i < 12; i+=3) {solids.add(new SolidStaticEntities(i,5,Images.forestTiles.get(33), handler));}
		for (int y = 7; y < 10; y++) {solids.add(new SolidStaticEntities(13,y,Images.forestTiles.get(5), handler));}

		Tektite blue = new Tektite(xOffset+(stageWidth/2) - 100,yOffset + (stageHeight/2) - 100,Images.blueTektike,handler);
		Tektite blue2 = new Tektite(xOffset+(stageWidth/2) - 50,yOffset + (stageHeight/2) - 50,Images.blueTektike,handler);
		Darknut darknut1 = new Darknut(xOffset+(stageWidth/2) - 100, yOffset + (stageHeight/2) +25, Images.DarknutRight, handler);
		blue.health = 10; blue.power = 3; blue2.health = 10; blue2.power = 3;
		monster.add(blue); monster.add(blue2); monster.add(darknut1);
		objects.get(6).set(7,solids);
		enemies.get(6).set(7, monster);


		//7,6
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		solids.add(new SectionDoor( 0,4,16*worldScale,16*worldScale*3, Direction.LEFT,handler));
		solids.add(new SectionDoor( 7,10,16*worldScale * 2,16*worldScale,Direction.DOWN,handler));
		solids.add(new SectionDoor( 15,4,16*worldScale,16*worldScale*3,Direction.RIGHT,handler));
		monster.add(new Creeper(xOffset+(stageWidth/2) - 80,yOffset + (stageHeight/2) - 100,Images.CreeperDown,handler));
		monster.add(new Snake(xOffset + (stageWidth /3) - 45, yOffset + (stageHeight/2)- 75, Images.snake, handler));
		monster.add(new Tektite(xOffset + (stageWidth /3), yOffset + (stageHeight/2)- 120, Images.bouncyEnemyFrames, handler));
		objects.get(7).set(6,solids);
		enemies.get(7).set(6, monster);

		//8,7
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		solids.add(new SectionDoor(0,5,16*worldScale,16*worldScale, Direction.LEFT,handler));
		solids.add(new SectionDoor(2,0,16*worldScale * 13,16*worldScale,Direction.UP,handler));
		solids.add(new SectionDoor(15,2,16*worldScale,16*worldScale*7,Direction.RIGHT,handler));
		monster.add(new Darknut(xOffset+(stageWidth/2) - 80,yOffset + (stageHeight/2) - 100,Images.DarknutDown,handler));
		monster.add(new Snake(xOffset + (stageWidth /3) - 45, yOffset + (stageHeight/2)- 75, Images.snake, handler));
		monster.add(new Tektite(xOffset + (stageWidth /3), yOffset + (stageHeight/2)- 120, Images.bouncyEnemyFrames, handler));
		objects.get(8).set(7,solids);
		enemies.get(8).set(7, monster);

		// 6, 6
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		solids.add(new SectionDoor(15,4,16*worldScale,16*worldScale*3, Direction.RIGHT,handler));
		solids.add(new SectionDoor(12,10,16*worldScale * 2,16*worldScale,Direction.DOWN,handler));
		solids.add(new SectionDoor(0,2,16*worldScale,16*worldScale*7,Direction.LEFT,handler));
		for (int i = 0; i < 11; i++) {solids.add(new SolidStaticEntities(i,0,Images.invisibleBlock, handler));}
		for (int i = 0; i < 11; i++) { if (i == 7 || i == 6) {continue;} solids.add(new SolidStaticEntities(i,1,Images.invisibleBlock, handler));}
		Tektite blue3 = new Tektite(xOffset+(stageWidth/2) - 200,yOffset + (stageHeight/2) - 100,Images.blueTektike,handler);
		Tektite blue4 = new Tektite(xOffset+(stageWidth/2) - 150,yOffset + (stageHeight/2) - 50,Images.bouncyEnemyFrames,handler);
		Darknut darknut2 = new Darknut(xOffset+(stageWidth/2) - 150, yOffset + (stageHeight/2) -75, Images.DarknutRight, handler);
		blue3.health = 10; blue3.power = 3;
		//		solids.add(new DungeonDoor(6,1,16*worldScale,16*worldScale,Direction.UP,"heManCaveEnter",handler,(7 * (ZeldaGameState.stageWidth/16)) + ZeldaGameState.xOffset + 50,(9 * (ZeldaGameState.stageHeight/11)) + ZeldaGameState.yOffset));
		//		solids.add(new DungeonDoor(7,1,16*worldScale,16*worldScale,Direction.UP,"heManCaveEnter",handler,(7 * (ZeldaGameState.stageWidth/16)) + ZeldaGameState.xOffset + 50,(9 * (ZeldaGameState.stageHeight/11)) + ZeldaGameState.yOffset));
		solids.add(new DungeonDoor(6,1,16*worldScale,16*worldScale,Direction.UP,"heManCaveEnter",handler,itemXToOverworldX(BossBattleState.roomWidth/2),itemYToOverworldY(BossBattleState.bottomY)));
		solids.add(new DungeonDoor(7,1,16*worldScale,16*worldScale,Direction.UP,"heManCaveEnter",handler,itemXToOverworldX(BossBattleState.roomWidth/2),itemYToOverworldY(BossBattleState.bottomY)));
		monster.add(blue3); monster.add(blue4);monster.add(darknut2);
		objects.get(6).set(6,solids);
		enemies.get(6).set(6, monster);

		// 5,6
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		solids.add(new SectionDoor(0,2,16*worldScale,16*worldScale*7,Direction.LEFT,handler));
		solids.add(new SectionDoor(15,2,16*worldScale,16*worldScale*7,Direction.RIGHT,handler));
		for (int i = 0; i < 16; i++) {solids.add(new SolidStaticEntities(i,0,Images.invisibleBlock, handler));}
		for (int i = 0; i < 16; i++) { if (!(i == 4)) solids.add(new SolidStaticEntities(i,9,Images.invisibleBlock, handler));}
		for (int i = 0; i < 16; i++) { if (!(i == 4)) solids.add(new SolidStaticEntities(i,10,Images.invisibleBlock, handler));}
		solids.add(new SectionDoor(4,10,16*worldScale,16*worldScale,Direction.DOWN,handler));
		for (int y = 0; y < 11 ; y++) {
			if (y == 5) {continue;}
			solids.add(new SolidStaticEntities(5, y, Images.invisibleBlock, handler));
			solids.add(new SolidStaticEntities(6, y, Images.invisibleBlock, handler));
		}
		//monster.add(new Darknut(xOffset+(stageWidth/3) - 80,yOffset + (stageHeight/2) - 100,Images.CreeperDown,handler));
		monster.add(new Snake(xOffset + (stageWidth /2) - 45, yOffset + (stageHeight/2)- 75, Images.snake, handler));
		objects.get(5).set(6,solids);
		enemies.get(5).set(6, monster);

		// 5,7
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		monster.add(new Snake(xOffset + (stageWidth /2) + 45, yOffset + (stageHeight/2)- 75, Images.snake, handler));
		monster.add(new Tektite(xOffset + (stageWidth /2) + 30, yOffset + (stageHeight/2)- 120, Images.bouncyEnemyFrames, handler));
		solids.add(new SectionDoor(4,0,16*worldScale,16*worldScale,Direction.UP,handler));
		solids.add(new DungeonDoor(2,1,16*worldScale,16*worldScale,Direction.UP,"shopEnter",handler,(2 * (ZeldaGameState.stageWidth/16)) + ZeldaGameState.xOffset,((ZeldaGameState.stageHeight/11)) + ZeldaGameState.yOffset + 40));
		solids.add(new SectionDoor(15,2,16*worldScale,16*worldScale*7, Direction.RIGHT,handler));

		solids.add(new SolidStaticEntities(15, 0, Images.invisibleBlock, handler));
		solids.add(new SolidStaticEntities(15, 1, Images.invisibleBlock, handler));
		solids.add(new SolidStaticEntities(15, 9, Images.invisibleBlock, handler));
		for (int y = 0; y < 11 ; y++) {
			solids.add(new SolidStaticEntities(5, y, Images.invisibleBlock, handler));
			solids.add(new SolidStaticEntities(6, y, Images.invisibleBlock, handler));
		}

		for (int x = 0; x < 15 ; x++) {
			if(x != 4) {
				solids.add(new SolidStaticEntities(x, 1, Images.invisibleBlock, handler));
			}
		}
		for (int x = 0; x < 16 ; x++) {solids.add(new SolidStaticEntities(x, 9, Images.invisibleBlock, handler));}
		for (int y = 1; y < 10 ; y++) {solids.add(new SolidStaticEntities(0, y, Images.invisibleBlock, handler));}		
		objects.get(5).set(7,solids);
		enemies.get(5).set(7, monster);

		// 4,6
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		solids.add(new SectionDoor(15,2,16*worldScale,16*worldScale*7,Direction.RIGHT,handler));
		solids.add(new SectionDoor(0,2,16*worldScale,16*worldScale*7,Direction.LEFT,handler));
		for (int i = 0; i < 16; i++) {solids.add(new SolidStaticEntities(i,0,Images.invisibleBlock, handler));}
		for (int i = 0; i < 16; i++) {solids.add(new SolidStaticEntities(i,10,Images.invisibleBlock, handler));}
		solids.add(new SolidStaticEntities(0, 9, Images.invisibleBlock, handler));
		solids.add(new SolidStaticEntities(15, 9, Images.invisibleBlock, handler));
		solids.add(new SolidStaticEntities(0, 1, Images.invisibleBlock, handler));
		solids.add(new SolidStaticEntities(15, 1, Images.invisibleBlock, handler));
		
		monster.add(new Creeper(xOffset+(stageWidth/2) - 80,yOffset + (stageHeight/2) - 100,Images.CreeperDown,handler));
		monster.add(new Tektite(xOffset + (stageWidth /3) - 45, yOffset + (stageHeight/2)- 75, Images.bouncyEnemyFrames, handler));
		//monster.add(new Darknut(xOffset + (stageWidth /2), yOffset + (stageHeight/2)- 90, Images.DarknutRight, handler));

		solids.add(new DungeonDoor(7,1,16*worldScale,16*worldScale,Direction.UP,"caveThreeEnter",handler,(7 * (ZeldaGameState.stageWidth/16)) + ZeldaGameState.xOffset,(9 * (ZeldaGameState.stageHeight/11)) + ZeldaGameState.yOffset));
		objects.get(4).set(6,solids);
		enemies.get(4).set(6, monster);

		// 4,7
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		monster.add(new Creeper(xOffset+(stageWidth/3) - 70,yOffset + (stageHeight/2) - 100,Images.CreeperDown,handler));
		//monster.add(new Darknut(xOffset + (stageWidth /2), yOffset + (stageHeight/2)- 90, Images.DarknutRight, handler));
		solids.add(new SectionDoor(0,2,16*worldScale,16*worldScale*7,Direction.LEFT,handler));
		solids.add(new DungeonDoor(8,4,16*worldScale,16*worldScale,Direction.UP,"dungeon1Enter",handler,itemXToOverworldX(Dungeon.roomWidth/2),itemYToOverworldY(Dungeon.roomHeight)));
		objects.get(4).set(7,solids);
		enemies.get(4).set(7, monster);

		// 3,6
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		monster.add(new Snake(xOffset+(stageWidth/2) - 80,yOffset + (stageHeight/2) - 100,Images.snake,handler));
		monster.add(new Tektite(xOffset + (stageWidth /3) - 45, yOffset + (stageHeight/2)- 75, Images.bouncyEnemyFrames, handler));
		//monster.add(new Darknut(xOffset + (stageWidth /2), yOffset + (stageHeight/2)- 90, Images.DarknutRight, handler));
		solids.add(new SectionDoor(15,2,16*worldScale,16*worldScale*7,Direction.RIGHT,handler));
		solids.add(new SectionDoor(7,10,16*worldScale*7,16*worldScale,Direction.DOWN,handler));
		for (int i = 0; i < 16; i++) {solids.add(new SolidStaticEntities(i,0,Images.invisibleBlock, handler));}
		for (int y = 0; y < 11; y++) {
			solids.add(new SolidStaticEntities(0, y, Images.invisibleBlock, handler));
			solids.add(new SolidStaticEntities(1, y, Images.invisibleBlock, handler));
		}
		for (int i = 0; i < 16; i++) { if (i < 7 || i > 13) solids.add(new SolidStaticEntities(i,10,Images.invisibleBlock, handler));}

		objects.get(3).set(6,solids);
		enemies.get(3).set(6, monster);

		// 3,7
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		monster.add(new Tektite(xOffset+(stageWidth/2) - 80,yOffset + (stageHeight/2) - 100,Images.bouncyEnemyFrames,handler));
		monster.add(new Tektite(xOffset + (stageWidth /3) - 45, yOffset + (stageHeight/2)- 75, Images.bouncyEnemyFrames, handler));
		//monster.add(new Darknut(xOffset + (stageWidth /2), yOffset + (stageHeight/2)- 90, Images.DarknutRight, handler));
		solids.add(new SectionDoor(7,0,16*worldScale*7,16*worldScale,Direction.UP,handler));
		solids.add(new SectionDoor(15,2,16*worldScale,16*worldScale*7,Direction.RIGHT,handler));
		objects.get(3).set(7,solids);
		enemies.get(3).set(7, monster);




		// 8,6
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		monster.add(new Creeper(xOffset+(stageWidth/2) - 80,yOffset + (stageHeight/2) - 100,Images.CreeperDown,handler));
		monster.add(new Snake(xOffset + (stageWidth /3), yOffset + (stageHeight/2)- 75, Images.snake, handler));
		//monster.add(new Darknut(xOffset + (stageWidth /2), yOffset + (stageHeight/2)- 20, Images.DarknutRight, handler));
		solids.add(new SectionDoor(0,4,16*worldScale,16*worldScale*3,Direction.LEFT,handler));
		solids.add(new SectionDoor(15,4,16*worldScale,16*worldScale*3,Direction.RIGHT,handler));
		solids.add(new SectionDoor(2,0,16*worldScale * 14,16*worldScale,Direction.UP,handler));
		solids.add(new SectionDoor(2,10,16*worldScale * 14,16*worldScale,Direction.DOWN,handler));
		objects.get(8).set(6,solids);
		enemies.get(8).set(6, monster);


		// 9,6
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		//monster.add(new Darknut(xOffset+(stageWidth/2) - 20,yOffset + (stageHeight/2),Images.DarknutDamageDown,handler));
		monster.add(new Snake(xOffset + (stageWidth /3) - 45, yOffset + (stageHeight/2)- 75, Images.snake, handler));
		monster.add(new Darknut(xOffset + (stageWidth /2), yOffset + (stageHeight/2)- 90, Images.DarknutRight, handler));
		solids.add(new SectionDoor(15,5,16*worldScale,16*worldScale*3,Direction.RIGHT,handler));
		solids.add(new SectionDoor(6,0,16*worldScale*2,16*worldScale,Direction.UP,handler));
		solids.add(new SectionDoor(0,3,16*worldScale,16*worldScale*3,Direction.LEFT,handler));

		objects.get(9).set(6,solids);
		enemies.get(9).set(6, monster);



	}


	@Override
	public void refresh() {
		inCave = false;
		inHeManCave = false;
		inPokemonCave = false;
		bossDefeated = false;
		usedDEBUG = false;
		potionsUsed = 0;
		rupeesSpent = 0;
		xOffset = handler.getWidth()/4;
		yOffset = handler.getHeight()/4;
		stageWidth = handler.getWidth()/3 + (handler.getWidth()/15);
		stageHeight = handler.getHeight()/2;
		worldScale = 2; //2
		mapX = 7;//7
		mapY = 7;//7
		mapWidth = 256;
		mapHeight = 176;
		cameraOffsetX =  ((mapWidth*mapX) + mapX + 1)*worldScale;
		cameraOffsetY = ((mapHeight*mapY) + mapY + 1)*worldScale;
		objects = new ArrayList<>();
		enemies = new ArrayList<>();
		caveObjects = new ArrayList<>();
		caveEntities = new ArrayList<>();
		cave2Objects = new ArrayList<>();
		cave2Entities = new ArrayList<>();
		cave3Objects = new ArrayList<>();
		cave3Entities = new ArrayList<>();
		rupeeAnim = new Animation(100, Images.rupeeFrames);
		oak = new Animation(252, Images.profOak);
		if (handler.getStoreState()!= null) handler.getStoreState().boughtSword = false;
		if (handler.getBossState()!=null) {handler.getBossState().refresh();}
		for (int i =0;i<16;i++){
			objects.add(new ArrayList<>());
			enemies.add(new ArrayList<>());
			for (int j =0;j<8;j++) {
				objects.get(i).add(new ArrayList<>());
				enemies.get(i).add(new ArrayList<>());
			}
		}

		addWorldObjects();

		if (handler.getBossState()!= null) {
			handler.getBossState().refresh();
		}

		if (handler.getDungeon1()!=null) {handler.getDungeon1().refresh();}

		link = new Link(xOffset+(stageWidth/2),yOffset + (stageHeight/2),Images.zeldaLinkFrames,handler);
	}


	/*********************************************************************************************************************
	 * 
	 * 											PRIVATE METHODS
	 * 
	 *********************************************************************************************************************/

	private boolean linkChoseAnItem() {
		if (inPokemonCave) {
			boolean sword = false; boolean heart = false;
			for (SolidStaticEntities items : cave3Objects) {
				if (items instanceof Item) {
					if (((Item) items).getName().equals("secondsword")) {
						sword = true;
					}

					else if (((Item) items).getName().equals("heartcontainer")) {
						heart = true;
					}
				}
			}

			return !(sword && heart);
		}

		return false;
	}


	//	private int overWorldXToItemX(int x) {
	//		BaseMovingEntity dummy = new BaseMovingEntity(x, 0, null, handler);
	//		return dummy.bounds.x;
	//	}
	//	
	//	private int overWorldYToItemY(int y) {
	//		BaseMovingEntity dummy = new BaseMovingEntity(0, y, null, handler);
	//		return dummy.bounds.y;
	//	}

	public void clearItems() {
		for (int i = 0; i < toRemove.size(); i++) {
			SolidStaticEntities b1 = toRemove.get(i);
			for (int k = 0; k < toTick.size(); k++) {
				SolidStaticEntities b2 = toTick.get(k);
				if (b1.equals(b2)) {
					toTick.remove(k);
				}
			}
		}
	}

	public void clearEntities() {
		for (int i = 0; i < toRemove2.size(); i++) {
			BaseMovingEntity b1 = toRemove2.get(i);
			for (int k = 0; k < entitiesToTick.size(); k++) {
				BaseMovingEntity b2 = entitiesToTick.get(k);
				if (b1.equals(b2)) {

					if (!(b2 instanceof Projectile) && !(handler.getState().equals(handler.getBossState()) && (handler.getBossState().inHeManRoom))) {

						if (b2 instanceof Enemy) {
							((Enemy)b2).dropItem();
						}
					}
					entitiesToTick.remove(k);
				}
			}
		}
	}

	/*********************************************************************************************************************
	 * 
	 * 											STATIC METHODS
	 * 
	 *********************************************************************************************************************/

	public static int itemXToOverworldX(int x) {
		Item dummy = new Item(x, 0, 0, "dummy", null, null);
		return dummy.bounds.x;
	}

	public static int itemYToOverworldY(int y) {
		Item dummy = new Item(0, y, 0, "dummy", null, null);
		return dummy.bounds.y;
	}

	public static int OverworldXToItemX(int x) {

		int closest = -1;
		int distance = -1;

		for (int i = 0; i < 16; i++) {
			if (distance == -1 || Math.abs(itemXToOverworldX(i) - x) < distance) {
				closest = i;
				distance = Math.abs(itemXToOverworldX(i) - x);
			}
		}

		return closest;

	}

	public static int OverworldYToItemY(int y) {

		int closest = -1;
		int distance = -1;

		for (int i = 0; i < 10; i++) {
			if (distance == -1 || Math.abs(itemYToOverworldY(i) - y) < distance) {
				closest = i;
				distance = Math.abs(itemYToOverworldY(i) - y);
			}
		}

		return closest;

	}


}
