import java.util.ArrayList;

public class ChessBoard{

	private Piece[][] board = new Piece[8][8];
	public Move lastMove;
	public BoardState bs;
	public Square WKing,BKing;
	public ArrayList<Move> turns;
	
	public boolean WQCastle = true,
					WKCastle = true,
					BQCastle = true,
					BKCastle = true;
	public Color turn;
	
	public ChessBoard(){
		turns = new ArrayList<Move>();
		for(int col = 0; col < 8; col++){
			for(int row = 0; row < 8; row++){
				board[row][col] = Piece.empty;
			}
		}
/*		board[0][0] = Piece.whiteRook;
		board[1][0] = Piece.whiteKnight;
		board[2][0] = Piece.whiteBishop;
		board[3][0] = Piece.whiteQueen;
		board[4][0] = Piece.whiteKing;
		board[5][0] = Piece.whiteBishop;
		board[6][0] = Piece.whiteKnight;
		board[7][0] = Piece.whiteRook;
		
		board[0][7] = Piece.blackRook;
		board[1][7] = Piece.blackKnight;
		board[2][7] = Piece.blackBishop;
		board[3][7] = Piece.blackQueen;
		board[4][7] = Piece.blackKing;
		board[5][7] = Piece.blackBishop;
		board[6][7] = Piece.blackKnight;
		board[7][7] = Piece.blackRook;
		
		for(int i=0; i < 8; i++){
			board[i][1] = Piece.whitePawn;
			board[i][6] = Piece.blackPawn;
		}
		WKing = new Square(4,0);
		BKing = new Square(4,7);
*/
		
		board[0][0] = Piece.whiteKing;
		WKing = new Square(0,0);
		board[7][7] = Piece.blackKing;
		BKing = new Square(7,7);
		
		board[2][3] = Piece.whitePawn;
		
		BKCastle = false;
		BQCastle = false;
		WKCastle = false;
		WQCastle = false;
		
		
		
		turn = Color.White;
		lastMove = new Move(new Square(0,0),new Square(0,0));
	}
	
	public void setBoardState(BoardState bs){
		this.bs = bs;
	}
	
	public Piece pieceAt(Square square){
		return board[square.Col()][square.Row()];
	}
	public Piece pieceAt(int col,int row){
		return board[col][row];
	}
	
	public void move(Move m){
		m.string = "";
		String s = "abcdefgh";
		if(bs != null){
			if(bs.moveType(m) == MoveType.castle){
				if(m.To().Col() == 2){m.string = "0-0-0";}
				else { m.string = "0-0";}
			} else
			if(bs.moveType(m) == MoveType.take || bs.moveType(m) == MoveType.enPassant){
				switch(pieceAt(m.From())){
				case blackPawn:case whitePawn: m.string = m.string.concat(s.substring(m.From().Col(),m.From().Col()+1));break;
				case blackRook:case whiteRook: m.string = m.string.concat("R");break;
				case blackKnight:case whiteKnight:m.string = m.string.concat("N");break;
				case blackBishop:case whiteBishop:m.string = m.string.concat("B");break;
				case blackQueen:case whiteQueen:m.string = m.string.concat("Q");break;
				case blackKing:case whiteKing:m.string = m.string.concat("K");break; 
				}
				m.string = m.string.concat("x" + m.To());
			} else
			if(bs.moveType(m) == MoveType.move){
				switch(pieceAt(m.From())){
				case blackRook:case whiteRook: m.string = m.string.concat("R");break;
				case blackKnight:case whiteKnight:m.string = m.string.concat("N");break;
				case blackBishop:case whiteBishop:m.string = m.string.concat("B");break;
				case blackQueen:case whiteQueen:m.string = m.string.concat("Q");break;
				case blackKing:case whiteKing:m.string = m.string.concat("K");break;
				}
				m.string = m.string.concat(m.To().toString());
			} else
			if(bs.moveType(m) == MoveType.promotion){
				m.string = m.To().toString() + "=Q";
			}
		}
		
		if((m.From().equals(BKing) || m.From().equals(WKing)) && (Math.abs(m.From().Col() - m.To().Col()) == 2)){
			Move newM;
			if(m.From().Col() > m.To().Col()){ // QueenSide
				newM = new Move(new Square(0,m.From().Row()),new Square(3,m.From().Row()));
			} else {  // KingSide
				newM = new Move(new Square(7,m.From().Row()),new Square(5,m.From().Row()));
			}
			move(newM);
			turns.remove(newM);
			turn = turn.opposite();
		}
	if((m.From().Row() == 3 && pieceAt(m.From()).color() == Color.Black) || m.From().Row() == 4 && pieceAt(m.From()).color() == Color.White){
			Square s1 = new Square(m.To().Col(),m.To().Row()+1);
			Square s2 = new Square(m.To().Col(),m.To().Row()-1);
			if(turn.opposite() == Color.White && lastMove.equals(new Move(s2,s1)) && pieceAt(s1) == Piece.whitePawn){
				board[m.To().Col()][m.From().Row()] = Piece.empty;
			} else
				if(turn.opposite() == Color.Black && lastMove.equals(new Move(s1,s2)) && pieceAt(s2) == Piece.blackPawn){
					board[m.To().Col()][m.From().Row()] = Piece.empty;	
				}
		}
		if(m.From().equals(new Square(0,0))){WQCastle = false;}
		if(m.From().equals(new Square(7,0))){WKCastle = false;}
		if(m.From().equals(new Square(0,7))){BQCastle = false;}
		if(m.From().equals(new Square(7,7))){BKCastle = false;}
		if(m.From().equals(WKing)){WKing = m.To(); WKCastle = false; WQCastle = false;}
		if(m.From().equals(BKing)){BKing = m.To(); BKCastle = false; BQCastle = false;}

		if((pieceAt(m.From()) == Piece.blackPawn) && m.To().Row() == 0){
			putPiece(Piece.blackQueen,m.To());
		} else if((pieceAt(m.From()) == Piece.whitePawn) && m.To().Row() == 7){
				putPiece(Piece.whiteQueen,m.To());
		} else {
			board[m.To().Col()][m.To().Row()] = board[m.From().Col()][m.From().Row()];
		}
		board[m.From().Col()][m.From().Row()] = Piece.empty;
		lastMove = m;
		if(bs != null){bs.update(m.To(), m.From());}
		if(bs != null && bs.inCheck(turn.opposite())){m.string = m.string.concat("+");}
		for(int i = m.string.length(); i < 5; i++){
			m.string = m.string.concat(" ");
		}
		turns.add(lastMove);
		turn = turn.opposite();
	}
	
	public void setBoard(ChessBoard b){
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				this.board[i][j] = b.board[i][j];
			}
		}
		WQCastle = b.WQCastle;
		WKCastle = b.WKCastle;
		BQCastle = b.BQCastle;
		BKCastle = b.BKCastle;
		lastMove = b.lastMove.clone();
		WKing = b.WKing.clone();
		BKing = b.BKing.clone();
		turn = b.turn;
		turns = b.turns;
		bs.generateState();
	}
	
	public String toString(){
        StringBuilder sb = new StringBuilder();
        Piece current;
        Square square = new Square(0,0);
        for(int i=7;i>=0;i--){
                sb.append(i+1 + "| ");
                for(int j=0;j<8;j++){
                        square = new Square(j,i);
                        current = pieceAt(square);
                       if(current == Piece.empty){
                    	   sb.append("-- ");
                       } else {
                           sb.append(current+" ");
                        }
                }
                sb.append("\n");
        }
        sb.append("--------------------------\n");
        sb.append("    a  b  c  d  e  f  g  h\n");
        return sb.toString();
	}
	
	public boolean canCastle(Color c, Piece side){
		if(c == Color.Black){
			if(side == Piece.blackKing){return BKCastle;}
			if(side == Piece.blackQueen){return BQCastle;}
		}
		if(c == Color.White){
			if(side == Piece.blackKing){return WKCastle;}
			if(side == Piece.blackQueen){return WQCastle;}
		}
		return false;
	}

	public void displayBoard(){
		System.out.print(this);
	}

	public Square King(Color c) {
		if(c == Color.White){return WKing;}
		else{return BKing;}		
	}
	
	public void putPiece(Piece p, Square s){
		board[s.Col()][s.Row()] = p;
	}
	
	public ChessBoard clone(){
		ChessBoard b = new ChessBoard();
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				b.board[i][j] = this.board[i][j];
			}
		}
		b.WQCastle = WQCastle;
		b.WKCastle = WKCastle;
		b.BQCastle = BQCastle;
		b.BKCastle = BKCastle;
		b.lastMove = lastMove.clone();
		b.WKing = WKing.clone();
		b.BKing = BKing.clone();
		b.turn = turn;
		b.turns = new ArrayList<Move>();
		for(Move m:turns){b.turns.add(m);}
		return b;
	}
	public boolean checkForRepeat(){
		if(turns.size() < 11){return false;}		
		int i = turns.size() -1;
		return (turns.get(i).equals(turns.get(i-4))
			&& turns.get(i-1).equals(turns.get(i-5))
			&& turns.get(i-2).equals(turns.get(i-6))
			&& turns.get(i-3).equals(turns.get(i-7))
			&& turns.get(i-4).equals(turns.get(i-8))
			&& turns.get(i-5).equals(turns.get(i-9))
			&& turns.get(i-6).equals(turns.get(i-10)));
	}
}
