package funny.gobang;

public class Man implements Player {

	ReadHelper helper = new ReadHelper();
	protected ChessBoard chessBoard;
	
	
	@Override
	public int[] thinkNextDown() {
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
				return new int[]{x,y};
			}else{
				System.out.println(String.format("Current Psition %d,%d has been setted, please choose a blank position",x,y));
			}
		}
		return new int[]{-1,-1};
	}
	

	@Override
	public void register(ChessBoard board) {
		this.chessBoard = board;
	}


	@Override
	public void watchOpponentDown(int x, int y) {
		// TODO Auto-generated method stub
		
	}

}
