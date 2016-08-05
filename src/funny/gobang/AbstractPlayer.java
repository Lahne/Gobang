package funny.gobang;

public abstract class AbstractPlayer implements Player {

	protected ChessBoard chessBoard;
	
	protected int pickedChessMan;
	
	@Override
	public void register(ChessBoard board) {
		this.chessBoard = board;
	}

	@Override
	public void pickChessMan(int chessMan) {
		this.pickedChessMan = chessMan;
	}

	public String getName(){
		String name = this.getClass().getSimpleName();
		if (pickedChessMan == ChessBoard.Black)
			return name + "-Black";
		if (pickedChessMan == ChessBoard.White)
			return name + "-White";
		return name;
	}
	
	@Override
	public void watchOpponentDown(Down down) {
		// TODO Auto-generated method stub
		
	}

}
