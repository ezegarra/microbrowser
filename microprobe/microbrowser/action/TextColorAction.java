package microbrowser.action;

import microbrowser.VisualDBConstants;
import prefuse.action.assignment.ColorAction;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

public class TextColorAction extends ColorAction {
	public TextColorAction(String group) {
		super(group, VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0));
		add("type = " + VisualDBConstants.NODE_TYPE_PATTERN, ColorLib.rgb(229,245,224));
	}
}
