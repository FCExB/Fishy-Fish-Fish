package entities;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

import base.World;

public abstract class Fish extends MovingEntity {

	private final float speed = 0.04f;
	private final float waterResistance = 0.02f;
	private final float maxSpeed;

	public Fish(Image image, float defaultScale, Vector3f position,
			Vector3f velocity, float maxSpeed, World world) {
		// super(new SpriteSheet(Assets.FISH_ANIMATED, 170, 227), 0.5f,
		// position, velocity, world);
		super(image, defaultScale, 16, position, velocity, world);
		this.maxSpeed = maxSpeed;
		lastPos = position;
	}

	@Override
	public void hitBy(Entity entity) {

	}

	private Vector3f lastPos;

	protected abstract void doSomething();

	@Override
	protected void act(int deltaT, GameContainer gc) {
		doSomething();

		boolean inWaterLast = world.inWater(lastPos);
		boolean inWaterNow = world.inWater(position);

		Vector3f velocity = getVelocity();

		if ((!inWaterLast && inWaterNow) || (!inWaterNow && inWaterLast)) {
			world.crossWaterLevel(position, changingScale, velocity.y
					/ maxSpeed);
		}

		lastPos = position;

		changingScale = (800 + position.z) / 800;

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

		if (getVelocity().length() < maxSpeed * changingScale) {
			result = getAccelerationDirection(gc);
		}

		if (result.lengthSquared() != 0) {

			result.normalise().scale(speed * changingScale * deltaT);

			return result;
		} else {
			Vector3f velocity = getVelocity();

			if (velocity.lengthSquared() != 0) {
				return (Vector3f) velocity.normalise().negate()
						.scale(waterResistance * changingScale * deltaT);
			} else
				return result;
		}
	}

	public boolean shouldReset() {
		if (position.x > 500 || position.x < -500 || position.y < -400) {
			return true;
		}

		if (position.x > -95 && position.x < 95 && position.y > 30
				&& position.y < 80) {
			return true;
		}

		return false;
	}

}