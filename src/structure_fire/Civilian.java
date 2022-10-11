package structure_fire;

import jig.Collision;
import jig.ResourceManager;
import jig.Vector;

public class Civilian extends Tile {

    public Civilian(float x, float y) {
        super(x, y);
        addImageWithBoundingBox(
                ResourceManager.getImage(
                        StructureFireGame.CIVILIAN)
        );
        setScale( 0.95f );
        isCivilian = true;
        timeToLive = 2000;
    }

    @Override
    public void update(final int delta, Player player, int row, int col) {
        Collision collides = this.collides(player);
        if ( collides != null ) {
            this.visible = false;
            this.saved = true;
        }
    }
}
