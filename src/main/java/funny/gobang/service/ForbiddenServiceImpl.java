package funny.gobang.service;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import funny.gobang.model.ChessBoard;
import funny.gobang.model.Point;
import funny.gobang.rule.ForbiddenRule;
import funny.gobang.rule.common.ForbiddenChessPatternResolverImpl;

@Service("forbiddenService")
public class ForbiddenServiceImpl implements ForbiddenService{

	ForbiddenRule forbiddenRule;
	
	@PostConstruct
	void initilize(){
		forbiddenRule = new ForbiddenChessPatternResolverImpl();
	}
	
	@Override
	public boolean isForbiddenPoint(int[][] board, Point point) {
		ChessBoard chessBoard = new ChessBoard(board[0].length, board);
		return isForbiddenPoint(chessBoard, point);
	}

	@Override
	public boolean isForbiddenPoint(ChessBoard board, Point point) {
		return forbiddenRule.isForbiddenPoint(board, point);
	}

	
}
