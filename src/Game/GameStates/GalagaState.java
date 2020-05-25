package Game.GameStates;

import Game.Galaga.Entities.BaseEntity;
import Game.Galaga.Entities.EnemyBee;
import Game.Galaga.Entities.EntityManager;
import Game.Galaga.Entities.HeMan;
import Game.Galaga.Entities.NewEnemy;
import Game.Galaga.Entities.PlayerShip;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by AlexVR on 1/24/2020.
 */
public class GalagaState extends State {

    public EntityManager entityManager;
    public String Mode = "Menu";
    private Animation titleAnimation;
    public int selectPlayers = 1;
    public String difficulty = "easy", previousDifficulty = "easy";
    public int startCooldown = 60*7;//seven seconds for the music to finish
    public int spawnCooldown = 60 * 5; // start to spawn enemies after 5 seconds of gameplay
    public boolean noEnemies = true; // Variable to check if enemies are spawned
    public boolean startMusic = false, runOnce =  false;
    public int randomNum;
    private boolean champion = false, silver = false, bronze = false;
    private boolean showChampionPrompt = true, showSilverPrompt = true, showBronzePrompt = true;
    public List<List<Boolean>> grid = initGalagaGrid();

    public GalagaState(Handler handler){
        super(handler);
        refresh();
        entityManager = new EntityManager(new PlayerShip(handler.getWidth()/2-64,handler.getHeight()- handler.getHeight()/7,64,64,Images.galagaPlayer[0],handler));
        titleAnimation = new Animation(256,Images.galagaLogo);
    }


    @Override
    public void tick() {
        if (Mode.equals("Stage")){

        	if (startCooldown<=0) {
                entityManager.tick();
                
                /* TODO: Modify the algorithm to spawn enemies individually, not together.
                 * As stated in the rubric: When an enemy dies, a counter should start to determine
                 * when the next enemy of that type should spawn
                 */
                if (entityManager.entities.isEmpty()) {
                	noEnemies = true;
                }
                
           	 if ( entityManager.playerShip.getHealth() <= 0 && entityManager.playerShip.deathAnimation.end) {
               	handler.getMusicHandler().changeMusic("LegendGameOver.wav");
              	Mode = "GameOver";
              	return;
              } 
    
                for (BaseEntity enemy : entityManager.entities) {
                	if (enemy instanceof EnemyBee || enemy instanceof NewEnemy) {
                		noEnemies = false;
                		break;
                	}
                	
                	else {
                		noEnemies = true;
                	}
                }
                
                if (spawnCooldown == 0 || noEnemies == true) {
                	Random random = new Random();
                	int beesToSpawn, shootersToSpawn;
                	
                	beesToSpawn = handler.getGalagaState().difficulty.equals("hard") ? 14 : random.nextInt(5) + 5;  // 5 - 9 bees
                	shootersToSpawn = handler.getGalagaState().difficulty.equals("hard") ? 12 : random.nextInt(3) + 3; // 3 - 5 Shooters
                	noEnemies = false;
                	
                	if (Mode == "Stage" && startCooldown <= 0 && startMusic == false) {
                		
                		if (difficulty.equals("hard")) {
                			handler.getMusicHandler().startMusic("hrdmode.wav");
                		}
                		
                		else if (difficulty.equals("baby")) {
                			handler.getMusicHandler().startMusic("TomboyishBabyMode.wav");
                		}

                		else {
                			handler.getMusicHandler().startMusic("BigBlueArena.wav");
                		}
                      	startMusic = true;
                      	
                	}
                	
                	/*
                	 * Superboss created by Jann C. :) He-Man is beautiful
                	 * 
                	 * He-Man only spawns under 3 circumstances:
                	 * 1) Player is on Hard Mode
                	 * 2) He-Man's % of spawn increases with every 2000 points
                	 * 3) The final row has a free spot/no He-Man
                	 */

                	int chance = random.nextInt(11);
                	int score = handler.getScoreManager().getGalagaCurrentScore();
          
                	
                	if (difficulty.equals("hard") && handler.getScoreManager().getGalagaCurrentScore() >= 2000 && chance <= (score / 2000)) {
                		List<List<Boolean>> grid = getGalagaGrid();
                		
                		// He-Man can only spawn in the first row
                		boolean shouldSpawn = false;
                		
                		// Check if the first row is empty
                		for (int i = 0; i < grid.get(1).size(); i++) {
                			if (grid.get(1).get(i) != null && grid.get(1).get(i) == false) {
                				shouldSpawn = true;
                				break;
                			}
                		}
                		
                		// Check if He-Man already exists
                		for (BaseEntity enemy : handler.getGalagaState().entityManager.entities) {
                			if (enemy instanceof HeMan) {
                				shouldSpawn = false;
                				break;
                			}
                		}
                		
                		
                		// Spawn He-Man
                		while (shouldSpawn) {
                			int col = random.nextInt(8);
                			if (grid.get(1).get(col) != null && grid.get(1).get(col) == false) {
                				handler.getGalagaState().entityManager.entities.add(new HeMan(10, 10, 60, 80, handler, col));
                            	handler.getMusicHandler().playEffect("heman.wav");	
                            	shouldSpawn = false;
                			}
                    	}
                	}

                	for (int i = 0; i < beesToSpawn; i++) {

                    	
                    	// Get the grid of the bee
                    	List<List<Boolean>> beeGrid = grid.subList(3,5);
                    	boolean shouldSpawn = false;
                    	
                    	// Check if the bee should be able to spawn
                    	for (int x = 0; x < beeGrid.size(); x ++) {
                    		for (int y = 0; y < beeGrid.get(x).size(); y ++) {
                    			if (beeGrid.get(x).get(y) != null && beeGrid.get(x).get(y) == false) {
                    				shouldSpawn = true;
                    				break;
                    			}
                    		}
                    	}
                    	
                    	// Spawn Bee
                    	while (shouldSpawn) {
                    		int row = random.nextInt(2) + 3;
                            int col = random.nextInt(8); 
                            
                            if (grid.get(row).get(col) != null && grid.get(row).get(col) == false) {
                            	handler.getGalagaState().entityManager.entities.add(new EnemyBee(10, 10, 30, 30, handler, row, col));
                            	shouldSpawn = false;
                            }
                            
                    	}
                    	
                    
                	}
                	
                	
                	for (int i = 0; i < shootersToSpawn; i++) {

                    	
                    	// Get the grid for the new enemy
                    	List<List<Boolean>> shootGrid = grid.subList(1, 3);
                    	
                    	boolean shouldSpawn = false;
                    	
                    	// Check if the new enemy should be able to spawn
                    	for (int x = 0; x < shootGrid.size(); x ++) {
                    		for (int y = 0; y < shootGrid.get(x).size(); y ++) {
                    			if (shootGrid.get(x).get(y) != null && shootGrid.get(x).get(y) == false) {
                    				shouldSpawn = true;
                    				break;
                    			}
                    		}
                    	}
                    	
                    	// Spawn New Enemy
                    	while (shouldSpawn == true) {
                    		int row = random.nextInt(2) + 1;
                            int col = random.nextInt(6) + 1;
                            
                            if (grid.get(row).get(col) != null && grid.get(row).get(col) == false) {
                            	handler.getGalagaState().entityManager.entities.add(new NewEnemy(10, 10, 30, 30, handler, row, col));
                            	shouldSpawn = false;
                            }
                            
                    	}

                    
                	}

                	spawnCooldown = random.nextInt(11 * 60) + 5 * 60;  // 5 - 15 seconds to spawn again 
                }
                
                else {
                	spawnCooldown--;
                }
            }else{
                startCooldown--;
            }
        }
        
        else if (Mode.equals("Menu")){
            titleAnimation.tick();
            if (handler.getKeyManager().up){
                selectPlayers=1;
                difficulty = "easy";
                
                // Set the previous difficulty now
                previousDifficulty = difficulty;
            	difficulty = new String("easy");
            	
            }else if (handler.getKeyManager().down){
                selectPlayers=2;
                difficulty = "hard";
                
                // Set the previous difficulty now
                previousDifficulty = difficulty;
            	difficulty = new String("hard");
                
            }
            
            // CTRL + B for baby mode
            else if (handler.getKeyManager().keyIsActive(KeyEvent.VK_CONTROL) && handler.getKeyManager().keyIsActive(KeyEvent.VK_B)) {
            	previousDifficulty = difficulty;

            	difficulty = new String("baby");

            	Mode = "Stage";
                handler.getMusicHandler().stopMusic();
                handler.getMusicHandler().playEffect("Galaga.wav");	
            }
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
                Mode = "Stage";
                handler.getMusicHandler().stopMusic();
                handler.getMusicHandler().playEffect("Galaga.wav");

            }


        }

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.cyan);
        g.fillRect(0,0,handler.getWidth(),handler.getHeight());
        g.setColor(Color.BLACK);
        g.fillRect(handler.getWidth()/4,0,handler.getWidth()/2,handler.getHeight());
        Random random = new Random(System.nanoTime());

        for (int j = 1;j < random.nextInt(15)+60;j++) {
            switch (random.nextInt(6)) {
                case 0:
                    g.setColor(Color.RED);
                    break;
                case 1:
                    g.setColor(Color.BLUE);
                    break;
                case 2:
                    g.setColor(Color.YELLOW);
                    break;
                case 3:
                    g.setColor(Color.GREEN);
                    break;
                case 4:
                	g.setColor(Color.WHITE);
                	break;
                case 5:
                	g.setColor(Color.MAGENTA);
                	break;
            }
            int randX = random.nextInt(handler.getWidth() - handler.getWidth() / 2) + handler.getWidth() / 4;
            int randY = random.nextInt(handler.getHeight());
            g.fillRect(randX, randY, 2, 2);

        }
        if (Mode.equals("Stage")) {
            g.setColor(Color.MAGENTA);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 62));
            g.drawString("HIGH",handler.getWidth()-handler.getWidth()/4,handler.getHeight()/16);
            g.drawString("SCORE",handler.getWidth()-handler.getWidth()/4+handler.getWidth()/48,handler.getHeight()/8);
            g.setColor(Color.MAGENTA);
            g.drawString(String.valueOf(handler.getScoreManager().getGalagaHighScore()),handler.getWidth()-handler.getWidth()/4+handler.getWidth()/48,handler.getHeight()/5);
            
            
            // Current Score Meter
            g.setColor(Color.RED);
            g.setFont(new Font("TimesRoman", Font.PLAIN,31));  // TODO: Change font size and relocate high score
            g.drawString("1UP", (int) (handler.getWidth() - handler.getWidth() * .54), handler.getHeight()/32);
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(handler.getScoreManager().getGalagaCurrentScore()),(int) (handler.getWidth()-handler.getWidth() * .56 + handler.getWidth()/48),handler.getHeight()/16);
            
            
            // Draw Difficulty
            g.setColor(Color.RED);
            g.drawString("DIFFICULTY: ", (int) (handler.getWidth()* .05) ,handler.getHeight()/16);
            g.drawString(difficulty.toUpperCase(), (int) (handler.getWidth() * .08) ,handler.getHeight()/8);
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 62));
            
            // Draw trophy if beaten
            
            if (champion) {
            	g.drawImage(Images.gold, (int) (handler.getWidth() * .05), handler.getHeight()/2, handler.getWidth()/6, handler.getHeight()/4, null);
            }
            
            else if (silver) {
            	g.drawImage(Images.silver, (int) (handler.getWidth() * .05), handler.getHeight()/2, handler.getWidth()/6, handler.getHeight()/4, null);
            }
            
            else if (bronze) {
            	g.drawImage(Images.bronze, (int) (handler.getWidth() * .05), handler.getHeight()/2, handler.getWidth()/6, handler.getHeight()/4, null);
            }
            
            //acortar
            if (handler.getScoreManager().getGalagaCurrentScore() > handler.getScoreManager().getGalagaHighScore()) {
            handler.getScoreManager().setGalagaHighScore(handler.getScoreManager().getGalagaCurrentScore());	
            }
            
            
            for (int i = 0; i< entityManager.playerShip.getHealth();i++) {
                g.drawImage(Images.galagaPlayer[0], (handler.getWidth() - handler.getWidth() / 4 + handler.getWidth() / 48) + ((entityManager.playerShip.width*2)*i), handler.getHeight()-handler.getHeight()/4, handler.getWidth() / 18, handler.getHeight() / 18, null);
            }
            if (startCooldown<=0) {
                entityManager.render(g);
            }else{
                g.setFont(new Font("TimesRoman", Font.PLAIN, 48));
                g.setColor(Color.MAGENTA);
                g.drawString("Start",handler.getWidth()/2-handler.getWidth()/18,handler.getHeight()/2);
            }
        }
        
        else if (Mode.equals("GameOver")) {
        	 
        	
        	
       	 	g.setColor(Color.YELLOW);
            g.setFont(new Font("TimesRoman", Font.PLAIN,36));  
       	 	g.drawString("Your Score: " + handler.getScoreManager().getGalagaCurrentScore() , (int) (handler.getWidth() - handler.getWidth() * .60), 0 + 40 );
       	 	g.setColor(Color.RED);
       	 	g.setFont(new Font("TimesRoman", Font.PLAIN,48));  
            g.drawString("---Game Over---", (int) (handler.getWidth() - handler.getWidth() * .60), handler.getHeight()/4);
            
            
         // Make sure we only run once
            if (runOnce == false) {
            	randomNum = random.nextInt(100);
            	runOnce = true;
           	}
            
            if (difficulty.equals("baby")) {
            	// Change the number every time you add a quote in the BABY function
            	int quotesInFunction = 4;
            	
            	if (randomNum > quotesInFunction) {
            		randomNum = random.nextInt(quotesInFunction);
            	}
            	drawBabyModeQuote(g, randomNum);
            }
            
            else if (difficulty.equals("easy")) {
            	// Change the number every time you add a quote in the EASY function
            	int quotesInFunction = 10;
            	
            	if (randomNum > quotesInFunction) {
            		randomNum = random.nextInt(quotesInFunction);
            	}
            	drawEasyModeQuote(g, randomNum);
            }
            
            else if (difficulty.equals("hard")){
            	// Change the number every time you add a quote in the HARD function
            	int quotesInFunction = 8;
            	
            	if (randomNum > quotesInFunction) {
            		randomNum = random.nextInt(quotesInFunction);
            	}
            	drawHardModeQuote(g, randomNum);
            }
     
            
            	
                g.setFont(new Font("TimesRoman", Font.PLAIN,36));
            	g.drawString("Press Y to continue" , (int) (handler.getWidth()  - handler.getWidth() * .60), handler.getHeight() - 70);

            	if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_Y)) {
            		Mode = "Menu";
            		refresh();
                    handler.getMusicHandler().changeMusic("nature.wav");
            		State.setState(handler.getMenuState());
            		entityManager.playerShip.setHealth(entityManager.playerShip.getHealth() + 3);
            		runOnce = false;
            		
            		showChampionPrompt = (champion) ? false : true;
            		showSilverPrompt = (silver) ? false : true;
            		showBronzePrompt = (bronze) ? false : true;
            		
            	}
            	
            }
            
       

        else if (Mode.equals("Menu")){

            g.setFont(new Font("TimesRoman", Font.PLAIN, 32));

            g.setColor(Color.MAGENTA);
            g.drawString("HIGH-SCORE:",handler.getWidth()/2-handler.getWidth()/18,32);

            g.setColor(Color.MAGENTA);
            g.drawString(String.valueOf(handler.getScoreManager().getGalagaHighScore()),handler.getWidth()/2-32,64);

            g.drawImage(titleAnimation.getCurrentFrame(),handler.getWidth()/2-(handler.getWidth()/12),handler.getHeight()/2-handler.getHeight()/3,handler.getWidth()/6,handler.getHeight()/7,null);

            g.drawImage(Images.galagaCopyright,handler.getWidth()/2-(handler.getWidth()/8),handler.getHeight()/2 + handler.getHeight()/3,handler.getWidth()/4,handler.getHeight()/8,null);

            g.setFont(new Font("TimesRoman", Font.PLAIN, 48));
            g.drawString("Easy Mode",handler.getWidth()/2-handler.getWidth()/16,handler.getHeight()/2);
            g.drawString("Hard Mode",handler.getWidth()/2-handler.getWidth()/16,handler.getHeight()/2+handler.getHeight()/12);
            if (selectPlayers == 1){
                g.drawImage(Images.galagaSelect,handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2-handler.getHeight()/32,32,32,null);
            }else{
                g.drawImage(Images.galagaSelect,handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2+handler.getHeight()/18,32,32,null);
            }


        }
    }



	@Override
    public void refresh() {
    	difficulty = previousDifficulty;
        handler.getScoreManager().setGalagaCurrentScore(0);
        entityManager = new EntityManager(new PlayerShip(handler.getWidth()/2-64,handler.getHeight()- handler.getHeight()/7,64,64,Images.galagaPlayer[0],handler));
        startCooldown = 60 * 7;
        spawnCooldown = 60 * 5;
        noEnemies = true;
        startMusic = false;
        this.Mode = "Menu";
        grid = initGalagaGrid();
    }

    
	private void drawBabyModeQuote(Graphics g, int num) {

        switch (num) {
        
	        case 0:
            	g.drawString("Imagine losing on Baby Mode.", (int) (handler.getWidth() - handler.getWidth() * .70), handler.getHeight()/2);
            	break;
            	
	        case 1:
            	g.drawString("How did you even lose...?", (int) (handler.getWidth() - handler.getWidth() * .70), handler.getHeight()/2);
            	break;
            	
	        case 2:
	        	g.drawString("Losing on baby mode.", (int) (handler.getWidth() - handler.getWidth() * .65), handler.getHeight()/2);
	        	g.drawString("You did it on purpose...", (int) (handler.getWidth() - handler.getWidth() * .65), handler.getHeight()/2 + 48);
	        	break;
	        case 3:
	        	g.drawString(":(", (int) (handler.getWidth() - handler.getWidth() * .53), handler.getHeight()/2 );
	        	break;

   		}
	}
    
	private void drawEasyModeQuote(Graphics g, int num) {

	        switch (num) {
	        
		        case 0:
		        	g.drawString("Don't be yourself, be a pizza. ", (int) (handler.getWidth() - handler.getWidth() * .75), handler.getHeight()/2);
		        	g.drawString("Everyone loves pizza.", (int) (handler.getWidth() - handler.getWidth() * .53), handler.getHeight()/2 + 48);
		        	break;
		        case 1:
		        	g.drawString("Are you proud of yourself?", (int) (handler.getWidth() - handler.getWidth() * .68), handler.getHeight()/2);
		        	break;
		        case 2:
		        	g.drawString("They say the sky is the limit, ", (int) (handler.getWidth() - handler.getWidth() * .70), handler.getHeight()/2);
		        	g.drawString("but don't lose too much", (int) (handler.getWidth() - handler.getWidth() * .65), handler.getHeight()/2 + 48); 
		        	break;
		        case 3:
			    	g.drawString("Déjá vu, ", (int) (handler.getWidth() - handler.getWidth() * .75), handler.getHeight()/2);
			    	g.drawString("have we been in this place before", (int) (handler.getWidth() - handler.getWidth() * .67), handler.getHeight()/2 + 48);
			    	break;
			    case 4:
			    	g.drawString("Each defeat is a step towards victory", (int) (handler.getWidth() - handler.getWidth() * .75), handler.getHeight()/2);
			    	break;
			    case 5:
			    	g.drawString("Maybe we need a Baby-Mode", (int) (handler.getWidth() - handler.getWidth() * .75), handler.getHeight()/2);
			    	break;
			    	
			    case 6:
			    	g.drawString("Life's not fair.", (int) (handler.getWidth() - handler.getWidth() * .58), handler.getHeight()/2);
			    	g.drawString("Is it, my little friend?", (int) (handler.getWidth() - handler.getWidth() * .65), handler.getHeight()/2 + 48);
			    	break;
			    case 7:
			    	g.drawString("F" , (int) (handler.getWidth() - handler.getWidth() * .5), handler.getHeight()/2);
			    	break;
			    	
			    case 8:
			    	g.drawString("oof", (int) (handler.getWidth() - handler.getWidth() * .51), handler.getHeight()/2);
			    	break;
			    	
			    case 9:
			    	g.drawString("CTRL + B on menu screen?", (int) (handler.getWidth() - handler.getWidth() * .65), handler.getHeight()/2 + 48);
			    	break;
	
	   		}
		}
	
	public List<List<Boolean>> initGalagaGrid() {
        List<List<Boolean>> grid = new ArrayList<>();
    	for( int i = 0; i < 5; i++) {  // for i in range(6)
			List<Boolean> row = new ArrayList<>();  // row = list()
			
			for( int k = 0; k < 8; k++) {  // for i in range(8)
				row.add(k, false);  // row.append(false)
			}
			grid.add(row);  // grid.append(row)
		}
    	
    	for (int i = 1; i < 3; i++) {
			grid.get(i).set(0, null);
			grid.get(i).set(7, null);
		}

		return grid;
    }
    
    public List<List<Boolean>> getGalagaGrid() {
    	return grid;
    }
	
    private void drawHardModeQuote(Graphics g, int num) {
    	
    	if (handler.getScoreManager().getGalagaCurrentScore() > 35000 && showChampionPrompt) { // Jann's personal best
    		g.drawString("Hey! You broke my high-score!" , (int) (handler.getWidth() - handler.getWidth() * .73), handler.getHeight()/2 - 48);
    		g.drawString(handler.getScoreManager().getGalagaCurrentScore() + " > " + 35000, (int) (handler.getWidth() - handler.getWidth() * .60), handler.getHeight()/2);
        	g.drawImage(Images.gold, (int) (handler.getWidth() * .40), (int) (handler.getHeight() * .60), handler.getWidth()/6, handler.getHeight()/4, null);

    		champion = true;
    		silver = true;
    		bronze = true;
    		return;
    	}
    	
    	else if(handler.getScoreManager().getGalagaCurrentScore() > 20000 && showSilverPrompt) {
    		g.drawString("You have earned the" , (int) (handler.getWidth() - handler.getWidth() * .65), handler.getHeight()/2 - 48);
    		g.drawString("Silver Trophy!" , (int) (handler.getWidth() - handler.getWidth() * .60), handler.getHeight()/2 );
        	g.drawImage(Images.silver, (int) (handler.getWidth() * .40), (int) (handler.getHeight() * .60), handler.getWidth()/6, handler.getHeight()/4, null);


    		silver = true;
    		bronze = true;
    		return;
    	}
    	
    	else if (handler.getScoreManager().getGalagaCurrentScore() > 10000 && showBronzePrompt) {
    		g.drawString("You have earned the" , (int) (handler.getWidth() - handler.getWidth() * .65), handler.getHeight()/2 - 48);
    		g.drawString("Bronze Trophy!" , (int) (handler.getWidth() - handler.getWidth() * .60), handler.getHeight()/2 );
        	g.drawImage(Images.bronze, (int) (handler.getWidth() * .40), (int) (handler.getHeight() * .60), handler.getWidth()/6, handler.getHeight()/4, null);

    		bronze = true;
    		return;
    	}

        switch (num) {
        
	        case 0:
            	g.drawString("Yeah, we lost here too.", (int) (handler.getWidth() - handler.getWidth() * .70), handler.getHeight()/2);
            	break;
            	
	        case 1:
            	g.drawString("Don't worry, this mode sucks.", (int) (handler.getWidth() - handler.getWidth() * .70), handler.getHeight()/2);
            	break;
            	
	        case 2:
	        	g.drawString("My little sister scored 13200 on", (int) (handler.getWidth() - handler.getWidth() * .70), handler.getHeight()/2);
            	g.drawString("this mode.", (int) (handler.getWidth() - handler.getWidth() * .60), handler.getHeight()/2 + 48);
	        	break;
	        	
	        case 3:
		    	g.drawString("CTRL + B on menu screen?", (int) (handler.getWidth() - handler.getWidth() * .65), handler.getHeight()/2 + 48);
		    	break;
		    	
	        case 4:
	        	g.drawString("Here is a tip:" , (int) (handler.getWidth() - handler.getWidth() * .60), handler.getHeight()/2 - 48);
	        	g.drawString("Get rid of the red enemies", (int) (handler.getWidth() - handler.getWidth() * .70), handler.getHeight()/2);
	        	g.drawString("as fast as you can", (int) (handler.getWidth() - handler.getWidth() * .65), handler.getHeight()/2 + 48);
	        	break;
	        	
	        case 5:
	        	g.drawString("Insanity is doing the same thing", (int) (handler.getWidth() - handler.getWidth() * .70), handler.getHeight()/2 - 48);
	        	g.drawString("over and over again and expecting", (int) (handler.getWidth() - handler.getWidth() * .72), handler.getHeight()/2 );
	        	g.drawString("different results.", (int) (handler.getWidth() - handler.getWidth() * .60), handler.getHeight()/2 + 48);
	        	g.drawString("- Albert Einstein", (int) (handler.getWidth() - handler.getWidth() * .5), handler.getHeight()/2 + (48 * 3));
	        	break;
	        	
	        case 6:
	        	g.drawString("Hard Mode:", (int) (handler.getWidth() - handler.getWidth() * .60), handler.getHeight()/2 );
	        	g.drawString("Courtesy from us :)", (int) (handler.getWidth() - handler.getWidth() * .65), handler.getHeight()/2 + 48);
	        	break;
	        	
	        case 7:
	        	int size = 12;
	            g.setFont(new Font("TimesRoman", Font.PLAIN, size));

	        	g.drawString("The plague of this universe became more powerful than you can ever imagine. Scraps of your spaceship", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 3));
	        	g.drawString("scattered in pieces across the twinkling lights of distant stars. The buzzing of space bees bring you", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) - (size * 3));
	        	g.drawString("discomfort in your final moments with the cracks in your helmet. You have lost. And in this universe where", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) - (size * 2));
	        	g.drawString("the enemy has regained its confidence in their battle the worlds as we knew it began to crumble under the", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) - (size));
	        	g.drawString("weight of this evil. They have come. And you were the last line of defense from Earth. Many women and", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2));
	        	g.drawString("children have lost their lives in the Space War against these monsters. Look what you've done. The chaos; the", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) + (size));
	        	g.drawString("carnage. You could have stopped it. You could have stopped all of it. Instead you have to watch with your", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) + (size * 2));
	        	g.drawString("last dying breath the destruction of mankind at your hands. Do you feel proud? Disgusted? Angry? I hear you.", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) + (size * 3));
	        	g.drawString("I understand... ", (int) (handler.getWidth() - handler.getWidth() * .53), handler.getHeight()/2 - (48 * 2) + (size * 5));

	        	g.drawString("I feel as though you were not granted the power which you needed in that time. I shall grant you that power.", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) + (size * 7));
	        	g.drawString("It is not an invisibility spell, it is not a new super weapon. Nor is it a defense mechanism or an enlarged", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) + (size * 8));
	        	g.drawString("plasma cannon. It is a restart. A chance to bring back the worlds that have been lost to the plague. These", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) + (size * 9));
	        	g.drawString("chances are so rarely handed out; so rarely earned, but you show promise. You remind me of myself: someone ", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) + (size * 10));
	        	g.drawString("willing to fight the odds at any cost to achieve your goals. Someone with determination and aspirations,", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) + (size * 11));
	        	g.drawString("someone with... perhaps a family? A loved one that you so desperately want to protect? Whoever the purpose is", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) + (size * 12));
	        	g.drawString("for, whatever the reason, I desire no more than to send you back in time for the success you will achieve. ", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) + (size * 13));
	        	g.drawString("You must achieve. It is your will. Your duty. But do you truly wish in the deepest parts of your still", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) + (size * 14));
	        	g.drawString("beating heart, as your body sways in the airless void in between life and death, to come back and right the", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) + (size * 15));
	        	g.drawString("wrongs that you have made mere seconds ago? This power is granted only to those whose might is strong. Are", (int) (handler.getWidth() - handler.getWidth() * .69), handler.getHeight()/2 - (48 * 2) + (size * 16));
	        	g.drawString("you willing to accept your fate as is, or will you decide to change it?", (int) (handler.getWidth() - handler.getWidth() * .64), handler.getHeight()/2 - (48 * 2) + (size * 17));
	        	g.drawString("I ask of you...", (int) (handler.getWidth() - handler.getWidth() * .53), handler.getHeight()/2 - (48 * 2) + (size * 19));

	        	g.drawString("Would you like to try again? ", (int) (handler.getWidth() - handler.getWidth() * .55), (int) (handler.getHeight() * .75));

	        	break;
   		}
	
	}
}
