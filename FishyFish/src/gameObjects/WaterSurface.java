package gameObjects;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Graphics;

import base.Camera;

public class WaterSurface {
	private Vector3f[][] points;
	private int numDeep;
	private int numWide;

	public WaterSurface(Vector3f backLeft, float width, float depth,
			int numWide, int numDeep) {
		float dx = width / numWide;
		float dz = depth / numDeep;

		points = new Vector3f[numDeep][numWide];

		for (int z = 0; z < numDeep; z++) {
			for (int x = 0; x < numWide; x++) {
				points[z][x] = new Vector3f(backLeft.x + x * dx, backLeft.y,
						backLeft.z - z * dz);
			}
		}

		this.numDeep = numDeep;
		this.numWide = numWide;
	}

	public void render(Camera camera, Graphics g) {

		for (int z = 0; z < numDeep; z++) {

			for (int x = z % 2; x < numWide; x += 2) {
				if (z - 1 >= 0) {
					if (x - 1 >= 0) {
						renderTriangle(new Vector3f[] { points[z][x],
								points[z][x - 1], points[z - 1][x] }, camera, g);
					}

					if (x + 1 < numWide) {

					}
				}

				if (z + 1 < numDeep) {
					if (x - 1 >= 0) {

					}

					if (x + 1 < numWide) {

					}
				}
			}
		}

	}

	private void renderTriangle(Vector3f[] points, Camera camera, Graphics g) {

	}
}
