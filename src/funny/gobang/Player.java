package funny.gobang;

public interface Player {

	void register(ChessBoard board);
	
	int[] thinkNextDown();
	
	void watchOpponentDown(int x,int y);
}
