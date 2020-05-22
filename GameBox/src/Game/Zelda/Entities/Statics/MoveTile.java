package Game.Zelda.Entities.Statics;

import java.awt.image.BufferedImage;

import Game.Zelda.Entities.MMBaseEntity;
import Game.Zelda.Entities.Dynamic.Direction;
import Game.Zelda.World.Map;
import Main.Handler;

public class MoveTile extends MMBaseEntity { // Or MMSolidStaticEntities depends
	
	Direction facing;

	public MoveTile(int x, int y, BufferedImage sprite, Handler handler, Direction direction) {
		super(x, y, sprite, handler);
		this.facing = direction;
	}
	
	public void moveLink(int tries) {
		// Link got stuck
		if (tries >= 100) {return;}

		Map.link.move(this.facing);
		
		for(MMBaseEntity blocks: Map.blocksOnMap) {
			// Base Case
			if (!(blocks instanceof MoveTile) && blocks.bounds.intersects(Map.link.bounds)){
				return;
			}
			else if(blocks instanceof MoveTile && blocks.bounds.intersects(Map.link.bounds)){
				((MoveTile)blocks).moveLink(tries+1);
			}

		}
		
	}
	

    @Override
    public void tick() {
    	super.tick();
        if (Map.link.interactBounds.intersects(bounds)){
        	moveLink(0);
            return;
        }
    }

}
