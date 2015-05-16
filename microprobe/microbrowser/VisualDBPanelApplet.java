package microbrowser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import netscape.javascript.JSObject;
import prefuse.util.ui.JPrefuseApplet;

public class VisualDBPanelApplet extends JPrefuseApplet implements ActionListener {
	private static final long serialVersionUID = -8268092529831579329L;
	JSObject win;
	MicroBrowserApplication visualDBPanel = null;
	
    public void init() {
    	
    	visualDBPanel = (MicroBrowserApplication) microbrowser.MicroBrowserApplication.createVisualization();
    	this.getContentPane().add(visualDBPanel);    
        win = JSObject.getWindow(this);
        
        
    }
    
    public void start() {
    	System.out.println("registering action listener");
    	//visualDBPanel.addActionListener(this);
    	
        
    	
    }
    
    public void actionPerformed(ActionEvent e) {
    	System.out.println("received event " + e);
    	if ( win != null) {
    		
            win.call("showThread", new String[]{e.getActionCommand()} );
    		
    	}
    }
} // end of class VisualDBPanelApplet
