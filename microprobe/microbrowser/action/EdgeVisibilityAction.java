package microbrowser.action;

import java.util.Iterator;
import java.util.logging.Logger;

import microbrowser.VisualDBConfig;
import microbrowser.VisualDBConstants;
import prefuse.action.GroupAction;
import prefuse.util.PrefuseLib;
import prefuse.visual.EdgeItem;

/**
 * The purpose of this action is to manage the visibility of the edges in the graph
 * 
 * @author Emilio Zegarra
 *
 */
public class EdgeVisibilityAction extends GroupAction {
	private static Logger logger = Logger.getLogger(EdgeVisibilityAction.class.getName());
	
	public EdgeVisibilityAction(String group) {
		this.setGroup(group);
	}
	
	@Override
	public void run(double frac) {
        // clock in
        long timein = System.currentTimeMillis();

		logger.info("running");
		int count = 0;
		@SuppressWarnings("unchecked")
		Iterator<EdgeItem> items = m_vis.items(m_group);

		while ( items.hasNext() ) {
			EdgeItem edge = items.next();
			
			if ( edge.getSourceItem().getInt("type") == VisualDBConstants.NODE_TYPE_LABEL || edge.getTargetNode().getInt("type") == VisualDBConstants.NODE_TYPE_LABEL) {
				PrefuseLib.updateVisible(edge, edge.getSourceItem().isVisible() && edge.getTargetItem().isVisible());
			}
			else if ( edge.getSourceItem().getInt("type") == VisualDBConstants.NODE_TYPE_PATTERN || edge.getTargetItem().getInt("type") == VisualDBConstants.NODE_TYPE_PATTERN) {
				PrefuseLib.updateVisible(edge, edge.getSourceItem().isVisible() && edge.getTargetItem().isVisible());
			} else {
				PrefuseLib.updateVisible(edge, edge.getSourceItem().isVisible() && edge.getTargetItem().isVisible() && edge.getDouble("similarity") >= VisualDBConfig.SLIDER_SIMILARITY_VALUE);				
			}
			
			if ( edge.isVisible()) {
				count++;
			}
		}

		// clock out
        long time = System.currentTimeMillis()-timein;
		logger.info("Made " + count + " edges visisble similarity=" + VisualDBConfig.SLIDER_SIMILARITY_VALUE  + " in " + (time/1000) + "." + (time%1000) + " seconds.");
	}

}
