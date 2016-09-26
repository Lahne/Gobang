package funny.gobang.rule.common;

import static funny.gobang.rule.ChessPatterns.*;

import funny.gobang.model.ChessType;
import funny.gobang.rule.ChessPatterns;

public class ChessPatternForWhiteResolver extends AbstractChessPatternResolver{
		
		
		public ChessPatternForWhiteResolver(){
			isBlackTarget = false;
			initilize();
		}

		
		void initilize(){
			initWhite("#ww$#", DEAD_THREE);
			
			initWhite("#eww$#", DEAD_THREE);
			
			initWhite("#wew$#", DEAD_THREE);
			initWhite("#wwe$#", DEAD_THREE);

			initWhite("ne$wwee", CONTINOUS_LIVE_THREE);
			
			initWhite("ne$wewe", JUMP_LIVE_THREE);
			
			initWhite("#wew$ewe", JUMP_LIVE_THREE);
			
			initWhite("ewwe$en", JUMP_LIVE_THREE);
			initWhite("ewwe$ewn", JUMP_LIVE_THREE);
			initWhite("ewwe$eww#", JUMP_LIVE_THREE);
			
			initWhite("ewew$ewe", DOUBLE_LIVE_THREE);
			
			initWhite("ewwe$ewwe", DOUBLE_LIVE_THREE);
			
			
			initWhite("eeww$#", SLEEP_THREE);
			
			initWhite("#eww$e#", SLEEP_THREE);
			
			initWhite("ewwe$#", SLEEP_THREE);
			
			initWhite("#wwe$en", SLEEP_THREE);
			initWhite("#wwe$ewn", SLEEP_THREE);
			initWhite("#wwe$eww#", SLEEP_THREE);
			
			initWhite("ewew$#", SLEEP_THREE);
			
			initWhite("#wew$en", SLEEP_THREE);

			initWhite("#we$wew#", SLEEP_THREE);
			
			
			initWhite("ew$wwe", LIVE_FOUR);

			initWhite("ewww$#", FIGHT_FOUR);
			
			initWhite("weww$#", FIGHT_FOUR);

			initWhite("weww$e#", FIGHT_FOUR);
			
			initWhite("wwew$#", FIGHT_FOUR);

			initWhite("wwew$en", FIGHT_FOUR);

			initWhite("wwew$ew#", FIGHT_FOUR);
			
			initWhite("nwwwe$#", FIGHT_FOUR);
			initWhite("nwwwe$en", FIGHT_FOUR);
			initWhite("nwwwe$ewn", FIGHT_FOUR);
			initWhite("nwwwe$eww#", FIGHT_FOUR);
			
			initWhite("wwe$weww", DOUBLE_FIGHT_FOUR);
			
			initWhite("we$wwew", DOUBLE_FIGHT_FOUR);
			
			initWhite("wwwe$ewww", DOUBLE_FIGHT_FOUR);
			
			initWhite("we$wwee", LIVE_THREE_FIGHT_FOUR);

			initWhite("wwew$ewe", LIVE_THREE_FIGHT_FOUR);
			
			initWhite("wwwe$ewwe", LIVE_THREE_FIGHT_FOUR);
			
			initWhite("#$www#", DEAD_FOUR);

			initWhite("nwwww$n", FIVE);
			
			initWhite("w$wwww", MORE_THAN_FIVE);
		}
		
		void initWhite(String matchers, ChessPatterns pattern){
			parseAndAdd(matchers, pattern);
		}
		
		protected StateNode parse(ParserHelper helper) {
			
	   		char c = helper.c;
	   		
	   		if ( c == 'b'){
	   			helper.addNodeIfAbsent(ChessType.BLACK, new FinishStateNode()).markState();
	   			return null;
	   		}else if (c == '#'){
	   			helper.addNodeIfAbsent(ChessType.BOUNDARY, new FinishStateNode()).markState();
	   			helper.addNodeIfAbsent(ChessType.BLACK, new FinishStateNode()).markState();
		   		return null;
	   		}else if (c == 'w'){
	   			return helper.addNodeIfAbsent(ChessType.WHITE, new StateNode()).markStateIfIsEnd().getBindingIfNotIsEnd();
	   		}else if( c == 'e'){
	   			return helper.addNodeIfAbsent(ChessType.EMPTY, new StateNode()).markStateIfIsEnd().getBindingIfNotIsEnd();
	   		}else if (c == 'n'){
	   			helper.addNodeIfAbsent(ChessType.BOUNDARY, new FinishStateNode()).markState();
	   			helper.addNodeIfAbsent(ChessType.BLACK, new FinishStateNode()).markState();
	   			return helper.addNodeIfAbsent(ChessType.EMPTY, new StateNode()).markStateIfIsEnd().getBindingIfNotIsEnd();
	   		}else{
	   			throw new RuntimeException("Parsing Parttern String Error: Not Support Char - " + c);
	   		}
	   		
	   	}
		
		
}