package funny.gobang.service;

import org.springframework.stereotype.Service;

/**
 * Created by charlie on 2016/8/14.
 */
@Service("evaluateMax")
public class EvaluationServiceMaxImpl implements EvaluationService {

    public long evaluate(int[][] board, int currentStone) {
        int[][] q = new int[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (board[i][j] != -1) {// 表示这个位置已经有点，不能下
                    board[i][j] = -1;
                } else {
                    // 对于一个点的权重计算
                    q[i][j] = findTargetValue(i, j, board);
                }
            }
        }
        int max = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (q[i][j] > max) {
                    max = q[i][j];
                }
            }
        }
        return max;
    }

    /**
     * 计算固定点的权重
     *
     * @param x
     * @param y
     * @param board
     * @return
     */
    public int findTargetValue(int x, int y, int[][] board) {
        int qz = 0;
        int w1 = 10000000;// 定义对于不同情况下，每个点要加上的权重
        int w2 = 50000;
        int w3 = 10000;
        int w4 = 5000;
        int w5 = 1000;
        int w6 = 500;
        int w7 = 100;
        int w8 = 50;
        int w9 = -100000000;
        int[] move = new int[4];
        boolean isBlank = true;//这里是先假定下一个颜色的棋子，然后确定下完之后所带来的改变
        board[x][y] = isBlank ? 0 : 1;
        move[0] = RuleService.x1(x, y, board);// 左右方向上的权重
        move[1] = RuleService.x2(x, y, board);// 上下方向上的权重
        move[2] = RuleService.x3(x, y, board);// 左高右低斜线的权重
        move[3] = RuleService.x4(x, y, board);// 做低有高斜线的权重
        // 如果点在中心位置，权重加1
        if (x == 7 && y == 7) {
            qz += 1;
        }
        for (int i = 0; i < 4; i++) {
            int positiveValue = move[i] < 0 ? -move[i] : move[i];
            if (positiveValue == 5) {
                // 如果出现五连
                qz += w1;
            } else if (move[i] == 4) {
                // 如果出现至少一端自由的四连
                qz += w3;
            } else if (move[i] == 3) {
                // 出现两端自由的三连
                qz += w5;
            } else if (move[i] == 2) {
                qz += w7;
            }
            if (isBlank) {
                if (RuleService.fails(move, board[x][y])) {
                    qz += w9;
                }
            }
        }
        // 此时计算人下在这里的影响，两次计算结果叠加在一起
        if (isBlank) {
            board[x][y] = 1;
        } else {
            board[x][y] = 0;
        }
        // 步骤与内容与上面相似
        move[0] = RuleService.x1(x, y, board);
        move[1] = RuleService.x2(x, y, board);
        move[2] = RuleService.x3(x, y, board);
        move[3] = RuleService.x4(x, y, board);

        for (int i = 0; i < 4; i++) {
            int positiveValue = move[i] < 0 ? -move[i] : move[i];
            if (positiveValue == 5) {
                qz += w2;
            } else if (move[i] == 4) {
                qz += w4;
            } else if (move[i] == 3) {
                qz += w6;
            } else if (move[i] == 2) {
                qz += w8;
            }
        }
        board[x][y] = -1;// 将位置恢复为无子状态
        return qz;
    }
}

