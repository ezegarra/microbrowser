package microbrowser.control;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import microbrowser.VisualDBConstants;
import microbrowser.ui.DiscussionDetailsPane;
import microbrowser.util.TraceService;
import prefuse.Display;
import prefuse.controls.ControlAdapter;
import prefuse.data.Node;
import prefuse.visual.VisualItem;

public class DiscussionDetailPaneControl extends ControlAdapter {
	private static Logger logger = Logger.getLogger(DiscussionDetailPaneControl.class.getName());
	
	private DiscussionDetailsPane discussionDetailsPane;
	
	public DiscussionDetailPaneControl(DiscussionDetailsPane discussionDetailsPane) {
		this.discussionDetailsPane = discussionDetailsPane;
	}
	
	@Override
	public void itemClicked(VisualItem item, MouseEvent e) {
		logger.info("item=" + item.getInt("id") + ", type=" + item.getInt("type") );
		
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        
        int type = item.getInt("type");
        if ( e.getClickCount() == 2 ) {
        	if (type == VisualDBConstants.NODE_TYPE_DISCUSSION || 
        			type == VisualDBConstants.NODE_TYPE_PATTERN ) {
        		discussionDetailsPane.parent.openDiscussionDetails((Node)item);
        	}
        } else {
    		switch ( type) {
    		case VisualDBConstants.NODE_TYPE_ANSWER:
    		case VisualDBConstants.NODE_TYPE_ANSWER_ACCEPTED:
    			//discussionDetailsPane.showSelectedAnswer(item);
    			discussionDetailsPane.setSelectedAnswer(item);
    			break;
    		case VisualDBConstants.NODE_TYPE_DISCUSSION:
    			discussionDetailsPane.showSelectedQuestion(item);
    			break;
    		}        	
        }
        
        TraceService.log(TraceService.EVENT_DIAG_DETAIL_ITEMCLICKED, "id="+ item.getString("id")+", type=" + item.getInt("type"));
	}
	
	
    /**
     * @see prefuse.controls.Control#itemEntered(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemEntered(VisualItem item, MouseEvent e) {
		logger.info("item=" + item.getInt("id") + ", type=" + item.getInt("type") );
		
    	int type = item.getInt("type");
        // mark the current item as selected from the list
    	if ( type == VisualDBConstants.NODE_TYPE_ANSWER || type == VisualDBConstants.NODE_TYPE_ANSWER_ACCEPTED) {
            
        	TraceService.log(TraceService.EVENT_DIAG_DETAIL_ITEMENTERED_ANSWER, "id="+ item.getString("id")+", type=" + item.getInt("type"));
        	
    		discussionDetailsPane.showSelectedAnswer(item, VisualDBConstants.ORIGIN_VIEW_DETAILS_DIAGRAM);
    		discussionDetailsPane.setSelectedAnswer(item);
    	} else {
            discussionDetailsPane.selectItemFromList(item);    		
    	}

    	String event = null;
    	
    	switch ( type ) {
    	case VisualDBConstants.NODE_TYPE_ANSWER:
    	case VisualDBConstants.NODE_TYPE_ANSWER_ACCEPTED:
    		event = TraceService.EVENT_DIAG_DETAIL_ITEMENTERED_ANSWER;
    		break;
    	case VisualDBConstants.NODE_TYPE_PATTERN:
    		event = TraceService.EVENT_DIAG_DETAIL_ITEMENTERED_PATTERN;
    		break;
    	case VisualDBConstants.NODE_TYPE_DISCUSSION:
    		event = TraceService.EVENT_DIAG_DETAIL_ITEMENTERED_DISCUSSION;
    		break;
    	case VisualDBConstants.NODE_TYPE_LABEL:
    		event = TraceService.EVENT_DIAG_DETAIL_ITEMENTERED_LABEL;
    		break;
    	case VisualDBConstants.NODE_TYPE_AGGREGATE:
    		event = TraceService.EVENT_DIAG_DETAIL_ITEMENTERED_AGGREGATE;
    		break;
    	}
    	
    	if ( event != null ) {
    		TraceService.log(event, "id="+ item.getString("id")+", type=" + item.getInt("type"));
    	}
    	
    }
    
    /**
     * @see prefuse.controls.Control#itemExited(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemExited(VisualItem item, MouseEvent e) {
		logger.info("item=" + item.getInt("id") + ", type=" + item.getInt("type") );
		
        Display d = (Display)e.getSource();
        d.setToolTipText(null);
        d.setCursor(Cursor.getDefaultCursor());

        // clear item selections
        discussionDetailsPane.selectItemFromList(null);
        
    	TraceService.log(TraceService.EVENT_DIAG_DETAIL_ITEMEXITED, "id="+ item.getString("id")+", type=" + item.getInt("type"));
    } 
}
