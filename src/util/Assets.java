package util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;

public class Assets {

	public static Image FISH_ANIMATED;
	public static Image FISH_STILL;
	public static Image WATER_SKY_BACKGROUND;

	public Assets() throws SlickException {

		// FISH_ANIMATED = new Image(
		// ResourceLoader
		// .getResourceAsStream("http://fcexb.com/test/data/fishSmall.png"),
		// "http://fcexb.com/test//data/fish sprite.png", false);

		FISH_STILL = new Image(
				ResourceLoader.getResourceAsStream("/data/fishSmall.png"),
				"/data/fishSmall.png", false);

		WATER_SKY_BACKGROUND = new Image(
				ResourceLoader.getResourceAsStream("/data/waterSky.jpg"),
				"/data/waterSky.jpg", false);
	}
}
