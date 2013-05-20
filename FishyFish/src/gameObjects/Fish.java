package gameObjects;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import util.Assets;
import base.World;
import entities.Entity;
import entities.MovingEntity;

public class Fish extends MovingEntity {

	private float speed = 0.03f;

	public Fish(Vector3f position, Vector3f velocity, World world) {
		// super(new SpriteSheet(Assets.FISH_ANIMATED, 170, 227), 0.5f,
		// position, velocity, world);
		super(Assets.FISH_STILL, 0.1f, position, velocity, world);
	}

	@Override
	public void hitBy(Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void act(int deltaT, GameContainer gc) {
		
	}

	@Override
	protected Vector3f acceleration(int deltaT, GameContainer gc) {

		float waterResistance = 0.01f;
		float maxSpeed = 5f;

		Vector3f result = new Vector3f();

		if (getVelocity().length() < maxSpeed) {
			if (world.inWater(position)) {
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
			} else {
				return new Vector3f(0, -World.GRAVITY * deltaT, 0);
			}
		} else {
			return (Vector3f) getVelocity().normalise().negate()
					.scale(waterResistance * deltaT);
		}

		if (result.lengthSquared() != 0) {
			return (Vector3f) result.normalise().scale(speed * deltaT);
		}

		return result;
	}

}
