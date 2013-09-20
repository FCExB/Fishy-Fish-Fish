package gameObjects;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import base.Camera;

public class ThreeDShape implements InWorldSpace {
	protected List<Vector3f> points;
	protected Color color;

	private float minZ;

	public ThreeDShape(List<Vector3f> points, Color color) {
		this.points = points;
		this.color = color;

		minZ = 0;

		for (Vector3f vec : points) {
			minZ = Math.min(minZ, vec.z);
		}
	}

	@Override
	public void render(Camera camera, Graphics g) {

		Polygon shape = new Polygon();

		for (Vector3f vec : points) {

			Vector2f cameraSpace = camera.worldToCameraSpace(vec);

			shape.addPoint(cameraSpace.getX(), cameraSpace.getY());
		}

		g.setColor(color);
		g.fill(shape);
	}

	@Override
	public float getZ() {
		return minZ;
	}

	public List<Vector3f> getPoints() {
		return points;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public int compareTo(InWorldSpace that) {

		if (this.equals(that) || this.getZ() == that.getZ()) {
			return 0;
		}

		if (this.getZ() < that.getZ()) {
			return -1;
		}

		return 1;
	}
}
