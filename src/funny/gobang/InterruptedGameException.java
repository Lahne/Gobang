package funny.gobang;

public class InterruptedGameException extends Exception {

	private static final long serialVersionUID = -7691494529753497679L;
	
	private Player player;
	
	public InterruptedGameException(Player player,String msg){
		super(msg);
		this.player = player;
	}
	
	public InterruptedGameException(Player player){
		this.player = player;
	}

	public InterruptedGameException(Player player, Throwable cause) {
		super(cause);
		this.player = player;
	}
	
	public InterruptedGameException(Player player, String message, Throwable cause) {
		super(message, cause);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}	
}
