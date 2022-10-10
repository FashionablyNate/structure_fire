package structure_fire;

import jig.ResourceManager;

public class Stone extends Planks {
    public Stone(float x, float y) {
        super(x, y);
        addImageWithBoundingBox(
                ResourceManager.getImage(
                        StructureFireGame.STONE)
        );
        flammable = false;
    }
}
