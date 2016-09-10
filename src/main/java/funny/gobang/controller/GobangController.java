package funny.gobang.controller;

import funny.gobang.AppConstants;
import funny.gobang.AppUtil;
import funny.gobang.model.AiResponse;
import funny.gobang.model.Point;
import funny.gobang.service.AiService;
import funny.gobang.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static funny.gobang.AppConstants.*;

/**
 * Created by charlie on 2016/8/14.
 */
@RestController
public class GobangController {
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    private List<Point> moves = new ArrayList<Point>(BOARD_SIZE * BOARD_SIZE);
    private int aiStone;

    @Autowired
    private AiService aiService;

    @Autowired
    private BoardService boardService;

    @RequestMapping("/init/{x}/{y}")
    public void init(@PathVariable int x, @PathVariable int y) {
        board[x][y] = moves.size() % 2 == 0 ? BLACK : WHITE;
        Point point = new Point(x, y);
        moves.add(point);
    }

    @RequestMapping("/start/{stone}")
    public AiResponse start(@PathVariable int stone) {
        aiStone = stone;
        boolean blackMove = moves.size() % 2 == 0;
        if (aiStone == BLACK && blackMove || aiStone == WHITE && !blackMove) {
            int[][] copyOfBoard = AppUtil.copyOf(board);
            AiResponse aiResponse = aiService.play(copyOfBoard, aiStone);
            board[aiResponse.getPoint().getX()][aiResponse.getPoint().getY()] = aiStone;
            moves.add(aiResponse.getPoint());
            return aiResponse;
        }
        return null;
    }

    @RequestMapping("/play/{x}/{y}")
    public AiResponse play(@PathVariable int x, @PathVariable int y) {
        board[x][y] = moves.size() % 2 == 0 ? BLACK : WHITE;
        Point point = new Point(x, y);
        moves.add(point);
        //TODO check if human move is win
        int[][] copyOfBoard = AppUtil.copyOf(board);
        AiResponse aiResponse = aiService.play(copyOfBoard, aiStone);
        board[aiResponse.getPoint().getX()][aiResponse.getPoint().getY()] = aiStone;
        moves.add(aiResponse.getPoint());
        return aiResponse;
    }

    @RequestMapping("/regret")
    public void regret() {
        if (moves.size() >= 2) {
            Point p = moves.remove(moves.size() - 1);
            board[p.getX()][p.getY()] = EMPTY;
            p = moves.remove(moves.size() - 1);
            board[p.getX()][p.getY()] = EMPTY;
        }
    }

    @RequestMapping("/reset")
    public void reset() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        aiStone = 0;
        moves.clear();
    }
}
