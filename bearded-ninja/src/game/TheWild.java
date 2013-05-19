package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TheWild extends StateBasedGame {

	public static final int TITLE_STATE = 1;
	public static final int GAMEPLAY_STATE = 2;

	public TheWild() {
		super("The Wild");
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		this.addState(new GameplayState(GAMEPLAY_STATE));
		this.addState(new TitleState(TITLE_STATE));
	}

}
