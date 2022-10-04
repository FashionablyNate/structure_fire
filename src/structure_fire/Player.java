package structure_fire;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Input;

public class Player extends Entity {
    private Vector velocity;

    public Player( final float x, final float y ) {
        super ( x, y );
        addImageWithBoundingBox(
                ResourceManager.getImage(
                        StructureFireGame.PLAYER_CHARACTER)
        );
        velocity = new Vector(0, 0);
    }

    public void update(final int delta) {
        this.velocity = this.velocity.add(new Vector(0, 0.002f * delta));
        translate(velocity.scale(delta));
    }

    public void movement(Input input, StructureFireGame fg) {
        if (this.getCoarseGrainedMinX() > 0) {
            // If user presses A or LEFT, moves paddle to the left. If not and paddle moving to left, slows it down
            if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)) {
                this.setVelocity(this.getVelocity().add(new Vector(-.006f, 0f)));
            } else if (this.getVelocity().getX() < 0) {
                this.setVelocity(this.getVelocity().add(new Vector(+.005f, 0f)));
            }
        } else {
            this.setVelocity(new Vector(0, 0));
            this.setX(this.getCoarseGrainedWidth() / 2 + 1);
        }

        if (this.getCoarseGrainedMaxX() < fg.ScreenWidth) {
            // If user presses D or RIGHT, moves paddle to the right. If not and paddle moving to right, slows it down
            if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
                this.setVelocity(this.getVelocity().add(new Vector(+.006f, 0f)));
            } else if (this.getVelocity().getX() > 0) {
                this.setVelocity(this.getVelocity().add(new Vector(-.005f, 0f)));
            }
        } else {
            this.setVelocity(new Vector(0, 0));
            this.setX(fg.ScreenWidth - (this.getCoarseGrainedWidth() / 2) - 1);
        }
    }

    public void spray(Input input, StructureFireGame fg) {
        if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ) {
            Float modifier = (fg.rand.nextFloat() - 0.5f) * 50;
            Vector direction = new Vector(this.getX() - input.getAbsoluteMouseX() + modifier, this.getY() - input.getAbsoluteMouseY() + modifier );
            WaterParticle particle = new WaterParticle(this.getX() + (this.getCoarseGrainedWidth() / 2) - 5, this.getY() + 10);
            particle.setVelocity(direction.unit().negate());
            fg.water_stream.add(particle);
        }
    }


    public void setVelocity(final Vector v) {
        velocity = v;
    }

    public Vector getVelocity() {
        return velocity;
    }
}
