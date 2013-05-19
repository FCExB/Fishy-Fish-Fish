package game.entities.stationary;

import game.entities.Entity;
import game.world.World;

import org.lwjgl.util.vector.Vector3f;

import util.Assets;

public class Fence extends Entity {

	public Fence(Vector3f position, World world) {
		super(Assets.MINECRAFT.getSubImage(7 * 16, 0, 16, 16), false, true, 16,
				position, world);
	}

}
