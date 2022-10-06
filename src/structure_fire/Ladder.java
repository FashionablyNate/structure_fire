package structure_fire;

import jig.Collision;
import jig.ResourceManager;
import jig.Vector;

public class Ladder extends Tile {

    public Ladder(float x, float y) {
        super(x, y);
        addImageWithBoundingBox(
                ResourceManager.getImage(
                        StructureFireGame.WOODEN_LADDER)
        );
        isLadder = true;
    }

    @Override
    public void update(final int delta, Player player, int row, int col) {
        Collision collides = this.collides(player);
        if ( collides != null ) {
            player.setVelocity(player.getVelocity().add(new Vector(0, -0.0022f * delta)));
        }
    }
}
