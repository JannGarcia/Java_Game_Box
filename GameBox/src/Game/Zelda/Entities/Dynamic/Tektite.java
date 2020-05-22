package Game.Zelda.Entities.Dynamic;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

public class Tektite extends Enemy {

	private final int animSpeed = 120;
	private final int damageAnimSpeedMult = 2;


	public Tektite(int x, int y, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
		this.speed = 2;
		this.power = 1;
		this.health = 5;
		damageAnim = new Animation(animSpeed / damageAnimSpeedMult, Images.TektiteDamage);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public void render(Graphics g){
		super.render(g);

	}

	@Override
	public void moveAlgorithm() {
		moveRandomly();
	}



	@Override
	protected int stayInPlaceCooldown() {return 60;}

	@Override
	protected int moveCooldown() {return 20;}

}
