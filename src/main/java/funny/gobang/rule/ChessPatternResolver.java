package funny.gobang.rule;

import funny.gobang.model.ChessBoard;
import funny.gobang.model.Point;

public interface ChessPatternResolver {
	
	/**
	 * used to check the target point chess pattern at direction on board
	 * 
	 * @param board
	 * @param point
	 * @param direction
	 * @return
	 */
	public ChessPatterns resolveChessPattern(ChessBoard board, Point target, Point direction);
	
	
	public ChessPatterns assumeChessPattern(ChessBoard board, Point target, Point direction,boolean blackOrWithe);
	
	
	
}
