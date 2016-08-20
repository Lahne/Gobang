package funny.gobang.ai.alphabeta;

import funny.gobang.ai.GoBangAI;
import funny.gobang.ai.GoBangEvaluator;
import funny.gobang.model.ChessBoard;
import funny.gobang.model.Point;
import static funny.gobang.model.ChessType.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * AlphaBetaPrunningAI
 * 
 * @since 2016-8-20
 * @author Lahne
 *
 */
public class AlphaBetaPrunningAI implements GoBangAI,GoBangEvaluator{

	private static long MAX_SCORE = Long.MAX_VALUE;
	
	private static final int MIN_DEPTH = 1;

    /**
     * the depth of search level.
     */
	private final int searchDepth;
	
	/**
	 * evaluate current situation
	 */
	private GoBangEvaluator evaluator;
	

	public AlphaBetaPrunningAI(int searchDepth) {
		super();
		this.searchDepth = ( searchDepth < MIN_DEPTH) ? MIN_DEPTH : searchDepth;
	}
	

	public AlphaBetaPrunningAI(int searchDepth, GoBangEvaluator evaluator) {
		super();
		this.searchDepth = searchDepth;
		this.evaluator = evaluator;
	}
	
	public GoBangEvaluator getEvaluator() {
		return evaluator;
	}



	public void setEvaluator(GoBangEvaluator evaluator) {
		this.evaluator = evaluator;
	}


	public Point getNext(int[][] board, int chessType){
		
		ChessBoard chessBoard = new ChessBoard(board[0].length,board);
		
		return getNext(chessBoard, chessType);
	}
	
	public Point getNext(ChessBoard board, int chessType){
		
		List<Point> validPoints = generate(board, chessType);

		List<PointScore> estimatePointScoreList = new LinkedList<>();
		
		for (Point onePoint : validPoints){
			ChessBoard newChessBoard = board.clone();
			
			newChessBoard.downChess(onePoint, chessType);
			
			if (checkIfWin(newChessBoard, chessType)){
				return onePoint;
			}
			
			long estimateScore = this.alphabeta(1, -MAX_SCORE, MAX_SCORE, newChessBoard, nextChessType(chessType) );
			
			estimatePointScoreList.add(new PointScore(onePoint,estimateScore));
		}
		
		return estimatePointScoreList.isEmpty() ? null : pickOne(estimatePointScoreList).getPoint();
		
	}
	
	@Override
	public long evaluate(ChessBoard board, int chessType){
		
		return evaluator.evaluate(board, chessType);
	}
	
	
	protected boolean checkIfWin(ChessBoard board, int chessType){
		
		return false;
	}
	
	private PointScore pickOne(List<PointScore> pointScoreList){
		PointScore maxPointScore = new PointScore(null, -MAX_SCORE);
		for (PointScore pointScore : pointScoreList){
			if (pointScore.getScore() >= maxPointScore.getScore()){
				maxPointScore = pointScore;
			}
		}
		return maxPointScore;
	}
	

	
	public List<Point> generate(ChessBoard board,int chessType){
		
		
		return Collections.emptyList();
	}
	

	
	private int nextChessType(int chessType){
		return (chessType == BLACK) ? WHITE : BLACK;
	}
	
	
	/**
	 * depth % 2 == 0 means max layer.
	 * depth % 2 == 1 means min layer.
	 * @param depth
	 * @param alpha
	 * @param beta
	 * @param board
	 * @param chessType
	 * @return
	 */
	private long alphabeta(int depth, long alpha, long beta, ChessBoard board, int chessType){
		
		if (searchDepth == depth){
			return evaluate(board,chessType);
		}
		
		List<Point> avalidPoints = generate(board, chessType);
		
		boolean isMax = (depth & 1) == 0;
		
		for (Point point : avalidPoints){
			
			board.downChess(point, chessType);
			
			if (isMax){
				if (checkIfWin(board, chessType)){
					alpha = MAX_SCORE;
				}else{
					alpha = Math.max(alpha, this.alphabeta(depth +1,alpha, beta, board, nextChessType(chessType)));
				}
			}else{
				if (checkIfWin(board, chessType)){
					beta = -MAX_SCORE;
				}else{
					beta = Math.min(beta, this.alphabeta(depth +1, alpha, beta, board, nextChessType(chessType)));	
				}
			}
			
			board.downChess(point, EMPTY);
			
			if (beta <= alpha){
				break;
			}
		}
		
		return isMax ? alpha : beta;
	}
	
	
	
	
	class PointScore {
		
		private Point point;
		private long score;
		
		public PointScore(Point point) {
			this(point,0);
		}
		
		public PointScore(Point point, long score) {
			super();
			this.point = point;
			this.score = score;
		}
		
		public Point getPoint() {
			return point;
		}
		public void setPoint(Point point) {
			this.point = point;
		}
		public long getScore() {
			return score;
		}
		public void setScore(long score) {
			this.score = score;
		}

	}
	
	
}
