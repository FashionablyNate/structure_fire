package structure_fire;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Iterator;


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

		Map.load( "level_one", fg );
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
//		g.drawString("Bounces: " + bounces, 10, 30);

		fg.map.forEach( (k, v) -> {
			v.render(g);
			if ( v.isOnFire && !fg.flames.contains( v.flame ) ) {
				fg.flames.add( v.flame );
			}
		});

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

		int row = (int) Math.floor(fg.player.getY() / 50);
		int col = (int) Math.floor(fg.player.getX() / 50);

		if (
			fg.map.containsKey(( row * 1000) + col ) &&
			fg.map.get( (row * 1000) + col ).isLadder
		) {
			fg.map.get( (row * 1000) + col ).update(delta, fg.player, 0, 0);
			for ( int y = col - 1; y <= col + 1; y++ ) {
				if ( y != 0 ) {
					if ( fg.map.containsKey( ( row * 1000 ) + y ) )
						fg.map.get( (row * 1000) + y ).update( delta, fg.player, 0, y - col );
				}
			}
		} else {
			for (int x = row - 1; x <= row + 1; x++) {
				for (int y = col - 1; y <= col + 1; y++) {
					if (fg.map.containsKey((x * 1000) + y))
						fg.map.get((x * 1000) + y).update(delta, fg.player, x - row, y - col);
				}
			}
		}

		for ( WaterParticle p : fg.water_stream ) {
			p.update(delta);
			int p_row = (int) Math.floor(p.getY() / 50);
			int p_col = (int) Math.floor(p.getX() / 50);
			if (fg.map.containsKey( (p_row * 1000) + p_col ) ) {
				p.visible = false;
			}
			if (p.getX() > fg.ScreenWidth || p.getX() < 0 || p.getY() > fg.ScreenHeight)
				p.visible = false;
		}

		for (Iterator<Burn> i = fg.flames.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
		}

		fg.player.update( delta );
	}

	@Override
	public int getID() {
		return StructureFireGame.PLAYINGSTATE;
	}
	
}