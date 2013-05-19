package game.entities.addons;

import game.Camera;
import game.entities.Entity;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import util.Assets;

public class HealthBar {
	private final int maxHealth;

	private int currentHealth;

	public HealthBar(int health) {
		maxHealth = currentHealth = health;
	}

	public boolean dead() {
		return currentHealth < 0;
	}

	public void reduce(int strength) {
		currentHealth -= strength;
	}

	public void render(Entity e, Color filter, Camera camera) {

		float zScaler = camera.zScaler();

		Vector3f position = e.getPosition();

		int x = Math.round(position.getX() - camera.getX()) + 500
				- e.getWidth() / 2;

		int y = Math.round((position.getZ() - camera.getY()) * zScaler + 300
				- (e.getDepth() * zScaler) / 2);

		float relativeHealth = (float) currentHealth / (float) maxHealth;

		Assets.HEALTH_BAR_BASE.draw(x, y, filter);
		Assets.HEALTH_BAR.draw(x, y, Assets.HEALTH_BAR.getWidth()
				* relativeHealth, Assets.HEALTH_BAR.getHeight(), filter);
	}
}
