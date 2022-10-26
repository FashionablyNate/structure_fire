package structure_fire;

import jig.Entity;
import jig.ResourceManager;

public class HouseHUDIcon extends Entity {

    public HouseHUDIcon(float x, float y) {
        super(x, y);
        addImage(
                ResourceManager.getImage(
                        StructureFireGame.HOUSE)
        );
    }
}
