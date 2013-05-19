package game.entities;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

public interface LightEmitting {
	public Color filterAt(Vector3f pos);
}
