package gameObjects;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import base.Camera;

public class ThreeDShape {
	private Vector3f[] points;
	private Color color;

	public ThreeDShape(Vector3f[] points, Color color) {
		this.points = points;
		this.color = color;
	}
	
	public void render(Camera camera, Graphics g) {
		
		Polygon shape = new Polygon();
		
		for(int i = 0; i < points.length; i++){
			
			Vector2f cameraSpace = camera.worldToCameraSpace(points[i]);
			
			shape.addPoint(cameraSpace.getX(), cameraSpace.getY());
		}
		
		g.setColor(color);
		g.fill(shape);
	}
}
