package structure_fire;

import jig.Collision;
import jig.ResourceManager;

public class Planks extends Tile {
    public Planks(float x, float y) {
        super(x, y);
        addImageWithBoundingBox(
                ResourceManager.getImage(
                        StructureFireGame.WOODEN_PLANKS)
        );
    }

    @Override
    public void update(final int delta, Player player) {
        Collision collides = this.collides(player);
        if ( collides != null ) {
            player.setVelocity( player.getVelocity().setY(0f) );
            player.setY( this.getY() - (this.getCoarseGrainedHeight()) );
        }
    }
}
