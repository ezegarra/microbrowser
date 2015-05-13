package microbrowser.action;

import java.awt.Font;

import microbrowser.VisualDBConstants;
import prefuse.action.assignment.FontAction;
import prefuse.util.FontLib;

public class NodeFontAction extends FontAction {

	public NodeFontAction(String group) {
		super(group, FontLib.getFont("Tahoma", 11));

		add("type=" + VisualDBConstants.NODE_TYPE_LABEL, FontLib.getFont("Courier New", Font.PLAIN, 15));
		add("type=" + VisualDBConstants.NODE_TYPE_PATTERN, FontLib.getFont("Arial", Font.BOLD, 15));
	}
}
