package Backend;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HumanPlayerText3 extends Player {

    String moveString = null;

	@Override
	public synchronized Move getMove(ChessBoard b) {

		while(moveString == null){try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		
		b.displayBoard();
		b.bs = new BoardState(b);
		
		Color c = b.turn;
		Piece p;
		Square toSquare;
		Move m = null;		
		
		Pattern p1 = Pattern.compile("[a-h][1-8]");
		Pattern p2 = Pattern.compile("[a-h][x][a-h][1-8]");
		Pattern p3 = Pattern.compile("[RNBQK][a-h][1-8]");
		Pattern p4 = Pattern.compile("[RNBQK][x][a-h][1-8]");
		Pattern p5 = Pattern.compile("0-0-0");
		Pattern p6 = Pattern.compile("0-0");
		Matcher m1 = p1.matcher(moveString);
		Matcher m2 = p2.matcher(moveString);
		Matcher m3 = p3.matcher(moveString);
		Matcher m4 = p4.matcher(moveString);
		Matcher m5 = p5.matcher(moveString);
		Matcher m6 = p6.matcher(moveString);
		
///CASTLING
		if(m5.matches()){
			int row;
			if(c == Color.White){row = 0;}
			else{row = 7;}
			return new Move(new Square(4,row),new Square(2,row));
		}
		if(m6.matches()){
			int row;
			if(c == Color.White){row = 0;}
			else{row = 7;}
			return new Move(new Square(4,row),new Square(6,row));
		}
		
///FIND PIECE TYPE		
		if(m1.matches() || m2.matches()){
			if(c == Color.White){p = Piece.whitePawn;} else { p = Piece.blackPawn;}
		} else if(m3.matches() || m4.matches()){
			switch(moveString.charAt(0)){
			case 'B': if(c == Color.White){p = Piece.whiteBishop;}else{p = Piece.blackBishop;}break;
			case 'K': if(c == Color.White){p = Piece.whiteKing;}else{p = Piece.blackKing;}break;
			case 'N': if(c == Color.White){p = Piece.whiteKnight;}else{p = Piece.blackKnight;}break;
			case 'Q': if(c == Color.White){p = Piece.whiteQueen;}else{p = Piece.blackQueen;}break;
			case 'R': if(c == Color.White){p = Piece.whiteRook;}else{p = Piece.blackRook;}break;
			default: p = Piece.empty;
			}
		} else {
			System.out.println("not valid syntax");
			return getMove(b);
		}
		
///FIND DESTINATION SQUARE		
		if(m2.matches() || m4.matches()){
			toSquare = new Square(moveString.charAt(2)-97,moveString.charAt(3)-49);
		} else {
			if(m1.matches()){
				toSquare = new Square(moveString.charAt(0)-97,moveString.charAt(1)-49);
			} else {
				toSquare = new Square(moveString.charAt(1)-97,moveString.charAt(2)-49);
			}
		}
		
///FIND ORIGIN
		if(m1.matches() || m2.matches()){
			int col = moveString.charAt(0)-97;
			for(int i = 0; i < 8; i++){
				if(b.pieceAt(col,i) == p && b.bs.moves[col*8+i].contains(toSquare)){
					m = new Move(new Square(col,i),toSquare);
				}
			}
		} else if(m3.matches() || m4.matches()){
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					if(b.pieceAt(i,j) == p && b.bs.moves[i*8+j].contains(toSquare)){
						m = new Move(new Square(i,j),toSquare);
					}
				}
			}
		}
		if(m == null){System.out.println("move not found");return getMove(b);}
		moveString = null;
		return m;
	}

	@Override
	public void hasMoved(Move m) {
		// TODO Auto-generated method stub

	}

	public synchronized void input(String textInput) {
		moveString = textInput;
		notify();
	}

}
