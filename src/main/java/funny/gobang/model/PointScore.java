package funny.gobang.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by charlie on 2016/9/26.
 */
public class PointScore {
    private Point point;
    private long score;
    private List<Point> path = new ArrayList<>();

    public PointScore(long score) {
        this.score = score;
    }

    public PointScore(Point point, long score) {
        this.point = point;
        this.score = score;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public List<Point> getPath() {
        return path;
    }

    public void setPath(List<Point> path) {
        this.path = path;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void addPoint(Point p) {
        this.point = p;
        path.add(0, p);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PointScore{");
        sb.append("point=").append(point);
        sb.append(", score=").append(score);
        sb.append(", path=").append(path);
        sb.append('}');
        return sb.toString();
    }
}
