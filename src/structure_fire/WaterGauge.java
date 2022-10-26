package structure_fire;

import jig.Entity;
import jig.ResourceManager;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class WaterGauge extends Entity {
    private SpriteSheet drain;
    private int stage;

    public WaterGauge(final float x, final float y) {
        super(x, y);
        drain = ResourceManager.getSpriteSheet(
                StructureFireGame.WATER_GAUGE, 100, 130
        );
        addImage( drain.getSubImage(0, 0) );
        this.stage = 0;
    }

    public void setFrame( int stage ) {
        if ( stage != this.stage ) {
            removeImage(drain.getSubImage(this.stage, 0));
            this.stage = stage;
            addImage(drain.getSubImage(stage, 0));
        }
    }
}