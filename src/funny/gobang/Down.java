package funny.gobang;

public class Down {

	public final int X;
	public final int Y;
	public final int chessMan;
	
	
	public Down(int x, int y, int chessMan) {
		super();
		X = x;
		Y = y;
		this.chessMan = chessMan;
	}
	
	public int getX(){
		return X;
	}
	
	public int getY(){
		return Y;
	}
	
	public int getChessMan(){
		return chessMan;
	}
	
}
