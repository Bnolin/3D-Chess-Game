
public class Move {
	private Square from;
	private Square to;
	
	public Move (Square s1, Square s2){
		from = s1;
		to = s2;
	}
	public Move (int x1, int y1, int x2, int y2){
		this(new Square(x1,y1), new Square(x2,y2));
	}
	public Square From(){return from;}
	public Square To(){return to;}
	
	public String toString(){
		return from + " " + to;
	}
	
	public int hashCode(){
		return from.hashCode()+64*to.hashCode();
	}
	public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;
        Move m = (Move)obj;
        return m.To().equals(to) && m.From().equals(from);
	}
	public Move clone(){
		return new Move(from,to);
	}
}
