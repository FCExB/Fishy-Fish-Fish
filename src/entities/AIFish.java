package entities;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;

import util.Assets;
import base.World;

public class AIFish extends Fish {

	private static final float maxSpeed = 9f;
	private static final float acceleration = 0.02f;

	private Vector3f nextPoint;

	public AIFish(World world) {
		super(Assets.FISH_STILL2, 2.1f, world.getRandomClearPosition(),
				new Vector3f(), world);

		selectNextPoint();
	}

	private void selectNextPoint() {
		nextPoint = world.getRandomClearPosition();
	}

	@Override
	protected Vector3f getFishAcceleration(GameContainer gc) {

		if (getVelocity().length() > maxSpeed) {
			return new Vector3f();
		}

		Vector3f aim = Vector3f.sub(nextPoint, position, null);

		if (aim.length() < 100) {
			selectNextPoint();
		}

		aim.normalise().scale(acceleration);

		return aim;

	}

	@Override
	protected void doSomething() {
		// TODO Auto-generated method stub

	}

}
