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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import microbrowser.VisualDBApplication;
import microbrowser.VisualDBConfig;
import microbrowser.VisualDBConstants;
import microbrowser.action.EdgeVisibilityAction;
import microbrowser.action.FillColorAction;
import microbrowser.action.NodeBorderColorAction;
import microbrowser.action.NodeFontAction;
import microbrowser.action.TextColorAction;
import microbrowser.action.UpdateListItemsAction;
import microbrowser.control.DiscussionOverviewPaneControlAdapter;
import microbrowser.control.NodeInfoTooltipControl;
import microbrowser.data.io.GraphMLDataReader;
import microbrowser.data.io.GraphMLGenerator;
import microbrowser.data.model.Discussion;
import microbrowser.data.model.Pattern;
import microbrowser.render.DiscussionRenderer;
import microbrowser.render.PatternRenderer;
import microbrowser.render.VisualDBListRenderer;
import microbrowser.ui.DiscussionDetailsPane.NodeColorAction;
import microbrowser.ui.DiscussionDetailsPane.TreeRootAction;
import microbrowser.util.DateLib;
import microbrowser.util.GraphUtil;
import microbrowser.util.TraceService;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.PolarLocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.filter.VisibilityFilter;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.action.layout.graph.RadialTreeLayout;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.HoverActionControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.expression.AndPredicate;
import prefuse.data.query.ListQueryBinding;
import prefuse.data.query.SearchQueryBinding;
import prefuse.data.search.KeywordSearchTupleSet;
import prefuse.data.search.SearchTupleSet;
import prefuse.data.tuple.DefaultTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
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
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.sort.TreeDepthItemSorter;

import com.michaelbaranov.microba.calendar.DatePicker;
import com.michaelbaranov.microba.calendar.VetoPolicy;
import com.michaelbaranov.microba.common.PolicyListener;

public class DiscussionOverviewByPatternPane extends DiscussionOverviewPane {
	private static final long serialVersionUID = -8279873905415635170L;
	private static Logger logger = Logger.getLogger(DiscussionOverviewByPatternPane.class.getName());
	
	private Display display = null;
    
	private static final String tree = "tree";
	private static final String treeNodes = "tree.nodes";
	private static final String treeEdges = "tree.edges";
	private static final String linear = "linear";
	private static final String mainnode = "mainnodes";
	
	private DefaultListModel listMenuModel;
	private JScrollPane listScrollPane;
	private JScrollPane patternListScrollPane;
	
	// the query bindings to perform the filtering
    private ListQueryBinding   typesQ    = null;
    private SearchQueryBinding searchQ   = null;
    
	public DiscussionOverviewByPatternPane(VisualDBApplication parent) {
		//this.display = display;
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
		m_vis.run("filter");
		
		// maintain a set of items that should be interpolated linearly
		// this isn't absolutely necessary, but makes the animations nicer
		// the PolarLocationAnimator should read this set and act accordingly
		m_vis.addFocusGroup(linear, new DefaultTupleSet());
		m_vis.getGroup(Visualization.FOCUS_ITEMS).addTupleSetListener(
				new TupleSetListener() {
					public void tupleSetChanged(TupleSet t, Tuple[] add,
							Tuple[] rem) {
						TupleSet linearInterp = m_vis.getGroup(linear);
						if (add.length < 1)
							return;
						linearInterp.clear();
						for (Node n = (Node) add[0]; n != null; n = n
								.getParent())
							linearInterp.addTuple(n);
					}
				});
	}
	

	// -- 1. load the data ------------------------------------------------
	public void setUpData()
	{	
		// Create the Visualization object.
		m_vis = new Visualization();

		display = new Display(m_vis);
		display.addComponentListener(new ComponentAdapter() {
			  public void componentResized(java.awt.event.ComponentEvent evt){
				  //logger.info("Resizing window");
				  
				 // System.out.println("componentResized w:" + VisualDBConfig.DISPLAY_SIZE_WIDTH + "==" + display.getWidth() + " - h: " + VisualDBConfig.DISPLAY_SIZE_HEIGHT + "==" + display.getHeight());

				  // change the location of the overview panel based on the
				  // new display dimension
				  if ( display.getHeight() != VisualDBConfig.DISPLAY_SIZE_HEIGHT  || display.getWidth() != VisualDBConfig.DISPLAY_SIZE_WIDTH) {
					  display.setSize(VisualDBConfig.DISPLAY_SIZE_WIDTH, VisualDBConfig.DISPLAY_SIZE_HEIGHT);
					  
					  m_vis.run("filter");
					  m_vis.runAfter("filter", "recolor");
				  }
			  }
		});

		/* read the graph data */
//		m_graphreader = new GraphMLDataReader();

//		this.setGraph(m_graphreader.getGraph(VisualDBConfig.SLIDER_INITIAL_VALUE,
//					(VisualDBConfig.EXPERIMENT_MODE == VisualDBConstants.EXPERIMENT_MODE_PATTERN_ONLY || 
//					VisualDBConfig.EXPERIMENT_MODE == VisualDBConstants.EXPERIMENT_MODE_PATTERN_LEADERBOARD),
//					VisualDBConfig.RANGE_SLIDER_LOW_VALUE,
//					VisualDBConfig.RANGE_SLIDER_HIGH_VALUE));
		
		// create the graph from the data
		Graph g = new Graph();
		g.getNodeTable().addColumns(GraphMLGenerator.NODE_SCHEMA);
		g.getEdgeTable().addColumns(GraphMLGenerator.EDGE_SCHEMA);
	
		
		Node root = GraphUtil.createLabelNode(g, "Patterns");
		
		// create the default pattern node
		Node noPatternLabelNode = GraphUtil.createLabelNode(g, "No pattern");
		GraphUtil.createEdge(g, VisualDBConstants.EDGE_TYPE_LABEL2LABEL, root, noPatternLabelNode);
		
		// load discussions without patterns
		this.loadDiscussionsWithNoPatterns(g, noPatternLabelNode);
		
		// load patterns
		this.loadPatterns(g, root);
		
		// add graph
		setGraph(g);
		
		// initialize the listing with the current visible nodes
		this.updateList(g);
	}
	
	private void updateList(Graph g) {
		
		this.listMenuModel.clear();
		
		// load all patterns
		Iterator <Pattern> patterns = GraphUtil.getAllPatterns(g);		
		while ( patterns.hasNext()) {
			Pattern p = patterns.next();
			Tuple t = GraphMLDataReader.getTupleById(g, new Integer(p.getId()));
			VisualItem vi = this.m_vis.getVisualItem("tree.nodes", t);
			this.listMenuModel.addElement(vi);
		}
		
		// load all discussions
		Iterator <Discussion> discussions = GraphUtil.getAllDiscussions(g);
		while ( discussions.hasNext()) {
			Discussion d = discussions.next();
			Tuple t = GraphMLDataReader.getTupleById(g, new Integer(d.getId()));
			VisualItem vi = this.m_vis.getVisualItem("tree.nodes", t);
			this.listMenuModel.addElement(vi);
		}
		
	}


	private void loadDiscussionsWithNoPatterns(Graph g, Node noPatternLabelNode) {
		Iterator<Discussion>iter = GraphUtil.getDiscussionsWithoutPattern(GraphMLDataReader.getSourceGraph(), true);
		while ( iter.hasNext() ) {
			Discussion d = iter.next();
			Node n = GraphUtil.createQuestionNode(g, d);
			GraphUtil.createEdge(g, VisualDBConstants.EDGE_TYPE_PATTERN2THREAD, noPatternLabelNode, n);
		}
	}


	private void loadPatterns(Graph g, Node root) {

		Iterator<Pattern> iter = GraphUtil.getAllPatterns(GraphMLDataReader
				.getSourceGraph());

		while (iter.hasNext()) {
			Pattern sourcePattern = iter.next();
			Node pattern = GraphUtil.createPatternNode(g, sourcePattern);

			// create edge from node to answer
			GraphUtil.createEdge(g,
					VisualDBConstants.EDGE_TYPE_LABEL2PATTERN, root, pattern);
			
			// for each pattern, load related discussions
			this.loadDiscussionsForPattern(g, sourcePattern, pattern);
		}
	}


	private void loadDiscussionsForPattern(Graph g, Pattern sourcePattern, Node pattern) {
		
		Iterator<Discussion> iter = GraphUtil.getDiscussionsByPattern(GraphMLDataReader.getSourceGraph(), sourcePattern, true);
		
		while (iter.hasNext()) {
			Discussion sourceNode = iter.next();
			
			Node n = GraphUtil.createQuestionNode(g, sourceNode);
			GraphUtil.createEdge(g, VisualDBConstants.EDGE_TYPE_PATTERN2THREAD, pattern, n);
		}
		
	}


	private void setGraph(Graph g) {
		VisualGraph vg = (VisualGraph) m_vis.add(tree, g);
		m_vis.setInteractive(treeEdges, null, false);

		// add the first node, which is the one we loaded, to the main node
		// group
		VisualItem f = (VisualItem)vg.getNode(0);
		m_vis.addFocusGroup(mainnode, new DefaultTupleSet());
		m_vis.getGroup(mainnode).setTuple(f);
		
		this.m_graph = g;
	}
	
	// -- 3. the renderers and renderer factory ---------------------------
	public void setRenderers()
	{
		DiscussionRenderer m_nodeRenderer;
		EdgeRenderer m_edgeRenderer;
		PatternRenderer patternRenderer;
		
		// -- set up renderers --
		m_nodeRenderer = new DiscussionRenderer();
		LabelRenderer titleRenderer = new LabelRenderer("title");
		titleRenderer.setRenderType(AbstractShapeRenderer.RENDER_TYPE_FILL);
		titleRenderer.setHorizontalAlignment(Constants.CENTER);
		titleRenderer.setHorizontalPadding(5);
		titleRenderer.setManageBounds(true);
		titleRenderer.setVerticalPadding(5);
		titleRenderer.setRoundedCorner(5, 5);
		m_edgeRenderer = new EdgeRenderer();
		patternRenderer = new PatternRenderer();
		
		DefaultRendererFactory drf = new DefaultRendererFactory(m_nodeRenderer);
		drf.add("type = " + VisualDBConstants.NODE_TYPE_LABEL, titleRenderer);
		drf.add("type = " + VisualDBConstants.NODE_TYPE_PATTERN, patternRenderer);
		drf.add(new InGroupPredicate(treeEdges), m_edgeRenderer);

		m_vis.setRendererFactory(drf);
	}
	

	public void setActions()
	{
		TraceService.log(TraceService.EVENT_SETUP_CREATEACTIONS_BEGIN, null);
		
		// Build search filters so they can be used in the actions here
        SearchTupleSet search = new KeywordSearchTupleSet();
        m_vis.addFocusGroup(Visualization.SEARCH_ITEMS, search);
        search.addTupleSetListener(new TupleSetListener() {
            public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
            	logger.info("tupleChanged");
                m_vis.cancel("animatePaint");
                m_vis.run("recolor");
                m_vis.run("animatePaint");
            }
        });
        
        // create the search query binding
		Graph g = (Graph) m_vis.getGroup(tree);
        this.searchQ = new SearchQueryBinding(g.getNodeTable(), "searchfield", (SearchTupleSet)this.m_vis.getGroup(Visualization.SEARCH_ITEMS));
        this.typesQ  = new ListQueryBinding(g.getNodeTable(), "type", true);
        AndPredicate andPredicateFilters = new AndPredicate(this.searchQ.getPredicate());
        andPredicateFilters.add(this.typesQ.getPredicate());
        
        // create the filter list action
        ActionList updateList = new ActionList();
        UpdateListItemsAction updateListItemsAction = new UpdateListItemsAction(tree, listMenuModel);
        updateList.add(updateListItemsAction);
        updateList.add(new EdgeVisibilityAction(treeEdges));
        this.m_vis.putAction("updateList", updateList);
       
		
		// -- set up processing actions --

		// colors
		ItemAction nodeColor = new NodeColorAction(treeNodes);
		ItemAction textColor = new TextColorAction(treeNodes);
		ItemAction nodeBorderColor = new NodeBorderColorAction(treeNodes);
		
		m_vis.putAction("textColor", textColor);

		ItemAction edgeColor = new ColorAction(treeEdges, VisualItem.STROKECOLOR, ColorLib.rgb(200, 200, 200));

		FontAction fonts = new NodeFontAction(treeNodes);

		// recolor
		ActionList recolor = new ActionList();
		recolor.add(textColor);
		recolor.add(new FillColorAction(treeNodes));
		recolor.add(new ColorAction(mainnode, VisualItem.STROKECOLOR, ColorLib.rgb(49,163,84)));
		recolor.add(new ColorAction(mainnode, VisualItem.FILLCOLOR, ColorLib.rgb(173,221,142)));
		m_vis.putAction("recolor", recolor);

		// repaint
		ActionList repaint = new ActionList();
		repaint.add(recolor);
		repaint.add(new RepaintAction());
		m_vis.putAction("repaint", repaint);

		// animate paint change
		ActionList animatePaint = new ActionList(400);
		animatePaint.add(new ColorAnimator(treeNodes));
		animatePaint.add(new RepaintAction());
		m_vis.putAction("animatePaint", animatePaint);

		// create the tree layout action
		RadialTreeLayout treeLayout = new RadialTreeLayout(tree);
		// treeLayout.setAngularBounds(-Math.PI/2, Math.PI);
		m_vis.putAction("treeLayout", treeLayout);

		CollapsedSubtreeLayout subLayout = new CollapsedSubtreeLayout(tree);
		m_vis.putAction("subLayout", subLayout);

		// create the filtering and layout
		ActionList filter = new ActionList();
		filter.add(new VisibilityFilter(andPredicateFilters));
		filter.add(new TreeRootAction(tree));
		filter.add(fonts);
		filter.add(treeLayout);
		filter.add(subLayout);
		filter.add(textColor);
		filter.add(nodeColor);
		filter.add(nodeBorderColor);
		filter.add(edgeColor);
		m_vis.putAction("filter", filter);
		m_vis.runAfter("filter", "recolor");

		// animated transition
		ActionList animate = new ActionList(1000);
		animate.setPacingFunction(new SlowInSlowOutPacer());
		animate.add(new QualityControlAnimator());
		animate.add(new VisibilityAnimator(tree));
		animate.add(new PolarLocationAnimator(treeNodes, linear));
		animate.add(new ColorAnimator(treeNodes));
		animate.add(new RepaintAction());
		m_vis.putAction("animate", animate);
		m_vis.alwaysRunAfter("filter", "animate");
		
		// register the listener for updates to the filters
        UpdateListener lstnr = new UpdateListener() {
            public void update(Object src) {
            	logger.info("update listerener detected");
                m_vis.run("filter");
                m_vis.run("recolor");
                
                // update the list
                m_vis.run("updateList");
            }
        };
        andPredicateFilters.addExpressionListener(lstnr);

        TraceService.log(TraceService.EVENT_SETUP_CREATEACTIONS_END, null);
	}
	

	public void setControls()
	{
		
		// ------------------------------------------------

		// initialize the display
		display.setSize(VisualDBConfig.DISPLAY_SIZE_WIDTH, VisualDBConfig.DISPLAY_SIZE_HEIGHT);
		//display.setSize(650, 650);
		display.pan(0, 0);

		display.setForeground(Color.BLACK);
		display.setBackground(Color.WHITE);
		display.setHighQuality(true);

		display.setItemSorter(new TreeDepthItemSorter());
		display.addControlListener(new DragControl());
		display.addControlListener(new ZoomToFitControl());
		display.addControlListener(new ZoomControl());
		display.addControlListener(new PanControl());
		display.addControlListener(new FocusControl(1, "filter"));
		display.addControlListener(new HoverActionControl("repaint"));
		display.addControlListener(new DiscussionOverviewPaneControlAdapter(this));
		display.addControlListener(new NeighborHighlightControl());
		display.addControlListener(new NodeInfoTooltipControl());
	}
	
	private Component buildCentralPanel() {
		return this.display;
	}
	
	private Component buildEastPanel() {
		logger.entering(this.getName(),"buildEastPanel");

        
        // build the search panel
        JSearchPanel searchPanel = searchQ.createSearchPanel(false);
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
					m_vis.run("repaint");
				}
				
				TraceService.log(TraceService.EVENT_LIST_MOUSEMOVED);
			}
		});
		
		listMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				// clear selection
				listMenu.clearSelection();
		        m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).clear();	
				m_vis.run("repaint");

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

					m_vis.run("repaint");
					TraceService.log(TraceService.EVENT_LIST_MOUSEENTERED, item.getString("id"));
				}
			}
			
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				
				TraceService.log(TraceService.EVENT_LIST_ITEMCLICKED);
				
				if ( evt.getClickCount() == 2 ) {
					int index = list.locationToIndex(evt.getPoint());
					VisualItem item = (VisualItem) list.getModel().getElementAt(index);
					
					// open thread details
					TraceService.log(TraceService.EVENT_DISCUSSION_OPEN);
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
		
		patternListScrollPane = new JScrollPane(patternsList);
		patternListScrollPane.setMinimumSize(new Dimension(400, 470));
		patternListScrollPane.setMaximumSize(new Dimension(400, 3000));
		patternListScrollPane.setPreferredSize(patternListScrollPane.getMinimumSize());
		
		tabbedPane.add("Pattern Browser", patternListScrollPane);
		
		logger.exiting(this.getName(),"buildEastPanel");

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
					logger.info("Reload graph " + rangeLabel.getText());
					TraceService.log(TraceService.EVENT_DATERANGE_CHANGE, rangeLabel.getText());
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

	public static DiscussionOverviewByPatternPane demo(VisualDBApplication parent) {
		DiscussionOverviewByPatternPane pane = new DiscussionOverviewByPatternPane(parent);

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
