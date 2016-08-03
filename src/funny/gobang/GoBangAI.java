package funny.gobang;

import java.util.Random;

public class GoBangAI implements Player {

	protected ChessBoard board;
	
	public String getName(){
		return this.getClass().getSimpleName();
	}
	
	@Override
	public Down thinkNextDown() {
		Random rand = new Random();
		int x = -1;
		int y = -1;
		do{
			x = rand.nextInt(board.getCapacity());
			y = rand.nextInt(board.getCapacity());
		}while(! board.canDown(x, y));
		return new Down(x,y,ChessBoard.White);
	}

	@Override
	public void register(ChessBoard board) {
		this.board = board;
	}

	@Override
	public void watchOpponentDown(Down down) {
		// TODO Auto-generated method stub
		
	}
}
