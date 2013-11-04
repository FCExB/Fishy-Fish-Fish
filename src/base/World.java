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
	private final int waterNumPointsWide = 32;
	private final int waterNumPointsDeep = 10;

	private final WaterSurface waterTop;
	private final ThreeDShape waterSide;

	private final ThreeDShape boatTop;
	private final ThreeDShape boatSide;

	private final ThreeDShape bucketFront;

	private PlayerFish player;

	private final Set<Fish> fish;

	private final List<InWorldSpace> allObjects;

	private final float greatestX, smallestX, greatestY, smallestY;

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

		List<Vector3f> boatTopVecs = new ArrayList<Vector3f>();

		boatTopVecs.add(new Vector3f(-100, 20, -300));
		boatTopVecs.add(new Vector3f(100, 20, -300));
		boatTopVecs.add(new Vector3f(100, 20, -100));
		boatTopVecs.add(new Vector3f(-100, 20, -100));

		boatTop = new ThreeDShape(boatTopVecs, new Color(0.3f, 0f, 0f, 1f));

		List<Vector3f> boatSideVecs = new ArrayList<Vector3f>();

		boatSideVecs.add(new Vector3f(-100, 20, -100));
		boatSideVecs.add(new Vector3f(100, 20, -100));
		boatSideVecs.add(new Vector3f(80, -35, -100));
		boatSideVecs.add(new Vector3f(-80, -35, -100));

		greatestX = 100;
		smallestX = -100;
		greatestY = 20;
		smallestY = -35;

		boatSide = new ThreeDShape(boatSideVecs, new Color(0.5f, 0f, 0f, 1f));

		List<Vector3f> bucketFrontVecs = new ArrayList<Vector3f>();

		bucketFrontVecs.add(new Vector3f(-20, 20, -150));
		bucketFrontVecs.add(new Vector3f(20, 20, -150));
		bucketFrontVecs.add(new Vector3f(30, 70, -150));
		bucketFrontVecs.add(new Vector3f(-30, 70, -150));

		bucketFront = new ThreeDShape(bucketFrontVecs, new Color(0f, 0.6f, 0f,
				0.8f));

		allObjects = waterTop.getShapes();
		allObjects.add(boatSide);
		allObjects.add(boatTop);
		allObjects.add(waterSide);
		allObjects.add(bucketFront);
	}

	public void update(GameContainer container, int delta) {
		waterTop.updatePoints(delta);

		for (Fish f : fish) {
			f.update(delta, container);
		}

		Vector3f position = player.getPosition();

		if (position.x > -20 && position.x < 20 && position.y < 50
				&& position.y > 10) {
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
					rand.nextFloat() * -lowestHeight, -(rand.nextFloat() * 400));
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

		if (position.y > 20)
			return false;

		if (Math.abs(position.x) < 150 && position.y > -70) {
			return false;
		}

		return true;
	}

	public Vector3f hitBoundry(Entity entity) {
		if ((this.greatestX >= entity.smallestX() && this.greatestX <= entity
				.greatestX())
				|| (this.smallestX >= entity.smallestX() && this.smallestX <= entity
						.greatestX())
				|| (this.greatestX <= entity.greatestX() && this.smallestX >= entity
						.smallestX())
				|| (this.greatestX >= entity.greatestX() && this.smallestX <= entity
						.smallestX())) {
			// There is an X overlap

			if (this.greatestY > entity.smallestY()
					&& this.greatestY <= entity.greatestY()
					|| this.smallestY >= entity.smallestY()
					&& this.smallestY <= entity.greatestY()
					|| this.greatestY <= entity.greatestY()
					&& this.smallestY >= entity.smallestY()
					|| this.greatestY >= entity.greatestY()
					&& this.smallestY <= entity.smallestY()) {
				// There is a y overlap

				Vector3f result = entity.getPosition();

				if (result.y > 0) {
					return new Vector3f(0, 0.8f, 0);
				}

				Vector3f.sub(result, new Vector3f(0, 0, 0), result);
				result.z = 0;
				result.normalise();
				return result;
			}

		}

		if (entity.smallestZ() < -400) {
			return new Vector3f(0, 0, 1);
		}

		if (entity.greatestZ() > 0) {
			return new Vector3f(0, 0, -1);
		}

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
