package Game.Galaga.Entities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;


public class EnemyBee extends BaseEntity {
    int row,col;//row 3-4, col 0-7
    boolean justSpawned=true,attacking=false, positioned=false,hit=false,centered = false, lockedOn = false;
    Animation idle,turn90Left;
    int spawnPos;//0 is left 1 is top, 2 is right, 3 is bottom
    int formationX,formationY,speed,centerCoolDown=60;
    int timeAlive=0;
    int waitCooldown;
    
    
    public EnemyBee(int x, int y, int width, int height, Handler handler,int row, int col) {
        super(x, y, width, height, Images.galagaEnemyBee[0], handler);
        this.row = row;
        this.col = col;
        handler.getGalagaState().getGalagaGrid().get(row).set(col, true);
        BufferedImage[] idleAnimList= new BufferedImage[2];
        idleAnimList[0] = Images.galagaEnemyBee[0];
        idleAnimList[1] = Images.galagaEnemyBee[1];
        idle = new Animation(512,idleAnimList);
        turn90Left = new Animation(128,Images.galagaEnemyBee);
        spawn();
        
        // Baby Mode: / 2 , Hard Mode: *2, Easy Mode: * 1
        speed = (handler.getGalagaState().difficulty.equals("baby")) ? 2 : handler.getGalagaState().difficulty.equals("hard") ? 8 : 4;
        
        // [5, 10] seconds on hard, [5, 18] otherwise
        waitCooldown = handler.getGalagaState().difficulty.equals("hard") ? (random.nextInt(6 * 60)) + 5 * 60 : (random.nextInt(13 * 60)) + 5 * 60;

        formationX=(handler.getWidth()/4)+(col*((handler.getWidth()/2)/8))+8;
        formationY=(row*(handler.getHeight()/10))+8;
   

    }

    private void spawn() {
        spawnPos = random.nextInt(3);
        switch (spawnPos){
            case 0://left
                x = (handler.getWidth()/4)-width;
                y = random.nextInt(handler.getHeight()-handler.getHeight()/8);
                break;
            case 1://top
                x = random.nextInt((handler.getWidth()-handler.getWidth()/2))+handler.getWidth()/4;
                y = -height;
                break;
            case 2://right
                x = (handler.getWidth()/2)+ width + (handler.getWidth()/4);
                y = random.nextInt(handler.getHeight()-handler.getHeight()/8);
                break;

        }
        bounds.x=x;
        bounds.y=y;
    }
    
    

    @Override
    public void tick() {
        super.tick();
        idle.tick();
        if (hit){
            if (enemyDeath.end){
                remove = true;
                handler.getGalagaState().getGalagaGrid().get(row).set(col, false);
                if (playerDamage == true) {	// If the enemy was killed by the player
                	handler.getScoreManager().addGalagaCurrentScore(100);                	
                }                

                return;
            }
            enemyDeath.tick();
        }
        
        if (justSpawned){
            timeAlive++;
            if (!centered && Point2D.distance(x,y,handler.getWidth()/2,handler.getHeight()/2)>speed){//reach center of screen
                switch (spawnPos){
                    case 0://left
                        x+=speed;
                        if (Point2D.distance(x,y,x,handler.getHeight()/2)>speed) {
                            if (y - 10 > handler.getHeight() / 2) {
                                y -= speed;
                            } else {
                                y += speed;
                            }
                        }
                        break;
                    case 1://top
                        y+=speed;
                        if (Point2D.distance(x,y,handler.getWidth()/2,y)>speed) {
                            if (x - 10> handler.getWidth() / 2) {
                                x -= speed;
                            } else {
                                x += speed;
                            }
                        }
                        break;
                    case 2://right
                        x-=speed;
                        if (Point2D.distance(x,y,x,handler.getHeight()/2)>speed) {
                            if (y - 10> handler.getHeight() / 2) {
                                y -= speed;
                            } else {
                                y += speed;
                            }
                        }
                        break;

                }
                if (timeAlive==60*10){   // if (timealive>=60*60*2  <== Original

                	playerDamage = false;
                	playSound = false;
                	damage(new PlayerLaser(0,0,0,0, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));
                	
                }

            }else {//move to formation
                if (!centered){
                    centered = true;
                    timeAlive = 0;
                }
                if (centerCoolDown<=0){

                    if (Point2D.distance(x, y, formationX, formationY) > speed) {//reach center of screen

                    	
                        if (Math.abs(y-formationY)>=6) {
                            y -= speed;
                        }
                        
                        // We are close to Y, so just move them to Y
                        else { 
                        	y = formationY;
                        }
                        
                        if (Point2D.distance(x,y,formationX,y) > speed/2) {
                            if (x >formationX) {
                                x -= speed;
                            } else {
                                x += speed;
                            }
                        }
                        
                        // We are close to X, so just move them to X
                        else { 
                        	x = formationX;
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
                	damage(new PlayerLaser(0,0,0,0, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));
                }
            }
        }else if (positioned){
        	// Use the randomly generated cooldown above (when enemy was created) to attack the player
        	if (waitCooldown == 0) {
        		attacking = true;
            	positioned = false;
        	}
        	
        	else {
        		waitCooldown--;
        	}

        	

        }else if (attacking){
        	attack();
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
            }else{
                g.drawImage(idle.getCurrentFrame(), x, y, width, height, null);

            }
        }
    }

    @Override
    public void damage(BaseEntity damageSource) {
        super.damage(damageSource);
        if (damageSource instanceof KirbyAttack) {
        	hit=true;
        	speed = 0;
        	if (playSound) {
        		handler.getMusicHandler().playEffect("explosion.wav");
             	playSound = false;
        }
        }	
        if (damageSource instanceof PlayerLaser){
            hit=true;
            speed = 0;  // Stop moving
            if (playSound) {
            	handler.getMusicHandler().playEffect("explosion.wav");
            	playSound = false;
            }
            damageSource.remove = true;
            
        }
    }
    
    public void attack() {
    	
    	// Dummy variables only used when He-Man calls this function
    	attacking = true; 
    	positioned = false;


    	// Keep getting the x coordinates of the ship
        
        int shipX = handler.getGalagaState().entityManager.playerShip.x;
        shipX += handler.getGalagaState().entityManager.playerShip.width / 2;
        // int shipY = handler.getGalagaState().entityManager.playerShip.y;

        if (lockedOn) { 
        	
            y += speed;  // Enemy will only move down
        

            // If enemy moves offscreen
        	if (this.y > handler.getHeight() + 50) {
        		playerDamage = false;  // Player never damaged it
        		lockedOn = false;  // It's not locked on anymore
        		attacking = false;  // It's not attacking anymore
        		playSound = false;  // Sound gets annoying
            	damage(new PlayerLaser(0,0,0,0, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));
            }
        
        }
        
        // Move left/right depending on ship's position
        else if (Point2D.distance(x,y,shipX,y)>speed/2) {
            if (x > shipX) {
                x -= speed;  // Left
            } 
            
            else {
                x += speed;  // Right
            }
        }
        
        else {
        	lockedOn = true;
        }


    
    }
}
