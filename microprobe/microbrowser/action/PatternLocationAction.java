package microbrowser.action;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import microbrowser.VisualDBConstants;
import prefuse.action.GroupAction;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.util.display.DisplayLib;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;

public class PatternLocationAction extends GroupAction {
	private static final Logger logger = Logger.getLogger(PatternLocationAction.class.getName());
	@Override
	public void run(double frac) {
		logger.info("run");
		
		Iterator iter = m_vis.items((Predicate)ExpressionParser.parse("type = " + VisualDBConstants.NODE_TYPE_PATTERN, true));
		while ( iter.hasNext() ) {
			ArrayList a = new ArrayList();
			NodeItem n = (NodeItem) iter.next();
			Iterator edges = n.edges();
			
			while ( edges.hasNext()) {
	            EdgeItem eitem = (EdgeItem)edges.next();
	            NodeItem nitem = eitem.getAdjacentItem(n);
	            a.add(eitem.getAdjacentItem(n));
			}
			
			// update the location of the pattern node
			Point2D p = DisplayLib.getCentroid(a.iterator());
			n.setX(p.getX());
			n.setY(p.getY());
			
			logger.info("setting point for pattern=" + n.getString("title") + " to x=" + p.getX() + ", y=" + p.getY());
		}

	}

}
