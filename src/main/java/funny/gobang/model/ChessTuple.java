package funny.gobang.model;

/**
 * The chess tuple which is separated by different stone or 2+ empties.
 * <p>
 * Created by charlie on 2016/9/17.
 */
public class ChessTuple {
    private int stone;
    private int count;
    private int empty;
    private int head;
    private int tail;
    private String tuple;

    public ChessTuple() {
    }

    public ChessTuple(int stone, int count, int empty, int head, int tail, String tuple) {
        this.stone = stone;
        this.count = count;
        this.empty = empty;
        this.head = head;
        this.tail = tail;
        this.tuple = tuple;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getEmpty() {
        return empty;
    }

    public void setEmpty(int empty) {
        this.empty = empty;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public int getTail() {
        return tail;
    }

    public void setTail(int tail) {
        this.tail = tail;
    }

    public String getTuple() {
        return tuple;
    }

    public void setTuple(String tuple) {
        this.tuple = tuple;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChessTuple that = (ChessTuple) o;

        if (stone != that.stone) return false;
        if (head != that.head) return false;
        if (tail != that.tail) return false;
        return tuple.equals(that.tuple);

    }

    @Override
    public int hashCode() {
        int result = stone;
        result = 31 * result + head;
        result = 31 * result + tail;
        result = 31 * result + tuple.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChessTuple{");
        sb.append("stone=").append(stone);
        sb.append(", count=").append(count);
        sb.append(", empty=").append(empty);
        sb.append(", head=").append(head);
        sb.append(", tail=").append(tail);
        sb.append(", tuple='").append(tuple).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
