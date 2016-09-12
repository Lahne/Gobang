package funny.gobang.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import funny.gobang.AppConstants;
import funny.gobang.model.ChessBoard;
import funny.gobang.model.Point;
import static funny.gobang.model.ChessType.*;
public class ForbiddenServiceSampleImplTest {
	
	ForbiddenServiceSampleImpl service = new ForbiddenServiceSampleImpl();

	@Rule
	public TestName name= new TestName();
	
	public void test(String chessTypes, int startX, int exceptedPattern,String message){
		ChessBoard chessBoard = new ChessBoard(15);
		
		Point downPoint = null;
		for (int i=0; i < chessTypes.length(); i++){
			char c = chessTypes.charAt(i);
			int chessType = -99;
			if (c == 'b'){
				chessType = BLACK;
			}else if ( c== 'e'){
				chessType = EMPTY;
			}else if (c == 'w'){
				chessType = WHITE;
			}else if ( c == '$'){
				downPoint = buildPoint(startX + i);
				chessType = EMPTY;
			}else {
				throw new RuntimeException("error chess type");
			}
			chessBoard.downChess(buildPoint(startX + i), chessType);
			
		}
		
		int pattern = service.getDerictionPattern(chessBoard, downPoint, AppConstants.directons[3]);
		
		assertEquals(message,exceptedPattern, pattern);
		
	}
	
	@Before
	public void before(){
		System.out.println("testName:" + name.getMethodName());
	}
	
	@After
	public void after(){
		System.out.println("----------------------");
	}
	
	public void test(String chessTypes, int startX, int exceptedPattern){
		test(chessTypes, startX, exceptedPattern, "");
	}
	private Point buildPoint(int x){
		return new Point(x, 0);
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
		ChessBoard board = new ChessBoard(15);
		Point checkPoint = new Point(7,7);
		downChessBoard(board, chessTypes1, checkPoint,AppConstants.directons[0]);
		downChessBoard(board, chessTypes2, checkPoint,AppConstants.directons[3]);
		assertEquals(expected, service.isForbiddenPoint(board, checkPoint));
	}

	@Test
	public void testContinousThree() {
		test("b$b",5,ForbiddenServiceSampleImpl.CONTINOUS_LIVE_THREE,"expect LIVE_THREE");
	}


	@Test
	public void testJUMPThree() {
		test("be$b",5,ForbiddenServiceSampleImpl.JUMP_LIVE_THREE);
	}
	
	@Test
	public void testCOMMON() {
		test("b$bw",5,ForbiddenServiceSampleImpl.COMMON);
	}
	
	@Test
	public void testCOMMON2() {
		test("be$bw",5,ForbiddenServiceSampleImpl.COMMON);
	}
	
	@Test
	public void testForbidden() {
		test("beb$beb",5,ForbiddenServiceSampleImpl.FORBIDDEN);
	}
	
	@Test
	public void testForbidden1() {
		test("bbe$bebb",5,ForbiddenServiceSampleImpl.FORBIDDEN);
	}
	
	@Test
	public void testIsForbidden1() {
		isForbidden("b$b", "b$b", true);
	}
	
	@Test
	public void testIsForbidden2() {
		isForbidden("b$b", "beb$", false);
	}
	
	@Test
	public void testIsForbidden3() {
		isForbidden("b$b", "b$bw", false);
	}
	
	@Test
	public void testIsForbidden4() {
		isForbidden("b$b", "b$bb", false);
	}
	
}
