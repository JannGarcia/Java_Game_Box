package Game.Galaga.Entities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;


public class HeMan extends BaseEntity {
    boolean justSpawned=true,attacking=false, positioned=false,hit=false,centered = false;
    Animation idle,turn90Left, attack;
    int spawnPos;//0 is left 1 is top, 2 is right, 3 is bottom
    int formationX,formationY = handler.getHeight(),speed,centerCoolDown=60;
    int timeAlive=0;
    int waitCooldown;
    int health = 2; // 3 hits
    int yDisplacement = 36; // He Man's X and Y coord should start at his head
    int soundCooldown = 6 * 60; // TODO: Add He-Man's attack duration
    int col;
    
    
    public HeMan(int x, int y, int width, int height, Handler handler, int col) {
        super(x, y, width, height, Images.heman[0], handler);
        this.col = col;
        formationX=(handler.getWidth()/4)+(col*((handler.getWidth()/2)/8))+8;
        BufferedImage[] idleAnimList= new BufferedImage[2];
        idleAnimList[0] = Images.heman[0];
        idleAnimList[1] = Images.heman[1];
        idle = new Animation(512,idleAnimList);
        
        handler.getGalagaState().getGalagaGrid().get(1).set(col, true);

        // Add He-Man's attack
        BufferedImage[] attackAnimList = new BufferedImage[8];
        attackAnimList[0] = Images.heman[0];
        attackAnimList[1] = Images.heman[1];
        attackAnimList[2] = Images.heman[2];
        attackAnimList[3] = Images.heman[3];
        attackAnimList[4] = Images.heman[4];
        attackAnimList[5] = Images.heman[3];
        attackAnimList[6] = Images.heman[2];
        attackAnimList[7] = Images.heman[1];
        attack = new Animation (512, attackAnimList);
        
        spawn();
        
        // Baby Mode: / 2 , Hard Mode: *2, Easy Mode: * 1
        speed = (handler.getGalagaState().difficulty.equals("baby")) ? 2 : handler.getGalagaState().difficulty.equals("hard") ? 8 : 4;
        
        // [5, 10] seconds on hard, [5, 18] otherwise
        waitCooldown = handler.getGalagaState().difficulty.equals("hard") ? (random.nextInt(6 * 60)) + 5 * 60 : (random.nextInt(13 * 60)) + 5 * 60;
   

    }

    private void spawn() {

        x = formationX;
        y = -height;
        bounds.x= x;
        bounds.y= y;
    }
    
    

    @Override
    public void tick() {
        super.tick();
        
        if (!attacking) {
        	idle.tick();
        }
        
        else if(attacking) {
        	attack.tick();
        }
        
        if (hit){
        	if (health > 0) {
        		health--;
        		hit = false;
        		playSound = true;

        	}
        	
        	else {
                speed = 0;

        		if (enemyDeath.end){
                    remove = true;
                    handler.getGalagaState().getGalagaGrid().get(1).set(col, false);

                    if (playerDamage == true) {	// If the enemy was killed by the player
                    	handler.getScoreManager().addGalagaCurrentScore(1000);                	
                    }                

                    return;
                }
                enemyDeath.tick();
        		
        	}
        	
        	
        }
        
        if (justSpawned){
            timeAlive++;
            if (!centered && Point2D.distance(x ,y, formationX ,handler.getHeight()/2) - yDisplacement >speed){//reach center of screen
            	
            	y+=speed;
            	
                if (timeAlive==60*10){

                	playerDamage = false;
                	playSound = false;
                	damage(new PlayerLaser(0,0,0,0, Images.flipVertical(Images.galagaPlayerLaser), handler, handler.getGalagaState().entityManager));
                	
                }

            }else {
                if (!centered){
                    centered = true;
                    timeAlive = 0;
                }
                if (centerCoolDown<=0){  	

                    if (Point2D.distance(x , y, x, formationY * .10 - yDisplacement) > speed) {//reach center of screen

                    	
                        if (Math.abs(y-formationY - yDisplacement)>=6) {
                            y -= speed;
                        }
                        
                        // We are close to Y, so just move them to Y
                        else { 
                        	y = formationY;
                        }
                    }else{

                        positioned =true;
                        justSpawned = false;

                    }
                }else{
                    centerCoolDown--;
                }
                if (timeAlive==60*10){  // Changed to == because >= causes sound to glitch out

                	playerDamage = false;
                	playSound = false;
                	damage(new PlayerLaser(0,0,0,0, Images.flipVertical(Images.galagaPlayerLaser), handler, handler.getGalagaState().entityManager));
                }
            }
        }else if (positioned){
        	// Use the randomly generated cooldown above (when enemy was created) to attack the player
        	
        	// Attack every 4 seconds
        	if (waitCooldown % (4 * 60) == 0) {
        		int playerX = handler.getGalagaState().entityManager.playerShip.x;
        		playerX += handler.getGalagaState().entityManager.playerShip.width / 2;
        		
                handler.getGalagaState().entityManager.shoot.add(new EnemyLaser(playerX, 0, width / 5, height / 2, Images.flipVertical(Images.galagaPlayerLaser), handler, handler.getGalagaState().entityManager));
        	}
        	
        	// Attack in the last 5 seconds
        	if (waitCooldown == 5 * 60) {
        		for (int i = 0; i < handler.getWidth(); i += (120 + random.nextInt(60))) {
                    handler.getGalagaState().entityManager.shoot.add(new EnemyLaser(i, 0, width / 5, height / 2, Images.flipVertical(Images.galagaPlayerLaser), handler, handler.getGalagaState().entityManager));
       	     }
        		
        	}
        	
        	if (waitCooldown == 0) {
        		attacking = true;
            	positioned = false;
        	}

        	else {
        		waitCooldown--;
        	}

        }

        else if (attacking){
        	
        	// Ultimate Attack
        	int attackType = random.nextInt(2);

        	// Play sound once
        	if (soundCooldown == 6 * 60) { 
        		handler.getMusicHandler().playEffect("heManAttack.wav");
        	}

        	// Summon 5 lasers
        	if (soundCooldown == 0 && attackType == 0) {
        	     for (int i = 0; i < handler.getWidth(); i += (120 + random.nextInt(35))) {
                     handler.getGalagaState().entityManager.shoot.add(new EnemyLaser(i, 0, width / 5, height / 2, Images.flipVertical(Images.galagaPlayerLaser), handler, handler.getGalagaState().entityManager));
        	     }
        	     soundCooldown = 6 * 60;
                 waitCooldown = handler.getGalagaState().difficulty.equals("hard") ? (random.nextInt(6 * 60)) + 5 * 60 : (random.nextInt(13 * 60)) + 5 * 60;
        	     attacking = false;
         		 positioned = true;
         		 attack.reset();
        		
        	}
        	
        	// Force every enemy to attack
        	else if (soundCooldown == 0 && attackType == 1) {
        		for (BaseEntity enemy : handler.getGalagaState().entityManager.entities) {
        			if (enemy instanceof Enemy) {((Enemy) enemy).attack();}	
        		}
        		soundCooldown = 6 * 60;
                waitCooldown = handler.getGalagaState().difficulty.equals("hard") ? (random.nextInt(6 * 60)) + 5 * 60 : (random.nextInt(13 * 60)) + 5 * 60;
                attacking = false;
        		positioned = true;
        		attack.reset();
        		 
        	}

        	else {
        		soundCooldown--;
        		
        	}

        }
        bounds.x=x;
        bounds.y=y;
    }
    
    
    @Override
    public void render(Graphics g) {
        ((Graphics2D)g).draw(new Rectangle(formationX,formationY,32,32));
        if (arena.contains(bounds)) {
            if (hit){
                g.drawImage(enemyDeath.getCurrentFrame(), x, y, width, height, null);
            }
            else if (attacking){
            	g.drawImage(attack.getCurrentFrame(), x, y, width, height, null);
            }
            else{
                g.drawImage(idle.getCurrentFrame(), x, y, width, height, null);

            }
        }
    }

    @Override
    public void damage(BaseEntity damageSource) {
        super.damage(damageSource);
        if (damageSource instanceof PlayerLaser){
            hit=true;

            if (playSound) {
            	handler.getMusicHandler().playEffect("explosion.wav");
            	playSound = false;
            }
            damageSource.remove = true;
            
        }
    }
}
