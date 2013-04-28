package Display;

import Backend.*;

public class Playdisplay {

	public static void main(String[] args) {
		Game g = new Game(new ComputerPlayerFinal(5), new ComputerPlayerFinal(5));
		Display d = new Display(g);
		g.playGame();
	}

}
