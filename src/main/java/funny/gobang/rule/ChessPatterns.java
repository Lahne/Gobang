package funny.gobang.rule;

public enum ChessPatterns {

		FORBIDDEN,
	
		UNKNOWN,
		ONE,
		
		TWO,
		LIVE_TWO,
		JUMP_LIVE_TWO,
		BIG_JUMP_LIVE_TWO,
		CONTINOUS_LIVE_TWO,
		SLEEP_TWO,
		DEAD_TWO,
			
		LIVE_THREE,
		CONTINOUS_LIVE_THREE,
		JUMP_LIVE_THREE,
		SLEEP_THREE,
		DEAD_THREE,
		
		FOUR,
		FIGHT_FOUR,
		LIVE_FOUR,
		DEAD_FOUR,
		
		
		FIVE,
		
		MORE_THAN_FIVE,
	
		
		LIVE_THREE_FIGHT_FOUR,
		DOUBLE_FIGHT_FOUR,
		DOUBLE_LIVE_THREE;
	
		public boolean isLiveThree(){
			return isIn(JUMP_LIVE_THREE,LIVE_THREE_FIGHT_FOUR,CONTINOUS_LIVE_THREE,LIVE_THREE);
		}
		
		public boolean isFightFourOrLiveFour(){
			return isIn(FIGHT_FOUR,DOUBLE_FIGHT_FOUR,LIVE_FOUR,LIVE_THREE_FIGHT_FOUR);
		}
		
		private boolean isIn(ChessPatterns... patterns){
			for (int i = 0; i < patterns.length; i++){
				if (this == patterns[i]){
					return true;
				}
			}
			return false;
		}
		
	
}
