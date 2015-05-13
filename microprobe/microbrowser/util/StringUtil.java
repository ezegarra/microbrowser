package microbrowser.util;

public class StringUtil {

	public static String toMultiLine(String text, String lineSep, int maxwidth) {
		StringBuffer sb = new StringBuffer();
		
		int currIdx = 0;
		int numLines = 0;
		
		maxwidth = maxwidth > text.length()? text.length(): maxwidth;
		
		numLines = (text.length() / maxwidth ) + 1;
		
		for (int i = 1; i < numLines; i++) {
			sb.append(text.substring(currIdx, (i * maxwidth) ));
			sb.append(lineSep);
			
			currIdx = (i * maxwidth );
		}
		sb.append(text.substring(currIdx));
		
		return sb.toString(); 
	}
}
