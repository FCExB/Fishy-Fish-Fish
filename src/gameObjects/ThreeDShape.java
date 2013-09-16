package gameObjects;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import base.Camera;

public class ThreeDShape implements Comparable<ThreeDShape> {
	protected List<Vector3f> points;
	protected Color color;

	public ThreeDShape(List<Vector3f> points, Color color) {
		this.points = points;
		this.color = color;

		return;
	}

	public void render(Camera camera, Graphics g) {

		Polygon shape = new Polygon();

		for (Vector3f vec : points) {

			Vector2f cameraSpace = camera.worldToCameraSpace(vec);

			shape.addPoint(cameraSpace.getX(), cameraSpace.getY());
		}

		g.setColor(color);
		g.fill(shape);
	}

	public float getMaxZ() {

		float maxZ = Float.MIN_VALUE;

		for (Vector3f vec : points) {
			maxZ = Math.max(maxZ, vec.z);
		}

		return maxZ;
	}

	public List<Vector3f> getPoints() {
		return points;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ThreeDShape)) {
			return false;
		}

		ThreeDShape that = (ThreeDShape) o;

		return this.points.equals(that.points);
	}

	@Override
	public int compareTo(ThreeDShape that) {

		if (this.equals(that)) {
			return 0;
		}

		if (this.getMaxZ() < that.getMaxZ()) {
			return -1;
		}

		return 1;
	}
}
