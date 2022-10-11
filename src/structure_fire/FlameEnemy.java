package structure_fire;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;

public class FlameEnemy extends Entity {
    private Vector velocity;
    private int pos_x, pos_y, goal_x, goal_y;
    private boolean reached_goal;
    private Animation flame;

    public FlameEnemy( final float x, final float y ) {
        super ( x, y );
        flame = new Animation(ResourceManager.getSpriteSheet(
                StructureFireGame.BURN_FIRE_IMG_RSC, 50, 65), 0, 0, 3, 0, true, 100,
                true);
        addAnimation(flame);
        flame.setLooping(true);
        setScale( 0.95f );
        velocity = new Vector(0, 0);
        reached_goal = true;

    }

    public void move( final int delta, StructureFireGame fg ) {

        if (reached_goal) {
            int pl_y = (int) Math.floor(fg.player.getY() / 50) + 1;
            int pl_x = (int) Math.floor(fg.player.getX() / 50);
            pos_x = (int) Math.floor(this.getX() / 50);
            pos_y = (int) Math.floor(this.getY() / 50);

            if (fg.map.containsKey( (pos_y * 1000) + pos_x )) {
                fg.map.get( (pos_y * 1000) + pos_x ).isOnFire = true;
            }

            fg.path = fg.pathFinder.findPath(null, pos_x, pos_y, pl_x, pl_y);
            if (
                    fg.path != null
            ) {
                goal_x = fg.path.getX(1);
                goal_y = fg.path.getY(1);
                reached_goal = false;
            }
        }
    }

    public void update(final int delta) {

        this.setVelocity(new Vector(
            0.001f * (((goal_x * 50) + 25) - this.getX()) * delta,
            0.001f * (((goal_y * 50) + 25) - this.getY()) * delta
        ));
        float x_diff = (((goal_x * 50) + 25) - this.getX());
        float y_diff = (((goal_y * 50) + 25) - this.getY());
        if (
            (-0.05 < x_diff) &&
            (x_diff < 0.05) &&
            (-0.05 < y_diff) &&
            (y_diff < 0.05)
        ) {
            reached_goal = true;
        }
//        this.setPosition((float)((this.goal[0] * 50) + 25), (float)((this.goal[1] * 50) + 25));
        translate(velocity.scale(delta));
    }


    public void setVelocity(final Vector v) {
        velocity = v;
    }

    public Vector getVelocity() {
        return velocity;
    }
}
