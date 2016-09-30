package funny.gobang.model;

/**
 * define tuple type with score for both side
 * Created by charlie on 2016/9/17.
 */
public enum TupleType {
    FIVE(1 << 30, 1 << 30 - 1),
    FOUR(1 << 24, 1 << 24 - 1),
    BLOCKED_FOUR(1 << 21, 1 << 21 - 1),
    THREE(1 << 18, 1 << 18 - 1),
    JUMP_THREE(1 << 17, 1 << 17 - 1),
    BLOCKED_THREE(1 << 15, 1 << 15 - 1),
    TWO(1 << 12, 1 << 12 - 1),
    JUMP_TWO(1 << 11, 1 << 11 - 1),
    BIG_JUMP_TWO(1 << 10, 1 << 10 - 1),
    BLOCKED_TWO(1 << 8, 1 << 8 - 1),
    ONE(1 << 6, 1 << 6 - 1),
    BLOCKED_ONE(1, 0),
    NONE(0, 0);

    private int score;
    private int opponentScore;

    TupleType(int score, int opponentScore) {
        this.score = score;
        this.opponentScore = opponentScore;
    }

    public int getScore() {
        return score;
    }

    public int getOpponentScore() {
        return opponentScore;
    }
}
