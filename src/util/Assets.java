package util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;

public class Assets {

	public static Image FISH_ANIMATED;
	public static Image FISH_STILL;
	public static Image WATER_SKY_BACKGROUND;

	public Assets() throws SlickException {

		FISH_ANIMATED = new Image(
				ResourceLoader.getResourceAsStream("bin/data/fish sprite.png"),
				"bin/data/fish sprite.png", false);
		FISH_STILL = new Image(
				ResourceLoader.getResourceAsStream("bin/data/fishSmall.png"),
				"bin/data/fishSmall.png", false);

		WATER_SKY_BACKGROUND = new Image(
				ResourceLoader.getResourceAsStream("bin/data/waterSky.jpg"),
				"bin/data/waterSky.jpg", false);
	}
}
