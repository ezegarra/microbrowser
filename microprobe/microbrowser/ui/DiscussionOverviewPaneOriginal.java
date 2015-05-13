package microbrowser.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

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
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import microbrowser.VisualDBApplication;
import microbrowser.VisualDBConfig;
import microbrowser.VisualDBConstants;
import microbrowser.action.EdgeStrokeColorAction;
import microbrowser.action.EdgeVisibilityAction;
import microbrowser.action.FillColorAction;
import microbrowser.action.TextColorAction;
import microbrowser.action.VisualDBUpdateItemVisibilityAction;
import microbrowser.action.ZoomGraphToFit;
import microbrowser.control.DiscussionOverviewPaneControlAdapter;
import microbrowser.control.NodeInfoTooltipControl;
import microbrowser.data.io.GraphMLDataReader;
import microbrowser.render.DiscussionRenderer;
import microbrowser.render.MicroProbeEdgeRenderer;
import microbrowser.render.PatternRenderer;
import microbrowser.render.VisualDBListRenderer;
import microbrowser.util.DateLib;
import microbrowser.util.TraceService;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataSizeAction;
import prefuse.action.layout.AxisLayout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.activity.ActivityAdapter;
import prefuse.activity.ActivityListener;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.query.SearchQueryBinding;
import prefuse.data.search.SearchTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
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
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

import com.michaelbaranov.microba.calendar.DatePicker;
import com.michaelbaranov.microba.calendar.VetoPolicy;
import com.michaelbaranov.microba.common.PolicyListener;

public class DiscussionOverviewPaneOriginal extends DiscussionOverviewByPatternPane /*JPanel*/ {
	private static final long serialVersionUID = -8279873905415635170L;
	
	public VisualDBApplication parent;
	public Visualization m_vis = null;
	private Display display = null;
	private Display overview = null;
	public Graph g = null;
	private GraphMLDataReader m_graphreader = null;
	
    private static final String graph = "graph";
    private static final String nodes = "graph.nodes";
    private static final String edges = "graph.edges";
    
	public JList listMenu;
	private DefaultListModel listMenuModel;
	private JScrollPane listScrollPane;
	
	public DiscussionOverviewPaneOriginal(VisualDBApplication parent) {
		super(parent);
		//this.display = display;
		this.parent = parent;
		
		this.listMenuModel = new DefaultListModel();
        // load data
		this.setUpData();
		this.setRenderers();
		this.setActions();
		this.setUpControls();
		
		// define the ui
		setLayout(new BorderLayout());
		add(buildCentralPanel(), BorderLayout.CENTER);
		add(buildEastPanel(), BorderLayout.EAST);
		add(buildSouthPanel(), BorderLayout.SOUTH);
		
		// run the action
        // filter graph and perform layout
        m_vis.run("draw");
	}
	

	// -- 1. load the data ------------------------------------------------
	public void setUpData()
	{	
		// Create the Visualization object.
		m_vis = new Visualization();

		display = new Display(m_vis);
		display.addComponentListener(new ComponentAdapter() {
			  public void componentResized(java.awt.event.ComponentEvent evt){
				 // System.out.println("componentResized w:" + VisualDBConfig.DISPLAY_SIZE_WIDTH + "==" + display.getWidth() + " - h: " + VisualDBConfig.DISPLAY_SIZE_HEIGHT + "==" + display.getHeight());

				  // change the location of the overview panel based on the
				  // new display dimension
				  if ( display.getHeight() != VisualDBConfig.DISPLAY_SIZE_HEIGHT  || display.getWidth() != VisualDBConfig.DISPLAY_SIZE_WIDTH) {
					  display.setSize(VisualDBConfig.DISPLAY_SIZE_WIDTH, VisualDBConfig.DISPLAY_SIZE_HEIGHT);
					  if ( overview != null ) {
						  overview.setLocation(0, display.getHeight()-overview.getHeight());
					  }
					  m_vis.run("zoom");					  
				  }
			  }
		});

		/* read the graph data */
		m_graphreader = new GraphMLDataReader();

		this.setGraph(m_graphreader.getGraph(VisualDBConfig.SLIDER_INITIAL_VALUE,
					(VisualDBConfig.EXPERIMENT_MODE == VisualDBConstants.EXPERIMENT_MODE_PATTERN_ONLY || 
					VisualDBConfig.EXPERIMENT_MODE == VisualDBConstants.EXPERIMENT_MODE_PATTERN_LEADERBOARD),
					VisualDBConfig.RANGE_SLIDER_LOW_VALUE,
					VisualDBConfig.RANGE_SLIDER_HIGH_VALUE));
	}
	
	/**
	 * Associates the given graph to the visualization
	 * @param g
	 */
    private void setGraph(Graph g) {
    	this.g = g;
        m_vis.add(graph, g);
        m_vis.setInteractive(edges, null, false);
    }  
	
	// -- 3. the renderers and renderer factory ---------------------------
	public void setRenderers()
	{
		// define the default renderer, which is for all threads
    	DefaultRendererFactory drf = new DefaultRendererFactory(new DiscussionRenderer());
        
    	// Add a rendered for the patterns
        //drf.add("type = " + VisualDBConstants.NODE_TYPE_THREAD, new LabelRenderer("answercount"));
        drf.add("type = " + VisualDBConstants.NODE_TYPE_PATTERN, new PatternRenderer());
        drf.add(new InGroupPredicate(edges), new MicroProbeEdgeRenderer());
        
        // set the render factory for the application
        m_vis.setRendererFactory(drf);
	}
	

	public void setActions()
	{
		TraceService.log(TraceService.EVENT_SETUP_CREATEACTIONS_BEGIN, null);
		
		String xfield 		= "x";
		String yfield 		= "y";

		AxisLayout x_axis = new AxisLayout(nodes, xfield, Constants.X_AXIS);
		this.m_vis.putAction("x", x_axis);

		AxisLayout y_axis = new AxisLayout(nodes, yfield, Constants.Y_AXIS);
		this.m_vis.putAction("y", y_axis);
		
		// add edge visibility action
		this.m_vis.putAction("edgevisibility", new EdgeVisibilityAction("graph.edges"));
		
		// configure search item visibility
		ActionList showall = new ActionList();
		showall.add(new VisualDBUpdateItemVisibilityAction(Visualization.ALL_ITEMS, listMenuModel));
		showall.add(new EdgeVisibilityAction("graph.edges"));
		
		ActionList showsearchmatches = new ActionList();
		showsearchmatches.add(new VisualDBUpdateItemVisibilityAction(Visualization.SEARCH_ITEMS, listMenuModel));
		showsearchmatches.add(new EdgeVisibilityAction("graph.edges"));
		
		this.m_vis.putAction("showsearchmatches", showsearchmatches);
		this.m_vis.putAction("showall", showall);
		/*
		 * Define the action that handles the coloring of the nodes and edges
		 */
		ActionList color = new ActionList(Activity.INFINITY);
		DataSizeAction itemSizeAction = new DataSizeAction(nodes,"answercount",40);
		itemSizeAction.setMaximumSize(10);
		color.add(new RepaintAction());
		color.add(new TextColorAction(nodes));
		color.add(new ColorAction(edges, VisualItem.FILLCOLOR, ColorLib.gray(100)));
		color.add(new EdgeStrokeColorAction(edges));
		
		color.add(itemSizeAction);
		color.add(new FillColorAction(nodes));
		color.add(new ColorAction(nodes, VisualItem.STROKECOLOR, ColorLib.rgb(99, 99, 99)));

		this.m_vis.putAction("color", color);

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
        
		ActionList layout = new ActionList(3000);
//		layout.add(new PatternLocationAction());
		layout.add(fl);
		//layout.add(new CircleLayout(graph));
		layout.addActivityListener(fReset);
		layout.add(new RepaintAction());
		this.m_vis.putAction("layout", layout);

		ActionList draw = new ActionList();
		draw.add(x_axis);
		draw.add(y_axis);
		draw.add(showall);
		//draw.add(new PatternLocationAction());
		
        ActionList forces = new ActionList();
        forces.add(fl);
        forces.add(new RepaintAction());
        forces.addActivityListener(fReset);
        m_vis.putAction("forces", forces);
        
        // zoom to fit
        m_vis.putAction("zoom", new ZoomGraphToFit());
        
		this.m_vis.putAction("draw", draw);
		this.m_vis.alwaysRunAfter("draw", "color");
		this.m_vis.alwaysRunAfter("draw", "layout");
		m_vis.runAfter("layout", "zoom");
	}
	

	public void setUpControls()
	{
		// -- 5. the display and interactive controls -------------------------

		// Create the Display object, and pass it the visualization that it 
		// will hold.
		//d = new Display(m_vis);

		// Set the size of the display.
		//display.setSize(850, 850);
		display.setSize(VisualDBConfig.DISPLAY_SIZE_WIDTH, VisualDBConfig.DISPLAY_SIZE_HEIGHT);
        //display.pan(510, 510);
		display.setDamageRedraw(false); // disable due to Java2D image clipping errors
		display.setForeground(Color.BLACK);
		//d.setBackground(Color.BLACK);

		//d.setCustomToolTip(m_customToolTip);
		// We use the addControlListener method to set up interaction.

		// The DragControl is a built in class for manually moving
		// nodes with the mouse. 
		display.addControlListener(new DragControl());
		// Pan with left-click drag on background
		display.addControlListener(new PanControl()); 
		// Zoom with right-click drag
		display.addControlListener(new ZoomControl());
		// add 
		//d.addControlListener(new FocusControl(1));
		display.addControlListener(new DiscussionOverviewPaneControlAdapter(this));
		
		NeighborHighlightControl neighborHighlightControl = new NeighborHighlightControl();
		neighborHighlightControl.setHighlightWithInvisibleEdge(true);
		display.addControlListener(neighborHighlightControl);
		display.addControlListener(new WheelZoomControl());
		display.addControlListener(new ZoomToFitControl());
		//d.addControlListener(new ToolTipControl("title"));
		display.addControlListener(new NodeInfoTooltipControl());
		display.addControlListener(new PanControl());
		display.addControlListener(new ZoomControl());
		
		// add overview window		
		overview = new Display(m_vis);
		overview.setBorder(
		BorderFactory.createLineBorder(Color.BLACK, 1));
		overview.setSize(100,100);
		overview.setLocation(0, display.getHeight()-overview.getHeight());
		overview.zoom(new Point2D.Float(0,0),0.1);
		display.add(overview);		
	}
	
	private Component buildCentralPanel() {
		return this.display;
	}
	
	private Component buildEastPanel() {
		
        // create the search query binding
        final SearchQueryBinding searchQ = new SearchQueryBinding(this.g.getNodeTable(), "body");
        final SearchTupleSet search = searchQ.getSearchSet(); 
        
		
//        searchQ.getPredicate().addExpressionListener(new UpdateListener() {
//			public void update(Object src) {
//				//listMenuModel.removeAllElements();
//				SearchTupleSet set = searchQ.getSearchSet();
//				String query = set.getQuery();
//
//				String visibility_action;
//
//				if ( "".equals(query) || query == null) {
//					visibility_action = "showall";                
//				} else {
//					visibility_action = "showsearchmatches";                              		
//				}
//				System.out.println("Running search query=" + query + ", action=" + visibility_action);
//				//m_vis.run(visibility_action);
//				TraceService.log(TraceService.EVENT_SEARCH_UPDATE, query);
//			}
//		});
        
        searchQ.getSearchSet().addTupleSetListener(new TupleSetListener() {
			
			@Override
			public void tupleSetChanged(TupleSet tset, Tuple[] added, Tuple[] removed) {
				String query = searchQ.getSearchSet().getQuery();
				
				if ( "".equals(query)) {
					//m_vis.run("showall");
					parent.openDiscussionOverview();
				} else {
					m_vis.run("showsearchmatches");
				}
				
				
				TraceService.log(TraceService.EVENT_SEARCH_UPDATE, query);
			}
		});

        m_vis.addFocusGroup(Visualization.SEARCH_ITEMS, search);
        
        JSearchPanel searchPanel = searchQ.createSearchPanel();
        searchPanel.setBackground(this.getBackground());
		searchPanel.setMinimumSize(new Dimension(400, 30));
		searchPanel.setSize(searchPanel.getMinimumSize());
		searchPanel.setShowResultCount(true);
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
				}
			}
		});
		
		listMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				// clear selection
				listMenu.clearSelection();
		        m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).clear();	

				TraceService.log(TraceService.EVENT_LIST_MOUSEEXITED);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// clear selection
				listMenu.clearSelection();

				int index = listMenu.locationToIndex(e.getPoint());
				
				if ( index > -1 ) {
					listMenu.setSelectedIndex(index);
					VisualItem item = (VisualItem) listMenu.getSelectedValue();
					m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).setTuple(item);		

					TraceService.log(TraceService.EVENT_LIST_MOUSEENTERED, item.getString("id"));
				}
			}
			
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				if ( evt.getClickCount() == 2 ) {
					int index = list.locationToIndex(evt.getPoint());
					VisualItem item = (VisualItem) list.getModel().getElementAt(index);
					
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
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				if ( evt.getClickCount() == 2 ) {
					int index = list.locationToIndex(evt.getPoint());
					Node item = (Node) list.getModel().getElementAt(index);
					
					// open thread details
					parent.openDiscussionDetails((Node)item);
				}
			}
			
		});
		Iterator i = GraphMLDataReader.getPatterns(GraphMLDataReader.getSourceGraph());
		while ( i.hasNext() ) {
			Tuple p = (Tuple) i.next();
			Node n 	= GraphMLDataReader.getSourceGraph().getNode(p.getRow());
			((DefaultListModel)patternsList.getModel()).addElement(n);
		}
		tabbedPane.add("Pattern Browser", patternsList);
		
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
					title.setText(item.getString("title"));
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
				
				System.out.println("changing: " +/* e + ", range=" + rangeSlider + */", isAdjusting=" +rangeSlider.getModel().getValueIsAdjusting());
				// reload the discussions
				if ( !rangeSlider.getModel().getValueIsAdjusting()) {
					System.out.println("reload graph");
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
					
					System.out.println("reload graph");
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
					
					System.out.println("reload graph");
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

	public static DiscussionOverviewPaneOriginal demo(VisualDBApplication parent) {
		DiscussionOverviewPaneOriginal pane = new DiscussionOverviewPaneOriginal(parent);

		return pane;
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
}
