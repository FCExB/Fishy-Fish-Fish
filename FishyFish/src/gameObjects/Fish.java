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

	private Vector3f lastPos = new Vector3f();

	@Override
	protected void act(int deltaT, GameContainer gc) {
		boolean inWaterLast = world.inWater(lastPos);
		boolean inWaterNow = world.inWater(position);

		if (!inWaterLast && inWaterNow) {
			world.enterWater(position, scale);
		}

		if (!inWaterNow && inWaterLast) {
			world.exitWater(position, scale);
		}

		lastPos = position;

		scale = (800 + position.z) / 800;
		
		Vector3f velocity = getVelocity();
		
		if(velocity.x < 0){
			horizontalFlip = true;
		} else horizontalFlip = false;
		
		float angle = (float) Math.toDegrees(Math.atan(-velocity.y/velocity.x));
		if(!Float.isNaN(angle)){
			rotationAngle = angle;
		} 
	}

	@Override
	protected Vector3f acceleration(int deltaT, GameContainer gc) {

		if (position.z < -400)
			position.z = -400;

		if (position.z > -7)
			position.z = -7;

		if (position.x < 1)
			position.x = 1;

		if (position.x > 770)
			position.x = 770;

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

				if (input.isKeyDown(Input.KEY_E)) {
					Vector3f.add(result, new Vector3f(0, 0, -1), result);
				}

				if (input.isKeyDown(Input.KEY_Q)) {
					Vector3f.add(result, new Vector3f(0, 0, 1), result);
				}

			} else {
				return new Vector3f(0, -World.GRAVITY * deltaT, 0);
			}
		}

		if (result.lengthSquared() != 0) {
			
			result.normalise().scale(speed * scale * deltaT);
			
			return result;
		} else {
			Vector3f velocity = getVelocity();

			if (velocity.lengthSquared() != 0) {
				
				return (Vector3f) velocity.normalise().negate()
						.scale(waterResistance * scale *deltaT);
			} else
				return result;
		}
	}

}
