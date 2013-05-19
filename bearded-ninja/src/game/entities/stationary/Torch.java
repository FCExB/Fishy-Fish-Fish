package game.entities.stationary;

import game.entities.Entity;
import game.entities.LightEmitting;
import game.world.World;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import util.Assets;

public class Torch extends Entity implements LightEmitting {

	public Torch(Vector3f position, World world) {
		super(Assets.TORCH, false, false, 16, position, world);
	}

	@Override
	public Color filterAt(Vector3f pos) {
		Vector3f difference = Vector3f.sub(pos, position, null);

		float ratio = 90f / difference.length();
		ratio = Math.min(0.9f, ratio);

		return new Color(ratio, ratio, ratio);
	}

}
