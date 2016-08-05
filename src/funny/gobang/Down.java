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
	
	public boolean equal(Object obj){
		
		if (obj instanceof Down){
			Down c = (Down)obj;
			
			if (c.getX() == this.getX() && c.getY() == this.getY() && this.getChessMan() == c.getChessMan()){
				return true;
			}
			
		}
		
		return false;
	}
	
}
