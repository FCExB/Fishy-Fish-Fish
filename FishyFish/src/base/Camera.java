package base;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class Camera {

	private int x;
	private int y;

	private float angle = 80;

	public Camera(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public float zScaler() {
		return (float) Math.sin(angle * (Math.PI / 180));
	}

	public float otherScaler() {
		return (float) Math.cos(angle * (Math.PI) / 180);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public Vector2f worldToCameraSpace(Vector3f world){

		float x = world.getX() - this.x + 500;
		float y = (world.getZ() - this.y) * zScaler()
				+ 300 - world.getY() * otherScaler();

		return new Vector2f(x, y);
	}
	
	public void update(GameContainer gc, int deltaT) {

		float rotateSpeed = 0.08f;

		Input input = gc.getInput();

		if (input.isKeyDown(Input.KEY_Q) && angle < 90) {
			angle += rotateSpeed * deltaT;
		} else if (input.isKeyDown(Input.KEY_E) && angle > 2) {
			angle -= rotateSpeed * deltaT;
		}

	}

//	public int getViewWidth() {
//		return viewWidth;
//	}
//
//	public void setViewWidth(int viewWidth) {
//		this.viewWidth = viewWidth;
//	}
//
//	public int getViewHeight() {
//		return viewHeight;
//	}
//
//	public void setViewHeight(int viewHeight) {
//		this.viewHeight = viewHeight;
//	}
//
//	public boolean inRenderView(Vector3f pos) {
//
//		int halfWidth = viewWidth / 2;
//		float halfHeight = (viewHeight / zScaler()) / 2;
//
//		return pos.x >= x - halfWidth && pos.x < x + halfWidth
//				&& pos.z >= y - halfHeight && pos.z < y + halfHeight;
//	}
}
