package structure_fire;

import jig.Entity;
import jig.ResourceManager;

public class Background extends Entity {
    public Background(float x, float y) {
        super(x, y);
        addImage(
                ResourceManager.getImage(
                        StructureFireGame.SKY)
        );
    }
}
