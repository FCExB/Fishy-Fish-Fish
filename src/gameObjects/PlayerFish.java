package gameObjects;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import base.World;

public class PlayerFish extends Fish {

	public PlayerFish(Vector3f position, Vector3f velocity, World world) {
		super(position, velocity, world);
	}

	@Override
	protected Vector3f getAccelerationDirection(GameContainer gc) {
		Vector3f result = new Vector3f();

		Input input = gc.getInput();

		if (input.isKeyDown(Input.KEY_W)) {
			Vector3f.add(result, new Vector3f(0, 1, 0), result);
		}

		if (input.isKeyDown(Input.KEY_S)) {
			Vector3f.add(result, new Vector3f(0, -1, 0), result);
		}

		if (input.isKeyDown(Input.KEY_A)) {
			Vector3f.add(result, new Vector3f(-1, 0, 0), result);
		}

		if (input.isKeyDown(Input.KEY_D)) {
			Vector3f.add(result, new Vector3f(1, 0, 0), result);
		}

		if (input.isKeyDown(Input.KEY_E)) {
			Vector3f.add(result, new Vector3f(0, 0, -0.5f), result);
		}

		if (input.isKeyDown(Input.KEY_Q)) {
			Vector3f.add(result, new Vector3f(0, 0, 0.5f), result);
		}

		return result;
	}
}
