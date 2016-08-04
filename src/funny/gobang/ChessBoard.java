package funny.gobang;

import java.io.IOException;

public class ChessBoard {

	private final int[][] board;
	public final int capacity;
	
	public static final int Black = -1;
	public static final int White = 1;
	public static final int Blank = 0;
	
	public ChessBoard(int capacity){
		this.capacity = capacity;
		this.board = new int[capacity][capacity];
	}
	
	
	public int[][] getBoard() {
		int[][] old = board;
		final int[][] current = new int[capacity][capacity];
		for(int i=0; i<capacity; i++)
			  for(int j=0; j<capacity; j++)
			    old[i][j]=current[i][j];
		return current;
	}



	public int getCapacity() {
		return capacity;
	}

	public boolean canDown(Down down){
		int x = down.getX();
		int y = down.getY();
		return board[y][x] == Blank;
	}
	
	public boolean canDown(int x, int y){
		return board[y][x] == Blank;
	}

	public void initialize(){
		
		for (int i=0; i < capacity; i++){
			for(int j=0; j < capacity; j++){
				board[i][j] = Blank;
			}
			
		}
		
	}
	
	
	public boolean down(Down down){
		if (canDown(down)){
			int x = down.getX();
			int y = down.getY();
			board[y][x] = down.getChessMan();
			return true;
		}
		return false;
	}
	
	private void printXRow(int y){
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
	
	public void printCurrentBoard(){
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
