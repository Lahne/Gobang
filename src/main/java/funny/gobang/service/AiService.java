package funny.gobang.service;

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
    private BoardService boardService;

    @Autowired
    private EvaluationService evaluationService;

    @Value("${search.depth:6}")
    private int searchDepth;

    public AiResponse play(int[][] board, int stone) {
        //TODO call alpha-beta
        //fake response
        AiResponse res = new AiResponse();
        res.setPoint(new Point(7, 8));
        res.setStone(stone);
        return res;
    }


}