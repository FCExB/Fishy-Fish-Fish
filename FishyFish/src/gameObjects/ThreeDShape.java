package gameObjects;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import base.Camera;

public class ThreeDShape implements Comparable<ThreeDShape> {
	private List<Vector3f> points;
	private Color color;
	private float maxZ = -1000;

	public ThreeDShape(List<Vector3f> points, Color color) {
		this.points = points;
		this.color = color;

		for (Vector3f vec : points) {
			maxZ = Math.max(maxZ, vec.z);
		}
		
		return;
	}

	public void render(Camera camera, Graphics g) {

		Polygon shape = new Polygon();

		for (Vector3f vec : points) {

			Vector2f cameraSpace = camera.worldToCameraSpace(vec);

			shape.addPoint(cameraSpace.getX(), cameraSpace.getY());
		}

		g.setColor(color);
		g.fill(shape);
	}

	public float getMaxZ(){
		return maxZ;
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof ThreeDShape)){
			return false;
		}
		
		ThreeDShape that = (ThreeDShape) o;
		
		return this.points.equals(that.points);
	}

	@Override
	public int compareTo(ThreeDShape that) {

		if(this.equals(that)){
			return 0;
		}
		
		if(this.maxZ<that.maxZ){
			return -1;
		}
		
		return 1;
	}
}
