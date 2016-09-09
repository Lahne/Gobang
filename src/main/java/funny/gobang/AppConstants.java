package funny.gobang;

import funny.gobang.model.Point;

/**
 * Created by charlie on 2016/8/14.
 */
public class AppConstants {
    public static final int BOARD_SIZE = 15;
    public static final int EMPTY = 0;
    public static final int BLACK = -1;
    public static final int WHITE = 1;
	
    public static final Point[] directons = new Point[]{new Point(1,1),new Point(-1,1),new Point(0,1),new Point(1,0)};
	

}
