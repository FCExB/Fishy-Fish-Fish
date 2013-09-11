package entities;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

import util.SpriteSheet;
import base.World;

public abstract class MovingEntity extends Entity {

	private Vector3f velocity = new Vector3f(0, 0, 0);

	public MovingEntity(SpriteSheet ss, float scale, int depth,
			Vector3f position, Vector3f initalVelocity, World world) {
		super(ss, true, depth, scale, position, world);

		velocity = initalVelocity;
	}

	public MovingEntity(Image image, float scale, int depth, Vector3f position,
			Vector3f initalVelocity, World world) {
		super(image, true, depth, scale, position, world);

		velocity = initalVelocity;
	}

	public Vector3f getVelocity() {
		return new Vector3f(velocity);
	}

	protected float getVelocitySize() {
		return velocity.length();
	}

	abstract protected Vector3f acceleration(int deltaT, GameContainer gc);

	private final float lowestNonZeroSpeed = 0.3f;

	@Override
	public void update(int deltaT, GameContainer gc) {
		super.update(deltaT, gc);

		act(deltaT, gc);

		Vector3f.add(velocity, acceleration(deltaT, gc), velocity);

		Vector3f oldPosition = position;

		position = Vector3f.add(position, velocity, null);

		if (velocity.length() < lowestNonZeroSpeed) {
			position = oldPosition;
			velocity = new Vector3f();
			return;
		}

		float bounceDamping = 0.4f;

		if (!world.positionClear(this)) {

			if (smallestY() < 50 && greatestY() > 50 && greatestX() > 800) {
				position = oldPosition;
				velocity = new Vector3f();
				return;
			}

			velocity.x = -velocity.x;
			velocity.z = -velocity.z;
			velocity.scale(bounceDamping);
			position = Vector3f.add(position, velocity, null);
		}
	}

	public void accelerate(Vector3f acceleration) {
		Vector3f.add(velocity, acceleration, velocity);
	}
}
