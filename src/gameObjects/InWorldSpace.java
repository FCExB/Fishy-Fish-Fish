package gameObjects;

import org.newdawn.slick.Graphics;

import base.Camera;

public interface InWorldSpace extends Comparable<InWorldSpace> {
	public float getZ();

	public void render(Camera camera, Graphics g);
}
