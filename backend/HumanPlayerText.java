import java.util.Scanner;


public class HumanPlayerText extends Player {

    private Scanner moveReader = new Scanner(System.in);

	
	
	
	@Override
	public Move getMove(ChessBoard b) {
		b.displayBoard();
		
		String moveNums;
		int x1,y1,x2,y2;
        moveNums = moveReader.next();
 //       if(moveNums < 0){
  //              System.exit(0);
   //     }
        x1 = moveNums.charAt(0)-97;
        y1 = moveNums.charAt(1)-49;
        x2 = moveNums.charAt(2)-97;
        y2 = moveNums.charAt(3)-49;
		return new Move(x1,y1,x2,y2);
	}




	@Override
	public void hasMoved(Move m) {
		
	}
}
