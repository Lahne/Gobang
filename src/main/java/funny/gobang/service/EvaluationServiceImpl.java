package funny.gobang.service;

import funny.gobang.model.ChessPattern;
import funny.gobang.model.Point;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static funny.gobang.AppConstants.BOARD_SIZE;
import static funny.gobang.AppConstants.EMPTY;

/**
 * Created by charlie on 2016/9/3.
 * based on https://github.com/zhming0/gobang/blob/gh-pages/gobang-ai-fast.js
 */
@Service("evaluationService")
public class EvaluationServiceImpl implements EvaluationService {
	private int[] scores = new int[8];
	private Map<ChessPattern, Integer>[] patternScores = new Map[2];

	public long evaluate(int[][] board, int currentStone) {
		long result = 0L;
		Map<ChessPattern, Integer> patterns = calcPatterns(board);
		Map<ChessPattern, Integer> patternScoreMap = patternScores[currentStone == 1 ? 0 : 1];
		for (ChessPattern pattern : patternScoreMap.keySet()) {
			Integer count = patterns.get(pattern);
			if (count != null) {
				result += patternScoreMap.get(pattern) * count;
			}
		}
		return result;
	}

	@PostConstruct
	private void init() {
		scores[0] = 0x40000000;
		for (int i = 1; i < 8; i++) {
			scores[i] = scores[i - 1] / 8;
		}
		patternScores[0] = generateScoreMap(1);
		patternScores[1] = generateScoreMap(-1);

	}

	private Map<ChessPattern, Integer> generateScoreMap(int crt) {
		int otr = -crt;
		Map<ChessPattern, Integer> map = new HashMap<>();
		map.put(new ChessPattern(crt, 5, 0), scores[0]);
		map.put(new ChessPattern(crt, 5, 1), scores[0]);
		map.put(new ChessPattern(crt, 5, 2), scores[0]);
		map.put(new ChessPattern(otr, 5, 0), -scores[1]);
		map.put(new ChessPattern(otr, 5, 1), -scores[1]);
		map.put(new ChessPattern(otr, 5, 2), -scores[1]);
		map.put(new ChessPattern(crt, 4, 1), scores[2]);
		map.put(new ChessPattern(crt, 4, 2), scores[2]);
		map.put(new ChessPattern(otr, 4, 2), -scores[3]);
		map.put(new ChessPattern(crt, 3, 2), scores[4]);
		map.put(new ChessPattern(otr, 3, 2), -scores[5]);
		map.put(new ChessPattern(otr, 4, 1), -scores[5]);
		map.put(new ChessPattern(crt, 2, 2), scores[6]);
		map.put(new ChessPattern(crt, 3, 1), scores[6]);
		map.put(new ChessPattern(otr, 2, 2), -scores[6]);
		map.put(new ChessPattern(otr, 3, 1), -scores[6]);
		map.put(new ChessPattern(crt, 2, 1), scores[7]);
		map.put(new ChessPattern(otr, 2, 1), -scores[7]);
		return map;
	}

	private Map<ChessPattern, Integer> calcPatterns(int[][] board) {
		Map<ChessPattern, Integer> patterns = new HashMap<>();
		//TODO seems X lines duplicated
		for (int y = 0; y < BOARD_SIZE; y++) {
			calcLine(patterns, board, new Point(0, y), new Point(1, 0));
			calcLine(patterns, board, new Point(0, y), new Point(1, 1));
			calcLine(patterns, board, new Point(0, y), new Point(1, -1));
		}

		for (int y = 0; y < BOARD_SIZE; y++) {
			calcLine(patterns, board, new Point(BOARD_SIZE - 1, y), new Point(-1, 1));
			calcLine(patterns, board, new Point(BOARD_SIZE - 1, y), new Point(-1, -1));
		}

		for (int x = 0; x < BOARD_SIZE; x++) {
			calcLine(patterns, board, new Point(x, 0), new Point(0, 1));
		}

		return patterns;
	}

	private void calcLine(Map<ChessPattern, Integer> patterns, int[][] board, Point p, Point dir) {
		List<List<Integer>> groups = readLine(board, p, dir);
		for (int k = 0; k < groups.size(); k++) {
			if (groups.get(k).get(0).equals(EMPTY) || groups.get(k).size() < 2) continue;
			int head = 0;
			if (k != 0 && groups.get(k - 1).get(0).equals(EMPTY)) head += 1;
			if (k != groups.size() - 1 && groups.get(k + 1).get(0).equals(EMPTY)) head += 1;
			ChessPattern pattern = new ChessPattern(groups.get(k).get(0), groups.get(k).size(), head);
			Integer count = patterns.get(pattern);
			if (count == null) {
				patterns.put(pattern, 1);
			} else {
				patterns.put(pattern, count + 1);
			}
		}
	}

	private List<List<Integer>> readLine(int[][] board, Point p, Point dir) {
		int i = p.getX();
		int j = p.getY();
		List<List<Integer>> resultList = new ArrayList<>();
		resultList.add(new ArrayList<>());
		int cnt = 0;
		while (i < BOARD_SIZE && j < BOARD_SIZE && i >= 0 && j >= 0) {
			List<Integer> group = resultList.get(cnt);
			if (group.isEmpty() || group.get(group.size() - 1).equals(board[i][j])) {
				group.add(board[i][j]);
			} else {
				cnt++;
				group = new ArrayList<>();
				resultList.add(group);
				group.add(board[i][j]);
			}
			i += dir.getX();
			j += dir.getY();
		}
		return resultList;
	}

	}

