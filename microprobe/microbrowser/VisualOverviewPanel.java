package microbrowser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import microbrowser.action.VisualOverviewNeighborLimitFilterAction;
import microbrowser.data.io.GraphMLDataReader;
import microbrowser.render.VisualOverviewRenderer;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.ToolTipControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.render.DefaultRendererFactory;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.ui.JFastLabel;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;

public class VisualOverviewPanel extends JPanel {
	private static final long serialVersionUID = 5692963271742836690L;
	
    private static final String graph = "graph";
    private static final String nodes = "graph.nodes";
    private static final String edges = "graph.edges";
    
	private Visualization m_vis;
	private GraphMLDataReader m_graphreader;
	
	private VisualItem m_item;
	
	public VisualOverviewPanel(String threadId) {
		super(new BorderLayout());
		
        // --------------------------------------------------------------------
        // create actions to process the visual data     
        final VisualOverviewNeighborLimitFilterAction filter = new VisualOverviewNeighborLimitFilterAction(graph, 10);
        
        DataColorAction fill = new DataColorAction(nodes, "type", Constants.NOMINAL, VisualItem.FILLCOLOR, ColorLib.getCategoryPalette(4));
        fill.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255,100,125));
        
        ActionList draw = new ActionList();
        draw.add(filter);
        draw.add(fill);
        draw.add(new ColorAction(nodes, VisualItem.STROKECOLOR, ColorLib.gray(1)));
        draw.add(new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0)));
        draw.add(new ColorAction(edges, VisualItem.FILLCOLOR, ColorLib.gray(200)));
        draw.add(new ColorAction(edges, VisualItem.STROKECOLOR, ColorLib.gray(100)));
        
        ActionList animate = new ActionList(Activity.INFINITY);
        //ActionList animate = new ActionList();
        animate.add(new ForceDirectedLayout(graph, true));
        //animate.add(new RandomLayout(graph));
        //animate.add(new CircleLayout(graph));
        animate.add(fill);
        animate.add(new RepaintAction());

        // create a new, empty visualization for our data
		m_graphreader = new GraphMLDataReader();
        m_vis = new Visualization();
        
        
        // 3643951
        int row = 0;
        Iterator<?> iter = m_graphreader.getSourceGraph().nodes();
        
        while ( iter.hasNext() ) {
        	Node n = (Node) iter.next();
        	
        	if ( n.get("id").equals(threadId)) {
        		row = n.getRow();
        		break;
        	}
        }
        setGraph(m_graphreader.getSourceGraph(), row);
        
        

        // finally, we register our ActionList with the Visualization.
        // we can later execute our Actions by invoking a method on our
        // Visualization, using the name we've chosen below.
        m_vis.putAction("draw", draw);
        m_vis.putAction("layout", animate);

        m_vis.runAfter("draw", "layout");
        
        // --------------------------------------------------------------------
        // set up a display to show the visualization
        
        Display display = new Display(m_vis);
        display.setSize(500,500);
        display.pan(250, 250);
        display.setForeground(Color.GRAY);
        display.setBackground(Color.WHITE);
        display.setHighQuality(true);
        
        // main display controls
        display.addControlListener(new FocusControl(1));
        display.addControlListener(new DragControl());
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new WheelZoomControl());
        display.addControlListener(new ZoomToFitControl());
        display.addControlListener(new NeighborHighlightControl());
        display.addControlListener(new ToolTipControl("title"));
        
        
        final JFastLabel title = new JFastLabel(m_item.getString("title"));
        title.setPreferredSize(new Dimension(350, 40));
        title.setVerticalAlignment(SwingConstants.BOTTOM);
        title.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
        title.setFont(FontLib.getFont("Tahoma", Font.BOLD, 18));
        
        final JFastLabel label = new JFastLabel();
        label.setPreferredSize(new Dimension(400, 30));
        label.setVerticalAlignment(SwingConstants.BOTTOM);
        label.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
        label.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 16));
        
        display.addControlListener(new ControlAdapter() {
            public void itemEntered(VisualItem item, MouseEvent e) {
            	label.setText(item.getString("title"));
            }
            public void itemExited(VisualItem item, MouseEvent e) {
            	label.setText(null);
            }
        });
        
        // add UI elements to the pane
        this.add(title, BorderLayout.NORTH);
        this.add(display);
        this.add(label, BorderLayout.SOUTH);
        
        // now we run our action list
        m_vis.run("draw");
	}

    
    public void setGraph(Graph g, int focusRow) {
        
    	//ShapeRenderer r = new ShapeRenderer();
    	VisualOverviewRenderer r = new VisualOverviewRenderer();
    	m_vis.setRendererFactory(new DefaultRendererFactory(r));
    	
        // update graph
        m_vis.removeGroup(graph);
        VisualGraph vg = m_vis.addGraph(graph, g);
        m_vis.setValue(edges, null, VisualItem.INTERACTIVE, Boolean.FALSE);
        VisualItem f = (VisualItem)vg.getNode(focusRow);
        
        f.setString("type", "2");
        
        m_vis.getGroup(Visualization.FOCUS_ITEMS).setTuple(f);
        f.setFixed(true);
        f.setInteractive(false);
        f.setX(0);
        f.setY(0);
 
        m_item = f;
    }
    
	public static void main(String [] args) {
		
		VisualOverviewPanel view = new VisualOverviewPanel("3643951");
        JFrame frame = new JFrame("MicroBrowser - Related Threads");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(view);
        frame.pack();
        frame.setVisible(true);
	}
}
