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
import util.Attribute;
import util.Attributes;

public class Creature extends MovingEntity {

	private final float acceleration;

	private final HealthBar health;

	private int timeSinceBaby, timeSinceFood;

	private final Attributes attributes;

	public Creature(Attributes attributes, Vector3f position, World world) {
		super(Assets.CREATURE, position, new Vector3f(0, 0, 0), world);
		this.attributes = attributes;
		health = new HealthBar((int) attributes.getAttribute(Attribute.HEALTH));
		acceleration = attributes.getAttribute(Attribute.SPEED);
	}

	@Override
	protected Vector3f acceleration(int deltaT, GameContainer gc) {

		if (Math.random() < attributes.getAttribute(Attribute.ANGER)) {

			Vector3f result = new Vector3f(world.getPlayerLocation().x
					- position.x, 0, world.getPlayerLocation().z - position.z);

			result.normalise().scale(acceleration * (deltaT / 1000f));

			return result;
		}

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
		timeSinceBaby += deltaT;
		timeSinceFood += deltaT;
	}

	@Override
	public void renderExtras(Camera camera, Graphics g, Color filter) {
		health.render(this, filter, camera);
	}

	public int getStrength() {
		return (int) attributes.getAttribute(Attribute.STRENGTH);
	}

	@Override
	public void hitBy(Entity entity) {
		super.hitBy(entity);

		if (entity instanceof Creature) {
			Creature that = (Creature) entity;

			if (that.timeSinceFood >= attributes.getAttribute(Attribute.FOOD)) {
				health.reduce(1);
				if (health.dead()) {
					world.removeEntity(this);
					that.timeSinceFood = 0;
				}

			} else if (timeSinceBaby >= attributes.getAttribute(Attribute.SEX)) {

				Vector3f babyPosition = Vector3f.sub(that.position,
						this.position, null);
				babyPosition.scale(2.5f);
				Vector3f.add(this.position, babyPosition, babyPosition);

				Attributes newAs = Attributes.breed(this.attributes,
						that.attributes);

				System.out.println(newAs);

				if (world.addEntity(new Creature(newAs, babyPosition, world))) {
					this.timeSinceBaby = 0;
					that.timeSinceBaby = 0;
				}
			}
		} else if (entity instanceof Bullet) {

			health.reduce(1);

			if (health.dead()) {
				world.removeEntity(this);
				world.removeEntity(entity);
			}
		}
	}
}
