package funny.gobang.service;

import funny.gobang.model.Point;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by charlie on 2016/8/27.
 */
@Service
public class ForbiddenService {
    public List<Point> find(int[][] board) {
        //TODO find all forbidden points
        return Collections.emptyList();
    }

}
