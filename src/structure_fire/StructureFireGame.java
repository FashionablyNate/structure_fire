package structure_fire;

import java.util.ArrayList;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A Simple Game of Bounce.
 * 
 * The game has three states: StartUp, Playing, and GameOver, the game
 * progresses through these states based on the user's input and the events that
 * occur. Each state is modestly different in terms of what is displayed and
 * what input is accepted.
 * 
 * In the playing state, our game displays a moving rectangular "ball" that
 * bounces off the sides of the game container. The ball can be controlled by
 * input from the user.
 * 
 * When the ball bounces, it appears broken for a short time afterwards and an
 * explosion animation is played at the impact site to add a bit of eye-candy
 * additionally, we play a short explosion sound effect when the game is
 * actively being played.
 * 
 * Our game also tracks the number of bounces and syncs the game update loop
 * with the monitor's refresh rate.
 * 
 * Graphics resources courtesy of qubodup:
 * http://opengameart.org/content/bomb-explosion-animation
 * 
 * Sound resources courtesy of DJ Chronos:
 * http://www.freesound.org/people/DJ%20Chronos/sounds/123236/
 * 
 * 
 * @author wallaces
 * 
 */
public class StructureFireGame extends StateBasedGame {
	
	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	
	public static final String PLAYER_CHARACTER = "structure_fire/resource/fireman_mockup.png";
	public static final String WOODEN_PLANKS = "structure_fire/resource/wooden_planks.png";
	public static final String WATER_PARTICLE = "structure_fire/resource/water_particle.png";
	public static final String GAMEOVER_BANNER_RSC = "structure_fire/resource/GameOver.png";
	public static final String STARTUP_BANNER_RSC = "structure_fire/resource/PressSpace.png";
	public static final String BANG_EXPLOSIONIMG_RSC = "structure_fire/resource/explosion.png";
	public static final String BANG_EXPLOSIONSND_RSC = "structure_fire/resource/explosion.wav";

	public final int ScreenWidth;
	public final int ScreenHeight;

	Player player;
	ArrayList<ArrayList<Tile>> map;

	ArrayList<WaterParticle> water_stream;

	/**
	 * Create the StructureFireGame frame, saving the width and height for later use.
	 * 
	 * @param title
	 *            the window's title
	 * @param width
	 *            the window's width
	 * @param height
	 *            the window's height
	 */
	public StructureFireGame(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;

		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);

		map = new ArrayList<>(12);
		for ( int i = 0; i < 12; i++ )
			map.add(new ArrayList<Tile>(12));
		water_stream = new ArrayList<>(1000);
	}


	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new StartUpState());
		addState(new GameOverState());
		addState(new PlayingState());
		
		// the sound resource takes a particularly long time to load,
		// we preload it here to (1) reduce latency when we first play it
		// and (2) because loading it will load the audio libraries and
		// unless that is done now, we can't *disable* sound as we
		// attempt to do in the startUp() method.
		ResourceManager.loadSound(BANG_EXPLOSIONSND_RSC);	

		// preload all the resources to avoid warnings & minimize latency...
		ResourceManager.loadImage(PLAYER_CHARACTER);
		ResourceManager.loadImage(WOODEN_PLANKS);
		ResourceManager.loadImage(WATER_PARTICLE);
		ResourceManager.loadImage(GAMEOVER_BANNER_RSC);
		ResourceManager.loadImage(STARTUP_BANNER_RSC);
		ResourceManager.loadImage(BANG_EXPLOSIONIMG_RSC);
		
		player = new Player(container.getWidth() >> 1, container.getHeight() >> 1);

	}
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new StructureFireGame("Structure Fire", 600, 800));
			app.setDisplayMode(600, 800, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
