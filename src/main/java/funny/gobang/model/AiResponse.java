package funny.gobang.model;

/**
 * Created by charlie on 2016/8/14.
 */
public class AiResponse {
    private int stone;
    private Point point;
    private boolean win;

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }
}
