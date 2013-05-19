package game.entities;

import game.entities.moving.Bullet;
import game.world.World;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

import util.SpriteSheet;

public abstract class MovingEntity extends Entity {

	private Vector3f velocity = new Vector3f(0, 0, 0);

	public MovingEntity(SpriteSheet ss, Vector3f position,
			Vector3f initalVelocity, World world) {
		super(ss, true, true, 16, position, world);

		velocity = initalVelocity;
	}

	public MovingEntity(Image image, Vector3f position,
			Vector3f initalVelocity, World world) {
		super(image, true, true, 16, position, world);

		velocity = initalVelocity;
	}

	public Vector3f getVelocity() {
		return new Vector3f(velocity);
	}

	protected float getVelocitySize() {
		return velocity.length();
	}

	abstract protected Vector3f acceleration(int deltaT, GameContainer gc);

	@Override
	public void update(int deltaT, GameContainer gc) {
		super.update(deltaT, gc);

		act(deltaT, gc);

		Vector3f.add(velocity, acceleration(deltaT, gc), velocity);

		applyWorldForces(deltaT);

		Vector3f oldPosition = position;

		position = Vector3f.add(position, velocity, null);

		if (!world.positionClear(this)) {
			position = oldPosition;
			velocity = new Vector3f();
		}
	}

	public void accelerate(Vector3f acceleration) {
		Vector3f.add(velocity, acceleration, velocity);
	}

	@Override
	public void hitBy(Entity entity) {
		super.hitBy(entity);

		if (entity instanceof Bullet) {
			Bullet bullet = (Bullet) entity;

			accelerate(bullet.getVelocity());
		}
	}

	private void applyWorldForces(int deltaT) {
		if (position.y <= 0) {

			if (velocity.y < 0) {
				velocity.y = 0f;
			}

			position.y = 0f;
			float friction = 0.9f;
			velocity.scale(friction);
		} else {
			float gravity = -0.01f;
			Vector3f.add(velocity, new Vector3f(0, gravity * deltaT, 0),
					velocity);
		}
	}
}
