package base;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import util.Assets;

public class GameplayState extends BasicGameState {

	private final int stateID;

	private final Camera camera = new Camera(500, 0);
	private World world;

	public static int score = 0;
	private int timeRan = 0;

	private final int timeLimit = 5 * 1000;

	public GameplayState(int stateID) throws SlickException {
		this.stateID = stateID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		new Assets();
		world = new World(this);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		timeRan += delta;

		if (timeRan > timeLimit) {
			game.enterState(FishGame.GAMEOVER_STATE);
		}

		world.update(container, delta);

		actOnInput(container, game);
	}

	private void actOnInput(GameContainer container, StateBasedGame game) {

		Input input = container.getInput();

		if (input.isKeyPressed(Input.KEY_R)) {
			world.reset();
		} else if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			game.enterState(FishGame.MAIN_MENU_STATE);
		}

	}

	public void fishLandsInBucket() {
		score++;
		world.reset();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		g.setAntiAlias(true);

		world.render(camera, g);

		renderUI(container, game, g);
	}

	private void renderUI(GameContainer container, StateBasedGame game,
			Graphics g) {
		g.setColor(Color.black);
		g.drawString("Score: " + Integer.toString(score), 400, 10);

		int timeLeft = timeLimit - timeRan;
		int seconds = Math.max(timeLeft / 1000, 0);
		int milliseconds = Math.max(timeLeft % 1000, 0);

		g.drawString("Time left: " + seconds + "." + milliseconds, 400, 30);
	}

	@Override
	public int getID() {
		return stateID;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		score = 0;
		timeRan = 0;
		world.reset();
	}
}
