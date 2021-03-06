package gameObjects;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import base.Camera;

public class BackgroundEffects {

	private final List<BackgroundBlob> blobs = new ArrayList<BackgroundBlob>();

	public BackgroundEffects() {

		for (int j = 0; j < 100; j++) {
			List<Vector3f> vectors = new ArrayList<Vector3f>();

			vectors.add(new Vector3f(-10, 10, 0));
			vectors.add(new Vector3f(10, 10, 0));
			vectors.add(new Vector3f(10, -10, 0));
			vectors.add(new Vector3f(-10, -10, 0));

			blobs.add(new BackgroundBlob(vectors));
		}
	}

	public void update(int delta, int score) {
		for (BackgroundBlob blob : blobs) {
			blob.update(delta, score);
		}
	}

	public void render(Camera camera, Graphics g) {
		g.setBackground(new Color(0.01f, 0.01f, 0.01f));

		for (BackgroundBlob blob : blobs) {
			blob.render(camera, g);
		}
	}

}
