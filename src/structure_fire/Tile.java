package structure_fire;

import jig.Collision;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Tile extends Entity {

    public boolean isLadder = false;
    public boolean isCivilian = false;
    public boolean isCollideable = true;
    public boolean isCollected = false;
    public boolean isBackground = false;
    public boolean saved = false;
    public boolean flammable = true;
    public boolean isOnFire = false;
    public int timeToLive = 4000;
    public boolean visible = true;
    public Burn flame;

    public Tile( final float x, final float y ) {
        super( x, y );
        flame = new Burn(x, y);
    }

    public void update(final int delta, Player player, int row, int col) {
    }
}
