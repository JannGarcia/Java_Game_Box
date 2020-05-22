package Game.PacMan.entities.Dynamics;

import Game.PacMan.entities.BaseEntity;
import Main.Handler;

import java.awt.image.BufferedImage;

public class BaseDynamic extends BaseEntity {

    protected String direction = "Right";
    protected double velX=1,velY = 1;
    protected int xOnMap=0, yOnMap=0;

    public BaseDynamic(int x, int y, int width, int height, Handler handler, BufferedImage sprite) {
        super(x, y, width, height,handler,sprite);
    }
    
    public int getxOnMap() {return this.xOnMap;}
    public int getyOnMap() {return this.yOnMap;}
    
    public void tick(){

    }

}
