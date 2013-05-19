package game.entities.stationary;

import game.entities.Entity;
import game.world.World;

import org.lwjgl.util.vector.Vector3f;

import util.Assets;

public class Rock extends Entity {

	public Rock(Vector3f position, World world) {
		super(Assets.ROCK, false, false, 16, position, world);
	}
}
