package microbrowser.action;

import java.awt.geom.Rectangle2D;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.util.GraphicsLib;
import prefuse.util.display.DisplayLib;

public class ZoomGraphToFit extends Action {

	@Override
	public void run(double frac) {
		
    	Display display = (Display)m_vis.getDisplay(0);
    	if ( !display.isTranformInProgress() ) 
        {
            Visualization vis = display.getVisualization();
            Rectangle2D bounds = vis.getBounds(Visualization.ALL_ITEMS);
            GraphicsLib.expand(bounds, 0);
            DisplayLib.fitViewToBounds(display, bounds, 0);
        }

	}

}
