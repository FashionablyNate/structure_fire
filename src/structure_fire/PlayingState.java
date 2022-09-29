package structure_fire;

import java.util.Iterator;

import jig.Vector;

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
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		bounces = 0;
		container.setSoundOn(true);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		StructureFireGame bg = (StructureFireGame)game;
		
		bg.player.render( g );
		g.drawString("Bounces: " + bounces, 10, 30);
		for (Bang b : bg.explosions)
			b.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		StructureFireGame bg = (StructureFireGame)game;

		bg.player.control( input, bg );

		bg.player.update( delta );
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