package Game.PacMan.entities.Statics;

import Main.Handler;

import java.awt.image.BufferedImage;

public class SpawnerGate extends BaseStatic {
	
	// The gate that prevents pacman from entering the spawner
	// SpawnerGate Block RGB: (0, 10, 255)  HEX: 000aff

    public SpawnerGate(int x, int y, int width, int height, Handler handler, BufferedImage sprite) {
        super(x, y, width, height,handler,sprite);
    }

}
