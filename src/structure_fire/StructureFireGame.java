package structure_fire;

import java.util.*;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

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
	
	public static final String PLAYER_CHARACTER_RIGHT = "structure_fire/resource/fireman_right.png";
	public static final String PLAYER_CHARACTER_FLASH = "structure_fire/resource/fireman_flash.png";
	public static final String PLAYER_CHARACTER_LEFT = "structure_fire/resource/fireman_left.png";
	public static final String CIVILIAN = "structure_fire/resource/civilian.png";
	public static final String COIN = "structure_fire/resource/coin.png";
	public static final String SPRINKLER = "structure_fire/resource/sprinkler.png";
	public static final String SPRINKLER_HUD = "structure_fire/resource/sprinkler_hud.png";
	public static final String HYDRANT = "structure_fire/resource/hydrant.png";
	public static final String HOUSE = "structure_fire/resource/house.png";
	public static final String BG_WOODEN_PLANKS = "structure_fire/resource/bg_wooden_planks.png";
	public static final String WOODEN_PLANKS = "structure_fire/resource/wooden_planks.png";
	public static final String WOODEN_LADDER = "structure_fire/resource/wooden_ladder.png";
	public static final String STONE = "structure_fire/resource/stone.png";
	public static final String SKY = "structure_fire/resource/background.png";
	public static final String HEART = "structure_fire/resource/heart.png";
	public static final String WATER_PARTICLE = "structure_fire/resource/water_particle.png";
	public static final String WATER_GAUGE = "structure_fire/resource/water_gauge.png";
	public static final String LEXICON = "structure_fire/resource/lexicon.png";
	public static final String GAMEOVER_BANNER_RSC = "structure_fire/resource/GameOver.png";
	public static final String STARTUP_BANNER_RSC = "structure_fire/resource/PressSpace.png";
	public static final String BURN_FIRE_IMG_RSC = "structure_fire/resource/flames.png";
	public static final String BURN_FIRE_AI = "structure_fire/resource/flames_ai.png";
	public static final String PATH = "structure_fire/resource/path.png";
	public static final String BANG_EXPLOSIONSND_RSC = "structure_fire/resource/explosion.wav";

	public final int ScreenWidth;
	public final int ScreenHeight;

	Player player;
	Map<Integer, Tile> map;
	Random rand;
	ArrayList<WaterParticle> water_stream;
	ArrayList<Burn> flames;
	SFTileMap tile_map;
	AStarPathFinder pathFinder;
	Path path;
	ArrayList<FlameEnemy> fl_enemy;
	Stack<int[]> civilians;
	Stack<Sprinkler> sprinklers;
	Integer sprinkler_inventory;
	WaterGauge water_gauge;
	HUD hud;
	Integer coins_score, civilians_score, percentage_score;
	boolean lost = false;

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

		rand = new Random();
		map = new HashMap<>();
		water_stream = new ArrayList<>(1000);
		flames = new ArrayList<Burn>(10);
		civilians = new Stack<>();
		sprinklers = new Stack<>();
		sprinkler_inventory = 3;
		fl_enemy = new ArrayList<>();
		coins_score = 0;
		civilians_score = 0;
		percentage_score = 0;
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
//		ResourceManager.loadSound(BANG_EXPLOSIONSND_RSC);

		// preload all the resources to avoid warnings & minimize latency...
		ResourceManager.loadImage(PLAYER_CHARACTER_RIGHT);
		ResourceManager.loadImage(PLAYER_CHARACTER_FLASH);
		ResourceManager.loadImage(PLAYER_CHARACTER_LEFT);
		ResourceManager.loadImage(CIVILIAN);
		ResourceManager.loadImage(COIN);
		ResourceManager.loadImage(SPRINKLER);
		ResourceManager.loadImage(SPRINKLER_HUD);
		ResourceManager.loadImage(HYDRANT);
		ResourceManager.loadImage(HOUSE);
		ResourceManager.loadImage(BG_WOODEN_PLANKS);
		ResourceManager.loadImage(WOODEN_PLANKS);
		ResourceManager.loadImage(WOODEN_LADDER);
		ResourceManager.loadImage(STONE);
		ResourceManager.loadImage(SKY);
		ResourceManager.loadImage(HEART);
		ResourceManager.loadImage(WATER_PARTICLE);
		ResourceManager.loadImage(WATER_GAUGE);
		ResourceManager.loadImage(LEXICON);
//		ResourceManager.loadImage(GAMEOVER_BANNER_RSC);
//		ResourceManager.loadImage(STARTUP_BANNER_RSC);
		ResourceManager.loadImage(BURN_FIRE_IMG_RSC);
		ResourceManager.loadImage(BURN_FIRE_AI);
		ResourceManager.loadImage(PATH);

		Entity.antiAliasing = false;
	}
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new StructureFireGame("Structure Fire", 600, 800));
			app.setDisplayMode(600, 800, false);
			app.setVSync(true);
			app.setTargetFrameRate( 60 );
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
