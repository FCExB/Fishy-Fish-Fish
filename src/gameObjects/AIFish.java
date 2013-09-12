package gameObjects;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;

import util.Assets;
import base.World;

public class AIFish extends Fish {

	private static final int lowestHeight = 500;

	private static final Random rand = new Random();

	private Vector3f nextPoint;

	public AIFish(World world) {
		super(Assets.FISH_STILL2, 2.1f, new Vector3f(rand.nextInt(world
				.getWidth() - 100) + 50, -rand.nextInt(lowestHeight),
				-rand.nextInt(world.getDepth() - 100) - 50), new Vector3f(),
				13f, world);

		selectNextPoint();
	}

	private void selectNextPoint() {
		nextPoint = new Vector3f(rand.nextFloat() * world.getWidth(),
				rand.nextFloat() * -lowestHeight, -rand.nextFloat()
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
