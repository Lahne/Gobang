package funny.gobang.service;

public class RuleService {
	// 确定此位置是否已经有点
	public static boolean Exit(int x, int y, int[][] board) {
		if (board[x][y] != -1) {
			return true;
		} else {
			return false;
		}
	}

	// 左右方向的权重
	public static int x1(int x, int y, int[][] board) {
		int flag = 0;
		int count = 1;// 记录有多少相连的同色点
		int i = x + 1;
		while (i < 15)// board[x,y]的右侧
		{
			if (board[x][y] == board[i][y])// 相同
			{
				count++;// 计数器加1
				i++;
			} else {
				break;// 一个不同便退出
			}
		}

		if (i == 15) {// 右侧到边缘
			flag++;
		} else {
			if (board[i][y] != -1)// 所停的位置有不同色点，已经被堵死
			{
				flag++;
			}
		}

		i = x - 1;
		while (i > 0)// 左侧
		{
			if (board[x][y] == board[i][y]) {
				count++;
				i--;
			} else {
				break;
			}
		}

		if (i == -1) {// 左侧到边缘
			flag++;
		} else {
			if (board[i][y] != -1)// 所停的位置有不同色点，已经被堵死
			{
				flag++;
			}
		}

		if (flag == 2)// 两侧皆被堵死
		{
			return -count;
		} else {
			if (flag == 1 && count == 4)
				return -count;
			if (flag == 1 && count == 3)// 出现连三的情况，且有一侧被堵
			{
				return -count;
			} else// 剩余情况
			{
				return count;
			}
		}
	}

	// 接下来x2,x3,x4与x1的方法实际上是一样的，不同的只是坐标的变换

	// 上下方向，与左右类似
	public static int x2(int x, int y, int[][] board) {
		int flag = 0;
		int count = 1;
		int i = y + 1;
		while (i < 15) {
			if (board[x][i] == board[x][y]) {
				count++;
				i++;
			} else {

				break;
			}
		}

		if (i == 15) {
			flag++;
		} else {
			if (board[x][i] != -1) {
				flag++;
			}
		}

		i = y - 1;
		while (i > 0) {
			if (board[x][i] == board[x][y]) {
				count++;
				i--;
			} else {
				break;
			}
		}

		if (i == -1) {
			flag++;
		} else {
			if (board[i][y] != -1) {
				flag++;
			}
		}

		if (flag == 2) {
			return -count;
		} else {
			if (flag == 1 && count == 3) {
				return -count;
			} else {
				return count;
			}
		}
	}

	// 左上的斜线方向
	public static int x3(int x, int y, int[][] board) {
		int flag = 0;
		int count = 1;
		int i = x - 1;
		int j = y - 1;
		while (i > 0 && j > 0) {
			if (board[x][y] == board[i][j]) {
				count++;
				i--;
				j--;
			} else {
				break;
			}
		}

		if (i == -1 || j == -1) {
			flag++;
		} else {
			if (board[i][j] != -1) {
				flag++;
			}
		}

		i = x + 1;
		j = y + 1;
		while (i < 15 && j < 15) {
			if (board[x][y] == board[i][j]) {
				count++;
				i++;
				j++;
			} else {
				break;
			}
		}

		if (i == 15 || j == 15) {
			flag++;
		} else {
			if (board[i][y] != -1) {
				flag++;
			}
		}

		if (flag == 2) {
			return -count;
		} else {
			if (flag == 1 && count == 3) {
				return -count;
			} else {
				return count;
			}
		}
	}

	// 右上的斜线方向
	public static int x4(int x, int y, int[][] board) {
		int flag = 0;
		int count = 1;
		int i = x - 1;
		int j = y + 1;

		while (i > 0 && j < 15) {
			if (board[i][j] == board[x][y]) {
				count++;
				i--;
				j++;
			} else {
				break;
			}
		}

		if (i == -1 || j == 15) {
			flag++;
		} else {
			if (board[i][j] != -1) {
				flag++;
			}
		}

		i = x + 1;
		j = y - 1;

		while (i < 15 && j >= 0) {
			if (board[i][j] == board[x][y]) {
				count++;
				i++;
				j--;
			} else {
				break;
			}
		}

		if (i == 15 || j == -1) {
			flag++;
		} else {
			if (board[i][y] != -1) {
				flag++;
			}
		}

		if (flag == 2) {
			return -count;
		} else {
			if (flag == 1 && count == 3) {
				return -count;
			} else {
				return count;
			}
		}
	}

	public static boolean fails(int[] move, int stonetype)//
	{
		if (stonetype == 0) {
			int count = 0;
			for (int i = 0; i < 4; i++) {
				if (move[i] == 3)// 出现三连状态
				{
					count++;
				}
			}

			if (count > 1)// 出现相交的三连子
			{
				return true;
			}

			count = 0;

			for (int i = 0; i < 4; i++) {
				if (move[i] == 4)// 出现四连状态
				{
					count++;
				}
			}
			if (count > 1)// 出现相交的四连子
			{
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean over(int[][] board)// 判断是否还有剩余的位置没有落子
	{
		boolean over = true;
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (board[i][j] == -1) {
					over = false;// 有空位
				}
			}
		}
		return over;
	}

	public static int winer(int x, int y, int[][] board) { // 判断是否有一方已经赢得比赛
		int[] move = new int[4];// 记录连子情况
		move[0] = RuleService.x1(x, y, board);
		move[1] = RuleService.x2(x, y, board);
		move[2] = RuleService.x3(x, y, board);
		move[3] = RuleService.x4(x, y, board);
		boolean win = false;

		if (fails(move, 1)) {
			return 0;
		} else {
			for (int i = 0; i < 4; i++) {
				if (abs(move[i]) == 5) {// 已经连上五子
					win = true;
				}
			}

			if (win) {
				return 1;// 出现赢家
			} else {
				if (over(board)) {
					return 2;// 平局
				} else {
					return 3;// 继续
				}
			}
		}
	}

	public static int abs(int x)// 绝对值
	{
		if (x < 0) {
			return -x;
		} else {
			return x;
		}
	}

}
