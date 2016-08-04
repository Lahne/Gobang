package funny.gobang;

public interface Player {

	public void register(ChessBoard board);
	
	public String getName();
	
	public Down thinkNextDown();
	
	//public void interruptGame(Game game);
	
	//public void giveup(Game game);
	
	public void watchOpponentDown(Down down);
	
	
}
