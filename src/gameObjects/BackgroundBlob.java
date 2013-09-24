package gameObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import base.Camera;

public class BackgroundBlob extends ThreeDShape {

	private final Vector3f location;

	private final Vector3f movementDirection;

	private final List<Vector3f> defaultVecs;

	private static final Random rand = new Random();

	private float scale = 1f;

	public BackgroundBlob(List<Vector3f> vecs) {
		super(vecs, randomColor());

		defaultVecs = new ArrayList<Vector3f>();

		for (Vector3f vec : vecs) {
			defaultVecs.add(new Vector3f(vec));
		}

		movementDirection = randomDirection(new Vector3f());
		location = randomLocation(new Vector3f());
	}

	private Vector3f randomDirection(Vector3f vec) {
		vec.x = rand.nextFloat() - 0.5f;
		vec.y = rand.nextFloat() - 0.5f;
		vec.z = 0;

		vec.normalise();
		return vec;
	}

	private Vector3f randomLocation(Vector3f vec) {
		vec.x = (rand.nextFloat() - 0.5f) * 1000;
		vec.y = rand.nextFloat() * 700 - 400;
		vec.z = -400 - rand.nextFloat() * 100;

		return vec;
	}

	public void update(int delta, int score) {
		score += 1;

		updatePosition(delta, score);
		updateColor(delta, score);
		updateScale(delta, score);
	}

	private static float scaleFadeSpeed = 0.001f;

	private float nextScale = 1;

	private void updateScale(int delta, int score) {

		if (Math.abs(scale - nextScale) < 0.1) {
			nextScale = 1 + rand.nextFloat() * ((score - 1) / 3f);
		}

		scale += Math.signum(nextScale - scale) * scaleFadeSpeed * delta
				* ((float) score / 10);
	}

	private static final float speed = 0.1f;

	private void updatePosition(int delta, int score) {
		location.x += movementDirection.x * delta * speed * ((float) score / 8);
		location.y += movementDirection.y * delta * speed * ((float) score / 8);

		if (location.x > 500 || location.x < -500) {
			randomDirection(movementDirection);

		}

		if (location.y > 300 || location.y < -400) {
			randomDirection(movementDirection);
		}
	}

	private float rFade = rand.nextFloat() * 2 - 1,
			gFade = rand.nextFloat() * 2 - 1, bFade = rand.nextFloat() * 2 - 1,
			aFade = rand.nextFloat() * 2 - 1;

	private static float colorFadeSpeed = 0.001f;

	private static final float maxA = 0.5f;
	private static final float colorMax = 0.6f;

	private void updateColor(int delta, int score) {

		color.r += rFade * colorFadeSpeed * delta * ((float) score / 10);
		color.g += gFade * colorFadeSpeed * delta * ((float) score / 10);
		color.b += bFade * colorFadeSpeed * delta * ((float) score / 10);
		color.a += aFade * colorFadeSpeed * delta * ((float) score / 13);

		if (color.r > colorMax || color.r < 0.1) {
			rFade = rand.nextFloat() * -Math.signum(rFade);
		}

		if (color.g > colorMax || color.g < 0.1) {
			gFade = rand.nextFloat() * -Math.signum(gFade);
		}

		if (color.b > colorMax || color.b < 0.1) {
			bFade = rand.nextFloat() * -Math.signum(bFade);
		}

		if (color.a > maxA || color.a < 0.3) {
			aFade = rand.nextFloat() * -Math.signum(aFade);
		}
	}

	@Override
	public void render(Camera camera, Graphics g) {
		for (int i = 0; i < defaultVecs.size(); i++) {
			defaultVecs.get(i).scale(scale);
			Vector3f.add(location, defaultVecs.get(i), points.get(i));
			defaultVecs.get(i).scale(1 / scale);
		}

		super.render(camera, g);
	}

	private static Color randomColor() {
		return new Color(rand.nextFloat() * colorMax, rand.nextFloat()
				* colorMax, rand.nextFloat() * colorMax, rand.nextFloat()
				* maxA);
	}
}
