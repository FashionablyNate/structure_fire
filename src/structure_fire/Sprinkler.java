package structure_fire;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Sprinkler extends Entity {

    public int timeToSpray;

    public Sprinkler(float x, float y) {
        super(x, y);
        addImage(
                ResourceManager.getImage(
                        StructureFireGame.SPRINKLER)
        );
        timeToSpray = 10000;
    }

    public void update( StructureFireGame fg, final int delta ) {
        if ( this.timeToSpray > 0 ) {
            float modifier = (fg.rand.nextFloat() - 0.5f) * 0.5f;
            WaterParticle particle =
                    new WaterParticle(this.getX(), this.getY() + 12);
            particle.setVelocity(new Vector(modifier, -0.5f));
            fg.water_stream.add(particle);
            this.timeToSpray -= delta;
        }
    }
}
