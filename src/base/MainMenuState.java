package base;

import gameObjects.BackgroundEffects;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuState extends BasicGameState {

	private final int stateID;
	private final Camera camera = new Camera(0, 0);

	private BackgroundEffects background = new BackgroundEffects();

	public MainMenuState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		// gc.setTargetFrameRate(30);

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		background.render(camera, g);

		g.setColor(Color.white);

		g.drawString("Here, Fishy Fish Fish!", 400, 200);
		g.drawString("Hit SPACE or A/X on a controller to play", 320, 230);

		g.drawString("W", 350, 300);
		g.drawString("A S D", 333, 315);

		g.drawString("To move OR use a controller!", 400, 307);

		g.drawString("GET IN THE BUCKET!", 400, 370);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input input = container.getInput();

		if (input.isKeyPressed(Input.KEY_SPACE)) {
			game.enterState(FishGame.GAMEPLAY_STATE);
			FishGame.USING_CONTROLLER = false;
		} else if (input.isButton1Pressed(0)) {
			game.enterState(FishGame.GAMEPLAY_STATE);
			FishGame.USING_CONTROLLER = true;
		}

		background.update(delta, 0);
	}

	@Override
	public int getID() {
		return stateID;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		background = new BackgroundEffects();
		Input input = container.getInput();

		input.clearKeyPressedRecord();
		input.clearControlPressedRecord();
	}

}
