package structure_fire;

import java.awt.*;
import java.awt.Font;
import java.util.Iterator;

import jig.ResourceManager;

import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;

/**
 * This state is active prior to the Game starting. In this state, sound is
 * turned off, and the structure_fire counter shows '?'. The user can only interact with
 * the game by pressing the SPACE key which transitions to the Playing State.
 * Otherwise, all game objects are rendered and updated normally.
 * 
 * Transitions From (Initialization), GameOverState
 * 
 * Transitions To PlayingState
 */
class StartUpState extends BasicGameState {

	Font font;
	TrueTypeFont ttf;
	Background sky;

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		StructureFireGame fg = (StructureFireGame)game;
		this.font = new Font("Serif", Font.PLAIN, 64);
		this.ttf = new TrueTypeFont(this.font, true);
		sky = new Background(fg.ScreenWidth >> 1, fg.ScreenHeight >> 1);
		fg.tile_map = new SFTileMap( "level_one", fg );
		fg.pathFinder = new AStarPathFinder(
				fg.tile_map,
				SFTileMap.WIDTH * SFTileMap.HEIGHT,
				false
		);
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		StructureFireGame fg = (StructureFireGame)game;
		this.font = new Font("Serif", Font.PLAIN, 64);
		this.ttf = new TrueTypeFont(this.font, true);
		sky = new Background(fg.ScreenWidth >> 1, fg.ScreenHeight >> 1);
		container.setSoundOn(false);
		fg.tile_map = new SFTileMap( "level_one", fg );
		fg.pathFinder = new AStarPathFinder(
				fg.tile_map,
				SFTileMap.WIDTH * SFTileMap.HEIGHT,
				false
		);
	}


	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		StructureFireGame fg = (StructureFireGame)game;
		this.sky.render( g );
		fg.map.forEach( (k, v) -> {
			if (v.visible)
				v.render(g);
		});
		for (Burn b : fg.flames)
			b.render(g);
		this.ttf.drawString(
				(fg.ScreenWidth >> 1) - 200,
				(fg.ScreenHeight >> 1) - 50,
				"Press Space...",
				Color.white
		);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		StructureFireGame fg = (StructureFireGame)game;

		if ( fg.flames.size() == 0 ) {
			fg.map.clear();
			this.init(container, game);
			fg.map.get((1000 * (fg.rand.nextInt(9) + 2)) + fg.rand.nextInt(9) + 2).isOnFire = true;
			fg.map.get((1000 * (fg.rand.nextInt(9) + 2)) + fg.rand.nextInt(9) + 2).isOnFire = true;
			fg.map.get((1000 * (fg.rand.nextInt(9) + 2)) + fg.rand.nextInt(9) + 2).isOnFire = true;
		}

		fg.tile_map.update_tiles( delta, fg, input );

		if (input.isKeyDown(Input.KEY_SPACE)) {
			fg.map.clear();
			fg.flames.clear();
			fg.enterState(StructureFireGame.PLAYINGSTATE);
		}
	}

	@Override
	public int getID() {
		return StructureFireGame.STARTUPSTATE;
	}
	
}