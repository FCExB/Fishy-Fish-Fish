package gameObjects;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;

import base.World;

public class AIFish extends Fish {

	private static final float lowestHeight = -500;

	private static final Random rand = new Random();

	private Vector3f nextPoint;

	public AIFish(World world) {
		super(new Vector3f(rand.nextFloat() * world.getWidth(),
				rand.nextFloat() * lowestHeight, -rand.nextFloat()
						* world.getDepth()), new Vector3f(), world);

		selectNextPoint();
	}

	private void selectNextPoint() {
		nextPoint = new Vector3f(rand.nextFloat() * world.getWidth(),
				rand.nextFloat() * lowestHeight, -rand.nextFloat()
						* world.getDepth());

		if (!world.positionClear(nextPoint)) {
			selectNextPoint();
		}
	}

	@Override
	protected Vector3f getAccelerationDirection(GameContainer gc) {

		Vector3f aim = Vector3f.sub(nextPoint, position, null);

		if (aim.length() < 200) {
			selectNextPoint();
		}

		return aim;

	}

}
