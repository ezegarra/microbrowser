package microbrowser.action;

import java.util.Iterator;
import java.util.logging.Logger;

import microbrowser.VisualDBConstants;
import microbrowser.data.io.GraphMLDataReader;
import microbrowser.util.PredicatesUtil;
import prefuse.Visualization;
import prefuse.action.GroupAction;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.util.PrefuseLib;
import prefuse.visual.VisualItem;

public class PatternVisibilityAction extends GroupAction {
	private static Logger logger = Logger.getLogger(PatternVisibilityAction.class.getName());
	private Graph m_graph;
	
	public PatternVisibilityAction(Visualization m_vis) {
		this.setGroup("graph.nodes");
		
		m_graph =(Graph) m_vis.getGroup("graph");
	}

	@Override
	public void run(double frac) {
        // clock in
        long timein = System.currentTimeMillis();
        
		logger.info("running");
		
		Iterator<VisualItem> items = m_vis.items(m_group, PredicatesUtil.TYPE_PATTERN);
		
		while ( items.hasNext()) {
			VisualItem n = items.next();
			Iterator<Integer> relatedIds = 	GraphMLDataReader.getRelatedItemIds(m_graph, n.getInt("id"));
			
			while ( relatedIds.hasNext()) {
				Integer nodeId = relatedIds.next();
				VisualItem item = (VisualItem) GraphMLDataReader.getNodeById(m_graph, nodeId);
				if ( item.isVisible() ) {
					PrefuseLib.updateVisible(n, true);
				}
				
				
			}
		}
		
		// clock out
        long time = System.currentTimeMillis()-timein;

        logger.info("Done in " + (time/1000) + "." + (time%1000) + " seconds.");
	}
}
