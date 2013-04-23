
public enum Color {
	Black,White,NA;

	public Color opposite() {
		if(this == Black){ return White;}
		else { return Black;}
	}
}
