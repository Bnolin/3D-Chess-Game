import java.util.ArrayList;
import java.util.Collection;

public class BoardState {
	ChessBoard board;
	ArrayList<Square>[] moves = (ArrayList<Square>[]) new ArrayList[64];
	ArrayList<Square>[] listeners = (ArrayList<Square>[]) new ArrayList[64];
	
	public BoardState(ChessBoard board){		
		this.board = board;
		this.board.setBoardState(this);
		generateState();
	}

	public void generateState() {
		for(int square = 0; square < 64; square++){
			moves[square] = new ArrayList<Square>();
			listeners[square] = new ArrayList<Square>();
		}
		
		for(int col = 0; col < 8; col++){
			for(int row = 0; row < 8; row++){
				Square s = new Square(row,col);
				moves[s.hashCode()] = getMovesFor(s);
	//			System.out.println(list);
				for(Square square: moves[s.hashCode()]){
					listeners[square.hashCode()].add(s);
				}
			}
		}

	if(board.BKCastle || board.BQCastle){		
		for(Square s: moves[board.BKing.hashCode()]){
			listeners[s.hashCode()].remove(board.BKing);
		}	
		moves[board.BKing.hashCode()] = getMovesFor(board.BKing);
		for(Square square: moves[board.BKing.hashCode()]){
			listeners[square.hashCode()].add(board.BKing);
		}
	}
	if(board.WKCastle || board.WQCastle){	
		for(Square s: moves[board.WKing.hashCode()]){
			listeners[s.hashCode()].remove(board.WKing);
		}	
		moves[board.WKing.hashCode()] = getMovesFor(board.WKing);
		for(Square square: moves[board.WKing.hashCode()]){
			listeners[square.hashCode()].add(board.WKing);
		}		
	}
		
		
	}
	private boolean isOnBoard(Square s){
		if(s.Row() >= 0 && s.Row() < 8 && s.Col() >= 0 && s.Col() < 8){return true;}
		return false;
	}
	private boolean isOnBoard(int row, int col) {
		if(row >= 0 && row < 8 && col >= 0 && col < 8){return true;}
		return false;
	}
	boolean isThreatenedBy(Square s, Color c){
		for(Square square: listeners[s.hashCode()]){
			if(c == board.pieceAt(square).color()){
				return true;
			}
		}
		return false;
	}
	private boolean isThreatenedBy(int col, int row, Color c) {
		for(Square square: listeners[col*8+row]){
			if(c == board.pieceAt(square).color()){
				return true;
			}
		}
		return false;
	}
	
	private ArrayList<Square> genKing(Square s) {
		int row = s.Row();
		int col = s.Col();
		ArrayList<Square> list = new ArrayList<Square>();
		if(isOnBoard(col-1,row-1)){list.add(new Square(col-1,row-1));}
		if(isOnBoard(col  ,row-1)){list.add(new Square(col  ,row-1));}
		if(isOnBoard(col+1,row-1)){list.add(new Square(col+1,row-1));}
		if(isOnBoard(col-1,row  )){list.add(new Square(col-1,row  ));}
		if(isOnBoard(col+1,row  )){list.add(new Square(col+1,row  ));}
		if(isOnBoard(col-1,row+1)){list.add(new Square(col-1,row+1));}
		if(isOnBoard(col  ,row+1)){list.add(new Square(col  ,row+1));}
		if(isOnBoard(col+1,row+1)){list.add(new Square(col+1,row+1));}
		
		//castling
		if(!inCheck(board.pieceAt(col,row).color())){
		
			if(board.canCastle(board.pieceAt(s).color(),Piece.blackKing)){
				if(		board.pieceAt(col+1,row) == Piece.empty && 
						board.pieceAt(col+2,row) == Piece.empty &&
						!isThreatenedBy(col+1,row,board.pieceAt(s).color().opposite()) &&
						!isThreatenedBy(col+2,row,board.pieceAt(s).color().opposite())){
					list.add(new Square(col+2,row));
				}
			}
			if(board.canCastle(board.pieceAt(s).color(),Piece.blackQueen)){
				if(		board.pieceAt(col-1,row) == Piece.empty && 
						board.pieceAt(col-2,row) == Piece.empty && 
						!isThreatenedBy(col-1,row,board.pieceAt(s).color().opposite()) &&
						!isThreatenedBy(col-2,row,board.pieceAt(s).color().opposite())){
					list.add(new Square(col-2,row));
				}
			}
		}
			return list;
	}
	
	private ArrayList<Square> genQueen(Square s) {
		ArrayList<Square> list = new ArrayList<Square>();
		Square square = new Square(s.Col(),s.Row());
		
		//right
		square.Col(1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Col(1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		square = new Square(s.Col(),s.Row());
	
		//left
		square.Col(-1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Col(-1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		square = new Square(s.Col(),s.Row());
		
		//up
		square.Row(1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Row(1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		square = new Square(s.Col(),s.Row());

		//down
		square.Row(-1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Row(-1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		square = new Square(s.Col(),s.Row());

		//up-right
		square.Col(1);
		square.Row(1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Row(1);
			square.Col(1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		square = new Square(s.Col(),s.Row());

		
		//up-left
		square.Col(-1);
		square.Row(1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Row(1);
			square.Col(-1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		square = new Square(s.Col(),s.Row());

		
		//down-right
		square.Col(1);
		square.Row(-1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Row(-1);
			square.Col(1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		square = new Square(s.Col(),s.Row());

		
		//down-left
		square.Col(-1);
		square.Row(-1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Row(-1);
			square.Col(-1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		
		
		return list;
	}

	private ArrayList<Square> genBishop(Square s) {
		ArrayList<Square> list = new ArrayList<Square>();
		Square square = new Square(s.Col(),s.Row());
		
		//up-right
		square.Col(1);
		square.Row(1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Row(1);
			square.Col(1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		square = new Square(s.Col(),s.Row());

		
		//up-left
		square.Col(-1);
		square.Row(1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Row(1);
			square.Col(-1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		square = new Square(s.Col(),s.Row());

		
		//down-right
		square.Col(1);
		square.Row(-1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Row(-1);
			square.Col(1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		square = new Square(s.Col(),s.Row());

		
		//down-left
		square.Col(-1);
		square.Row(-1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Row(-1);
			square.Col(-1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		
		
		return list;
	}

	private ArrayList<Square> genKnight(Square s) {
		int row = s.Row();
		int col = s.Col();
		ArrayList<Square> list = new ArrayList<Square>();
		if(isOnBoard(col-2,row-1)){list.add(new Square(col-2,row-1));}
		if(isOnBoard(col+2,row-1)){list.add(new Square(col+2,row-1));}
		if(isOnBoard(col+1,row-2)){list.add(new Square(col+1,row-2));}
		if(isOnBoard(col-1,row-2)){list.add(new Square(col-1,row-2));}
		if(isOnBoard(col+1,row+2)){list.add(new Square(col+1,row+2));}
		if(isOnBoard(col-1,row+2)){list.add(new Square(col-1,row+2));}
		if(isOnBoard(col+2,row+1)){list.add(new Square(col+2,row+1));}
		if(isOnBoard(col-2,row+1)){list.add(new Square(col-2,row+1));}
		
		return list;
	}

	private ArrayList<Square> genRook(Square s) {
		ArrayList<Square> list = new ArrayList<Square>();
		Square square = new Square(s.Col(),s.Row());
		
		//right
		square.Col(1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Col(1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		square = new Square(s.Col(),s.Row());
	
		//left
		square.Col(-1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Col(-1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		square = new Square(s.Col(),s.Row());
		
		//up
		square.Row(1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Row(1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		square = new Square(s.Col(),s.Row());

		//down
		square.Row(-1);
		while(isOnBoard(square) && board.pieceAt(square) == Piece.empty){
			list.add(square.clone());
			square.Row(-1);
		}
		if(isOnBoard(square)){list.add(square.clone());}
		
		return list;
	}
	
	private ArrayList<Square> genBPawn(Square s) {
		int row = s.Row();
		int col = s.Col();
		ArrayList<Square> list = new ArrayList<Square>();
		if(isOnBoard(col,row-1) && board.pieceAt(col,row-1) == Piece.empty){list.add(new Square(col,row-1));}
		if(isOnBoard(col+1,row-1)/* && board.pieceAt(new Square(col+1,row-1)).color() == Color.White*/){list.add(new Square(col+1,row-1));}
		if(isOnBoard(col-1,row-1)/* && board.pieceAt(new Square(col-1,row-1)).color() == Color.White*/){list.add(new Square(col-1,row-1));}
		if(row == 6 && board.pieceAt(col,row-1) == Piece.empty && board.pieceAt(col,row-2) == Piece.empty){list.add(new Square(col,row-2));}
		return list;
	}

	private ArrayList<Square> getWPawn(Square s) {
		int row = s.Row();
		int col = s.Col();
		ArrayList<Square> list = new ArrayList<Square>();
		if(isOnBoard(col,row+1) && board.pieceAt(col,row+1) == Piece.empty){list.add(new Square(col,row+1));}
		if(isOnBoard(col+1,row+1)/* && board.pieceAt(new Square(col+1,row+1)).color() == Color.Black*/){list.add(new Square(col+1,row+1));}
		if(isOnBoard(col-1,row+1)/* && board.pieceAt(new Square(col-1,row+1)).color() == Color.Black*/){list.add(new Square(col-1,row+1));}
		if(row == 1 && board.pieceAt(col,row+1) == Piece.empty && board.pieceAt(col,row+2) == Piece.empty){list.add(new Square(col,row+2));}
		return list;
	}

	public boolean hasMove(Move m) {
		return moves[m.From().hashCode()].contains(m.To());
	}

	public boolean inCheck(Color c) {
		for(Square s: listeners[board.King(c).hashCode()]){
			if(board.pieceAt(s).color() != c && moveType(new Move(s,board.King(c))).isValid()){return true;}
		}
		return false;
	}
	
	public 	ArrayList<Move> allMovesFor(Color c){
		
		ArrayList<Move> allMoves = new ArrayList<Move>();
		for(int col = 0; col < 8; col++){
			for(int row = 0; row < 8; row++){
				Square curSquare = new Square(col,row);
				if(board.pieceAt(curSquare).color() == c){
					for(Square s: moves[curSquare.hashCode()]){
						if(moveType(new Move(curSquare,s)).isValid()){						
							Move m = new Move(curSquare,s);
							allMoves.add(m);
						}
					}
				}
			}
		}
		ArrayList<Move> toRemove = new ArrayList<Move>();
		for(Move m: allMoves){
			ChessBoard b = board.clone();
			board.move(m);
			if(inCheck(board.turn.opposite())){toRemove.add(m);}
			board.setBoard(b);
		}
		allMoves.removeAll(toRemove);
		return allMoves;
	}
	
	private ArrayList<Square> getMovesFor(Square s){
		switch (board.pieceAt(s)){
		case whitePawn:
			return getWPawn(s);
		case blackPawn:
			return genBPawn(s);
		case whiteRook: case blackRook:
			return genRook(s);
		case whiteKnight: case blackKnight:
			return genKnight(s);
		case whiteBishop: case blackBishop:
			return genBishop(s);
		case whiteQueen: case blackQueen:
			return genQueen(s);
		case whiteKing: case blackKing:
			return genKing(s);
		case empty:
			return new ArrayList<Square>();
	}
		return null;
	}

	public void update(Square...squares){
		generateState();
	}
	public MoveType moveType(Move m){
		
		//castle
		if(board.pieceAt(m.From()) == Piece.blackKing || board.pieceAt(m.From()) == Piece.whiteKing){
			if(Math.abs(m.To().Col()-m.From().Col()) == 2){return MoveType.castle;}
		}
		//Pawn listeners
		if(board.pieceAt(m.From()) == Piece.blackPawn || board.pieceAt(m.From()) == Piece.whitePawn){
			if(m.To().Row() == 0 || m.To().Row() == 7){return MoveType.promotion;}
			if((m.From().Col() == m.To().Col()) && board.pieceAt(m.To()) != Piece.empty){	return MoveType.listen;}
			if(m.From().Col() != m.To().Col()){
				if(board.pieceAt(m.To()) == Piece.empty){
					if((m.From().Row() == 3 && board.pieceAt(m.From()).color() == Color.Black) || m.From().Row() == 4 && board.pieceAt(m.From()).color() == Color.White){
						Square s1 = new Square(m.To().Col(),m.To().Row()+1);
						Square s2 = new Square(m.To().Col(),m.To().Row()-1);
						if(board.turn.opposite() == Color.White && board.lastMove.equals(new Move(s2,s1)) && board.pieceAt(s1) == Piece.whitePawn){
					//		System.out.println("enpassant");
							return MoveType.enPassant;
						} else
							if(board.turn.opposite() == Color.Black && board.lastMove.equals(new Move(s1,s2)) && board.pieceAt(s2) == Piece.blackPawn){
					//			System.out.println("enpassant");
								return MoveType.enPassant;
							}
					}
					return MoveType.listen;
				}
			}
		}
		
		if(board.pieceAt(m.To()) == Piece.empty){return MoveType.move;}
		if(board.pieceAt(m.To()).color() == board.pieceAt(m.From()).color()){ return MoveType.cover;}
		if(board.pieceAt(m.To()).color() != board.pieceAt(m.From()).color()){ return MoveType.take;}
		
		return MoveType.notAMove;

	}
	
	public void displayBS(){
		Square s;
		for(int col = 0; col < 8; col++){
			for(int row = 0; row < 8; row++){
				s = new Square(col,row);
				System.out.println(s + ":  " + moves[s.hashCode()]);
			}
		}
//		for(int col = 0; col < 8; col++){
//			for(int row = 0; row < 8; row++){
//				s = new Square(col,row);
//				System.out.println(s + ":  " + listeners[s.hashCode()]);
//			}
//		}
	}

}
