package structure_fire;

import jig.Collision;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Tile extends Entity {

    public Tile( final float x, final float y ) {
        super( x, y );
        addImageWithBoundingBox(
                ResourceManager.getImage(
                        StructureFireGame.WOODEN_PLANKS)
        );
    }

    public void update(final int delta, Player player) {
        Collision collides = this.collides(player);
        if ( collides != null ) {
            player.setVelocity( player.getVelocity().setY(0f) );
            player.setY( this.getY() - (this.getCoarseGrainedHeight()) );
        }
    }
}
