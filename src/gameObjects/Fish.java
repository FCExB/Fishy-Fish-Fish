package gameObjects;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;

import util.Assets;
import base.World;
import entities.Entity;
import entities.MovingEntity;

public abstract class Fish extends MovingEntity {

	private final float speed = 0.04f;
	private final float waterResistance = 0.02f;
	private final float maxSpeed = 15f;

	public Fish(Vector3f position, Vector3f velocity, World world) {
		// super(new SpriteSheet(Assets.FISH_ANIMATED, 170, 227), 0.5f,
		// position, velocity, world);
		super(Assets.FISH_STILL, 0.9f, position, velocity, world);
	}

	@Override
	public void hitBy(Entity entity) {

	}

	private Vector3f lastPos = new Vector3f();

	@Override
	protected void act(int deltaT, GameContainer gc) {
		boolean inWaterLast = world.inWater(lastPos);
		boolean inWaterNow = world.inWater(position);

		Vector3f velocity = getVelocity();

		if ((!inWaterLast && inWaterNow) || (!inWaterNow && inWaterLast)) {
			world.crossWaterLevel(position, scale, velocity.y / maxSpeed);
		}

		lastPos = position;

		scale = (800 + position.z) / 800;

		if (velocity.lengthSquared() != 0) {
			if (velocity.x < 0) {
				horizontalFlip = true;
			} else {
				horizontalFlip = false;
			}
		}

		float angle = (float) Math.toDegrees(Math
				.atan(-velocity.y / velocity.x));
		if (!Float.isNaN(angle)) {
			rotationAngle = angle;
		}
	}

	protected abstract Vector3f getAccelerationDirection(GameContainer gc);

	@Override
	protected Vector3f acceleration(int deltaT, GameContainer gc) {

		if (!world.inWater(position)) {
			return new Vector3f(0, -World.GRAVITY * deltaT, 0);
		}

		Vector3f result = new Vector3f();

		if (getVelocity().length() < maxSpeed * scale) {
			result = getAccelerationDirection(gc);
		}

		if (result.lengthSquared() != 0) {

			result.normalise().scale(speed * scale * deltaT);

			return result;
		} else {
			Vector3f velocity = getVelocity();

			if (velocity.lengthSquared() != 0) {
				return (Vector3f) velocity.normalise().negate()
						.scale(waterResistance * scale * deltaT);
			} else
				return result;
		}
	}

}
