package structure_fire;

import jig.ResourceManager;

public class BGPlanks extends Tile {

    public BGPlanks(float x, float y) {
        super(x, y);
        addImageWithBoundingBox(
                ResourceManager.getImage(
                        StructureFireGame.BG_WOODEN_PLANKS)
        );
        isCollideable = false;
        isBackground = true;
    }
}
