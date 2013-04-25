
public enum Piece {
	empty,whitePawn,blackPawn,whiteRook,blackRook,whiteKnight,blackKnight,whiteBishop,blackBishop,whiteQueen,blackQueen,whiteKing,blackKing;
	
	public Color color(){
		switch (this){
			case whitePawn: case whiteRook: case whiteKnight: case whiteBishop: case whiteQueen: case whiteKing: return Color.White;
			case blackPawn: case blackRook: case blackKnight: case blackBishop: case blackQueen: case blackKing: return Color.Black;
		}
		return Color.NA;
	}
	public String toString(){
		switch (this){
		case whitePawn: return "wp";
		case whiteRook:  return "wR";
		case whiteKnight: return "wN";
		case whiteBishop: return "wB";
		case whiteQueen: return "wQ";
		case whiteKing: return "wK";
		case blackPawn: return "bp";
		case blackRook: return "bR";
		case blackKnight: return "bN";
		case blackBishop: return "bB";
		case blackQueen: return "bQ";
		case blackKing:  return "bK";
	}
		return "empty";
	}
	public int value() {
		switch (this){
		case whitePawn: case blackPawn: return 1;
		case whiteRook: case blackRook: return 5;
		case whiteKnight: case blackKnight: case whiteBishop: case blackBishop: return 3;
		case whiteQueen: case blackQueen: return 9;
		}
		return 0;
	}
	
}
