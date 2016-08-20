package funny.gobang.ai;

import funny.gobang.model.ChessBoard;

public interface GoBangEvaluator {

	long evaluate(ChessBoard board,int chessType);
	
}
