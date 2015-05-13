package microbrowser.control;

import java.awt.event.MouseEvent;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import microbrowser.ui.DiscussionOverviewPane;
import microbrowser.util.TraceService;
import prefuse.controls.ControlAdapter;
import prefuse.data.Node;
import prefuse.visual.VisualItem;
/**
 * Interactive drag control that is "aggregate-aware"
 */
public class DiscussionOverviewPaneControlAdapter extends ControlAdapter {
	private static Logger logger = Logger.getLogger(DiscussionOverviewPaneControlAdapter.class.getName());
	DiscussionOverviewPane overviewPane = null;
    
	public DiscussionOverviewPaneControlAdapter(DiscussionOverviewPane parent) {
		super();
		this.overviewPane = parent;
	}
	/**
	 * @see prefuse.controls.Control#itemClicked(VisualItem item, java.awt.event.MouseEvent e) 
	 */
	public void itemClicked(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        
		if ( e.getClickCount() == 2 ) {
			logger.info("id="+ item.getString("id")+", type=" + item.getInt("type"));

			this.overviewPane.parent.openDiscussionDetails((Node)item);
			
			TraceService.log(TraceService.EVENT_DIAG_ITEMCLICKED, "id="+ item.getString("id")+", type=" + item.getInt("type"));			
		}
	}
	
    /**
     * @see prefuse.controls.Control#itemEntered(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemEntered(VisualItem item, MouseEvent e) {

        // mark the current item as selected from the list
    	overviewPane.listMenu.clearSelection();
        overviewPane.listMenu.setSelectedValue(item, true);
        
        // update teh content of the tags display
        overviewPane.m_prefuseTable.getTable().clear();
        StringTokenizer st = new StringTokenizer(item.getString("tags"), ",");
        while ( st.hasMoreTokens()) {
        	overviewPane.m_prefuseTable.getTable().setString(overviewPane.m_prefuseTable.getTable().addRow(), "TAGS", st.nextToken());
            overviewPane.m_prefuseTable.setVisible(true);
        }
        
    	TraceService.log(TraceService.EVENT_DIAG_ITEMENTERED, "id="+ item.getString("id")+", type=" + item.getInt("type"));
    }
    
    /**
     * @see prefuse.controls.Control#itemExited(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemExited(VisualItem item, MouseEvent e) {
    	
        // clear item selections
        overviewPane.listMenu.clearSelection();
        
        // clear the items in the tags list 
        overviewPane.m_prefuseTable.getTable().clear();
        overviewPane.m_prefuseTable.setVisible(false);
        
    	TraceService.log(TraceService.EVENT_DIAG_ITEMEXITED, "id="+ item.getString("id")+", type=" + item.getInt("type"));
    } 
    
    public void itemPressed(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
    }
    
    public void itemReleased(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
    }
    
    @Override
    public void itemDragged(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
    	
    	TraceService.log(TraceService.EVENT_DIAG_ITEMDRAGGED, "id=" + item.getString("id") + ", type=" + item.getInt("type"));
    }
}
