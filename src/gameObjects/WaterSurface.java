package gameObjects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.lwjgl.util.vector.Vector3f;

public class WaterSurface {
	private static final int UPDATE_FREQUENCY = 150;

	private static final float SHIMMER_CHANCE = 0.4f;
	private static final float SHIMMER_RANGE = 0.6f;

	private static final float INCREASE_SHIMMER_FACTOR = 1.18f;

	private final Vector3f[][] points;
	private final int numDeep;
	private final int numWide;

	private final float waterLevel;

	private int timeSinceUpdate = 0;

	private final Random rand = new Random();

	private final Set<WaterSurfaceTriangle> triangles = new HashSet<WaterSurfaceTriangle>();

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

		setupTriangles();
	}

	private void setupTriangles() {

		for (int z = 0; z < numDeep; z++) {

			for (int x = z % 2; x < numWide; x += 2) {
				if (z - 1 >= 0) {
					if (x - 1 >= 0) {

						List<Vector3f> triangle = new ArrayList<Vector3f>(3);

						triangle.add(points[z][x]);
						triangle.add(points[z][x - 1]);
						triangle.add(points[z - 1][x]);

						triangles.add(new WaterSurfaceTriangle(triangle));

					}

					if (x + 1 < numWide) {

						List<Vector3f> triangle = new ArrayList<Vector3f>(3);

						triangle.add(points[z][x]);
						triangle.add(points[z][x + 1]);
						triangle.add(points[z - 1][x]);

						triangles.add(new WaterSurfaceTriangle(triangle));

					}
				}

				if (z + 1 < numDeep) {
					if (x - 1 >= 0) {
						List<Vector3f> triangle = new ArrayList<Vector3f>(3);

						triangle.add(points[z][x]);
						triangle.add(points[z][x - 1]);
						triangle.add(points[z + 1][x]);

						triangles.add(new WaterSurfaceTriangle(triangle));

					}

					if (x + 1 < numWide) {

						List<Vector3f> triangle = new ArrayList<Vector3f>(3);

						triangle.add(points[z][x]);
						triangle.add(points[z][x + 1]);
						triangle.add(points[z + 1][x]);

						triangles.add(new WaterSurfaceTriangle(triangle));

					}
				}
			}
		}

	}

	public void updatePoints(int delta) {
		timeSinceUpdate += delta;

		if (timeSinceUpdate >= UPDATE_FREQUENCY) {

			timeSinceUpdate = 0;

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

			for (WaterSurfaceTriangle triangle : triangles) {
				triangle.update();
			}
		}
	}

	public List<InWorldSpace> getShapes() {
		System.out.println("Number of triangles: " + triangles.size());
		return new ArrayList<InWorldSpace>(triangles);
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

		verticalSpeed += 0.3 * Math.signum(verticalSpeed);
		verticalSpeed /= 10f;

		float firstRingRange = 40 * fishScale * Math.abs(verticalSpeed);
		float secondRingRange = 60 * fishScale * Math.abs(verticalSpeed);
		float firstRingSize = 50 * fishScale * verticalSpeed;
		float secondRingSize = 35 * fishScale * verticalSpeed;

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
