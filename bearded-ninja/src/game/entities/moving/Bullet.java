package game.entities.moving;

import game.entities.MovingEntity;
import game.entities.stationary.Explosion;
import game.world.World;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;

import util.Assets;

public class Bullet extends MovingEntity {

	public Bullet(Vector3f position, Vector3f initalVelocity, World world) {
		super(Assets.BULLET, position, initalVelocity, world);
	}

	@Override
	protected Vector3f acceleration(int deltaT, GameContainer gc) {
		return new Vector3f(0, 0, 0);
	}

	@Override
	protected void act(int deltaT, GameContainer gc) {
		if (position.y <= 0) {
			Vector3f explosionLocation = new Vector3f(position.x, 0, position.z);

			world.removeEntity(this);
			world.addEntity(new Explosion(explosionLocation, world));
		}
	}

}
