package game.entities.stationary;

import game.entities.Entity;
import game.world.World;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;

import util.Assets;
import util.SpriteSheet;

public class Explosion extends Entity {

	public Explosion(Vector3f position, World world) {
		super(new SpriteSheet(Assets.EXPLOSION, 16, 16), false, false, 0,
				position, world);
	}

	@Override
	protected void act(int deltaT, GameContainer gc) {
		animating = true;

		if (getAnimationFrame() == 12) {
			world.removeEntity(this);
		}
	}

}
