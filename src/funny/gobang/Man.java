package funny.gobang;

public class Man implements Player {

	ReadHelper helper = new ReadHelper();
	protected ChessBoard chessBoard;
	
	
	public String getName(){
		return this.getClass().getSimpleName();
	}
	
	@Override
	public Down thinkNextDown() {
		int capacity = chessBoard.getCapacity();
		int nextChess = ChessBoard.Black;
		String nextChessMan = nextChess == ChessBoard.Black ? "Black": "White";
		int[] down = null;
		while( (down = helper.readNextDown(nextChessMan)) != null){
			int x = down[0];
			int y = down[1];
			if (x < 0 || x >= capacity || y < 0 || y >= capacity){
				System.out.println("Out of index, X and Y should >=0 && < "+capacity);
				continue;
			}
			if (chessBoard.canDown(x, y)){
				return new Down(x,y,nextChess);
			}else{
				System.out.println(String.format("Current Psition %d,%d has been setted, please choose a blank position",x,y));
			}
		}
		
		return new Down(-1,-1,nextChess);
	}
	

	@Override
	public void register(ChessBoard board) {
		this.chessBoard = board;
	}


	@Override
	public void watchOpponentDown(Down down) {
		// TODO Auto-generated method stub
		
	}

}
