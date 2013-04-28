package Backend;

import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Game {
	public ChessBoard b;
	public BoardState bs;
	public Color turn = Color.White;
	public Move currentMove;
	public Player player1; // White
	public Player player2; // Black
	public long player1Time = 0;
	public long player2Time = 0;
	
	
	public Game(Player p1, Player p2){
		b = new ChessBoard();
		bs = new BoardState(b);
		player1 = p1;
		player2 = p2;
	}
	
	public void turn(){
		Player p;
		if(turn == Color.White){p = player1;}
		else{p = player2;}
		long startTime = System.currentTimeMillis();
		boolean hasMoved = false;
		while(!hasMoved){
			currentMove = p.getMove(b.clone());
			if(!bs.hasMove(currentMove) || !(b.pieceAt(currentMove.From()).color() == turn) || !bs.moveType(currentMove).isValid()){
				System.out.println("Sorry Move Invalid");
			} else {
				ChessBoard backUpB = b.clone();
				b.move(currentMove); hasMoved = true;
				if(bs.inCheck(turn)){
					b.setBoard(backUpB);
					hasMoved = false;
				}
			}
		}
		if(bs.inCheck(turn.opposite())){System.out.println("Check");}
		long endTime = System.currentTimeMillis();
		while(endTime-startTime < 1000){endTime = System.currentTimeMillis();}
		if(p == player1){
			player1Time += endTime - startTime;
			System.out.println("player's total used time: " + toTime(player1Time));
		}
		else{
			player2Time += endTime - startTime;
			System.out.println("player's total used time: " + toTime(player2Time));
		}
		System.out.println("current move time: " + toTime(endTime-startTime));
		player1.hasMoved(currentMove);
		player2.hasMoved(currentMove);
		turn = turn.opposite();
	}
	public String toTime(long milsec){
			long min = TimeUnit.MILLISECONDS.toMinutes(milsec);
			milsec -= min*60000;
			long sec = TimeUnit.MILLISECONDS.toSeconds(milsec);
			milsec -= sec*1000;
			return "" + min + ":" + sec + "." + milsec;
	}
	public void displayGame(){
		for(int i = 0; i < b.turns.size(); i+=2){
			if(i+1 == b.turns.size()){System.out.println((i+2)/2 + ". " + b.turns.get(i).string + " | ");}
			else{System.out.println((i+2)/2 + ". " + b.turns.get(i).string + " | " + b.turns.get(i+1).string);}
		}
	}
	
	
	/*
	public static Boolean testBoardState(BoardState bs, ChessBoard b){
		ChessBoard testBoard = b.clone();
		BoardState testBS = new BoardState(testBoard);
		
		boolean continuity = true;
		for(int i = 0; i < 64; i++){
			if(!testBS.moves[i].equals(bs.moves[i])){
				continuity = false;
			}
		}
	
		return continuity;
		
		
	}
	*/
	
	
	
	
	public void playGame(){
		while (!bs.allMovesFor(turn).isEmpty() && !b.checkForRepeat()){
			turn();
		}
		if(bs.inCheck(turn)){
			int l = b.turns.size()-1;
			while(b.turns.get(l).string.endsWith(" ")){b.turns.get(l).string = b.turns.get(l).string.substring(0,b.turns.get(l).string.length()-1);}
			b.turns.get(l).string = b.turns.get(l).string.concat("+");
		}
		b.displayBoard();
		displayGame();
	}
	
	
}
