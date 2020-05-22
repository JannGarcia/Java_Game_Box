package Game.PacMan.entities.Statics;

import Main.Handler;
import Resources.Images;

public class BigDot extends BaseStatic{
	public int blinkCooldown = 60;
    public BigDot(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height, handler, Images.pacmanDots[0]);
    }
}
