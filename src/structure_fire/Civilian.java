package structure_fire;

import jig.Collision;
import jig.ResourceManager;
import jig.Vector;

public class Civilian extends Tile {

    public Civilian(float x, float y) {
        super(x, y);
        addImage(
                ResourceManager.getImage(
                        StructureFireGame.BG_WOODEN_PLANKS)
        );
        addImageWithBoundingBox(
                ResourceManager.getImage(
                        StructureFireGame.CIVILIAN)
        );
        isCivilian = true;
        timeToLive = 2000;
        isCollideable = false;
    }

    @Override
    public void update(final int delta, Player player, int row, int col) {
        Collision collides = this.collides(player);
        if ( collides != null ) {
            removeImage(
                    ResourceManager.getImage(
                            StructureFireGame.CIVILIAN)
            );
            if ( !this.saved ) {
                player.civilians_saved++;
                this.isCivilian = false;
            }
            this.saved = true;
        }
    }
}
