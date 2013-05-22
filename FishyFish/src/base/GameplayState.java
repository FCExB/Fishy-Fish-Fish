package base;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import util.Assets;
import entities.Entity;
import gameObjects.Fish;
import gameObjects.ThreeDShape;

public class GameplayState extends BasicGameState {

	private final int stateID;

	private Camera camera = new Camera(500, 0);
	private World world;
	private Fish fish;

	public GameplayState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		new Assets();
		world = new World();
		fish = new Fish(new Vector3f(400, 0, 0), new Vector3f(), world);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		fish.update(delta, container);
		
		camera.update(container, delta);
		world.update(delta);

		actOnInput(container, game, delta);
	}

	private void actOnInput(GameContainer container, StateBasedGame game,
			int delta) {

		Input input = container.getInput();

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		g.setAntiAlias(true);

		world.render(camera, g, fish);

		renderUI(container, game, g);
	}

	private void renderUI(GameContainer container, StateBasedGame game,
			Graphics g) {
//		g.setColor(new Color(0, 0, 255, 100));
//		g.fill(new Rectangle(0, 200, 800, 400));
	}

	@Override
	public int getID() {
		return stateID;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
}
