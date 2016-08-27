package funny.gobang.ai.alphabeta;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import funny.gobang.ai.AIUtils;
import funny.gobang.model.ChessBoard;
import funny.gobang.model.Point;

/**
 * AlphaBetaPrunningConcurrentAI 
 * 
 * 
 * need further improvement
 * 
 * @since 2016-8-21
 * @author Lahne
 *
 */
public class AlphaBetaPrunningConcurrentAI extends AlphaBetaPrunningAI{

	protected ExecutorService exeutorService; 
	
	public AlphaBetaPrunningConcurrentAI(int searchDepth) {
		exeutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}
	
	
	
	public ExecutorService getExeutorService() {
		return exeutorService;
	}



	public void setExeutorService(ExecutorService exeutorService) {
		this.exeutorService = exeutorService;
	}


	public void shutdown(){
		exeutorService.shutdown();
	}
	
	public boolean isShutdown(){
		return exeutorService.isShutdown();
	}
	
	
	public Point getNext(final ChessBoard board,final int chessType){
		if (isShutdown()){
			throw new IllegalStateException("AI has been shutdown");
		}
		
		List<Point> validPoints = generate(board, chessType);

		WorkerManager manager = new WorkerManager(board,chessType,validPoints);
		
		List<EstimateScoreWorker> wokers = manager.assignWorkers();
		
		for (EstimateScoreWorker worker : wokers){
			exeutorService.submit(worker);
		}
		
		try {
			manager.waitAllCompeletion();
		} catch (InterruptedException e) {
			throw new RuntimeException("Current Thread is interrupted");
		}
		
		return manager.getResults().isEmpty() ? null : pickOne(manager.getResults()).getPoint();
	}

	
	class WorkerManager{
		
		private final AtomicBoolean hasfindBest;
		
		private final List<PointScore> results;
		
		private final CountDownLatch endGate;
		
		private volatile boolean cancelTask = false;
		
		private final ChessBoard board;
		
		private final int chessType;
		
		private final List<Point> pointsForWorkers;

		private List<EstimateScoreWorker> workers;
		
		public WorkerManager(ChessBoard board,int chessType, List<Point> pointsForWorkers) {
			super();
			this.board = board;
			this.chessType = chessType;
			this.pointsForWorkers = pointsForWorkers;
			this.hasfindBest = new AtomicBoolean(false);
			this.results = new Vector<PointScore>();
			this.endGate = new CountDownLatch(pointsForWorkers.size());
		}

		
		public List<EstimateScoreWorker> assignWorkers(){
			
			if (workers != null)
				return workers;
			
			List<EstimateScoreWorker> workers = new LinkedList<>();
			for (Point point: pointsForWorkers){
				workers.add(new EstimateScoreWorker(this, point));
			}
			
			this.workers = Collections.unmodifiableList(workers);
			
			return workers;
			
		}
		
		
		public boolean cancelTask() {
			return cancelTask;
		}


		public void setCancelTask(boolean cancelTask) {
			this.cancelTask = cancelTask;
		}


		public boolean hasfindBest() {
			return hasfindBest.get();
		}

		public void markFindBest(boolean findBest){
			hasfindBest.set(findBest);
		}
		
		public void addResult(PointScore pointScore){
			results.add(pointScore);
		}
		
		public List<PointScore> getResults() {
			return results;
		}

		public void markWorkerCompletion(EstimateScoreWorker worker){
			endGate.countDown();
		}
		
		public void waitAllCompeletion() throws InterruptedException{
			endGate.await();
		}


		public ChessBoard getBoard() {
			return board;
		}


		public int getChessType() {
			return chessType;
		}
		
		
	}
	
	class EstimateScoreWorker implements Runnable{

		private final WorkerManager manager;
		
		private final ChessBoard board;
		
		private final Point point;
		
		private  int chessType;
		
		public EstimateScoreWorker(WorkerManager manager,Point point) {
			super();
			this.manager = manager;
			this.board = manager.getBoard().clone();
			this.chessType = manager.getChessType();
			this.point = point;
		}


		public ChessBoard getBoard() {
			return board;
		}


		public Point getPoint() {
			return point;
		}


		@Override
		public void run() {
			board.downChess(point, chessType);
			
			if (AIUtils.checkIfWin(board, point)){
				manager.addResult(new PointScore(point,MAX_SCORE));
				manager.markFindBest(true);
				return;
			}
			try{
				long estimateScore = this.alphabeta(1, -MAX_SCORE, MAX_SCORE, board, nextChessType(chessType) );
				manager.addResult(new PointScore(point,estimateScore));
			}catch(Exception e){
				//catch exception
			}finally{
				manager.markWorkerCompletion(this);
			}
			
		}
		
		
		/**
		 * depth % 2 == 0 means max layer.
		 * depth % 2 == 1 means min layer.
		 * @param depth
		 * @param alpha
		 * @param beta
		 * @param board
		 * @param chessType
		 * @return
		 */
		protected long alphabeta(int depth, long alpha, long beta, ChessBoard board, int chessType){
			
			if (manager.cancelTask()){
				throw new RuntimeException("Cancel Current Task");
			}
			
			if (manager.hasfindBest()){
				throw new RuntimeException("Other Branch has got best Point");
			}
			
			if (searchDepth == depth){
				return evaluate(board,chessType);
			}
			
			List<Point> avalidPoints = generate(board, chessType);
			
			boolean isMax = (depth & 1) == 0;
			
			for (Point point : avalidPoints){
				
				board.downChess(point, chessType);
				
				if (isMax){
					if (AIUtils.checkIfWin(board, point)){
						alpha = MAX_SCORE;
					}else{
						alpha = Math.max(alpha, this.alphabeta(depth +1,alpha, beta, board, nextChessType(chessType)));
					}
				}else{
					if (AIUtils.checkIfWin(board, point)){
						beta = -MAX_SCORE;
					}else{
						beta = Math.min(beta, this.alphabeta(depth +1, alpha, beta, board, nextChessType(chessType)));	
					}
				}
				
				board.remove(point);
				
				if (beta <= alpha){
					break;
				}
			}
			
			return isMax ? alpha : beta;
		}
		
		
	} 
	
	
}
