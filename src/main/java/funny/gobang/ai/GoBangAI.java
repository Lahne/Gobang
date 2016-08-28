package funny.gobang.ai;

import funny.gobang.model.ChessBoard;
import funny.gobang.model.Point;

public interface GoBangAI {

	Point getNext(int[][] board, int chessType);
	
}
