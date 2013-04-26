package Display;

import Backend.*;

public class Playdisplay {

	public static void main(String[] args) {
		Game g = new Game(new ComputerPlayer4(7), new ComputerPlayer4(7));
		Display d = new Display(g);
		g.playGame();
	}

}
