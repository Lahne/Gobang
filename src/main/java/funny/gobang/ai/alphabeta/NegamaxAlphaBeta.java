package funny.gobang.ai.alphabeta;

import funny.gobang.AppConstants;
import funny.gobang.ai.GoBangAI;
import funny.gobang.model.Point;
import funny.gobang.model.PointScore;
import funny.gobang.model.TTEntry;
import funny.gobang.service.BoardService;
import funny.gobang.service.EvaluationService;
import funny.gobang.service.TTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Negamax algorithm with alpha beta pruning
 * <p>
 * Created by charlie on 2016/9/27.
 */
@Service("NegamaxAlphaBeta")
public class NegamaxAlphaBeta implements GoBangAI {
    private static final Logger LOGGER = LoggerFactory.getLogger(NegamaxAlphaBeta.class);

    @Autowired
//    @Qualifier("evaluationService")
    @Qualifier("evaluateTuple")
    private EvaluationService evaluationService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private TTService ttService;

    @Value("${search.depth:4}")
    protected int searchDepth;

    @Override
    public Point getNext(int[][] board, int chessType) {
        long boardHash = ttService.getBoardHash(board);
        PointScore ps = negamax(board, boardHash, searchDepth, -Long.MAX_VALUE, Long.MAX_VALUE, chessType);
        LOGGER.info(ps.toString());
        return ps.getPoint();
    }

    public PointScore negamax(int[][] board, long boardHash, int depth, long alpha, long beta, int currentStone) {
        long origAlpha = alpha;
        TTEntry entry = ttService.lookup(boardHash);
        if (entry != null && entry.getDepth() >= depth) {
            if (TTEntry.ScoreType.EXACT.equals(entry.getType())) {
                PointScore ps=new PointScore(entry.getScore());
                ps.addPoint(entry.getPoint());
                return ps;
            } else if (TTEntry.ScoreType.LOWER_BOUND.equals(entry.getType())) {
                alpha = Math.max(alpha, entry.getScore());
            } else {
                beta = Math.min(beta, entry.getScore());
            }
            if (beta <= alpha) {
                PointScore ps=new PointScore(entry.getScore());
                ps.addPoint(entry.getPoint());
                return ps;
            }
        }

        if (depth == 0) {
            long score = evaluationService.evaluate(board, currentStone);
            return new PointScore(score);
        }

        List<Point> points = boardService.getCandidatePoints(board, currentStone);
        sort(points, board, currentStone);

        PointScore bestPs = new PointScore(Long.MIN_VALUE);
        for (Point p : points) {
            if (boardService.isWin(board, p, currentStone)) {
                bestPs.setScore(Long.MAX_VALUE);
                bestPs.addPoint(p);
                return bestPs;
            }
            board[p.getX()][p.getY()] = currentStone;

            long positionHash = ttService.getPositionHash(p.getX(), p.getY(), currentStone);
            boardHash ^= positionHash;

            PointScore ps = negamax(board, boardHash, depth - 1, -beta, -alpha, -currentStone);
            ps.setScore(-ps.getScore());

            board[p.getX()][p.getY()] = AppConstants.EMPTY;
            boardHash ^= positionHash;

            if (ps.getScore() > bestPs.getScore()) {
                bestPs = ps;
                bestPs.addPoint(p);
            }
            alpha = Math.max(alpha, ps.getScore());
            if (beta <= alpha) {
                break;
            }
        }

        entry = new TTEntry();
        entry.setKey(boardHash);
        entry.setDepth(depth);
        entry.setScore(bestPs.getScore());
        entry.setPoint(bestPs.getPoint());
        if (bestPs.getScore() <= origAlpha) {
            entry.setType(TTEntry.ScoreType.UPPER_BOUND);
        } else if (bestPs.getScore() >= beta) {
            entry.setType(TTEntry.ScoreType.LOWER_BOUND);
        } else {
            entry.setType(TTEntry.ScoreType.EXACT);
        }

        ttService.store(entry);

        return bestPs;
    }

    private void sort(List<Point> points, int[][] board, int currentStone) {
        Map<Point, Long> pointScores = new HashMap<>(points.size());

        for (Point p : points) {
            board[p.getX()][p.getY()] = currentStone;
            long score = -evaluationService.evaluate(board, -currentStone);
            board[p.getX()][p.getY()] = AppConstants.EMPTY;
            pointScores.put(p, score);

        }

        Collections.sort(points, (o1, o2) -> pointScores.get(o2).compareTo(pointScores.get(o1)));
    }


}
