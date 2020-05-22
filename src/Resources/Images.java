package Resources;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Created by AlexVR on 1/24/2020.
 */
public class Images {


	public static BufferedImage titleScreenBackground;
	public static BufferedImage pauseBackground;
	public static BufferedImage selectionBackground;
	public static BufferedImage galagaCopyright;
	public static BufferedImage galagaSelect;
	public static BufferedImage muteIcon;
	public static BufferedImage galagaPlayerLaser;
	public static BufferedImage reflectedLaser;
	public static BufferedImage galagaEnemyLaser;
    public static BufferedImage pacmanGameOver;
	public static BufferedImage[] startGameButton;
	public static BufferedImage[] galagaLogo;
	public static BufferedImage[] pauseResumeButton;
	public static BufferedImage[] pauseToTitleButton;
	public static BufferedImage[] pauseOptionsButton;
	public static BufferedImage[] galagaPlayer;
	public static BufferedImage[] galagaPlayerDeath;
	public static BufferedImage[] galagaEnemyDeath;
	public static BufferedImage[] galagaEnemyBee;
    public static BufferedImage[] galagaNewEnemy;
    public static BufferedImage[] galagaEnemyButterfly;
    public static BufferedImage[] galagaEnemyBomber;
    public static BufferedImage[] KirbyPowerUp;
	
	/////////////////////////////////////////////////////////////////////////

	public static BufferedImage map1;
	public static BufferedImage ghost;
	public static BufferedImage[] pacmanDots;
	public static BufferedImage[] pacmanRight;
	public static BufferedImage[] pacmanLeft;
	public static BufferedImage[] pacmanUp;
	public static BufferedImage[] pacmanDown;
	public static BufferedImage[] bound;
	public static BufferedImage[] heman;
	public static BufferedImage intro;
	public static BufferedImage start;

    public static BufferedImage TEST_MAP;
    public static BufferedImage originalMap;
    public static BufferedImage map4;
    public static BufferedImage map5;
    public static BufferedImage inso_rocks;
    public static BufferedImage bonusStage;
    public static BufferedImage mapDerek;
    public static BufferedImage mapAle;
    public static BufferedImage spawnerGate;
    public static BufferedImage spawnerGateVer;
    public static BufferedImage confetti;
    

	public static BufferedImage[] blinkyRight;
	public static BufferedImage[] blinkyLeft;
	public static BufferedImage[] blinkyUp;
	public static BufferedImage[] blinkyDown;
	public static BufferedImage[] pinkyRight;
	public static BufferedImage[] pinkyLeft;
	public static BufferedImage[] pinkyUp;
	public static BufferedImage[] pinkyDown;
	public static BufferedImage[] inkyRight;
	public static BufferedImage[] inkyLeft;
	public static BufferedImage[] inkyUp;
	public static BufferedImage[] inkyDown;
	public static BufferedImage[] clydeRight;
	public static BufferedImage[] clydeLeft;
	public static BufferedImage[] clydeUp;
	public static BufferedImage[] clydeDown;
	public static BufferedImage[] turnToBlue;
    public static BufferedImage eyesRight;
    public static BufferedImage eyesLeft;
    public static BufferedImage eyesUp;
    public static BufferedImage eyesDown;

    // items
    public static BufferedImage[] pacmanFruits;
    public static BufferedImage[] pacmanDeath;
    public static BufferedImage[] transportBlock;

	public static BufferedImage pacmanImageSheet;
	public SpriteSheet pacmanSpriteSheet;
    
    
    
    ///////////////////////////////////////////////////////////////////////////////


	public static BufferedImage galagaImageSheet;
	public SpriteSheet galagaSpriteSheet;


	public static BufferedImage heManImage;
	public static BufferedImage heManAttack;
	public SpriteSheet heManSheet;
	public SpriteSheet heManAttackSheet;

	public static BufferedImage zeldaImageSheet;
	public static BufferedImage storyImageSheet;
	public static BufferedImage credits;

	public SpriteSheet zeldaSpriteSheet;
	public SpriteSheet npcSpriteSheet;
	public SpriteSheet storySpriteSheet;
	public static BufferedImage zeldaTriforceLogo;
	public static BufferedImage zeldaMap;

	public static BufferedImage space;

	public static BufferedImage oldMan;
	public static BufferedImage zeldaFire;
	public static ArrayList<BufferedImage> zeldaTiles;
	public static BufferedImage[] zeldaTitleFrames;
	public static BufferedImage[] zeldaStoryFrames;
	public static BufferedImage zeldaWorldLayoutTileImage;
	public SpriteSheet zeldaWorldLayoutTileSpriteSheet;
	public static ArrayList<BufferedImage> zeldaWorldLayoutTiles;

	public static BufferedImage zeldaLinkImage;
	public SpriteSheet zeldaLinkSpriteSheet;
	public static BufferedImage[] zeldaLinkFrames;
	public static BufferedImage linkHoldingItem;
	public static BufferedImage linkHoldingItem2;

	public static ArrayList<BufferedImage> forestTiles;
	public static ArrayList<BufferedImage> caveTiles;
	public static ArrayList<BufferedImage> mountainTiles;
	public static ArrayList<BufferedImage> graveTiles;
	public static ArrayList<BufferedImage> moveTiles;

	public static BufferedImage EnemyOverwoldImage;
	public SpriteSheet EnemyOverwoldSpriteSheet;
	public static BufferedImage[] bouncyEnemyFrames;
	public static BufferedImage[] blueTektike;
	public static BufferedImage[] snake;
	public static BufferedImage npcSprite;
	

	public static BufferedImage linkSword;
	public static BufferedImage secondSword;
	public static BufferedImage masterSword;
	public static BufferedImage linkSwordRight;
	public static BufferedImage secondSwordRight;
	public static BufferedImage masterSwordRight;
	public static BufferedImage linkSwordDown;
	public static BufferedImage secondSwordDown;
	public static BufferedImage masterSwordDown;
	public static BufferedImage linkSwordLeft;
	public static BufferedImage secondSwordLeft;
	public static BufferedImage masterSwordLeft;

	public static BufferedImage[] linkAttackUp, linkAttackDown, linkAttackLeft, linkAttackRight;
	public static BufferedImage[] linkAttackUpSecond, linkAttackDownSecond, linkAttackLeftSecond, linkAttackRightSecond;
	public static BufferedImage[] linkAttackUpMaster, linkAttackDownMaster, linkAttackLeftMaster, linkAttackRightMaster;
	public static BufferedImage[] linkDamage, linkDamagePhase2;
	public static BufferedImage[] TektiteDamage;
	public static BufferedImage[] DarknutUp;
	public static BufferedImage[] DarknutDown;
	public static BufferedImage[] DarknutRight;
	public static BufferedImage[] DarknutLeft;
	public static BufferedImage[] DarknutDamageUp;
	public static BufferedImage[] DarknutDamageDown;
	public static BufferedImage[] DarknutDamageRight;
	public static BufferedImage[] DarknutDamageLeft;
	public static BufferedImage[] donnelIdle;
	public static BufferedImage[] donnelAttackLeft;
	public static BufferedImage[] donnelAttackRight;
	public static BufferedImage[] donnelUp;
	public static BufferedImage[] donnelDown;
	public static BufferedImage[] donnelLeft;
	public static BufferedImage[] donnelRight;
	public static SpriteSheet donnelSheet;
	
	// Bill's sprite array works different. Each index is a different color!
	public static BufferedImage[] billUp;
	public static BufferedImage[] billDown;
	public static BufferedImage[] billLeft;
	public static BufferedImage[] billRight;
	public static BufferedImage[] billWithBlueEye;
	public static BufferedImage[] billLookingLeft;
	public static BufferedImage[] billLookingRight;
	public static BufferedImage[] billSummon;
	public static BufferedImage[] billSummonBack;
	public static BufferedImage[][] billFireAttack;
	public static BufferedImage[] billFire;
	public static BufferedImage[][] billEyeThrow;
	public static BufferedImage[][] billEyeGrowth;
	public static BufferedImage billSymbol;
	public static BufferedImage billEye;
	public static BufferedImage billEye2;
	public static SpriteSheet billSheet;

	//CREEPER


	public static BufferedImage[] CreeperDown; 
	public static BufferedImage[] CreeperUp; 
	public static BufferedImage[] CreeperUpRight; 
	public static BufferedImage[] CreeperRight; 
	public static BufferedImage[] CreeperLeft;

	public static BufferedImage[] CreeperDamageUp; 
	public static BufferedImage[] CreeperDamageDown;
	public static BufferedImage[] CreeperDamageRight; 
	public static BufferedImage[] CreeperDamageLeft;

	public static BufferedImage[] CreeperExplosionLeft;
	public static BufferedImage[] CreeperExplosionRight;

	public static BufferedImage[] LinkBow;
	public static BufferedImage[] linkArrow;
	public static BufferedImage itemSheet;
	public static SpriteSheet itemSpriteSheet;
	public static BufferedImage triForce[];
	public static BufferedImage linkHeart;
	public static BufferedImage linkHalfHeart;
	public static BufferedImage linkEmptyHeart;
	public static BufferedImage linkPotion;
	public static BufferedImage linkKey;
	public static BufferedImage linkKey2;
	public static BufferedImage heartContainer;
	public static BufferedImage rupee, blueRupee;
	public static BufferedImage[] rupeeFrames;
	public static BufferedImage[] profOak;
	public static BufferedImage invisibleBlock;
	
	public static BufferedImage bronze,silver,gold;

	public static BufferedImage enemy3Sheet;
	public static SpriteSheet enemy3SpriteSheet;

	public static BufferedImage enemy2Sheet;
	public static SpriteSheet enemy2SpriteSheet;

	public static BufferedImage oakSheet;
	public static BufferedImage oakRoom;
	public static SpriteSheet oakSpriteSheet;
	public static SpriteSheet pokeBallSheet;
	public static BufferedImage pokeBall, loveBall;

	public static BufferedImage[] burgerPants;
	public static BufferedImage burgerArms;
	public static SpriteSheet burgerSheet;
	public static BufferedImage sansEye;


	public static BufferedImage ZeldaEnemy2;
	public static SpriteSheet ZeldaEnemy2Sprite;
	public static BufferedImage storeBG;

	public static BufferedImage bowSheet;
	public static SpriteSheet bowSpriteSheet;

	public static BufferedImage creeperSheet;
	public static SpriteSheet creeperSpriteSheet;

	public Images() {

		startGameButton = new BufferedImage[3];
		pauseResumeButton = new BufferedImage[2];
		pauseToTitleButton = new BufferedImage[2];
		pauseOptionsButton = new BufferedImage[2];
		galagaLogo = new BufferedImage[3];
		galagaPlayer = new BufferedImage[8];//not full yet, only has second to last image on sprite sheet
		galagaPlayerDeath = new BufferedImage[8];
		galagaEnemyDeath = new BufferedImage[5];
        galagaEnemyButterfly = new BufferedImage[2];
        galagaEnemyBomber = new BufferedImage[2];
		galagaEnemyBee = new BufferedImage[8];
        galagaNewEnemy = new BufferedImage[8];
    	KirbyPowerUp = new BufferedImage[2];

		pacmanDots = new BufferedImage[2];
		pacmanRight = new BufferedImage[2];
		pacmanLeft = new BufferedImage[2];
		pacmanUp = new BufferedImage[2];
		pacmanDown = new BufferedImage[2];
        pacmanDeath = new BufferedImage[11];
        transportBlock = new BufferedImage[2];
        pacmanFruits = new BufferedImage[5];

		heman = new BufferedImage[5];
		bound = new BufferedImage[16];
		
		billUp = new BufferedImage[4];
		billDown = new BufferedImage[4];
		billLeft = new BufferedImage[4];
		billRight = new BufferedImage[4];
		billWithBlueEye = new BufferedImage[4];
		billLookingLeft = new BufferedImage[4];
		billLookingRight = new BufferedImage[4];
		billSummon = new BufferedImage[4];;
		billSummonBack = new BufferedImage[4];
		billFireAttack = new BufferedImage[4][10];
		billEyeThrow = new BufferedImage[4][8];
		billEyeGrowth = new BufferedImage[4][6];
		billFire = new BufferedImage[2];

		blinkyRight = new BufferedImage[2];
		blinkyLeft = new BufferedImage[2];
		blinkyUp = new BufferedImage[2];
		blinkyDown = new BufferedImage[2];
		pinkyRight = new BufferedImage[2];
		pinkyLeft = new BufferedImage[2];
		pinkyUp = new BufferedImage[2];
		pinkyDown = new BufferedImage[2];
		inkyRight = new BufferedImage[2];
		inkyLeft = new BufferedImage[2];
		inkyUp = new BufferedImage[2];
		inkyDown = new BufferedImage[2];
		clydeRight = new BufferedImage[2];
		clydeLeft = new BufferedImage[2];
		clydeUp = new BufferedImage[2];
		clydeDown = new BufferedImage[2];
		turnToBlue = new BufferedImage[4];

		zeldaTiles = new ArrayList<>();
		zeldaTitleFrames = new BufferedImage[6];
		zeldaStoryFrames = new BufferedImage[8];
		zeldaWorldLayoutTiles = new ArrayList<>();

		forestTiles = new ArrayList<>();
		caveTiles = new ArrayList<>();
		graveTiles = new ArrayList<>();
		mountainTiles = new ArrayList<>();
		moveTiles = new ArrayList<>();

		zeldaLinkFrames = new BufferedImage[8];
		rupeeFrames = new BufferedImage[2];

		// ATTACKING ANIMATIONS
		linkAttackUp = new BufferedImage[4];
		linkAttackDown = new BufferedImage[4];
		linkAttackRight = new BufferedImage[4];
		linkAttackLeft = new BufferedImage[4];
		linkAttackUpSecond = new BufferedImage[4];
		linkAttackDownSecond = new BufferedImage[4];
		linkAttackRightSecond = new BufferedImage[4];
		linkAttackLeftSecond = new BufferedImage[4];
		linkAttackUpMaster = new BufferedImage[4];
		linkAttackDownMaster = new BufferedImage[4];
		linkAttackRightMaster = new BufferedImage[4];
		linkAttackLeftMaster = new BufferedImage[4];

		DarknutDown = new BufferedImage[2];
		DarknutUp = new BufferedImage[1];
		DarknutRight = new BufferedImage[2];
		DarknutLeft = new BufferedImage[2];

		DarknutDamageUp = new BufferedImage[1];
		DarknutDamageDown = new BufferedImage[2];
		DarknutDamageRight = new BufferedImage[2];
		DarknutDamageLeft = new BufferedImage[2];

		CreeperDown = new BufferedImage[3];
		CreeperUp = new BufferedImage[3];
		CreeperUpRight = new BufferedImage[3];
		CreeperRight = new BufferedImage[3];
		CreeperLeft = new BufferedImage[3];

		CreeperDamageUp = new BufferedImage[2];
		CreeperDamageDown = new BufferedImage[2];
		CreeperDamageRight = new BufferedImage[2];
		CreeperDamageLeft = new BufferedImage[2];

		CreeperExplosionLeft = new BufferedImage[8];
		CreeperExplosionRight = new BufferedImage[8];

		donnelIdle = new BufferedImage[4];
		donnelAttackLeft = new BufferedImage[4];
		donnelAttackRight = new BufferedImage[4];
		donnelUp = new BufferedImage[4];
		donnelDown = new BufferedImage[4];
		donnelLeft = new BufferedImage[4];
		donnelRight = new BufferedImage[4];

		burgerPants = new BufferedImage[7];

		linkDamage = new BufferedImage[5];
		linkDamagePhase2 = new BufferedImage[3];
		TektiteDamage = new BufferedImage[2];

		linkArrow = new BufferedImage[4];
		LinkBow = new BufferedImage[3];
		bouncyEnemyFrames = new BufferedImage[2];
		blueTektike = new BufferedImage[2];
		snake = new BufferedImage[2];

		profOak = new BufferedImage[4];
		
		triForce = new BufferedImage[2];



		try {

			startGameButton[0]= loadImage("/UI/Buttons/Start/NormalStartButton.png");
			startGameButton[1]= loadImage("/UI/Buttons/Start/HoverStartButton.png");
			startGameButton[2]= loadImage("/UI/Buttons/Start/ClickedStartButton.png");

			titleScreenBackground = loadImage("/UI/Backgrounds/Title.png");

			pauseBackground = loadImage("/UI/Backgrounds/Pause.png");

			selectionBackground = loadImage("/UI/Backgrounds/Selection.png");

			galagaCopyright = loadImage("/UI/Misc/Copyright.png");

			galagaSelect = loadImage("/UI/Misc/galaga_select.png");

			muteIcon = loadImage("/UI/Misc/mute.png");

			galagaLogo[0] = loadImage("/UI/Misc/galaga_logo.png");
			galagaLogo[1] = loadImage("/UI/Buttons/Selection/Galaga/hover_galaga_logo.png");
			galagaLogo[2] = loadImage("/UI/Buttons/Selection/Galaga/pressed_galaga_logo.png");

			pauseResumeButton[0] = loadImage("/UI/Buttons/Pause/Resume/NormalHoverResume.png");
			pauseResumeButton[1] = loadImage("/UI/Buttons/Pause/Resume/PressedResume.png");

			pauseToTitleButton[0] = loadImage("/UI/Buttons/Pause/ToTitle/NormalHoverToTitleButton.png");
			pauseToTitleButton[1] = loadImage("/UI/Buttons/Pause/ToTitle/PressedToTitleButton.png");

			pauseOptionsButton[0] = loadImage("/UI/Buttons/Pause/ToOptions/NormalHoverToOptionsButton.png");
			pauseOptionsButton[1] = loadImage("/UI/Buttons/Pause/ToOptions/PressedToOptionsButton.png");

			galagaImageSheet = loadImage("/UI/SpriteSheets/Galaga/Galaga.png");
			galagaSpriteSheet = new SpriteSheet(galagaImageSheet);

			galagaPlayer[0] = galagaSpriteSheet.crop(160,55,15,16);
			
            KirbyPowerUp[0] = ImageIO.read(getClass().getResourceAsStream("/UI/SpriteSheets/Galaga/KirbyWarp.png"));
            KirbyPowerUp[1] = ImageIO.read(getClass().getResourceAsStream("/UI/SpriteSheets/Galaga/KirbyWarpDim.png"));

			galagaPlayerDeath[0] = galagaSpriteSheet.crop(209,48,32,32);
			galagaPlayerDeath[1] = galagaSpriteSheet.crop(209,48,32,32);
			galagaPlayerDeath[2] = galagaSpriteSheet.crop(247,48,32,32);
			galagaPlayerDeath[3] = galagaSpriteSheet.crop(247,48,32,32);
			galagaPlayerDeath[4] = galagaSpriteSheet.crop(288,47,32,32);
			galagaPlayerDeath[5] = galagaSpriteSheet.crop(288,47,32,32);
			galagaPlayerDeath[6] = galagaSpriteSheet.crop(327,47,32,32);
			galagaPlayerDeath[7] = galagaSpriteSheet.crop(327,47,32,32);

			galagaEnemyDeath[0] = galagaSpriteSheet.crop(201,191,32,32);
			galagaEnemyDeath[1] = galagaSpriteSheet.crop(223,191,32,32);
			galagaEnemyDeath[2] = galagaSpriteSheet.crop(247,191,32,32);
			galagaEnemyDeath[3] = galagaSpriteSheet.crop(280,191,32,32);
			galagaEnemyDeath[4] = galagaSpriteSheet.crop(320,191,32,32);

			galagaEnemyBee[0] = galagaSpriteSheet.crop(188,178,9,10);
			galagaEnemyBee[1] = galagaSpriteSheet.crop(162,178,13,10);
			galagaEnemyBee[2] = galagaSpriteSheet.crop(139,177,11,12);
			galagaEnemyBee[3] = galagaSpriteSheet.crop(113,176,14,13);
			galagaEnemyBee[4] = galagaSpriteSheet.crop(90,177,13,13);
			galagaEnemyBee[5] = galagaSpriteSheet.crop(65,176,13,14);
			galagaEnemyBee[6] = galagaSpriteSheet.crop(42,178,12,11);
			galagaEnemyBee[7] = galagaSpriteSheet.crop(19,177,10,13);
			
            galagaNewEnemy[0] = galagaSpriteSheet.crop(188,154,9,10);
            galagaNewEnemy[1] = galagaSpriteSheet.crop(162,154,13,10);
            galagaNewEnemy[2] = galagaSpriteSheet.crop(139,153,11,12);
            galagaNewEnemy[3] = galagaSpriteSheet.crop(113,152,14,13);
            galagaNewEnemy[4] = galagaSpriteSheet.crop(90,153,13,13);
            galagaNewEnemy[5] = galagaSpriteSheet.crop(65,152,13,14);
            galagaNewEnemy[6] = galagaSpriteSheet.crop(42,154,12,11);
            galagaNewEnemy[7] = galagaSpriteSheet.crop(19,153,10,13);
            
            galagaEnemyButterfly[0] = galagaSpriteSheet.crop(188,154,9,10);
            galagaEnemyButterfly[1] = galagaSpriteSheet.crop(162,154,13,10);
            galagaEnemyBomber[0] = galagaSpriteSheet.crop(161,103,15,16);
            galagaEnemyBomber[1] = galagaSpriteSheet.crop(185,104,15,15);


			galagaPlayerLaser = galagaSpriteSheet.crop(365 ,219,3,8);
			reflectedLaser = createImageTransparent(loadImage("/UI/Misc/playerLaser.png"),"playerLaser", new Color(255,255,255).getRGB());
			galagaEnemyLaser = flipVertical(galagaPlayerLaser);

            pacmanImageSheet = ImageIO.read(getClass().getResourceAsStream("/UI/SpriteSheets/PacMan/BackgroundGreen.png"));
			pacmanImageSheet = createImageTransparent(pacmanImageSheet, "PacManBG", new Color(0,0,0).getRGB());
			pacmanSpriteSheet = new SpriteSheet(pacmanImageSheet);
			map1 = loadImage("/UI/Backgrounds/PacManMaps/map1.png");
            pacmanGameOver = loadImage("/UI/Misc/game_over.png");
			ghost = pacmanSpriteSheet.crop(456,64,16,16);
			pacmanDots[0] = pacmanSpriteSheet.crop(643,18,16,16);
			pacmanDots[1] = pacmanSpriteSheet.crop(623,18,16,16);

			bound[0] = pacmanSpriteSheet.crop(603,18,16,16);//single
			bound[1] = pacmanSpriteSheet.crop(615,37,16,16);//right open
			bound[2] = pacmanSpriteSheet.crop(635,37,16,16);//down open
			bound[3] = pacmanSpriteSheet.crop(655,37,16,16);//left open
			bound[4] = pacmanSpriteSheet.crop(655,57,16,16);//up open
			bound[5] = pacmanSpriteSheet.crop(655,75,16,16);//up/down
			bound[6] = pacmanSpriteSheet.crop(656,116,16,16);//left/Right
			bound[7] = pacmanSpriteSheet.crop(656,136,16,16);//up/Right
			bound[8] = pacmanSpriteSheet.crop(655,174,16,16);//up/left
			bound[9] = pacmanSpriteSheet.crop(655,155,16,16);//down/Right
			bound[10] = pacmanSpriteSheet.crop(655,192,16,16);//down/left
			bound[11] = pacmanSpriteSheet.crop(664,232,16,16);//all
			bound[12] = pacmanSpriteSheet.crop(479,191,16,16);//left
			bound[13] = pacmanSpriteSheet.crop(494,191,16,16);//right
			bound[14] = pacmanSpriteSheet.crop(479,208,16,16);//top
			bound[15] = pacmanSpriteSheet.crop(479,223,16,16);//bottom

			pacmanRight[0] = pacmanSpriteSheet.crop(473,1,12,13);
			pacmanRight[1] = pacmanSpriteSheet.crop(489,1,13,13);

			pacmanLeft[0] = pacmanSpriteSheet.crop(474,17,12,13);
			pacmanLeft[1] = pacmanSpriteSheet.crop(489,1,13,13);

			pacmanUp[0] = pacmanSpriteSheet.crop(473,34,13,12);
			pacmanUp[1] = pacmanSpriteSheet.crop(489,1,13,13);

			pacmanDown[0] = pacmanSpriteSheet.crop(473,48,13,12);
			pacmanDown[1] = pacmanSpriteSheet.crop(489,1,13,13);

			// ------------------ GHOSTS -----------------------

			// Blinky
			blinkyRight[0] = pacmanSpriteSheet.crop(457,65,14,13);
			blinkyRight[1] = pacmanSpriteSheet.crop(473,65,14,13);

			blinkyLeft[0] = pacmanSpriteSheet.crop(489,65,14,13);
			blinkyLeft[1] = pacmanSpriteSheet.crop(505,65,14,13);

			blinkyUp[0] = pacmanSpriteSheet.crop(521,65,14,13);
			blinkyUp[1] = pacmanSpriteSheet.crop(537,65,14,13);

			blinkyDown[0] = pacmanSpriteSheet.crop(553,65,14,13);
			blinkyDown[1] = pacmanSpriteSheet.crop(569,65,14,13);

			// Pinky
			pinkyRight[0] = pacmanSpriteSheet.crop(457,81,14,13);
			pinkyRight[1] = pacmanSpriteSheet.crop(473,81,14,13);

			pinkyLeft[0] = pacmanSpriteSheet.crop(489,81,14,13);
			pinkyLeft[1] = pacmanSpriteSheet.crop(505,81,14,13);

			pinkyUp[0] = pacmanSpriteSheet.crop(521,81,14,13);
			pinkyUp[1] = pacmanSpriteSheet.crop(537,81,14,13);

			pinkyDown[0] = pacmanSpriteSheet.crop(553,81,14,13);
			pinkyDown[1] = pacmanSpriteSheet.crop(569,81,14,13);

			// Inky
			inkyRight[0] = pacmanSpriteSheet.crop(457,97,14,13);
			inkyRight[1] = pacmanSpriteSheet.crop(473,97,14,13);

			inkyLeft[0] = pacmanSpriteSheet.crop(489,97,14,13);
			inkyLeft[1] = pacmanSpriteSheet.crop(505,97,14,13);

			inkyUp[0] = pacmanSpriteSheet.crop(521,97,14,13);
			inkyUp[1] = pacmanSpriteSheet.crop(537,97,14,13);

			inkyDown[0] = pacmanSpriteSheet.crop(553,97,14,13);
			inkyDown[1] = pacmanSpriteSheet.crop(569,97,14,13);


			// Clyde
			clydeRight[0] = pacmanSpriteSheet.crop(457,113,14,13);
			clydeRight[1] = pacmanSpriteSheet.crop(473,113,14,13);

			clydeLeft[0] = pacmanSpriteSheet.crop(489,113,14,13);
			clydeLeft[1] = pacmanSpriteSheet.crop(505,113,14,13);

			clydeUp[0] = pacmanSpriteSheet.crop(521,113,14,13);
			clydeUp[1] = pacmanSpriteSheet.crop(537,113,14,13);

			clydeDown[0] = pacmanSpriteSheet.crop(553,113,14,13);
			clydeDown[1] = pacmanSpriteSheet.crop(569,113,14,13);

			// TURN-TO-BLUE
			turnToBlue[0] = pacmanSpriteSheet.crop(585, 65, 14, 13);
			turnToBlue[1] = pacmanSpriteSheet.crop(601, 65, 14, 13);
			turnToBlue[2] = pacmanSpriteSheet.crop(617, 65, 14, 13);
			turnToBlue[3] = pacmanSpriteSheet.crop(633, 65, 14, 13);
			
            map1 = loadImage("/UI/Backgrounds/PacManMaps/map1.png");
            TEST_MAP = loadImage("/UI/Backgrounds/PacManMaps/map2.png");
            originalMap = loadImage("/UI/Backgrounds/PacManMaps/map3.png");
            map4 = loadImage("/UI/Backgrounds/PacManMaps/map4.png");
            map5 = loadImage("/UI/Backgrounds/PacManMaps/map5.png");
            inso_rocks = loadImage("/UI/Backgrounds/PacManMaps/dont_tell_anyone.png");
            mapDerek = loadImage("/UI/Backgrounds/PacManMaps/mapDerek.png");
            mapAle = loadImage("/UI/Backgrounds/PacManMaps/aleMap.png");
            bonusStage = loadImage("/UI/Backgrounds/PacManMaps/bonusStage.png");
            confetti = loadImage("/UI/Misc/confetti.png");
            
            // fruits
            pacmanFruits[0] = pacmanSpriteSheet.crop(490,50,12,13); // cherry
            pacmanFruits[1] = pacmanSpriteSheet.crop(506,50,11,13); // strawberry
            pacmanFruits[2] = pacmanSpriteSheet.crop(522,50,12,13); // Orange
            pacmanFruits[3] = pacmanSpriteSheet.crop(538,50,12,13); // Apple
            pacmanFruits[4] = pacmanSpriteSheet.crop(555,50,12,13); // watermelon
            
            // Eyes
            eyesRight = pacmanSpriteSheet.crop(585,81,14,13);
            eyesLeft = pacmanSpriteSheet.crop(601,81,14,13);
            eyesUp = pacmanSpriteSheet.crop(617,81,14,13);
            eyesDown = pacmanSpriteSheet.crop(633,81,14,13);
            
            
            spawnerGate = pacmanSpriteSheet.crop(332, 95, 2, 16);
            spawnerGateVer = pacmanSpriteSheet.crop(554, 150, 16, 16);
            
            
            pacmanDeath[0] = pacmanSpriteSheet.crop(505, 1, 13, 12);
            pacmanDeath[1] = pacmanSpriteSheet.crop(520, 1, 14, 12);
            pacmanDeath[2] = pacmanSpriteSheet.crop(536, 1, 14, 12);
            pacmanDeath[3] = pacmanSpriteSheet.crop(552, 1, 14, 12);
            pacmanDeath[4] = pacmanSpriteSheet.crop(568, 1, 14, 12);
            pacmanDeath[5] = pacmanSpriteSheet.crop(584, 1, 14, 12);
            pacmanDeath[6] = pacmanSpriteSheet.crop(600, 1, 14, 12);
            pacmanDeath[7] = pacmanSpriteSheet.crop(616, 1, 14, 12);
            pacmanDeath[8] = pacmanSpriteSheet.crop(632, 1, 14, 12);
            pacmanDeath[9] = pacmanSpriteSheet.crop(648, 1, 14, 12);
            pacmanDeath[10] = pacmanSpriteSheet.crop(664, 1, 14, 13);
            
            transportBlock[0] = pacmanSpriteSheet.crop(554, 167, 16, 16);
            transportBlock[1] = pacmanSpriteSheet.crop(554, 186, 16, 16);
             

			heManImage = loadImage("/UI/SpriteSheets/PacMan/heman.png");
			heManSheet = new SpriteSheet(heManImage);            

			heManAttack = loadImage("/UI/SpriteSheets/PacMan/hemanattack.png");
			heManAttackSheet = new SpriteSheet(heManAttack);

			heman[0] = heManSheet.crop(0, 11, 809, 1217);
			heman[1] = heManSheet.crop(865, 11, 809, 1217);
			heman[2] = heManAttackSheet.crop(69, 172, 526, 777);  // 69 => 595  172 => 949
			heman[3] = heManAttackSheet.crop(694, 172, 510, 777);	  // 694 => 1204    
			heman[4] = heManAttackSheet.crop(1303, 172, 549, 777);   // 1303 = > 1852

			intro = loadImage("/UI/SpriteSheets/PacMan/intro.png");
			start = loadImage("/UI/Backgrounds/startScreen.png");

			zeldaImageSheet = loadImage("/UI/Backgrounds/Zelda/tileSet.png");
			zeldaTriforceLogo = loadImage("/UI/Backgrounds/Zelda/triforceLogo.png");
			zeldaMap = loadImage("/UI/Backgrounds/Zelda/map.png");
			zeldaMap = createImageTransparent(zeldaMap,"zelddaMap_0,128,0,green",new Color(0,128,0).getRGB());
			zeldaImageSheet = createImageTransparent(zeldaImageSheet,"tileSets_0,120,0,green",new Color(0,128,0).getRGB());
			zeldaSpriteSheet = new SpriteSheet(zeldaImageSheet);

			npcSprite = loadImage("/UI/Backgrounds/Zelda/npc.png");

			ZeldaEnemy2 = loadImage("/UI/Backgrounds/Zelda/enemy2.png");            
			ZeldaEnemy2Sprite = new SpriteSheet(ZeldaEnemy2);
			
			credits = loadImage("/UI/Backgrounds/credits.png");

			// DARKNUT

			DarknutUp[0] = ZeldaEnemy2Sprite.crop(35, 90, 16, 16);

			DarknutDown[0] = ZeldaEnemy2Sprite.crop(1, 90, 16, 16);
			DarknutDown[1] = ZeldaEnemy2Sprite.crop(18, 90, 16, 16);

			DarknutRight[0] = ZeldaEnemy2Sprite.crop(52, 90, 16, 16);
			DarknutRight[1] = ZeldaEnemy2Sprite.crop(69, 90, 16, 16);

			DarknutLeft[0] = flipHorizontal(DarknutRight[0]);
			DarknutLeft[1] = flipHorizontal(DarknutRight[1]);

			DarknutDamageUp[0] = ZeldaEnemy2Sprite.crop(35, 107, 16, 16);

			DarknutDamageDown[0] = ZeldaEnemy2Sprite.crop(1, 107, 16, 16);
			DarknutDamageDown[1] = ZeldaEnemy2Sprite.crop(18, 107, 16, 16);

			DarknutDamageRight[0] = ZeldaEnemy2Sprite.crop(52, 107, 16, 16);
			DarknutDamageRight[1] = ZeldaEnemy2Sprite.crop(69, 107, 16, 16);

			DarknutDamageLeft[0] = flipHorizontal(DarknutDamageRight[0]);
			DarknutDamageLeft[1] = flipHorizontal(DarknutDamageRight[1]);

			// CREEPER
			creeperSheet = loadImage("/UI/Backgrounds/Zelda/Creeper.png");
			creeperSpriteSheet = new SpriteSheet(creeperSheet);

			CreeperLeft[0] = creeperSpriteSheet.crop(2, 29, 15, 24);
			CreeperLeft[1] = creeperSpriteSheet.crop(24, 29, 15, 24);
			CreeperLeft[2] = creeperSpriteSheet.crop(44, 29, 15, 24);

			CreeperRight[0] = flipHorizontal(CreeperLeft[0]);
			CreeperRight[1] = flipHorizontal(CreeperLeft[1]);
			CreeperRight[2] = flipHorizontal(CreeperLeft[2]);

			CreeperDown[0] = creeperSpriteSheet.crop(2, 1, 15, 24);
			CreeperDown[1] = creeperSpriteSheet.crop(23, 1, 15, 24);
			CreeperDown[2] = creeperSpriteSheet.crop(44, 1, 15, 24);

			CreeperUp[0] = creeperSpriteSheet.crop(5, 57, 15, 24);
			CreeperUp[1] = creeperSpriteSheet.crop(24, 57, 15, 24);
			CreeperUp[2] = creeperSpriteSheet.crop(45, 57, 15, 24);
			
			CreeperUpRight[0] = creeperSpriteSheet.crop(64, 58, 14, 22);
			CreeperUpRight[1] = creeperSpriteSheet.crop(84, 58, 14, 22);			
			CreeperUpRight[2] = creeperSpriteSheet.crop(104, 58, 14, 22);

			CreeperDamageLeft[0] = creeperSpriteSheet.crop(2, 193, 15, 24);
			CreeperDamageLeft[1] = creeperSpriteSheet.crop(44, 193, 15, 24);

			CreeperDamageRight[0] = flipHorizontal(CreeperDamageLeft[0]);
			CreeperDamageRight[1] = flipHorizontal(CreeperDamageLeft[1]);

			CreeperDamageDown[0] = creeperSpriteSheet.crop(2, 165, 14, 24);
			CreeperDamageDown[1] = creeperSpriteSheet.crop(44, 165, 14, 24);

			CreeperDamageUp[0] = creeperSpriteSheet.crop(5, 221, 14, 24);
			CreeperDamageUp[1] = creeperSpriteSheet.crop(45, 221, 14, 24);

			CreeperExplosionLeft[0] = creeperSpriteSheet.crop(123, 1, 18, 22);
			CreeperExplosionLeft[1] = creeperSpriteSheet.crop(142, 1, 18, 22);
			CreeperExplosionLeft[2] = creeperSpriteSheet.crop(161, 1, 18, 22);
			CreeperExplosionLeft[3] = creeperSpriteSheet.crop(179, 1, 18, 22);
			CreeperExplosionLeft[4] = creeperSpriteSheet.crop(121, 57, 13, 22);
			CreeperExplosionLeft[5] = creeperSpriteSheet.crop(134, 58, 21, 22);
			CreeperExplosionLeft[6] = creeperSpriteSheet.crop(155, 57, 26, 24);
			CreeperExplosionLeft[7] = creeperSpriteSheet.crop(181, 56, 23, 26);


			CreeperExplosionRight[0] = flipHorizontal(CreeperExplosionLeft[0]);
			CreeperExplosionRight[1] = flipHorizontal(CreeperExplosionLeft[1]);
			CreeperExplosionRight[2] = flipHorizontal(CreeperExplosionLeft[2]);
			CreeperExplosionRight[3] = flipHorizontal(CreeperExplosionLeft[3]);
			CreeperExplosionRight[4] = creeperSpriteSheet.crop(121, 57, 13, 22);
			CreeperExplosionRight[5] = creeperSpriteSheet.crop(134, 58, 21, 22);
			CreeperExplosionRight[6] = creeperSpriteSheet.crop(155, 57, 26, 24);
			CreeperExplosionRight[7] = creeperSpriteSheet.crop(181, 56, 23, 26);

			
			// BILL CYPHER
			billSheet =  new SpriteSheet(createImageTransparent(loadImage("/UI/Backgrounds/Zelda/bill.png"),"bill", new Color(0,162,232).getRGB()));

			billUp[0] = billSheet.crop(111, 10, 26, 39);
			billDown[0] = billSheet.crop(12,10,26,39);
			billLeft[0] = billSheet.crop(71, 13, 13, 35);
			billRight[0] = billSheet.crop(90, 13, 13, 35);
			billWithBlueEye[0] = billSheet.crop(42,10,25,39);
			billLookingLeft[0] = billSheet.crop(140,10,25,39);
			billLookingRight[0] = billSheet.crop(168,10,25,39);
			billSummon[0] = billSheet.crop(194,10,30,39);
			billSummonBack[0] = billSheet.crop(226,10,30,39);
			
			billUp[1] = billSheet.crop(111, 50, 26, 39);
			billDown[1] = billSheet.crop(12,50,26,39);
			billLeft[1] = billSheet.crop(71, 53, 13, 35);
			billRight[1] = billSheet.crop(90, 53, 13, 35);
			billWithBlueEye[1] = billSheet.crop(42,50,25,39);
			billLookingLeft[1] = billSheet.crop(140,50,25,39);
			billLookingRight[1] = billSheet.crop(168,50,25,39);
			billSummon[1] = billSheet.crop(194,50,30,39);
			billSummonBack[1] = billSheet.crop(226,50,30,39);
			
			billUp[2] = billSheet.crop(111, 132, 26, 39);
			billDown[2] = billSheet.crop(12,132,26,39);
			billLeft[2] = billSheet.crop(71, 135, 13, 35);
			billRight[2] = billSheet.crop(90, 135, 13, 35);
			billWithBlueEye[2] = billSheet.crop(42,132,25,39);
			billLookingLeft[2] = billSheet.crop(140,132,25,39);
			billLookingRight[2] = billSheet.crop(168,132,25,39);
			billSummon[2] = billSheet.crop(194,132,30,39);
			billSummonBack[2] = billSheet.crop(226,132,30,39);
			
			billUp[3] = billSheet.crop(111, 91, 26, 39);
			billDown[3] = billSheet.crop(12,91,26,39);
			billLeft[3] = billSheet.crop(71, 94, 13, 35);
			billRight[3] = billSheet.crop(90, 94, 13, 35);
			billWithBlueEye[3] = billSheet.crop(42,91,25,39);
			billLookingLeft[3] = billSheet.crop(140,91,25,39);
			billLookingRight[3] = billSheet.crop(168,91,25,39);
			billSummon[3] = billSheet.crop(194,91,30,39);
			billSummonBack[3] = billSheet.crop(226,91,30,39);
			
			
			billFireAttack[0][0] = billSheet.crop(261,10,293-261,39);
			billFireAttack[0][1] = billSheet.crop(298,10,329-298,42);
			billFireAttack[0][2] = billSheet.crop(452,10,329-298,42);
			billFireAttack[0][3] = billFireAttack[0][1];
			billFireAttack[0][4] = billFireAttack[0][2];
			billFireAttack[0][5] = billFireAttack[0][1];
			billFireAttack[0][6] = billFireAttack[0][2];
			billFireAttack[0][7] = billSheet.crop(334,10,372-334,42);
			billFireAttack[0][8] = billSheet.crop(375,10,413-375,45);
			billFireAttack[0][9] = billSheet.crop(415,10,447-415,45);
			
			billFireAttack[1][0] = billSheet.crop(261,56,293-261,39);
			billFireAttack[1][1] = billSheet.crop(298,56,329-298,42);
			billFireAttack[1][2] = billSheet.crop(452,56,329-298,42);
			billFireAttack[1][3] = billFireAttack[1][1];
			billFireAttack[1][4] = billFireAttack[1][2];
			billFireAttack[1][5] = billFireAttack[1][1];
			billFireAttack[1][6] = billFireAttack[1][2];
			billFireAttack[1][7] = billSheet.crop(334,56,372-334,42);
			billFireAttack[1][8] = billSheet.crop(375,56,413-375,45);
			billFireAttack[1][9] = billSheet.crop(415,56,447-415,45);
			
			billFireAttack[3][0] = billSheet.crop(261,103,293-261,39);
			billFireAttack[3][1] = billSheet.crop(298,103,329-298,42);
			billFireAttack[3][2] = billSheet.crop(452,103,329-298,42);
			billFireAttack[3][3] = billFireAttack[3][1];
			billFireAttack[3][4] = billFireAttack[3][2];
			billFireAttack[3][5] = billFireAttack[3][1];
			billFireAttack[3][6] = billFireAttack[3][2];
			billFireAttack[3][7] = billSheet.crop(334,103,372-334,42);
			billFireAttack[3][8] = billSheet.crop(375,103,413-375,45);
			billFireAttack[3][9] = billSheet.crop(415,103,447-415,45);
			
			billFireAttack[2][0] = billSheet.crop(261,150,293-261,39);
			billFireAttack[2][1] = billSheet.crop(298,150,329-298,42);
			billFireAttack[2][2] = billSheet.crop(452,150,329-298,42);
			billFireAttack[2][3] = billFireAttack[2][1];
			billFireAttack[2][4] = billFireAttack[2][2];
			billFireAttack[2][5] = billFireAttack[2][1];
			billFireAttack[2][6] = billFireAttack[2][2];			
			billFireAttack[2][7] = billSheet.crop(334,150,372-334,42);
			billFireAttack[2][8] = billSheet.crop(375,150,413-375,45);
			billFireAttack[2][9] = billSheet.crop(415,150,447-415,45);
			
			billFire[0] =  flipVertical(billSheet.crop(321, 11, 8, 11));
			billFire[1] =  flipVertical(billSheet.crop(475, 10, 8, 12));
			
			billEyeThrow[0][0] = billSheet.crop(489,10,518-489,39);
			billEyeThrow[0][1] = billSheet.crop(519,10,555-519,40);
			billEyeThrow[0][2] = billEyeThrow[0][0];
			billEyeThrow[0][3] = billEyeThrow[0][1];
			billEyeThrow[0][4] = billSheet.crop(562,10,597-562,40);
			billEyeThrow[0][5] = billSheet.crop(598,10,649-598,40);
			billEyeThrow[0][6] = billSheet.crop(641,10,673-641,43);
			billEyeThrow[0][7] = billSheet.crop(678,10,710-678,40);
			
			billEyeThrow[1][0] = billSheet.crop(489,56,518-489,39);
			billEyeThrow[1][1] = billSheet.crop(519,56,555-519,40);
			billEyeThrow[1][2] = billEyeThrow[1][0];
			billEyeThrow[1][3] = billEyeThrow[1][1];
			billEyeThrow[1][4] = billSheet.crop(562,56,597-562,40);
			billEyeThrow[1][5] = billSheet.crop(598,56,649-598,40);
			billEyeThrow[1][6] = billSheet.crop(641,56,673-641,43);
			billEyeThrow[1][7] = billSheet.crop(678,56,710-678,40);
			
			billEyeThrow[3][0] = billSheet.crop(489,103,518-489,39);
			billEyeThrow[3][1] = billSheet.crop(519,103,555-519,40);
			billEyeThrow[3][2] = billEyeThrow[3][0];
			billEyeThrow[3][3] = billEyeThrow[3][1];
			billEyeThrow[3][4] = billSheet.crop(562,103,597-562,40);
			billEyeThrow[3][5] = billSheet.crop(598,103,649-598,40);
			billEyeThrow[3][6] = billSheet.crop(641,103,673-641,43);
			billEyeThrow[3][7] = billSheet.crop(678,103,710-678,40);
			
			billEyeThrow[2][0] = billSheet.crop(489,150,518-489,39);
			billEyeThrow[2][1] = billSheet.crop(519,150,555-519,40);
			billEyeThrow[2][2] = billEyeThrow[2][0];
			billEyeThrow[2][3] = billEyeThrow[2][1];
			billEyeThrow[2][4] = billSheet.crop(562,150,597-562,40);
			billEyeThrow[2][5] = billSheet.crop(598,150,649-598,40);
			billEyeThrow[2][6] = billSheet.crop(641,150,673-641,43);
			billEyeThrow[2][7] = billSheet.crop(678,150,710-678,40);

			billEyeGrowth[0][0] = billSheet.crop(713,10,743-713,40);
			billEyeGrowth[0][1] = billSheet.crop(744,10,771-744,40);
			billEyeGrowth[0][2] = billSheet.crop(773,10,798-773,40);
			billEyeGrowth[0][3] = billSheet.crop(713,10,743-713,40);
			billEyeGrowth[0][4] = billSheet.crop(801, 10, 25, 39);
			billEyeGrowth[0][5] = billSheet.crop(832, 10, 25, 39);
			
			billEyeGrowth[1][0] = billSheet.crop(713,56,743-713,40);
			billEyeGrowth[1][1] = billSheet.crop(744,56,771-744,40);
			billEyeGrowth[1][2] = billSheet.crop(773,56,798-773,40);
			billEyeGrowth[1][3] = billSheet.crop(713,56,743-713,40);
			billEyeGrowth[1][4] = billSheet.crop(801, 56, 25, 39);
			billEyeGrowth[1][5] = billSheet.crop(832, 56, 25, 39);
			
			billEyeGrowth[3][0] = billSheet.crop(713,103,743-713,40);
			billEyeGrowth[3][1] = billSheet.crop(744,103,771-744,40);
			billEyeGrowth[3][2] = billSheet.crop(773,103,798-773,40);
			billEyeGrowth[3][3] = billSheet.crop(713,103,743-713,40);
			billEyeGrowth[3][4] = billSheet.crop(801, 103, 25, 39);
			billEyeGrowth[3][5] = billSheet.crop(832, 103, 25, 39);
			
			billEyeGrowth[2][0] = billSheet.crop(713,150,743-713,40);
			billEyeGrowth[2][1] = billSheet.crop(744,150,771-744,40);
			billEyeGrowth[2][2] = billSheet.crop(773,150,798-773,40);
			billEyeGrowth[2][3] = billSheet.crop(713,150,743-713,40);
			billEyeGrowth[2][4] = billSheet.crop(801, 150, 25, 39);
			billEyeGrowth[2][5] = billSheet.crop(832, 150, 25, 39);

			billEye = billSheet.crop(877, 3, 11, 9);
			billEye2 = billSheet.crop(889, 3, 11, 9);

			
			billSymbol = createImageTransparent(
					createImageTransparent(
					loadImage("/UI/Backgrounds/Zelda/billSymbol.png"), "billSymbol", new Color(230,230,230).getRGB()),
					"billSymbol1", new Color(255,255,255).getRGB());
			/////
			
			bronze = createImageTransparent(loadImage("/UI/Misc/bronze.png"), "bronze", new Color(255,255,255).getRGB());
			silver = createImageTransparent(loadImage("/UI/Misc/silver.png"), "silver", new Color(255,255,255).getRGB());
			gold = createImageTransparent(loadImage("/UI/Misc/gold.png"), "gold", new Color(255,255,255).getRGB());

			storyImageSheet = loadImage("/UI/Backgrounds/Zelda/title.png");
			storySpriteSheet = new SpriteSheet(storyImageSheet);
			zeldaTitleFrames[5] = loadImage("/UI/Backgrounds/Zelda/TitleScreen/frame_0.gif");
			zeldaTitleFrames[4] = loadImage("/UI/Backgrounds/Zelda/TitleScreen/frame_1.gif");
			zeldaTitleFrames[3] = loadImage("/UI/Backgrounds/Zelda/TitleScreen/frame_2.gif");
			zeldaTitleFrames[2] = loadImage("/UI/Backgrounds/Zelda/TitleScreen/frame_3.gif");
			zeldaTitleFrames[1] = loadImage("/UI/Backgrounds/Zelda/TitleScreen/frame_4.gif");
			zeldaTitleFrames[0] = loadImage("/UI/Backgrounds/Zelda/TitleScreen/frame_5.gif");

			zeldaStoryFrames[0] = storySpriteSheet.crop(1, 250, 256,223);
			zeldaStoryFrames[1] = storySpriteSheet.crop(258, 250, 256,223);
			zeldaStoryFrames[2] = storySpriteSheet.crop(515, 250, 256,223);
			zeldaStoryFrames[3] = storySpriteSheet.crop(772, 250, 256,223);
			zeldaStoryFrames[4] = storySpriteSheet.crop(1, 475, 256,223);
			zeldaStoryFrames[5] = storySpriteSheet.crop(258, 475, 256,223);
			zeldaStoryFrames[6] = storySpriteSheet.crop(515, 475, 256,223);
			zeldaStoryFrames[7] = storySpriteSheet.crop(772, 475, 256,64);

			zeldaLinkImage = loadImage("/UI/Backgrounds/Zelda/link.png");
			zeldaLinkImage = createImageTransparent(zeldaLinkImage,"link_0,128,0_green",new Color(0,128,0).getRGB());
			zeldaLinkSpriteSheet = new SpriteSheet(createImageTransparent(zeldaLinkImage,"link_116,116,116_gray",new Color(116,116,116).getRGB()));
			zeldaLinkFrames[0] = zeldaLinkSpriteSheet.crop(1,11,16,16);
			zeldaLinkFrames[1] = zeldaLinkSpriteSheet.crop(18,11,16,16);
			zeldaLinkFrames[2] = zeldaLinkSpriteSheet.crop(35,11,16,16);
			zeldaLinkFrames[3] = zeldaLinkSpriteSheet.crop(52,11,16,16);
			zeldaLinkFrames[4] = zeldaLinkSpriteSheet.crop(69,11,16,16);
			zeldaLinkFrames[5] = zeldaLinkSpriteSheet.crop(86,11,16,16);
			zeldaLinkFrames[6] = zeldaLinkSpriteSheet.crop(213,11,16,16);
			zeldaLinkFrames[7] = zeldaLinkSpriteSheet.crop(230,11,16,16);

			zeldaWorldLayoutTileImage = loadImage("/UI/Backgrounds/Zelda/layout.png");
			zeldaWorldLayoutTileSpriteSheet = new SpriteSheet( createImageTransparent(zeldaWorldLayoutTileImage,"layout_0,128,0_green",new Color(0,128,0).getRGB()));
			zeldaWorldLayoutTiles.add(zeldaWorldLayoutTileSpriteSheet.crop(1,154,152,84));
			zeldaWorldLayoutTiles.add(createImage(zeldaWorldLayoutTiles.get(0),"forest_brown4greeen",brown.getRGB(),new Color(0,168,0).getRGB()));
			zeldaWorldLayoutTiles.add(createImage(zeldaWorldLayoutTiles.get(0),"cave_brown4greeen",brown.getRGB(),new Color(124,8,0).getRGB()));
			zeldaWorldLayoutTiles.add(createImage(zeldaWorldLayoutTiles.get(0),"grave_brown4greeen",brown.getRGB(),new Color(252,252,252).getRGB()));


			EnemyOverwoldImage = loadImage("/UI/Backgrounds/Zelda/enemy3.png");
			EnemyOverwoldImage = createImageTransparent(EnemyOverwoldImage,"enemies_overworld_116,116,116_green",new Color(116,116,116).getRGB());
			EnemyOverwoldSpriteSheet = new SpriteSheet( createImageTransparent(EnemyOverwoldImage,"enemies_overworld_0,128,0_green",new Color(0,128,0).getRGB()));

			npcSpriteSheet = new SpriteSheet(npcSprite); 
			itemSheet = loadImage("/UI/Backgrounds/Zelda/items.png");
			itemSpriteSheet = new SpriteSheet(itemSheet);

			oakSheet = loadImage("/UI/Backgrounds/Zelda/profOak.png");
			oakSheet = createImageTransparent(oakSheet, "oak_0_128_128", new Color(0,128,128).getRGB());
			oakSpriteSheet = new SpriteSheet(oakSheet);

			pokeBallSheet = new SpriteSheet(createImageTransparent(loadImage("/UI/Backgrounds/Zelda/pokeballs.png"),"pokeballs_163_73_164", new Color(163,73,164).getRGB()));

			burgerSheet = new SpriteSheet(createImageTransparent(loadImage("/UI/Backgrounds/Zelda/burgerpants.png"),"burgerpants_195_134_255", new Color(195,134,255).getRGB()));

			donnelSheet = new SpriteSheet(createImageTransparent(loadImage("/UI/Backgrounds/Zelda/DONNEL.png"), "donnel", new Color(255,0,255).getRGB()));


			pokeBall = pokeBallSheet.crop(2, 3, 10, 10);
			loveBall = pokeBallSheet.crop(66, 51, 10, 10);

			bouncyEnemyFrames[0] = EnemyOverwoldSpriteSheet.crop(162,90,16,16);
			bouncyEnemyFrames[1] = EnemyOverwoldSpriteSheet.crop(179,90,16,16);

			linkHoldingItem = zeldaLinkSpriteSheet.crop(230, 11, 16, 16);
			linkHoldingItem2 = zeldaLinkSpriteSheet.crop(213,11,16,16);


			// SWORD ATTACKING
			// FIRST
			linkAttackUp[0] = zeldaLinkSpriteSheet.crop(1, 109, 16, 16);
			linkAttackUp[1] = zeldaLinkSpriteSheet.crop(18, 97, 16, 27);
			linkAttackUp[2] = zeldaLinkSpriteSheet.crop(35, 98, 16, 26);
			linkAttackUp[3] = zeldaLinkSpriteSheet.crop(52, 106, 16, 19);

			linkAttackDown[0] = zeldaLinkSpriteSheet.crop(1, 47, 16, 16);
			linkAttackDown[1] = zeldaLinkSpriteSheet.crop(18, 47, 16, 27);
			linkAttackDown[2] = zeldaLinkSpriteSheet.crop(36, 47, 16, 23);
			linkAttackDown[3] = zeldaLinkSpriteSheet.crop(52, 47, 16, 19);

			linkAttackRight[0] = zeldaLinkSpriteSheet.crop(1, 77, 16, 16);
			linkAttackRight[1] = zeldaLinkSpriteSheet.crop(17, 77, 28, 16);
			linkAttackRight[2] = zeldaLinkSpriteSheet.crop(46, 77, 23, 16);
			linkAttackRight[3] = zeldaLinkSpriteSheet.crop(70, 77, 19, 16);

			linkAttackLeft[0] = flipHorizontal(linkAttackRight[0]);
			linkAttackLeft[1] = flipHorizontal(linkAttackRight[1]);
			linkAttackLeft[2] = flipHorizontal(linkAttackRight[2]);
			linkAttackLeft[3] = flipHorizontal(linkAttackRight[3]);


			// SECOND SWORD
			// 94 -> 187, 111 -> 204, 128 -> 221, 145 -> 238
			//139 -> 232, 163 -> 256
			linkAttackUpSecond[0] = zeldaLinkSpriteSheet.crop(187, 109, 16, 16);
			linkAttackUpSecond[1] = zeldaLinkSpriteSheet.crop(204, 97, 16, 27);
			linkAttackUpSecond[2] = zeldaLinkSpriteSheet.crop(221, 98, 16, 26);
			linkAttackUpSecond[3] = zeldaLinkSpriteSheet.crop(238, 106, 16, 19);

			linkAttackDownSecond[0] = zeldaLinkSpriteSheet.crop(187, 47, 16, 16);
			linkAttackDownSecond[1] = zeldaLinkSpriteSheet.crop(204, 47, 16, 27);
			linkAttackDownSecond[2] = zeldaLinkSpriteSheet.crop(221, 47, 16, 23);
			linkAttackDownSecond[3] = zeldaLinkSpriteSheet.crop(238, 47, 16, 19);

			linkAttackRightSecond[0] = zeldaLinkSpriteSheet.crop(187, 77, 16, 16);
			linkAttackRightSecond[1] = zeldaLinkSpriteSheet.crop(204, 77, 28, 16);
			linkAttackRightSecond[2] = zeldaLinkSpriteSheet.crop(232, 77, 23, 16);
			linkAttackRightSecond[3] = zeldaLinkSpriteSheet.crop(256, 77, 19, 16);

			linkAttackLeftSecond[0] = flipHorizontal(linkAttackRightSecond[0]);
			linkAttackLeftSecond[1] = flipHorizontal(linkAttackRightSecond[1]);
			linkAttackLeftSecond[2] = flipHorizontal(linkAttackRightSecond[2]);
			linkAttackLeftSecond[3] = flipHorizontal(linkAttackRightSecond[3]);


			// MASTER SWORD
			// 94 <- 187, 111 <- 204, 128 <- 221, 145 <- 238
			//139 <- 232, 163 <- 256
			linkAttackUpMaster[0] = zeldaLinkSpriteSheet.crop(94, 109, 16, 16);
			linkAttackUpMaster[1] = zeldaLinkSpriteSheet.crop(111, 97, 16, 27);
			linkAttackUpMaster[2] = zeldaLinkSpriteSheet.crop(128, 98, 16, 26);
			linkAttackUpMaster[3] = zeldaLinkSpriteSheet.crop(145, 106, 16, 19);

			linkAttackDownMaster[0] = zeldaLinkSpriteSheet.crop(94, 47, 16, 16);
			linkAttackDownMaster[1] = zeldaLinkSpriteSheet.crop(111, 47, 16, 27);
			linkAttackDownMaster[2] = zeldaLinkSpriteSheet.crop(128, 47, 16, 23);
			linkAttackDownMaster[3] = zeldaLinkSpriteSheet.crop(145, 47, 16, 19);

			linkAttackRightMaster[0] = zeldaLinkSpriteSheet.crop(94, 77, 16, 16);
			linkAttackRightMaster[1] = zeldaLinkSpriteSheet.crop(111, 77, 28, 16);
			linkAttackRightMaster[2] = zeldaLinkSpriteSheet.crop(139, 77, 23, 16);
			linkAttackRightMaster[3] = zeldaLinkSpriteSheet.crop(163, 77, 19, 16);

			linkAttackLeftMaster[0] = flipHorizontal(linkAttackRightMaster[0]);
			linkAttackLeftMaster[1] = flipHorizontal(linkAttackRightMaster[1]);
			linkAttackLeftMaster[2] = flipHorizontal(linkAttackRightMaster[2]);
			linkAttackLeftMaster[3] = flipHorizontal(linkAttackRightMaster[3]);

			////////////////////////////////////////////////////////////////////////////////

			linkDamagePhase2[0] = zeldaLinkSpriteSheet.crop(58, 224, 16, 16);
			linkDamagePhase2[1] = zeldaLinkSpriteSheet.crop(75, 224, 16, 16);
			linkDamagePhase2[2] = zeldaLinkSpriteSheet.crop(92, 224, 16, 16);

			linkDamage[0] = zeldaLinkFrames[0];
			linkDamage[1] = linkDamagePhase2[new Random().nextInt(linkDamagePhase2.length)];
			linkDamage[2] = zeldaLinkSpriteSheet.crop(200, 241, 16, 16);
			linkDamage[3] = zeldaLinkSpriteSheet.crop(223, 241, 16, 16);
			linkDamage[4] = zeldaLinkFrames[0];

			donnelIdle[0] = donnelSheet.crop(136, 4, 18, 25);
			donnelIdle[1] = donnelSheet.crop(167, 4, 18, 25);
			donnelIdle[2] = donnelSheet.crop(199, 4, 18, 25);
			donnelIdle[3] = donnelSheet.crop(230, 4, 18, 25);

			donnelAttackLeft[0] = donnelSheet.crop(133, 36, 158-133, 25);
			donnelAttackLeft[1] = donnelSheet.crop(167, 36, 189-167, 25);
			donnelAttackLeft[2] = donnelSheet.crop(191, 36, 221-191, 25);
			donnelAttackLeft[3] = donnelSheet.crop(223, 36, 253-223, 25);

			donnelAttackRight[0] = flipHorizontal(donnelAttackLeft[0]);
			donnelAttackRight[1] = flipHorizontal(donnelAttackLeft[1]);
			donnelAttackRight[2] = flipHorizontal(donnelAttackLeft[2]);
			donnelAttackRight[3] = flipHorizontal(donnelAttackLeft[3]);

			donnelLeft[0] = donnelSheet.crop(133, 68, 152-133, 25);
			donnelLeft[1] = donnelSheet.crop(164, 68, 185-164, 25);
			donnelLeft[2] = donnelSheet.crop(193, 68, 218-193, 25);
			donnelLeft[3] = donnelSheet.crop(228, 68, 249-228, 25);

			donnelRight[0] = donnelSheet.crop(135, 100, 152-135, 25);
			donnelRight[1] = donnelSheet.crop(167, 100, 185-167, 25);
			donnelRight[2] = donnelSheet.crop(199, 100, 218-199, 25);
			donnelRight[3] = donnelSheet.crop(231, 100, 249-231, 25);

			donnelDown[0] = donnelSheet.crop(135, 131, 154-135, 25);
			donnelDown[1] = donnelSheet.crop(167, 131, 187-167, 25);
			donnelDown[2] = donnelSheet.crop(199, 131, 219-199, 25);
			donnelDown[3] = donnelSheet.crop(231, 131, 20, 25);

			donnelUp[0] = donnelSheet.crop(133, 163, 152-133, 25);
			donnelUp[1] = donnelSheet.crop(164, 163, 20, 25);
			donnelUp[2] = donnelSheet.crop(196, 163, 216-196, 25);
			donnelUp[3] = donnelSheet.crop(228, 163, 20, 25);


			enemy3Sheet = loadImage("/UI/Backgrounds/Zelda/enemy3.png");
			enemy3SpriteSheet = new SpriteSheet(enemy3Sheet);

			enemy2Sheet = loadImage("/UI/Backgrounds/Zelda/enemy2.png");
			enemy2SpriteSheet = new SpriteSheet(enemy2Sheet);


			blueTektike[0] = enemy3SpriteSheet.crop(196, 90, 16, 16);
			blueTektike[1] = enemy3SpriteSheet.crop(213, 90, 16, 16);

			snake[0]= enemy2SpriteSheet.crop(126, 59, 16, 16);
			snake[1]= enemy2SpriteSheet.crop(143, 59, 16, 16);

			TektiteDamage[0] = enemy3SpriteSheet.crop(196, 108, 16, 16);
			TektiteDamage[1] = enemy3SpriteSheet.crop(213, 108, 16, 16);

			//BOW

			bowSheet = loadImage("/UI/Backgrounds/Zelda/linkBow.png");
			bowSpriteSheet = new SpriteSheet(bowSheet);
			//RIGHT
			LinkBow[0] = bowSpriteSheet.crop(1, 0, 19, 16);
			//LEFT
			LinkBow[1] = flipHorizontal(LinkBow[0]);
			//DOWN
			LinkBow[2] = bowSpriteSheet.crop(23, 0, 16, 16);
			//ARROW
			//UP
			linkArrow[0] = zeldaLinkSpriteSheet.crop(2, 185, 7, 16);
			//RIGHT
			linkArrow[1] = zeldaLinkSpriteSheet.crop(8, 185, 16, 16);
			//LEFT
			linkArrow[2] = flipHorizontal(linkArrow[1]);
			//DOWN
			linkArrow[3] = bowSpriteSheet.crop(43, 0, 7, 16);

			// Cave Images

			oldMan = npcSpriteSheet.crop(1, 11, 16, 16);
			zeldaFire = npcSpriteSheet.crop(52, 11, 16, 16);

			// Swords
			linkSword = zeldaLinkSpriteSheet.crop(1, 154, 7, 16);
			secondSword = zeldaLinkSpriteSheet.crop(71, 154, 7, 16);
			masterSword = zeldaLinkSpriteSheet.crop(36, 154, 7, 16);

			linkSwordRight = rotate(linkSword, 90);
			secondSwordRight = rotate(secondSword, 90);
			masterSwordRight = rotate(masterSword, 90);
			//linkSwordRight = zeldaLinkSpriteSheet.crop(10, 158, 15, 9);
			//secondSwordRight = zeldaLinkSpriteSheet.crop(80, 158, 15, 9);
			//masterSwordRight = zeldaLinkSpriteSheet.crop(45, 158, 15, 9);

			linkSwordLeft = flipHorizontal(linkSwordRight);
			secondSwordLeft = flipHorizontal(secondSwordRight);
			masterSwordLeft = flipHorizontal(masterSwordRight);

			linkSwordDown = flipVertical(linkSword);
			secondSwordDown = flipVertical(secondSword);
			masterSwordDown = flipVertical(masterSword);






			// Link's Hearts

			linkHeart = itemSpriteSheet.crop(0, 0, 7, 8);
			linkHalfHeart = itemSpriteSheet.crop(8, 0, 7, 8);
			linkEmptyHeart = itemSpriteSheet.crop(16,0,7,8);
			heartContainer = itemSpriteSheet.crop(25, 1, 12, 12);
			linkPotion = itemSpriteSheet.crop(80, 0, 8, 15);
			linkKey = itemSpriteSheet.crop(240, 0, 8, 16);
			linkKey2 = itemSpriteSheet.crop(248, 0, 8, 16);
			rupee = itemSpriteSheet.crop(72, 0, 8, 16);
			blueRupee = itemSpriteSheet.crop(72, 16, 8, 16);
			triForce[0] = itemSpriteSheet.crop(274, 2, 11, 11);
			triForce[1] = itemSpriteSheet.crop(274, 18, 11, 11);

			rupeeFrames[0] = rupee; rupeeFrames[1] = blueRupee;

			invisibleBlock = loadImage("/UI/Backgrounds/Zelda/invisible.png");



			profOak[0]= oakSpriteSheet.crop(212, 3, 14, 20);
			profOak[1]= oakSpriteSheet.crop(228, 3, 14, 20);
			profOak[2]= oakSpriteSheet.crop(244, 3, 14, 20);
			profOak[3]= oakSpriteSheet.crop(228, 3, 14, 20);

			space = loadImage("/UI/Backgrounds/space.jpg");


			storyImageSheet = loadImage("/UI/Backgrounds/Zelda/title.png");
			storySpriteSheet = new SpriteSheet(storyImageSheet);
			zeldaTitleFrames[5] = loadImage("/UI/Backgrounds/Zelda/TitleScreen/frame_0.gif");
			zeldaTitleFrames[4] = loadImage("/UI/Backgrounds/Zelda/TitleScreen/frame_1.gif");
			zeldaTitleFrames[3] = loadImage("/UI/Backgrounds/Zelda/TitleScreen/frame_2.gif");
			zeldaTitleFrames[2] = loadImage("/UI/Backgrounds/Zelda/TitleScreen/frame_3.gif");
			zeldaTitleFrames[1] = loadImage("/UI/Backgrounds/Zelda/TitleScreen/frame_4.gif");
			zeldaTitleFrames[0] = loadImage("/UI/Backgrounds/Zelda/TitleScreen/frame_5.gif");


			oakRoom = oakSpriteSheet.crop(0, 0, 208, 213);

			burgerPants[0] = burgerSheet.crop(5, 21, 55, 92);
			burgerPants[1] = burgerSheet.crop(65, 21, 56, 93);
			burgerPants[2] = burgerSheet.crop(126, 21, 52, 106);
			burgerPants[3] = burgerSheet.crop(183, 21, 84, 96);
			burgerPants[4] = burgerSheet.crop(272, 21, 62, 91);
			burgerPants[5] = burgerSheet.crop(339, 21, 62, 91);
			burgerPants[6] = burgerSheet.crop(406, 21, 83, 102);

			burgerArms = burgerSheet.crop(5, 149, 50, 45);

			sansEye = loadImage("/UI/Backgrounds/Zelda/eye.png");

			storeBG = loadImage("/UI/Backgrounds/Zelda/storeBG.png");

			// Move Tiles
			//Up
			moveTiles.add(npcSpriteSheet.crop(162, 11, 16, 16));
			//Right
			moveTiles.add(npcSpriteSheet.crop(181, 11, 16, 16));
			//Down
			moveTiles.add(npcSpriteSheet.crop(200, 11, 16, 16));
			// Left
			moveTiles.add(flipHorizontal(moveTiles.get(1)));


			//dungeon one tiles
			zeldaTiles.add(zeldaSpriteSheet.crop(815,11,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(848,11,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(881,11,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(914,11,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(947,11,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(848,44,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(815,44,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(881,44,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(914,44,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(947,44,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(815,77,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(848,77,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(881,77,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(914,77,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(947,77,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(815,110,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(848,110,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(881,110,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(914,110,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(947,110,32,32));
			zeldaTiles.add(zeldaSpriteSheet.crop(984,11,16,16));
			zeldaTiles.add(zeldaSpriteSheet.crop(1001,11,16,16));
			zeldaTiles.add(zeldaSpriteSheet.crop(1018,11,16,16));
			zeldaTiles.add(zeldaSpriteSheet.crop(1035,11,16,16));
			zeldaTiles.add(zeldaSpriteSheet.crop(1001,28,16,16));
			zeldaTiles.add(zeldaSpriteSheet.crop(984,28,16,16));
			zeldaTiles.add(zeldaSpriteSheet.crop(1018,28,16,16));
			zeldaTiles.add(zeldaSpriteSheet.crop(1035,28,16,16));
			zeldaTiles.add(zeldaSpriteSheet.crop(984,45,16,16));
			zeldaTiles.add(zeldaSpriteSheet.crop(1001,45,16,16));

			//main world tiles
			SpriteSheet mountain = new SpriteSheet(zeldaWorldLayoutTiles.get(0));
			SpriteSheet forest = new SpriteSheet(zeldaWorldLayoutTiles.get(1));
			SpriteSheet cave = new SpriteSheet(createImageTransparent(zeldaWorldLayoutTiles.get(2),"caveTransparent_252,216,168_crema", new Color(252,216,168).getRGB()));
			SpriteSheet grave = new SpriteSheet(zeldaWorldLayoutTiles.get(3));

			mountainTiles.add(mountain.crop(0,0,16,16));
			mountainTiles.add(mountain.crop(17,0,16,16));
			mountainTiles.add(mountain.crop(34,0,16,16));
			mountainTiles.add(mountain.crop(51,0,16,16));
			mountainTiles.add(mountain.crop(17,17,16,16));
			mountainTiles.add(mountain.crop(34,17,16,16));
			mountainTiles.add(mountain.crop(51,17,16,16));
			mountainTiles.add(mountain.crop(68,0,16,16));
			mountainTiles.add(mountain.crop(85,0,16,16));
			mountainTiles.add(mountain.crop(102,0,16,16));
			mountainTiles.add(mountain.crop(68,17,16,16));
			mountainTiles.add(mountain.crop(85,17,16,16));
			mountainTiles.add(mountain.crop(102,17,16,16));
			mountainTiles.add(mountain.crop(68,34,16,16));
			mountainTiles.add(mountain.crop(85,34,16,16));
			mountainTiles.add(mountain.crop(102,34,16,16));
			mountainTiles.add(mountain.crop(119,0,16,16));
			mountainTiles.add(mountain.crop(136,0,16,16));
			mountainTiles.add(mountain.crop(119,17,16,16));
			mountainTiles.add(mountain.crop(136,17,16,16));
			mountainTiles.add(mountain.crop(119,34,16,16));
			mountainTiles.add(mountain.crop(136,34,16,16));
			mountainTiles.add(mountain.crop(0,51,16,16));
			mountainTiles.add(mountain.crop(17,51,16,16));
			mountainTiles.add(mountain.crop(34,51,16,16));
			mountainTiles.add(mountain.crop(0,68,16,16));
			mountainTiles.add(mountain.crop(34,68,16,16));
			mountainTiles.add(mountain.crop(51,51,16,16));
			mountainTiles.add(mountain.crop(68,51,16,16));
			mountainTiles.add(mountain.crop(85,51,16,16));
			mountainTiles.add(mountain.crop(51,68,16,16));
			mountainTiles.add(mountain.crop(85,68,16,16));
			mountainTiles.add(mountain.crop(0,17,16,16));
			mountainTiles.add(mountain.crop(0,34,16,16));
			mountainTiles.add(mountain.crop(17,34,16,16));
			mountainTiles.add(mountain.crop(34,34,16,16));
			mountainTiles.add(mountain.crop(51,34,16,16));
			mountainTiles.add(mountain.crop(17,68,16,16));
			mountainTiles.add(mountain.crop(68,68,16,16));
			mountainTiles.add(mountain.crop(102,51,16,16));
			mountainTiles.add(mountain.crop(119,51,16,16));
			mountainTiles.add(mountain.crop(136,51,16,16));

			forestTiles.add(forest.crop(0,0,16,16));
			forestTiles.add(forest.crop(17,0,16,16));
			forestTiles.add(forest.crop(34,0,16,16));
			forestTiles.add(forest.crop(51,0,16,16));
			forestTiles.add(forest.crop(17,17,16,16));
			forestTiles.add(forest.crop(34,17,16,16));
			forestTiles.add(forest.crop(51,17,16,16));
			forestTiles.add(forest.crop(68,0,16,16));
			forestTiles.add(forest.crop(85,0,16,16));
			forestTiles.add(forest.crop(102,0,16,16));
			forestTiles.add(forest.crop(68,17,16,16));
			forestTiles.add(forest.crop(85,17,16,16));
			forestTiles.add(forest.crop(102,17,16,16));
			forestTiles.add(forest.crop(68,34,16,16));
			forestTiles.add(forest.crop(85,34,16,16));
			forestTiles.add(forest.crop(102,34,16,16));
			forestTiles.add(forest.crop(119,0,16,16));
			forestTiles.add(forest.crop(136,0,16,16));
			forestTiles.add(forest.crop(119,17,16,16));
			forestTiles.add(forest.crop(136,17,16,16));
			forestTiles.add(forest.crop(119,34,16,16));
			forestTiles.add(forest.crop(136,34,16,16));
			forestTiles.add(forest.crop(0,51,16,16));
			forestTiles.add(forest.crop(17,51,16,16));
			forestTiles.add(forest.crop(34,51,16,16));
			forestTiles.add(forest.crop(0,68,16,16));
			forestTiles.add(forest.crop(34,68,16,16));
			forestTiles.add(forest.crop(51,51,16,16));
			forestTiles.add(forest.crop(68,51,16,16));
			forestTiles.add(forest.crop(85,51,16,16));
			forestTiles.add(forest.crop(51,68,16,16));
			forestTiles.add(forest.crop(85,68,16,16));
			forestTiles.add(forest.crop(0,17,16,16));
			forestTiles.add(forest.crop(0,34,16,16));
			forestTiles.add(forest.crop(17,34,16,16));
			forestTiles.add(forest.crop(34,34,16,16));
			forestTiles.add(forest.crop(51,34,16,16));
			forestTiles.add(forest.crop(17,68,16,16));
			forestTiles.add(forest.crop(68,68,16,16));
			forestTiles.add(forest.crop(102,51,16,16));
			forestTiles.add(forest.crop(119,51,16,16));
			forestTiles.add(forest.crop(136,51,16,16));

			caveTiles.add(cave.crop(0,0,16,16));
			caveTiles.add(cave.crop(17,0,16,16));
			caveTiles.add(cave.crop(34,0,16,16));
			caveTiles.add(cave.crop(51,0,16,16));
			caveTiles.add(cave.crop(17,17,16,16));
			caveTiles.add(cave.crop(34,17,16,16));
			caveTiles.add(cave.crop(51,17,16,16));
			caveTiles.add(cave.crop(68,0,16,16));
			caveTiles.add(cave.crop(85,0,16,16));
			caveTiles.add(cave.crop(102,0,16,16));
			caveTiles.add(cave.crop(68,17,16,16));
			caveTiles.add(cave.crop(85,17,16,16));
			caveTiles.add(cave.crop(102,17,16,16));
			caveTiles.add(cave.crop(68,34,16,16));
			caveTiles.add(cave.crop(85,34,16,16));
			caveTiles.add(cave.crop(102,34,16,16));
			caveTiles.add(cave.crop(119,0,16,16));
			caveTiles.add(cave.crop(136,0,16,16));
			caveTiles.add(cave.crop(119,17,16,16));
			caveTiles.add(cave.crop(136,17,16,16));
			caveTiles.add(cave.crop(119,34,16,16));
			caveTiles.add(cave.crop(136,34,16,16));
			caveTiles.add(cave.crop(0,51,16,16));
			caveTiles.add(cave.crop(17,51,16,16));
			caveTiles.add(cave.crop(34,51,16,16));
			caveTiles.add(cave.crop(0,68,16,16));
			caveTiles.add(cave.crop(34,68,16,16));
			caveTiles.add(cave.crop(51,51,16,16));
			caveTiles.add(cave.crop(68,51,16,16));
			caveTiles.add(cave.crop(85,51,16,16));
			caveTiles.add(cave.crop(51,68,16,16));
			caveTiles.add(cave.crop(85,68,16,16));
			caveTiles.add(cave.crop(0,17,16,16));
			caveTiles.add(cave.crop(0,34,16,16));
			caveTiles.add(cave.crop(17,34,16,16));
			caveTiles.add(cave.crop(34,34,16,16));
			caveTiles.add(cave.crop(51,34,16,16));
			caveTiles.add(cave.crop(17,68,16,16));
			caveTiles.add(cave.crop(68,68,16,16));
			caveTiles.add(cave.crop(102,51,16,16));
			caveTiles.add(cave.crop(119,51,16,16));
			caveTiles.add(cave.crop(136,51,16,16));

			graveTiles.add(grave.crop(0,0,16,16));
			graveTiles.add(grave.crop(17,0,16,16));
			graveTiles.add(grave.crop(34,0,16,16));
			graveTiles.add(grave.crop(51,0,16,16));
			graveTiles.add(grave.crop(17,17,16,16));
			graveTiles.add(grave.crop(34,17,16,16));
			graveTiles.add(grave.crop(51,17,16,16));
			graveTiles.add(grave.crop(68,0,16,16));
			graveTiles.add(grave.crop(85,0,16,16));
			graveTiles.add(grave.crop(102,0,16,16));
			graveTiles.add(grave.crop(68,17,16,16));
			graveTiles.add(grave.crop(85,17,16,16));
			graveTiles.add(grave.crop(102,17,16,16));
			graveTiles.add(grave.crop(68,34,16,16));
			graveTiles.add(grave.crop(85,34,16,16));
			graveTiles.add(grave.crop(102,34,16,16));
			graveTiles.add(grave.crop(119,0,16,16));
			graveTiles.add(grave.crop(136,0,16,16));
			graveTiles.add(grave.crop(119,17,16,16));
			graveTiles.add(grave.crop(136,17,16,16));
			graveTiles.add(grave.crop(119,34,16,16));
			graveTiles.add(grave.crop(136,34,16,16));
			graveTiles.add(grave.crop(0,51,16,16));
			graveTiles.add(grave.crop(17,51,16,16));
			graveTiles.add(grave.crop(34,51,16,16));
			graveTiles.add(grave.crop(0,68,16,16));
			graveTiles.add(grave.crop(34,68,16,16));
			graveTiles.add(grave.crop(51,51,16,16));
			graveTiles.add(grave.crop(68,51,16,16));
			graveTiles.add(grave.crop(85,51,16,16));
			graveTiles.add(grave.crop(51,68,16,16));
			graveTiles.add(grave.crop(85,68,16,16));
			graveTiles.add(grave.crop(0,17,16,16));
			graveTiles.add(grave.crop(0,34,16,16));
			graveTiles.add(grave.crop(17,34,16,16));
			graveTiles.add(grave.crop(34,34,16,16));
			graveTiles.add(grave.crop(51,34,16,16));
			graveTiles.add(grave.crop(17,68,16,16));
			graveTiles.add(grave.crop(68,68,16,16));
			graveTiles.add(grave.crop(102,51,16,16));
			graveTiles.add(grave.crop(119,51,16,16));
			graveTiles.add(grave.crop(136,51,16,16));


		}catch (IOException e) {
			e.printStackTrace();
		}


	}


	public BufferedImage invertImage(BufferedImage bufferedImage, String name) {
		String path = Objects.requireNonNull(getClass().getClassLoader().getResource(".")).getPath();
		String path2 = path.substring(0,path.indexOf("/bin/"))+"/res/Edited/"+name+".png";
		File imagess = new File(path2.replaceAll("%20"," "));
		if (imagess.exists()){
			try {
				return ImageIO.read(imagess.getAbsoluteFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		for (int x = 0; x < bufferedImage.getWidth(); x++) {
			for (int y = 0; y < bufferedImage.getHeight(); y++) {
				int rgba = bufferedImage.getRGB(x, y);
				Color col = new Color(rgba, true);
				col = new Color(255 - col.getRed(),
						255 - col.getGreen(),
						255 - col.getBlue());
				bufferedImage.setRGB(x, y, col.getRGB());
			}
		}
		File f = null;

		try
		{
			path = Objects.requireNonNull(getClass().getClassLoader().getResource(".")).getPath();
			path2 = path.substring(0,path.indexOf("/bin/"))+"/res/Edited/"+name+".png";
			f = new File(path2.replaceAll("%20"," "));
			System.out.println("File saved in: "+path2);
			ImageIO.write(bufferedImage, "png", f);
		}
		catch(IOException e)
		{
			System.out.println("Error: " + e);
		}
		return bufferedImage;
	}

	public static Color transparant = new Color(255, 255, 255, 0);
	public static Color brown = new Color(200,76,12);

	public BufferedImage createImageTransparent(BufferedImage image, String name, int RGBToReplace){


		return createImage(image,name,RGBToReplace,transparant.getRGB());
	}

	public BufferedImage createImage(BufferedImage image, String name, int RGBToReplace,int RGBReplaicing){

		int width = image.getWidth();
		int height = image.getHeight();
		String path = Objects.requireNonNull(getClass().getClassLoader().getResource(".")).getPath();
		String path2 = path.substring(0,path.indexOf("/bin/"))+"/res/Edited/"+name+".png";
		File imagess = new File(path2.replaceAll("%20"," "));
		if (imagess.exists()){
			try {
				return ImageIO.read(imagess.getAbsoluteFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		// Create buffered image object
		BufferedImage img = null;

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		// file object
		File f = null;

		// create random values pixel by pixel
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if (image.getRGB(x, y) == RGBToReplace) {
					img.setRGB(x, y, RGBReplaicing);
				} else {
					img.setRGB(x, y, image.getRGB(x, y));
				}


			}
		}

		// write image, AKA save it to pc
		try
		{
			path = Objects.requireNonNull(getClass().getClassLoader().getResource(".")).getPath();
			path2 = path.substring(0,path.indexOf("/bin/"))+"/res/Edited/"+name+".png";
			f = new File(path2.replaceAll("%20"," "));
			System.out.println("File saved in: "+path2);
			ImageIO.write(img, "png", f);
		}
		catch(IOException e)
		{
			System.out.println("Error: " + e);
		}
		return img;
	}


	public static BufferedImage loadImage(String path) throws IOException {
		try {
			return ImageIO.read(Images.class.getResourceAsStream(path));
		} catch (IOException e) {
			throw e;
		}

	}

	public static BufferedImage tint(BufferedImage src, float r, float g, float b) {

		// Copy image
		BufferedImage newImage = new BufferedImage(src.getWidth(), src.getHeight(), Transparency.TRANSLUCENT);
		Graphics2D graphics = newImage.createGraphics();
		graphics.drawImage(src, 0, 0, null);
		graphics.dispose();

		// Color image
		for (int i = 0; i < newImage.getWidth(); i++) {
			for (int j = 0; j < newImage.getHeight(); j++) {
				int ax = newImage.getColorModel().getAlpha(newImage.getRaster().getDataElements(i, j, null));
				int rx = newImage.getColorModel().getRed(newImage.getRaster().getDataElements(i, j, null));
				int gx = newImage.getColorModel().getGreen(newImage.getRaster().getDataElements(i, j, null));
				int bx = newImage.getColorModel().getBlue(newImage.getRaster().getDataElements(i, j, null));
				rx *= r;
				gx *= g;
				bx *= b;
				newImage.setRGB(i, j, (ax << 24) | (rx << 16) | (gx << 8) | (bx << 0));
			}
		}
		return newImage;
	}

	public static BufferedImage flipHorizontal(BufferedImage image){
		// Flip the image horizontally
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-image.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);
		return image;
	}

	public static BufferedImage flipVertical(BufferedImage image){
		// Flip the image vertically
		AffineTransform ty = AffineTransform.getScaleInstance(1, -1);
		ty.translate(0, -image.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(ty, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);
		return image;
	}

	public static BufferedImage rotate90(BufferedImage image){

		AffineTransform t = new AffineTransform();
		t.rotate(Math.PI/2,image.getWidth()/2, image.getHeight()/2);
		double offset = (image.getWidth()-image.getHeight())/2;
		t.translate(offset,offset);
		AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);
		return image;
	}

	public static BufferedImage rotate270(BufferedImage image){

		AffineTransform t = new AffineTransform();
		t.rotate(-Math.PI/2,image.getWidth()/2, image.getHeight()/2);
		double offset = (image.getWidth()-image.getHeight())/2;
		t.translate(-offset,-offset);
		AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);
		return image;
	}

	// FIXME: Only works until 90
	public static BufferedImage rotate(BufferedImage image, int angle){
		AffineTransform t = new AffineTransform();
		t.rotate(Math.toRadians(angle),image.getWidth()/2, image.getHeight()/2);
		double offset = (image.getWidth()-image.getHeight())/2;
		t.translate(offset,offset);
		AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);
		return image;
	}



}