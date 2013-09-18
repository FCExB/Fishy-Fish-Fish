package entities;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import util.Assets;
import base.World;

public class PlayerFish extends Fish {

	public PlayerFish(Vector3f velocity, World world) {
		super(Assets.FISH_STILL, 1.2f, world.getRandomClearPosition(),
				velocity, 15.5f, 0.04f, world);

		position.z = -200;
	}

	Vector3f result = new Vector3f();

	@Override
	protected Vector3f getAccelerationDirection(GameContainer gc) {

		result.scale(0);

		Input input = gc.getInput();

		if (input.getControllerCount() > 0) {
			result.x = input.getAxisValue(0, 1);
			result.y = -input.getAxisValue(0, 0);

			if (result.length() < 0.5) {
				result.scale(0);
			}
			return result;
		}

		if (input.isKeyDown(Input.KEY_W)) {
			result.y += 1;
		}

		if (input.isKeyDown(Input.KEY_S)) {
			result.y -= 1;
		}

		if (input.isKeyDown(Input.KEY_A)) {
			result.x -= 1;
		}

		if (input.isKeyDown(Input.KEY_D)) {
			result.x += 1;
		}

		return result;
	}

	@Override
	protected void doSomething() {

	}
}
