package funny.gobang;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class GoBangAI extends AbstractPlayer {

	protected final ExecutorService exeutorService;
	
	protected long limitedThinkingTime;
	
	protected TimeUnit timeUnit;
	
	public GoBangAI(){
		this(Executors.newSingleThreadExecutor());
	}
	
	public GoBangAI(ExecutorService executorService){
		this.exeutorService = executorService;
		limitThinkingTime(Long.MAX_VALUE, TimeUnit.SECONDS);
	}
	
	public void limitThinkingTime(long limitedTime,TimeUnit timeUnit){
		this.limitedThinkingTime = limitedTime;
		this.timeUnit = timeUnit;
	}
	
//	public GoBangAI(ExecutorService executorService,Thinking ){
//		this.exeutorService = executorService;
//	}
	
	@Override
	public Down thinkNextDown(int round) throws InterruptedGameException {
		FutureTask<Down> nextDownTask = (FutureTask<Down>) exeutorService.submit(new Thinking());
		try {
			Down result = nextDownTask.get(limitedThinkingTime,timeUnit);
			return result;
		} catch (Exception e) {
			throw new InterruptedGameException(this);
		} 
	}

	@Override
	public void watchOpponentDown(Down down) {
		// TODO Auto-generated method stub
		
	}
	
	class TimingThinking extends FutureTask<Down>{
		
		public TimingThinking(Callable<Down> callable) {
			super(callable);
		}
		
	}
	
	class Thinking implements Callable<Down>{
		
		@Override
		public Down call() throws Exception {
			Random rand = new Random();
			int x = -1;
			int y = -1;
			do{
				x = rand.nextInt(chessBoard.getCapacity());
				y = rand.nextInt(chessBoard.getCapacity());
			}while(! chessBoard.canDown(x, y));
			return new Down(x,y,pickedChessMan);
		}
		
	}
	
	protected Down thinkNextDownInternal(ChessBoard board,int chessMan){
		Random rand = new Random();
		int x = -1;
		int y = -1;
		do{
			x = rand.nextInt(board.getCapacity());
			y = rand.nextInt(board.getCapacity());
		}while(! board.canDown(x, y));
		return new Down(x,y,chessMan);
	}

	@Override
	public void exit() {
		if (exeutorService == null)
			return;
		exeutorService.shutdownNow();
		
	}

	public RoundResult thinkFirstRound(){
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
	
	public RoundResult thinkSecondRound(){
		Random rand = new Random();
		int randNo = rand.nextInt(2);
		int pickedChessMan = ChessBoard.White;
		if (randNo == 1){
			pickedChessMan = ChessBoard.Black;
		}
		pickChessMan(pickedChessMan);
		return new RoundResult(pickedChessMan);
	}
	
	public RoundResult thinkThirdRound(){
		Down down = thinkNextDownInternal(chessBoard, ChessBoard.White);
		return new RoundResult(down);
	}
	
	public RoundResult thinkFourthRound(){
		ChessBoard board = chessBoard.clone();
		List<Down> downs = new ArrayList<>();
		int chessMan = ChessBoard.Black;
		for (int i=0; i < 2; i++){
			Down down = thinkNextDownInternal(board, chessMan);
			downs.add(down);
			board.down(down);
		}
		return  new RoundResult(downs);
	}
	
	public RoundResult thinkFifthRound(List<Down> downs){
		Random rand = new Random();
		int removed = rand.nextInt(2);		
		return new RoundResult(downs.get(removed));
	}
	
	public RoundResult thinkAfterfiveRound(){
		Down down = thinkNextDownInternal(chessBoard, pickedChessMan);
		return new RoundResult(down);
	}
	
	@Override
	public RoundResult executeCommand(RoundCommand command) {
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
