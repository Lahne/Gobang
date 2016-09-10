package funny.gobang.model;

/**
 * Created by charlie on 2016/9/3.
 */
public class ChessPattern {
    private int stone;
    private int length;
    private int head;

    public ChessPattern() {
    }

    public ChessPattern(int stone, int length, int head) {
        this.stone = stone;
        this.length = length;
        this.head = head;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChessPattern that = (ChessPattern) o;

        if (stone != that.stone) return false;
        if (length != that.length) return false;
        return head == that.head;

    }

    @Override
    public int hashCode() {
        int result = stone;
        result = 31 * result + length;
        result = 31 * result + head;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChessPattern{");
        sb.append("stone=").append(stone);
        sb.append(", length=").append(length);
        sb.append(", head=").append(head);
        sb.append('}');
        return sb.toString();
    }
}
