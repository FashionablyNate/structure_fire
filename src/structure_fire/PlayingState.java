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
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		bounces = 0;
		container.setSoundOn(true);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		StructureFireGame fg = (StructureFireGame)game;
		
		fg.player.render( g );

		fg.map.forEach( (k, v) -> {
			v.render(g);
		});
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
		fg.tile_map.update_tiles( delta, fg );

		for ( WaterParticle p : fg.water_stream ) {
			p.update(delta);
			int p_row = (int) Math.floor(p.getY() / 50);
			int p_col = (int) Math.floor(p.getX() / 50);
			if (fg.map.containsKey( (p_row * 1000) + p_col ) ) {
				p.visible = false;
				fg.map.get(  (p_row * 1000) + p_col ).isOnFire = false;
			}
			if (p.getX() > fg.ScreenWidth || p.getX() < 0 || p.getY() > fg.ScreenHeight)
				p.visible = false;
		}

		fg.map.forEach( (k, v) -> {
			if ( v.isOnFire && !fg.flames.contains( v.flame ) ) {
				fg.flames.add( v.flame );
			} else if (!v.isOnFire) {
				fg.flames.remove( v.flame );
			}
		});

		fg.player.update( delta );
		fg.fl_enemy.move( delta, fg );
		fg.fl_enemy.update( delta );
	}

	@Override
	public int getID() {
		return StructureFireGame.PLAYINGSTATE;
	}
	
}