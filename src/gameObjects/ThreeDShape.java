package gameObjects;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import base.Camera;
import entities.Entity;

public class ThreeDShape implements InWorldSpace {
	protected List<Vector3f> points;
	protected Color color;

	private float z = 0;

	public ThreeDShape(List<Vector3f> points, Color color) {
		this.points = points;
		this.color = color;

		for (Vector3f vec : points) {
			z += vec.z;
		}

		z /= points.size();
	}

	@Override
	public void render(Camera camera, Graphics g) {

		Polygon shape = new Polygon();

		boolean toDraw = false;

		for (Vector3f vec : points) {

			toDraw |= camera.inRenderView(vec);

			Vector2f cameraSpace = camera.worldToCameraSpace(vec);

			shape.addPoint(cameraSpace.getX(), cameraSpace.getY());
		}

		if (toDraw) {
			g.setColor(color);
			g.fill(shape);
		}
	}

	@Override
	public float getZ() {
		return z;
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
