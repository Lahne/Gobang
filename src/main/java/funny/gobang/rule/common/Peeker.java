package funny.gobang.rule.common;

import funny.gobang.model.ChessBoard;
import funny.gobang.model.ChessType;
import funny.gobang.model.Point;

public class Peeker {

    Point delta;

	ChessBoard board;

	Point startPoint = null;// exclude

	public Point endPoint = null;

	public int count = 0;

	int type;

	boolean left;
	
	public Peeker() {

	}

	public Peeker(ChessBoard board, Point delta) {
		this.delta = delta;
		this.board = board;
	}

	public Point getDelta() {
		return delta;
	}

	public void setDelta(Point delta) {
		this.delta = delta;
	}

	public void clear() {
		type = ChessType.BOUNDARY;
		count = 0;
		endPoint = null;
		startPoint = null;
	}

	public void peekFromEnd(boolean left) {
		peek(left, endPoint);
	}

	public void peek(boolean left, Point excludeStartPoint) {
		peek(left, excludeStartPoint, 1);
	}

	private boolean peek(boolean left, Point excludeStartPoint, int expectedCount) {
		int cap = board.getCapacity();
		int dltX = left ? -delta.getX() : delta.getX();
		int dltY = left ? -delta.getY() : delta.getY();

		clear();

		startPoint = excludeStartPoint;

		if (excludeStartPoint.getX() == cap - 1 || excludeStartPoint.getY() == cap - 1
				|| excludeStartPoint.getX() == 0 || excludeStartPoint.getY() == 0) {
			return false;
		}

		boolean needCheckExpectedCount = expectedCount > 0 ? true : false;

		int x = excludeStartPoint.getX() + dltX;
		int y = excludeStartPoint.getY() + dltY;

		type = board.getChessType(x, y);

		for (; x < cap && x >= 0 && y < cap && y >= 0; x += dltX, y += dltY) {
			int chessType = board.getChessType(x, y);
			if (type != chessType) {
				break;
			}

			count++;
			endPoint = new Point(x, y);
			type = chessType;

			if (needCheckExpectedCount && expectedCount == count) {
				return true;
			}

		}

		if (needCheckExpectedCount && expectedCount != count) {
			clear();
			return false;
		}
		return true;

	}

	private boolean peek(boolean left, Point excludeStartPoint, int expectedCount, int expectedType) {
		int cap = board.getCapacity();
		int dltX = left ? -delta.getX() : delta.getX();
		int dltY = left ? -delta.getY() : delta.getY();

		clear();

		type = expectedType;
		startPoint = excludeStartPoint;
		endPoint = excludeStartPoint;
		
		if (excludeStartPoint.getX() == cap - 1 || excludeStartPoint.getY() == cap - 1
				|| excludeStartPoint.getX() == 0 || excludeStartPoint.getY() == 0) {
			
			return false;
		}

		boolean needCheckExpectedCount = expectedCount > 0 ? true : false;

		for (int x = excludeStartPoint.getX() + dltX, y = excludeStartPoint.getY() + dltY; x < cap && x >= 0
				&& y < cap && y >= 0; x += dltX, y += dltY) {
			int chessType = board.getChessType(x, y);
			if (chessType != expectedType) {
				break;
			}
			count++;
			endPoint = new Point(x, y);

			if (needCheckExpectedCount && expectedCount == count) {
				return true;
			}

		}

		if (needCheckExpectedCount && expectedCount != count) {
			clear();
			return false;
		}

		return true;

	}

	public int peekOneFromEnd(boolean left){
		peekFromEnd(left);
		return type;
	}
	
	public int peekOneFromEnd(){
		return peekOneFromEnd(left);
	}
	
	public boolean peekSpecific(boolean left, Point excludeStartPoint, int expectedType) {
		return peek(left, excludeStartPoint, -1, expectedType);
	}

	public boolean peekSpecificCount(boolean left, Point excludeStartPoint, int expectedType, int expectedCount) {
		return peek(left, excludeStartPoint, expectedCount, expectedType);
	}
}

