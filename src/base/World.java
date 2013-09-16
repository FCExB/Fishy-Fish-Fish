package base;

import entities.AIFish;
import entities.Entity;
import entities.Fish;
import entities.PlayerFish;
import gameObjects.ThreeDShape;
import gameObjects.WaterSurface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import util.AudioManager;

public class World {

	public static final float GRAVITY = 0.012f;

	private final GameplayState state;

	private final Vector3f playerResetPosition1 = new Vector3f(-300, -20, -200);
	private final Vector3f playerResetPosition2 = new Vector3f(300, -20, -200);

	private final int waterWidth = 1200;
	private final int waterDepth = 400;
	private final int waterNumPointsWide = 45;
	private final int waterNumPointsDeep = 15;

	private final WaterSurface waterTop;
	private final ThreeDShape waterSide;

	// private final WaterSurface smallWaterTop;
	// private final ThreeDShape smallWaterSide;

	private final ThreeDShape jettyTopTop;
	private final ThreeDShape jettyTopSide;
	// private final ThreeDShape jettyFront;
	private final ThreeDShape bucketFront;

	private PlayerFish player = new PlayerFish(playerResetPosition1,
			new Vector3f(), this);

	private final Set<Fish> fish;

	public World(GameplayState state) throws SlickException {

		this.state = state;

		fish = new HashSet<Fish>();

		waterTop = new WaterSurface(new Vector3f(-600, 0, 0), waterWidth,
				waterDepth, waterNumPointsWide, waterNumPointsDeep);

		List<Vector3f> waterSideVecs = waterTop.getFrontRow();

		waterSideVecs.add(new Vector3f(600, -400, 0));
		waterSideVecs.add(new Vector3f(-600, -400, 0));

		waterSide = new ThreeDShape(waterSideVecs, new Color(0, 0, 50, 100));

		// smallWaterTop = new WaterSurface(new Vector3f(970, 0, 0), 130,
		// waterDepth, 6, waterNumPointsDeep);
		//
		// List<Vector3f> smallWaterSideVecs = smallWaterTop.getFrontRow();
		//
		// smallWaterSideVecs.add(new Vector3f(1000, -600, 0));
		// smallWaterSideVecs.add(new Vector3f(970, -600, 0));
		//
		// smallWaterSide = new ThreeDShape(smallWaterSideVecs, new Color(0, 0,
		// 50, 100));

		// List<Vector3f> jettyFrontVecs = new ArrayList<Vector3f>();
		//
		// jettyFrontVecs.add(new Vector3f(820, 50, -100));
		// jettyFrontVecs.add(new Vector3f(970, 50, -100));
		// jettyFrontVecs.add(new Vector3f(970, -600, -100));
		// jettyFrontVecs.add(new Vector3f(820, -600, -100));
		//
		// jettyFront = new ThreeDShape(jettyFrontVecs, new Color(0.9f, 0f, 0f,
		// 0.95f));

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
	}

	public void update(GameContainer container, int delta) {
		waterTop.updatePoints(delta);
		// smallWaterTop.updatePoints(delta);

		for (Fish f : fish) {
			f.update(delta, container);
		}

		Vector3f position = player.getPosition();

		if (position.x > -20 && position.x < 20 && position.y < 80
				&& position.y > 50) {
			state.fishLandsInBucket();
			fish.add(new AIFish(this));
		}
	}

	public void render(Camera camera, Graphics g) {
		// Assets.WATER_SKY_BACKGROUND.draw(0, 0);

		// smallWaterTop.render(camera, g, new TreeSet<Fish>());
		// smallWaterSide.render(camera, g);

		if (inWater(player.getPosition())) {
			waterTop.render(camera, g, new TreeSet<Fish>(fish));
			// jettyFront.render(camera, g);
			jettyTopTop.render(camera, g);
			jettyTopSide.render(camera, g);
			waterSide.render(camera, g);
			bucketFront.render(camera, g);
		} else {
			fish.remove(player);
			waterTop.render(camera, g, new TreeSet<Fish>(fish));
			// jettyFront.render(camera, g);
			jettyTopTop.render(camera, g);
			jettyTopSide.render(camera, g);
			waterSide.render(camera, g);
			player.render(camera, g);
			bucketFront.render(camera, g);
			fish.add(player);
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

		return true;
	}

	public Vector3f hitBoundry(Entity entity) {

		// if (entity.greatestX() > 820 && entity.smallestY() < 50) {
		// return new Vector3f(1, 0, 0);
		// }

		if (entity.greatestY() > 50 && entity.smallestY() < 50
				&& entity.getPosition().x > -95 && entity.getPosition().x < 95) {
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
			if (Math.random() > 0.5) {
				player = new PlayerFish(playerResetPosition1, new Vector3f(),
						this);
			} else {
				player = new PlayerFish(playerResetPosition2, new Vector3f(),
						this);
			}
			fish.add(player);
		}
	}

	public void resetAll() {
		fish.clear();

		if (Math.random() > 0.5) {
			player = new PlayerFish(playerResetPosition1, new Vector3f(), this);
		} else {
			player = new PlayerFish(playerResetPosition2, new Vector3f(), this);
		}
		fish.add(player);

		waterTop.reset();
		//
		// for (int i = 0; i < 10; i++) {
		// fish.add(new AIFish(this));
		// }
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
