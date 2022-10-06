package structure_fire;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import static com.apple.eio.FileManager.getResource;


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

		int row = (int) Math.floor(fg.player.getY() / 50);
		int col = (int) Math.floor(fg.player.getX() / 50);

		if ( input.isKeyDown(Input.KEY_SPACE) &&
				fg.map.containsKey(( row * 1000) + col ) &&
				fg.map.get( (row * 1000) + col ).isLadder ) {
			fg.map.get((row * 1000) + col).update(delta, fg.player, 0, 0);
		} else {
			for (int x = row - 1; x <= row + 1; x++) {
				for (int y = col - 1; y <= col + 1; y++) {
					if (x != 0 && y != 0) {
						if (fg.map.containsKey((x * 1000) + y))
							fg.map.get((x * 1000) + y).update(delta, fg.player, x - row, y - col);
					}
				}
			}
		}

//		if ( fg.map.containsKey( (row * 1000) + col ) )
//			fg.map.get( (row * 1000) + col ).update( delta, fg.player );
//		else if ( fg.map.containsKey( ( (row + 1) * 1000) + col ) ) {
//			if (fg.map.containsKey((row * 1000) + (col + 1)))
//				fg.map.get((row * 1000) + (col + 1)).update(delta, fg.player);
//			else if (fg.map.containsKey((row * 1000) + (col - 1)))
//				fg.map.get((row * 1000) + (col - 1)).update(delta, fg.player);
//			fg.map.get(((row + 1) * 1000) + col).update(delta, fg.player);
//		} else if ( fg.map.containsKey( ( (row - 1) * 1000) + col ) )
//			fg.map.get( ( (row - 1) * 1000 ) + col ).update( delta, fg.player );
//		else if ( fg.map.containsKey( ( row * 1000) + (col + 1) ) )
//			fg.map.get( (row * 1000) + (col + 1) ).update( delta, fg.player );
//		else if ( fg.map.containsKey( ( row * 1000) + (col - 1) ) )
//			fg.map.get( (row * 1000) + (col - 1) ).update( delta, fg.player );

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