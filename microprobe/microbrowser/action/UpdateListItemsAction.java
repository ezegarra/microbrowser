package microbrowser.action;

import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import microbrowser.VisualDBConfig;
import microbrowser.VisualDBConstants;
import prefuse.action.GroupAction;
import prefuse.visual.VisualItem;

public class UpdateListItemsAction extends GroupAction {
	Logger logger = Logger.getLogger(UpdateListItemsAction.class.getName());

	DefaultListModel listMenuModel;
	
	public UpdateListItemsAction(String group, DefaultListModel listMenuModel) {
		super(group);
		
		this.listMenuModel = listMenuModel;
	}

	@Override
	public void run(double frac) {
        // clock in
        long timein = System.currentTimeMillis();
        
		logger.info("Running");
		
        listMenuModel.clear();

        // since the jlist of items is updated asynchronously, lets update it using
        // a swinguitility asynch method
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Iterator<VisualItem> objects = (Iterator<VisualItem>)m_vis.visibleItems(m_group);
                while ( objects.hasNext()) {
                	VisualItem item = objects.next();
                	
                	if ( item != null && item.getInt("type") == VisualDBConstants.NODE_TYPE_DISCUSSION || item.getInt("type") == VisualDBConstants.NODE_TYPE_PATTERN) {
                    	listMenuModel.addElement(item);                		
                	}
                }
            }
            

        });        

        // clock out
        long time = System.currentTimeMillis()-timein;
		logger.info("Done in " + (time/1000) + "." + (time%1000) + " seconds.");
	}

}
