package game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
	
	public static void main(String[] args) throws SlickException {
		TheWild game = new TheWild();

		AppGameContainer container = new AppGameContainer(game);

		container.setDisplayMode(1000, 600, false);
		// container.setDisplayMode(1366, 768, true);

		container.start();
		
		return;
	}

}
