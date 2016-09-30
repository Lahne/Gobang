package funny.gobang.model;

/**
 * Created by charlie on 2016/9/28.
 */
public class TTEntry {
    public enum ScoreType {
        EXACT, LOWER_BOUND, UPPER_BOUND;
    }

    private long key;
    private long score;
    private int depth;
    private ScoreType type;
    private Point point;

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public ScoreType getType() {
        return type;
    }

    public void setType(ScoreType type) {
        this.type = type;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TTEntry ttEntry = (TTEntry) o;

        if (key != ttEntry.key) return false;
        if (score != ttEntry.score) return false;
        if (depth != ttEntry.depth) return false;
        if (type != ttEntry.type) return false;
        return point.equals(ttEntry.point);

    }

    @Override
    public int hashCode() {
        int result = (int) (key ^ (key >>> 32));
        result = 31 * result + (int) (score ^ (score >>> 32));
        result = 31 * result + depth;
        result = 31 * result + type.hashCode();
        result = 31 * result + point.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TTEntry{");
        sb.append("key=").append(key);
        sb.append(", score=").append(score);
        sb.append(", depth=").append(depth);
        sb.append(", type=").append(type);
        sb.append(", point=").append(point);
        sb.append('}');
        return sb.toString();
    }
}
