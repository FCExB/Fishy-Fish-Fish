package game.entities.stationary;

import game.entities.Entity;
import game.world.World;

import org.lwjgl.util.vector.Vector3f;

import util.Assets;

public class Flower extends Entity {
	public Flower(Vector3f position, World world) {
		super(Assets.MINECRAFT.getSubImage(13 * 16, 0, 16, 16), false, false,
				16, position, world);
	}
}
