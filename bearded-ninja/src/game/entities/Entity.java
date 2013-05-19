package game.entities;

import game.Camera;
import game.entities.moving.Bullet;
import game.entities.moving.Player;
import game.entities.stationary.Explosion;
import game.world.World;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import util.Assets;
import util.SpriteSheet;

public abstract class Entity implements Comparable<Entity> {

	protected Vector3f position;

	private final SpriteSheet animation;

	private int animationFrame;
	protected int spriteSheetRow;
	private int time;
	protected boolean animating = false;

	protected final int width;
	protected final int height;
	protected final int depth;

	private final boolean hasShadow;
	private final boolean solid;

	protected final World world;

	public Entity(SpriteSheet ss, boolean hasShadow, boolean solid, int depth,
			Vector3f position, World world) {
		this.position = position;
		this.world = world;
		animation = ss;
		width = ss.getSpriteWidth();
		height = ss.getSpriteHeight();
		this.depth = depth;
		this.hasShadow = hasShadow;
		this.solid = solid;
	}

	public Entity(Image image, boolean hasShadow, boolean solid, int depth,
			Vector3f position, World world) {
		this(new SpriteSheet(image, image.getWidth(), image.getHeight()),
				hasShadow, solid, depth, position, world);
	}

	public boolean isSolid() {
		return solid;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getDepth() {
		return depth;
	}

	public float greatestX() {
		return position.x + width / 2;
	}

	public float greatestY() {
		return position.y + height;
	}

	public float greatestZ() {
		return position.z + depth / 2;
	}

	public float smallestX() {
		return position.x - width / 2;
	}

	public float smallestY() {
		return position.y;
	}

	public float smallestZ() {
		return position.z - depth / 2;
	}

	protected int getAnimationFrame() {
		return animationFrame;
	}

	public Vector3f getPosition() {
		return new Vector3f(position);
	}

	public void render(Camera camera, Graphics g) {
		if (camera.inRenderView(position)) {

			float zScaler = camera.zScaler();
			float otherScaler = camera.otherScaler();

			if (hasShadow) {
				int x = Math.round(position.getX() - camera.getX()) + 500
						- width / 2;

				int y = Math.round((position.getZ() - camera.getY()) * zScaler
						+ 300 - (depth * zScaler) / 2);

				float xScale = 1;
				float yScale = zScaler;

				// if (position.y > 0) {
				// x += Math.abs((width / 2) * (1 - 1 / (1 + position.y) * 2));
				// y += Math.abs(((depth * zScaler) / 2)
				// * (1 - 1 / (1 + position.y)) * 2);
				//
				// xScale *= Math.abs(1 / position.y) * 2;
				// zScaler *= Math.abs(1 / position.y) * 2;
				// }

				Assets.SHADOW.draw(x, y, width * xScale, depth * yScale);
			}

			Image image = animation.getSubImage(animationFrame, spriteSheetRow);

			int x = Math.round(position.getX() - camera.getX()) + 500 - width
					/ 2;
			int y = Math.round((position.getZ() - camera.getY()) * zScaler
					+ 300 - height * otherScaler - position.y * otherScaler);
			float xScale = 1;
			float yScale = otherScaler;

			Color filter = world.filterAtLocation(position);

			image.draw(x, y, width * xScale, height * yScale, filter);

			renderExtras(camera, g, filter);
		}
	}

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

	protected void act(int deltaT, GameContainer gc) {
	}

	public boolean collides(Entity that) {

		if (that != this && solid) {
			if (this.greatestX() > that.smallestX()
					&& this.greatestX() < that.greatestX()
					|| this.smallestX() > that.smallestX()
					&& this.smallestX() < that.greatestX()) {
				// There is an X overlap

				if (this.greatestZ() > that.smallestZ()
						&& this.greatestZ() < that.greatestZ()
						|| this.smallestZ() > that.smallestZ()
						&& this.smallestZ() < that.greatestZ()) {
					// There is a Z overlap

					if (this.greatestY() > that.smallestY()
							&& this.greatestY() < that.greatestY()
							|| this.smallestY() > that.smallestY()
							&& this.smallestY() < that.greatestY()) {
						// There is a y overlap

						return true;
					}

				}
			}
		}

		return false;
	}

	public void hitBy(Entity entity) {
		if (entity instanceof Bullet) {
			Vector3f explosionLocation = new Vector3f(entity.position.x,
					entity.position.y, position.z);

			world.removeEntity(entity);

			world.addEntity(new Explosion(explosionLocation, world));
		} else if (entity instanceof Player) {
			Player player = (Player) entity;
			player.jumping = false;
		}
	}

	@Override
	public int compareTo(Entity that) {
		return new Float(position.z).compareTo(that.position.z);
	}

}
