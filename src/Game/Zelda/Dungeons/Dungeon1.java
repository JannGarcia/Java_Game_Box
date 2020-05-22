package Game.Zelda.Dungeons;

import java.util.Random;

import Game.Zelda.Entities.Dynamic.BillCipher;
import Game.Zelda.Entities.Dynamic.Direction;
import Game.Zelda.Entities.Statics.DungeonDoor;
import Game.Zelda.Entities.Statics.Item;
import Game.Zelda.World.Dungeon;
import Game.Zelda.World.Room;
import Main.Handler;
import Resources.Images;

public class Dungeon1 extends Dungeon {
	
	private int bossRoomX = this.dungeonWidth/2, bossRoomY = this.dungeonHeight-1;
	private boolean inBossRoom = false;
	
	BillCipher boss;

	public Dungeon1(Handler handler, int startX, int startY, int width, int height, DungeonDoor exit, String name) {
		super(handler, startX, startY, width, height, exit, name);
		refresh();
	}
	
	@Override
	public void tick() {
		super.tick();
		
		// If you're in the boss room
		if (bossRoomX == this.roomX && bossRoomY == this.roomY) {
			inBossRoom = true;
			if (boss.defeated()) {
				this.getCurrentRoom().openBottomDoor();
			}
		}
		
		// Leaving room
		else if (inBossRoom) {
			inBossRoom = false;
			musicHandler.changeMusic(super.DUNGEON_MUSIC);
		}
		
	}

	@Override
	public void addDungeonObjects() {
		
		// THIRD LEVEL
		this.getRoomAt(2, 2).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 10, "rupee", Images.blueRupee, handler));
		this.getRoomAt(2, 2).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(2, 2).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(2, 2).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "arrow", Images.linkArrow[0], handler));
		this.getRoomAt(2, 2).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "potion", Images.linkPotion, handler));
		
		this.getRoomAt(3, 2).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 10, "rupee", Images.blueRupee, handler));
		this.getRoomAt(3, 2).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(3, 2).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(3, 2).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "arrow", Images.linkArrow[0], handler));
		this.getRoomAt(3, 2).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "potion", Images.linkPotion, handler));

		this.getRoomAt(4, 2).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 10, "rupee", Images.blueRupee, handler));
		this.getRoomAt(4, 2).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(4, 2).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(4, 2).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "potion", Images.linkPotion, handler));

		// SECOND LEVEL
		this.getRoomAt(1, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 10, "rupee", Images.blueRupee, handler));
		this.getRoomAt(1, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(1, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(1, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "arrow", Images.linkArrow[0], handler));
		this.getRoomAt(1, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "potion", Images.linkPotion, handler));

		this.getRoomAt(2, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(2, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(2, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "arrow", Images.linkArrow[0], handler));

		this.getRoomAt(3, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(3, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(3, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(3, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "arrow", Images.linkArrow[0], handler));
		this.getRoomAt(3, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "potion", Images.linkPotion, handler));

		this.getRoomAt(4, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(4, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(4, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "arrow", Images.linkArrow[0], handler));
		this.getRoomAt(4, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "arrow", Images.linkArrow[0], handler));

		this.getRoomAt(5, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(5, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(5, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(5, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "arrow", Images.linkArrow[0], handler));
		this.getRoomAt(5, 1).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "potion", Images.linkPotion, handler));
		
		//FIRST LEVEL
		this.getRoomAt(0, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(0, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(0, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(0, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "heartcontainer", Images.heartContainer, handler));

		this.getRoomAt(1, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(1, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(1, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(1, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "arrow", Images.linkArrow[0], handler));
		this.getRoomAt(1, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "potion", Images.linkPotion, handler));

		this.getRoomAt(2, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(2, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(2, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(2, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "arrow", Images.linkArrow[0], handler));
		
		this.getRoomAt(3, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(3, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(2, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "potion", Images.linkPotion, handler));
		
		this.getRoomAt(4, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(4, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(4, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(4, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "arrow", Images.linkArrow[0], handler));
		
		this.getRoomAt(5, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(5, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(5, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(5, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "arrow", Images.linkArrow[0], handler));
		this.getRoomAt(5, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "potion", Images.linkPotion, handler));

		this.getRoomAt(6, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(6, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(6, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, 5, "rupee", Images.blueRupee, handler));
		this.getRoomAt(6, 0).getBlocks().add(new Item(new Random().nextInt(8) +3, new Random().nextInt(10)+1, "heartcontainer", Images.heartContainer, handler));

	}

	@Override
	public void addExtraDoors() {

		// Forming a triangle shape
		int left = 0, right = this.dungeonWidth - 1;
		int height = 0;
		while (left <= right && height < this.dungeonHeight) {
			Room room1 = this.getRoomAt(left, height);
			Room room2 = this.getRoomAt(right, height);
			room1.closeLeftDoor(); room1.closeTopDoor();
			room2.closeRightDoor(); room2.closeTopDoor();
			left++; right--; height++;
		}

		// Closing All Doors
		for (int i = 0; i < this.dungeonWidth; i++) {
			if (!(i == this.dungeonWidth / 2)) {
				this.getRoomAt(i, 0).closeTopDoor();
			}
		}

		this.getRoomAt(this.dungeonWidth / 2, 1).closeTopDoor();

		// Locking Doors
		this.getRoomAt(0, 0).lockRightDoor();
		this.getRoomAt(this.dungeonWidth - 1, 0).lockLeftDoor();
		this.getRoomAt(this.dungeonWidth / 2, 1).lockLeftDoor();
		this.getRoomAt(this.dungeonWidth / 2, 1).lockRightDoor();
		this.getRoomAt(this.dungeonWidth / 2, 2).lockTopDoor();
	}

	@Override
	public void addDungeonEnemies() {

		BillCipher cy = zeldaState.createBill();
		cy.setDirection(Direction.DOWN);
		this.boss = cy;
		this.getRoomAt(bossRoomX, bossRoomY).addEnemyAt(roomWidth/2, roomHeight/2, cy);
		
	    // FIRST LEVEL
		
		this.getRoomAt(0, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(0, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSuperTektite());
		this.getRoomAt(0, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createTektite());
		this.getRoomAt(0, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createTektite());
		this.getRoomAt(0, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createDarknut());
		this.getRoomAt(0, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSnake());

		this.getRoomAt(1, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(1, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSuperTektite());
		this.getRoomAt(1, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createTektite());
		this.getRoomAt(1, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSnake());

		this.getRoomAt(2, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSuperTektite());
		this.getRoomAt(2, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createTektite());
		this.getRoomAt(2, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createTektite());
		this.getRoomAt(2, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSnake());
		
		this.getRoomAt(4, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(4, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createTektite());
		this.getRoomAt(4, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createDarknut());
		this.getRoomAt(4, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createTektite());
		this.getRoomAt(4, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createTektite());

		
		this.getRoomAt(5, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(5, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createTektite());
		this.getRoomAt(5, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createTektite());
		this.getRoomAt(5, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createDarknut());
		
		this.getRoomAt(6, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(6, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSuperTektite());
		this.getRoomAt(6, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createTektite());
		this.getRoomAt(6, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSnake());
		this.getRoomAt(6, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createDarknut());
		this.getRoomAt(6, 0).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSnake());
		
		// SECOND LEVEL 
		
		this.getRoomAt(1, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(1, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSuperTektite());
		this.getRoomAt(1, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createDarknut());
		this.getRoomAt(1, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSnake());
		
		this.getRoomAt(2, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(2, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSuperTektite());
		this.getRoomAt(2, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createDarknut());
		this.getRoomAt(2, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(2, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createDarknut());
		this.getRoomAt(2, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSnake());
		
		this.getRoomAt(3, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(3, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSuperTektite());
		this.getRoomAt(3, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSnake());
		this.getRoomAt(3, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createTektite());
		this.getRoomAt(3, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createDarknut());
		this.getRoomAt(3, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSnake());
		
		this.getRoomAt(4, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(4, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSuperTektite());
		this.getRoomAt(4, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSuperTektite());
		this.getRoomAt(4, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createDarknut());
		this.getRoomAt(4, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSnake());
		
		this.getRoomAt(5, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(5, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSuperTektite());
		this.getRoomAt(5, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(5, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(5, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createDarknut());
		this.getRoomAt(5, 1).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSnake());
		
		// THIRD LEVEL
		
		this.getRoomAt(2, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(2, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSuperTektite());
		this.getRoomAt(2, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(2, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createDarknut());
		this.getRoomAt(2, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSnake());

		this.getRoomAt(3, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(3, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSuperTektite());
		this.getRoomAt(3, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(3, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(3, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createDarknut());

		this.getRoomAt(4, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(4, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createSuperTektite());
		this.getRoomAt(4, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(4, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createCreeper());
		this.getRoomAt(4, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createDarknut());
		this.getRoomAt(4, 2).addEnemyAt(new Random().nextInt(6) + 3,new Random().nextInt(9)+1 , zeldaState.createDonnel());
	
		
	}

	@Override
	public void refresh() {
		super.refresh();
	}

}
