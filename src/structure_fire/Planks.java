package structure_fire;

import jig.Collision;
import jig.ResourceManager;
import jig.Vector;

import java.util.Objects;

public class Planks extends Tile {

    public Planks(float x, float y) {
        super(x, y);
        addImageWithBoundingBox(
                ResourceManager.getImage(
                        StructureFireGame.WOODEN_PLANKS)
        );
    }

    @Override
    public void update(final int delta, Player player, int row, int col) {
        Collision collides = this.collides(player);
        if ( collides != null ) {
            if ( row == 1 && col == 0 ) {
                player.setVelocity( player.getVelocity().setY( 0.0f ) ) ;
                player.setY( this.getY() - player.getCoarseGrainedHeight() );
            }
            else if ( row == -1 && col == 0 ) {
                player.setVelocity( player.getVelocity().setY( 0.0f ) );
                player.setY( this.getY() + player.getCoarseGrainedHeight() + 2 );
            }
            else if ( row == 0 && col == 1 ) {
                player.setVelocity( player.getVelocity().setX( 0.0f ) );
                player.setX( this.getX() - player.getCoarseGrainedWidth() - 2 );
            }
            else if ( row == 0 && col == -1 ) {
                player.setVelocity( player.getVelocity().setX( 0.0f ) );
                player.setX( this.getX() + player.getCoarseGrainedWidth() + 2 );
            }
        }
    }
}
