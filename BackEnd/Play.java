
public class Play {

	public static void main(String[] args) {
	//	Game g = new Game(new ComputerPlayer3(), new ComputerPlayer3());
	//	Game g = new Game(new HumanPlayerText(), new HumanPlayerText());
	//	Game g = new Game(new HumanPlayerText(), new ComputerPlayer2(4,5));
	//	Game g = new Game(new ComputerPlayer2(4,5), new ComputerPlayer4(6,4));
	//	Game g = new Game(new ComputerPlayer4(6,4), new ComputerPlayer2(4,1));
	//	Game g = new Game(new ComputerPlayer2(4,1), new ComputerPlayer2(4,1));
		Game g = new Game(new ComputerPlayer4(7,4), new ComputerPlayer4(7,4));
	//	Game g = new Game(new ComputerPlayer2(4,5), new HumanPlayerText());
	//	Game g = new Game(new HumanPlayerText(), new ComputerPlayer4(7,4));
	//	Game g = new Game(new ComputerPlayer4(7,4),new HumanPlayerText());
	//	Game g = new Game(new ComputerPlayerFinal(),new ComputerPlayerFinal());
		
		g.playGame();

	}

}
