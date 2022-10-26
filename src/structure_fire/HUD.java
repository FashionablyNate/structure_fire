package structure_fire;

import jig.ResourceManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.util.ArrayList;

public class HUD {

    Lexicon score_digit_0, score_digit_1, score_digit_2, score_digit_3;
    private Integer percent_unburned;
    public Font font;
    private Civilian civilian;
    private Coin coin;
    private HouseHUDIcon house;
    public TrueTypeFont ttf;
    private ArrayList<Tile> background;

    public HUD( StructureFireGame fg ) {
        fg.water_gauge = new WaterGauge(200, 685);
        this.font = new Font("Serif", Font.PLAIN, 30);
        this.ttf = new TrueTypeFont(this.font, true);
        this.civilian = new Civilian(470, 730);
        this.civilian.removeImage(
                ResourceManager.getImage(
                        StructureFireGame.BG_WOODEN_PLANKS)
        );
        this.coin = new Coin(470, 680);
        this.coin.removeImage(
                ResourceManager.getImage(
                        StructureFireGame.BG_WOODEN_PLANKS)
        );
        this.house = new HouseHUDIcon(470, 625);
        this.background = new ArrayList<>();

        for ( int x = 0; x < 12; x++ ) {
            for ( int y = 12; y < 16; y++ ) {
                this.background.add(new BGPlanks((x * 50) + 25, (y * 50) + 25));
            }
        }
    }

    public void render( Graphics g, StructureFireGame fg ) {
        for ( Tile t : this.background ) {
            t.render( g );
        }
        fg.water_gauge.render( g );
        this.house.render( g );
        ttf.drawString(500, 610, ": " + this.percent_unburned.toString() + "%", Color.white);
        this.coin.render( g );
        ttf.drawString(500, 660, "x " + fg.player.coins, Color.white);
        this.civilian.render( g );
        ttf.drawString(500, 710, "x " + fg.player.civilians_saved, Color.white);
        for ( int i = 0; i < fg.sprinkler_inventory; i++ )
            new SprinklerHUDIcon( 50, 600 + ( i * 50 ) ).render(g);
    }

    public void update( SFTileMap tm ) {
        this.percent_unburned = (int) Math.ceil(100 * tm.flammable_tiles_left / tm.initial_flammable_tiles);
    }
}
