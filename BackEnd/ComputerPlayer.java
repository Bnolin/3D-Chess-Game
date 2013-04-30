package Backend;
import java.util.ArrayList;


public class ComputerPlayer extends Player {
	
	public class Node{
		ChessBoard b;
		BoardState bs;
		Move m;
		int depth;
		float value;
		public Node(ChessBoard board,Move move,int depth){
			b = board;
			m = move;
			this.depth = depth;
			bs = new BoardState(b);
		}
		public float alphaBeta(Node n, float alpha, float beta){
			if(n.depth == 0){return evaluate(n);}

			ArrayList<Move> allMoves = new ArrayList<Move>();
			for(int col = 0; col < 8; col++){
				for(int row = 0; row < 8; row++){
					Square curSquare = new Square(col,row);
					if(n.b.pieceAt(curSquare).color() == n.b.turn){
						for(Square s: n.bs.moves[curSquare.hashCode()]){
							if(n.bs.moveType(new Move(curSquare,s)).isValid()){						
								Move m = new Move(curSquare,s);
								allMoves.add(m);
							}
						}
					}
				}
			}
			for(Move m: allMoves){
				ChessBoard b = n.b.clone();
				b.move(m);
				Node newNode = new Node(b,m, n.depth-1);

				if(!newNode.bs.inCheck(n.b.turn)){
					if(n.b.turn == Color.White){
						alpha = Math.max(alpha, alphaBeta(newNode,alpha,beta));
						if(beta <= alpha){return alpha;}
					} else {
						beta = Math.min(beta, alphaBeta(newNode,alpha,beta));
						if(beta <= alpha){return beta;}
					}

				}
			}
			if(n.b.turn == Color.White){return alpha;}
			else{return beta;}			
		}
		
		public float evaluate(Node n) {
			BoardState bs = n.bs;	
			
			if(bs == null){return 0;}
		
				ChessBoard b = bs.board;		
				///////////////////////////////////////////////		
				//TERRITORY CALCULATION && PIECE THREATENING
				int threatVal = 0;
				float territory = 0;
				int Wterr = 0;
				int Bterr = 0;
				int count = 0;
				for(int i = 0; i < 8; i++){
					for(int j = 0; j < 8; j++){
						Square square  = new Square(i,j);
						count = 0;
						for(Square s: bs.listeners[square.hashCode()]){	
							if(b.pieceAt(s).color() == Color.White){count++;}
							if(b.pieceAt(s).color() == Color.Black){count--;}
						}
						int iTemp = (Math.abs(2*i -7)-1)/2;
						int jTemp = (Math.abs(2*j -7)-1)/2;
		
						int squareVal = (int) Math.pow(2, 3-Math.max(iTemp, jTemp));
		
						if(count > 0){
							Wterr+=squareVal;
							if(b.pieceAt(square).color() == Color.Black){threatVal += b.pieceAt(square).value();}
						}
						if(count < 0){
							Bterr+=squareVal;
							if(b.pieceAt(square).color() == Color.White){threatVal -= b.pieceAt(square).value();}
						}
					}
				}
				territory = (float)Wterr/(float)(Wterr+Bterr);
				///////////////////////////////////////////////////////////////////		
				//PIECE VALUE CALCULATION
				float pieces;
				int Bpieces = 0;
				int Wpieces = 0;
		
				Square curSquare;
				for(int i = 0; i < 8; i++){
					for(int j = 0; j < 8; j++){
						curSquare = new Square(j,i);
						if(b.pieceAt(curSquare).color() == Color.White){Wpieces += b.pieceAt(curSquare).value();}
						if(b.pieceAt(curSquare).color() == Color.Black){Bpieces += b.pieceAt(curSquare).value();}
					}
				}
				Wpieces+=threatVal*2/3;
				pieces = (float)Wpieces/(float)(Math.max(1,Bpieces+Wpieces));
				/////////////////////////////////////////////////////////////////////
				//TOTAL CALCULATION
		
				float total = (territory + 5* pieces)/6;
				/////////////////////////////////////////////////////////////////////
				if(total == 1){return .999f;}
				if(total == 0){return .001f;}			
				return total;
		}
		
		public ArrayList<Node> generateChildren(){
			ArrayList<Move> allMoves = new ArrayList<Move>();
			ArrayList<Node> allChild = new ArrayList<Node>(); 

			for(int col = 0; col < 8; col++){
				for(int row = 0; row < 8; row++){
					Square curSquare = new Square(col,row);
					if(b.pieceAt(curSquare).color() == b.turn){
						for(Square s: bs.moves[curSquare.hashCode()]){
							if(bs.moveType(new Move(curSquare,s)).isValid()){						
								Move m = new Move(curSquare,s);
								if(bs.moveType(m) == MoveType.take){allMoves.add(0,m);}
								else{allMoves.add(m);}
							}
						}
					}
				}
			}
			for(Move m: allMoves){
				ChessBoard board = b.clone();
				board.move(m);
				allChild.add(new Node(board,m,depth-1));
			}
			return allChild;
		}
	}
	
	public int level;
	
	public ComputerPlayer(int level){
		this.level = level;
	}
	
	@Override
	public Move getMove(ChessBoard b) {
		b.displayBoard();

		Node root = new Node(b.clone(),null,level);
		Node bestN = null;
		for(Node n: root.generateChildren()){
			if(!n.bs.inCheck(n.b.turn.opposite())){
				n.value = n.alphaBeta(n, 0, 1);
				if(bestN == null){bestN = n;}
				else{
					if(bestN.value < n.value && n.b.turn.opposite() == Color.White){
						bestN = n;
					} else if(bestN.value > n.value && n.b.turn.opposite() == Color.Black){
						bestN = n;
					}
				}
			}
		}
		System.out.println(bestN.m);
		return bestN.m;
	}

	@Override
	public void hasMoved(Move m) {
		// TODO Auto-generated method stub

	}

}
