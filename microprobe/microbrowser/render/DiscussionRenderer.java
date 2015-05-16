package microbrowser.render;

import java.awt.Shape;

import microbrowser.VisualDBConstants;
import prefuse.render.ShapeRenderer;
import prefuse.visual.VisualItem;

public class DiscussionRenderer extends ShapeRenderer {

    private int m_baseSize = 20;
	
	protected Shape getRawShape(VisualItem item) {
		Shape s;
		int shapetype = getShapeType(item);
		
        double x = item.getX();
        if ( Double.isNaN(x) || Double.isInfinite(x) )
            x = 0;
        double y = item.getY();
        if ( Double.isNaN(y) || Double.isInfinite(y) )
            y = 0;
        double width = getShapeWidth(item);//*item.getSize();
        
        double height = getShapeHeight(item);
        
        // Center the shape around the specified x and y
        if ( width > 1 ) {
            x = x-width/2;
            y = y-width/2;
        }
        
		switch( shapetype ) {
			case VisualDBConstants.SHAPE_PATTERN: 
				s = triangle_up((float)x, (float)y, (float)width);
				break;
			case VisualDBConstants.SHAPE_QUESTION_ANSWERED:
				//s = hexagon((float)x, (float)y, (float)height);
				s = cross((float)x, (float)y, (float)height);
				//s = rectangle((float)x, (float)y, (float)width, (float)width);
				s = ellipse((float)x, (float)y, (float)width, (float)height);
				break;
			case VisualDBConstants.SHAPE_QUESTION_UNANSWERED:
				s = diamond((float)x, (float)y, (float)width);
				break;
			case VisualDBConstants.SHAPE_ANSWER:
				final double SIZE_LIMIT = 50;
				width = width > SIZE_LIMIT? SIZE_LIMIT : width;
				height = height > SIZE_LIMIT ? SIZE_LIMIT : height;

				//s = ellipse((float)x, (float)y, (float)width, (float)height);
				//s = cross((float)x, (float)y, (float)height);
				s = rectangle((float)x, (float)y, (float)width, (float)width);
				break;
			default: 
				s = ellipse(x, y, width, width);
				break;
		}
		return s;
	}

	private int getShapeType(VisualItem item) {
		// determine shape type based on item properties
		int type = item.getInt("type");
		
		// if it is a thread node, determine further classification
		switch ( item.getInt("type") ) {
		case VisualDBConstants.NODE_TYPE_DISCUSSION:
			if ( 0 == item.getInt("acceptedanswerid") ) {
				type = VisualDBConstants.SHAPE_QUESTION_UNANSWERED;
			} else {
				type = VisualDBConstants.SHAPE_QUESTION_ANSWERED;
			}
			break;
			
		case VisualDBConstants.NODE_TYPE_ANSWER:
		case VisualDBConstants.NODE_TYPE_ANSWER_ACCEPTED:
			type = VisualDBConstants.SHAPE_ANSWER;
			break;
		case VisualDBConstants.NODE_TYPE_PATTERN:	
			type = VisualDBConstants.SHAPE_PATTERN;
			break;
		}
					
		return type;
	}
	
	private double getShapeWidth(VisualItem item) {
		
		// determine shape type based on item properties
		int itemType = getShapeType(item);
		int width = m_baseSize;
		
		switch ( itemType ) {
		case VisualDBConstants.SHAPE_PATTERN:
			width += 15;
			break;
		case VisualDBConstants.SHAPE_QUESTION_UNANSWERED:
		case VisualDBConstants.SHAPE_QUESTION_ANSWERED:
			width += item.getInt("answercount");
			break;
		case VisualDBConstants.SHAPE_ANSWER:
			width += item.getInt("score"); 
			break;
		}
		return width / 1.5;
	}

	private double getShapeHeight(VisualItem item) {

		// determine shape type based on item properties
		int itemType = getShapeType(item);
		int height = m_baseSize;

		switch ( itemType ) {
		case VisualDBConstants.SHAPE_QUESTION_ANSWERED:
		case VisualDBConstants.SHAPE_QUESTION_UNANSWERED:
			height += item.getInt("answercount");
			break;
		case VisualDBConstants.SHAPE_ANSWER:
			height += item.getInt("score");
			break;				
		}
		return height / 1.5;
	}
}
