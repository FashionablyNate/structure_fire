package structure_fire;

import jig.Collision;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Tile extends Entity {

    public boolean isLadder = false;

    public Tile( final float x, final float y ) {
        super( x, y );
    }

    public void update(final int delta, Player player, int row, int col) {
    }
}
