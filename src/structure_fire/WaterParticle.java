package structure_fire;

import jig.Collision;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class WaterParticle extends Entity {

    private Vector velocity;

    public WaterParticle( final float x, final float y ) {
        super( x, y );
        addImageWithBoundingBox(
                ResourceManager.getImage(
                        StructureFireGame.WATER_PARTICLE)
        );
        velocity = new Vector(0, 0);
    }

    public void update(final int delta) {
        this.velocity = this.velocity.add(new Vector(0, 0.002f * delta));
        translate(velocity.scale(delta));
    }

    public void setVelocity(final Vector v) {
        velocity = v;
    }

    public Vector getVelocity() {
        return velocity;
    }
}
