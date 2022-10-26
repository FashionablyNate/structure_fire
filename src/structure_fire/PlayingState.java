package structure_fire;

import jig.Vector;
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
	Background sky;
	private String level;

	private boolean show_paths = false;

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		StructureFireGame fg = (StructureFireGame)game;

		this.level = "level_one";
		fg.hud = new HUD( fg );
		sky = new Background(fg.ScreenWidth >> 1, fg.ScreenHeight >> 1);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		bounces = 0;
		container.setSoundOn(true);

		StructureFireGame fg = (StructureFireGame)game;

		fg.tile_map = new SFTileMap( this.level, fg );
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

		this.sky.render( g );

		fg.map.forEach( (k, v) -> {
			if (v.visible)
				v.render(g);
		});
		fg.player.render(g);
		for (FlameEnemy f : fg.fl_enemy) {
			if (!f.give_up)
				f.render(g);
		}

		fg.water_stream.removeIf(waterParticle -> !waterParticle.visible);
		for ( WaterParticle p : fg.water_stream )
			p.render(g);
		for (Burn b : fg.flames)
			b.render(g);
		for (Sprinkler s : fg.sprinklers)
			s.render(g);
		fg.hud.render( g, fg );

		if (fg.path != null && this.show_paths) {
			for (int i = 1; i < fg.path.getLength(); i++) {
				new PathImage((fg.path.getX(i) * 50) + 25, (fg.path.getY(i) * 50) + 25).render(g);
			}
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		StructureFireGame fg = (StructureFireGame)game;

		if ( input.isKeyDown(Input.KEY_F1) ) {
			this.show_paths = true;
		}
		if ( input.isKeyDown(Input.KEY_1) ) {
			this.level = "level_one";
			fg.map.clear();
			fg.flames.clear();
			fg.fl_enemy.clear();
			fg.enterState(StructureFireGame.GAMEOVERSTATE);
			this.enter(container, game);
		}
		if ( input.isKeyDown(Input.KEY_2) ) {
			this.level = "level_two";
			fg.map.clear();
			fg.flames.clear();
			fg.fl_enemy.clear();
			fg.enterState(StructureFireGame.GAMEOVERSTATE);
			this.enter(container, game);
		}
		if ( input.isKeyDown(Input.KEY_3) ) {
			this.level = "level_three";
			fg.map.clear();
			fg.flames.clear();
			fg.fl_enemy.clear();
			fg.enterState(StructureFireGame.GAMEOVERSTATE);
			this.enter(container, game);
		}

		fg.player.movement( input, fg );
		fg.player.spray( input, fg );
		fg.player.powerup( input, fg );

		if ( fg.player.flashing > 0 )
			fg.player.flash( delta );

		fg.player.update( delta );

		for (Sprinkler s : fg.sprinklers)
			s.update( fg, delta );

		for (FlameEnemy f : fg.fl_enemy) {
			f.move(delta, fg);
			f.update(delta);
		}
		if (fg.fl_enemy.isEmpty() && fg.tile_map.time_since_flame_killed > 1000) {
			outer:
			for (int x = 11; x >= 0; x--) {
				for (int y = 11; y >= 0; y--) {
					if (
							fg.map.containsKey((x * 1000) + y) &&
									fg.map.get((x * 1000) + y).isOnFire
					) {
						fg.fl_enemy.add(new FlameEnemy(
								fg.map.get((x * 1000) + y).getX(),
								fg.map.get((x * 1000) + y).getY()
						));
						break outer;
					}
				}
			}
			fg.tile_map.time_since_flame_killed = 0;
		} else if (fg.fl_enemy.isEmpty()) {
			fg.tile_map.time_since_flame_killed += delta;
		}

		fg.civilians.removeIf( x -> fg.map.get( (x[0] * 1000) + x[1]).isOnFire);
		fg.civilians.removeIf( x -> fg.map.get( (x[0] * 1000) + x[1]).saved);
		fg.sprinklers.removeIf( x -> x.timeToSpray <= 0 );

		fg.tile_map.update_tiles( delta, fg, input );

		fg.hud.update( fg.tile_map );

		if ( fg.player.health <= 0 )
			fg.enterState(StructureFireGame.GAMEOVERSTATE);
		if ( fg.flames.size() == 0 ) {
			for ( FlameEnemy f : fg.fl_enemy ) {
				if ( !f.give_up )
					return;
			}
			fg.civilians_score += fg.player.civilians_saved;
			fg.coins_score += fg.player.coins;
			fg.percentage_score += ((int) Math.ceil(
				100 * fg.tile_map.flammable_tiles_left /
				fg.tile_map.initial_flammable_tiles
			));
			if (this.level.matches("level_one"))
				this.level = "level_two";
			else if (this.level.matches("level_two"))
				this.level = "level_three";
			else if (this.level.matches("level_three")) {
				this.level = "level_one";
				fg.map.clear();
				fg.flames.clear();
				fg.fl_enemy.clear();
				fg.enterState(StructureFireGame.GAMEOVERSTATE);
			}
			this.enter(container, game);
		}
	}

	@Override
	public int getID() {
		return StructureFireGame.PLAYINGSTATE;
	}
	
}