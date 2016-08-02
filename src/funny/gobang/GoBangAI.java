package funny.gobang;

import java.util.Random;

public class GoBangAI implements Player {

	protected ChessBoard board;
	
	
	
	@Override
	public int[] thinkNextDown() {
		Random rand = new Random();
		int x = -1;
		int y = -1;
		do{
			x = rand.nextInt(board.getCapacity());
			y = rand.nextInt(board.getCapacity());
		}while(! board.canDown(x, y));
		return new int[]{x,y};
	}

	@Override
	public void register(ChessBoard board) {
		this.board = board;
	}

	@Override
	public void watchOpponentDown(int x, int y) {
		// TODO Auto-generated method stub
		
	}
}
