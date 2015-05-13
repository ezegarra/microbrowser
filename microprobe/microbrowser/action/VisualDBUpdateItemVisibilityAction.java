package microbrowser.action;

import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import prefuse.Visualization;
import prefuse.action.GroupAction;
import prefuse.data.Tuple;
import prefuse.util.PrefuseLib;
import prefuse.visual.VisualItem;

public class VisualDBUpdateItemVisibilityAction extends GroupAction {
	private static Logger logger = Logger.getLogger(VisualDBUpdateItemVisibilityAction.class.getName());
	DefaultListModel listMenuModel;
	
	public VisualDBUpdateItemVisibilityAction(String group, DefaultListModel listMenuModel) {
		super(group);
		
		this.listMenuModel = listMenuModel;
	}

	@Override
	public void run(double frac) {
		logger.info("running actions");
        Iterator<?> visible_items = m_vis.items(m_group);
        while ( visible_items.hasNext() ) {
        	VisualItem item = (VisualItem) visible_items.next();
        	if ( item.getRow() > -1 && item.isValid() )
       		PrefuseLib.updateVisible(item, false);        		
        }

        Iterator<?>items = null;
        
        if ( Visualization.ALL_ITEMS.equals(m_group)) {
        	items = m_vis.items("graph.nodes");
        	
            while ( items.hasNext() ) {
            	VisualItem item = (VisualItem) items.next();
            	PrefuseLib.updateVisible(item, true);
            }
           

        } else {
        	items = m_vis.getFocusGroup(m_group).tuples();

        	while ( items.hasNext() ) {
                Tuple t = (Tuple)items.next();
                VisualItem item = m_vis.getVisualItem(m_group, t);
                if ( item != null && item.isValid() ) {
                	PrefuseLib.updateVisible(item, true);                	
                }
            }
        }

        // since the jlist of items is updated asynchronously, lets update it using
        // a swinguitility asynch method
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Iterator<VisualItem> objects = (Iterator<VisualItem>)m_vis.visibleItems("graph.nodes");
                listMenuModel.clear();
                while ( objects.hasNext()) {
                	VisualItem item = objects.next();
                	
                	if ( item == null ) {
                		logger.info("item is null " + item);
                	}
                	listMenuModel.addElement(item);
                }
            }
        });        
	}

}
