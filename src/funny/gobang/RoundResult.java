package funny.gobang;

import java.util.List;

public class RoundResult {

	private List<Down> downs;
	
	private Down down;
	
	private int pickedChessMan;
	
	
	
	public RoundResult(List<Down> downs) {
		super();
		this.downs = downs;
	}

	public RoundResult(Down down) {
		super();
		this.down = down;
	}

	public RoundResult(int pickedChessMan) {
		super();
		this.pickedChessMan = pickedChessMan;
	}

	public int getPickedChessMan() {
		return pickedChessMan;
	}

	public void setPickedChessMan(int pickedChessMan) {
		this.pickedChessMan = pickedChessMan;
	}

	public void setDowns(List<Down> downs) {
		this.downs = downs;
	}

	public void setDown(Down down) {
		this.down = down;
	}

	public List<Down> getDowns() {
		return downs;
	}

	public Down getDown() {
		return down;
	}
	

	
	
}
