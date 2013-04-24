import java.util.Scanner;


public class HumanPlayerText2 extends Player {

	
    private Scanner moveReader = new Scanner(System.in);

	@Override
	public Move getMove(ChessBoard b) {
		b.displayBoard();
		b.bs = new BoardState(b);
		
		Color c = b.turn;
		String moveString = moveReader.next();
		Piece p;
		Square toSquare;
		Move m = null;
		

		if(Character.isUpperCase(moveString.charAt(0))){
			switch(moveString.charAt(0)){
			case 'B': if(c == Color.White){p = Piece.whiteBishop;}else{p = Piece.blackBishop;}break;
			case 'K': if(c == Color.White){p = Piece.whiteKing;}else{p = Piece.blackKing;}break;
			case 'N': if(c == Color.White){p = Piece.whiteKnight;}else{p = Piece.blackKnight;}break;
			case 'Q': if(c == Color.White){p = Piece.whiteQueen;}else{p = Piece.blackQueen;}break;
			case 'R': if(c == Color.White){p = Piece.whiteRook;}else{p = Piece.blackRook;}break;
			default: p = Piece.empty;
			}
			if(moveString.charAt(1) == 'x'){
				toSquare = new Square(moveString.charAt(2)-97,moveString.charAt(3)-49);
			} else {
				toSquare = new Square(moveString.charAt(1)-97,moveString.charAt(2)-49);
			}		
		} else {
			if(c == Color.White){p = Piece.whitePawn;} else { p = Piece.blackPawn;}
			if(moveString.charAt(1) == 'x'){
				toSquare = new Square(moveString.charAt(2)-97,moveString.charAt(3)-49);
			} else {
				toSquare = new Square(moveString.charAt(0)-97,moveString.charAt(1)-49);
			}
		}
		if(toSquare.Col()<0 || toSquare.Col()>7 || toSquare.Row()<0 || toSquare.Row()>7){
			return getMove(b);
		}
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(b.pieceAt(i,j) == p && b.bs.moves[i*8+j].contains(toSquare)){
					if(m == null){
						m = new Move(new Square(i,j),toSquare);
					} else {
						System.out.println("redundant move");
						m = null;
					}
				}
			}
		}
		System.out.println(toSquare);
		if(m == null){return getMove(b);}
		return m;
		
		
	}

	@Override
	public void hasMoved(Move m) {
		// TODO Auto-generated method stub

	}

}
