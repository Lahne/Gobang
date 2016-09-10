package funny.gobang.ai.alphabeta;

import funny.gobang.ai.AIUtils;
import funny.gobang.ai.GoBangAI;
import funny.gobang.model.ChessBoard;
import funny.gobang.model.Point;
import funny.gobang.service.BoardService;
import funny.gobang.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static funny.gobang.model.ChessType.*;

import java.util.LinkedList;
import java.util.List;

/**
 * AlphaBetaPrunningAI
 * 
 * @since 2016-8-20
 * @author Lahne
 *
 */
@Service
public class AlphaBetaPrunningAI implements GoBangAI{

	public static long MAX_SCORE = Long.MAX_VALUE;

	@Autowired
	protected BoardService boardService;
	@Autowired
    @Qualifier("evaluationService")
	protected EvaluationService evaluationService;

	@Value("${search.depth:4}")
	protected int searchDepth;
	

@Override
	public Point getNext(int[][] board, int chessType){
		
		ChessBoard chessBoard = new ChessBoard(board[0].length,board);
		
		return getNext(chessBoard, chessType);
	}
	
	public Point getNext(ChessBoard board, int chessType){
		List<Point> validPoints = generate(board, chessType);
		
		List<PointScore> estimatePointScoreList = new LinkedList<PointScore>();
		
		for (Point onePoint : validPoints){
			ChessBoard newChessBoard = board.clone();
			
			newChessBoard.downChess(onePoint, chessType);
			
			if (AIUtils.checkIfWin(newChessBoard, onePoint)){
				return onePoint;
			}
			
			long estimateScore = this.alphabeta(1, -MAX_SCORE, MAX_SCORE, newChessBoard, nextChessType(chessType) );
			
			estimatePointScoreList.add(new PointScore(onePoint,estimateScore));
		}
		
		return estimatePointScoreList.isEmpty() ? null : pickOne(estimatePointScoreList).getPoint();
		
	}
	
	public long evaluate(ChessBoard board, int chessType){
		
		return evaluationService.evaluate(board.getBoard(), chessType);
	}
	
	protected PointScore pickOne(List<PointScore> pointScoreList){
		PointScore maxPointScore = new PointScore(null, -MAX_SCORE);
		for (PointScore pointScore : pointScoreList){
			if (pointScore.getScore() >= maxPointScore.getScore()){
				maxPointScore = pointScore;
			}
		}
		return maxPointScore;
	}
	

	
	public List<Point> generate(ChessBoard board,int chessType){
		return boardService.getCandidatePoints(board.getBoard(),chessType);
	}
	

	
	protected int nextChessType(int chessType){
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
	protected long alphabeta(int depth, long alpha, long beta, ChessBoard board, int chessType){
		
		if (searchDepth == depth){
			return evaluate(board,chessType);
		}
		
		List<Point> avalidPoints = generate(board, chessType);
		
		boolean isMax = (depth & 1) == 0;
		
		for (Point point : avalidPoints){
			
			board.downChess(point, chessType);
			
			if (isMax){
				if (AIUtils.checkIfWin(board, point)){
					alpha = MAX_SCORE;
				}else{
					alpha = Math.max(alpha, this.alphabeta(depth +1,alpha, beta, board, nextChessType(chessType)));
				}
			}else{
				if (AIUtils.checkIfWin(board, point)){
					beta = -MAX_SCORE;
				}else{
					beta = Math.min(beta, this.alphabeta(depth +1, alpha, beta, board, nextChessType(chessType)));	
				}
			}
			
			board.remove(point);
			
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
