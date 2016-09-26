package funny.gobang.rule.common;

import funny.gobang.model.Point;

public interface PatternMatcher {

	boolean match(Matcher matcher, Point point);
	
}
