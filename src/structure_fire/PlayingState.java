package structure_fire;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


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
		StructureFireGame bg = (StructureFireGame)game;
		int row = 11;
		for ( int col = 0; col < 12; col++ ) {
			bg.map.put( (row * 1000) + col, new Tile( (col * 50) + 25, (row * 50) + 25 ));
		}
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

		fg.map.forEach( (k, v) -> v.render( g ) );

		fg.water_stream.removeIf(waterParticle -> !waterParticle.visible);
		for ( WaterParticle p : fg.water_stream )
			p.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		StructureFireGame fg = (StructureFireGame)game;

		fg.player.movement( input, fg );
		fg.player.spray( input, fg );

		int row = (int) Math.floor(fg.player.getY() / 50) + 1;
		int col = (int) Math.floor(fg.player.getX() / 50);
		for ( int i = row - 1; i <= row + 1; i++ ) {
			for ( int j = col - 1; j <= col + 1; j++ ) {
				if ( fg.map.containsKey( (i * 1000) + j ) )
					fg.map.get( (i * 1000) + j ).update(delta, fg.player);
			}
		}

		for ( WaterParticle p : fg.water_stream ) {
			p.update(delta);
			if (p.getX() > fg.ScreenWidth || p.getX() < 0 || p.getY() > fg.ScreenHeight)
				p.visible = false;
		}

		fg.player.update( delta );
	}

	@Override
	public int getID() {
		return StructureFireGame.PLAYINGSTATE;
	}
	
}