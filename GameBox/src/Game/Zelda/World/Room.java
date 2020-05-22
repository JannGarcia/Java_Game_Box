package Game.Zelda.World;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Game.GameStates.Zelda.BossBattleState;
import Game.GameStates.Zelda.ZeldaGameState;
import Game.Zelda.Entities.Dynamic.BaseMovingEntity;
import Game.Zelda.Entities.Dynamic.Direction;
import Game.Zelda.Entities.Statics.DungeonDoor;
import Game.Zelda.Entities.Statics.Item;
import Game.Zelda.Entities.Statics.SolidStaticEntities;
import Main.Handler;
import Resources.Images;

public class Room {

	private Handler handler;
	private Dungeon home;

	private ArrayList<SolidStaticEntities> blocks;
	private ArrayList<BaseMovingEntity> enemies;
	private DungeonDoor leftDoor, rightDoor, topDoor, bottomDoor;
	private SolidStaticEntities leftWall, rightWall, topWall, bottomWall;
	private int leftLoc, rightLoc, topLoc, bottomLoc;

	private int leftX, rightX, topY, bottomY;
	public int xInMap, yInMap;
	private int roomWidth, roomHeight;

	public Room(Handler handler, Dungeon home) {
		this.handler = handler;
		this.home = home;
		leftX = 0;
		topY = -1;
		rightX = Dungeon.roomWidth;
		bottomY = Dungeon.roomHeight;
		roomWidth = rightX - leftX;
		roomHeight = bottomY - topY;

		initRoom();
	}

	public void initRoom() {

		if (roomWidth <= 0 || roomHeight <= 0) {
			throw new IllegalArgumentException(
					"roomWidth (" + roomWidth + ") or roomHeight (" + roomHeight + ") is smaller than 0.");
		}

		blocks = new ArrayList<>();
		enemies = new ArrayList<>();

		// Top Wall / Bottom Wall
		for (int x = leftX, xBlock = 0; x <= rightX && xBlock <= roomWidth; x += 2, xBlock += 2) {

			boolean up = false, down = false;

			if ((xBlock == (int) (roomWidth / 2) - 1 || xBlock == (int) (roomWidth / 2))) {
				DungeonDoor door = new DungeonDoor(x, topY, 16 * ZeldaGameState.worldScale * 2,
						16 * ZeldaGameState.worldScale * 2, Direction.UP, "movingUp", handler,
						ZeldaGameState.itemXToOverworldX(x), ZeldaGameState.itemYToOverworldY(bottomY - 2));
				door.sprite = home.topDoorImage;
				this.topDoor = door;
				this.topWall = new SolidStaticEntities(x, topY, home.topWallImage, handler);
				blocks.add(door);
				this.topLoc = this.blocks.indexOf(door);
				up = true;
			}

			if ((xBlock == (int) (roomWidth / 2) - 1 || xBlock == (int) (roomWidth / 2))) {
				DungeonDoor door = new DungeonDoor(x, bottomY, 16 * ZeldaGameState.worldScale * 2,
						16 * ZeldaGameState.worldScale * 2, Direction.DOWN, "movingDown", handler,
						ZeldaGameState.itemXToOverworldX(x), ZeldaGameState.itemYToOverworldY(topY + 2));
				door.sprite = home.bottomDoorImage;
				this.bottomDoor = door;
				this.bottomWall = new SolidStaticEntities(x, bottomY, home.bottomWallImage, handler);
				blocks.add(door);
				this.bottomLoc = this.blocks.indexOf(door);
				down = true;
			}

			if (!up) {
				blocks.add(new SolidStaticEntities(x, topY, home.topWallImage, handler));
			}
			if (!down) {
				blocks.add(new SolidStaticEntities(x, bottomY, home.bottomWallImage, handler));
			}

		}

		// Left Wall / Right Wall
		for (int y = topY, yBlock = 0; y <= bottomY && yBlock <= roomHeight; y += 2, yBlock += 2) {

			boolean left = false, right = false;

			// DRAWING CORNERS
			if (y == bottomY || y == topY) {
				blocks.add(new SolidStaticEntities(leftX, y, home.cornerTile, handler));
				blocks.add(new SolidStaticEntities(rightX, y, home.cornerTile, handler));
				blocks.add(new SolidStaticEntities(leftX, y + 1, home.cornerTile, handler));
				blocks.add(new SolidStaticEntities(rightX, y + 1, home.cornerTile, handler));
				blocks.add(new SolidStaticEntities(leftX + 1, y, home.cornerTile, handler));
				blocks.add(new SolidStaticEntities(rightX + 1, y, home.cornerTile, handler));
				blocks.add(new SolidStaticEntities(leftX + 1, y + 1, home.cornerTile, handler));
				blocks.add(new SolidStaticEntities(rightX + 1, y + 1, home.cornerTile, handler));
				continue;
			}

			if ((yBlock == (int) (roomHeight / 2) - 1 || yBlock == (int) (roomHeight / 2))) {
				DungeonDoor door = new DungeonDoor(leftX, y, 16 * ZeldaGameState.worldScale * 2,
						16 * ZeldaGameState.worldScale * 2, Direction.LEFT, "movingLeft", handler,
						ZeldaGameState.itemXToOverworldX(rightX - 2), ZeldaGameState.itemYToOverworldY(y));
				door.sprite = home.leftDoorImage;
				blocks.add(door);
				this.leftLoc = this.blocks.indexOf(door);
				this.leftDoor = door;
				this.leftWall = new SolidStaticEntities(leftX, y, home.leftWallImage, handler);
				left = true;

			}

			if ((yBlock == (int) (roomHeight / 2) - 1 || yBlock == (int) (roomHeight / 2))) {
				DungeonDoor door = new DungeonDoor(rightX, y, 16 * ZeldaGameState.worldScale * 2,
						16 * ZeldaGameState.worldScale * 2, Direction.RIGHT, "movingRight", handler,
						ZeldaGameState.itemXToOverworldX(leftX + 2), ZeldaGameState.itemYToOverworldY(y));
				door.sprite = home.rightDoorImage;
				blocks.add(door);
				this.rightLoc = this.blocks.indexOf(door);
				this.rightDoor = door;
				this.rightWall = new SolidStaticEntities(rightX, y, home.rightWallImage, handler);
				right = true;

			}

			if (!left) {
				blocks.add(new SolidStaticEntities(leftX, y, home.leftWallImage, handler));
			}
			if (!right) {
				blocks.add(new SolidStaticEntities(rightX, y, home.rightWallImage, handler));
			}

		}

	}

	public void clearEntities() {
		this.enemies.clear();
	}

	private void addEnemy(BaseMovingEntity entity) {
		this.getEnemies().add(entity);
	}

	public int getbottomY() {
		return this.bottomY;
	}

	public int getrightX() {
		return this.rightX;
	}

	public int getleftX() {
		return this.leftX;
	}

	public int gettopY() {
		return this.topY;
	}

	public ArrayList<SolidStaticEntities> getBlocks() {
		return this.blocks;
	}

	public ArrayList<BaseMovingEntity> getEnemies() {
		return this.enemies;
	}
	
	public void addEnemyAt(int x, int y, BaseMovingEntity entity) {
		// If any boundary is out of the map
		if (x < leftX || x > rightX) {x = x < leftX ? (leftX + 2) : (leftX - 2);}
		if (y < topY || y > bottomY) {y = y < topY ? (topY + 2) : (bottomY - 2);}
		
		entity.x = ZeldaGameState.itemXToOverworldX(x);
		entity.y = ZeldaGameState.itemYToOverworldY(y);
		
		this.addEnemy(entity);
	}

	public void setDoors(boolean up, boolean down, boolean left, boolean right) {
		if (up) {
			closeTopDoor();
		} else {
			openTopDoor();
		}
		if (down) {
			closeBottomDoor();
		} else {
			openBottomDoor();
		}
		if (left) {
			closeLeftDoor();
		} else {
			openLeftDoor();
		}
		if (right) {
			closeRightDoor();
		} else {
			openRightDoor();
		}
	}

	public void setLeftEntrance(DungeonDoor dungeonDoor) {

		dungeonDoor.x = this.blocks.get(leftLoc).x;
		dungeonDoor.y = this.blocks.get(leftLoc).y;
		dungeonDoor.x = this.leftDoor.x;
		dungeonDoor.y = this.leftDoor.y;
		dungeonDoor.bounds.x = ZeldaGameState.itemXToOverworldX(dungeonDoor.x);
		dungeonDoor.bounds.y = ZeldaGameState.itemYToOverworldY(dungeonDoor.y);
		this.getBlocks().set(leftLoc, dungeonDoor);
		this.home.getRoomAt(this.home.entranceX - 1, this.home.entranceY).setDoors(true, true, true, true);
		this.home.getRoomAt(this.home.entranceX - 1, this.home.entranceY - 1).closeTopDoor();
		this.home.getRoomAt(this.home.entranceX - 1, this.home.entranceY + 1).closeBottomDoor();
		this.home.getRoomAt(this.home.entranceX - 2, this.home.entranceY).closeRightDoor();

	}

	public void setRightEntrance(DungeonDoor dungeonDoor) {

		dungeonDoor.x = this.blocks.get(rightLoc).x;
		dungeonDoor.y = this.blocks.get(rightLoc).y;
		dungeonDoor.bounds.x = ZeldaGameState.itemXToOverworldX(dungeonDoor.x);
		dungeonDoor.bounds.y = ZeldaGameState.itemYToOverworldY(dungeonDoor.y);
		this.getBlocks().set(rightLoc, dungeonDoor);
		this.home.getRoomAt(this.home.entranceX + 1, this.home.entranceY).setDoors(true, true, true, true);
		this.home.getRoomAt(this.home.entranceX + 1, this.home.entranceY - 1).closeTopDoor();
		this.home.getRoomAt(this.home.entranceX + 1, this.home.entranceY + 1).closeBottomDoor();
		this.home.getRoomAt(this.home.entranceX + 2, this.home.entranceY).closeLeftDoor();
		

	}

	public void setBottomEntrance(DungeonDoor dungeonDoor) {
		
		dungeonDoor.x = this.blocks.get(bottomLoc).x;
		dungeonDoor.y = this.blocks.get(bottomLoc).y;
		dungeonDoor.bounds.x = ZeldaGameState.itemXToOverworldX(dungeonDoor.x);
		dungeonDoor.bounds.y = ZeldaGameState.itemYToOverworldY(dungeonDoor.y);
		this.getBlocks().set(bottomLoc, dungeonDoor);
		this.home.getRoomAt(this.home.entranceX, this.home.entranceY - 1).setDoors(true, true, true, true);
		this.home.getRoomAt(this.home.entranceX - 1, this.home.entranceY - 1).closeRightDoor();
		this.home.getRoomAt(this.home.entranceX + 1, this.home.entranceY - 1).closeLeftDoor();
		this.home.getRoomAt(this.home.entranceX, this.home.entranceY - 2).closeTopDoor();

	}

	public void setTopEntrance(DungeonDoor dungeonDoor) {
		
		dungeonDoor.x = this.blocks.get(topLoc).x;
		dungeonDoor.y = this.blocks.get(topLoc).y;
		dungeonDoor.bounds.x = ZeldaGameState.itemXToOverworldX(dungeonDoor.x);
		dungeonDoor.bounds.y = ZeldaGameState.itemYToOverworldY(dungeonDoor.y);
		this.getBlocks().set(topLoc, dungeonDoor);
		this.home.getRoomAt(this.home.entranceX, this.home.entranceY + 1).setDoors(true, true, true, true);
		this.home.getRoomAt(this.home.entranceX - 1, this.home.entranceY + 1).closeRightDoor();
		this.home.getRoomAt(this.home.entranceX + 1, this.home.entranceY + 1).closeLeftDoor();
		this.home.getRoomAt(this.home.entranceX, this.home.entranceY + 2).closeBottomDoor();
		
	}

	public void closeLeftDoor() {
		this.getBlocks().set(leftLoc, this.leftWall);
		if (xInMap - 1 >= 0 && !(home.exit.direction.equals(Direction.LEFT) && home.inEntranceRoom())) {
			Room otherRoom = home.getRoomAt(this.xInMap - 1, this.yInMap);
			otherRoom.getBlocks().set(otherRoom.rightLoc, otherRoom.rightWall);
		}
	}

	public void closeRightDoor() {
		this.getBlocks().set(rightLoc, this.rightWall);
		if (xInMap + 1 < home.dungeonWidth && !(home.exit.direction.equals(Direction.RIGHT) && home.inEntranceRoom())) {
			Room otherRoom = home.getRoomAt(this.xInMap + 1, this.yInMap);
			otherRoom.getBlocks().set(otherRoom.leftLoc, otherRoom.leftWall);
		}

	}

	public void closeBottomDoor() {
		this.getBlocks().set(bottomLoc, this.bottomWall);
		if (yInMap - 1 >= 0 && !(home.exit.direction.equals(Direction.DOWN) && home.inEntranceRoom())) {
			Room otherRoom = home.getRoomAt(this.xInMap, this.yInMap-1);
			otherRoom.getBlocks().set(otherRoom.topLoc, otherRoom.topWall);
		}
	}

	public void closeTopDoor() {
		this.getBlocks().set(topLoc, this.topWall);
		if (yInMap + 1 < home.dungeonHeight && !(home.exit.direction.equals(Direction.UP) && home.inEntranceRoom())) {
			Room otherRoom = home.getRoomAt(this.xInMap, this.yInMap+1);
			otherRoom.getBlocks().set(otherRoom.bottomLoc, otherRoom.bottomWall);
		}
	}

	public void openLeftDoor() {
		if (xInMap - 1 >= 0 && !(home.exit.direction.equals(Direction.LEFT) && home.inEntranceRoom())) {
			this.getBlocks().set(leftLoc, this.leftDoor);
			Room otherRoom = home.getRoomAt(this.xInMap - 1, this.yInMap);
			otherRoom.getBlocks().set(otherRoom.rightLoc, otherRoom.rightDoor);
		}

	}

	public void openRightDoor() {
		if (xInMap + 1 < home.dungeonWidth && !(home.exit.direction.equals(Direction.RIGHT) && home.inEntranceRoom())) {
			this.getBlocks().set(rightLoc, this.rightDoor);
			Room otherRoom = home.getRoomAt(this.xInMap + 1, this.yInMap);
			otherRoom.getBlocks().set(otherRoom.leftLoc, otherRoom.leftDoor);
		}

	}

	public void openBottomDoor() {
		if (yInMap - 1 >= 0 && !(home.exit.direction.equals(Direction.DOWN) && home.inEntranceRoom())) {
			this.getBlocks().set(bottomLoc, this.bottomDoor);
			Room otherRoom = home.getRoomAt(this.xInMap, this.yInMap-1);
			otherRoom.getBlocks().set(otherRoom.topLoc, otherRoom.topDoor);
		}

	}

	public void openTopDoor() {
		if (yInMap + 1 < home.dungeonHeight && !(home.exit.direction.equals(Direction.UP) && home.inEntranceRoom())) {
			this.getBlocks().set(topLoc, this.topDoor);
			Room otherRoom = home.getRoomAt(this.xInMap, this.yInMap+1);
			otherRoom.getBlocks().set(otherRoom.bottomLoc, otherRoom.bottomDoor);
		}

	}

	public void lockLeftDoor() {
		if (xInMap - 1 >= 0 && !(home.exit.direction.equals(Direction.LEFT) && home.inEntranceRoom())) {
			this.getBlocks().set(leftLoc, new Item(leftX, roomHeight % 2 == 0 ? roomHeight / 2 - 1 : roomHeight / 2,
					"lockedleft", home.leftLockImage, handler));
			Room otherRoom = home.getRoomAt(xInMap-1, yInMap);
			otherRoom.getBlocks().set(rightLoc, new Item(rightX, roomHeight % 2 == 0 ? roomHeight / 2 - 1 : roomHeight / 2,
					"lockedright", home.rightLockImage, handler));
		}

	}

	public void lockRightDoor() {
		if (xInMap + 1 < home.dungeonWidth && !(home.exit.direction.equals(Direction.RIGHT) && home.inEntranceRoom())) {
			this.getBlocks().set(rightLoc, new Item(rightX, roomHeight % 2 == 0 ? roomHeight / 2 - 1 : roomHeight / 2,
					"lockedright", home.rightLockImage, handler));
			Room otherRoom = home.getRoomAt(xInMap+1, yInMap);
			otherRoom.getBlocks().set(leftLoc, new Item(leftX, roomHeight % 2 == 0 ? roomHeight / 2 - 1 : roomHeight / 2,
					"lockedleft", home.leftLockImage, handler));

		}

	}

	public void lockBottomDoor() {
		if (yInMap - 1 >= 0 && !(home.exit.direction.equals(Direction.DOWN) && home.inEntranceRoom())) {
			this.getBlocks().set(bottomLoc, new Item(roomWidth % 2 == 0 ? roomWidth / 2 : roomWidth / 2 - 1, bottomY,
					"lockedbottom", home.bottomLockImage, handler));
			Room otherRoom = home.getRoomAt(xInMap, yInMap-1);
			otherRoom.getBlocks().set(otherRoom.topLoc, new Item(roomWidth % 2 == 0 ? roomWidth / 2 : roomWidth / 2 - 1, topY,
					"lockedup", home.topLockImage, handler));
		}

	}

	public void lockTopDoor() {
		if (yInMap + 1 < home.dungeonHeight && !(home.exit.direction.equals(Direction.UP) && home.inEntranceRoom())) {
			this.getBlocks().set(topLoc, new Item(roomWidth % 2 == 0 ? roomWidth / 2 : roomWidth / 2 - 1, topY,
					"lockedup", home.topLockImage, handler));
			Room otherRoom = home.getRoomAt(xInMap, yInMap+1);
			otherRoom.getBlocks().set(otherRoom.bottomLoc, new Item(roomWidth % 2 == 0 ? roomWidth / 2 : roomWidth / 2 - 1, bottomY,
					"lockedbottom", home.bottomLockImage, handler));
		}

	}

	public void refresh() {
		initRoom();
	}

}
