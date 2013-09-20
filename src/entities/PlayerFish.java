package entities;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import util.Assets;
import base.FishGame;
import base.World;

public class PlayerFish extends Fish {

	private static final float maxSpeed = 14f;
	private static final float acceleration = 0.05f;

	public PlayerFish(Vector3f velocity, World world) {
		super(Assets.PLAYER_FISH, 1.2f, world.getRandomClearPosition(),
				velocity, world);

		position.z = -200;
	}

	private final Vector3f result = new Vector3f();

	@Override
	protected Vector3f getFishAcceleration(GameContainer gc) {

		result.scale(0);

		if (getVelocity().length() > maxSpeed) {
			return result;
		}

		Input input = gc.getInput();

		if (FishGame.USING_CONTROLLER) {
			result.x = input.getAxisValue(0, 1);
			result.y = -input.getAxisValue(0, 0);

			if (result.length() < 0.6) {
				result.scale(0);
			}

		} else {

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
		}

		if (result.lengthSquared() != 0) {
			result.normalise();
		}

		result.scale(acceleration);

		return result;
	}

	@Override
	protected void doSomething() {

	}
}
