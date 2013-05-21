package base;

import entities.Entity;
import gameObjects.Fish;
import gameObjects.ThreeDShape;
import gameObjects.WaterSurface;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class World {
	
	public static final float GRAVITY = 0.005f;
	
	private WaterSurface waterTop;
	private ThreeDShape waterSide;

	public World() throws SlickException {
		
		waterTop = new WaterSurface(new Vector3f(0, 0, 0),800,100,10,10);
		
		List<Vector3f> waterSideVecs = new ArrayList<Vector3f>();
		
		waterSideVecs.add(new Vector3f(0, 0, 0));
		waterSideVecs.add(new Vector3f(800, 0, 0));
		waterSideVecs.add(new Vector3f(800, -600, 0));
		waterSideVecs.add(new Vector3f(0, -600, 0));
		
		waterSide = new ThreeDShape(waterSideVecs, new Color(0, 0, 150, 100));
	}

	public void update(int delta) {
		waterTop.updatePoints(delta);
	}

	public void render(Camera camera, Graphics g, Fish fish) {
		waterTop.render(camera, g);
		fish.render(camera, g);
		waterSide.render(camera, g);
	}

	public boolean positionClear(Entity entity) {

		return true;
	}

	public Color filterAtLocation(Vector3f location) {
		return null;
	}
	
	public boolean inWater(Vector3f position){
		return position.x>0 && position.x<800 &&
				position.y< 0;
	}
}
