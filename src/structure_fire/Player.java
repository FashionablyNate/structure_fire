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
        translate(velocity.scale(delta));
    }

    public void control(Input input, StructureFireGame bg) {
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

        if (this.getCoarseGrainedMaxX() < bg.ScreenWidth) {
            // If user presses D or RIGHT, moves paddle to the right. If not and paddle moving to right, slows it down
            if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
                this.setVelocity(this.getVelocity().add(new Vector(+.006f, 0f)));
            } else if (this.getVelocity().getX() > 0) {
                this.setVelocity(this.getVelocity().add(new Vector(-.005f, 0f)));
            }
        } else {
            this.setVelocity(new Vector(0, 0));
            this.setX(bg.ScreenWidth - (this.getCoarseGrainedWidth() / 2) - 1);
        }
    }


    public void setVelocity(final Vector v) {
        velocity = v;
    }

    public Vector getVelocity() {
        return velocity;
    }
}
