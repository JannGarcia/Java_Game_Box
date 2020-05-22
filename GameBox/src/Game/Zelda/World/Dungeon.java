package Game.Zelda.World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Game.GameStates.State;
import Game.GameStates.Zelda.ZeldaGameState;
import Game.Zelda.Entities.Dynamic.Link;
import Game.Zelda.Entities.Statics.DungeonDoor;
import Main.Handler;
import Resources.Images;
import Resources.MusicHandler;

public abstract class Dungeon extends State {
	
	protected int dungeonWidth, dungeonHeight;
	protected int entranceX , entranceY;
	protected int roomX, roomY;
	public static int roomWidth=16, roomHeight=11;
	protected String name;
	protected DungeonDoor exit;
	protected ArrayList<ArrayList<Room>> rooms;
	protected Handler handler;
	protected MusicHandler musicHandler;
	protected ZeldaGameState zeldaState;
	protected Link link;
	public String DUNGEON_MUSIC = "dungeon.wav";
	
	public BufferedImage leftDoorImage = Images.zeldaTiles.get(5);
	public BufferedImage rightDoorImage = Images.zeldaTiles.get(11);
	public BufferedImage topDoorImage = Images.zeldaTiles.get(1);
	public BufferedImage bottomDoorImage = Images.zeldaTiles.get(16);
	public BufferedImage leftWallImage = Images.zeldaTiles.get(6);
	public BufferedImage rightWallImage = Images.zeldaTiles.get(10);
	public BufferedImage topWallImage = Images.zeldaTiles.get(0);
	public BufferedImage bottomWallImage = Images.zeldaTiles.get(15);
	public BufferedImage leftLockImage = Images.zeldaTiles.get(7);
	public BufferedImage rightLockImage = Images.zeldaTiles.get(12);
	public BufferedImage bottomLockImage = Images.zeldaTiles.get(17);
	public BufferedImage topLockImage = Images.zeldaTiles.get(2);
	public BufferedImage cornerTile = Images.zeldaTiles.get(26);
	public BufferedImage floorTile = Images.zeldaTiles.get(26);
	
	public Dungeon(Handler handler, int startX, int startY, int width, int height, DungeonDoor exit, String name) {
		super(handler);
		
		if(startX >= width) {throw new IllegalArgumentException("Start X in dungeon " + name + " is bigger or equal than width (" + startX + ">=" + width + ")");}
		if (startY >= height) {throw new IllegalArgumentException("Start Y in dungeon " + name + " is bigger or equal than height (" + startY + ">=" + height + ")");}
		this.zeldaState = handler.getZeldaGameState();
		this.musicHandler = handler.getMusicHandler();
		this.link = zeldaState.link;
		this.roomX = startX; this.entranceX = startX;
		this.roomY = startY; this.entranceY = startY;
		this.dungeonWidth = width; this.dungeonHeight = height;
		this.name = name; this.exit = exit;
		this.exit.name = "dungeon" + this.name + "Exit";
		refresh();
	}
	
	@Override
	public void tick() {
		zeldaState.tick();
	}
	
	@Override
	public void render(Graphics g) {
		// Drawing Floor
		for (int x = 0; x <= roomWidth+1; x++) {
			for (int y = -1; y <= roomHeight+1; y++) {
				int x1 = (x * ZeldaGameState.stageWidth/16) + ZeldaGameState.xOffset;
				int y1 = (y * ZeldaGameState.stageHeight/11) + ZeldaGameState.yOffset;
				g.drawImage(floorTile, x1, y1, (floorTile.getWidth()+2) * ZeldaGameState.worldScale, (floorTile.getHeight()+1) * ZeldaGameState.worldScale, null);
			}
		}
		zeldaState.render(g);
//		g.drawString("X: " + roomX, 100, 100);
//		g.drawString("Y: " + roomY, 100, 150);
	}
	
	@Override
	public void refresh() {
		
		rooms = new ArrayList<>();

		// Create all the rooms :)
		for (int i =0;i<dungeonWidth;i++){
			rooms.add(new ArrayList<>());
			for (int j =0;j<dungeonHeight;j++) {
				Room room = new Room(handler,this);
				room.xInMap = i; room.yInMap = j;
				rooms.get(i).add(room);
			}
		}
		
		
		// Close all the top and right doors
		for (int i = 0; i < dungeonHeight; i++) {
			this.getRoomAt(0,i).closeLeftDoor();
			this.getRoomAt(dungeonWidth-1,i).closeRightDoor();			
		}
		
		for (int i = 0; i < dungeonWidth; i++) {
			this.getRoomAt(i,0).closeBottomDoor();
			this.getRoomAt(i,dungeonHeight - 1).closeTopDoor();
		}
		
		// Set the entrance
		switch(this.exit.direction) {
		case DOWN:
			this.getRoomAt(this.entranceX, this.entranceY).setBottomEntrance(this.exit);
			break;
		case LEFT:
			this.getRoomAt(this.entranceX, this.entranceY).setLeftEntrance(this.exit);
			break;
		case RIGHT:
			this.getRoomAt(this.entranceX, this.entranceY).setRightEntrance(this.exit);
			break;
		case UP:
			this.getRoomAt(this.entranceX, this.entranceY).setTopEntrance(this.exit);
			break;
		}

		this.roomX = entranceX; this.roomY = entranceY;
		addDungeonObjects();
		addDungeonEnemies();
		addExtraDoors();
	}
	
	
	/**
	 * In this function, you should add decoration and items. <br>
	 * THESE WILL NOT BE REFRESHED WHEN LINK ENTERS A ROOM <br>
	 */
	public abstract void addDungeonObjects();
	
	/**
	 * In this function, add all the entities to their respective rooms <br>
	 * These entities will be refreshed every time link moves room. <br>
	 */
	public abstract void addDungeonEnemies();
	
	
	/**
	 *  In this function, close all the doors you need to :) <br>
	 */
	public abstract void addExtraDoors();
	
	public ArrayList<Room> getAllRooms(){
		ArrayList<Room> toReturn = new ArrayList<>();
		
		for (int i = 0; i < dungeonWidth; i++) {
			for (int k = 0; k < dungeonHeight; k++) {
				toReturn.add(getRoomAt(i,k));
			}
		}
		
		return toReturn;
	}
	
	public DungeonDoor getExit() {return this.exit;}
	
	public void clearAllEnemies() {for (Room room : getAllRooms()) {room.clearEntities();}}
	
	public Room getRoomAt(int x, int y) {
		try {return this.rooms.get(x).get(y);}
		catch (IndexOutOfBoundsException e) {/*System.out.println("Dummy door returned");*/ return new Room(handler, this);}
	}
	
	public Room getCurrentRoom() {return this.getRoomAt(this.roomX, this.roomY);}
	
	public void moveUp() {roomY = roomY + 1 >= dungeonHeight ? roomY : roomY+1; this.clearAllEnemies(); addDungeonEnemies();}
	public void moveDown() {roomY = roomY - 1 < 0 ? 0 : roomY-1; this.clearAllEnemies(); addDungeonEnemies();}
	public void moveLeft() {roomX = roomX - 1 < 0 ? 0 : roomX-1; this.clearAllEnemies(); addDungeonEnemies();}
	public void moveRight() {roomX = roomX + 1 > dungeonWidth ? roomX : roomX+1; this.clearAllEnemies(); addDungeonEnemies();}
	
	public String getName() {return this.name;}
	public ArrayList<ArrayList<Room>> getRooms(){return this.rooms;}
	public int getX() {return this.roomX;}
	public int getY() {return this.roomY;}
	public String getSong() {return this.DUNGEON_MUSIC;}
	public boolean inEntranceRoom() {return this.roomX == entranceX && this.roomY == entranceY;}

}
