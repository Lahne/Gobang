package funny.gobang.service;

import funny.gobang.ai.GoBangAI;
import funny.gobang.model.AiResponse;
import funny.gobang.model.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by charlie on 2016/8/14.
 */
@Service
public class AiService {
    @Autowired
    private GoBangAI goBangAI;

    @Autowired
    private BoardService boardService;

    public AiResponse play(int[][] board, int stone) {
        Point point = goBangAI.getNext(board, stone);
        AiResponse res = new AiResponse();
        res.setWin(boardService.isWin(board,point,stone));
        res.setPoint(point);
        res.setStone(stone);
        return res;
    }


}