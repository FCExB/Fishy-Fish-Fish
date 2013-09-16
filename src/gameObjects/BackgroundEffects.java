package gameObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import base.Camera;

public class BackgroundEffects {

	private final List<ThreeDShape> shapes = new ArrayList<ThreeDShape>();
	private final Random rand = new Random();

	public BackgroundEffects() {

		for (int j = 0; j < 5; j++) {
			List<Vector3f> vectors = new ArrayList<Vector3f>();

			for (int i = 0; i < 6; i++) {
				vectors.add(newValues(new Vector3f(1000, 1000, 1000)));
			}

			shapes.add(new ThreeDShape(vectors, randomColor()));
		}
	}

	private Color randomColor() {
		return new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(),
				rand.nextFloat());
	}

	private Vector3f newValues(Vector3f vector) {

		vector.x += rand.nextFloat() * 10;
		vector.y += rand.nextFloat() * 10;
		vector.z += rand.nextFloat() * 10;

		if (vector.x > 600 || vector.x < -600) {
			vector.x = rand.nextFloat() * 200 - 600;
		}

		if (vector.y > 400 || vector.y < -400) {
			vector.y = rand.nextFloat() * 100 - 400;
		}

		if (vector.z > -400 || vector.z < -500) {
			vector.z = -400 - rand.nextFloat() * 100;
		}

		return vector;
	}

	public void update(int delta) {
		for (ThreeDShape shape : shapes) {
			for (Vector3f vec : shape.getPoints()) {
				newValues(vec);
			}

			shape.setColor(randomColor());
		}
	}

	public void render(Camera camera, Graphics g) {
		for (ThreeDShape shape : new TreeSet<ThreeDShape>(shapes)) {
			shape.render(camera, g);
		}
	}
}
