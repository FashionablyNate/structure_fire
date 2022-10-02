package structure_fire;

import java.util.ArrayList;

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
			bg.map.get(row).add(col, new Tile((col * 50) + 25, (row * 50) + 25));
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
		for ( ArrayList<Tile> r : fg.map ) {
			for ( Tile t : r ) {
				t.render( g );
			}
		}

		for ( WaterParticle p : fg.water_stream ) {
			p.render(g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		StructureFireGame fg = (StructureFireGame)game;

		fg.player.movement( input, fg );
		fg.player.spray( input, fg );

		for ( ArrayList<Tile> r : fg.map ) {
			for ( Tile t : r ) {
				t.update( delta, fg.player );
			}
		}

		for ( WaterParticle p : fg.water_stream ) {
			p.update(delta);
		}

		fg.player.update( delta );
//		// structure_fire the ball...
//		boolean bounced = false;
//		if (bg.ball.getCoarseGrainedMaxX() > bg.ScreenWidth
//				|| bg.ball.getCoarseGrainedMinX() < 0) {
//			bg.ball.bounce(90);
//			bounced = true;
//		} else if (bg.ball.getCoarseGrainedMaxY() > bg.ScreenHeight
//				|| bg.ball.getCoarseGrainedMinY() < 0) {
//			bg.ball.bounce(0);
//			bounced = true;
//		}
//		if (bounced) {
//			bg.explosions.add(new Bang(bg.ball.getX(), bg.ball.getY()));
//			bounces++;
//		}
//		bg.ball.update(delta);
//
//		// check if there are any finished explosions, if so remove them
//		for (Iterator<Bang> i = bg.explosions.iterator(); i.hasNext();) {
//			if (!i.next().isActive()) {
//				i.remove();
//			}
//		}
//
//		if (bounces >= 10) {
//			((GameOverState)game.getState(StructureFireGame.GAMEOVERSTATE)).setUserScore(bounces);
//			game.enterState(StructureFireGame.GAMEOVERSTATE);
//		}
	}

	@Override
	public int getID() {
		return StructureFireGame.PLAYINGSTATE;
	}
	
}