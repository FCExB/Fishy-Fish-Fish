package base;

import entities.Entity;
import gameObjects.AIFish;
import gameObjects.Fish;
import gameObjects.PlayerFish;
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

import util.Assets;

public class World {

	public static final float GRAVITY = 0.013f;

	private final GameplayState state;

	private final Vector3f playerResetPosition = new Vector3f(400, -20, -100);

	private final int waterWidth = 800;
	private final int waterDepth = 400;
	private final int waterNumPointsWide = 35;
	private final int waterNumPointsDeep = 17;

	private final WaterSurface waterTop;
	private final ThreeDShape waterSide;

	private final ThreeDShape jettyTop;
	private final ThreeDShape jettyFront;
	private final ThreeDShape bucketFront;

	private PlayerFish player = new PlayerFish(playerResetPosition,
			new Vector3f(), this);

	private final Set<Fish> fish;

	public World(GameplayState state) throws SlickException {

		this.state = state;

		fish = new HashSet<Fish>();

		waterTop = new WaterSurface(new Vector3f(0, 0, 0), waterWidth,
				waterDepth, waterNumPointsWide, waterNumPointsDeep);

		List<Vector3f> waterSideVecs = waterTop.getFrontRow();

		waterSideVecs.add(new Vector3f(800, -600, 0));
		waterSideVecs.add(new Vector3f(0, -600, 0));

		waterSide = new ThreeDShape(waterSideVecs, new Color(0, 0, 150, 100));

		List<Vector3f> jettyFrontVecs = new ArrayList<Vector3f>();

		jettyFrontVecs.add(new Vector3f(800, 100, 0));
		jettyFrontVecs.add(new Vector3f(1000, 100, 0));
		jettyFrontVecs.add(new Vector3f(1000, -600, 0));
		jettyFrontVecs.add(new Vector3f(800, -600, 0));

		jettyFront = new ThreeDShape(jettyFrontVecs, new Color(0.9f, 0f, 0f,
				0.9f));

		List<Vector3f> jettyTopVecs = new ArrayList<Vector3f>();

		jettyTopVecs.add(new Vector3f(800, 100, 0));
		jettyTopVecs.add(new Vector3f(1000, 100, 0));
		jettyTopVecs.add(new Vector3f(1000, 100, -100));
		jettyTopVecs.add(new Vector3f(800, 100, -100));

		jettyTop = new ThreeDShape(jettyTopVecs, new Color(0.4f, 0f, 0f, 0.8f));

		List<Vector3f> bucketFrontVecs = new ArrayList<Vector3f>();

		bucketFrontVecs.add(new Vector3f(880, 100, 0));
		bucketFrontVecs.add(new Vector3f(920, 100, 0));
		bucketFrontVecs.add(new Vector3f(930, 150, 0));
		bucketFrontVecs.add(new Vector3f(870, 150, 0));

		bucketFront = new ThreeDShape(bucketFrontVecs, new Color(0f, 0.6f, 0f,
				0.8f));
	}

	public void update(GameContainer container, int delta) {
		waterTop.updatePoints(delta);

		for (Fish f : fish) {
			f.update(delta, container);
		}

		Vector3f position = player.getPosition();

		if (position.x > 880 && position.x < 930 && position.y < 150) {
			state.fishLandsInBucket();
			fish.add(new AIFish(this));
		}
	}

	public void render(Camera camera, Graphics g) {
		Assets.WATER_SKY_BACKGROUND.draw(0, 0);

		waterTop.render(camera, g, new TreeSet<Fish>(fish));
		waterSide.render(camera, g);
		jettyTop.render(camera, g);
		bucketFront.render(camera, g);
		jettyFront.render(camera, g);

	}

	public boolean positionClear(Vector3f position) {

		if (position.z < -400) {
			return false;
		}

		if (position.z > -7)
			return false;

		if (position.x < 1)
			return false;

		if (position.x > 770 && position.y < 100)
			return false;

		return true;
	}

	public boolean positionClear(Entity entity) {

		Vector3f position = entity.getPosition();

		if (position.z < -400) {
			return false;
		}

		if (position.z > -7)
			return false;

		if (position.x < 1)
			return false;

		if (position.x > 770 && position.y < 100)
			return false;

		return true;
	}

	public void resetPlayer() {
		fish.remove(player);
		player = new PlayerFish(playerResetPosition, new Vector3f(), this);
		fish.add(player);
	}

	public void resetAll() {
		fish.clear();

		player = new PlayerFish(playerResetPosition, new Vector3f(), this);
		fish.add(player);

		waterTop.reset();

		for (int i = 0; i < 100; i++) {
			fish.add(new AIFish(this));
		}
	}

	public Color filterAtLocation(Vector3f location) {
		return null;
	}

	public boolean inWater(Vector3f position) {
		return position.x > 0 && position.x < 800 && position.y < -15;
	}

	public void crossWaterLevel(Vector3f position, float fishScale,
			float verticalSpeed) {
		waterTop.crossWaterLevel(position, fishScale, verticalSpeed);
	}

	public int getWidth() {
		return waterWidth;
	}

	public int getDepth() {
		return waterDepth;
	}
}
