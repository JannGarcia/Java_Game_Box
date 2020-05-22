package Game.Galaga.Entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import Main.Handler;
import Resources.Animation;
import Resources.Images;

public abstract class Enemy extends BaseEntity {

    int row,col;
    boolean justSpawned = true, attacking = false, positioned = false, hit = false, centered = false;
    Animation idle, turn90Left;
    int spawnPos, formationX,formationY,speed,centerCoolDown=60;
    int timeAlive=0;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite, Handler handler, int row, int col) {
		super(x, y, width, height, sprite, handler);
		this.row = row;
		this.col = col;
        handler.getGalagaState().getGalagaGrid().get(row).set(col, true);
        formationX=(handler.getWidth()/4)+(col*((handler.getWidth()/2)/8))+8;
        formationY=(row*(handler.getHeight()/10))+8;
        speed = (handler.getGalagaState().difficulty.equals("baby")) ? 2 : handler.getGalagaState().difficulty.equals("hard") ? 8 : 4;
	}
	
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
                        x = x < handler.getWidth()/2 ? x+speed : x;
                        if (Point2D.distance(x,y,x,handler.getHeight()/2)>speed) {
                            if (y > handler.getHeight() / 2) {
                                y -= speed;
                            } else {
                                y += speed;
                            }
                        }
                        else {y = handler.getHeight()/2;}
                        break;
                    case 1://top
                        y = y < handler.getHeight()/2 ? y+speed : y;
                        if (Point2D.distance(x,y,handler.getWidth()/2,y)>speed) {
                            if (x > handler.getWidth() / 2) {
                                x -= speed;
                            } else {
                                x += speed;
                            }
                        }
                        else {x = handler.getWidth()/2;}
                        break;
                    case 2://right
                        x = x > handler.getWidth()/2 ? x-speed : x;
                        if (Point2D.distance(x,y,x,handler.getHeight()/2)>speed) {
                            if (y > handler.getHeight() / 2) {
                                y -= speed;
                            } else {
                                y += speed;
                            }
                        }
                        else {y = handler.getHeight()/2;}
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

                    if (Point2D.distance(x, y, formationX, formationY) > speed) {

                    	
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
        }
        bounds.x=x;
        bounds.y=y;
    
	}
	
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
	
	public void damage(BaseEntity damageSource) {
        super.damage(damageSource);
        if (damageSource instanceof KirbyAttack || damageSource instanceof PlayerLaser) {
        	hit=true;
        	speed = 0;
        	if (playSound) {
        		handler.getMusicHandler().playEffect("explosion.wav");
             	playSound = false;
        	}
        	
        	if (damageSource instanceof PlayerLaser) damageSource.remove = true;
        }	

    
	}
	
    protected void spawn() {
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
    
    public abstract void attack();

}
