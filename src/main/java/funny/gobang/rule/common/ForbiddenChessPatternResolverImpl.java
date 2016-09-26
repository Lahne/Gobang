package funny.gobang.rule.common;

import static funny.gobang.rule.ChessPatterns.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import funny.gobang.AppConstants;
import funny.gobang.model.ChessBoard;
import funny.gobang.model.ChessType;
import funny.gobang.model.Point;
import funny.gobang.rule.ChessPatternResolver;
import funny.gobang.rule.ChessPatterns;
import funny.gobang.rule.ForbiddenRule;
import funny.gobang.rule.common.AbstractChessPatternResolver;
import funny.gobang.rule.common.ChessPatternForWhiteResolver;
import funny.gobang.rule.common.Matcher;
import funny.gobang.rule.common.PatternMatcher;

public class ForbiddenChessPatternResolverImpl implements ChessPatternResolver, ForbiddenRule{
	
	PatternMatcher whiteResolver;
	
	PatternMatcher blackResolver;
	
	//default use tolerant model
	boolean isTolerant = true;
	
	public ForbiddenChessPatternResolverImpl(){
		this.whiteResolver = new ChessPatternForWhiteResolver();
		this.blackResolver = new ChessPatternForBlackResolver();
		initilize();
	}
	
	
	public boolean isTolerant() {
		return isTolerant;
	}


	public void setTolerant(boolean isTolerant) {
		this.isTolerant = isTolerant;
	}


	void initilize(){
		initilizeBlack();
	}
	
	void initilizeBlack(){

		/*
		 * one
		 */
		
		initBlack("nebbe$#", SLEEP_THREE);

		initBlack("#bbe$#", DEAD_THREE);

		initBlack("ne$ebb#", SLEEP_THREE);

		initBlack("nmbbm$mn", JUMP_LIVE_THREE);

		initBlack("bebbe$en", SLEEP_THREE);
		initBlack("nebbe$ebn", SLEEP_THREE);
		initBlack("nebbe$ebbn", SLEEP_THREE);
		initBlack("nebbe$ebbbn", FIGHT_FOUR);
		initBlack("bebbe$ebbeb", DEAD_THREE);

		initBlack("nbbbe$ebbbn", DOUBLE_FIGHT_FOUR);

		initBlack("nbbbm$nn", FIGHT_FOUR);
		initBlack("nbbbm$ebn", FIGHT_FOUR);
		initBlack("nbbbm$ebbn", FIGHT_FOUR);
		
		
		/*
		 * two
		 */
		initBlack("#beb$#", DEAD_THREE);

		initBlack("nebe$b#", SLEEP_THREE);
		
		initBlack("nbbmb$nn", FIGHT_FOUR);
		
		initBlack("nm$bmbmn", JUMP_LIVE_THREE);
		
		initBlack("neb$mbbn", FIGHT_FOUR);
		
		initBlack("nebe$bebbn", FIGHT_FOUR);
		
		initBlack("nebeb$mbbn", FIGHT_FOUR);
		
		initBlack("bbbe$bmbbn", FIGHT_FOUR);
		
		initBlack("nbbe$bebbn", DOUBLE_FIGHT_FOUR);
		
		initBlack("nebeb$ebbb", SLEEP_THREE);
		
		initBlack("beb$ebeb", DEAD_THREE);
		
		initBlack("neb$eb#", SLEEP_THREE);
		
		initBlack("nebeb$eb#", SLEEP_THREE);
		
		initBlack("#beb$eb#", DEAD_THREE);
		
		initBlack("bbbe$beb#", DEAD_THREE);

		initBlack("bbbe$beben", SLEEP_THREE);
		
		initBlack("nbbe$beb#", FIGHT_FOUR);
		
		initBlack("ne$bebeb", SLEEP_THREE);

		initBlack("nebe$beben", DOUBLE_LIVE_THREE);
		
		/*
		 * three
		 */
		
		
		initBlack("#b$b#", DEAD_THREE);
		
		initBlack("#ebb$#", DEAD_THREE);

		initBlack("nm$bbmmn", CONTINOUS_LIVE_THREE);
		
		initBlack("bbeb$b#", DEAD_THREE);
		
		initBlack("nbmb$b#", FIGHT_FOUR);
		
		initBlack("bbebb$e#", DEAD_THREE);
		
		initBlack("beeb$b#", DEAD_THREE);
		
		initBlack("neeb$b#", SLEEP_THREE);
		
		initBlack("nbmbb$e#", FIGHT_FOUR);
		
		initBlack("beebb$e#", SLEEP_THREE);
		
		initBlack("neebb$ebb", SLEEP_THREE);
		
		initBlack("beebb$ebb", DEAD_THREE);
		
		initBlack("bbebb$ebb", DEAD_THREE);
		
		initBlack("bbebb$mbn", FIGHT_FOUR);
		
		initBlack("neebb$mbn", FIGHT_FOUR);
		
		initBlack("nbeb$bebn", DOUBLE_FIGHT_FOUR);
		
		initBlack("beebb$mbn", FIGHT_FOUR);

		initBlack("beebb$eeb", SLEEP_THREE);
		
		/*
		 * four
		 */
		initBlack("nmb$bbmn", LIVE_FOUR);

		initBlack("#bbb$mn", FIGHT_FOUR);
		
		initBlack("#b$bb#", DEAD_FOUR);

		initBlack("#$bbbeb", DEAD_FOUR);
		
		initBlack("be$bbbeb", DEAD_FOUR);
		
		initBlack("bebb$bmn", FIGHT_FOUR);

		initBlack("nmbb$beb", FIGHT_FOUR);
		
		/*
		 * five
		 */
		initBlack("nbbbb$n", FIVE);
		
		/*
		 * more than five
		 */
		initBlack("b$bbbb", MORE_THAN_FIVE);
	}
	
	void initBlack(String matchers, ChessPatterns pattern){
		((AbstractChessPatternResolver)blackResolver).parseAndAdd(matchers, pattern);
	}
	

	@Override
	public boolean isForbiddenPoint(ChessBoard board, Point point) {
		return isForbiddenPoint(board, point, AppConstants.directons);
	}
	
	
	@Override
	public boolean isForbiddenPoint(ChessBoard board, Point point, Point[] directions) {
		if (board.getChessType(point) != ChessType.EMPTY) {
			throw new RuntimeException("the point on board is not EMPTY!");
		}

		int liveThreeCount = 0;
		int fourCount = 0;

		for (int i = 0; i < directions.length; i++) {
			ChessPatterns pattern = assumeChessPattern(board, point, AppConstants.directons[i],true);
			
			if (pattern.isLiveThree()){
				liveThreeCount++;
				
				if (pattern == JUMP_LIVE_THREE && isTolerant){
					liveThreeCount--;
				}
			}
			
			if (pattern.isFightFourOrLiveFour()){
				fourCount++;
			}
			
			if (liveThreeCount == 2 || fourCount == 2) {
				return true;
			}
		}

		return false;
	}
	


	@Override
	public ChessPatterns resolveChessPattern(ChessBoard board, Point target, Point direction) {
		int chessType = board.getChessType(target);

		if(chessType == ChessType.EMPTY)
			return UNKNOWN;
	
		return blackOrWhiteResolve(chessType == ChessType.BLACK, board, target, direction);
	}

	@Override
	public ChessPatterns assumeChessPattern(ChessBoard board, Point target, Point direction,
			boolean blackOrWithe) {
		int chessType = board.getChessType(target);

		if(chessType != ChessType.EMPTY)
			throw new RuntimeException("target point must be empty if calling this function");
		
		return blackOrWhiteResolve(blackOrWithe, board, target, direction);
	}
	
	
	
	private ChessPatterns blackOrWhiteResolve(boolean blackOrWithe,ChessBoard board, Point target, Point direction) {
		Matcher matcher = new Matcher(board, target, direction);
		matcher.isBlackTarget = blackOrWithe;
		
		PatternMatcher resolver = blackOrWithe ? blackResolver: whiteResolver;
		
		
		if (resolver.match(matcher, target)){
			return matcher.pattern;
		}
		
		return UNKNOWN;
	}
	
	

	
	public class ChessPatternForBlackResolver extends AbstractChessPatternResolver{
		MoreCheckingForState moreCheckingForState = new MoreCheckingForState();
		
		public ChessPatternForBlackResolver(){
		}
		
		@Override
		protected StateNode parse(ParserHelper helper) {
			
	   		char c = helper.c;
	   		
	   		if ( c == 'w'){
	   			helper.addNodeIfAbsent(ChessType.WHITE, new FinishStateNode()).markState();
	   			return null;
	   		}else if (c == '#'){
	   			helper.addNodeIfAbsent(ChessType.BOUNDARY, new FinishStateNode()).markState();
	   			helper.addNodeIfAbsent(ChessType.WHITE, new FinishStateNode()).markState();
		   		return null;
	   		}else if (c == 'b'){
	   			return helper.addNodeIfAbsent(ChessType.BLACK, new StateNode()).markStateIfIsEnd().getBindingIfNotIsEnd();
	   		}else if( c == 'e'){
	   			return helper.addNodeIfAbsent(ChessType.EMPTY, new StateNode()).markStateIfIsEnd().getBindingIfNotIsEnd();
	   		}else if (c == 'n'){
	   			helper.addNodeIfAbsent(ChessType.BOUNDARY, new FinishStateNode()).markState();
	   			helper.addNodeIfAbsent(ChessType.WHITE, new FinishStateNode()).markState();
	   			return helper.addNodeIfAbsent(ChessType.EMPTY, new StateNode()).markStateIfIsEnd().getBindingIfNotIsEnd();
	   		}else if (c =='m'){
	   			moreCheckingForState.add(helper.state, helper.relateToTaget);
	   			return helper.addNodeIfAbsent(ChessType.EMPTY, new StateNode()).markStateIfIsEnd().getBindingIfNotIsEnd();
	   		}else{
	   			throw new RuntimeException("Parsing Parttern String Error: Not Support Char - " + c);
	   		}
			
		}
	 	
		@Override
		protected boolean checkingBySameId(int sameId, Matcher matcher, Point point){

			matcher.markMatchedAndSetPattern(getPatternByState(sameId));
			return true;
			
			/* default disable more checking at here, there is a little bug
			if (enableMoreChecking && moreCheckingForState.needMoreChecking(sameId)){
				return moreCheckingForState.doCheck(sameId, matcher, point);
			}
			*/
		}

	 	
		class MoreCheckingForState{
			
			Map<Integer,List<Integer>> moreCheckingMap;
			
			Point[] getOtherDirections(Point delta){
				List<Point> otherDirections = new LinkedList<>();
				for (int i = 0; i < AppConstants.directons.length; i++){
					if (!AppConstants.directons[i].equals(delta)){
						otherDirections.addAll(otherDirections);
					}
				}
				return otherDirections.toArray(new Point[0]);
			}

			List<Point> getEmptyPointToCheck(int id,Point target,Point delta){
				List<Integer> emptyRelateToTarget = moreCheckingMap.get(id);
				List<Point> emptyPointsToCheck = new LinkedList<>();
				for (int i =0; i < emptyPointsToCheck.size(); i++){
					int mult = emptyRelateToTarget.get(i);
					emptyPointsToCheck.add(new Point(target.getX() + mult * delta.getX()  , target.getY() + mult * delta.getY()));
				}
				
				return emptyPointsToCheck;
			}
			
			void add(int id,int relativeToTarget){
				if (moreCheckingMap == null){
					moreCheckingMap = new HashMap<>();
				}
				if (!moreCheckingMap.containsKey(id)){
					moreCheckingMap.put(id, new ArrayList<>());
				}
				moreCheckingMap.get(id).add(relativeToTarget);
			}
			
			boolean needMoreChecking(int id){
				return moreCheckingMap!= null && moreCheckingMap.containsKey(id);
			}
			
			
			boolean doCheck(int id, Matcher matcher, Point point){
				
				Point[] otherDirections = getOtherDirections(matcher.delta);
				
				List<Point> pointsToCheck = getEmptyPointToCheck(id,point,matcher.delta);
				
				for (int i=0; i < pointsToCheck.size(); i++){
					Point emptyPoint = pointsToCheck.get(i);
					if (isForbiddenPoint(matcher.board, emptyPoint, otherDirections)){
						ChessBoard newBoard = matcher.board.clone();
						newBoard.downChess(emptyPoint, ChessType.WHITE);
						Matcher newMatcher = new Matcher(newBoard, matcher.target, matcher.delta);
						if (match(newMatcher, point)){
							return matcher.markMatchedAndSetPattern(newMatcher.pattern);
						}else{
							return matcher.markFail();
						}
					}
				}
				
				return true;
			}
			
		}
	 	
	}
	
	
	
	


	
	
}
