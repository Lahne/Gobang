package funny.gobang.ai;

import funny.gobang.model.ChessBoard;
import funny.gobang.model.Point;

public interface GoBangAI {

	public Point getNext(ChessBoard board, int chessType);
	
}
