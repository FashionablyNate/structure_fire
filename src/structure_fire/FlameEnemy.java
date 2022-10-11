package structure_fire;

import jdk.internal.util.xml.impl.Pair;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;

import java.util.Stack;

public class FlameEnemy extends Entity {
    private Vector velocity;
    private int pos_x, pos_y, goal_x, goal_y;
    private boolean reached_goal;
    public boolean give_up;
    private Animation flame;
    private Stack<int[]> options;

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
        give_up = false;
        options = new Stack<>();

    }

    public void move( final int delta, StructureFireGame fg ) {

        if (reached_goal && !give_up) {
            pos_x = (int) Math.floor(this.getX() / 50);
            pos_y = (int) Math.floor(this.getY() / 50);
            int pl_x = -1, pl_y = -1, pot_x, pot_y, old_length = 1000;
            if (fg.civilians.size() > 0) {
                for ( int i = 0; i < fg.civilians.size(); i++ ) {
                    pot_x = fg.civilians.get(i)[1];
                    pot_y = fg.civilians.get(i)[0];
                    fg.path = fg.pathFinder.findPath(null, pos_x, pos_y, pot_x, pot_y);
                    if (fg.path != null) {
                        if ( fg.path.getLength() < old_length ) {
                            pl_x = pot_x; pl_y = pot_y;
                            old_length = fg.path.getLength();
                        }
                    }
                }
            } else {
                pl_x = (int) Math.floor(fg.player.getX() / 50);
                pl_y = (int) Math.floor(fg.player.getY() / 50) + 1;
            }
            if (pl_x == -1 && pl_y == -1)
                fg.path = null;
            else
                fg.path = fg.pathFinder.findPath(null, pos_x, pos_y, pl_x, pl_y);

            if (fg.map.containsKey( (pos_y * 1000) + pos_x )) {
                fg.map.get( (pos_y * 1000) + pos_x ).isOnFire = true;
            }

            if (
                    fg.path != null
            ) {
                goal_x = fg.path.getX(1);
                goal_y = fg.path.getY(1);
            } else {
                fg.map.forEach( (k, v) -> {
                    if (v.flammable && !v.isOnFire) {
                        options.push(new int[]{(int) Math.floor(v.getX() / 50), (int) Math.floor(v.getY() / 50)});
                    }
                });
                for ( int i = 0; i < options.size() && fg.path == null; i++ ) {
                    fg.path = fg.pathFinder.findPath(null, pos_x, pos_y, options.get(i)[0], options.get(i)[1] );
                }
                if (fg.path == null) {
                    give_up = true;
                } else {
                    goal_x = fg.path.getX(1);
                    goal_y = fg.path.getY(1);
                    options.clear();
                }
            }
            reached_goal = false;
        }
    }

    public void update(final int delta) {

        this.setVelocity(new Vector(
            0.002f * (((goal_x * 50) + 25) - this.getX()) * delta,
            0.002f * (((goal_y * 50) + 25) - this.getY()) * delta
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
