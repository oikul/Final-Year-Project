package engine;
import java.awt.Dimension;
import java.awt.Toolkit;

import game.Game;

public class Main {

	public static void main(String args[]) {
		try {
			boolean vSync = true;
			IGameLogic gameLogic = new Game();
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			GameEngine gameEng = new GameEngine("GAME", screen.width, screen.height, vSync, gameLogic);
			gameEng.start();
		} catch (Exception excp) {
			excp.printStackTrace();
			System.exit(-1);
		}
	}
}
