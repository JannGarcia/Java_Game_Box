package Game.PacMan.entities.Dynamics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import Game.PacMan.entities.Statics.BaseStatic;
import Game.PacMan.entities.Statics.BigDot;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

public class HeMan extends BaseDynamic {
	
	private BufferedImage[] heMan1 = {Images.heman[0], Images.heman[1]}, heMan2 = Images.heman;
	private BufferedImage map;
	public Animation idleAnim = new Animation(512, heMan1);
	public Animation attackAnim = new Animation(512, heMan2);
	
	private int waitCooldown = new Random().nextInt(10 * 60) + 10 * 60; // [10, 20] seconds
	private int soundCooldown = 6 * 60;
	private int resetCooldown;
	private boolean attacking;
	public boolean playDeath = false;
	public boolean canMove = false;
	
	public HeMan(int x, int y, int width, int height, Handler handler) {
		super(x, y, width, height, handler, Images.bound[0]);
	}
	
	@Override
	public void tick() {
		
		if (resetCooldown == 0) {resetEffects();}
		else {resetCooldown--;}
		
		
		// Move to position
		if (this.getY() != handler.getHeight()/2 && canMove) {
			this.y = (int) (this.getY() < handler.getHeight() / 2 ? this.getY() + this.velY : this.getY() - this.velY);
		}
		
		else {
			
			if (attacking && canMove) {
				attackAnim.tick();
				
				int attackType = new Random().nextInt(5);
				
	        	if (soundCooldown == 6 * 60) { 
	        		handler.getMusicHandler().playEffect("hemanAttack.wav");
	        	}
	        	
	        	
	        	if (soundCooldown == 0) {
	        		
	        		boolean success = false;

	        		resetEffects();
	        		switch(attackType) {
	        		case 0: // Make dots invisible (Change Map Variable)
	        			handler.getMap().invisibleDots = true;
	        			resetCooldown = 8 * 60; // 8 seconds
	        			success = true;
	        			break;
	        		case 1: // Replace Ghosts with black boxes (Change Map Variable)
	        			handler.getMap().invisibleGhosts = true;
	        			resetCooldown = 300; // 5 seconds
	        			success = true;
	        			break;
	        			
	        		case 2: // Teleport PacMan
	        			success = teleportPacMan();
	        			break;
	        			
	        		case 3: // Give pac-man a fruit. Awwwww.
	        			handler.getScoreManager().addPacmanCurrentScore(120);
	        			handler.getMusicHandler().playEffect("pacman_eatfruit.wav");
	        			success = true;
	        			break;
	        			
	        		case 4:
	        			handler.getMap().invisibleGhosts = true;
	        			handler.getMap().invisibleDots = true;
	        			success = teleportPacMan();
	        			resetCooldown = 600;
	        			break;
	        		}
	        		
	        		if (success) {
	        			soundCooldown = 6 * 60;
	        			attacking = false;
	        			waitCooldown = new Random().nextInt(20 * 60) + 10 * 60; // [10, 30] seconds
	        			attackAnim.reset();
	        		}
	        	}
	        	
	        	else {soundCooldown--;}

			}
			
			else {
				// Just wait
				if (waitCooldown <= 0) {attacking = true;}
				else {waitCooldown--;}
				idleAnim.tick();
						
			}
			
		}
	}
	
	public boolean isAttacking() {return attacking;}
	public boolean isDefeated() {
		boolean defeated = true;
		for (BaseStatic block: handler.getMap().getBlocksOnMap()) {
			if (block instanceof BigDot) {
				defeated = false;
				break;
			}
		}
		
		return defeated;
	}
	
	public boolean teleportPacMan() {
		BufferedImage map = handler.getMap().currentMap;
		int pixelMultiplier = (handler.getHeight()-100)/map.getHeight();
		int emptyBlock = new Color(255,255,255).getRGB();
		
		for (int tries = 0; tries < 100; tries++) {
			int i = new Random().nextInt(map.getWidth());
			int j = new Random().nextInt(map.getHeight());
			int xPos = (i*pixelMultiplier)+((handler.getWidth()-(map.getWidth()*pixelMultiplier)))/2;
			int yPos = (j*pixelMultiplier)+((handler.getHeight()-(map.getHeight()*pixelMultiplier)))/2;
			int currentPixel = map.getRGB(i, j);
			
			if (currentPixel == emptyBlock) {
				handler.getPacman().setX(xPos);
				handler.getPacman().setY(yPos);
				return true;
			}

		}
		
		return false;
		
		
	}
	
	public void resetEffects() {
		handler.getMap().invisibleDots = false;
		handler.getMap().invisibleGhosts = false;
	}
}
