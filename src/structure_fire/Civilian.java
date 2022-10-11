package structure_fire;

import jig.ResourceManager;

public class Civilian extends Tile {
    public Civilian(float x, float y) {
        super(x, y);
        addImageWithBoundingBox(
                ResourceManager.getImage(
                        StructureFireGame.CIVILIAN)
        );
        setScale( 0.95f );
    }
}
