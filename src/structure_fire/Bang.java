package structure_fire;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.Animation;

/**
 * A class representing a transient explosion. The game should monitor
 * explosions to determine when they are no longer active and remove/hide
 * them at that point.
 */
class Bang extends Entity {
	private Animation explosion;

	public Bang(final float x, final float y) {
		super(x, y);
		explosion = new Animation(ResourceManager.getSpriteSheet(
				StructureFireGame.BURN_FIRE_IMG_RSC, 64, 64), 0, 0, 22, 0, true, 50,
				true);
		addAnimation(explosion);
		explosion.setLooping(false);
	}

	public boolean isActive() {
		return !explosion.isStopped();
	}
}