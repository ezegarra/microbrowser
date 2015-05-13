package microbrowser.render;

import java.awt.Shape;

import prefuse.render.ShapeRenderer;
import prefuse.visual.VisualItem;

public class VisualOverviewRenderer extends ShapeRenderer {

    private int m_baseSize = 30;
	
	protected Shape getRawShape(VisualItem item) {
		Shape s;
		
		int type = item.getInt("type");
        double x = item.getX();
        if ( Double.isNaN(x) || Double.isInfinite(x) )
            x = 0;
        double y = item.getY();
        if ( Double.isNaN(y) || Double.isInfinite(y) )
            y = 0;
        double width = m_baseSize*item.getSize();
        
        // Center the shape around the specified x and y
        if ( width > 1 ) {
            x = x-width/2;
            y = y-width/2;
        }
        
		switch( type ) {
			case 0: // pattern
				s = triangle_up((float)x, (float)y, (float)width);
				break;
			case 2: // this is the main item for the overview
				s = ellipse(x, y, width + 20, width + 20);
				//s = diamond((float)x, (float)y, (float)width);
				break;
			default: // type=1, thread
				s = ellipse(x, y, width, width);
				//s = diamond((float)x, (float)y, (float)width);
				break;
		}
		return s;
	}

}
