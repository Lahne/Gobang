package funny.gobang;

import java.io.IOException;
import java.util.List;

public class ChessBoard implements Cloneable{

	private final int[][] board;
	public final int capacity;
	
	public static final int Black = -1;
	public static final int White = 1;
	public static final int Blank = 0;
	
	public ChessBoard(int capacity){
		this.capacity = capacity;
		this.board = new int[capacity][capacity];
		clear();
	}
	
	private ChessBoard(int capacity, int[][] board){
		this.capacity = capacity;
		this.board = board;
	} 
	
	public synchronized int[][] getBoard() {
		int[][] old = board;
		final int[][] current = new int[capacity][capacity];
		for(int i=0; i<capacity; i++)
			  for(int j=0; j<capacity; j++)
				  current[i][j]=old[i][j];
		return current;
	}

	@Override
	public synchronized ChessBoard clone(){
		int[][] board = this.getBoard();
		return new ChessBoard(capacity, board);
	} 


	public int getCapacity() {
		return capacity;
	}

	public synchronized boolean canDown(Down down){
		int x = down.getX();
		int y = down.getY();
		return board[y][x] == Blank;
	}
	
	public synchronized boolean canDown(int x, int y){
		return board[y][x] == Blank;
	}

	public synchronized void clear(){
		for (int i=0; i < capacity; i++){
			for(int j=0; j < capacity; j++){
				board[i][j] = Blank;
			}
		}
	}
	
	public synchronized int remove(int x, int y){
		int chessMan = board[y][x];
		board[y][x] = Blank;
		return chessMan;
	}
	
	public synchronized int remove(Down down){
		int x = down.getX();
		int y = down.getY();
		return remove(x,y);
	}
	
	
	public synchronized boolean down(Down down){
		if (canDown(down)){
			int x = down.getX();
			int y = down.getY();
			board[y][x] = down.getChessMan();
			return true;
		}
		return false;
	}
	
	public synchronized boolean down(List<Down> downs){
		boolean ret = true;
		for (Down down : downs ){
			if (!down(down)){
				ret = false;
			}
		}
		return ret;
	}
	
	private synchronized void printXRow(int y){
		System.out.print(String.format("%2d|", y));
		for(int j=0; j < capacity; j++){
			String chess = "";
			if (board[y][j] == White){
				chess = "O";
			}else if (board[y][j] == Black){
				chess = "X";
			}else{
				chess = " ";
			}
			System.out.print(String.format("%2s|", chess));
		}
		System.out.println();
	}
	
	public synchronized void printCurrentBoard(){
		System.out.print(String.format("%2s|", ""));
		for (int i=0; i < capacity; i++){
			System.out.print(String.format("%2d|", i));
		}
		System.out.println();
		for (int i=0; i < capacity; i++){
			printXRow(i);
		}	
	}
	
	public static void main(String[] args) throws IOException{
		
		int capacity = 15;
		ChessBoard chessBoard = new ChessBoard(capacity);
		ReadHelper helper = new ReadHelper();
		int[] down = null;
		
		String nextChessMan = "Black";
		int nextChess = Black;
		chessBoard.printCurrentBoard();
		while( (down = helper.readNextDown(nextChessMan)) != null){
			int x = down[0];
			int y = down[1];
			if (x < 0 || x >= capacity || y < 0 || y >= capacity){
				System.out.println("Out of index, X and Y should >=0 && < "+capacity);
				continue;
			}
			if (chessBoard.down(new Down(x, y, nextChess))){
				nextChess = nextChess == Black ? White : Black;
				nextChessMan = nextChess == Black ? "Black":"White";
				chessBoard.printCurrentBoard();
			}else{
				System.out.println(String.format("Current Psition %d,%d has been setted, please choose a blank position",x,y));
			}
			
		}
		
		System.out.println("==========Finished=========");
		
	}
	
}
