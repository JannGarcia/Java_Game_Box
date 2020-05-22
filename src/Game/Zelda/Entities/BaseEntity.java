package Game.Zelda.Entities;

import static Game.GameStates.Zelda.ZeldaGameState.worldScale;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Main.Handler;

/**
 * Created by AlexVR on 3/14/2020
 */
public class BaseEntity {
    public int x,y,width,height;
    public BufferedImage sprite;
    public Rectangle bounds;
    public Handler handler;

    public BaseEntity(int x, int y, BufferedImage sprite, Handler handler) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.handler = handler;
        if (sprite!=null) {
            width = sprite.getWidth() * worldScale;
            height = sprite.getHeight() * worldScale;
        }else{
            width = 1;
            height = 1;
        }
        bounds = new Rectangle(x,y,width,height);

    }

    public void tick(){

    }

    public void render(Graphics g) {

    }
}
