package game.entities.stationary;

import game.entities.Entity;
import game.world.World;

import org.lwjgl.util.vector.Vector3f;

import util.Assets;

public class Tree extends Entity {

	public Tree(Vector3f position, World world) {
		super(Assets.TREE, true, true, 16, position, world);
	}

	@Override
	public void hitBy(Entity entity) {
		super.hitBy(entity);
	}
}
