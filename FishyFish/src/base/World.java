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
import org.newdawn.slick.SlickException;

import util.Assets;

public class World {

	public static final float GRAVITY = 0.005f;

	private WaterSurface waterTop;
	private ThreeDShape waterSide;

	private ThreeDShape jettyFront;

	private Fish fish;

	public World() throws SlickException {
		
		fish = new Fish(new Vector3f(400, 0, -70), new Vector3f(), this);
		
		waterTop = new WaterSurface(new Vector3f(0, 0, 0),800,400,50,50);
		
		List<Vector3f> waterSideVecs = waterTop.getFrontRow();
		
		waterSideVecs.add(new Vector3f(800, -600, 0));
		waterSideVecs.add(new Vector3f(0, -600, 0));
		
		waterSide = new ThreeDShape(waterSideVecs, new Color(0, 0, 150, 100));
		
		
		List<Vector3f> jettyVecs = new ArrayList<Vector3f>();
		
		jettyVecs.add(new Vector3f(800, 100, 0));
		jettyVecs.add(new Vector3f(1000, 100, 0));
		jettyVecs.add(new Vector3f(1000, -600, 0));
		jettyVecs.add(new Vector3f(800, -600, 0));
		
		jettyFront = new ThreeDShape(jettyVecs, new Color(1f,0f,0f,0.8f));
	}

	public void update(GameContainer container, int delta) {
		waterTop.updatePoints(delta);
		fish.update(delta, container);
	}

	public void render(Camera camera, Graphics g) {
		Assets.WATER_SKY_BACKGROUND.draw(0, 0);

		
		waterTop.render(camera, g, fish);
		waterSide.render(camera, g);
		jettyFront.render(camera, g);
	}

	public boolean positionClear(Entity entity) {

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
