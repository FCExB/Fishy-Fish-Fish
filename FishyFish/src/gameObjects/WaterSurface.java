package gameObjects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import base.Camera;

public class WaterSurface {
	private static final int UPDATE_FREQUENCY = 100;

	private Vector3f[][] points;
	private int numDeep;
	private int numWide;

	private int timeSinceUpdate = 0;
	
	private Random rand = new Random();

	private Set<ThreeDShape> polys = new HashSet<ThreeDShape>();

	public WaterSurface(Vector3f frontLeft, float width, float depth,
			int numWide, int numDeep) {
		float dx = width / (numWide - 1);
		float dz = depth / (numDeep - 1);

		points = new Vector3f[numDeep][numWide];

		for (int z = 0; z < numDeep; z++) {
			for (int x = 0; x < numWide; x++) {
				points[z][x] = new Vector3f(frontLeft.x + x * dx, frontLeft.y,
						frontLeft.z - z * dz);
			}
		}

		this.numDeep = numDeep;
		this.numWide = numWide;

		updateTriangles();
	}

	private void updateTriangles() {

		timeSinceUpdate = 0;

		for (int z = 0; z < numDeep; z++) {

			for (int x = z % 2; x < numWide; x += 2) {
				if (z - 1 >= 0) {
					if (x - 1 >= 0) {

						List<Vector3f> triangle = new ArrayList<Vector3f>(3);

						triangle.add(points[z][x]);
						triangle.add(points[z][x - 1]);
						triangle.add(points[z - 1][x]);

						polys.add(new ThreeDShape(triangle,
								calculateColor(triangle)));

					}

					if (x + 1 < numWide) {

						List<Vector3f> triangle = new ArrayList<Vector3f>(3);

						triangle.add(points[z][x]);
						triangle.add(points[z][x + 1]);
						triangle.add(points[z - 1][x]);

						polys.add(new ThreeDShape(triangle,
								calculateColor(triangle)));

					}
				}

				if (z + 1 < numDeep) {
					if (x - 1 >= 0) {
						List<Vector3f> triangle = new ArrayList<Vector3f>(3);

						triangle.add(points[z][x]);
						triangle.add(points[z][x - 1]);
						triangle.add(points[z + 1][x]);

						polys.add(new ThreeDShape(triangle,
								calculateColor(triangle)));

					}

					if (x + 1 < numWide) {

						List<Vector3f> triangle = new ArrayList<Vector3f>(3);

						triangle.add(points[z][x]);
						triangle.add(points[z][x + 1]);
						triangle.add(points[z + 1][x]);

						polys.add(new ThreeDShape(triangle,
								calculateColor(triangle)));

					}
				}
			}
		}

	}

	public void updatePoints(int delta) {
		timeSinceUpdate += delta;

		if (timeSinceUpdate >= UPDATE_FREQUENCY) {

			int z = rand.nextInt(numDeep);
			int zEnd = rand.nextInt(numDeep);
			
			int x = rand.nextInt(numWide);
			int xEnd = rand.nextInt(numWide);
			
			for (;z < zEnd; z++) {
				for (; x < xEnd; x++) {
					points[z][x].y += (Math.random() - 0.5) * 5;
				}
			}

			updateTriangles();
		}
	}

	private Color calculateColor(List<Vector3f> triangle) {

		float height = Float.MIN_VALUE;
		for (Vector3f vec : triangle) {
			height = Math.max(vec.y, height);
		}

		height += 0.2;
		height /= 5;

		return new Color(0f, 0f, height, 0.6f);
	}

	public void render(Camera camera, Graphics g) {
		for (ThreeDShape shape : polys) {
			shape.render(camera, g);
		}
	}
}
