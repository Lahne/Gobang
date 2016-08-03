package funny.gobang;

import java.io.IOException;

public class Game {
	
	protected Player offensive;
	
	protected Player defensive;
	
	protected ChessBoard chessBoard;
	
	protected Referee referee;
	
	public Game(){
		
	}
	
	public void setOffensive(Player player){
		this.offensive = player;
	}
	
	public void setDefensive(Player player){
		this.defensive = player;
	}
	
	private boolean interrupted(Down down){
		int x = down.getX();
		int y = down.getY();
		int capacity= chessBoard.getCapacity();
		if (x < 0 || x >= capacity || y < 0 || y >= capacity){
			return true;
		}
		return false;
		
	}
	
	public boolean isGameOver(){
		//referee.isGameOver(this);
		return false;
	}
	
	public void start(){
		this.chessBoard = new ChessBoard(15);
		offensive.register(chessBoard);
		defensive.register(chessBoard);

		Player nextPlayer = offensive;
		int nextChess = ChessBoard.Black;
		chessBoard.printCurrentBoard();
		while ( !isGameOver() ){
			Down down = nextPlayer.thinkNextDown();
			
			String player = "";
			if (nextPlayer.equals(offensive)){
				player = "offensive["+ offensive.getName()+"]";
			}else{
				player = "defensive["+ defensive.getName()+"]";
			}
			if (interrupted(down)){
				System.out.println(player + " interrupt the game!");
				break;
			}

			chessBoard.down(down);
			System.out.println(String.format("%s down <X,Y> : <%d,%d>",player,down.getX(),down.getY()));
			chessBoard.printCurrentBoard();
			nextPlayer = nextPlayer.equals(defensive) ? offensive : defensive;
			nextChess = nextChess == ChessBoard.Black ? ChessBoard.White : ChessBoard.Black;
		}
		
		System.out.println("==========Finished=========");
		
	}
	
	
	public static void main(String[] args) throws IOException{
		Game gobang = new Game();
		gobang.setOffensive(new Man());
		gobang.setDefensive(new GoBangAI());
		gobang.start();
	}

}
