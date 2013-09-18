package entities;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

import base.World;

public abstract class Fish extends MovingEntity {

	private final float waterResistance = 0.03f;

	public Fish(Image image, float defaultScale, Vector3f position,
			Vector3f velocity, World world) {
		super(image, defaultScale, 16, position, velocity, world);
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
			world.crossWaterLevel(position, changingScale, velocity.y);
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

	protected abstract Vector3f getFishAcceleration(GameContainer gc);

	@Override
	protected Vector3f acceleration(int deltaT, GameContainer gc) {

		if (!world.inWater(position)) {
			return new Vector3f(0, -World.GRAVITY * deltaT, 0);
		}

		Vector3f result = getFishAcceleration(gc);

		Vector3f waterResistanceAcceleration = getVelocity();

		if (result.lengthSquared() == 0
				&& waterResistanceAcceleration.lengthSquared() != 0) {
			waterResistanceAcceleration.normalise().negate()
					.scale(waterResistance);

			Vector3f.add(result, waterResistanceAcceleration, result);
		}

		result.scale(changingScale * deltaT);

		return result;
	}

	public boolean shouldReset() {
		if (position.x > 500 || position.x < -500 || position.y < -400) {
			return true;
		}

		if (position.x > -110 && position.x < 110 && position.y > 20
				&& position.y < 65) {
			return true;
		}

		return false;
	}

}
