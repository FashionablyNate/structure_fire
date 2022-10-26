package structure_fire;

import jig.Entity;
import jig.ResourceManager;

public class SprinklerHUDIcon extends Entity {
    public SprinklerHUDIcon(float x, float y) {
        super(x, y);
        addImage(
                ResourceManager.getImage(
                        StructureFireGame.SPRINKLER_HUD)
        );
    }
}
