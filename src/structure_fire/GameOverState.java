package structure_fire;

import java.awt.*;
import java.util.Iterator;

import jig.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;


/**
 * This state is active when the Game is over. In this state, the ball is
 * neither drawn nor updated; and a gameover banner is displayed. A timer
 * automatically transitions back to the StartUp State.
 * 
 * Transitions From PlayingState
 * 
 * Transitions To StartUpState
 */
class GameOverState extends BasicGameState {
	

	private int timer;
	private Font font, header_font;
	private TrueTypeFont ttf, header_ttf;
	private Coin coin;
	private Civilian civilian;
	private HouseHUDIcon house;

	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		this.header_font = new Font("Serif",Font.PLAIN, 64);
		this.header_ttf = new TrueTypeFont(this.header_font, true);
		this.font = new Font("Serif",Font.PLAIN, 32);
		this.ttf = new TrueTypeFont(this.font, true);
		this.civilian = new Civilian(170, 300);
		this.civilian.removeImage(
				ResourceManager.getImage(
						StructureFireGame.BG_WOODEN_PLANKS)
		);
		this.coin = new Coin(170, 400);
		this.coin.removeImage(
				ResourceManager.getImage(
						StructureFireGame.BG_WOODEN_PLANKS)
		);
		this.house = new HouseHUDIcon(170, 500);
		timer = 4000;
	}

	
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {

		StructureFireGame fg = (StructureFireGame)game;

		header_ttf.drawString(120, 150, "GAME OVER", org.newdawn.slick.Color.red);

		this.civilian.render( g );
		ttf.drawString(270, 285, "x " + fg.civilians_score, org.newdawn.slick.Color.white);
		this.coin.render( g );
		ttf.drawString(270, 375, "x " + fg.coins_score, org.newdawn.slick.Color.white);
		this.house.render( g );
		ttf.drawString(
				270,
				480,
				": " + fg.percentage_score
				+ "%", org.newdawn.slick.Color.white
		);
		header_ttf.drawString(
				120,
				580,
				"Score: " +
				fg.percentage_score * (fg.coins_score + 1) * (fg.civilians_score + 1),
				org.newdawn.slick.Color.white
		);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		StructureFireGame fg = (StructureFireGame)game;
		
		timer -= delta;
		if (timer <= 0) {
			fg.percentage_score = 0;
			fg.coins_score = 0;
			fg.civilians_score = 0;
			game.enterState(StructureFireGame.STARTUPSTATE, new EmptyTransition(), new HorizontalSplitTransition());
		}
	}

	@Override
	public int getID() {
		return StructureFireGame.GAMEOVERSTATE;
	}
	
}