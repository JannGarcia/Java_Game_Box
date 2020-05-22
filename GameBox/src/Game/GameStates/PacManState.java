package Game.GameStates;

import Game.PacMan.World.MapBuilder;
import Game.PacMan.entities.Dynamics.BaseDynamic;
import Game.PacMan.entities.Dynamics.Ghost;
import Game.PacMan.entities.Dynamics.HeMan;
import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Game.PacMan.entities.Statics.Dot;
import Game.PacMan.entities.Statics.Fruit;
import Game.PacMan.entities.Statics.GhostSpawner;
import Main.Handler;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class PacManState extends State {

    public String Mode = "Intro";
    public int startCooldown = 60*4;  // Four seconds for the music to finish
    private int spawnCooldown = 0;
    public int ghostCount = 0;
    public int respawnCooldown = 0;
    private final int NUMBER_OF_GHOSTS = 4;
    private int droningCooldown = 20;
    private int level = 1; // Level Mechanic!
    
    // Removed Maps: Images.map1, Images.TEST_MAP;
    private BufferedImage[] maps = {Images.originalMap, Images.map4, /*Images.inso_rocks,*/ Images.mapAle , Images.map5, Images.mapDerek, Images.bonusStage};


    // determines whether galaga sprites will be used
    private boolean usingGalagaSprites = false;

    // The variables below are for He-Man purposes only
    public boolean heManCanSpawn;
    public int heManSpawnTime =  new Random().nextInt(2 * 60) + 2 * 60; // [2, 4] seconds
    public boolean heManIsDead = true;


    public PacManState(Handler handler){
        super(handler);
        handler.setMap(MapBuilder.createMap(maps[level - 1], handler));
    }


	@Override
    public void tick() {
        // reverses the value of galagaSprites when G is pressed
        if (!Mode.equals("Stage") && handler.getKeyManager().keyJustPressed(KeyEvent.VK_G)) {
            this.usingGalagaSprites = !this.usingGalagaSprites;
        }

        switch (Mode) {
            case "Stage":
            	
            	// Only used to determine if He-Man can spawn
                if (startCooldown == 0) {
                    heManCanSpawn = getHeMan() != null;
                    startCooldown--;
                } 
                
                // Prevents Pac-Man from moving once he dies
                if (respawnCooldown > 0) {respawnCooldown--;}
                
                else if (startCooldown <= 0) {
                	// Add a ghost every second
                    if (spawnCooldown == 0 && ghostCount < NUMBER_OF_GHOSTS) {
                        addGhosts(1, false);
                        spawnCooldown = 60;
                    } 
                    else {spawnCooldown--;}

                    for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) {
                        entity.tick();
                    }

                    // If Pac-Man has eaten every dot/fruit
                    if (!dotsLeft()) {
                        Mode = "LevelUp";
                        return;
                    }

                    if (droningCooldown <= 0) {
                        handler.getMusicHandler().playEffect("pacman_droning.wav");
                        droningCooldown = 20;
                    } else
                        droningCooldown--;

                    ArrayList<BaseStatic> toREmove = new ArrayList<>();

                    // Removing Extra Ghosts
                    ArrayList<BaseDynamic> toRemove = new ArrayList<>();
                    for (BaseDynamic entity : handler.getMap().getEnemiesOnMap()) {
                        if ((entity instanceof Ghost)) {
                            if (((Ghost) entity).dead && ghostCount > NUMBER_OF_GHOSTS && !ghostIsLastOfItsKind(((Ghost) entity))) {
                                toRemove.add(entity);
                                ghostCount--;
                            }
                        } else if ((entity instanceof HeMan)) {
                            // He Man only dies when there are no bigdots left
                            if (((HeMan) entity).isDefeated()) {
                                ((HeMan) entity).resetEffects();
                                heManIsDead = true;
                                ((HeMan) entity).canMove = false;
                                entity.y = -100;
                                heManCanSpawn = false;
                                handler.getMusicHandler().stopMusic();
                                handler.getMusicHandler().playEffect("explosion.wav");
                                toRemove.add(entity);
                            }
                        }
                    }

                    // Removing Blocks
                    for (BaseStatic blocks : handler.getMap().getBlocksOnMap()) {
                        if (blocks instanceof Dot) {
                            if (blocks.getBounds().intersects(handler.getPacman().getBounds())) {
                                handler.getMusicHandler().playEffect("pacman_chomp.wav");
                                toREmove.add(blocks);
                                handler.getScoreManager().addPacmanCurrentScore(10);
                            }
                        } else if (blocks instanceof BigDot) {
                            if (blocks.getBounds().intersects(handler.getPacman().getBounds())) {
                                handler.getMusicHandler().playEffect("pacman_chomp.wav");
                                toREmove.add(blocks);
                                handler.getScoreManager().addPacmanCurrentScore(50);
                                // makes ghosts edible
                                for (BaseDynamic ghost : handler.getMap().getEnemiesOnMap()) {
                                    if (ghost instanceof Ghost) {
                                        ((Ghost) ghost).setEdible(true);
                                        ((Ghost) ghost).resetEdibleCooldown();
                                    }
                                }
                            }
                        } else if (blocks instanceof Fruit) {
                            if (blocks.getBounds().intersects(handler.getPacman().getBounds())) {
                                handler.getMusicHandler().playEffect("pacman_eatFruit.wav");
                                toREmove.add(blocks);
                                handler.getScoreManager().addPacmanCurrentScore(120);
                            }
                        }
                    }

                    for (BaseStatic removing : toREmove) {
                        handler.getMap().getBlocksOnMap().remove(removing);
                    }

                    for (BaseDynamic removing : toRemove) {
                        handler.getMap().getEnemiesOnMap().remove(removing);
                    }

                    // debug commands
                    if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_N) && handler.getPacman().health < 3 && Handler.DEBUG) {
                        handler.getPacman().health++; // add health up to a max of 3
                    } else if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_P) && handler.getPacman().health > 0 && Handler.DEBUG) {
                        handler.getPacman().hit = true; // kill pacman
                    } else if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_C) && Handler.DEBUG) {
                        addGhosts(1, true);  // Add ghost
                    }

                    // Auto Level Up
                    else if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER) && Handler.DEBUG) {
                        Mode = "LevelUp";
                        resetHeMan();
                        return;
                    } else if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_K) && Handler.DEBUG) {
                        Mode = "GameOver";
                        return;
                    }


                    if (heManCanSpawn) {
                        if (heManSpawnTime == 0 && !getHeMan().canMove && heManIsDead) {
                            getHeMan().canMove = true;
                            handler.getMusicHandler().playEffect("heman.wav");
                            handler.getMusicHandler().changeMusic("hemantheme.wav");
                            heManIsDead = false;
                        } else if (heManSpawnTime < 0) {
                        } else {
                            heManSpawnTime--;
                        }

                        if (getHeMan().isDefeated()) {resetHeMan();}
                    }
                } else {
                    startCooldown--;
                }
                break;

            case "Menu":
                handler.getMusicHandler().stopMusic();
                if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)) {
                    Mode = "Stage";
                    handler.setMap(MapBuilder.createMap(maps[level - 1], handler));
                    handler.getMusicHandler().playEffect("pacman_beginning.wav");
                }
                break;

            case "Intro":
                if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)) {
                    Mode = "Menu";
                }
                break;

            case "GameOver":
                handler.getMusicHandler().stopMusic();
                level = 1;
                // updates score
                Mode = "Intro";
                refresh();
                handler.getPacManEndGameState().refresh();
                handler.changeState(handler.getPacManEndGameState());
                break;

            case "LevelUp":
                handler.getMusicHandler().stopMusic();
                int previousHealth = handler.getPacman().health;
                refresh();
                level++;

                try {
                    handler.setMap(MapBuilder.createMap(maps[level - 1], handler));
                } catch (IndexOutOfBoundsException e) {
                    // If we have no more maps, loop back!
                    level = 1;
                    handler.setMap(MapBuilder.createMap(maps[level - 1], handler));
                }

                Mode = "Stage";
                handler.getPacman().health = previousHealth;
                handler.getMusicHandler().playEffect("pacman_beginning.wav");
        }
    }


    @Override
    public void render(Graphics g) {

        switch (Mode) {
            case "Stage":
                String pacManStatus;

                Graphics2D g2 = (Graphics2D) g.create();
                handler.getMap().drawMap(g2);
                g.setColor(Color.WHITE);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 32));
                g.drawString("Score: " + handler.getScoreManager().getPacmanCurrentScore(), 10, 25);
                g.drawString("High-Score: " + handler.getScoreManager().getPacmanHighScore(), 10, 75);
                // galaga sprites
                if (usingGalagaSprites)
                    g.drawString("Galaga Mode", 10, 225);
                // Pac-Man status
                if (handler.getPacman().isInvulnerable())
                    g.drawString("Status: Invulnerable", 10, 275);


                // Draw Pac-Man's lives
                for (int i = 0; i < handler.getPacman().health; i++) {
                    g.drawImage(Images.pacmanRight[0], (50 * i) + 10, 125, 45, 45, null);
                }

                if (respawnCooldown > 0) {
                    g.setColor(Color.RED);
                    g.drawString("READY", handler.getWidth() / 2 - 55, handler.getHeight() / 2);
                    respawnCooldown--;
                }
                break;

            case "Menu":
                g.drawImage(Images.start, handler.getWidth() / 4, 0, handler.getWidth() / 2, handler.getHeight(), null);
                break;

            case "Intro":
                g.drawImage(Images.intro, handler.getWidth() / 4, 0, handler.getWidth() / 2, handler.getHeight(), null);
        }
    }


    public void addGhosts(int amount, boolean override) {
    	for (BaseStatic entity : handler.getMap().getBlocksOnMap()) {
    		if (entity instanceof GhostSpawner) {
    			for (int i = 0; i < amount; i++) {
                    ((GhostSpawner) entity).spawnGhost(override);
    			}

    		}
    	}
    }


    public boolean dotsLeft() {
    	for (BaseStatic block : handler.getMap().getBlocksOnMap()) {
    		if (block instanceof Fruit || block instanceof BigDot || block instanceof Dot) {
    			return true;
    		}
    	}
    	return false;
    }


    private void resetHeMan() {
        heManSpawnTime =  new Random().nextInt(2 * 60) + 2 * 60;
        heManIsDead = true;
    }


    private HeMan getHeMan() {
    	for (BaseDynamic e : handler.getMap().getEnemiesOnMap()) {
    		if (e instanceof HeMan) {
    			return (HeMan) e;
    		}
    	}
    	
    	return null;
    }
    
    
    public boolean isUsingGalagaSprites() {
        return usingGalagaSprites;
    }
    
    
    private boolean ghostIsLastOfItsKind(Ghost g) {
    	boolean last = true;
    	for (BaseDynamic ghost : handler.getMap().getEnemiesOnMap()) {
    		if (ghost instanceof Ghost) {
    			Ghost g1 = (Ghost) ghost;
    			if (g1.equals(g)) {continue;}
    			
    			if (g1.ghostName.equals(g.ghostName)) {last = false; break;}
    			
    		}
    	}
    	
    	return last;
    }


    @Override
    public void refresh() {
        handler.getMap().reset();
        ghostCount = 0;
        startCooldown = 60 * 4;
        spawnCooldown = 0;
        respawnCooldown = 0;
        this.Mode = "Intro";
    }
}
