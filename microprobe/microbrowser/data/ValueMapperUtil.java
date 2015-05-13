package microbrowser.data;

import java.util.HashMap;
import java.util.Map;

public class ValueMapperUtil {
	public static String getClassificationValue(int classification) {
		Map<Integer, String>values = new HashMap<Integer, String>();
		values.put(0, "Unclassified");
		values.put(1, "Hints");
		values.put(2, "How-Tos");
		values.put(3, "Clarifications");
		
		return values.get(new Integer(classification));
	}

}
