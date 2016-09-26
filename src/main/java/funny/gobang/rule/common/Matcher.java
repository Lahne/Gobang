package funny.gobang.rule.common;

import funny.gobang.model.ChessBoard;
import funny.gobang.model.Point;
import funny.gobang.rule.ChessPatterns;

public class Matcher {

	public ChessBoard board;

	public ChessPatterns pattern;
	
	public Point delta;

	public Point target;

	public boolean isMatched = false;

	public boolean isBlackTarget = true;


	public Matcher(ChessBoard board, Point target, Point delta) {
		super();
		this.board = board;
		this.delta = delta;
		this.target = target;
	}
	
	public boolean markMatchedAndSetPattern(ChessPatterns pattern){
		this.isMatched = true;
		this.pattern = pattern;
		return true;
	}
	
	public boolean markFail(){
		this.isMatched = false;
		return false;
	}

	
}
