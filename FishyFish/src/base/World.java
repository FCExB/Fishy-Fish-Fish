package base;

import entities.Entity;
import gameObjects.Fish;
import gameObjects.ThreeDShape;
import gameObjects.WaterSurface;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import util.Assets;

public class World {

	public static final float GRAVITY = 0.009f;

	private WaterSurface waterTop;
	private ThreeDShape waterSide;

	private ThreeDShape jettyTop;
	private ThreeDShape jettyFront;
	private ThreeDShape bucketFront;

	private Fish fish;

	public World() throws SlickException {

		fish = new Fish(new Vector3f(400, 0, -70), new Vector3f(), this);

		waterTop = new WaterSurface(new Vector3f(0, 0, 0), 800, 400, 50, 50);

		List<Vector3f> waterSideVecs = waterTop.getFrontRow();

		waterSideVecs.add(new Vector3f(800, -600, 0));
		waterSideVecs.add(new Vector3f(0, -600, 0));

		waterSide = new ThreeDShape(waterSideVecs, new Color(0, 0, 150, 100));

		List<Vector3f> jettyFrontVecs = new ArrayList<Vector3f>();

		jettyFrontVecs.add(new Vector3f(800, 100, 0));
		jettyFrontVecs.add(new Vector3f(1000, 100, 0));
		jettyFrontVecs.add(new Vector3f(1000, -600, 0));
		jettyFrontVecs.add(new Vector3f(800, -600, 0));

		jettyFront = new ThreeDShape(jettyFrontVecs, new Color(0.9f, 0f, 0f, 0.9f));

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
		fish.update(delta, container);
		
		Vector3f position = fish.getPosition();
		
		if(position.x > 880 && position.x < 930 && position.y < 150){
			fish = new Fish(new Vector3f(400, 0, -70), new Vector3f(), this); 
		}
		
		Input input = container.getInput();
		
		if(input.isKeyPressed(Input.KEY_ESCAPE)){
			fish = new Fish(new Vector3f(400, 0, -70), new Vector3f(), this);
		}
	}

	public void render(Camera camera, Graphics g) {
		Assets.WATER_SKY_BACKGROUND.draw(0, 0);

		waterTop.render(camera, g, fish);
		waterSide.render(camera, g);
		jettyTop.render(camera, g);
		bucketFront.render(camera, g);
		jettyFront.render(camera, g);

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

	public Color filterAtLocation(Vector3f location) {
		return null;
	}

	public boolean inWater(Vector3f position) {
		return position.x > 0 && position.x < 800 && position.y < -15;
	}

	public void enterWater(Vector3f position, float fishScale) {
		waterTop.enterWater(position, fishScale);
	}

	public void exitWater(Vector3f position, float fishScale) {
		waterTop.exitWater(position, fishScale);
	}
}
