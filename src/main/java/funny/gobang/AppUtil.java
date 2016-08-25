package funny.gobang;

import java.util.Arrays;

/**
 * Created by charlie on 2016/8/14.
 */
public class AppUtil {
    public static int[][] copyOf(int[][] a) {
        int[][] copy = new int[a.length][];
        for (int i = 0; i < a.length; i++) {
            copy[i] = Arrays.copyOf(a[i], a.length);
        }
        return copy;
    }

}
