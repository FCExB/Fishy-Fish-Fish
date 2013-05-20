package util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Assets {

	public static Image FISH_ANIMATED;
	public static Image FISH_STILL;

	public Assets() throws SlickException {
		FISH_ANIMATED = new Image("bin/data/fish sprite.png");
		FISH_STILL = new Image("bin/data/cartoon-fish-whales-picture.gif");
	}
}
