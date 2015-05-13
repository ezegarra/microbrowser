package microbrowser.control;

import java.awt.FontMetrics;
import java.awt.event.MouseEvent;

import javax.swing.ToolTipManager;

import microbrowser.VisualDBConstants;
import microbrowser.util.DateLib;
import microbrowser.util.StringUtil;
import prefuse.Display;
import prefuse.controls.Control;
import prefuse.controls.ControlAdapter;
import prefuse.render.Renderer;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

public class NodeInfoTooltipControl extends ControlAdapter implements Control {
	
	public void itemEntered(VisualItem item, MouseEvent e) 
	{
		if(item instanceof NodeItem)
		{
	        Display d = (Display)e.getSource();
	        String text = "";        
	        FontMetrics fm = Renderer.DEFAULT_GRAPHICS.getFontMetrics(item.getFont());
	        final int LINE_WIDTH = 100;
	        
	        int type = item.getInt("type");
        	String body =  item.getString("body");
        	String title = item.getString("title");
        	
	        switch ( type ) {
		        case VisualDBConstants.NODE_TYPE_DISCUSSION:
	            	text =  "<html><body><b>" + StringUtil.toMultiLine(title, "<br />", LINE_WIDTH) + "</b><br /><b>" + item.getInt("answercount") + "</b> answers<br />Tags: " +  item.getString("tags");
	            	text += "<br />Last activity date: " + DateLib.formatDate(item.getLong("lastactivitydate"));
	            	text += "<br />Discussion id: <b>" + item.getInt("id") + "</b> &nbsp; &nbsp; Views: " + item.getInt("viewcount") + "</body></html>";
		            break;
		        case VisualDBConstants.NODE_TYPE_ANSWER:
		        case VisualDBConstants.NODE_TYPE_ANSWER_ACCEPTED:
		        	if ( body.length() > LINE_WIDTH * 10)
		        		body = body.substring(0, LINE_WIDTH * 10);
		        	
		        	text = "<html><body>" + StringUtil.toMultiLine(body, "<br />", LINE_WIDTH) + "</body></html>";
		        	break;
		        case VisualDBConstants.NODE_TYPE_LABEL:
		        	text = null;
		        	break;
		        case VisualDBConstants.NODE_TYPE_PATTERN:
		            text = "<html><h3>" + StringUtil.toMultiLine(title, "<br />", LINE_WIDTH) + "</h3>";
		            text += "<b>Description</b><br />";
		            text +=  StringUtil.toMultiLine(body, "<br />", LINE_WIDTH);
		            text += "<br /><b>Solution</b><br />";
		            text += StringUtil.toMultiLine(item.getString("solution"), "<br />", LINE_WIDTH);
		            text += "<br />Pattern id: " + item.getInt("id") + "</html>";
		            break;
		        case VisualDBConstants.NODE_TYPE_AGGREGATE:
		        	text += "Emilio<br />";
		        	break;
	            default:
		            text = "<html>" + StringUtil.toMultiLine(title, "<br />", LINE_WIDTH) + "</html>";
	        }
	        ToolTipManager.sharedInstance().setInitialDelay(0);
	        d.setToolTipText(text);
		} 
		else if ( item instanceof EdgeItem) {
			Display d = (Display)e.getSource();
			d.setToolTipText("similarity=" + item.getDouble("similarity"));
		}
	}
	
    public void itemExited(VisualItem item, MouseEvent e) {
    	Display d = (Display)e.getSource();
    	d.setToolTipText(null);
    } 
}
