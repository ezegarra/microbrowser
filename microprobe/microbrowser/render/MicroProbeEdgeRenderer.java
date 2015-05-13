package microbrowser.render;

import java.awt.geom.Point2D;

import prefuse.Constants;
import prefuse.render.EdgeRenderer;
import prefuse.visual.EdgeItem;
import prefuse.visual.VisualItem;

public class MicroProbeEdgeRenderer extends EdgeRenderer {

	public MicroProbeEdgeRenderer() {
		super(Constants.EDGE_TYPE_LINE);
		setArrowType(Constants.EDGE_ARROW_FORWARD);
		setArrowHeadSize(10, 10);
	}
	
    /**
     * Determines the control points to use for cubic (Bezier) curve edges. 
     * Override this method to provide custom curve specifications.
     * To reduce object initialization, the entries of the Point2D array are
     * already initialized, so use the <tt>Point2D.setLocation()</tt> method rather than
     * <tt>new Point2D.Double()</tt> to more efficiently set custom control points.
     * @param eitem the EdgeItem we are determining the control points for
     * @param cp array of Point2D's (length >= 2) in which to return the control points
     * @param x1 the x co-ordinate of the first node this edge connects to
     * @param y1 the y co-ordinate of the first node this edge connects to
     * @param x2 the x co-ordinate of the second node this edge connects to
     * @param y2 the y co-ordinate of the second node this edge connects to
     */
    protected void getCurveControlPoints(EdgeItem eitem, Point2D[] cp, 
                    double x1, double y1, double x2, double y2) 
    {
        double dx = x2-x1, dy = y2-y1;      
        //cp[0].setLocation(x1+2*dx/3,y1);
        //cp[1].setLocation(x2-dx/8,y2-dy/8);
        cp[0].setLocation(x1+2*dx,y1);
        cp[1].setLocation(x2,y2);
    }
    
    /**
     * Returns the line width to be used for this VisualItem. By default,
     * returns the base width value set using the {@link #setDefaultLineWidth(double)}
     * method, scaled by the item size returned by
     * {@link VisualItem#getSize()}. Subclasses can override this method to
     * perform custom line width determination, however, the preferred
     * method is to change the item size value itself.
     * @param item the VisualItem for which to determine the line width
     * @return the desired line width, in pixels
     */
    protected double getLineWidth(VisualItem item) {

    	//Double similarity = item.getDouble("similarity") * 5;
    	
        //return similarity > 1 ? similarity.doubleValue(): item.getSize();
    	return 1.7;
    }
}
