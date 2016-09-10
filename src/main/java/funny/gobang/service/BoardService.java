package funny.gobang.service;

import funny.gobang.AppUtil;
import funny.gobang.model.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static funny.gobang.AppConstants.*;

/**
 * Created by charlie on 2016/8/14.
 */
@Service
public class BoardService {
    @Autowired
    private ForbiddenService forbiddenService;

    public List<int[][]> generate(int[][] board, int currentStone) {
        List<Point> points = getCandidatePoints(board, currentStone);
        List<int[][]> boards = new ArrayList<>(points.size());
        for (Point p : points) {
            int[][] b = AppUtil.copyOf(board);
            b[p.getX()][p.getY()] = currentStone;
            boards.add(b);
        }
        return boards;
    }

    public List<Point> getCandidatePoints(int[][] board, int currentStone) {
        List<Point> points = new ArrayList<>();
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (board[x][y] == EMPTY && hasNeighbor(board, x, y)) {
                    points.add(new Point(x, y));
                }
            }
        }
        if (currentStone == BLACK) {
            List<Point> forbiddenPoints = new LinkedList<>();
            for (Point point : points) {
                if (forbiddenService.isForbiddenPoint(board, point)) {
                    forbiddenPoints.add(point);
                }
            }
            points.removeAll(forbiddenPoints);
        }
        return points;
    }

    private boolean hasNeighbor(int[][] board, int x, int y) {
        int startX = x < 2 ? 0 : x - 2;
        int endX = x >= BOARD_SIZE - 2 ? BOARD_SIZE - 1 : x + 2;
        int startY = y < 2 ? 0 : y - 2;
        int endY = y >= BOARD_SIZE - 2 ? BOARD_SIZE - 1 : y + 2;
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                if (i == x && j == y) {
                    continue;
                }
                if (board[i][j] != EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isWin(int[][] board, Point p, int currentStone) {
        int[][] dirs = {{0, 1}, {1, 0},
                {1, 1}, {-1, 1}};
        for (int[] dir : dirs) {
            int cnt = 1;
            for (int i = -1; i <= 1; i += 2) {
                int sx = p.getX() + i * dir[0];
                int sy = p.getY() + i * dir[1];
                while (sy < BOARD_SIZE && sy >= 0 && sx < BOARD_SIZE && sx >= 0 &&
                        board[sx][sy] == currentStone) {
                    cnt++;
                    if (cnt == 5) {
                        return true;
                    }
                    sx += i * dir[0];
                    sy += i * dir[1];
                }
            }
        }
        return false;
    }
}
