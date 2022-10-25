package structure_fire;

import jig.Collision;
import jig.ResourceManager;

public class FireHydrant extends Tile {

    public FireHydrant(float x, float y) {
        super(x, y);
        addImageWithBoundingBox(
                ResourceManager.getImage(
                        StructureFireGame.HYDRANT)
        );
        isCollideable = false;
    }

    @Override
    public void update(final int delta, Player player, int row, int col) {
        Collision collides = this.collides(player);
        if ( collides != null ) {
            if ( player.water_level < 1.0f ) {
                player.water_level += 0.01;
            }
        }
    }
}
