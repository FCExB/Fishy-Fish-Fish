package base;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameOverState extends BasicGameState {

	private final int stateID;

	public GameOverState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(Color.white);
		g.drawString("GAME OVER!!!", 400, 200);
		g.drawString("Score: " + GameplayState.score, 400, 230);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input input = container.getInput();

		if (input.isKeyPressed(Input.KEY_SPACE)) {
			game.enterState(FishGame.GAMEPLAY_STATE);
		}
	}

	@Override
	public int getID() {
		return stateID;
	}

}
