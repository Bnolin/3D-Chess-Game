
public abstract class Player {
	private Color color;
	
	public abstract Move getMove(ChessBoard b);
	public abstract void hasMoved(Move m);
	public Color Color(){return color;}
}
