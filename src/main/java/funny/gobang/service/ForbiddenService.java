package funny.gobang.service;

import funny.gobang.model.ChessBoard;
import funny.gobang.model.Point;

public interface ForbiddenService {
	
  public boolean isForbiddenPoint(int[][] board,Point point);
	
  public boolean isForbiddenPoint(ChessBoard board,Point point);
	
}
