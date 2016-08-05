package funny.gobang;

import java.util.ArrayList;
import java.util.List;

public class Man extends AbstractPlayer {

	protected ReadHelper helper = new ReadHelper();
	
	
	

	
	@Override
	public Down thinkNextDown(int round) throws InterruptedGameException {
		int capacity = chessBoard.getCapacity();
		int nextChess = ChessBoard.Black;
		String nextChessMan = nextChess == ChessBoard.Black ? "Black": "White";
		int[] down = null;
		while( (down = helper.readNextDown(nextChessMan)) != null){
			int x = down[0];
			int y = down[1];
			if (x < 0 || x >= capacity || y < 0 || y >= capacity){
				System.out.println("Out of index, X and Y should >=0 && < "+capacity);
				continue;
			}
			if (chessBoard.canDown(x, y)){
				return new Down(x,y,nextChess);
			}else{
				System.out.println(String.format("Current Psition %d,%d has been setted, please choose a blank position",x,y));
			}
		}
		
		throw new InterruptedGameException(this);
		
	}
	

	@Override
	public void exit() {
		helper.close();
	}

	protected Down thinkNextDownInternal(ChessBoard board,int chessMan) throws InterruptedGameException{
		return thinkNextDownInternal(board,chessMan,false);
	}
	
	protected Down thinkNextDownInternal(ChessBoard board,int chessMan, boolean removed) throws InterruptedGameException{
		int capacity = board.getCapacity();
		int nextChess = chessMan;
		String nextChessMan = nextChess == ChessBoard.Black ? "Black": "White";
		int[] down = null;
		
		String display = removed ? "Please choose one to remove" : "Please input "+ nextChessMan+" down X,Y"; 
		while( (down = helper.readNextDown(display)) != null){
			int x = down[0];
			int y = down[1];
			if (x < 0 || x >= capacity || y < 0 || y >= capacity){
				System.out.println("Out of index, X and Y should >=0 && < "+capacity);
				continue;
			}
			if (removed || board.canDown(x, y)){
				return new Down(x,y,nextChess);
			}else{
				System.out.println(String.format("Current Psition %d,%d has been setted, please choose a blank position",x,y));
			}
		}
		
		throw new InterruptedGameException(this);
	}

	public RoundResult thinkFirstRound() throws InterruptedGameException{
		ChessBoard board = chessBoard.clone();
		
		List<Down> downs = new ArrayList<>();
		int chessMan = ChessBoard.Black;
		for (int i=0; i < 3; i++){
			Down down = thinkNextDownInternal(board, chessMan);
			downs.add(down);
			board.down(down);
			chessMan = chessMan == ChessBoard.Black ? ChessBoard.White : ChessBoard.Black;
		}
		
		return  new RoundResult(downs);
	}
	
	public RoundResult thinkSecondRound() throws InterruptedGameException{
		
		String pickedChessMan;
		while( (pickedChessMan = helper.pickChessMan()) != null){
			
			if (pickedChessMan.trim().equalsIgnoreCase("black")){
				return new RoundResult(ChessBoard.Black);
			}else if (pickedChessMan.trim().equalsIgnoreCase("white")){
				return new RoundResult(ChessBoard.White);
			}else{
				System.out.println("must input Black or White");
			}
		}
		
		throw new InterruptedGameException(this);
	}
	
	public RoundResult thinkThirdRound() throws InterruptedGameException{
		Down down = thinkNextDownInternal(chessBoard, ChessBoard.White);
		return new RoundResult(down);
	}
	
	public RoundResult thinkFourthRound() throws InterruptedGameException{
		
		ChessBoard board = chessBoard.clone();
		List<Down> downs = new ArrayList<>();
		int chessMan = ChessBoard.Black;
		for (int i=0; i < 2; i++){
			Down down = thinkNextDownInternal(board, chessMan);
			downs.add(down);
			board.down(down);
		}
		return new RoundResult(downs);
	}
	
	public RoundResult thinkFifthRound(List<Down> downs) throws InterruptedGameException{

		boolean isRightInput = false;
		Down thinkeddown = null;
		do{
			System.out.println("Please remove one of them");
			for (Down down : downs){
				System.out.print("<"+ down.getX()+","+down.getY()+"> ");
			}
			System.out.println();
			thinkeddown = thinkNextDownInternal(chessBoard, ChessBoard.Black,true);
			for (Down down : downs){
				if (thinkeddown.equal(down)){
					isRightInput = true;
				}
			}
		}while (!isRightInput);
		
		return new RoundResult(thinkeddown);
	}
	
	public RoundResult thinkAfterfiveRound() throws InterruptedGameException{
		Down down = thinkNextDownInternal(chessBoard, pickedChessMan);
		return new RoundResult(down);
	}
	
	@Override
	public RoundResult executeCommand(RoundCommand command) throws InterruptedGameException{
		int roundCommand = command.getRoundCommand();
		if (roundCommand == RoundCommand.COMMAND_DOWN_AFTER_FIVE){
			return thinkAfterfiveRound();
		}else if (roundCommand == RoundCommand.COMMAND_DOWN_THREE_CHESS){
			return thinkFirstRound();
		}else if (roundCommand == RoundCommand.COMMAND_PICK_CHESSMAN){
			return thinkSecondRound();
		}else if (roundCommand == RoundCommand.COMMAND_DOWN_ONE){
			return thinkThirdRound();
		}else if (roundCommand == RoundCommand.COMMAND_DOWN_TWO_CHESS){
			return thinkFourthRound();
		}else if (roundCommand == RoundCommand.COMMAND_REMOVE_ONE_CHESS){
			return thinkFifthRound(command.getRemoveDowns());
		}
		
		return null;
	}
}
