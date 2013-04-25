
public enum MoveType {
	move,take,cover,listen,castle,promotion,notAMove, enPassant;

	public boolean isValid() {
		switch(this){
			case move: case take: case castle: case promotion: case enPassant: return true;
		}
		return false;
	}
}
