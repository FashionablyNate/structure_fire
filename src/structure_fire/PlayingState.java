package structure_fire;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;


/**
 * This state is active when the Game is being played. In this state, sound is
 * turned on, the structure_fire counter begins at 0 and increases until 10 at which
 * point a transition to the Game Over state is initiated. The user can also
 * control the ball using the WAS & D keys.
 * 
 * Transitions From StartUpState
 * 
 * Transitions To GameOverState
 */
class PlayingState extends BasicGameState {
	int bounces;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		StructureFireGame fg = (StructureFireGame)game;

		fg.tile_map = new SFTileMap( "level_one", fg );
		fg.pathFinder = new AStarPathFinder(
				fg.tile_map,
				SFTileMap.WIDTH * SFTileMap.HEIGHT,
				false
		);
		fg.fl_enemy.give_up = false;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		bounces = 0;
		container.setSoundOn(true);

		StructureFireGame fg = (StructureFireGame)game;

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
		
		fg.player.render( g );

		fg.map.forEach( (k, v) -> {
			if (v.visible)
				v.render(g);
		});
		if (!fg.fl_enemy.give_up)
			fg.fl_enemy.render( g );

		fg.water_stream.removeIf(waterParticle -> !waterParticle.visible);
		for ( WaterParticle p : fg.water_stream )
			p.render(g);
		for (Burn b : fg.flames)
			b.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		StructureFireGame fg = (StructureFireGame)game;

		fg.player.movement( input, fg );
		fg.player.spray( input, fg );
		fg.player.update( delta );

		fg.tile_map.update_tiles( delta, fg );

		fg.fl_enemy.move( delta, fg );
		fg.fl_enemy.update( delta );

		fg.civilians.removeIf( x -> fg.map.get( (x[0] * 1000) + x[1]).isOnFire);
		fg.civilians.removeIf( x -> fg.map.get( (x[0] * 1000) + x[1]).saved);

		if (fg.flames.size() == 0 && fg.fl_enemy.give_up) {
			fg.enterState(StructureFireGame.GAMEOVERSTATE);
		}
	}

	@Override
	public int getID() {
		return StructureFireGame.PLAYINGSTATE;
	}
	
}