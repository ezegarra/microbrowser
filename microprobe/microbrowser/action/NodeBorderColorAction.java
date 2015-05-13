package microbrowser.action;

import microbrowser.VisualDBConfig;
import microbrowser.util.DateLib;
import prefuse.action.assignment.ColorAction;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

public class NodeBorderColorAction extends ColorAction {
	public NodeBorderColorAction(String group) {
		super(group, VisualItem.STROKECOLOR, ColorLib.rgb(99, 99, 99));
		
		// add color rules
		add("lastactivitydate = " + DateLib.getDateAsLong(VisualDBConfig.RANGE_SLIDER_LOW_VALUE) +"L", ColorLib.rgb(43,140,190));
		add("lastactivitydate = " + DateLib.getDateAsLong(VisualDBConfig.RANGE_SLIDER_HIGH_VALUE) +"L", ColorLib.rgb(43,140,190));
	}
}
