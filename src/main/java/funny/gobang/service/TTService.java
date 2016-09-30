package funny.gobang.service;

import funny.gobang.model.TTEntry;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static funny.gobang.AppConstants.*;

/**
 * Created by charlie on 2016/9/28.
 */
@Service
public class TTService {
    private long[][] aZobristTable = new long[BOARD_SIZE][BOARD_SIZE];
    private long[][] bZobristTable = new long[BOARD_SIZE][BOARD_SIZE];
    private Map<Long, TTEntry> tt = new HashMap<>(1000000);

    @PostConstruct
    public void init() {
        Random r = new Random();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                aZobristTable[i][j] = r.nextLong();
                bZobristTable[i][j] = r.nextLong();
            }
        }
    }

    public long getPositionHash(int x, int y, int stone) {
        return stone == BLACK ? aZobristTable[x][y] : bZobristTable[x][y];
    }

    public long getBoardHash(int[][] board) {
        long hash = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int stone = board[i][j];
                if (stone != EMPTY) {
                    hash ^= getPositionHash(i, j, stone);
                }
            }
        }
        return hash;
    }

    public TTEntry lookup(long zobristKey) {
        return tt.get(zobristKey);
    }

    public void store(TTEntry entry) {
        TTEntry old = tt.get(entry.getKey());
        if (old == null || old.getDepth() <= entry.getDepth()) {
            tt.put(entry.getKey(), entry);
        }
    }

}
