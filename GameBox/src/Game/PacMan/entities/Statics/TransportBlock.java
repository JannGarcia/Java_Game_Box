package Game.PacMan.entities.Statics;

import Main.Handler;
import Resources.Images;


public class TransportBlock extends BaseStatic {
	
	// The block the pacman can teleport from
	// TransportBlock RGB: (250, 80, 15)  HEX: fa500f
	
	public int animationTick = 30;	
	private TransportBlock destination;

    public TransportBlock(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height,handler,Images.bound[0]);
    }
    
    public void setDestination(TransportBlock block) {this.destination = block;}
    public TransportBlock getDestination() {return this.destination;}

}
