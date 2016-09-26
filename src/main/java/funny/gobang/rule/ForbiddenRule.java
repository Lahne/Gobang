package funny.gobang.rule;

import funny.gobang.model.ChessBoard;
import funny.gobang.model.Point;

public interface ForbiddenRule {
	
	public boolean isForbiddenPoint(ChessBoard board,Point point);
	
	public boolean isForbiddenPoint(ChessBoard board, Point point, Point[] directions);
	
}
