package game;

import game.entities.Entity;
import game.entities.moving.Creature;
import game.entities.moving.NPC;
import game.entities.moving.Player;
import game.entities.stationary.Fence;
import game.entities.stationary.Torch;
import game.world.Point;
import game.world.Tile;
import game.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import util.Assets;
import util.Attributes;

public class GameplayState extends BasicGameState {

	private final int stateID;

	private static final int WORLD_WIDTH = 100;
	private static final int WORLD_HEIGHT = 100;

	Player player;
	Camera camera;
	World world;

	List<Entity> entitiesInView = new LinkedList<Entity>();
	List<Entity> entitiesNotInView = new LinkedList<Entity>();

	SortedMap<Point, Tile> tiles = new TreeMap<Point, Tile>();

	private int timeSinceSearch;

	private int currentSelection;

	public GameplayState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		new Assets();

		entitiesInView.clear();
		entitiesNotInView.clear();
		tiles.clear();

		camera = new Camera(500, 300, 1032, 632);

		world = new World(WORLD_WIDTH, WORLD_HEIGHT, entitiesInView, tiles,
				camera);
		player = new Player(0, 0, world, game);

		if (!world.addPlayer(player, new Vector3f(0, 0, 0))) {
			init(gc, game);
			return;
		}

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		actOnInput(container, game, delta);

		camera.update(container, player, delta);
		camera.addTilesInUpdateView(world);

		world.update(delta);

		timeSinceSearch += delta;

		for (Entity e : entitiesInView) {
			if (camera.inUpdateView(e.getPosition())) {
				e.update(delta, container);
			} else {
				world.removeEntity(e);
				entitiesNotInView.add(e);
			}
		}

		List<Entity> nowInView = new ArrayList<Entity>();

		if (timeSinceSearch >= 800) {
			for (Entity e : entitiesNotInView) {
				if (camera.inUpdateView(e.getPosition()) && world.addEntity(e)) {
					nowInView.add(e);
					e.update(delta, container);
				}
			}

			timeSinceSearch = 0;
		}

		world.addAllEntities();

		world.removeAllEntities();

		entitiesNotInView.removeAll(nowInView);
	}

	private void actOnInput(GameContainer container, StateBasedGame game,
			int delta) {

		Input input = container.getInput();

		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			game.enterState(TheWild.TITLE_STATE);
		}

		if (input.isKeyDown(Input.KEY_1)) {
			currentSelection = 0;
		} else if (input.isKeyDown(Input.KEY_2)) {
			currentSelection = 1;
		}
		if (input.isKeyDown(Input.KEY_3)) {
			currentSelection = 2;
		}
		if (input.isKeyDown(Input.KEY_4)) {
			currentSelection = 3;
		}
		if (input.isKeyDown(Input.KEY_5)) {
			currentSelection = 4;
		}

		int x = Mouse.getX();
		int z = 600 - Mouse.getY();
		x = x + camera.getX() - 500;
		z = Math.round((z - 300) / camera.zScaler() + camera.getY());

		Vector3f mousePoint = new Vector3f(x, 0, z);

		if (Mouse.isButtonDown(0)) {
			switch (currentSelection) {
			case 0:
				player.shootAt(mousePoint);
				break;
			case 1:
				world.addEntity(new Creature(new Attributes(), mousePoint,
						world));
				break;
			case 2:
				world.addEntity(new Fence(mousePoint, world));
				break;
			case 3:
				world.addEntity(new Torch(mousePoint, world));
				break;
			case 4:
				world.addEntity(new NPC(mousePoint, world));
				break;
			}
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		world.render(camera);

		Collections.sort(entitiesInView);

		for (Entity e : entitiesInView) {
			e.render(camera, g);
		}

		renderUI(container, game, g);
	}

	private void renderUI(GameContainer container, StateBasedGame game,
			Graphics g) {

		g.drawRect(340, 568, 320, 32);
		Assets.BULLET.draw(340, 568, 32, 16);
		Assets.CREATURE.draw(372, 568, 32, 32);
		Assets.FENCE.draw(404, 568, 32, 32);

		g.drawRect(340 + 32 * currentSelection, 568, 32, 32);

	}

	@Override
	public int getID() {
		return stateID;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		init(container, game);
	}
}
