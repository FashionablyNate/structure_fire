package structure_fire;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class PathImage extends Entity {
    public PathImage( final float x, final float y ) {
        super ( x, y );
        addImage(
                ResourceManager.getImage(
                        StructureFireGame.PATH)
        );
    }
}
