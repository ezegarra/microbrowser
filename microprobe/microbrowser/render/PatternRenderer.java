package microbrowser.render;

import prefuse.Constants;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.LabelRenderer;

public class PatternRenderer extends LabelRenderer {
	
	public PatternRenderer() {
		super("title");
		setHorizontalPadding(5);
        setVerticalPadding(5);
        setRoundedCorner(8, 8);
        setMaxTextWidth(100);
		setRenderType(AbstractShapeRenderer.RENDER_TYPE_FILL);
		setHorizontalAlignment(Constants.CENTER);
		setManageBounds(true);
	}

}
