package microbrowser.action;

import java.util.Iterator;

import prefuse.Constants;
import prefuse.Visualization;
import prefuse.action.GroupAction;
import prefuse.data.Graph;
import prefuse.data.expression.Predicate;
import prefuse.data.tuple.TupleSet;
import prefuse.data.util.BreadthFirstIterator;
import prefuse.data.util.FilterIterator;
import prefuse.util.PrefuseLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

/*
 * The filter shows up to n neighbords for a focused node to limit
 * the number of items displayed on the panels
 */
public class VisualOverviewNeighborLimitFilterAction extends GroupAction {

    protected int m_limit;
    protected String m_sources;
    protected Predicate m_groupP;
    protected BreadthFirstIterator m_bfs;
    
    public VisualOverviewNeighborLimitFilterAction(String group, int limit) {
        this(group, Visualization.FOCUS_ITEMS, limit);
    }
    
    public VisualOverviewNeighborLimitFilterAction(String group, String sources, int limit)
    {
        super(group);
        m_sources 	= sources;
        m_limit 	= limit;
        m_groupP 	= new InGroupPredicate( PrefuseLib.getGroupName(group, Graph.NODES));
        m_bfs = new BreadthFirstIterator();
    }
    
	@Override
	public void run(double frac) {
        // mark the items
        Iterator<?> items = m_vis.visibleItems(m_group);
        while ( items.hasNext() ) {
            VisualItem item = (VisualItem)items.next();
            item.setDOI(Constants.MINIMUM_DOI);
        }
        
        // set up the graph traversal
        TupleSet src = m_vis.getGroup(m_sources);
        Iterator<?> srcs = new FilterIterator(src.tuples(), m_groupP);
        m_bfs.init(srcs, 1, Constants.NODE_AND_EDGE_TRAVERSAL);
        
        // traverse the graph looking for nodes
        int count = 0;
        while ( m_bfs.hasNext() ) {
            VisualItem item = (VisualItem)m_bfs.next();

            //int d = m_bfs.getDepth(item);
            item.setExpanded(true);
            
            if ( m_vis.isInGroup(item, m_group+".nodes") && "1".equals(item.getString("type")) ) {
            	
            	if ( count < m_limit ) {
                    PrefuseLib.updateVisible(item, true);
                    item.setDOI(0);
                	count++;            		
            	}
            } else {
                PrefuseLib.updateVisible(item, true);
                item.setDOI(0);            	
            }
        }
                
        // mark unreached items
        items = m_vis.visibleItems(m_group);
        while ( items.hasNext() ) {
            VisualItem item = (VisualItem)items.next();
            if ( item.getDOI() == Constants.MINIMUM_DOI ) {
                PrefuseLib.updateVisible(item, false);
                item.setExpanded(false);
            }
        }

	}

}
