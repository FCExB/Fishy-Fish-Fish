package base;

import entities.AIFish;
import entities.Entity;
import entities.Fish;
import entities.PlayerFish;
import gameObjects.InWorldSpace;
import gameObjects.ThreeDShape;
import gameObjects.WaterSurface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import util.AudioManager;

public class World {

	public static final float GRAVITY = 0.015f;

	public static final int lowestHeight = 300;

	private final GameplayState state;

	private final int waterWidth = 1200;
	private final int waterDepth = 400;
	private final int waterNumPointsWide = 45;
	private final int waterNumPointsDeep = 15;

	private final WaterSurface waterTop;
	private final ThreeDShape waterSide;

	private final ThreeDShape jettyTopTop;
	private final ThreeDShape jettyTopSide;

	private final ThreeDShape bucketFront;

	private PlayerFish player;

	private final Set<Fish> fish;

	private final List<InWorldSpace> allObjects;

	public World(GameplayState state) throws SlickException {

		this.state = state;

		fish = new HashSet<Fish>();

		do {
			player = new PlayerFish(new Vector3f(), this);
		} while (hitFish(player));

		waterTop = new WaterSurface(new Vector3f(-600, 0, 0), waterWidth,
				waterDepth, waterNumPointsWide, waterNumPointsDeep);

		List<Vector3f> waterSideVecs = waterTop.getFrontRow();

		waterSideVecs.add(new Vector3f(600, -400, 0));
		waterSideVecs.add(new Vector3f(-600, -400, 0));

		waterSide = new ThreeDShape(waterSideVecs, new Color(0, 0, 50, 100));

		List<Vector3f> jettyTopTopVecs = new ArrayList<Vector3f>();

		jettyTopTopVecs.add(new Vector3f(-95, 50, -300));
		jettyTopTopVecs.add(new Vector3f(95, 50, -300));
		jettyTopTopVecs.add(new Vector3f(95, 50, -100));
		jettyTopTopVecs.add(new Vector3f(-95, 50, -100));

		jettyTopTop = new ThreeDShape(jettyTopTopVecs, new Color(0.3f, 0f, 0f,
				1f));

		List<Vector3f> jettyTopSideVecs = new ArrayList<Vector3f>();

		jettyTopSideVecs.add(new Vector3f(-95, 50, -100));
		jettyTopSideVecs.add(new Vector3f(95, 50, -100));
		jettyTopSideVecs.add(new Vector3f(95, 20, -100));
		jettyTopSideVecs.add(new Vector3f(-95, 20, -100));

		jettyTopSide = new ThreeDShape(jettyTopSideVecs, new Color(0.5f, 0f,
				0f, 1f));

		List<Vector3f> bucketFrontVecs = new ArrayList<Vector3f>();

		bucketFrontVecs.add(new Vector3f(-20, 50, -150));
		bucketFrontVecs.add(new Vector3f(20, 50, -150));
		bucketFrontVecs.add(new Vector3f(30, 100, -150));
		bucketFrontVecs.add(new Vector3f(-30, 100, -150));

		bucketFront = new ThreeDShape(bucketFrontVecs, new Color(0f, 0.6f, 0f,
				0.8f));

		allObjects = waterTop.getShapes();
		allObjects.add(jettyTopSide);
		allObjects.add(jettyTopTop);
		allObjects.add(waterSide);
		allObjects.add(bucketFront);
	}

	public void update(GameContainer container, int delta) {
		waterTop.updatePoints(delta);

		for (Fish f : fish) {
			f.update(delta, container);
		}

		Vector3f position = player.getPosition();

		if (position.x > -20 && position.x < 20 && position.y < 65
				&& position.y > 40) {
			state.fishLandsInBucket();
		}
	}

	public void addAIFish() {
		Fish f = new AIFish(this);
		fish.add(f);
		allObjects.add(f);
	}

	public void render(Camera camera, Graphics g) {

		Collections.sort(allObjects);

		for (InWorldSpace thing : allObjects) {
			thing.render(camera, g);
		}
	}

	public boolean hitFish(Entity entity) {
		if (entity != player) {
			return false;
		}

		for (Fish ph : fish) {
			if (entity.collides(ph)) {
				return true;
			}
		}

		return false;
	}

	public boolean playerHitFish() {
		return hitFish(player);
	}

	private static final Random rand = new Random();

	public Vector3f getRandomClearPosition() {
		Vector3f point;

		do {
			point = new Vector3f((rand.nextFloat() - 0.5f) * 1000,
					rand.nextFloat() * -lowestHeight,
					-(rand.nextFloat() * 300) - 50);
		} while (!positionClear(point));

		return point;
	}

	public boolean positionClear(Vector3f position) {

		if (position.z < -350) {
			return false;
		}

		if (position.z > -50)
			return false;

		if (position.x < -500)
			return false;

		if (position.x > 500)
			return false;

		if (position.y < -300)
			return false;

		if (position.y > 0)
			return false;

		return true;
	}

	public Vector3f hitBoundry(Entity entity) {

		if (entity.greatestY() > 50 && entity.smallestY() < 50
				&& entity.getPosition().x > -100
				&& entity.getPosition().x < 100) {
			return new Vector3f(0, 1, 0);
		}

		if (entity.smallestZ() < -400) {
			return new Vector3f(0, 0, 1);
		}

		if (entity.greatestZ() > 0)
			return new Vector3f(0, 0, 1);

		return new Vector3f();

	}

	public void resetPlayer() {
		if (player.shouldReset()) {
			fish.remove(player);
			allObjects.remove(player);
			do {
				player = new PlayerFish(new Vector3f(), this);
			} while (hitFish(player));

			fish.add(player);
			allObjects.add(player);
		}
	}

	public void resetAll() {
		allObjects.removeAll(fish);
		fish.clear();

		do {
			player = new PlayerFish(new Vector3f(), this);
		} while (hitFish(player));

		fish.add(player);
		allObjects.add(player);

		waterTop.reset();
	}

	public Color filterAtLocation(Vector3f location) {
		return null;
	}

	public boolean inWater(Vector3f position) {
		return position.y < -15;
	}

	public void crossWaterLevel(Vector3f position, float fishScale,
			float verticalSpeed) {
		AudioManager.playSound(position.x, position.y, position.z,
				AudioManager.SPLASH);
		waterTop.crossWaterLevel(position, fishScale, verticalSpeed);
	}

	public int getWidth() {
		return waterWidth;
	}

	public int getDepth() {
		return waterDepth;
	}
}
