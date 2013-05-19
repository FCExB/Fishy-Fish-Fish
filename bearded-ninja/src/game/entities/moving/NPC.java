package game.entities.moving;

import game.Camera;
import game.entities.Entity;
import game.entities.MovingEntity;
import game.entities.addons.HealthBar;
import game.world.World;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import util.Assets;
import util.SpriteSheet;

public class NPC extends MovingEntity {

	private final HealthBar health = new HealthBar(100);
	private final float acceleration = 30f;

	public NPC(Vector3f position, World world) {
		super(new SpriteSheet(Assets.IRISH_DRESS, 64, 64, 9), position,
				new Vector3f(0, 0, 0), world);

		spriteSheetRow = 9;
	}

	@Override
	protected Vector3f acceleration(int deltaT, GameContainer gc) {

		Vector3f result = new Vector3f(Math.round(Math.random() * 2 - 1), 0,
				Math.round(Math.random() * 2 - 1));

		if (result.length() == 0) {
			return result;
		}

		result.normalise().scale(acceleration * (deltaT / 1000f));
		return result;
	}

	@Override
	protected void act(int deltaT, GameContainer gc) {

		if (getVelocitySize() > 0.2) {
			animating = true;
		} else {
			animating = false;
		}

		Vector3f velocity = getVelocity();

		if (velocity.x > 0 && velocity.z > 0) {
			if (velocity.x > velocity.z) {
				spriteSheetRow = 11;
			} else {
				spriteSheetRow = 10;
			}
		} else if (velocity.x > 0 && velocity.z <= 0) {
			if (velocity.x > -velocity.z) {
				spriteSheetRow = 11;
			} else {
				spriteSheetRow = 8;
			}
		} else if (velocity.x <= 0 && velocity.z <= 0) {
			if (-velocity.x > -velocity.z) {
				spriteSheetRow = 9;
			} else {
				spriteSheetRow = 8;
			}
		} else if (velocity.x <= 0 && velocity.z > 0) {
			if (-velocity.x > velocity.z) {
				spriteSheetRow = 9;
			} else {
				spriteSheetRow = 10;
			}
		}
	}

	@Override
	public void renderExtras(Camera camera, Graphics g, Color filter) {
		health.render(this, filter, camera);
	}

	@Override
	public void hitBy(Entity entity) {
		super.hitBy(entity);

		if (entity instanceof Creature) {
			Creature c = (Creature) entity;
			health.reduce(c.getStrength());
		}
	}

}
