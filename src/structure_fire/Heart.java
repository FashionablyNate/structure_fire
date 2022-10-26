package structure_fire;

import jig.Entity;
import jig.ResourceManager;

public class Heart extends Entity {
    public Heart(float x, float y) {
        super(x, y);
        addImage(
                ResourceManager.getImage(
                        StructureFireGame.HEART)
        );
    }
}
