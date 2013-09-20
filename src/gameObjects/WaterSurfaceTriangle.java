package gameObjects;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

public class WaterSurfaceTriangle extends ThreeDShape {

	public WaterSurfaceTriangle(List<Vector3f> points) {
		super(points, new Color(0f, 0f, 0f, 0.7f));
	}

	public void update() {
		calculateColor();
	}

	private void calculateColor() {

		float height = Float.MIN_VALUE;
		for (Vector3f vec : points) {
			height = Math.max(vec.y, height);
		}

		height += 0.5;
		height /= 5;

		color.b = height;
	}
}
