
public class Square {
	private int row,col;
	private final String letters = "abcdefgh";
	
	public Square(int col, int row){
		this.row = row;
		this.col = col;
	}
	
	public int Row(){
		return row;
	}
	public int Col(){
		return col;
	}
	public void Col(int i) {
		col+=i;
	}
	public void Row(int i) {
		row +=i;
	}
	public String toString(){
		return letters.substring(col, col+1) + (row+1);
	}
	public int hashCode(){
		return col * 8 + row;
	}
	public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;
        Square s = (Square)obj;
        return s.Row() == row && s.Col() == col;
	}
	public Square clone(){
		return new Square(col,row);
	}
}
