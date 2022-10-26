package structure_fire;

import jig.Entity;
import jig.ResourceManager;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class Lexicon extends Entity {
    private SpriteSheet lexicon;
    private int stage;

    public Lexicon(final float x, final float y) {
        super(x, y);
        lexicon = ResourceManager.getSpriteSheet(
                StructureFireGame.LEXICON, 50, 50
        );
        addImage( lexicon.getSubImage(0, 0) );
        this.stage = 0;
    }

    public void setFrame( int stage ) {
        if ( stage != this.stage ) {
            removeImage(lexicon.getSubImage(this.stage, 0));
            this.stage = stage;
            addImage(lexicon.getSubImage(stage, 0));
        }
    }
}