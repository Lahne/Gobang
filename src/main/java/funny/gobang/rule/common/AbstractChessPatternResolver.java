package funny.gobang.rule.common;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import funny.gobang.model.ChessType;
import funny.gobang.model.Point;
import funny.gobang.rule.ChessPatterns;

public abstract class AbstractChessPatternResolver implements PatternMatcher{
	
	
	private HashSet<String> initilizeSet = new HashSet<>();
	
    protected HashMap<Integer, State> statesMap = new HashMap<>();
    
    protected HashMap<Integer, StateTree> benchMarkToStateTree = new HashMap<>(6);
    
    protected boolean isBlackTarget = true;
    
	public AbstractChessPatternResolver(){
		
	}
	
	public void parseAndAdd(String matchers, ChessPatterns patterns){
		parseAndAddInternal(matchers,patterns);
		String reverse = new StringBuffer(matchers).reverse().toString();
		parseAndAddInternal(reverse,patterns);
	}
	
	private void parseAndAddInternal(String matchers, ChessPatterns patterns){
		if (initilizeSet.contains(matchers)) {
			return;
		}
		Integer stateId = generateNextStateId();
		State state = new State(matchers,patterns,stateId);
		
		parse(matchers, state, isBlackTarget);
		initilizeSet.add(matchers);
		
		statesMap.put(stateId, new State(matchers,patterns,stateId));
		
	}
	
	protected int generateNextStateId(){
		return statesMap.size() +1;
	}
	
    protected abstract StateNode parse(ParserHelper helper);  	
 	
	protected boolean checkingBySameId(int sameId, Matcher matcher, Point point){
		matcher.markMatchedAndSetPattern(getPatternByState(sameId));
		return true;
	}
	
	protected StateTree addIfAbsent(int benchmark){
		if (benchMarkToStateTree.get(benchmark) == null){
			benchMarkToStateTree.put(benchmark, new StateTree());
		}
		return benchMarkToStateTree.get(benchmark);
	}
	
	protected int getExpectedTypeCount(String str){
		int i = 0;
		for (; i < str.length();i++){
			char expected = isBlackTarget ? 'b' : 'w';
			if (str.charAt(i) != expected){
				break;
			}
		}
		return i;
	}
	
	protected String parseBenchMark(State state, String orig){
		int count = getExpectedTypeCount(orig);
		state.benchmark += count;
		return orig.substring(count,orig.length());
	}
	
	protected void parse(String matchers, State state,boolean isBlackTarget){
		int indexOfTarget = matchers.indexOf('$');
		if (indexOfTarget == -1)
			throw new RuntimeException("must cotain a target - $");
		
		String leftString = matchers.substring(0, indexOfTarget);
		leftString = new StringBuffer(leftString).reverse().toString();

		leftString = parseBenchMark(state,leftString);
		
		String rightString = matchers.substring(indexOfTarget+1, matchers.length());
		
		rightString = parseBenchMark(state,rightString);
		
		StateTree stateTree = addIfAbsent(state.benchmark);
		
		parseInternal(leftString, stateTree, state, true);
		
		parseInternal(rightString, stateTree, state, false);
		
	}
	
	protected void parseInternal(String str,StateTree stateTree,State state,boolean leftOrRight){
		
		StateNode nextChess = leftOrRight ? stateTree.leftRoot : stateTree.rightRoot;
		
		if (str.length() == 0){
			nextChess.markState(state.id);
			return;
		}
		
		for (int i = 0; i < str.length(); i++){
			if (nextChess == null)
				break;
			char c = str.charAt(i);
			int relateToTarget = i * (leftOrRight ? -1 : 1);
			nextChess = parse(new ParserHelper(nextChess,c ,  i == str.length() -1, state.id, relateToTarget));
		}
	}
	
	protected ChessPatterns getPatternByState(int id){
		return statesMap.get(id).chessPattern;
	}
	
	protected SearchHelper[] getleftAndRightHelper(Matcher matcher, Point point){
		SearchHelper leftHelper = new SearchHelper(true, matcher, point);
		SearchHelper rightHelper = new SearchHelper(false, matcher, point);
		return new SearchHelper[]{leftHelper,rightHelper};
	}
	
	protected int calculateBenchMark(SearchHelper[] leftAndRightHelper){
		int countLeft = leftAndRightHelper[0].peekUtilMeetDifferent();
		int countRight = leftAndRightHelper[1].peekUtilMeetDifferent();
		return countLeft+ countRight + 1;
	}
	
	@Override
	public boolean match(Matcher matcher, Point point) {
		SearchHelper[] twoHelper = getleftAndRightHelper(matcher, point);

		int benchMark = calculateBenchMark(twoHelper);
		
		StateTree tree = benchMarkToStateTree.get(benchMark >= 6 ? 6 : benchMark);
		
		if (tree == null){
			return matcher.markFail();
		}
		
		return tree.match(matcher, point,twoHelper);
	}
	
  
	protected class ParserHelper{
		public final StateNode stateNode;
		public final char c;
		public final boolean isEnd;
		public final int state;
		public final int relateToTaget;
		
		public ParserHelper(StateNode stateNode, char c, boolean isEnd, int state, int relateToTaget) {
			super();
			this.stateNode = stateNode;
			this.c = c;
			this.isEnd = isEnd;
			this.state = state;
			this.relateToTaget = relateToTaget;
		}
		
		public ParserHelper addNodeIfAbsent(int chessType,StateNode mayAdded){
			if (stateNode.getStateForChess(chessType) == null){
				stateNode.addStateForChess(chessType, mayAdded);
			}
			return new ParserHelper(stateNode.getStateForChess(chessType), c, isEnd, state, relateToTaget);
		}
		
		public ParserHelper markState(){
			stateNode.markState(state);
			return this;
		}
		
		public ParserHelper markStateIfIsEnd(){
			if (isEnd){
				stateNode.markState(state);
			}
			return this;
		}
		
		public StateNode getBindingIfNotIsEnd(){
			return !isEnd ? stateNode : null;
		}
		
	}
	

	protected class SearchHelper{
		StateNode expected;
		Peeker peeker;
		StateIdPool foundIds;
		boolean left;
		String meetString = "";
		boolean isBlackTarget;
		
		SearchHelper(boolean left,Matcher matcher, Point point){
			this.left = left;
			this.peeker =  new Peeker(matcher.board,matcher.delta);
			this.peeker.endPoint = point;
			this.foundIds = new StateIdPool();
			this.isBlackTarget = matcher.isBlackTarget;
		}
		
		public void setStateForChess(StateTree stateTree){
			this.expected = left ? stateTree.leftRoot: stateTree.rightRoot;
			foundIds.addStates(left ? stateTree.leftRoot.markedStates: stateTree.rightRoot.markedStates);
		}
		
		public boolean canPeekNextPoint(){
			return expected != null && expected.shouldPeekNext();
		}
		
		public int peekUtilMeetDifferent(){
			peeker.peekSpecific(left, peeker.endPoint, isBlackTarget ? ChessType.BLACK : ChessType.WHITE);
			return peeker.count;
		}
		
		
		public Integer getSameIdInFound(StateIdPool another){
			return foundIds.getSameState(another);
		}
		
		public StateNode peekNextPoint(){
			int chessType = peeker.peekOneFromEnd(left);
			expected = expected.getStateForChess(chessType);
			return expected;
		}
		
		public boolean isCurrentPointHasMarkedId(){
			return expected != null && expected.hasMarkedStates();
		}
		
		public StateIdPool getCurrentPointMarkedIds(){
			return expected.markedStates;
		}
		
		public void addFoundIds(StateIdPool ids){
			foundIds.addStates(ids);
		}
		
	}
	
	protected class State{
		   
		   public String pattern;
		   
		   ChessPatterns chessPattern;
		   
		   int benchmark;
		   
		   Integer id;
		   
		   List<Integer> relativeTarget;

		   public State(String pattern, ChessPatterns chessPattern, Integer id) {
			super();
			if (id == null){
				throw new RuntimeException("Can't be null");
			}
			
			this.pattern = pattern;
			this.chessPattern = chessPattern;
			this.id = id;
			this.benchmark = 1;
			
		   }
		   
			void addRelativeTarget(int i){
				if (relativeTarget == null){
					relativeTarget = new LinkedList<>();
				}
				relativeTarget.add(i);
			}

			@Override
			public int hashCode() {
				return id.hashCode();
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (!(obj instanceof State))
					return false;
				State other = (State) obj;
				
				if (!id.equals(other.id))
					return false;
				return true;
			}
		   
	   }
	
	
	
	protected class StateIdPool{
		
		HashSet<Integer> stateIds;
		
		StateIdPool(){
			stateIds = new HashSet<>();
		}
		
		boolean containsState(int state){
			return stateIds.contains(state);
		}
		
		void addState(int state){
			stateIds.add(state);
		}
		
		void printAll(){
			Iterator<Integer> it = stateIds.iterator();
			while (it.hasNext()){
				Integer state = it.next();
				System.out.print(state+",");
			}
			System.out.println();
		}
		
		void addStates(StateIdPool another){
			if (another == null || another == this)
				return;
			
			Iterator<Integer> it = another.stateIds.iterator();
			while (it.hasNext()){
				Integer state = it.next();
				if (!this.containsState(state)){
					addState(state);
				}
			}
		}
		
		int size(){
			return stateIds == null ? 0 : stateIds.size();
		}
		
		Integer getSameState(StateIdPool another){
			if (another == null)
				return null;
			if (another == this){
				throw new RuntimeException("In same statespool");
			}
			
			Iterator<Integer> it = stateIds.iterator();
			while (it.hasNext()){
				Integer state = it.next();
				if (another.containsState(state)){
					return state;
				}
			}
			
			return null;
		}
	}
	
	
	protected class StateTree {
			StateNode rightRoot;
			StateNode leftRoot;
			int benchMark;
			
			public StateTree() {
				this.rightRoot = new StateNode();
				this.leftRoot = new StateNode();
			}
			
			protected <T> void swapFirstAndSecond(T[] arr){
				T tmp = arr[0];
				arr[0] = arr[1];
				arr[1] = tmp;
			}
			
			void bindRootStateNode(SearchHelper[] twoHelper){
				twoHelper[0].setStateForChess(this);
				twoHelper[1].setStateForChess(this);
			}
			
			public boolean match(Matcher matcher, Point point,SearchHelper[] twoHelper) {
				bindRootStateNode(twoHelper);
				return matchInternal(matcher, point, twoHelper);
			}
			
			private boolean matchInternal(Matcher matcher, Point point,SearchHelper[] twoHelper) {
				
				while(true){
					if (!twoHelper[0].canPeekNextPoint() && !twoHelper[1].canPeekNextPoint()){
						Integer sameId = twoHelper[0].getSameIdInFound(twoHelper[1].foundIds);
						if (sameId != null){
							return checkingBySameId(sameId, matcher, point);
						}
						return matcher.markFail();
					}else if (!twoHelper[0].canPeekNextPoint()){
						swapFirstAndSecond(twoHelper);
						continue;
					}
					
					twoHelper[0].peekNextPoint();
					
					if (twoHelper[0].isCurrentPointHasMarkedId()){
						StateIdPool oneSideEndIds = twoHelper[0].getCurrentPointMarkedIds();
						Integer sameId = twoHelper[1].getSameIdInFound(oneSideEndIds);
						if (sameId != null){
							return checkingBySameId(sameId, matcher, point);
						}
						twoHelper[0].addFoundIds(oneSideEndIds);
						swapFirstAndSecond(twoHelper);
					}
				}
			}
		}

	
	public class StateNode  {
		
		StateIdPool markedStates;
		
		Map<Integer, StateNode> nextExpectedPointMap = new HashMap<>();
		
		boolean isIdEndAtHere(int id){
			if (markedStates == null)
				return false;
			return markedStates.containsState(id);
		}
		
		boolean hasMarkedStates(){
			return markedStates != null && markedStates.size() != 0;
		}
		
		StateIdPool getMarkedStates(){
			return markedStates;
		}
		
		void markState(int id){
			if (markedStates == null){
				markedStates = new StateIdPool();
			}
			markedStates.addState(id);
		}
		
		
		StateNode addStateForChessIfAbsent(int chessType,StateNode stateForChess){
			if (nextExpectedPointMap.get(chessType) == null){
				nextExpectedPointMap.put(chessType, stateForChess);
			}
			return nextExpectedPointMap.get(chessType);
		}
		
		
		void addStateForChess(int chessType,StateNode stateForChess){
			nextExpectedPointMap.put(chessType, stateForChess);
		}
		
		StateNode addStateForChess(int type){
			if (nextExpectedPointMap.get(type) == null){
				if (type == ChessType.BLACK || type == ChessType.EMPTY){
					nextExpectedPointMap.put(type, new StateNode());
				}else{
					nextExpectedPointMap.put(type, new FinishStateNode());
				}
			}
			return nextExpectedPointMap.get(type);
		}
		
		StateNode getStateForChess(int type){
			return nextExpectedPointMap.get(type);
		}
		
		
		boolean shouldPeekNext(){
			return true;
		}
		
	}
	
	public class FinishStateNode extends StateNode{
		boolean shouldPeekNext(){
			return false;
		}
	}

}
