package util;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Assets {

	public static Image FISH_ANIMATED;
	public static Image FISH_STILL;
	public static Image FISH_STILL2;
	public static Image WATER_SKY_BACKGROUND;

	public Assets() throws SlickException {

		// FISH_ANIMATED = new Image(
		// ResourceLoader
		// .getResourceAsStream("http://fcexb.com/test/data/fishSmall.png"),
		// "http://fcexb.com/test//data/fish sprite.png", false);

		FISH_STILL = new Image("data/fishSmall.png");
		FISH_STILL2 = new Image("data/ocean_1.png").getSubImage(1, 32 * 3, 32,
				32);

		WATER_SKY_BACKGROUND = new Image("data/waterSky.jpg");
	}
}
