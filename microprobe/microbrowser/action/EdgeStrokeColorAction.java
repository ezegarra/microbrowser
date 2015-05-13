package microbrowser.action;

import prefuse.action.assignment.ColorAction;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

public class EdgeStrokeColorAction extends ColorAction {

	public EdgeStrokeColorAction(String group) {
		super(group, VisualItem.STROKECOLOR);
	}

	/**
	 * @see prefuse.action.assignment.ColorAction#getColor(prefuse.visual.VisualItem)
	 */
	public int getColor(VisualItem edge) {

		double similarity = edge.getDouble("similarity");

		int color;

		if (similarity < 0.2) {
			color = ColorLib.rgb(204,204,204);
		} else if (similarity < 0.3) {
			color = ColorLib.rgb(240,240,240);
		} else if (similarity < 0.4) {
			color = ColorLib.rgb(217,217,217);
		} else if (similarity < 0.5) {
			color = ColorLib.rgb(189,189,189);
		} else if (similarity < 0.6) {
			color = ColorLib.rgb(150,150,150);
		} else if (similarity < 0.7) {
			color = ColorLib.rgb(115,115,115);
		} else if (similarity < 0.8) {
			color = ColorLib.rgb(82,82,82);
		} else if (similarity < 0.9) {
			color = ColorLib.rgb(37,37,37);
		} else {
			color = ColorLib.rgb(0, 0, 0);
		}
		return color;
	}

}
