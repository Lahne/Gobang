package funny.gobang.service;

/**
 * Created by charlie on 2016/8/14.
 */
public interface EvaluationService {

    long evaluate(int[][] board, int currentStone);
}

