package funny.gobang.service;

import org.springframework.stereotype.Service;

/**
 * Created by charlie on 2016/8/14.
 */
@Service
public class EvaluationService {

	public long evaluate(int[][] board, int currentStone) {
		// TODO
		return 0L;
	}

	//============below implementation can't work with a-b algorithm
	/*private boolean mifis;

	public EvaluationService(boolean ifis) {
		// 初始化，默认为后下，白棋
		mifis = ifis;
	}

	public int x;// 电脑要下的点的坐标x

	public int y;// 电脑要下的点的坐标y

	public void down(int[][] board)// 计算对于计算机来讲，每个点的权重
	{
		int[][] q = new int[15][15];
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (board[i][j] != -1)// 表示这个位置已经有点，不能下
				{
					q[i][j] = -1;
				} else {
					q[i][j] = findQz(i, j, board);// 对于一个点的权重计算
				}
			}
		}
		forMax(q);// 找出权重最大的点
	}

	public void forMax(int[][] q)// 找出权重最大的点，在上个函数中用到
	{
		int max = 0;
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (q[i][j] > max) {
					x = i;
					y = j;
					max = q[i][j];
				}
			}
		}
	}



	public int findQz(int x, int y, int[][] board)// 计算固定点的权重
	{
		int qz = 0;
		int w1 = 10000000;// 定义对于不同情况下，每个点要加上的权重
		int w2 = 50000;
		int w3 = 10000;
		int w4 = 5000;
		int w5 = 1000;
		int w6 = 500;
		int w7 = 100;
		int w8 = 50;
		int w9 = -100000000;
		int[] move = new int[4];
		if (mifis)// 这里是先假定下一个颜色的棋子，然后确定下完之后所带来的改变
		{// 先计算的是电脑下在这里的影响
			board[x][y] = 0;
		} else {
			board[x][y] = 1;
		}
		move[0] = RuleService.x1(x, y, board);// 左右方向上的权重
		move[1] = RuleService.x2(x, y, board);// 上下方向上的权重
		move[2] = RuleService.x3(x, y, board);// 左高右低斜线的权重
		move[3] = RuleService.x4(x, y, board);// 做低有高斜线的权重
		if (x == 7 && y == 7)// 如果点在中心位置，权重加1
		{
			qz += 1;
		}

		for (int i = 0; i < 4; i++) {
			if (abs(move[i]) == 5)// 如果出现五连
			{
				qz += w1;
			} else if (move[i] == 4)// 如果出现至少一端自由的四连
			{
				qz += w3;
			} else if (move[i] == 3)// 出现两端自由的三连
			{
				qz += w5;
			} else if (move[i] == 2) {
				qz += w7;
			}

			if (mifis) {
				if (RuleService.fails(move, board[x][y])) {
					qz += w9;
				}
			}
		}
		// 此时计算人下在这里的影响，两次计算结果叠加在一起
		if (mifis) {
			board[x][y] = 1;
		} else {
			board[x][y] = 0;
		}
		// 步骤与内容与上面相似
		move[0] = RuleService.x1(x, y, board);
		move[1] = RuleService.x2(x, y, board);
		move[2] = RuleService.x3(x, y, board);
		move[3] = RuleService.x4(x, y, board);

		for (int i = 0; i < 4; i++) {
			if (abs(move[i]) == 5) {
				qz += w2;
			} else if (move[i] == 4) {
				qz += w4;
			} else if (move[i] == 3) {
				qz += w6;
			} else if (move[i] == 2) {
				qz += w8;
			}
		}
		board[x][y] = -1;// 将位置恢复为无子状态
		return qz;
	}

	public int abs(int x)// 返回绝对值
	{
		if (x < 0) {
			return -x;
		} else {
			return x;
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}*/
}

