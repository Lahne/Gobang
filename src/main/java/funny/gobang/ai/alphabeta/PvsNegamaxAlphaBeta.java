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

import java.nio.channels.InterruptedByTimeoutException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Negamax algorithm with alpha beta pruning
 * <p>
 * Created by charlie on 2016/9/27.
 */
@Service("PvsNegamaxAlphaBeta")
public class PvsNegamaxAlphaBeta implements GoBangAI {
    private static final Logger LOGGER = LoggerFactory.getLogger(PvsNegamaxAlphaBeta.class);

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
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<PointScore> future = es.submit(() -> {
            PointScore lastPs = null;
            for (int depth = 2; depth < 100; depth += 1) {
                try {
                    PointScore ps = negamax(board, boardHash, depth, -Long.MAX_VALUE, Long.MAX_VALUE, chessType);
                    LOGGER.info("depth:" + depth);
                    lastPs = ps;
                    if (lastPs.getScore() == Long.MAX_VALUE) {
                        break;
                    }
                } catch (InterruptedByTimeoutException e) {
                    break;
                }
            }
            return lastPs;
        });
        es.shutdown();

        PointScore ps;

        try {
            boolean terminated = es.awaitTermination(60, TimeUnit.SECONDS);
            if (!terminated) {
                es.shutdownNow();
            }
            ps = future.get();
            LOGGER.info(ps.toString());
            return ps.getPoint();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        return null;
    }

    public PointScore negamax(int[][] board, long boardHash, int depth, long alpha, long beta, int currentStone) throws InterruptedByTimeoutException {
        if (Thread.interrupted()) {
            throw new InterruptedByTimeoutException();
        }
        if (depth == 0) {
            long score = evaluationService.evaluate(board, currentStone);
            return new PointScore(score);
        }

        List<Point> points = boardService.getCandidatePoints(board, currentStone);
        points=sort(points, board, boardHash, currentStone);

        PointScore bestPs = new PointScore(Long.MIN_VALUE);
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (boardService.isWin(board, p, currentStone)) {
                bestPs.setScore(Long.MAX_VALUE);
                bestPs.addPoint(p);
                return bestPs;
            }
            board[p.getX()][p.getY()] = currentStone;

            long positionHash = ttService.getPositionHash(p.getX(), p.getY(), currentStone);
            boardHash ^= positionHash;

            PointScore ps;
            if (i == 0) {
                ps = negamax(board, boardHash, depth - 1, -beta, -alpha, -currentStone);
            } else {
                ps = negamax(board, boardHash, depth - 1, -alpha - 1, -alpha, -currentStone);
                if (-ps.getScore() > alpha && -ps.getScore() < beta) {
                    ps = negamax(board, boardHash, depth - 1, -beta, ps.getScore(), -currentStone);
                }
            }
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

        TTEntry entry = new TTEntry();
        entry.setKey(boardHash);
        entry.setDepth(depth);
        entry.setScore(bestPs.getScore());

        ttService.store(entry);

        return bestPs;
    }

    private List<Point> sort(List<Point> points, int[][] board, long boardHash, int currentStone) {
        Map<Point, Long> goodPointScores = new HashMap<>(points.size());
        Map<Point, Long> pointScores = new HashMap<>(points.size());

        for (Point p : points) {
            long positionHash = ttService.getPositionHash(p.getX(), p.getY(), currentStone);
            boardHash ^= positionHash;
            TTEntry entry = ttService.lookup(boardHash);
            if (entry != null) {
                goodPointScores.put(p, entry.getScore());
            } else {
                board[p.getX()][p.getY()] = currentStone;
                long score = -evaluationService.evaluate(board, -currentStone);
                board[p.getX()][p.getY()] = AppConstants.EMPTY;
                pointScores.put(p, score);
            }
        }

        List<Point> goodPoints = Arrays.asList(goodPointScores.keySet().toArray(new Point[0]));
        Collections.sort(goodPoints, (o1, o2) -> goodPointScores.get(o2).compareTo(goodPointScores.get(o1)));
        List<Point> normalPoints = Arrays.asList(pointScores.keySet().toArray(new Point[0]));
        Collections.sort(normalPoints, (o1, o2) -> pointScores.get(o2).compareTo(pointScores.get(o1)));

        List<Point> sortedPoints=new ArrayList<>(points.size());
        sortedPoints.addAll(goodPoints);
        sortedPoints.addAll(normalPoints);

        return sortedPoints;
    }


}
