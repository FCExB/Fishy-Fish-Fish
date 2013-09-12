package entities;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import util.SpriteSheet;
import base.Camera;
import base.World;

public abstract class Entity implements Comparable<Entity> {

	protected Vector3f position;

	private final SpriteSheet animation;

	private int animationFrame;
	protected int spriteSheetRow;
	private int time;
	protected boolean animating = false;

	protected final int originalWidth;
	protected final int originalHeight;
	protected final int depth;
	private final float fixedScale;
	protected float changingScale = 1f;

	protected float rotationAngle = 0;
	protected boolean horizontalFlip = false;

	private final boolean solid;

	protected final World world;

	public Entity(SpriteSheet ss, boolean solid, int depth, float scale,
			Vector3f position, World world) {
		this.position = position;
		this.world = world;
		animation = ss;
		originalWidth = ss.getSpriteWidth();
		originalHeight = ss.getSpriteHeight();
		this.fixedScale = scale;
		this.depth = depth;
		this.solid = solid;
	}

	public Entity(Image image, boolean solid, int depth, float scale,
			Vector3f position, World world) {
		this(new SpriteSheet(image, image.getWidth(), image.getHeight()),
				solid, depth, scale, position, world);
	}

	public boolean isSolid() {
		return solid;
	}

	public int getHeight() {
		if (Math.abs(rotationAngle) < 50) {

			return originalHeight;
		}

		return originalWidth;
	}

	public int getWidth() {

		if (Math.abs(rotationAngle) > 50) {

			return originalHeight;
		}

		return originalWidth;
	}

	public int getDepth() {
		return depth;
	}

	public float greatestX() {
		return position.x + (getWidth() / 2) * fixedScale * changingScale;
	}

	public float greatestY() {
		return position.y + (getHeight()) * fixedScale * changingScale;
	}

	public float greatestZ() {
		return position.z + (depth / 2) * fixedScale * changingScale;
	}

	public float smallestX() {
		return position.x - +(getWidth() / 2) * fixedScale * changingScale;
	}

	public float smallestY() {
		return position.y;
	}

	public float smallestZ() {
		return position.z - (depth / 2) * fixedScale * changingScale;
	}

	protected int getAnimationFrame() {
		return animationFrame;
	}

	public Vector3f getPosition() {
		return new Vector3f(position);
	}

	public void render(Camera camera, Graphics g) {
		// if (camera.inRenderView(position)) {

		float zScaler = camera.zScaler();
		float otherScaler = camera.otherScaler();

		int x = (int) (Math.round(position.getX() - camera.getX()) + 500 - (originalWidth
				* fixedScale * changingScale) / 2);
		int y = Math.round((position.getZ() - camera.getY()) * zScaler + 300
				- originalHeight * fixedScale * changingScale * otherScaler
				- position.y * otherScaler);
		float xScale = 1;
		float yScale = otherScaler;

		Image image = animation.getSubImage(animationFrame, spriteSheetRow);

		image = image.getFlippedCopy(horizontalFlip, false);

		image.setRotation(rotationAngle);

		image.draw(x, y, originalWidth * fixedScale * changingScale * xScale,
				originalHeight * fixedScale * changingScale * yScale);

		// renderExtras(camera, g, filter);
		// }
	}

	/**
	 * Override when other details are to be rendered other than the entity
	 * 
	 * @param camera
	 * @param g
	 * @param filter
	 */
	public void renderExtras(Camera camera, Graphics g, Color filter) {
	}

	public void update(int deltaT, GameContainer gc) {
		act(deltaT, gc);

		time += deltaT;
		if (time >= 60 && animating) {
			animationFrame = (animationFrame + 1)
					% animation.getNumberOfSpritesWide();
			time = 0;
		}
	}

	protected abstract void act(int deltaT, GameContainer gc);

	public boolean collides(Entity that) {

		if (that != this && solid) {
			if (this.greatestX() > that.smallestX()
					&& this.greatestX() < that.greatestX()
					|| this.smallestX() > that.smallestX()
					&& this.smallestX() < that.greatestX()) {
				// There is an X overlap

				// if (this.greatestZ() > that.smallestZ()
				// && this.greatestZ() < that.greatestZ()
				// || this.smallestZ() > that.smallestZ()
				// && this.smallestZ() < that.greatestZ()) {
				// There is a Z overlap

				if (this.greatestY() > that.smallestY()
						&& this.greatestY() < that.greatestY()
						|| this.smallestY() > that.smallestY()
						&& this.smallestY() < that.greatestY()) {
					// There is a y overlap

					return true;
				}

				// }
			}
		}

		return false;
	}

	public abstract void hitBy(Entity entity);

	@Override
	public int compareTo(Entity that) {
		return new Float(position.z).compareTo(that.position.z);
	}
}
