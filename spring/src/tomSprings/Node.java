package tomSprings;
import java.awt.Color;
import java.awt.Graphics;

public class Node {

	Vector2D velocity;
	Vector2D position;
	Vector2D force;
	Node catched;
	float mass;
	boolean anchor;
	boolean renderIt;
	boolean focused;
	boolean recentlyAnchored;

	boolean checkFocused() {
		return focused;
	}

	void resetFocused() {
		focused = false;
	}

	boolean checkRecentlyAnchored() {
		return recentlyAnchored;
	}

	void toggleAnchor() {
		if (anchor != true) {
			anchor = true;
		} else {
			anchor = false;
			recentlyAnchored = true;
		}
	}

	void draw(Graphics g) {
		if (!renderIt) {
			return;
		}

		else {
			if (focused == true) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.white);
			}

			if (catched != null) {
				g.fillOval((int) onFlyX - 2, (int) onFlyY - 1, 4, 4);
			} else {

				g.fillOval((int) position.x - 2, (int) position.y - 1, 4, 4);
			}

			return;
		}

	}

	void addForceFrom(float force, Vector2D from) {

		float distance = position.distance(from);

		float dx = from.x - position.x;
		float dy = from.y - position.y;

		this.force.x += (-dx / distance) * force;
		this.force.y += (-dy / distance) * force;

	}

	float distanceNodes(Node a, Node b) // Distance measuring
	{
		return (float) Math.sqrt((a.position.x - b.position.x)
				* (a.position.x - b.position.x) + (a.position.y - b.position.y)
				* (a.position.y - b.position.y));
	}

	void checkMouseFocus(Node a) {

		if ((a.position.x <= onFlyX + 4) && (a.position.x >= onFlyX - 4)) {
			if ((a.position.y <= onFlyY + 4) && (a.position.y >= onFlyY - 4)) {
				a.focused = true;
			}
		} else {
			a.focused = false;
		}
	}

	void move() {

		if (anchor || this == catched) {
			return;
		}

		checkRecentlyAnchored();

		if (recentlyAnchored == true) {
			velocity.x *= 0.25F;
			velocity.y *= 0.25F;

			position.x += velocity.x;
			position.y += velocity.y;

		} else {
			velocity.x *= 0.95F;
			velocity.y *= 0.95F;

			position.x += velocity.x;
			position.y += velocity.y;
		}

		if (position.x < 4.0F) {
			position.x = 4.0F;
			velocity.x *= -0.6F;
		}

		if (position.y < 2.0F) {
			position.y = 2.0F;
			velocity.y *= -0.6F;
		}

		if (position.x > SpringSimulation.w - 2.0F) {
			position.x = SpringSimulation.w - 2.0F;
			velocity.x *= -0.6F;
		}

		if (position.y > SpringSimulation.h - 10.0F) {
			position.y = SpringSimulation.h - 10.0F;
			velocity.y *= -0.9F;
		}

		recentlyAnchored = false;
	}

	Node(int x, int y, float mass) {
		velocity = new Vector2D();
		position = new Vector2D();
		force = new Vector2D();

		position.x = x;
		position.y = y;
		anchor = false;
		this.mass = mass;
		renderIt = true;
		recentlyAnchored = false;
		focused = false;
	}

	Node(float x, float y, float mass) {
		velocity = new Vector2D();
		position = new Vector2D();
		force = new Vector2D();

		position.x = x;
		position.y = y;
		anchor = false;
		this.mass = mass;
		renderIt = true;
		recentlyAnchored = false;
		focused = false;
	}

}
