package funny.gobang.service;


import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import funny.gobang.AppConstants;
import funny.gobang.model.ChessBoard;
import funny.gobang.model.ChessType;
import funny.gobang.model.Point;

import static funny.gobang.model.ChessType.BLACK;
import static funny.gobang.model.ChessType.EMPTY;
import static funny.gobang.model.ChessType.WHITE;
import static funny.gobang.service.ForbiddenServicePatternImpl.*;

public class ForbiddenServicePatternImplTest {

	
	ForbiddenServicePatternImpl service = new ForbiddenServicePatternImpl();
	
	
	Point delta = AppConstants.directons[0];
	
	Point delta1 = AppConstants.directons[1];
	
	Point targetPoint = new Point(7,7);
	
	
	
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
	
	@Test
	public void testGetPattern(){
		int expectedPattern = CONTINOUS_LIVE_THREE;
		testGetPattern("ee$bbeeb",expectedPattern);
	}
	
	
	@Test
	public void testPeeker(){
		ChessBoard board = buildBoard("ee$bbeeb");
		Peeker peeker = new ForbiddenServicePatternImpl.Peeker(board,delta);
		
		Point startPoint = targetPoint;
		peeker.peek(true, startPoint);
		
		assertEquals(ChessType.EMPTY, peeker.type);
		assertEquals(1, peeker.count);
		
		startPoint = peeker.endPoint;
		peeker.peek(true, startPoint);
		
		assertEquals(ChessType.EMPTY, peeker.type);
		assertEquals(1, peeker.count);
		
		startPoint = targetPoint;
		peeker.peek(false, startPoint);
		
		assertEquals(ChessType.BLACK, peeker.type);
		assertEquals(1, peeker.count);
		
		startPoint = targetPoint;
		peeker.peekSpecific(false, startPoint,ChessType.BLACK);
		
		assertEquals(ChessType.BLACK, peeker.type);
		assertEquals(2, peeker.count);
		
		
		startPoint = targetPoint;
		peeker.peekSpecificCount(false, startPoint,ChessType.BLACK,3);
		
		assertEquals(-999, peeker.type);
		assertEquals(0, peeker.count);
		
	}
	
	
	
	public void testGetPattern(String line,int expected){
		ChessBoard board = buildBoard(line);
		int pattern = service.getDerictionPattern(board, targetPoint, delta);
		
		assertEquals(expected, pattern);
	}
	
	
	@Rule
	public TestName name= new TestName();
	
	public void testGetDirectionPattern(String chessTypes, int exceptedPattern){
		ChessBoard chessBoard = buildBoard(chessTypes);
		
		int pattern = service.getDerictionPattern(chessBoard, targetPoint,delta);
		String message = "Checking: "+chessTypes +",Expected Pattern: "+ exceptedPattern;
		System.out.println(message);
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
	
	public void isForbidden(String chessTypes1,String chessTypes2, boolean expected){
		isForbidden(chessTypes1, chessTypes2,  expected,"");
	}

	public void isForbidden(String chessTypes1,String chessTypes2, boolean expected,String as){
		ChessBoard board = new ChessBoard(15);
		downChessBoard(board, chessTypes1, targetPoint,AppConstants.directons[0]);
		downChessBoard(board, chessTypes2, targetPoint,AppConstants.directons[3]);
		String message = "one:"+  chessTypes1+" ,two: " +chessTypes2 +" should be " + (expected ? "forbidden" : "not forbidden") +" "+ as;
		System.out.println(message);
		assertEquals(message ,expected, service.isForbiddenPoint(board, targetPoint));
	}
	
	@Test
	public void testContinousThree() {
		testGetDirectionPattern("b$b",ForbiddenServicePatternImpl.CONTINOUS_LIVE_THREE);
	}


	@Test
	public void testJUMPThree() {
		testGetDirectionPattern("be$b",ForbiddenServicePatternImpl.JUMP_LIVE_THREE);
	}
	
	@Test
	public void testSleepThree() {
		testGetDirectionPattern("b$bw",ForbiddenServicePatternImpl.SLEEP_THREE);
		testGetDirectionPattern("be$bw",ForbiddenServicePatternImpl.SLEEP_THREE);
	}
	@Test
	public void testFightFour() {
		testGetDirectionPattern("bbbe$bebb",ForbiddenServicePatternImpl.FIGHT_FOUR);
		testGetDirectionPattern("bbbe$e",ForbiddenServicePatternImpl.FIGHT_FOUR);
	}
	
	
	@Test
	public void testDeadThree() {
		testGetDirectionPattern("wbe$bw",ForbiddenServicePatternImpl.DEAD_THREE);
	}
	
	@Test
	public void testForbidden() {
		testGetDirectionPattern("beb$beb",ForbiddenServicePatternImpl.FORBIDDEN);
		testGetDirectionPattern("bbe$bebb",ForbiddenServicePatternImpl.FORBIDDEN);
		
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
