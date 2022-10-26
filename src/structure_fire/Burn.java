package structure_fire;

import jig.Entity;
import jig.ResourceManager;
import org.newdawn.slick.Animation;

public class Burn extends Entity {
    private Animation flame;

    public Burn(final float x, final float y) {
        super(x, y);
        flame = new Animation(ResourceManager.getSpriteSheet(
                StructureFireGame.BURN_FIRE_IMG_RSC, 50, 65), 0, 0, 3, 0, true, 100,
                true);
        addAnimation(flame);
        flame.setLooping(true);
    }

    public boolean isActive() {
        return !flame.isStopped();
    }
}
