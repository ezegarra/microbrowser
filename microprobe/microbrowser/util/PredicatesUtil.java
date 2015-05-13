package microbrowser.util;

import microbrowser.VisualDBConstants;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;

public class PredicatesUtil {

	public static Predicate TYPE_DISCUSSION = (Predicate) ExpressionParser.parse("type = " + VisualDBConstants.NODE_TYPE_DISCUSSION,true);
	public static Predicate TYPE_PATTERN = (Predicate) ExpressionParser.parse("type = " + VisualDBConstants.NODE_TYPE_PATTERN);
}
