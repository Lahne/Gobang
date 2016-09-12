package funny.gobang.service;

import funny.gobang.AppConstants;
import funny.gobang.model.ChessBoard;
import funny.gobang.model.ChessType;
import funny.gobang.model.Point;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service("forbiddenService")
public class ForbiddenServicePatternImpl {

	public final static int COMMON = 1;
	public final static int LIVE_THREE = 2;
	public final static int CONTINOUS_LIVE_THREE = 3;
	public final static int JUMP_LIVE_THREE = 4;
	public final static int SLEEP_THREE = 5;
	public final static int DEAD_THREE = 6;

	public final static int FOUR = 10;
	public final static int FIGHT_FOUR = 11;
	public final static int LIVE_FOUR = 12;
	public final static int DEAD_FOUR = 13;

	public final static int WIN = 20;

	public final static int FORBIDDEN = -1;

	public boolean isTolerant = true;

	
	
	Node root ;
	
	boolean initilized = false;

	ForbiddenServicePatternImpl() {
		initilize();
	}

	void initilize() {
		
		if (initilized)
			return;
		
		root = new RootNode();
		
		init("bebbe$eb", DEAD_THREE);
		init("beb$ebeb", DEAD_THREE);
		init("be$bebeb", DEAD_THREE);

		init("#b$eb#", DEAD_THREE);
		init("#$beb#", DEAD_THREE);

		init("#bbe$#", DEAD_THREE);

		init("#b$b#", DEAD_THREE);
		init("#$bb#", DEAD_THREE);

		init("ne$bbeen", CONTINOUS_LIVE_THREE);
		init("nee$bben", CONTINOUS_LIVE_THREE);
		init("neb$been", CONTINOUS_LIVE_THREE);

		init("ne$beben", JUMP_LIVE_THREE);
		init("nebbe$en", JUMP_LIVE_THREE);
		init("neb$eben", JUMP_LIVE_THREE);

		init("neb$b#", SLEEP_THREE);
		init("nebb$#", SLEEP_THREE);
		init("ne$bb#", SLEEP_THREE);
		init("ne$ebb#", SLEEP_THREE);
		init("nebe$b#", SLEEP_THREE);
		init("nebeb$#", SLEEP_THREE);
		
		init("neb$beeb", SLEEP_THREE);
		init("ne$bbeeb", SLEEP_THREE);
		init("nebb$eeb", SLEEP_THREE);

		init("beebb$en", SLEEP_THREE);
		init("beeb$ben", SLEEP_THREE);
		init("bee$bben", SLEEP_THREE);

		init("nebbe$eb", SLEEP_THREE);
		init("bebbe$en", SLEEP_THREE);

		init("ne$bebeb", SLEEP_THREE);
		init("neb$ebeb", SLEEP_THREE);

		init("be$beben", SLEEP_THREE);
		init("beb$eben", SLEEP_THREE);

		init("neb$bben", LIVE_FOUR);
		init("ne$bbben", LIVE_FOUR);

		init("nbbeb$ebbn", FORBIDDEN);// should before below FIGHT_FOUR
		init("nbeb$bebn", FORBIDDEN);
		
		init("bebb$ben", FIGHT_FOUR);
		init("beb$bben", FIGHT_FOUR);
		init("be$bbben", FIGHT_FOUR);

		init("nebb$beb", FIGHT_FOUR);
		init("nebbb$eb", FIGHT_FOUR);
		init("neb$bbeb", FIGHT_FOUR);
		init("ne$bbbeb", FIGHT_FOUR);
		
		init("nbbbe$n", FIGHT_FOUR);
		init("nnbb$ebn", FIGHT_FOUR);
		init("nnb$bebn", FIGHT_FOUR);
		init("nn$bbebn", FIGHT_FOUR);
		
		
		init("#bbb$en", FIGHT_FOUR);
		init("#bb$ben", FIGHT_FOUR);
		init("#b$bben", FIGHT_FOUR);
		init("#$bbben", FIGHT_FOUR);

		init("nb$ebbn", FIGHT_FOUR);//bbeb$ebbn => forbidden
		init("n$bebbn", FIGHT_FOUR);

		init("#b$bb#", DEAD_FOUR);
		init("#$bbb#", DEAD_FOUR);

		init("#$bbbeb", DEAD_FOUR);
		init("#b$bbeb", DEAD_FOUR);
		init("#bb$beb", DEAD_FOUR);
		init("#bbb$eb", DEAD_FOUR);

		initilized = true;
		((RootNode)root).release();
	}


	/**
	 * @param matchers
	 *            n means not black, # mean white or boundary ? means anything
	 * @param patterns
	 */
	private void init(String matchers, int patterns) {
		RootNode rootNode = ((RootNode)root);
		rootNode.parseAndAdd(matchers, patterns);
		String reverseMatcher = new StringBuffer(matchers).reverse().toString();
		rootNode.parseAndAdd(reverseMatcher, patterns);
	}




	public boolean isForbiddenPoint(int[][] board, Point point) {
		ChessBoard chessBoard = new ChessBoard(board[0].length, board);
		return isForbiddenPoint(chessBoard, point);

	}

	/**
	 * point on chess board should be EMPTY, and we assume the point will be
	 * BLACK as just BLACK has forbidden point
	 * 
	 * @param board
	 * @param point
	 * @return if current point is forbidden point, return true
	 */
	public boolean isForbiddenPoint(ChessBoard board, Point point) {
		if (board.getChessType(point) != ChessType.EMPTY) {
			throw new RuntimeException("the point on board is not EMPTY!");
		}

		int liveThreeCount = 0;
		int fourCount = 0;

		boolean needCountJUMPThree = isTolerant ? false : true;
		for (int i = 0; i < AppConstants.directons.length; i++) {
			int pattern = getDerictionPattern(board, point, AppConstants.directons[i]);
			if (pattern == FORBIDDEN) {
				return true;
			} else if (pattern == CONTINOUS_LIVE_THREE) {
				liveThreeCount++;
			} else if (pattern == JUMP_LIVE_THREE && needCountJUMPThree) { 
				// if go here, means it's not tolerant
				liveThreeCount++;
			} else if (pattern == FOUR) {
				fourCount++;
			}
			if (liveThreeCount == 2 || fourCount == 2) {
				return true;
			}
		}

		return false;
	}

	public int getDerictionPattern(ChessBoard board, Point point, Point delta) {

		Matcher matcher = new Matcher(board, point, delta);

		if (root.match(matcher, true, point)){
			return matcher.pattern;
		}
		
		return COMMON;
	}

	static class Matcher {

		ChessBoard board;

		Peeker peeker;

		int pattern;
		
		Point delta;
		Point target;

		boolean isMatched = false;

		Point first;
		Point last;

		public Matcher(ChessBoard board, Point target, Point delta) {
			super();
			this.board = board;
			this.delta = delta;
			this.target = target;
			this.peeker = new Peeker(board, delta);
		}
		
		void markMatchedAndSetPattern(int pattern){
			this.isMatched = true;
			this.pattern = pattern;
		}
		
		void markFail(){
			this.isMatched = false;
		}

	}

	static Node accept = new Node();

	static class Node {

		Node next;

		Node() {
			next = accept;
		}

		boolean match(Matcher matcher, boolean left, Point point) {
			if (left) {
				matcher.first = point;
			} else {
				matcher.last = point;
			}
			return true;
		}

		
		void study() {

		}

	}

	
	
	// always first
	static class RootNode extends Node{
		
		private Set<String> initilizeSet = new HashSet<>();

		
		Map<Integer, List<PatternNode>> patternsMap = new HashMap<>();
		
		int patternNodesCount = 0;
		
		void parseAndAdd(String matchers, int patterns){
			if (initilizeSet.contains(matchers)) {
				return;
			}
			PatternNode patternNode = parse(matchers, patterns);
			
			patternNodesCount++;
			
			int key = patternNode.blackContainsTarget;
			if (!patternsMap.containsKey(patternNode.blackContainsTarget)){
				patternsMap.put(key, new LinkedList<>());
			}
			List<PatternNode> patternNodes = patternsMap.get(key);
			
			patternNodes.add(patternNode);
			
			initilizeSet.add(matchers);
			
		}
		
		void release(){
			System.out.println("initilized patterns : " + initilizeSet.size());
			initilizeSet = null;// Release temporary storage
		}
		
	    private Node setAsNextNode(int count, char type, Node prevNode) {

				Node ret = null;
				if (type == 'n') {
					ret = new NotBlack(prevNode);
				} else if (type == 'b') {
					ret = new Expected(count, ChessType.BLACK, prevNode);
				} else if (type == 'e') {
					ret = new Expected(count, ChessType.EMPTY, prevNode);
				} else if (type == '#') {
					ret = new WhiteOrBoundary();
				} else if (type == '?') {
					ret = new Any();
				} else if (type == 'w') {
					ret = new Expected(count, ChessType.WHITE, prevNode);
				} else {
					throw new RuntimeException("");
				}

				return ret;
			}
		
		private PatternNode parse(String matchers, int patterns) {
			if (matchers.length() < 1) {
				throw new RuntimeException("size should >= 1");
			}
			int indexOfTarget = matchers.indexOf('$');

			if (indexOfTarget == -1) {
				throw new RuntimeException("target $ must be existed");
			}
			
			int blackCount = 1;
			
			for (int i = indexOfTarget -1; i > 0; i-- ){
				if (matchers.charAt(i) == 'b'){
					blackCount++;
				}else{
					break;
				}
			}
			for (int i = indexOfTarget +1; i < matchers.length(); i++ ){
				if (matchers.charAt(i) == 'b'){
					blackCount++;
				}else{
					break;
				}
			}
			

			PatternNode rootNode = new PatternNode(patterns,blackCount);

			if (indexOfTarget != 0) {
				char type = matchers.charAt(0);

				int count = 1;

				Node prevNode = null;

				for (int i = 1; i < indexOfTarget; i++) {
					char c = matchers.charAt(i);

					if (c == type) {
						count++;
					} else {
						prevNode = setAsNextNode(count, type, prevNode);
						count = 1;
						type = c;
					}
				}
				prevNode = setAsNextNode(count, type, prevNode);
				rootNode.prev = prevNode;
			}

			if (indexOfTarget != matchers.length() - 1) {

				char type = matchers.charAt(matchers.length() - 1);

				int count = 1;

				Node nextNode = null;

				for (int i = matchers.length() - 2; i > indexOfTarget; i--) {
					char c = matchers.charAt(i);

					if (c == type) {
						count++;
					} else {
						nextNode = setAsNextNode(count, type, nextNode);
						count = 1;
						type = c;
					}
				}
				nextNode = setAsNextNode(count, type, nextNode);
				rootNode.next = nextNode;
			}

			return rootNode;
		}
		
		
		
		int getContinousBlackContainTarget(Matcher matcher, Point point){
			Peeker peeker = matcher.peeker;
			if (peeker == null){
				throw new RuntimeException("Peeker should not be null");
			}
			
			int continousBlackContainTarget = 1;
			peeker.peekSpecific(true, point, ChessType.BLACK);
			continousBlackContainTarget += peeker.count;
			peeker.peekSpecific(false, point, ChessType.BLACK);
			continousBlackContainTarget += peeker.count;
			
			return continousBlackContainTarget;
		}
		
		boolean match(Matcher matcher, boolean left, Point point) {
			
			// check ContinousBlackContainTarget at first, if count >= 5, we can direct return
			int conBlack = getContinousBlackContainTarget(matcher,point);
			
			if (conBlack >= 5){
				matcher.markMatchedAndSetPattern(conBlack == 5 ? WIN : FORBIDDEN);
				return true;
			}
			
			List<PatternNode> patternNodes = patternsMap.get(conBlack);
			
			if (patternNodes == null){
				return false;
			}
			
			System.out.println("patternNodes count: " + patternNodes.size());
			
			for (PatternNode patternNode : patternNodes) {
				if (patternNode.match(matcher, true, point)) {
					matcher.pattern = patternNode.pattern;
					matcher.isMatched = true;
					return true;
				}
			}
			matcher.isMatched = false;
			
			return false;
		}
		
	}
	
	
	static class PatternNode extends Node {

		int blackContainsTarget = 1;
		
		int pattern;

		Node prev;// left

		PatternNode(int pattern) {
			this.pattern = pattern;
		}
		
		PatternNode(int pattern, int blackContainsTarget) {
			this(pattern);
			this.blackContainsTarget = blackContainsTarget;
		}

		void setPrev(Node leftNode) {
			this.prev = leftNode;
		}

		void setNext(Node rightNode) {
			this.next = rightNode;
		}

		boolean match(Matcher matcher, boolean left, Point point) {

			boolean isPrevMatched = false;

			if (prev != null) {
				isPrevMatched = prev.match(matcher, true, matcher.target);
			}

			if (!isPrevMatched) {
				matcher.markFail();
				return false;
			}
			
			if (next == null || next.match(matcher, false, matcher.target)) {
				matcher.markMatchedAndSetPattern(pattern);
				return true;
			}
			
			matcher.markFail();
			
			return false;
		}

	}

	static class Any extends Node {

		Any() {

		}

		boolean match(Matcher matcher, boolean left, Point point) {
			return true;
		}

	}

	static class NotBlack extends Node {

		public NotBlack(Node next) {
			this.next = next;
		}
		
		boolean match(Matcher matcher, boolean left, Point startPoint) {

			if (startPoint == null) {
				return true;
			}
			Peeker peeker = matcher.peeker;
			peeker.peek(left, startPoint);

			if (peeker.type == ChessType.BLACK) {
				return false;
			}
			if (next == null){
				return true;
			}
			return next.match(matcher, left, peeker.endPoint);
		}

	}

	static class WhiteOrBoundary extends Node {

		boolean match(Matcher matcher, boolean left, Point startPoint) {

			if (startPoint == null) {
				return false;
			}

			Peeker peeker = matcher.peeker;
			peeker.peek(left, startPoint);

			if (peeker.type == ChessType.BLACK || peeker.type == ChessType.EMPTY) {
				return false;
			}

			return next.match(matcher, left, peeker.endPoint);
		}

	}

	static class Expected extends Node {
		int count;
		int expectedChess;

		Expected(int count, int chessType, Node next) {
			this.count = count;
			this.expectedChess = chessType;
			this.next = next;
		}

		boolean match(Matcher matcher, boolean left, Point startPoint) {

			if (startPoint == null) {
				return false;
			}
			Peeker peeker = matcher.peeker;
			Point excludeStartPoint = startPoint;
			for (int i = 0; i < count; i++) {
				peeker.peek(left, excludeStartPoint);
				if (!isExpected(peeker.type)) {
					return false;
				}
				excludeStartPoint = peeker.endPoint;
			}
			if (next == null){
				return true;
			}
			
			return next.match(matcher, left, excludeStartPoint);
		}

		boolean isExpected(int chessType) {
			return expectedChess == chessType;
		}

	}

	static class Peeker {

		public static final int Boundary = -999;
		
		public Point delta;

		public ChessBoard board;

		public Point startPoint = null;// exclude

		public Point endPoint = null;

		public int count = 0;

		public int type; // a boundary, b black, e empty , w white

		public Peeker() {

		}

		public Peeker(ChessBoard board, Point delta) {
			this.delta = delta;
			this.board = board;
		}

		public Point getDelta() {
			return delta;
		}

		public void setDelta(Point delta) {
			this.delta = delta;
		}

		public void clear() {
			type = Boundary;
			count = 0;
			endPoint = null;
			startPoint = null;
		}

		public boolean isEndPointNull() {
			return endPoint == null;
		}

		public void peekFromEnd(boolean left) {
			peek(left, endPoint);
		}

		public void peek(boolean left, Point excludeStartPoint) {
			peek(left, excludeStartPoint, 1);
		}

		private boolean peek(boolean left, Point excludeStartPoint, int expectedCount) {
			int cap = board.getCapacity();
			int dltX = left ? -delta.getX() : delta.getX();
			int dltY = left ? -delta.getY() : delta.getY();

			clear();

			startPoint = excludeStartPoint;

			if (excludeStartPoint.getX() == cap - 1 || excludeStartPoint.getY() == cap - 1
					|| excludeStartPoint.getX() == 0 || excludeStartPoint.getY() == 0) {
				return false;
			}

			boolean needCheckExpectedCount = expectedCount > 0 ? true : false;

			int x = excludeStartPoint.getX() + dltX;
			int y = excludeStartPoint.getY() + dltY;

			type = board.getChessType(x, y);

			for (; x < cap && x >= 0 && y < cap && y >= 0; x += dltX, y += dltY) {
				int chessType = board.getChessType(x, y);
				if (type != chessType) {
					break;
				}

				count++;
				endPoint = new Point(x, y);
				type = chessType;

				if (needCheckExpectedCount && expectedCount == count) {
					return true;
				}

			}

			if (needCheckExpectedCount && expectedCount != count) {
				clear();
				return false;
			}
			return true;

		}

		private boolean peek(boolean left, Point excludeStartPoint, int expectedCount, int expectedType) {
			int cap = board.getCapacity();
			int dltX = left ? -delta.getX() : delta.getX();
			int dltY = left ? -delta.getY() : delta.getY();

			clear();

			type = expectedType;
			startPoint = excludeStartPoint;

			if (excludeStartPoint.getX() == cap - 1 || excludeStartPoint.getY() == cap - 1
					|| excludeStartPoint.getX() == 0 || excludeStartPoint.getY() == 0) {
				return false;
			}

			boolean needCheckExpectedCount = expectedCount > 0 ? true : false;

			for (int x = excludeStartPoint.getX() + dltX, y = excludeStartPoint.getY() + dltY; x < cap && x >= 0
					&& y < cap && y >= 0; x += dltX, y += dltY) {
				int chessType = board.getChessType(x, y);
				if (chessType != expectedType) {
					break;
				}
				count++;
				endPoint = new Point(x, y);

				if (needCheckExpectedCount && expectedCount == count) {
					return true;
				}

			}

			if (needCheckExpectedCount && expectedCount != count) {
				clear();
				return false;
			}

			return true;

		}

		public boolean peekSpecific(boolean left, Point excludeStartPoint, int expectedType) {
			return peek(left, excludeStartPoint, -1, expectedType);
		}

		public boolean peekSpecificCount(boolean left, Point excludeStartPoint, int expectedType, int expectedCount) {
			return peek(left, excludeStartPoint, expectedCount, expectedType);
		}
	}

}
