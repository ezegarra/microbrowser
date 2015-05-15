package microbrowser.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import microbrowser.VisualDBApplication;
import microbrowser.VisualDBConfig;
import microbrowser.VisualDBConstants;
import microbrowser.action.EdgeStrokeColorAction;
import microbrowser.action.EdgeVisibilityAction;
import microbrowser.action.FillColorAction;
import microbrowser.action.NodeBorderColorAction;
import microbrowser.action.PatternLocationAction;
import microbrowser.action.PatternVisibilityAction;
import microbrowser.action.TextColorAction;
import microbrowser.action.UpdateListItemsAction;
import microbrowser.action.ZoomGraphToFit;
import microbrowser.control.DiscussionOverviewPaneControlAdapter;
import microbrowser.control.NodeInfoTooltipControl;
import microbrowser.data.io.GraphMLDataReader;
import microbrowser.render.DiscussionRenderer;
import microbrowser.render.MicroProbeEdgeRenderer;
import microbrowser.render.PatternRenderer;
import microbrowser.render.VisualDBListRenderer;
import microbrowser.util.DateLib;
import microbrowser.util.GraphUtil;
import microbrowser.util.PredicatesUtil;
import microbrowser.util.TraceService;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.assignment.DataSizeAction;
import prefuse.action.filter.VisibilityFilter;
import prefuse.action.layout.AxisLayout;
import prefuse.action.layout.Layout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.activity.ActivityAdapter;
import prefuse.activity.ActivityListener;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.HoverActionControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.expression.AndPredicate;
import prefuse.data.expression.OrPredicate;
import prefuse.data.query.ListQueryBinding;
import prefuse.data.query.SearchQueryBinding;
import prefuse.data.search.KeywordSearchTupleSet;
import prefuse.data.search.SearchTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.render.PolygonRenderer;
import prefuse.render.Renderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.GraphicsLib;
import prefuse.util.PrefuseLib;
import prefuse.util.TimeLib;
import prefuse.util.UpdateListener;
import prefuse.util.force.DragForce;
import prefuse.util.force.ForceItem;
import prefuse.util.force.ForceSimulator;
import prefuse.util.force.NBodyForce;
import prefuse.util.force.SpringForce;
import prefuse.util.ui.JFastLabel;
import prefuse.util.ui.JRangeSlider;
import prefuse.util.ui.JSearchPanel;
import prefuse.util.ui.JValueSlider;
import prefuse.util.ui.UILib;
import prefuse.visual.AggregateItem;
import prefuse.visual.AggregateTable;
import prefuse.visual.DecoratorItem;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.HoverPredicate;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.sort.ItemSorter;

import com.michaelbaranov.microba.calendar.DatePicker;
import com.michaelbaranov.microba.calendar.VetoPolicy;
import com.michaelbaranov.microba.common.PolicyListener;

public class DiscussionOverviewBySimilarityPane extends DiscussionOverviewPane {
	private static final long serialVersionUID = -8279873905415635170L;
	private static Logger logger = Logger.getLogger(DiscussionOverviewBySimilarityPane.class.getName());
	
	private Display display = null;
	private Display overview = null;
	private GraphMLDataReader m_graphreader = null;
	
    private static final String graph = "graph";
    private static final String nodes = "graph.nodes";
    private static final String edges = "graph.edges";
    public static final String AGGR = "aggregates";

    public static final String AGGR_DECORATORS = "aggrDeco";
    private static final Schema DECORATOR_SCHEMA = PrefuseLib.getVisualItemSchema(); 
    static { 
        DECORATOR_SCHEMA.setDefault(VisualItem.INTERACTIVE, false); 
        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(3)); 
        DECORATOR_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma",16));
    }
    
	private DefaultListModel listMenuModel;
	private JScrollPane listScrollPane;
	private JScrollPane patternListScrollPane;
	
	// the query bindings to perform the filtering
    private ListQueryBinding   typesQ    = null;
    private SearchQueryBinding searchQ   = null;
    
    private Rectangle2D m_dataB = new Rectangle2D.Double();
    
	public DiscussionOverviewBySimilarityPane(VisualDBApplication parent) {
		super(parent);
		
		this.listMenuModel = new DefaultListModel();
        // load data
		this.setUpData();
		
		this.setRenderers();
		this.setActions();
		this.setControls();

		// define the ui
		setLayout(new BorderLayout());
		add(buildCentralPanel(), BorderLayout.CENTER);
		add(buildEastPanel(), BorderLayout.EAST);
		add(buildSouthPanel(), BorderLayout.SOUTH);
		
		// run the action
        // filter graph and perform layout
        
		// initialize the listing with the current visible nodes
		this.updateList(this.m_graph);

	}
	

	// -- 1. load the data ------------------------------------------------
	public void setUpData()
	{	
		TraceService.log(TraceService.EVENT_SETUP_LOADDATA_BEGIN, null);
		
		// Create the Visualization object.
		m_vis = new Visualization();

		display = new Display(m_vis);
		display.addComponentListener(new ComponentAdapter() {
			  public void componentResized(java.awt.event.ComponentEvent evt){
				 logger.info("componentResized w:" + VisualDBConfig.DISPLAY_SIZE_WIDTH + "==" + display.getWidth() + " - h: " + VisualDBConfig.DISPLAY_SIZE_HEIGHT + "==" + display.getHeight());

				  // change the location of the overview panel based on the
				  // new display dimension
				  if ( display.getHeight() != VisualDBConfig.DISPLAY_SIZE_HEIGHT  || display.getWidth() != VisualDBConfig.DISPLAY_SIZE_WIDTH) {
					 // display.setSize(VisualDBConfig.DISPLAY_SIZE_WIDTH, VisualDBConfig.DISPLAY_SIZE_HEIGHT);
					  if ( overview != null ) {
						  overview.setLocation(0, display.getHeight()-overview.getHeight());
						  m_prefuseTable.setLocation(100, display.getHeight()-m_prefuseTable.getHeight());
					  }
					  
					  //m_vis.run("draw");
					  //m_vis.runAfter("draw", "color");
					  //m_vis.runAfter("color", "zoom");
					  displayLayout();
					  
				  }
				  
			  }
		});
        //displayLayout();
        
		/* read the graph data */
		m_graphreader = new GraphMLDataReader();

		this.setGraph(m_graphreader.getGraph(VisualDBConfig.SLIDER_INITIAL_VALUE,
					(VisualDBConfig.EXPERIMENT_MODE == VisualDBConstants.EXPERIMENT_MODE_PATTERN_ONLY || 
					VisualDBConfig.EXPERIMENT_MODE == VisualDBConstants.EXPERIMENT_MODE_PATTERN_LEADERBOARD),
					VisualDBConfig.RANGE_SLIDER_LOW_VALUE,
					VisualDBConfig.RANGE_SLIDER_HIGH_VALUE));
		
		// create aggregates
		this.createAggregates(this.m_graph);
		
		TraceService.log(TraceService.EVENT_SETUP_LOADDATA_END, null);
	}
	
	/**
	 * Creates the aggragate rules
	 * @param m_graph
	 */
	private void createAggregates(Graph m_graph) {
        AggregateTable at = m_vis.addAggregates(AGGR);
        at.addColumn(VisualItem.POLYGON, float[].class);
        at.addColumn("id", int.class);
        at.addColumn("label", String.class, "");
        at.addColumn("type", int.class, VisualDBConstants.NODE_TYPE_AGGREGATE);
		
        VisualGraph vg = (VisualGraph) m_vis.getGroup(graph);
        Iterator nodes;
        
        // if we agregate by pattern follow this logic
        switch(  VisualDBConfig.AGGREGATE_TYPE ) {
        
	        case VisualDBConstants.AGGREGATE_TYPE_PATTERN:
	            // add nodes to aggregates
	            nodes = vg.getNodes().tuples(PredicatesUtil.TYPE_PATTERN);
	            while ( nodes.hasNext()) {
	            	VisualItem i = (VisualItem)nodes.next();
	                AggregateItem aitem = (AggregateItem)at.addItem();
	                aitem.setInt("id", i.getInt("id"));
	                aitem.setString("label", i.getString("title"));
	                aitem.addItem(i);
	                
	                // get related nodes
	                Iterator il = vg.edges(vg.getNode(i.getRow()));
	                while ( il.hasNext()) {
	                	Edge j = (Edge)il.next();
	                	VisualItem k = (VisualItem) j.getTargetNode();
	                	aitem.addItem(k);
	                }
	            }        	
	            break;
            
	        case VisualDBConstants.AGGREGATE_TYPE_TAG:
	        	
	        	nodes = vg.getNodes().tuples(PredicatesUtil.TYPE_DISCUSSION);
	        	Map<String, AggregateItem> aggregates = new HashMap<String, AggregateItem>();
	        	while ( nodes.hasNext()) {
	        		VisualItem item = (VisualItem) nodes.next();
	                StringTokenizer st = new StringTokenizer(item.getString("tags"), ",");
	                while ( st.hasMoreTokens()) {
	                	String tag = st.nextToken();
	                	if ( !tag.equalsIgnoreCase("java")) {
	                		AggregateItem aitem = null;	                	
		                	if ( aggregates.containsKey(tag)) {
		                		aitem = aggregates.get(tag);
		                	}
		                	else {
		                		aitem = (AggregateItem)at.addItem();
		    	                aitem.setInt("id", aggregates.size());
		    	                aitem.setString("label", tag);
		    	                aggregates.put(tag, aitem);
		                	}
		                	
		                	aitem.addItem(item);                		
	                	}
	                }
	        	}
	        }
        
	}


	/**
	 * Associates the given graph to the visualization
	 * @param g
	 */
    private void setGraph(Graph g) {
        m_vis.add(graph, g);
        m_vis.setInteractive(edges, null, false);

        this.m_graph = g;
    }  
	
	
	private void updateList(Graph g) {
		logger.info("running update list");
		
		m_vis.run("updateList");
		
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				listMenuModel.clear();
//
//				// load all patterns
//				Iterator <Pattern> patterns = GraphUtil.getAllPatterns(m_graph);		
//				while ( patterns.hasNext()) {
//					Pattern p = patterns.next();
//					Tuple t = GraphMLDataReader.getTupleById(m_graph, new Integer(p.getId()));
//					VisualItem vi = m_vis.getVisualItem("graph.nodes", t);
//					listMenuModel.addElement(vi);
//				}
//
//				// load all discussions
//				Iterator <Discussion> discussions = GraphUtil.getAllDiscussions(m_graph);
//				while ( discussions.hasNext()) {
//					Discussion d = discussions.next();
//					Tuple t = GraphMLDataReader.getTupleById(m_graph, new Integer(d.getId()));
//					VisualItem vi = m_vis.getVisualItem("graph.nodes", t);
//					listMenuModel.addElement(vi);
//				} 	
//			}
//		});

		
	}
	
	// -- 3. the renderers and renderer factory ---------------------------
	public void setRenderers()
	{
		// define the default renderer, which is for all threads
    	DefaultRendererFactory drf = new DefaultRendererFactory(new DiscussionRenderer());
        // draw aggregates as polygons with curved edges
        Renderer polyR = new PolygonRenderer(Constants.POLY_TYPE_CURVE);
        ((PolygonRenderer)polyR).setCurveSlack(0.15f);
        
    	// Add a rendered for the patterns
        //drf.add("type = " + VisualDBConstants.NODE_TYPE_THREAD, new LabelRenderer("answercount"));
        drf.add("type = " + VisualDBConstants.NODE_TYPE_PATTERN, new PatternRenderer());
        drf.add(new InGroupPredicate(edges), new MicroProbeEdgeRenderer());
        drf.add("ingroup('aggregates')", polyR);
        drf.add(new InGroupPredicate(AGGR), polyR);
        drf.add(new InGroupPredicate(AGGR_DECORATORS), new LabelRenderer("label"));
        // set the render factory for the application
        m_vis.setRendererFactory(drf);
        
        // the HoverPredicate makes this group of decorators to appear only on mouseOver
       // DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(255, 128));
        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(100));
        DECORATOR_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma", Font.BOLD, 48));
        m_vis.addDecorators(AGGR_DECORATORS, AGGR, new HoverPredicate(), DECORATOR_SCHEMA);
	}
	

	public void setActions()
	{
		TraceService.log(TraceService.EVENT_SETUP_CREATEACTIONS_BEGIN, null);
		
		// Build search filters so they can be used in the actions here
        //SearchTupleSet search = new PrefixSearchTupleSet();
        SearchTupleSet search = new KeywordSearchTupleSet();
        m_vis.addFocusGroup(Visualization.SEARCH_ITEMS, search);
        search.addTupleSetListener(new TupleSetListener() {
            public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
            	logger.info("tupleChanged");
				String query = searchQ.getSearchSet().getQuery();
				TraceService.log(TraceService.EVENT_SEARCH_UPDATE, query);
				
                //m_vis.cancel("color");
            	//m_vis.run("draw");
            }
        });
        
        // create the search query binding
		Graph g = (Graph) m_vis.getGroup(graph);
        this.searchQ = new SearchQueryBinding(g.getNodeTable(), "searchfield", (SearchTupleSet)this.m_vis.getGroup(Visualization.SEARCH_ITEMS));
        this.typesQ  = new ListQueryBinding(g.getNodeTable(), "type", true);
        AndPredicate andPredicateFilters = new AndPredicate(this.searchQ.getPredicate());
        andPredicateFilters.add(this.typesQ.getPredicate());
        OrPredicate orPredicateFilter = new OrPredicate(andPredicateFilters, PredicatesUtil.TYPE_PATTERN);
		
		// add edge visibility action
		ActionList edgevisibility = new ActionList();
		edgevisibility.add(new EdgeVisibilityAction(edges));
		edgevisibility.add(new RepaintAction());
		this.m_vis.putAction("edgevisibility", edgevisibility);
        
        // create the filter list action
        ActionList updateList = new ActionList();
        updateList.add(new PatternVisibilityAction(m_vis));
        updateList.add(new UpdateListItemsAction(nodes, listMenuModel));
        updateList.add(edgevisibility);
        this.m_vis.putAction("updateList", updateList);
        
		AxisLayout x_axis = new AxisLayout(nodes, "x", Constants.X_AXIS);
        x_axis.setLayoutBounds(m_dataB);
        m_vis.putAction("x", x_axis);

		AxisLayout y_axis = new AxisLayout(nodes, "y", Constants.Y_AXIS);
        y_axis.setLayoutBounds(m_dataB);
        m_vis.putAction("y", y_axis);
		
		/*
		 * Define the action that handles the coloring of the nodes and edges
		 */
		
        ColorAction aStroke = new ColorAction(AGGR, VisualItem.STROKECOLOR);
        aStroke.setDefaultColor(ColorLib.gray(200));
        aStroke.add("_hover", ColorLib.rgb(255,100,100));
        
        int[] palette = new int[] {
            ColorLib.rgba(255,200,200,150),
            ColorLib.rgba(200,255,200,150),
            ColorLib.rgba(200,200,255,150),
            ColorLib.rgb(255,255,204),
            ColorLib.rgb(208,209,230)
        };

        
		ActionList color = new ActionList();
		DataSizeAction itemSizeAction = new DataSizeAction(nodes,"answercount",40);
		itemSizeAction.setMaximumSize(10);
		color.add(new TextColorAction(nodes));
		color.add(new ColorAction(edges, VisualItem.FILLCOLOR, ColorLib.rgb(100,100,100)));
		color.add(new EdgeStrokeColorAction(edges));
		color.add(itemSizeAction);
		color.add(new FillColorAction(nodes));
		color.add(new DataColorAction(AGGR, "id", Constants.NOMINAL, VisualItem.FILLCOLOR, palette));
		color.add(aStroke);
		//color.add(new ColorAction(nodes, VisualItem.STROKECOLOR, ColorLib.rgb(99, 99, 99)));
		color.add(new NodeBorderColorAction(nodes));
		this.m_vis.putAction("color", color);

		
		// repaint
		ActionList repaint = new ActionList();
		repaint.add(color);
		repaint.add(new RepaintAction());
		m_vis.putAction("repaint", repaint);
		
		/*
		 * Define the force layout action to prevent node overlap
		 */
        // this will cause docs to move out of the way when dragging
        final ForceDirectedLayout fl = new DataMountainForceLayout(false);
        ActivityListener fReset = new ActivityAdapter() {
            public void activityCancelled(Activity a) {
                fl.reset(); 
             } 
        };
        
		ActionList layout = new ActionList(2000);
		//layout.add(fl);
		layout.add(new ForceDirectedLayout(graph, true));
		layout.addActivityListener(fReset);
		layout.add(new AggregateLayout(AGGR));
        layout.add(new LabelLayout2(AGGR_DECORATORS));
		layout.add(new RepaintAction());
		this.m_vis.putAction("layout", layout);

		ActionList draw = new ActionList();
		//draw.add(new VisibilityFilter(orPredicateFilter));
		draw.add(new VisibilityFilter(andPredicateFilters));
		draw.add(x_axis);
		draw.add(y_axis);
        //draw.add(ylabels);
		draw.add(color);
		//draw.add(new RepaintAction());
		
        ActionList forces = new ActionList();
        forces.add(fl);
        forces.add(new RepaintAction());
        forces.addActivityListener(fReset);
        m_vis.putAction("forces", forces);
        
        // zoom to fit
        m_vis.putAction("zoom", new ZoomGraphToFit());
        
        ActionList showSearchMatches = new ActionList();
		//showSearchMatches.add(new VisibilityFilter(orPredicateFilter));
        showSearchMatches.add(new VisibilityFilter(andPredicateFilters));
        //showSearchMatches.add(new RepaintAction());
        m_vis.putAction("showSearchMatches", showSearchMatches);
        
        m_vis.putAction("patternlayout", new PatternLocationAction());
        
		this.m_vis.putAction("draw", draw);
		this.m_vis.alwaysRunAfter("draw", "patternlayout");
		this.m_vis.alwaysRunAfter("draw", "layout");
		this.m_vis.alwaysRunAfter("draw", "edgevisibility");
		
		m_vis.alwaysRunAfter("layout", "zoom");
		
		// register the listener for updates to the filters
        UpdateListener lstnr = new UpdateListener() {
            public void update(Object src) {
            	logger.info("update listerener detected");

                m_vis.runAfter("showSearchMatches", "updateList");
            	m_vis.run("showSearchMatches");
//                m_vis.run("color");
            	
                // update the list
            }
        };
        andPredicateFilters.addExpressionListener(lstnr);		

		TraceService.log(TraceService.EVENT_SETUP_CREATEACTIONS_END, null);
	}
	

	public void setControls()
	{
		// -- 5. the display and interactive controls -------------------------

		// Create the Display object, and pass it the visualization that it 
		// will hold.
		//d = new Display(m_vis);

		// Set the size of the display.
		//display.setSize(850, 850);
		display.setSize(VisualDBConfig.DISPLAY_SIZE_WIDTH, VisualDBConfig.DISPLAY_SIZE_HEIGHT);
		//display.setSize(650, 650);
		display.pan(0, 0);
        //display.pan(510, 510);
		display.setDamageRedraw(false); // disable due to Java2D image clipping errors
		display.setForeground(Color.BLACK);
		//d.setBackground(Color.BLACK);

		//d.setCustomToolTip(m_customToolTip);
		// We use the addControlListener method to set up interaction.
		display.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
		//display.setItemSorter(new DataMountainSorter());
		// The DragControl is a built in class for manually moving
		// nodes with the mouse. 
		display.addControlListener(new DragControl());
		// Pan with left-click drag on background
		display.addControlListener(new PanControl()); 
		// Zoom with right-click drag
		display.addControlListener(new ZoomControl());
		// add 
		//d.addControlListener(new FocusControl(1));
		//display.addControlListener(new AggregateDragControl());
		display.addControlListener(new AggregateDragControl3());
		display.addControlListener(new DiscussionOverviewPaneControlAdapter(this));
		
		NeighborHighlightControl neighborHighlightControl = new NeighborHighlightControl();
		neighborHighlightControl.setHighlightWithInvisibleEdge(true);
		display.addControlListener(neighborHighlightControl);
		display.addControlListener(new WheelZoomControl());
		display.addControlListener(new ZoomToFitControl());
		//d.addControlListener(new ToolTipControl("title"));
		display.addControlListener(new HoverActionControl("repaint"));
		display.addControlListener(new NodeInfoTooltipControl());
		
		// add overview window		
		overview = new Display(m_vis);
		overview.setBorder(
		BorderFactory.createLineBorder(Color.BLACK, 1));
		overview.setSize(100 , 100);
		overview.setLocation(0, display.getHeight()-overview.getHeight());
		overview.zoom(new Point2D.Float(0,0),0.1);
		display.add(overview);		
		
		
		//Table t = new Table();
		//t.addColumn("TAG", String.class);
		//m_prefuseTable = new JPrefuseTable(t);		
		m_prefuseTable.getTable().clear();
		//m_prefuseTable.getTable().setString(m_prefuseTable.getTable().addRow(), "tag", "Emilio");
		m_prefuseTable.setSize(100,100);
		m_prefuseTable.setPreferredSize(new Dimension(100, 100));
		m_prefuseTable.setEnabled(false);
		m_prefuseTable.setLocation(100, display.getHeight()-m_prefuseTable.getHeight() - 100);
		display.add(m_prefuseTable);
	}

	private Component buildCentralPanel() {
		logger.info("display=" + this.display);
		return this.display;
	}
	
	private Component buildEastPanel() {
        
        JSearchPanel searchPanel = searchQ.createSearchPanel(false);
        searchPanel.setBackground(this.getBackground());
		searchPanel.setMinimumSize(new Dimension(400, 30));
		searchPanel.setSize(searchPanel.getMinimumSize());
		searchPanel.setShowResultCount(true);
		searchPanel.setShowCancel(false);
		searchPanel.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 14));
		searchPanel.setCancelColor(Color.RED);
		searchPanel.setBorder(new EmptyBorder(10,0,10,0));
		
		// build the results list
		listMenu = new JList(listMenuModel);
		listMenu.setCellRenderer(new VisualDBListRenderer());
		listMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listMenu.setVisibleRowCount(-1);
		
		listMenu.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// clear selection
				listMenu.clearSelection();
				
				int index = listMenu.locationToIndex(e.getPoint());
				
				if ( index > -1) {
					listMenu.setSelectedIndex(index);
					VisualItem item = (VisualItem) listMenu.getSelectedValue();
					m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).setTuple(item);
					
					TraceService.log(TraceService.EVENT_LIST_MOUSEMOVED,  "id=" + item.getString("id") + ", type=" + item.getInt("type"));
					
					m_vis.run("repaint");
				}
			}
		});
		
		listMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				// clear selection
				((JList)e.getSource()).clearSelection();
		        m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).clear();	

				TraceService.log(TraceService.EVENT_LIST_MOUSEEXITED);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// clear selection
				((JList)e.getSource()).clearSelection();

				int index = ((JList)e.getSource()).locationToIndex(e.getPoint());
				
				if ( index > -1 ) {
					listMenu.setSelectedIndex(index);
					VisualItem item = (VisualItem) ((JList)e.getSource()).getSelectedValue();
					m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).setTuple(item);		
					m_vis.run("repaint");
					
					TraceService.log(TraceService.EVENT_LIST_MOUSEENTERED,  "id=" + item.getString("id") + ", type=" + item.getInt("type"));
				}
			}
			
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				if ( evt.getClickCount() == 2 ) {
					int index = list.locationToIndex(evt.getPoint());
					VisualItem item = (VisualItem) list.getModel().getElementAt(index);
					
					TraceService.log(TraceService.EVENT_LIST_ITEMCLICKED, "id=" + item.getString("id") + ", type=" + item.getInt("type"));
					
					// open thread details
					parent.openDiscussionDetails((Node)item);
				}
			}
			
		});
		
		
		listScrollPane = new JScrollPane(listMenu);
		listScrollPane.setMinimumSize(new Dimension(400, 470));
		listScrollPane.setMaximumSize(new Dimension(400, 3000));
		listScrollPane.setPreferredSize(listScrollPane.getMinimumSize());
		
		// create search result layout
		JPanel eastPanel = new JPanel(new BorderLayout());
		eastPanel.setPreferredSize(new Dimension(450, 500));
		eastPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		eastPanel.add(BorderLayout.NORTH, searchPanel);
		eastPanel.add(BorderLayout.CENTER, listScrollPane);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add("Filter Discussions", eastPanel);
		JList patternsList = new JList(new DefaultListModel());
		patternsList.setCellRenderer(new VisualDBListRenderer());
		patternsList.addMouseListener(new MouseAdapter() {		
			@Override
			public void mouseExited(MouseEvent e) {
				// clear selection
				((JList)e.getSource()).clearSelection();
		        m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).clear();	

				TraceService.log(TraceService.EVENT_PATTERNLIST_MOUSEEXITED);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// clear selection
				((JList)e.getSource()).clearSelection();

				int index = ((JList)e.getSource()).locationToIndex(e.getPoint());
				
				if ( index > -1 ) {
					((JList)e.getSource()).setSelectedIndex(index);
					Node item = (Node) ((JList)e.getSource()).getSelectedValue();
					m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).setTuple(item);		
					m_vis.run("repaint");
					
					TraceService.log(TraceService.EVENT_PATTERNLIST_MOUSEENTERED, "id=" + item.getString("id") + ", type=" + item.getInt("type"));
				}
			}
			
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				if ( evt.getClickCount() == 2 ) {
					int index = list.locationToIndex(evt.getPoint());
					Node item = (Node) list.getModel().getElementAt(index);
					
					TraceService.log(TraceService.EVENT_PATTERNLIST_ITEMCLICKED, "id=" + item.getString("id") + ", type=" + item.getInt("type"));
					
					// open thread details
					parent.openDiscussionDetails((Node)item);
				}
			}
			
		});
		
		patternsList.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// clear selection
				((JList)e.getSource()).clearSelection();
				
				int index = ((JList)e.getSource()).locationToIndex(e.getPoint());
				
				if ( index > -1) {
					((JList)e.getSource()).setSelectedIndex(index);
					Node n = (Node)((JList)e.getSource()).getSelectedValue();
					VisualGraph vg = (VisualGraph) m_vis.getGroup(graph);
					VisualItem item = (VisualItem) vg.getNode(((Node)GraphUtil.getNodeById(m_graph, n.getInt("id"))).getRow());
					
					m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).setTuple(item);
					
					TraceService.log(TraceService.EVENT_PATTERNLIST_MOUSEMOVED,  "id=" + item.getString("id") + ", type=" + item.getInt("type"));

					m_vis.run("repaint");
					
				}
			}
		});
		
		Iterator i = GraphMLDataReader.getPatterns(GraphMLDataReader.getSourceGraph());
		while ( i.hasNext() ) {
			Tuple p = (Tuple) i.next();
			Node n 	= GraphMLDataReader.getSourceGraph().getNode(p.getRow());
			((DefaultListModel)patternsList.getModel()).addElement(n);
		}
		
		patternListScrollPane = new JScrollPane(patternsList);
		patternListScrollPane.setMinimumSize(new Dimension(400, 470));
		patternListScrollPane.setMaximumSize(new Dimension(400, 3000));
		patternListScrollPane.setPreferredSize(patternListScrollPane.getMinimumSize());
		
		tabbedPane.add("Pattern Browser", patternListScrollPane);
		
		return tabbedPane;
	}
	
	private Component buildSouthPanel() {
		final JFastLabel title = new JFastLabel("                 ");
		title.setPreferredSize(new Dimension(600, 20));
		title.setVerticalAlignment(SwingConstants.BOTTOM);
		title.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
		title.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 16));
		this.display.addControlListener(new ControlAdapter() {
			public void itemEntered(VisualItem item, MouseEvent e) {
				if ( item.canGetString("title") )
					title.setText(item.getString("title") + " ID=" + item.getInt("id") );
			}
			public void itemExited(VisualItem item, MouseEvent e) {
				title.setText(null);
			}
		});
		
		/**
		 * Build the range slider
		 */
		final JRangeSlider rangeSlider = new JRangeSlider(
									VisualDBConfig.RANGE_SLIDER_MIN_VALUE, 
									VisualDBConfig.RANGE_SLIDER_MAX_VALUE, 
									VisualDBConfig.RANGE_SLIDER_LOW_VALUE, 
									VisualDBConfig.RANGE_SLIDER_HIGH_VALUE, 1);
		final JLabel rangeLabel = new JLabel(DateLib.formatDate(rangeSlider.getLowValue()) + " to " + DateLib.formatDate(rangeSlider.getHighValue()));
		rangeSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				VisualDBConfig.RANGE_SLIDER_HIGH_VALUE = rangeSlider.getHighValue();
				VisualDBConfig.RANGE_SLIDER_LOW_VALUE  = rangeSlider.getLowValue();
				
				rangeLabel.setText(DateLib.formatDate(VisualDBConfig.RANGE_SLIDER_LOW_VALUE) + " to " + DateLib.formatDate(VisualDBConfig.RANGE_SLIDER_HIGH_VALUE));
				
				logger.info("changing: " +/* e + ", range=" + rangeSlider + */", isAdjusting=" +rangeSlider.getModel().getValueIsAdjusting());
				// reload the discussions
				if ( !rangeSlider.getModel().getValueIsAdjusting()) {
					logger.info("reload graph");
					parent.openDiscussionOverview();					
				}
			}
		});
		
		final DatePicker beginDate = new DatePicker(new Date(DateLib.getDateAsLong(VisualDBConfig.RANGE_SLIDER_LOW_VALUE)));
		beginDate.setShowTodayButton(false);
		beginDate.setShowNoneButton(false);
		beginDate.setPreferredSize(new Dimension(150, 25));
		beginDate.setMaximumSize(beginDate.getPreferredSize());
		beginDate.setVetoPolicy(new VetoPolicy() {
			
			@Override
			public void removeVetoPolicyListener(PolicyListener listener) {
			}
			
			@Override
			public void addVetoPolicyListener(PolicyListener listener) {
			}
			
			@Override
			public boolean isRestricted(Object source, Calendar date) {
				Calendar min = Calendar.getInstance();
				min.setTimeInMillis(DateLib.getDateAsLong(VisualDBConfig.RANGE_SLIDER_MIN_VALUE));
				Calendar high = Calendar.getInstance();
				high.setTimeInMillis(DateLib.getDateAsLong(VisualDBConfig.RANGE_SLIDER_HIGH_VALUE));
				
				return min.compareTo(date) > 0 || high.compareTo(date) < 0;
			}
			
			@Override
			public boolean isRestrictNull(Object source) {
				return true;
			}
		});
		beginDate.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ( "date".equals(evt.getPropertyName())) {
					VisualDBConfig.RANGE_SLIDER_LOW_VALUE = DateLib.getDateAsInt(((Date)evt.getNewValue()).getTime());
					
					logger.info("reload graph");
					parent.openDiscussionOverview();		
				}
				
			}
		});
		
		final DatePicker endDate = new DatePicker(new Date(DateLib.getDateAsLong(VisualDBConfig.RANGE_SLIDER_HIGH_VALUE)));
		endDate.setShowNoneButton(false);
		endDate.setPreferredSize(new Dimension(150, 25));
		endDate.setMaximumSize(beginDate.getPreferredSize());
		endDate.setVetoPolicy(new VetoPolicy() {
			
			@Override
			public void removeVetoPolicyListener(PolicyListener listener) {
			}
			
			@Override
			public void addVetoPolicyListener(PolicyListener listener) {
			}
			
			@Override
			public boolean isRestricted(Object source, Calendar date) {
				Calendar max = Calendar.getInstance();
				max.setTimeInMillis(DateLib.getDateAsLong(VisualDBConfig.RANGE_SLIDER_MAX_VALUE));
				Calendar low = Calendar.getInstance();
				low.setTimeInMillis(DateLib.getDateAsLong(VisualDBConfig.RANGE_SLIDER_LOW_VALUE));
				return max.compareTo(date) < 0 || low.compareTo(date) > 0;
			}
			
			@Override
			public boolean isRestrictNull(Object source) {
				return true;
			}
		});
		endDate.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ( "date".equals(evt.getPropertyName())) {
					VisualDBConfig.RANGE_SLIDER_HIGH_VALUE = DateLib.getDateAsInt(((Date)evt.getNewValue()).getTime());
					
					logger.info("reload graph");
					parent.openDiscussionOverview();		
				}
				
			}
		});
		
		JButton resetDatesBtn = new JButton("Reset Dates");
		resetDatesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					beginDate.setDate(new Date(TimeLib.getDate(VisualDBConfig.CAL, 2013, 5, 1)));
					endDate.setDate(new Date(DateLib.getDateAsLong(VisualDBConfig.RANGE_SLIDER_MAX_VALUE)));
					
				} catch (PropertyVetoException e1) {
					e1.printStackTrace();
				}
			}
		});

		JValueSlider edgeSimilaritySlider = new JValueSlider("Similarity strength ", 0, 1, VisualDBConfig.SLIDER_SIMILARITY_VALUE);
		edgeSimilaritySlider.setBackground(this.getBackground());
		edgeSimilaritySlider.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
	            JValueSlider s = (JValueSlider)e.getSource();
	            VisualDBConfig.SLIDER_SIMILARITY_VALUE = s.getValue().doubleValue();
	            m_vis.run("edgevisibility");
			}
		});

		
		Box rangeSliderBox = UILib.getBox(new Component[] {
						new JLabel("Begin date:"),
						beginDate,
						new JLabel("End date:"),
						endDate,
						resetDatesBtn,
						edgeSimilaritySlider
				}, true, 5, 10);
		
		UILib.addStrut(rangeSliderBox, true, 300);
		Box footerBox = UILib.getBox(new Component[]{
				UILib.getBox(new Component[] {rangeSliderBox, new JLabel("                            ")},true, 0, 100), title }, false, 0, 0, 0);
		
		return footerBox;
	}

	public static DiscussionOverviewBySimilarityPane demo(VisualDBApplication parent) {
		DiscussionOverviewBySimilarityPane pane = new DiscussionOverviewBySimilarityPane(parent);

		return pane;
	}
	    
    public void displayLayout() {
        Insets i = this.display.getInsets();
        int w = this.display.getWidth();
        int h = this.display.getHeight();
        int iw = i.left+i.right;
        int ih = i.top+i.bottom;
        int aw = 85;
        int ah = 15;
        
        m_dataB.setRect(i.left, i.top, w-iw-aw, h-ih-ah);
        //m_xlabB.setRect(i.left, h-ah-i.bottom, w-iw-aw, ah-10);
       // m_ylabB.setRect(i.left, i.top, w-iw, h-ih-ah);
        
        m_vis.run("draw");
    }
    
    private static final String ANCHORITEM = "_anchorItem";
    private static final Schema ANCHORITEM_SCHEMA = new Schema();
    static {
        ANCHORITEM_SCHEMA.addColumn(ANCHORITEM, ForceItem.class);
    }
    
    public class DataMountainForceLayout extends ForceDirectedLayout {
        
        public DataMountainForceLayout(boolean enforceBounds) {
            super("graph",enforceBounds,false);
            
            ForceSimulator fsim = new ForceSimulator();
            fsim.addForce(new NBodyForce(-0.8f, 360f, NBodyForce.DEFAULT_THETA));
            fsim.addForce(new SpringForce(1e-5f,5));
            fsim.addForce(new DragForce());
            setForceSimulator(fsim);
            
            m_nodeGroup = "graph.nodes";
            m_edgeGroup = null;
        }
        
        protected float getMassValue(VisualItem n) {
            return n.isHover() ? 5f : 1f;
        }

        public void reset() {
            Iterator iter = m_vis.visibleItems(m_nodeGroup);
            while ( iter.hasNext() ) {
                VisualItem item = (VisualItem)iter.next();
                ForceItem aitem = (ForceItem)item.get(ANCHORITEM);
                if ( aitem != null ) {
                    aitem.location[0] = (float)item.getEndX();
                    aitem.location[1] = (float)item.getEndY();
                }
            }
            super.reset();
        }
        protected void initSimulator(ForceSimulator fsim) {
            // make sure we have force items to work with
            TupleSet t = (TupleSet)m_vis.getGroup(m_group);
            t.addColumns(ANCHORITEM_SCHEMA);
            t.addColumns(FORCEITEM_SCHEMA);
            
            Iterator iter = m_vis.visibleItems(m_nodeGroup);
            while ( iter.hasNext() ) {
                VisualItem item = (VisualItem)iter.next();
                // get force item
                ForceItem fitem = (ForceItem)item.get(FORCEITEM);
                if ( fitem == null ) {
                    fitem = new ForceItem();
                    item.set(FORCEITEM, fitem);
                }
                fitem.location[0] = (float)item.getEndX();
                fitem.location[1] = (float)item.getEndY();
                fitem.mass = getMassValue(item);
                
                // get spring anchor
                ForceItem aitem = (ForceItem)item.get(ANCHORITEM);
                if ( aitem == null ) {
                    aitem = new ForceItem();
                    item.set(ANCHORITEM, aitem);
                    aitem.location[0] = fitem.location[0];
                    aitem.location[1] = fitem.location[1];
                }
                
                fsim.addItem(fitem);
                fsim.addSpring(fitem, aitem, 0);
            }     
        }       
    } // end of inner class DataMountainForceLayout
    
    public static class DataMountainSorter extends ItemSorter {
        public int score(VisualItem item) {
        	if ( item.canGetInt("type") && item.getInt("type") == VisualDBConstants.NODE_TYPE_PATTERN) {
        		return  (int)(5000*item.getY());
        	} else 
            return (int)(10000*item.getY());
        }
    } // end of inner class DataMountainComparator
}


/**
 * Layout algorithm that computes a convex hull surrounding
 * aggregate items and saves it in the "_polygon" field.
 */
class AggregateLayout extends Layout {
    
    private int m_margin = 5; // convex hull pixel margin
    private double[] m_pts;   // buffer for computing convex hulls
    
    public AggregateLayout(String aggrGroup) {
        super(aggrGroup);
    }
    
    /**
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
    public void run(double frac) {
        
        AggregateTable aggr = (AggregateTable)m_vis.getGroup(m_group);
        // do we have any  to process?
        int num = aggr.getTupleCount();
        if ( num == 0 ) return;
        
        // update buffers
        int maxsz = 0;
        for ( Iterator aggrs = aggr.tuples(); aggrs.hasNext();  )
            maxsz = Math.max(maxsz, 4*2*
                    ((AggregateItem)aggrs.next()).getAggregateSize());
        if ( m_pts == null || maxsz > m_pts.length ) {
            m_pts = new double[maxsz];
        }
        
        // compute and assign convex hull for each aggregate
        Iterator aggrs = m_vis.visibleItems(m_group);
        while ( aggrs.hasNext() ) {
            AggregateItem aitem = (AggregateItem)aggrs.next();

            int idx = 0;
            if ( aitem.getAggregateSize() == 0 ) continue;
            VisualItem item = null;
            Iterator iter = aitem.items();
            while ( iter.hasNext() ) {
                item = (VisualItem)iter.next();
                if ( item.isVisible() ) {
                    addPoint(m_pts, idx, item, m_margin);
                    idx += 2*4;
                }
            }
            // if no aggregates are visible, do nothing
            if ( idx == 0 ) continue;

            // compute convex hull
            double[] nhull = GraphicsLib.convexHull(m_pts, idx);
            
            // prepare viz attribute array
            float[]  fhull = (float[])aitem.get(VisualItem.POLYGON);
            if ( fhull == null || fhull.length < nhull.length )
                fhull = new float[nhull.length];
            else if ( fhull.length > nhull.length )
                fhull[nhull.length] = Float.NaN;
            
            // copy hull values
            for ( int j=0; j<nhull.length; j++ )
                fhull[j] = (float)nhull[j];
            aitem.set(VisualItem.POLYGON, fhull);
            aitem.setValidated(false); // force invalidation
        }
    }
    
    private static void addPoint(double[] pts, int idx, 
                                 VisualItem item, int growth)
    {
        Rectangle2D b = item.getBounds();
        double minX = (b.getMinX())-growth, minY = (b.getMinY())-growth;
        double maxX = (b.getMaxX())+growth, maxY = (b.getMaxY())+growth;
        pts[idx]   = minX; pts[idx+1] = minY;
        pts[idx+2] = minX; pts[idx+3] = maxY;
        pts[idx+4] = maxX; pts[idx+5] = minY;
        pts[idx+6] = maxX; pts[idx+7] = maxY;
    }
    
} // end of class AggregateLayout


/**
 * Interactive drag control that is "aggregate-aware"
 */
class AggregateDragControl extends ControlAdapter {

    private VisualItem activeItem;
    protected Point2D down = new Point2D.Double();
    protected Point2D temp = new Point2D.Double();
    protected boolean dragged;
    
    /**
     * Creates a new drag control that issues repaint requests as an item
     * is dragged.
     */
    public AggregateDragControl() {
    }
        
    /**
     * @see prefuse.controls.Control#itemEntered(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemEntered(VisualItem item, MouseEvent e) {
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        activeItem = item;
        if ( !(item instanceof AggregateItem) )
            setFixed(item, true);
    }
    
    /**
     * @see prefuse.controls.Control#itemExited(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemExited(VisualItem item, MouseEvent e) {
        if ( activeItem == item ) {
            activeItem = null;
            setFixed(item, false);
        }
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getDefaultCursor());
    }
    
    /**
     * @see prefuse.controls.Control#itemPressed(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemPressed(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        dragged = false;
        Display d = (Display)e.getComponent();
        d.getAbsoluteCoordinate(e.getPoint(), down);
        if ( item instanceof AggregateItem )
            setFixed(item, true);
    }
    
    /**
     * @see prefuse.controls.Control#itemReleased(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemReleased(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        if ( dragged ) {
            activeItem = null;
            setFixed(item, false);
            dragged = false;
        }            
    }
    
    /**
     * @see prefuse.controls.Control#itemDragged(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemDragged(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        dragged = true;
        Display d = (Display)e.getComponent();
        d.getAbsoluteCoordinate(e.getPoint(), temp);
        double dx = temp.getX()-down.getX();
        double dy = temp.getY()-down.getY();
        
        move(item, dx, dy);
        
        down.setLocation(temp);
    }

    protected static void setFixed(VisualItem item, boolean fixed) {
        if ( item instanceof AggregateItem ) {
            Iterator items = ((AggregateItem)item).items();
            while ( items.hasNext() ) {
                setFixed((VisualItem)items.next(), fixed);
            }
        } else {
            item.setFixed(fixed);
        }
    }
    
    protected static void move(VisualItem item, double dx, double dy) {
        if ( item instanceof AggregateItem ) {
            Iterator items = ((AggregateItem)item).items();
            while ( items.hasNext() ) {
                move((VisualItem)items.next(), dx, dy);
            }
        } else {
            double x = item.getX();
            double y = item.getY();
            item.setStartX(x);  item.setStartY(y);
            item.setX(x+dx);    item.setY(y+dy);
            item.setEndX(x+dx); item.setEndY(y+dy);
        }
    }
    
} // end of class AggregateDragControl

/**
 * Interactive drag control that is "aggregate-aware"
 */
class AggregateDragControl3 extends ControlAdapter {
    private VisualItem activeItem;
    protected Point2D down = new Point2D.Double();
    protected Point2D temp = new Point2D.Double();
    protected boolean dragged;
    /**
     * Creates a new drag control that issues repaint requests as an item
     * is dragged.
     */
    public AggregateDragControl3() {
    }
    /**
     * @see prefuse.controls.Control#itemEntered(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemEntered(VisualItem item, MouseEvent e) {
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        activeItem = item;
        if ( !(item instanceof AggregateItem) )
            setFixed(item, true);
    }
    /**
     * @see prefuse.controls.Control#itemExited(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemExited(VisualItem item, MouseEvent e) {
        if ( activeItem == item ) {
            activeItem = null;
            setFixed(item, false);
        }
        Display d = (Display)e.getSource();
        d.setCursor(Cursor.getDefaultCursor());
    }
    /**
     * @see prefuse.controls.Control#itemPressed(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemPressed(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        dragged = false;
        Display d = (Display)e.getComponent();
        d.getAbsoluteCoordinate(e.getPoint(), down);
        if ( item instanceof AggregateItem )
            setFixed(item, true);
    }
    /**
     * @see prefuse.controls.Control#itemReleased(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemReleased(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        if ( dragged ) {
            activeItem = null;
            setFixed(item, false);
            dragged = false;
        }            
    }
    /**
     * @see prefuse.controls.Control#itemDragged(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemDragged(VisualItem item, MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;
        dragged = true;
        Display d = (Display)e.getComponent();
        d.getAbsoluteCoordinate(e.getPoint(), temp);
        double dx = temp.getX()-down.getX();
        double dy = temp.getY()-down.getY();
        move(item, dx, dy);
        down.setLocation(temp);
    }
    protected static void setFixed(VisualItem item, boolean fixed) {
        if ( item instanceof AggregateItem ) {
            Iterator items = ((AggregateItem)item).items();
            while ( items.hasNext() ) {
                setFixed((VisualItem)items.next(), fixed);
            }
        } else {
            item.setFixed(fixed);
        }
    }
    protected static void move(VisualItem item, double dx, double dy) {
        if ( item instanceof AggregateItem ) {
            Iterator items = ((AggregateItem)item).items();
            while ( items.hasNext() ) {
                move((VisualItem)items.next(), dx, dy);
            }
        } else {
            double x = item.getX();
            double y = item.getY();
            item.setStartX(x);  item.setStartY(y);
            item.setX(x+dx);    item.setY(y+dy);
            item.setEndX(x+dx); item.setEndY(y+dy);
        }
    }
} // end of class AggregateDragControl

/**
 * Set label positions. Labels are assumed to be DecoratorItem instances,
 * decorating their respective nodes. The layout simply gets the bounds
 * of the decorated node and assigns the label coordinates to the center
 * of those bounds.
 */
class LabelLayout2 extends Layout {
    public LabelLayout2(String group) {
        super(group);
    }
    public void run(double frac) {
        Iterator iter = m_vis.items(m_group);
        while ( iter.hasNext() ) {
            DecoratorItem decorator = (DecoratorItem)iter.next();
            VisualItem decoratedItem = decorator.getDecoratedItem();
            Rectangle2D bounds = decoratedItem.getBounds();
            double x = bounds.getCenterX();
            double y = bounds.getCenterY();
            /* modification to move edge labels more to the arrow head
            double x2 = 0, y2 = 0;
            if (decoratedItem instanceof EdgeItem){
                VisualItem dest = ((EdgeItem)decoratedItem).getTargetItem(); 
                x2 = dest.getX();
                y2 = dest.getY();
                x = (x + x2) / 2;
                y = (y + y2) / 2;
            }
            */
            setX(decorator, null, x);
            setY(decorator, null, y);
        }
    }
} // end of inner class LabelLayout    

