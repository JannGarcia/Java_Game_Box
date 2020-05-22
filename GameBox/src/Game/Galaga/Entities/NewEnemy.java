package Game.Galaga.Entities;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class NewEnemy extends BaseEntity {
    int row,col;//row 3-4, col 0-7
    boolean justSpawned=true,attacking=false, positioned=false,hit=false,centered = false;
    Animation idle,turn90Left;
    int spawnPos;//0 is left 1 is top, 2 is right, 3 is bottom
    int formationX,formationY,speed,centerCoolDown=60;
    int timeAlive=0;
    int shootCooldown;
    public NewEnemy(int x, int y, int width, int height, Handler handler,int row, int col) {
        super(x, y, width, height, Images.galagaNewEnemy[0], handler);
        this.row = row;
        this.col = col;
        ((List) handler.getGalagaState().getGalagaGrid().get(row)).set(col, true);
        BufferedImage[] idleAnimList= new BufferedImage[2];
        idleAnimList[0] = Images.galagaNewEnemy[0];
        idleAnimList[1] = Images.galagaNewEnemy[1];
        idle = new Animation(512,idleAnimList);
        turn90Left = new Animation(128,Images.galagaNewEnemy);
        spawn();
        
        // Double speed and reduced launch rate if on hard mode
        speed = (handler.getGalagaState().difficulty.equals("baby")) ? 2 : handler.getGalagaState().difficulty.equals("hard") ? 8 : 4;
        shootCooldown = handler.getGalagaState().difficulty.equals("hard") ? (random.nextInt(2 * 60)) + 5 * 60 : (random.nextInt(6 * 60)) + 5 * 60;

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
                
//            case 3://down
//                x = random.nextInt((handler.getWidth()/2))+handler.getWidth()/4;
//                y = handler.getHeight()+height;
//                break;
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
                ((List) handler.getGalagaState().getGalagaGrid().get(row)).set(col, false);
                if (playerDamage == true) {  // If the enemy was killed by the player
                	handler.getScoreManager().addGalagaCurrentScore(100);
                }
                return;
            }
            enemyDeath.tick();
        }
        
        
        if (justSpawned){
            timeAlive++;
            if (!centered && Point.distance(x,y,handler.getWidth()/2,handler.getHeight()/2)>speed){//reach center of screen
                switch (spawnPos){
                    case 0://left
                        x+=speed;
                        if (Point.distance(x,y,x,handler.getHeight()/2)>speed) {
                            if (y > handler.getHeight() / 2) {
                                y -= speed;
                            } else {
                                y += speed;
                            }
                        }
                        break;
                    case 1://top
                        y+=speed;
                        if (Point.distance(x,y,handler.getWidth()/2,y)>speed) {
                            if (x > handler.getWidth() / 2) {
                                x -= speed;
                            } else {
                                x += speed;
                            }
                        }
                        break;
                    case 2://right
                        x-=speed;
                        if (Point.distance(x,y,x,handler.getHeight()/2)>speed) {
                            if (y > handler.getHeight() / 2) {
                                y -= speed;
                            } else {
                                y += speed;
                            }
                        }
                        break;
                        
//                    case 3://down
//                        y-=speed;
//                        if (Point.distance(x,y,handler.getWidth()/2,y)>speed) {
//                            if (x > handler.getWidth() / 2) {
//                                x -= speed;
//                            } else {
//                                x += speed;
//                            }
//                        }
//                        break;
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

                    if (Point.distance(x, y, formationX, formationY) > speed) {//reach center of screen

                    	
                        if (Math.abs(y-formationY)>=6) {
                            y -= speed;
                        }
                        
                        // We are close to Y, so just move them to Y
                        else { 
                        	y = formationY;
                        }
                        
                        if (Point.distance(x,y,formationX,y) > speed/2) {
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
        	// Attack     
        	if (shootCooldown == 0) {
        		
        	    attacking = true;
        	    positioned = false;
        	}

        	shootCooldown-- ;
        }
        
        else if (attacking) {
        	attack();
        	// Baby Mode: [5, 15] seconds, Hard Mode: [5, 7] seconds, Easy Mode: [5, 10] seconds
            shootCooldown = handler.getGalagaState().difficulty.equals("baby") ? random.nextInt(11) * 60  + 5 * 60 
            		: handler.getGalagaState().difficulty.equals("hard") ? random.nextInt(3) * 60 + 5 * 60 
            		: random.nextInt(6) * 60 + 5 * 60;
        	attacking = false;
    	    positioned = true;
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
            speed = 0; // Stop moving
            if (playSound) {
            	handler.getMusicHandler().playEffect("explosion.wav");
            	playSound = false;
            }
            damageSource.remove = true;
            
        }
    }
    
    public void attack() {
        handler.getGalagaState().entityManager.shoot.add(new EnemyLaser(this.x + (width / 2), this.y + 3, width / 5, height / 2, Images.galagaPlayerLaser, handler, handler.getGalagaState().entityManager));
    }
}

