package gameObjects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import base.Camera;

public class WaterSurface {
	private static final int UPDATE_FREQUENCY = 100;

	private static final float SHIMMER_CHANCE = 0.3f;
	private static final float SHIMMER_RANGE = 0.6f;

	private static final float INCREASE_SHIMMER_FACTOR = 1.2f;

	private final Vector3f[][] points;
	private final int numDeep;
	private final int numWide;

	private final float waterLevel;

	private int timeSinceUpdate = 0;

	private final Random rand = new Random();

	private final SortedSet<ThreeDShape> polys = new TreeSet<ThreeDShape>();

	public WaterSurface(Vector3f frontLeft, float width, float depth,
			int numWide, int numDeep) {
		waterLevel = frontLeft.y;

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
		polys.clear();

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

			// int z = 0; //rand.nextInt(numDeep);
			// int zEnd = numDeep; // rand.nextInt(numDeep);
			//
			// int x = 0; // rand.nextInt(numWide);
			// int xEnd = numWide; // rand.nextInt(numWide);

			for (int z = 0; z < numDeep; z++) {
				for (int x = 0; x < numWide; x++) {
					float average = waterLevel;
					int numSides = 1;

					if (z - 1 >= 0) {
						if (x - 1 >= 0) {
							average += points[z - 1][x - 1].y
									* INCREASE_SHIMMER_FACTOR;
							numSides++;
						}

						if (x + 1 < numWide) {
							average += points[z - 1][x + 1].y
									* INCREASE_SHIMMER_FACTOR;
							numSides++;
						}

					}

					if (z + 1 < numDeep) {
						if (x - 1 >= 0) {
							average += points[z + 1][x - 1].y
									* INCREASE_SHIMMER_FACTOR;
							numSides++;
						}

						if (x + 1 < numWide) {
							average += points[z + 1][x + 1].y
									* INCREASE_SHIMMER_FACTOR;
							numSides++;
						}
					}
					points[z][x].y = average / numSides;

					if (rand.nextFloat() < SHIMMER_CHANCE) {
						points[z][x].y += (rand.nextFloat() - 0.5) * 2
								* SHIMMER_RANGE;
					}
				}
			}

			updateTriangles();
		}
	}

	private Color calculateColor(List<Vector3f> triangle) {

		// return new Color(rand.nextFloat(), rand.nextFloat(),
		// rand.nextFloat());

		float height = Float.MIN_VALUE;
		for (Vector3f vec : triangle) {
			height = Math.max(vec.y, height);
		}

		height += 0.5;
		height /= 5;

		return new Color(0f, 0f, height, 0.7f);
	}

	public void render(Camera camera, Graphics g, SortedSet<Fish> fish) {

		Iterator<Fish> theFish = fish.iterator();
		Iterator<ThreeDShape> theShapes = polys.iterator();

		Fish nextFish = theFish.next();
		ThreeDShape nextShape = theShapes.next();

		boolean fishEmpty = false, shapesEmpty = false;

		while (!fishEmpty && !shapesEmpty) {

			if (nextFish.getPosition().z < nextShape.getMaxZ()) {
				nextFish.render(camera, g);
				if (theFish.hasNext()) {
					nextFish = theFish.next();
				} else {
					fishEmpty = true;
				}
			} else {
				nextShape.render(camera, g);
				if (theShapes.hasNext()) {
					nextShape = theShapes.next();
				} else {
					shapesEmpty = true;
				}
			}
		}

		if (fishEmpty) {
			nextShape.render(camera, g);
			while (theShapes.hasNext()) {
				nextShape = theShapes.next();
				nextShape.render(camera, g);
			}
		}

		if (shapesEmpty) {
			nextFish.render(camera, g);
			while (theFish.hasNext()) {
				nextFish = theFish.next();
				nextFish.render(camera, g);
			}
		}
	}

	public List<Vector3f> getFrontRow() {
		List<Vector3f> result = new ArrayList<Vector3f>();
		for (int x = 0; x < numWide; x++) {
			result.add(points[0][x]);
		}
		return result;
	}

	public void crossWaterLevel(Vector3f crossPoint, float fishScale,
			float verticalSpeed) {

		verticalSpeed += 0.1;

		float firstRingRange = 40 * fishScale * Math.abs(verticalSpeed);
		float secondRingRange = 60 * fishScale * Math.abs(verticalSpeed);
		float firstRingSize = 50 * fishScale * verticalSpeed;
		float secondRingSize = 30 * fishScale * verticalSpeed;

		for (Vector3f[] vecs : points) {
			for (Vector3f vec : vecs) {
				Vector3f difference = Vector3f.sub(vec, crossPoint, null);
				float distance = difference.length();

				if (distance < firstRingRange) {
					vec.y += firstRingSize;
				} else if (distance < secondRingRange) {
					vec.y -= secondRingSize;
				}
			}
		}

	}

	public void reset() {
		for (Vector3f[] line : points) {
			for (Vector3f point : line) {
				point.y = waterLevel;
			}
		}
	}
}
