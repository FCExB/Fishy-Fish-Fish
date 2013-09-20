package base;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;

import util.AudioManager;

public class Camera {

	private float x;
	private float y;

	private float angle = 8f;

	public Camera(int x, int y) {
		this.x = x;
		this.y = y;

		defaultX = x;
		defaultY = y;

		nextX = x;
		nextY = y;
		nextAngle = angle;
	}

	public float zScaler() {
		return (float) Math.sin(angle * (Math.PI / 180));
	}

	public float otherScaler() {
		return (float) Math.cos(angle * (Math.PI) / 180);
	}

	public int getX() {
		return (int) x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int) y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Vector2f worldToCameraSpace(Vector3f world) {

		float x = world.getX() - this.x + 500;
		float y = (world.getZ() - this.y) * zScaler() + 300 - world.getY()
				* otherScaler();

		return new Vector2f(x, y);
	}

	private static final float MAX_ANGLE = 12f;
	private static final float MIN_ANGLE = 0.3f;

	private final float defaultX;
	private final float defaultY;

	private static final float xRange = 45;

	private static final float yRange = 45;

	private float nextX;
	private float nextY;

	private float nextAngle;

	private final Random rand = new Random();

	public void update(GameContainer gc, int deltaT, int score) {

		if (nextAngle + 0.5 > angle && nextAngle - 0.5 < angle) {
			nextAngle = (MAX_ANGLE - MIN_ANGLE) * rand.nextFloat() + MIN_ANGLE;
		}

		if (nextX + 3 > x && nextX - 3 < x) {
			nextX = (rand.nextFloat() - 0.5f) * 2 * xRange
					* ((float) score / 4) + defaultX;
		}

		if (nextY + 3 > y && nextY - 3 < y) {
			nextY = (rand.nextFloat() - 0.5f) * 2 * yRange
					* ((float) score / 4) + defaultY;
		}

		float rotateSpeed = 0.001f * ((float) score / 2);

		angle += (nextAngle - angle) * rotateSpeed * deltaT;

		float movementSpeed = 0.001f * ((float) score / 3);

		x += (nextX - x) * movementSpeed * deltaT;
		y += (nextY - y) * movementSpeed * deltaT;

		AudioManager.updateListenerPosition(x, y);

		// Input input = gc.getInput();
		//
		// if (input.isKeyDown(Input.KEY_Q) && angle < 90) {
		// angle += rotateSpeed * deltaT;
		// } else if (input.isKeyDown(Input.KEY_E) && angle > 2) {
		// angle -= rotateSpeed * deltaT;
		// }

	}

	// public int getViewWidth() {
	// return viewWidth;
	// }
	//
	// public void setViewWidth(int viewWidth) {
	// this.viewWidth = viewWidth;
	// }
	//
	// public int getViewHeight() {
	// return viewHeight;
	// }
	//
	// public void setViewHeight(int viewHeight) {
	// this.viewHeight = viewHeight;
	// }
	//

	public boolean inRenderView(Vector3f pos) {

		int halfWidth = 1000 / 2;
		float halfHeight = (600 / zScaler()) / 2;

		return pos.x >= x - halfWidth && pos.x < x + halfWidth
				&& pos.z >= y - halfHeight && pos.z < y + halfHeight;
	}
}
