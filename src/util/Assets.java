package util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Assets {

	public static Image PLAYER_FISH;
	public static Image AI_FISH;

	public Assets() throws SlickException {

		PLAYER_FISH = new Image("data/fishSmall.png");
		AI_FISH = new Image("data/enemy.png");
	}
}
