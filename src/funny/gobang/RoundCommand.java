package funny.gobang;

import java.util.List;

public class RoundCommand {
	public static final int COMMAND_DOWN_THREE_CHESS = 1;
	public static final int COMMAND_PICK_CHESSMAN = 2;
	public static final int COMMAND_DOWN_ONE = 3;
	public static final int COMMAND_DOWN_TWO_CHESS = 4;
	public static final int COMMAND_REMOVE_ONE_CHESS = 5;
	public static final int COMMAND_DOWN_AFTER_FIVE = 6;
	
	private int roundCommand ;
	
	private List<Down> removeDowns;
	
	public RoundCommand(int roundCommand){
		this.roundCommand = roundCommand;
	}
	
	public RoundCommand(int roundCommand, List<Down> downs){
		this.roundCommand = roundCommand;
		this.removeDowns = downs;
	}
	
	
	public int getRoundCommand(){
		return roundCommand;
	}
	
	public List<Down> getRemoveDowns(){
		return removeDowns;
	}
	
	
	
}
