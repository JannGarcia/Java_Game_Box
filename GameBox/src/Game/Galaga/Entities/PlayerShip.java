package Game.Galaga.Entities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import Game.GameStates.State;

/**
 * Created by AlexVR on 1/25/2020
 */
public class PlayerShip extends BaseEntity{

    private int health = 3,attackCooldown = 30,speed =6,destroyedCoolDown = 60*7, invulnerableCooldown = 60;
    private boolean attacking = false, destroyed = false;
    private Animation deathAnimation;

 
     public PlayerShip(int x, int y, int width, int height, BufferedImage sprite, Handler handler) {
        super(x, y, width, height, sprite, handler);
        

        deathAnimation = new Animation(256,Images.galagaPlayerDeath);
        }

    

    @Override
    public void tick() {
        super.tick();
        if (destroyed){
            if (destroyedCoolDown<=0){
                destroyedCoolDown=60*7;
                destroyed=false;
                deathAnimation.reset();
                bounds.x=x;
                
                // Can't get hurt for one second
                invulnerableCooldown = 60;
            }else{
                deathAnimation.tick();
                destroyedCoolDown--;
            }
        }else {
            if (attacking) {
                if (attackCooldown <= 0) {
                    attacking = false;
                } else {
                    attackCooldown--;
                }
            }
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER) && !attacking) {
                handler.getMusicHandler().playEffect("laser.wav");
                
                
                // If baby mode, reduce cooldown, if hard, increase, otherwise keep at 30
                attackCooldown = (handler.getGalagaState().difficulty.equals("baby")) ? 15 : (handler.getGalagaState().difficulty.equals("hard")) ? 60:30;
                
                
                attacking = true;
                handler.getGalagaState().entityManager.entities.add(new PlayerLaser(this.x + (width / 2), this.y - 3, width / 5, height / 2, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));

            }
            if (handler.getKeyManager().left && x > handler.getWidth() / 4 + 4){
                x -= (speed);
                
            }
            if (handler.getKeyManager().right && x < handler.getWidth() / 2 + handler.getWidth() / 4 - (18 * 4)) {
                x += (speed);
            }
            

            // Debug keys
            
          //Test KirbyAttack
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_K) && Handler.DEBUG) {
            	if (handler.getScoreManager().getGalagaCurrentScore() >= 2000) {
            		handler.getScoreManager().setGalagaCurrentScore(handler.getScoreManager().getGalagaCurrentScore()-2000);
            	handler.getMusicHandler().playEffect("KirbyWarpStar.wav");
            	
            	for (BaseEntity enemy : handler.getGalagaState().entityManager.entities) {
            		if (enemy instanceof KirbyAttack) {
            			if ( ((KirbyAttack) enemy).direction == 1 ) {
            				((KirbyAttack) enemy).direction = 0;
            			}
            			else {
            				((KirbyAttack) enemy).direction = 1;
            			}
            		}
            	}
            	
            	int direction = random.nextInt(2);
                int KirbyX = direction == 1 ? handler.getWidth() - 10 : 10;
                int KirbyY =  ((random.nextInt(4) + 1) *(handler.getHeight()/10))+8;
        		handler.getGalagaState().entityManager.entities.add(new KirbyAttack(KirbyX ,KirbyY, width, height, Images.KirbyPowerUp[0], handler, handler.getGalagaState().entityManager));
            }

            }
            
            // Summon HeMan
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_T) && Handler.DEBUG) {

        		List<List<Boolean>> grid = handler.getGalagaState().getGalagaGrid();
        		
        		// He-Man can only spawn in the first row
        		boolean shouldSpawn = false;
        		
        		// Check if the first row is empty
        		for (int i = 0; i < grid.get(1).size(); i++) {
        			if (grid.get(1).get(i) == false) {
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
        			if (grid.get(1).get(col).toString() == "false") {
        				handler.getGalagaState().entityManager.entities.add(new HeMan(10, 10, 60, 80, handler, col));
                    	handler.getMusicHandler().playEffect("heman.wav");	
                    	shouldSpawn = false;
        			}
            	}
        	
            }
            
            //Prevents Negative life You need to remember to be positive in the face of adversity
            if (health < 0){health = 0;}
            if (health > 3) {health = 3;}
            
            // Gives a life to the player
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_H) && Handler.DEBUG) {
            	if (health < 3) {
            		health ++ ; 
            	}
            }
            // Initiates GameOver Sequence
            if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_U) && Handler.DEBUG) {
            	handler.getGalagaState().Mode = "GameOver";
            	handler.getMusicHandler().changeMusic("LegendGameOver.wav");
            }
            
            // Kills the player
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_N) && Handler.DEBUG) {
            	invulnerableCooldown = 0;
            	damage(null);
            }
            
            // Gives 100 to the player
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_1) && Handler.DEBUG) {
            	handler.getScoreManager().setGalagaCurrentScore(handler.getScoreManager().getGalagaCurrentScore() + 100); 
            }
            
            // Kill all enemies
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_X) && Handler.DEBUG) {
            	for (BaseEntity enemy : handler.getGalagaState().entityManager.entities) {
            		enemy.playerDamage = false;
            		enemy.playSound = false;
            		enemy.damage(new PlayerLaser(0,0,0,0, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));
            	}
            }
           
          //Sends player to pause State
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)){
            	handler.getMusicHandler().changeMusic("ChemicalPlantPause.wav");
            	State.setState(handler.getPauseState());
            }
            
            // Spawns a Bee to a random unoccupied spot
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_P) && Handler.DEBUG) {
            	
            	// Get the grid of the bee
            	List<List<Boolean>> grid = handler.getGalagaState().getGalagaGrid();
            	List<List<Boolean>> beeGrid = grid.subList(3,5);
            	boolean shouldSpawn = false;
            	
            	// Check if the bee should be able to spawn
            	for (int i = 0; i < beeGrid.size(); i ++) {
            		for (int k = 0; k < beeGrid.get(i).size(); k ++) {
            			if (beeGrid.get(i).get(k) != null && !beeGrid.get(i).get(k)) {
            				shouldSpawn = true;
            				break;
            			}
            		}
            	}
            	
            	while (shouldSpawn == true) {
            		int row = random.nextInt(2) + 3;
                    int col = random.nextInt(8); 
                    
                    if (grid.get(row).get(col) != null && !grid.get(row).get(col)) {
                    	handler.getGalagaState().entityManager.entities.add(new EnemyBee(10, 10, 30, 30, handler, row, col));
                    	shouldSpawn = false;
                    }
                    
            	}
            	
            }  
            
            // Summon New Enemy
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_O) && Handler.DEBUG) {
            	
            	// Get the grid for the new enemy
            	List<List<Boolean>> grid = handler.getGalagaState().getGalagaGrid();
            	List<List<Boolean>> shootGrid = grid.subList(1, 3);
            	
            	boolean shouldSpawn = false;
            	
            	// Check if the new enemy should be able to spawn
            	for (int i = 0; i < shootGrid.size(); i ++) {
            		for (int k = 0; k < shootGrid.get(i).size(); k ++) {
            			if (shootGrid.get(i).get(k) != null && !shootGrid.get(i).get(k)) {
            				shouldSpawn = true;
            				break;
            			}
            		}
            	}
            	
            	while (shouldSpawn == true) {
            		int row = random.nextInt(2) + 1;  // random.nextInt(2) + 3;
                    int col = random.nextInt(6) + 1;
                    
                    if (grid.get(row).get(col) != null && !grid.get(row).get(col)) {
                    	handler.getGalagaState().entityManager.entities.add(new NewEnemy(10, 10, 30, 30, handler, row, col));
                    	shouldSpawn = false;
                    }
                    
            	}

            }
            
            
            // Give a free health for every 10000 points on the secret Baby Mode
            for (int i = 10000; i <= handler.getScoreManager().getGalagaCurrentScore(); i += 10000) {
            	if ( handler.getScoreManager().getGalagaCurrentScore() - i == 0 && handler.getGalagaState().difficulty.equals("baby") && health < 3) {
            		health++;
            	}
            }
            
            bounds.x = x;
            invulnerableCooldown--;
            
        }

    }

   

	@Override
    public void render(Graphics g) {
         if (destroyed){
             if (deathAnimation.end){
            	 g.setColor(Color.MAGENTA);
                 g.drawString("READY",handler.getWidth()/2-handler.getWidth()/12,handler.getHeight()/2);
             }else {
                 g.drawImage(deathAnimation.getCurrentFrame(), x, y, width, height, null);
             }
         }else {
             super.render(g);
         }
    }

    @Override
    public void damage(BaseEntity damageSource) {
        if (damageSource instanceof PlayerLaser || damageSource instanceof KirbyAttack || invulnerableCooldown > 0){
            return;
        }
        health--;
        destroyed = true;
        handler.getMusicHandler().playEffect("explosion.wav");
        bounds.x = -10;
        
        if (health == 0) {
        	handler.getMusicHandler().changeMusic("LegendGameOver.wav");
        }
        
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
    
    public boolean isDestroyed() {
    	return this.destroyed;
    }

}
