package microbrowser.action;

import java.awt.Color;

import microbrowser.VisualDBConstants;
import prefuse.action.assignment.ColorAction;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

public class FillColorAction extends ColorAction {
	public FillColorAction(String group) {
		super(group, VisualItem.FILLCOLOR, ColorLib.rgb(247,247,247));
		
		// add coloring rules
		add("_hover or ingroup('_focus_')", ColorLib.rgb(208,28,139));
		add("_highlight", ColorLib.rgb(153,142,195));
		add("type = " + VisualDBConstants.NODE_TYPE_PATTERN, Color.GREEN.darker().getRGB());
		add("type = " + VisualDBConstants.NODE_TYPE_ANSWER, ColorLib.rgb(166,189,219));
		add("type = " + VisualDBConstants.NODE_TYPE_ANSWER_ACCEPTED, ColorLib.rgb(43,140,190));
		add("type = " + VisualDBConstants.NODE_TYPE_LABEL, ColorLib.rgb(166,189,219));
		add("viewcount < 5000", ColorLib.rgb(254,237,222));
		add("viewcount < 10000", ColorLib.rgb(253,208,162));
		add("viewcount < 30000", ColorLib.rgb(253,174,107));
		add("viewcount < 50000", ColorLib.rgb(253,141,60));
		add("viewcount < 100000", ColorLib.rgb(241,105,19));
		add("viewcount < 300000", ColorLib.rgb(217,72,1));
		add("viewcount >= 300000", ColorLib.rgb(140,45,4));
	}
} // end of inner class VisualDBFillColorAction
