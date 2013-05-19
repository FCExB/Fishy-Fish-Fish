package game.world;

import org.newdawn.slick.Image;

public class Tile {

	private Image image;

	public Tile(Image image) {
		this.image = image;
	}

	public Image getImage() {
		return image;
	}

	protected void setImage(Image newImage) {
		image = newImage;
	}
}
