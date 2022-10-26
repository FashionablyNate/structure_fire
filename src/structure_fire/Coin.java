package structure_fire;

import jig.Collision;
import jig.ResourceManager;

public class Coin extends Tile {
    public Coin(float x, float y) {
        super(x, y);
        addImage(
                ResourceManager.getImage(
                        StructureFireGame.BG_WOODEN_PLANKS)
        );
        addImageWithBoundingBox(
                ResourceManager.getImage(
                        StructureFireGame.COIN)
        );
        isCollideable = false;
    }

    @Override
    public void update(final int delta, Player player, int row, int col) {
        Collision collides = this.collides(player);
        if ( collides != null ) {
            removeImage(
                    ResourceManager.getImage(
                            StructureFireGame.COIN)
            );
            if (!this.isCollected) {
                player.coins++;
                this.isCollected = true;
            }
        }
    }
}
