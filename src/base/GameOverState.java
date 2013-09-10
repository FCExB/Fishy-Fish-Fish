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
		game.getState(FishGame.GAMEPLAY_STATE).render(container, game, g);

		g.setColor(Color.yellow);

		// Font font = new Font("Arial", Font.BOLD, 20);
		// g.setFont(new UnicodeFont(font, 12, false, false));

		g.drawString("GAME OVER!!!", 300, 400);
		g.drawString("Score: " + GameplayState.score, 300, 420);

		g.drawString("Press SPACE for instant restart", 300, 460);

		g.drawString("Or press ESCAPE for main menu", 300, 480);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input input = container.getInput();

		if (input.isKeyPressed(Input.KEY_SPACE)) {
			game.enterState(FishGame.GAMEPLAY_STATE);
		} else if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			game.enterState(FishGame.MAIN_MENU_STATE);
		}
	}

	@Override
	public int getID() {
		return stateID;
	}

}