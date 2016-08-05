package funny.gobang;

public interface Player {

	public String getName();

	public void register(ChessBoard board);
	
	public void pickChessMan(int chessMan);
	
	public Down thinkNextDown(int round) throws InterruptedGameException;
	
	//public void interruptGame(Game game);
	
	//public void giveup(Game game);
	
	public void watchOpponentDown(Down down);
	
	public void exit();
	
	public RoundResult executeCommand(RoundCommand command) throws InterruptedGameException;
}
