package funny.gobang.rule.common;

import static funny.gobang.model.ChessType.*;
import static org.junit.Assert.*;
import static funny.gobang.rule.ChessPatterns.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TestName;

import funny.gobang.AppConstants;
import funny.gobang.model.ChessBoard;
import funny.gobang.model.Point;
import funny.gobang.rule.ChessPatterns;
import funny.gobang.rule.MyCategories.*;

public class ForbiddenChessPatternResolverImplTest {

	
	ForbiddenChessPatternResolverImpl resolver = new ForbiddenChessPatternResolverImpl();
	
	Point delta = AppConstants.directons[0];
	
	Point delta1 = AppConstants.directons[1];
	
	Point targetPoint = new Point(7,7);
	
	
	@Rule
	public TestName name= new TestName();
	
	
	@Before
	public void before(){
		System.out.println("testName:" + name.getMethodName());
	}
	
	@After
	public void after(){
		System.out.println("----------------------");
	}
	
	public  ChessBoard buildBoard(String chessTypes){
		ChessBoard board = new ChessBoard(15);
		downChessBoard(board, chessTypes, targetPoint,delta);
		
		return board;
	}
	

	
	public void testAssumeBlackPattern(String chessTypes, ChessPatterns exceptedPattern){
		testAssumePattern(true, chessTypes, exceptedPattern);
	}
	
	public void testAssumeWhitePattern(String chessTypes, ChessPatterns exceptedPattern){
		testAssumePattern(false, chessTypes, exceptedPattern);
	}
	
	
	private void testAssumePattern(boolean blackOrWhite,String chessTypes, ChessPatterns exceptedPattern){
		ChessBoard chessBoard = buildBoard(chessTypes);
		String message = "Checking: "+chessTypes +",Expected Pattern: "+ exceptedPattern;
		System.out.println(message);
		
		ChessPatterns pattern = resolver.assumeChessPattern(chessBoard, targetPoint, delta, blackOrWhite);
	
		assertEquals(message ,exceptedPattern, pattern);
	}
	



	private static Point buildPoint(Point point,Point delta, int mutiple){
		return new Point(  point.getX() + delta.getX() * mutiple ,  point.getY() + delta.getY() * mutiple);
	}
	
	public static void downChessBoard(ChessBoard board,String chessTypes,Point point, Point delta){
		
		int indexCheckPoint = chessTypes.indexOf('$');
		if (indexCheckPoint < 0){
			throw new RuntimeException("don't contain $");
		}
		
		for (int i = indexCheckPoint -1; i >= 0 && i < indexCheckPoint; i--){
			Point downPoint = buildPoint(point, delta, i - indexCheckPoint);
			char c = chessTypes.charAt(i);
			int chessType = -99;
			if (c == 'b'){
				chessType = BLACK;
			}else if ( c== 'e'){
				chessType = EMPTY;
			}else if (c == 'w'){
				chessType = WHITE;
			}else if ( c == '$'){
				chessType = EMPTY;
			}else {
				throw new RuntimeException("error chess type");
			}
			board.downChess(downPoint, chessType);
		}
		
		for (int i = indexCheckPoint + 1; i >= 0 && i < chessTypes.length(); i++){
			Point downPoint = buildPoint(point, delta, i - indexCheckPoint);
			char c = chessTypes.charAt(i);
			int chessType = -99;
			if (c == 'b'){
				chessType = BLACK;
			}else if ( c== 'e'){
				chessType = EMPTY;
			}else if (c == 'w'){
				chessType = WHITE;
			}else if ( c == '$'){
				chessType = EMPTY;
			}else {
				throw new RuntimeException("error chess type");
			}
			board.downChess(downPoint, chessType);
		}
		
	}
	

	@Test
	@Category(Black.class)
	public void testAssumeOneBlackPattern(){
		
		/*
		 *  one
		 */
		testAssumeBlackPattern("$",UNKNOWN);
		testAssumeBlackPattern("$eb",UNKNOWN);
		testAssumeBlackPattern("$ebb",JUMP_LIVE_THREE);
		testAssumeBlackPattern("$ebw",UNKNOWN);
		testAssumeBlackPattern("$ebbb",FIGHT_FOUR);
		testAssumeBlackPattern("$ebbbw",FIGHT_FOUR);
		testAssumeBlackPattern("$ebbbb",UNKNOWN);
		
		testAssumeBlackPattern("w$eb",UNKNOWN);
		testAssumeBlackPattern("w$ebb",SLEEP_THREE);
		testAssumeBlackPattern("w$ebw",UNKNOWN);
		testAssumeBlackPattern("w$ebbb",FIGHT_FOUR);
		testAssumeBlackPattern("w$ebbbw",FIGHT_FOUR);
		testAssumeBlackPattern("w$ebbbb",UNKNOWN);
		
		testAssumeBlackPattern("we$eb",UNKNOWN);
		testAssumeBlackPattern("we$ebb",JUMP_LIVE_THREE);
		testAssumeBlackPattern("we$ebw",UNKNOWN);
		testAssumeBlackPattern("we$ebbb",FIGHT_FOUR);
		testAssumeBlackPattern("we$ebbbw",FIGHT_FOUR);
		testAssumeBlackPattern("we$ebbbb",UNKNOWN);
		
		testAssumeBlackPattern("be$eb",UNKNOWN);
		testAssumeBlackPattern("be$ebb",SLEEP_THREE);
		testAssumeBlackPattern("be$ebw",UNKNOWN);
		testAssumeBlackPattern("be$ebbb",FIGHT_FOUR);
		testAssumeBlackPattern("be$ebbbw",FIGHT_FOUR);
		testAssumeBlackPattern("be$ebbbb",UNKNOWN);// not set
		
		testAssumeBlackPattern("bbe$eb",SLEEP_THREE);
		testAssumeBlackPattern("bbe$ebb",SLEEP_THREE);
		testAssumeBlackPattern("bbe$ebw",SLEEP_THREE);
		testAssumeBlackPattern("bbe$ebbb",FIGHT_FOUR);
		testAssumeBlackPattern("bbe$ebbbw",FIGHT_FOUR);
		
		testAssumeBlackPattern("bbbe$eb",FIGHT_FOUR);
		testAssumeBlackPattern("bbbe$ebb",FIGHT_FOUR);
		testAssumeBlackPattern("bbbe$ebw",FIGHT_FOUR);
		testAssumeBlackPattern("bbbe$ebbb",DOUBLE_FIGHT_FOUR);
		testAssumeBlackPattern("bbbe$ebbbw",DOUBLE_FIGHT_FOUR);
		
	}
	
	@Test
	@Category(Black.class)
	public void testAssumeTwoBlackPattern(){
		/*
		 * tbo
		 */
		testAssumeBlackPattern("b$",UNKNOWN);
		testAssumeBlackPattern("b$eb",JUMP_LIVE_THREE);
		testAssumeBlackPattern("b$ew",UNKNOWN);
		testAssumeBlackPattern("b$ebw",SLEEP_THREE);
		testAssumeBlackPattern("b$ebb",FIGHT_FOUR);
		testAssumeBlackPattern("b$ebbw",FIGHT_FOUR);
		testAssumeBlackPattern("b$ebbb",UNKNOWN);
		testAssumeBlackPattern("b$ebbbw",UNKNOWN);
		
		testAssumeBlackPattern("wb$e",UNKNOWN);
		testAssumeBlackPattern("wb$eb",SLEEP_THREE);
		testAssumeBlackPattern("wb$ew",UNKNOWN);
		testAssumeBlackPattern("wb$ebb",FIGHT_FOUR);
		testAssumeBlackPattern("wb$ebw",DEAD_THREE);
		testAssumeBlackPattern("wb$ebbb",UNKNOWN);
		testAssumeBlackPattern("wb$ebbbw",UNKNOWN);
		testAssumeBlackPattern("wb$ebbbb",UNKNOWN);
		
		testAssumeBlackPattern("beb$eb",DOUBLE_LIVE_THREE);
		testAssumeBlackPattern("beb$ew",JUMP_LIVE_THREE);
		testAssumeBlackPattern("beb$ebb",FIGHT_FOUR);
		testAssumeBlackPattern("beb$ebw",SLEEP_THREE);
		testAssumeBlackPattern("beb$ebbb",SLEEP_THREE);
		testAssumeBlackPattern("beb$ebbbw",SLEEP_THREE);
		testAssumeBlackPattern("beb$ebbbb",SLEEP_THREE);
		
		testAssumeBlackPattern("wbeb$eb",SLEEP_THREE);
		testAssumeBlackPattern("wbeb$ew",SLEEP_THREE);
		testAssumeBlackPattern("wbeb$ebb",FIGHT_FOUR);
		testAssumeBlackPattern("wbeb$ebw",DEAD_THREE);
		testAssumeBlackPattern("wbeb$ebbb",DEAD_THREE);
		testAssumeBlackPattern("wbeb$ebbbw",DEAD_THREE);
		testAssumeBlackPattern("wbeb$ebbbb",DEAD_THREE);
		
		testAssumeBlackPattern("bbeb$eb",FIGHT_FOUR);
		testAssumeBlackPattern("bbeb$ew",FIGHT_FOUR);
		testAssumeBlackPattern("bbeb$ebb",DOUBLE_FIGHT_FOUR);
		testAssumeBlackPattern("bbeb$ebw",FIGHT_FOUR);
		testAssumeBlackPattern("bbeb$ebbb",FIGHT_FOUR);
		testAssumeBlackPattern("bbeb$ebbbw",FIGHT_FOUR);
		testAssumeBlackPattern("bbeb$ebbbb",FIGHT_FOUR);
		
		testAssumeBlackPattern("wbbeb$eb",FIGHT_FOUR);
		testAssumeBlackPattern("wbbeb$ew",FIGHT_FOUR);
		testAssumeBlackPattern("wbbeb$ebb",DOUBLE_FIGHT_FOUR);
		testAssumeBlackPattern("wbbeb$ebw",FIGHT_FOUR);
		testAssumeBlackPattern("wbbeb$ebbb",FIGHT_FOUR);
		testAssumeBlackPattern("wbbeb$ebbbw",FIGHT_FOUR);
		testAssumeBlackPattern("wbbeb$ebbbb",FIGHT_FOUR);
		
		testAssumeBlackPattern("bbbeb$eb",SLEEP_THREE);
		testAssumeBlackPattern("bbbeb$ew",UNKNOWN);
		testAssumeBlackPattern("bbbeb$ebb",FIGHT_FOUR);
		testAssumeBlackPattern("bbbeb$ebw",DEAD_THREE);
		testAssumeBlackPattern("bbbeb$ebbb",UNKNOWN);
		testAssumeBlackPattern("bbbeb$ebbbw",UNKNOWN);
		testAssumeBlackPattern("bbbeb$ebbbb",UNKNOWN);
		
		
	}
	
	@Test
	@Category(Black.class)
	public void testAssumeThreeBlackPattern(){
		/*
		 * three
		 */
		testAssumeBlackPattern("bb$",CONTINOUS_LIVE_THREE);
		testAssumeBlackPattern("b$b",CONTINOUS_LIVE_THREE);
		
		testAssumeBlackPattern("bb$eb",FIGHT_FOUR);
		testAssumeBlackPattern("bb$ew",CONTINOUS_LIVE_THREE);
		testAssumeBlackPattern("bb$ebw",FIGHT_FOUR);
		testAssumeBlackPattern("bb$ebb",SLEEP_THREE);
		testAssumeBlackPattern("bb$ebbw",SLEEP_THREE);
		testAssumeBlackPattern("bb$ebbb",SLEEP_THREE);
		testAssumeBlackPattern("bb$ebbbw",SLEEP_THREE);
		
		testAssumeBlackPattern("wbb$e",SLEEP_THREE);
		testAssumeBlackPattern("wbb$ew",DEAD_THREE);
		testAssumeBlackPattern("wbb$eb",FIGHT_FOUR);
		testAssumeBlackPattern("wbb$ebb",DEAD_THREE);
		testAssumeBlackPattern("wbb$ebw",FIGHT_FOUR);
		testAssumeBlackPattern("wbb$ebbb",DEAD_THREE);
		testAssumeBlackPattern("wbb$ebbbw",DEAD_THREE);
		testAssumeBlackPattern("wbb$ebbbb",DEAD_THREE);
		
		testAssumeBlackPattern("bebb$e",FIGHT_FOUR);
		testAssumeBlackPattern("bebb$eb",DOUBLE_FIGHT_FOUR);
		testAssumeBlackPattern("bebb$ew",FIGHT_FOUR);
		testAssumeBlackPattern("bebb$ebb",FIGHT_FOUR);
		testAssumeBlackPattern("bebb$ebw",DOUBLE_FIGHT_FOUR);
		testAssumeBlackPattern("bebb$ebbb",FIGHT_FOUR);
		testAssumeBlackPattern("bebb$ebbbw",FIGHT_FOUR);
		testAssumeBlackPattern("bebb$ebbbb",FIGHT_FOUR);
		
		testAssumeBlackPattern("wbebb$e",FIGHT_FOUR);
		testAssumeBlackPattern("wbebb$eb",DOUBLE_FIGHT_FOUR);
		testAssumeBlackPattern("wbebb$ew",FIGHT_FOUR);
		testAssumeBlackPattern("wbebb$ebb",FIGHT_FOUR);
		testAssumeBlackPattern("wbebb$ebw",DOUBLE_FIGHT_FOUR);
		testAssumeBlackPattern("wbebb$ebbb",FIGHT_FOUR);
		testAssumeBlackPattern("wbebb$ebbbw",FIGHT_FOUR);
		testAssumeBlackPattern("wbebb$ebbbb",FIGHT_FOUR);
		
		testAssumeBlackPattern("bbebb$e",SLEEP_THREE);
		testAssumeBlackPattern("bbebb$eb",FIGHT_FOUR);
		testAssumeBlackPattern("bbebb$ew",DEAD_THREE);
		testAssumeBlackPattern("bbebb$ebb",DEAD_THREE);
		testAssumeBlackPattern("bbebb$ebw",FIGHT_FOUR);
		testAssumeBlackPattern("bbebb$ebbb",DEAD_THREE);
		testAssumeBlackPattern("bbebb$ebbbw",DEAD_THREE);
		testAssumeBlackPattern("bbebb$ebbbb",DEAD_THREE);
		
		testAssumeBlackPattern("wbbebb$e",SLEEP_THREE);
		testAssumeBlackPattern("wbbebb$eb",FIGHT_FOUR);
		testAssumeBlackPattern("wbbebb$ew",DEAD_THREE);
		testAssumeBlackPattern("wbbebb$ebb",DEAD_THREE);
		testAssumeBlackPattern("wbbebb$ebw",FIGHT_FOUR);
		testAssumeBlackPattern("wbbebb$ebbb",DEAD_THREE);
		testAssumeBlackPattern("wbbebb$ebbbw",DEAD_THREE);
		testAssumeBlackPattern("wbbebb$ebbbb",DEAD_THREE);
		
		testAssumeBlackPattern("bbbebb$e",SLEEP_THREE);
		testAssumeBlackPattern("bbbebb$eb",FIGHT_FOUR);
		testAssumeBlackPattern("bbbebb$ew",DEAD_THREE);
		testAssumeBlackPattern("bbbebb$ebb",DEAD_THREE);
		testAssumeBlackPattern("bbbebb$ebw",FIGHT_FOUR);
		testAssumeBlackPattern("bbbebb$ebbb",DEAD_THREE);
		testAssumeBlackPattern("bbbebb$ebbbw",DEAD_THREE);//???
		testAssumeBlackPattern("bbbebb$ebbbb",DEAD_THREE);//???
		
	}
	
	@Test
	@Category(Black.class)
	public void testAssumeFourBlackPattern(){
		/*
		 * four
		 */
		
		testAssumeBlackPattern("bbb$",LIVE_FOUR);
		testAssumeBlackPattern("bb$b",LIVE_FOUR);
		testAssumeBlackPattern("b$bb",LIVE_FOUR);
		testAssumeBlackPattern("$bbb",LIVE_FOUR);
		
		testAssumeBlackPattern("webbb$ew",LIVE_FOUR);
		testAssumeBlackPattern("webb$bew",LIVE_FOUR);
		testAssumeBlackPattern("web$bbew",LIVE_FOUR);
		testAssumeBlackPattern("we$bbbew",LIVE_FOUR);
		
		testAssumeBlackPattern("bebbb$ew",FIGHT_FOUR);
		testAssumeBlackPattern("bebb$bew",FIGHT_FOUR);
		testAssumeBlackPattern("beb$bbew",FIGHT_FOUR);
		testAssumeBlackPattern("be$bbbew",FIGHT_FOUR);
		
		testAssumeBlackPattern("bebbb$eb",DEAD_FOUR);
		testAssumeBlackPattern("bebb$beb",DEAD_FOUR);
		testAssumeBlackPattern("beb$bbeb",DEAD_FOUR);
		testAssumeBlackPattern("be$bbbeb",DEAD_FOUR);
		
		testAssumeBlackPattern("wbbb$",FIGHT_FOUR);
		testAssumeBlackPattern("wbb$b",FIGHT_FOUR);
		testAssumeBlackPattern("wb$bb",FIGHT_FOUR);
		testAssumeBlackPattern("w$bbb",FIGHT_FOUR);
		testAssumeBlackPattern("wbbb$w",DEAD_FOUR);
		testAssumeBlackPattern("wbb$bw",DEAD_FOUR);
		testAssumeBlackPattern("wb$bbw",DEAD_FOUR);
		testAssumeBlackPattern("w$bbbw",DEAD_FOUR);
		
		testAssumeBlackPattern("wbbb$ew",FIGHT_FOUR);
		testAssumeBlackPattern("wbb$bew",FIGHT_FOUR);
		testAssumeBlackPattern("wb$bbew",FIGHT_FOUR);
		testAssumeBlackPattern("w$bbbew",FIGHT_FOUR);
		
		testAssumeBlackPattern("wbbb$eb",DEAD_FOUR);
		testAssumeBlackPattern("wbb$beb",DEAD_FOUR);
		testAssumeBlackPattern("wb$bbeb",DEAD_FOUR);
		testAssumeBlackPattern("w$bbbeb",DEAD_FOUR);
		
	}
	
	@Test
	@Category(Black.class)
	public void testAssumeFiveBlackPattern(){
		/*
		 * FIVE
		 */
		testAssumeBlackPattern("b$bbb",FIVE);
		testAssumeBlackPattern("$bbbb",FIVE);
		testAssumeBlackPattern("bb$bb",FIVE);
		testAssumeBlackPattern("bbb$b",FIVE);
		testAssumeBlackPattern("bbbb$",FIVE);
		
		testAssumeBlackPattern("wb$bbb",FIVE);
		testAssumeBlackPattern("w$bbbb",FIVE);
		testAssumeBlackPattern("wbb$bb",FIVE);
		testAssumeBlackPattern("wbbb$b",FIVE);
		testAssumeBlackPattern("wbbbb$",FIVE);
		
		testAssumeBlackPattern("wb$bbbw",FIVE);
		testAssumeBlackPattern("w$bbbbw",FIVE);
		testAssumeBlackPattern("wbb$bbw",FIVE);
		testAssumeBlackPattern("wbbb$bw",FIVE);
		testAssumeBlackPattern("wbbbb$w",FIVE);
		
	}
	
	@Test
	@Category(Black.class)
	public void testAssumeMoreThanFiveBlackPattern(){
		/*
		 * more than five
		 */
		testAssumeBlackPattern("b$bbbb",MORE_THAN_FIVE);
		testAssumeBlackPattern("bb$bbb",MORE_THAN_FIVE);
		testAssumeBlackPattern("bbb$bb",MORE_THAN_FIVE);
		testAssumeBlackPattern("bbbb$b",MORE_THAN_FIVE);
	}
	
	
	@Test
	@Category(White.class)
	public void testAssumeOneWhitePattern(){
		
		/*
		 *  one
		 */
		testAssumeWhitePattern("$",UNKNOWN);
		testAssumeWhitePattern("$ew",UNKNOWN);
		testAssumeWhitePattern("$eww",JUMP_LIVE_THREE);
		testAssumeWhitePattern("$ewb",UNKNOWN);
		testAssumeWhitePattern("$ewww",FIGHT_FOUR);
		testAssumeWhitePattern("$ewwwb",FIGHT_FOUR);
		testAssumeWhitePattern("$ewwww",UNKNOWN);
		
		testAssumeWhitePattern("b$ew",UNKNOWN);
		testAssumeWhitePattern("b$eww",SLEEP_THREE);
		testAssumeWhitePattern("b$ewb",UNKNOWN);
		testAssumeWhitePattern("b$ewww",FIGHT_FOUR);
		testAssumeWhitePattern("b$ewwwb",FIGHT_FOUR);
		testAssumeWhitePattern("b$ewwww",UNKNOWN);
		
		testAssumeWhitePattern("be$ew",UNKNOWN);
		testAssumeWhitePattern("be$eww",JUMP_LIVE_THREE);
		testAssumeWhitePattern("be$ewb",UNKNOWN);
		testAssumeWhitePattern("be$ewww",FIGHT_FOUR);
		testAssumeWhitePattern("be$ewwwb",FIGHT_FOUR);
		testAssumeWhitePattern("be$ewwww",UNKNOWN);
		
		testAssumeWhitePattern("we$ew",UNKNOWN);
		testAssumeWhitePattern("we$eww",JUMP_LIVE_THREE);
		testAssumeWhitePattern("we$ewb",UNKNOWN);
		testAssumeWhitePattern("we$ewww",FIGHT_FOUR);
		testAssumeWhitePattern("we$ewwwb",FIGHT_FOUR);
		testAssumeWhitePattern("we$ewwww",UNKNOWN);// not set
		
		testAssumeWhitePattern("wwe$ew",JUMP_LIVE_THREE);
		testAssumeWhitePattern("wwe$eww",DOUBLE_LIVE_THREE);
		testAssumeWhitePattern("wwe$ewb",JUMP_LIVE_THREE);
		testAssumeWhitePattern("wwe$ewww",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("wwe$ewwwb",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("wwe$ewwww",LIVE_THREE_FIGHT_FOUR);
		
		testAssumeWhitePattern("wwwe$ew",FIGHT_FOUR);
		testAssumeWhitePattern("wwwe$eww",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("wwwe$ewb",FIGHT_FOUR);
		testAssumeWhitePattern("wwwe$ewww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wwwe$ewwwb",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wwwe$ewwww",DOUBLE_FIGHT_FOUR);
		
	}
	
	@Test
	@Category(White.class)
	public void testAssumeTwoWhitePattern(){
		/*
		 * two
		 */
		testAssumeWhitePattern("w$",UNKNOWN);
		testAssumeWhitePattern("w$ew",JUMP_LIVE_THREE);
		testAssumeWhitePattern("w$eb",UNKNOWN);
		testAssumeWhitePattern("w$ewb",SLEEP_THREE);
		testAssumeWhitePattern("w$eww",FIGHT_FOUR);
		testAssumeWhitePattern("w$ewwb",FIGHT_FOUR);
		testAssumeWhitePattern("w$ewww",FIGHT_FOUR);
		testAssumeWhitePattern("w$ewwwb",FIGHT_FOUR);
		
		testAssumeWhitePattern("bw$e",UNKNOWN);
		testAssumeWhitePattern("bw$ew",SLEEP_THREE);
		testAssumeWhitePattern("bw$eb",UNKNOWN);
		testAssumeWhitePattern("bw$eww",FIGHT_FOUR);
		testAssumeWhitePattern("bw$ewb",DEAD_THREE);
		testAssumeWhitePattern("bw$ewww",FIGHT_FOUR);
		testAssumeWhitePattern("bw$ewwwb",FIGHT_FOUR);
		testAssumeWhitePattern("bw$ewwww",FIGHT_FOUR);
		
		testAssumeWhitePattern("wew$ew",DOUBLE_LIVE_THREE);
		testAssumeWhitePattern("wew$eb",JUMP_LIVE_THREE);
		testAssumeWhitePattern("wew$eww",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("wew$ewb",JUMP_LIVE_THREE);
		testAssumeWhitePattern("wew$ewww",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("wew$ewwwb",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("wew$ewwww",LIVE_THREE_FIGHT_FOUR);
		
		testAssumeWhitePattern("bwew$ew",JUMP_LIVE_THREE);
		testAssumeWhitePattern("bwew$eb",SLEEP_THREE);
		testAssumeWhitePattern("bwew$eww",FIGHT_FOUR);
		testAssumeWhitePattern("bwew$ewb",SLEEP_THREE);
		testAssumeWhitePattern("bwew$ewww",FIGHT_FOUR);
		testAssumeWhitePattern("bwew$ewwwb",FIGHT_FOUR);
		testAssumeWhitePattern("bwew$ewwww",FIGHT_FOUR);
		
		testAssumeWhitePattern("wwew$ew",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("wwew$eb",FIGHT_FOUR);
		testAssumeWhitePattern("wwew$eww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wwew$ewb",FIGHT_FOUR);
		testAssumeWhitePattern("wwew$ewww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wwew$ewwwb",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wwew$ewwww",DOUBLE_FIGHT_FOUR);
		
		testAssumeWhitePattern("bwwew$ew",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("bwwew$eb",FIGHT_FOUR);
		testAssumeWhitePattern("bwwew$eww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("bwwew$ewb",FIGHT_FOUR);
		testAssumeWhitePattern("bwwew$ewww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("bwwew$ewwwb",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("bwwew$ewwww",DOUBLE_FIGHT_FOUR);
		
		testAssumeWhitePattern("wwwew$ew",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("wwwew$eb",FIGHT_FOUR);
		testAssumeWhitePattern("wwwew$eww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wwwew$ewb",FIGHT_FOUR);
		testAssumeWhitePattern("wwwew$ewww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wwwew$ewwwb",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wwwew$ewwww",DOUBLE_FIGHT_FOUR);
		
		
	}
	
	@Test
	@Category(White.class)
	public void testAssumeThreeWhitePattern(){
		/*
		 * three
		 */
		testAssumeWhitePattern("ww$",CONTINOUS_LIVE_THREE);
		testAssumeWhitePattern("w$w",CONTINOUS_LIVE_THREE);
		
		testAssumeWhitePattern("ww$ew",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("ww$eb",CONTINOUS_LIVE_THREE);
		testAssumeWhitePattern("ww$ewb",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("ww$eww",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("ww$ewwb",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("ww$ewww",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("ww$ewwwb",LIVE_THREE_FIGHT_FOUR);
		
		testAssumeWhitePattern("bww$e",SLEEP_THREE);
		testAssumeWhitePattern("bww$eb",DEAD_THREE);
		testAssumeWhitePattern("bww$ew",FIGHT_FOUR);
		testAssumeWhitePattern("bww$eww",FIGHT_FOUR);
		testAssumeWhitePattern("bww$ewb",FIGHT_FOUR);
		testAssumeWhitePattern("bww$ewww",FIGHT_FOUR);
		testAssumeWhitePattern("bww$ewwwb",FIGHT_FOUR);
		testAssumeWhitePattern("bww$ewwww",FIGHT_FOUR);
		
		testAssumeWhitePattern("weww$e",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("weww$ew",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("weww$eb",FIGHT_FOUR);
		testAssumeWhitePattern("weww$eww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("weww$ewb",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("weww$ewww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("weww$ewwwb",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("weww$ewwww",DOUBLE_FIGHT_FOUR);
		
		testAssumeWhitePattern("bweww$e",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("bweww$ew",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("bweww$eb",FIGHT_FOUR);
		testAssumeWhitePattern("bweww$eww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("bweww$ewb",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("bweww$ewww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("bweww$ewwwb",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("bweww$ewwww",DOUBLE_FIGHT_FOUR);
		
		testAssumeWhitePattern("wweww$e",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("wweww$ew",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wweww$eb",FIGHT_FOUR);
		testAssumeWhitePattern("wweww$eww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wweww$ewb",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wweww$ewww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wweww$ewwwb",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wweww$ewwww",DOUBLE_FIGHT_FOUR);
		
		testAssumeWhitePattern("bwweww$e",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("bwweww$ew",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("bwweww$eb",FIGHT_FOUR);
		testAssumeWhitePattern("bwweww$eww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("bwweww$ewb",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("bwweww$ewww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("bwweww$ewwwb",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("bwweww$ewwww",DOUBLE_FIGHT_FOUR);
		
		testAssumeWhitePattern("wwweww$e",LIVE_THREE_FIGHT_FOUR);
		testAssumeWhitePattern("wwweww$ew",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wwweww$eb",FIGHT_FOUR);
		testAssumeWhitePattern("wwweww$eww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wwweww$ewb",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wwweww$ewww",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wwweww$ewwwb",DOUBLE_FIGHT_FOUR);
		testAssumeWhitePattern("wwweww$ewwww",DOUBLE_FIGHT_FOUR);
		
		
	}
	
	@Test
	@Category(White.class)
	public void testAssumeFourWhitePattern(){
		/*
		 * four
		 */
		
		testAssumeWhitePattern("www$",LIVE_FOUR);
		testAssumeWhitePattern("ww$w",LIVE_FOUR);
		testAssumeWhitePattern("w$ww",LIVE_FOUR);
		testAssumeWhitePattern("$www",LIVE_FOUR);
		
		testAssumeWhitePattern("bewww$eb",LIVE_FOUR);
		testAssumeWhitePattern("beww$web",LIVE_FOUR);
		testAssumeWhitePattern("bew$wweb",LIVE_FOUR);
		testAssumeWhitePattern("be$wwweb",LIVE_FOUR);
		
		testAssumeWhitePattern("wewww$eb",LIVE_FOUR);
		testAssumeWhitePattern("weww$web",LIVE_FOUR);
		testAssumeWhitePattern("wew$wweb",LIVE_FOUR);
		testAssumeWhitePattern("we$wwweb",LIVE_FOUR);
		
		testAssumeWhitePattern("wewww$ew",LIVE_FOUR);
		testAssumeWhitePattern("weww$wew",LIVE_FOUR);
		testAssumeWhitePattern("wew$wwew",LIVE_FOUR);
		testAssumeWhitePattern("we$wwwew",LIVE_FOUR);
		
		testAssumeWhitePattern("bwww$",FIGHT_FOUR);
		testAssumeWhitePattern("bww$w",FIGHT_FOUR);
		testAssumeWhitePattern("bw$ww",FIGHT_FOUR);
		testAssumeWhitePattern("b$www",FIGHT_FOUR);
		testAssumeWhitePattern("bwww$b",DEAD_FOUR);
		testAssumeWhitePattern("bww$wb",DEAD_FOUR);
		testAssumeWhitePattern("bw$wwb",DEAD_FOUR);
		testAssumeWhitePattern("b$wwwb",DEAD_FOUR);
		
		testAssumeWhitePattern("bwww$eb",FIGHT_FOUR);
		testAssumeWhitePattern("bww$web",FIGHT_FOUR);
		testAssumeWhitePattern("bw$wweb",FIGHT_FOUR);
		testAssumeWhitePattern("b$wwweb",FIGHT_FOUR);
		
		testAssumeWhitePattern("bwww$ew",FIGHT_FOUR);
		testAssumeWhitePattern("bww$wew",FIGHT_FOUR);
		testAssumeWhitePattern("bw$wwew",FIGHT_FOUR);
		testAssumeWhitePattern("b$wwwew",FIGHT_FOUR);
		
		
		
	}
	
	@Test
	@Category(White.class)
	public void testAssumeFiveWhitePattern(){
		/*
		 * FIVE
		 */
		testAssumeWhitePattern("w$www",FIVE);
		testAssumeWhitePattern("$wwww",FIVE);
		testAssumeWhitePattern("ww$ww",FIVE);
		testAssumeWhitePattern("www$w",FIVE);
		testAssumeWhitePattern("wwww$",FIVE);
		
		testAssumeWhitePattern("bw$www",FIVE);
		testAssumeWhitePattern("b$wwww",FIVE);
		testAssumeWhitePattern("bww$ww",FIVE);
		testAssumeWhitePattern("bwww$w",FIVE);
		testAssumeWhitePattern("bwwww$",FIVE);
		
		testAssumeWhitePattern("bw$wwwb",FIVE);
		testAssumeWhitePattern("b$wwwwb",FIVE);
		testAssumeWhitePattern("bww$wwb",FIVE);
		testAssumeWhitePattern("bwww$wb",FIVE);
		testAssumeWhitePattern("bwwww$b",FIVE);
		
		
	}
	
	@Test
	@Category(White.class)
	public void testAssumeMoreThanFiveWhitePattern(){
		/*
		 * more than five
		 */
		testAssumeWhitePattern("w$wwww",MORE_THAN_FIVE);
		testAssumeWhitePattern("ww$www",MORE_THAN_FIVE);
		testAssumeWhitePattern("www$ww",MORE_THAN_FIVE);
		testAssumeWhitePattern("wwww$w",MORE_THAN_FIVE);
	}

	public void isForbidden(String chessTypes1,String chessTypes2, boolean expected){
		isForbidden(chessTypes1, chessTypes2,  expected,"");
	}

	public void isForbidden(String chessTypes1,String chessTypes2, boolean expected,String as){
		ChessBoard board = new ChessBoard(15);
		downChessBoard(board, chessTypes1, targetPoint,AppConstants.directons[0]);
		downChessBoard(board, chessTypes2, targetPoint,AppConstants.directons[3]);
		String message = "one:"+  chessTypes1+" ,two: " +chessTypes2 +" should be " + (expected ? "forbidden" : "not forbidden") +" "+ as;
		System.out.println(message);
		assertEquals(message ,expected, resolver.isForbiddenPoint(board, targetPoint));
	}
	
	
	@Test
	public void testIsForbidden() {
		isForbidden("b$b", "b$b", true);
		isForbidden("bb$", "$bb", true);
		
		isForbidden("b$b", "beb$", false,"as contious live three and jump live three is tolerant");
		isForbidden("b$b", "b$bw", false);
		isForbidden("b$b", "b$bb", false);
	}
	
}
