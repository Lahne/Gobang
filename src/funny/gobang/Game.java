package funny.gobang;

import java.io.IOException;
import java.util.List;

public class Game {
	
	protected Player offensive;
	
	protected Player defensive;
	
	protected ChessBoard chessBoard;
	
	protected Referee referee;
	
	protected int capacity;
	
	
	public Game(){
		this(15);
	}
	
	public Game(int capacity){
		this.capacity = capacity;
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
		this.chessBoard = new ChessBoard(capacity);
		offensive.register(chessBoard);
		offensive.pickChessMan(ChessBoard.Black);
		defensive.register(chessBoard);
		defensive.pickChessMan(ChessBoard.White);
		
		Player nextPlayer = offensive;
		int nextChess = ChessBoard.Black;
		chessBoard.printCurrentBoard();
		boolean isGameInterrupted = false;
		
		int round = 0;
		
		while ( !isGameInterrupted && !isGameOver() ){
			try{
				round++;
				Down down = nextPlayer.thinkNextDown(round);
				String player = "";
				if (nextPlayer.equals(offensive)){
					player = "offensive["+ offensive.getName()+"]";
				}else{
					player = "defensive["+ defensive.getName()+"]";
				}
//				if (interrupted(down)){
//					System.out.println(player + " interrupt the game!");
//					break;
//				}
				chessBoard.down(down);
				System.out.println(String.format("%s down <X,Y> : <%d,%d>",player,down.getX(),down.getY()));
				chessBoard.printCurrentBoard();
				nextPlayer = nextPlayer.equals(defensive) ? offensive : defensive;
				nextChess = nextChess == ChessBoard.Black ? ChessBoard.White : ChessBoard.Black;
			}catch(InterruptedGameException e){
				isGameInterrupted = true;
				Player player = e.getPlayer();
				System.out.println(player.getName() + " interrupt the game!");
			}
		}
		
		offensive.exit();
		defensive.exit();
		
		System.out.println("==========Finished=========");
		
	}
	
	private Down executeDown(Player player,int command) throws InterruptedGameException{
		RoundResult result = player.executeCommand(new RoundCommand(command));
		Down down = result.getDown();
		chessBoard.down(result.getDown());
		System.out.println("------------------------------------------------------");
		System.out.println(String.format("##### %s down <X,Y> : <%d,%d>",getPlayerName(player),down.getX(),down.getY()));
		chessBoard.printCurrentBoard();
		return result.getDown();
	}
	
	private List<Down> executeMuiltDowns(Player player,int command) throws InterruptedGameException {
		RoundResult result = player.executeCommand(new RoundCommand(command));
		chessBoard.down(result.getDowns());
		chessBoard.printCurrentBoard();
		return result.getDowns();
	}
	
	private String getPlayerName(Player player){
		String playerName = "";
		if (player.equals(offensive)){
			playerName = "offensive["+ offensive.getName()+"]";
		}else{
			playerName = "defensive["+ defensive.getName()+"]";
		}
		return playerName;
	}
	
	public void start1(){
		this.chessBoard = new ChessBoard(capacity);
		offensive.register(chessBoard);
		defensive.register(chessBoard);
		
		chessBoard.printCurrentBoard();
		try{
			//round one
			System.out.println("Round 1: "+ getPlayerName(offensive) +" down three chessMans at first");
			executeMuiltDowns(offensive, RoundCommand.COMMAND_DOWN_THREE_CHESS);
			
			//round two
			System.out.println("Round 2: "+  getPlayerName(defensive) +" pick chessMan");
			RoundResult result = defensive.executeCommand(new RoundCommand(RoundCommand.COMMAND_PICK_CHESSMAN));
			int pickedChessMan = result.getPickedChessMan();
			System.out.println("defensive " + defensive.getName() + " picked " + ((pickedChessMan == ChessBoard.Black) ? "Black" : "White"));
			defensive.pickChessMan(pickedChessMan);
			if (pickedChessMan == ChessBoard.Black){
				offensive.pickChessMan(ChessBoard.White);
			}else{
				offensive.pickChessMan(ChessBoard.Black);
			}
			
			//round three
			Player nextRoundPlayer = (pickedChessMan == ChessBoard.Black)? offensive : defensive;
			System.out.println("Round 3: "+ getPlayerName(nextRoundPlayer)+ " chessMan input one White down");
			executeDown(nextRoundPlayer, RoundCommand.COMMAND_DOWN_ONE);
			
			//round fourth
			nextRoundPlayer = (nextRoundPlayer.equals(defensive))? offensive : defensive;
			System.out.println("Round 4: "+ getPlayerName(nextRoundPlayer)+ " input two Black downs");
			List<Down> twoDowns= executeMuiltDowns(nextRoundPlayer, RoundCommand.COMMAND_DOWN_TWO_CHESS);
			
			//round five
			nextRoundPlayer = (nextRoundPlayer.equals(defensive))? offensive : defensive;
			System.out.println("Round 5: "+ getPlayerName(nextRoundPlayer)+ " remove one Black down");
			result  = nextRoundPlayer.executeCommand(new RoundCommand(RoundCommand.COMMAND_REMOVE_ONE_CHESS,twoDowns));
			chessBoard.remove(result.getDown());
			System.out.println(String.format("%s remove Black ChessMan <X,Y> : <%d,%d>",getPlayerName(nextRoundPlayer),result.getDown().getX(),result.getDown().getY()));
			chessBoard.printCurrentBoard();

			//round after five
			nextRoundPlayer = (nextRoundPlayer.equals(defensive))? offensive : defensive;
			while (!isGameOver() ){
				executeDown(nextRoundPlayer, RoundCommand.COMMAND_DOWN_AFTER_FIVE);
				nextRoundPlayer = nextRoundPlayer.equals(defensive) ? offensive : defensive;
			}
		}catch(InterruptedGameException e){
			Player player = e.getPlayer();
			System.out.println(player.getName() + " interrupt the game!");
		}finally{
			offensive.exit();
			defensive.exit();
			System.out.println("==========Finished=========");
		}
	}
	
	public static void main(String[] args) throws IOException{
		Game gobang = new Game(15);
		gobang.setOffensive(new Man());
		gobang.setDefensive(new GoBangAI());
		gobang.start1();
	}

}
