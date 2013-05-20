package base;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import entities.Entity;
import gameObjects.ThreeDShape;

public class World {
	
	public static final float GRAVITY = 0.005f;
	
	private ThreeDShape waterTop;
	private ThreeDShape waterSide;

	public World() throws SlickException {
		
		waterTop = new ThreeDShape(new Vector3f[] { 
				new Vector3f(0, 0, 0),
				new Vector3f(0, 0, -100), 
				new Vector3f(800, 0, -100),
				new Vector3f(800, 0, 0), 
				}, new Color(0, 0, 250, 100));
		
		waterSide = new ThreeDShape(new Vector3f[] { 
				new Vector3f(0, 0, 0),
				new Vector3f(800, 0, 0), 
				new Vector3f(800, -600, 0),
				new Vector3f(0, -600, 0)
				}, new Color(0, 0, 150, 100));
	}

	public void update(int deltaT) {
	}

	public void render(Camera camera, Graphics g) {
		waterTop.render(camera, g);
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
