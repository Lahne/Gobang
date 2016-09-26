package funny.gobang.service.tried;

import funny.gobang.AppConstants;
import funny.gobang.model.ChessBoard;
import funny.gobang.model.ChessType;
import funny.gobang.model.Point;
import funny.gobang.service.ForbiddenService;

public class ForbiddenServiceSampleImpl implements ForbiddenService {
	
	public final static int COMMON = 1;
	public final static int LIVE_THREE = 2;
	public final static int CONTINOUS_LIVE_THREE = 5;
	public final static int JUMP_LIVE_THREE = 6;
	
	public final static int SLEEP_THREE = 3;
	public final static int FOUR = 4;
	public final static int FORBIDDEN = -1;
	
	
    public boolean isTolerant = true;
    

    
	public boolean isForbiddenPoint(int[][] board,Point point){
		ChessBoard chessBoard = new ChessBoard(board[0].length, board);
		return isForbiddenPoint(chessBoard, point);
		
	}
	
    /**
	 * point on chess board should be EMPTY, 
	 * and we assume the point will be BLACK as just BLACK has forbidden point
	 * @param board
	 * @param point
	 * @return if current point is forbidden point, return true
	 */
	public boolean isForbiddenPoint(ChessBoard board,Point point){
		if (board.getChessType(point) != ChessType.EMPTY){
			throw new RuntimeException("the point on board is not EMPTY!");
		}
		
		int liveThreeCount = 0;
		int fourCount = 0;
		
		boolean needCountJUMPThree = isTolerant ? false : true;
    	for (int i =0; i < AppConstants.directons.length; i++){
    		int pattern = getDerictionPattern(board, point, AppConstants.directons[i]);
    		if (pattern == FORBIDDEN){
    			return true;
    		}else if (pattern == CONTINOUS_LIVE_THREE){
    			liveThreeCount++;
    		}else if (pattern == JUMP_LIVE_THREE && needCountJUMPThree){ //tolerant at here
    			liveThreeCount++;
    		}else if ( pattern == FOUR){
    			fourCount++;
    		}
    		if (liveThreeCount == 2 || fourCount == 2){
    			return true;
    		}
    	}
    	
		return false;
	}
	
	
	protected int[] findPoints(boolean left,ChessBoard board,Point point,Point delta){
    	int cap = board.getCapacity();
    	int dltX = left ? -delta.getX() : delta.getX();
    	int dltY = left ? -delta.getY() : delta.getY();
    	
    	int[] points = new int[board.getCapacity()];
    	int count = 0;
    	boolean findTwoContinuousEmpty = false;
		for (int x= point.getX()+dltX ,y= point.getY()+dltY; x < cap && x >= 0 && y < cap && y>=0;x += dltX,y += dltY){
			points[count] = board.getChessType(x,y);
			if (ChessType.WHITE == points[count]){
				break;
			}if (ChessType.EMPTY == points[count]){
				if (findTwoContinuousEmpty){
					break;
				}
				if (count > 0 && ChessType.EMPTY == points[count-1]){
					findTwoContinuousEmpty = true;
				}
			}
			count++;
		}
		int[] dest = new int[count];
		System.arraycopy(points, 0, dest, 0, count);
		return dest;
	}
	
		public int getDerictionPattern(ChessBoard board,Point point,Point delta){
	    	
	    	int[] leftPoints = findPoints(true, board, point, delta);
	    	int[] rightPoints = findPoints(false, board, point, delta);
	    	
	    	int totalCount = leftPoints.length + rightPoints.length + 1;
	    	if (totalCount  < 5){
	    		return COMMON;
	    	}
	    	
	    	int totalBlackCount = getBalckCountBeforeTwoEmpty(leftPoints) +  getBalckCountBeforeTwoEmpty(rightPoints) +1;
	    	
	    	//String pointsString1 = convertToString(leftPoints,"$",rightPoints);
    		//System.out.println("convert : " + pointsString1);
	    	if (totalBlackCount < 3){
	    		return COMMON;
	    	}else if (totalBlackCount == 3){
	    		String pointsString = convertToString(leftPoints,"b",rightPoints);
	    		if (pointsString.contains("beebbbeeb")
	    				|| pointsString.contains("beebbebee")
	    				|| pointsString.contains("beebebbee")
	    				|| pointsString.contains("eebbebeeb")
	    				|| pointsString.contains("eebebbeeb")){
	    			return SLEEP_THREE;
	    		}
	    		if (pointsString.contains("eebbbe") || pointsString.contains("ebbbee") ){
	    			return CONTINOUS_LIVE_THREE;
	    		}
	    		
	    		if (pointsString.contains("ebbebe") || pointsString.contains("ebebbe")){
	    			return JUMP_LIVE_THREE;
	    		}
	    	}else if (totalBlackCount == 4){
	    	    String pointsString = convertToString(leftPoints,"b",rightPoints);
	    		if (pointsString.contains("ebbbb")
	    				|| pointsString.contains("bbbbe")
	    				|| pointsString.contains("bebbb")
	    				|| pointsString.contains("bbebb")
	    				|| pointsString.contains("bbbeb")){
	    			return FOUR;
	    		}
	    	} if (totalBlackCount >= 5){
	    		String pointsString = convertToString(leftPoints,"$",rightPoints);
	    		if (pointsString.contains("beb$beb")
	    				|| pointsString.contains("bbbe$ebbb")){
	    			return FORBIDDEN;
	    		}
	    		if (pointsString.contains("bbe$bebb")
	    				|| pointsString.contains("bbeb$ebb")
	    				|| pointsString.contains("bbbb$b")
	    				|| pointsString.contains("bbb$bb")
	    				|| pointsString.contains("bb$bbb")
	    				|| pointsString.contains("b$bbbb")){
	    			return FORBIDDEN;
	    		}
	    	}
	    	
			return COMMON;
		}
		
		protected String convertToString(int[] leftPoints,String point,int[] rightPointst){
	    	String pointsString = "";
	    	for (int i= leftPoints.length -1; i >= 0; i--){//reverse left array
	    		if (ChessType.BLACK == leftPoints[i]){
	    			pointsString += "b";
	    		}else{
	    			assert ChessType.EMPTY == leftPoints[i];
	    			pointsString += "e";
	    		}
	    	}
	    	pointsString += point;
	    	for (int i=0; i < rightPointst.length; i++){
	    		if (ChessType.BLACK == rightPointst[i]){
	    			pointsString += "b";
	    		}else{
	    			assert ChessType.EMPTY == rightPointst[i];
	    			pointsString += "e";
	    		}
	    	}
			return pointsString;
		}
		
		protected int getBalckCountBeforeTwoEmpty(int[] points){
			int count = 0;
	    	for (int i = 0; i < points.length; i++){
	    		if (ChessType.BLACK == points[i]){
	    			count++;
	    		}else{
	    			assert ChessType.EMPTY == points[i];
	    			if ( i > 0 && points[i-1] ==  ChessType.EMPTY){
	    				break;
	    			}
	    		}
	    	}
	    	return count;
			
		}
}
