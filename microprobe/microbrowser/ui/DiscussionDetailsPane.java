package microbrowser.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import microbrowser.MicroBrowserApplication;
import microbrowser.VisualDBConfig;
import microbrowser.VisualDBConstants;
import microbrowser.action.EdgeStrokeColorAction;
import microbrowser.action.FillColorAction;
import microbrowser.control.DiscussionDetailPaneControl;
import microbrowser.control.NodeInfoTooltipControl;
import microbrowser.data.io.DBDataReader;
import microbrowser.data.io.GraphMLDataReader;
import microbrowser.data.io.GraphMLGenerator;
import microbrowser.data.model.Answer;
import microbrowser.render.AnswersListRenderer;
import microbrowser.render.DiscussionRenderer;
import microbrowser.render.VisualDBListRenderer;
import microbrowser.util.GraphUtil;
import microbrowser.util.TraceService;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.GroupAction;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.PolarLocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.graph.RadialTreeLayout;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.HoverActionControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.expression.OrPredicate;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.search.PrefixSearchTupleSet;
import prefuse.data.search.SearchTupleSet;
import prefuse.data.tuple.DefaultTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.ui.JFastLabel;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.sort.TreeDepthItemSorter;

public class DiscussionDetailsPane extends JPanel {
	private static Logger logger = Logger.getLogger(DiscussionDetailsPane.class.getName());
	private static final long serialVersionUID = 1L;

	private Visualization m_vis = null;
	private Display display = null;
	public MicroBrowserApplication parent = null;
	protected Node theNode = null;

	private DiscussionRenderer m_nodeRenderer;
	private EdgeRenderer m_edgeRenderer;

	private static final String tree = "tree";
	private static final String treeNodes = "tree.nodes";
	private static final String treeEdges = "tree.edges";
	private static final String linear = "linear";
	private static final String mainnode = "mainnodes";

	protected JTabbedPane tabbedPane = new JTabbedPane();
	private JEditorPane questionText = new JEditorPane("text/html", "");
	private JEditorPane answerText = new JEditorPane("text/html", "");
	private JScrollPane answerScrollPane = null;
	
	private JTabbedPane eastPanelTabPane = new JTabbedPane();
	private DefaultListModel listMenuModel = new DefaultListModel();
	private JList listMenu = null;
	private DefaultListModel answersListMenuModel = new DefaultListModel();
	private JList answersList = null;
	
	public DiscussionDetailsPane(MicroBrowserApplication parent, int threadid) {
		super(new BorderLayout());

		this.parent = parent;

		m_vis = new Visualization();

		// -- set up visualization --
		display = new Display(m_vis);

		// load data
		this.loadRecord(threadid);

		this.setRenderers();

		this.setActions();

		this.setControls();

		// define layout
		this.add(buildCenterPanel(), BorderLayout.CENTER);
		this.add(buildSouthPanel(), BorderLayout.SOUTH);
		this.add(buildEastPanel(), BorderLayout.EAST);

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

		SearchTupleSet search = new PrefixSearchTupleSet();
		m_vis.addFocusGroup(Visualization.SEARCH_ITEMS, search);
		search.addTupleSetListener(new TupleSetListener() {
			public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
				m_vis.cancel("animatePaint");
				m_vis.run("recolor");
				m_vis.run("animatePaint");
			}
		});
		
	}

	private void setRenderers() {
		logger.info("set up renderers");
		
		// -- set up renderers --
		m_nodeRenderer = new DiscussionRenderer();
		LabelRenderer titleRendered = new LabelRenderer("title");
		titleRendered.setRenderType(AbstractShapeRenderer.RENDER_TYPE_FILL);
		titleRendered.setHorizontalAlignment(Constants.CENTER);
		titleRendered.setHorizontalPadding(5);
		titleRendered.setManageBounds(true);
		titleRendered.setVerticalPadding(5);
		titleRendered.setRoundedCorner(5, 5);
		m_edgeRenderer = new EdgeRenderer();

		DefaultRendererFactory drf = new DefaultRendererFactory(m_nodeRenderer);
		drf.add("type = " + VisualDBConstants.NODE_TYPE_LABEL, titleRendered);
		drf.add(new InGroupPredicate(treeEdges), m_edgeRenderer);

		m_vis.setRendererFactory(drf);
	}

	private void setControls() {
		logger.info("set up controls");
		// ------------------------------------------------

		// initialize the display
		display.setSize(650, 650);
		display.pan(0, 0);

		display.setForeground(Color.BLACK);
		display.setBackground(Color.WHITE);
		display.setHighQuality(true);

		display.setItemSorter(new TreeDepthItemSorter());
		display.addControlListener(new DragControl());
		display.addControlListener(new ZoomToFitControl());
		display.addControlListener(new ZoomControl());
		display.addControlListener(new PanControl());
		FocusControl fc = new FocusControl(1, "filter");
		fc.setFilter(new OrPredicate(new OrPredicate((Predicate) ExpressionParser.parse("type = " + VisualDBConstants.NODE_TYPE_ANSWER ), 
				(Predicate) ExpressionParser.parse("type = " + VisualDBConstants.NODE_TYPE_ANSWER_ACCEPTED )), 
				(Predicate) ExpressionParser.parse("INGROUP('tree.nodes') AND id = " + theNode.getInt("id")) ));
		display.addControlListener(fc);
		display.addControlListener(new HoverActionControl("repaint"));
		display.addControlListener(new DiscussionDetailPaneControl(this));
		display.addControlListener(new NodeInfoTooltipControl());
	}

	private void setActions() {
		logger.info("set up actions");
		
		// -- set up processing actions --

		// colors
		ItemAction nodeColor = new NodeColorAction(treeNodes);
		ItemAction textColor = new TextColorAction(treeNodes);
		m_vis.putAction("textColor", textColor);

		ItemAction edgeColor = new EdgeStrokeColorAction(treeEdges);
		
		FontAction fonts = new FontAction(treeNodes, FontLib.getFont("Tahoma",
				10));
		fonts.add("ingroup('_focus_')", FontLib.getFont("Tahoma", 11));

		// recolor
		ActionList recolor = new ActionList();
		//recolor.add(nodeColor);
		recolor.add(textColor);
		recolor.add(new ColorAction(treeNodes, VisualItem.STROKECOLOR, ColorLib
				.rgb(99, 99, 99)));
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
		filter.add(new TreeRootAction(tree));
		filter.add(fonts);
		filter.add(treeLayout);
		filter.add(subLayout);
		filter.add(textColor);
		filter.add(nodeColor);
		filter.add(edgeColor);
		m_vis.putAction("filter", filter);
		m_vis.runAfter("filter", "recolor");

		// animated transition
		ActionList animate = new ActionList(1250);
		animate.setPacingFunction(new SlowInSlowOutPacer());
		animate.add(new QualityControlAnimator());
		animate.add(new VisibilityAnimator(tree));
		animate.add(new PolarLocationAnimator(treeNodes, linear));
		animate.add(new ColorAnimator(treeNodes));
		animate.add(new RepaintAction());
		m_vis.putAction("animate", animate);
		m_vis.alwaysRunAfter("filter", "animate");

	}

	private Component buildCenterPanel() {
		return display;
	}

	protected Component buildSouthPanel() {
		// set question text attributes
		questionText.setEditable(false);
		questionText.setBackground(Color.white);
		questionText.setOpaque(true);
		questionText.setAlignmentY(TOP_ALIGNMENT);

		// Put the question pane in a scroll pane.
		final JScrollPane questionScrollPane = new JScrollPane(questionText);
		questionScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		questionScrollPane.setPreferredSize(new Dimension(250, 200));
		questionScrollPane
				.setMinimumSize(questionScrollPane.getPreferredSize());

		// Put the answer text in a scroll pane
		answerScrollPane = new JScrollPane(answerText);
		answerScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		answerScrollPane.setPreferredSize(new Dimension(250, 145));
		answerScrollPane.setMinimumSize(new Dimension(10, 10));

		// create the tabbed pane
		tabbedPane.add("Question", questionScrollPane);
		tabbedPane.add("Answers", answerScrollPane);

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				questionScrollPane.getVerticalScrollBar().setValue(0);
				answerScrollPane.getVerticalScrollBar().setValue(0);
			}
		});

		// define the action buttons
		JFastLabel title = new JFastLabel("Asked by " + theNode.getString("owner") );
		UILib.setFont(title, new Font(Font.MONOSPACED, Font.BOLD, 14));
		Node patternNode = GraphMLDataReader.getPatternForNode(GraphMLDataReader.getSourceGraph(), theNode);
		String t = patternNode == null? "No pattern selected" : patternNode.getString("title");
		JFastLabel pattern = new JFastLabel( t );
		pattern.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
		JButton submitAnswer = new JButton("Create Answer");
		submitAnswer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SubmitAnswerDialog d = new SubmitAnswerDialog(null, theNode);
				d.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						// reload the graph
						parent.openDiscussionDetails(theNode);
						
						parent.openDiscussionOverview(false);
					}
				});
				d.setVisible(true);
				
				TraceService.log(TraceService.EVENT_ANSWER_CREATE_OPEN);				
			}
		});
		
		JButton changePattern = new JButton("Change Pattern");
		changePattern.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				TraceService.log(TraceService.EVENT_PATTERN_CHANGE_OPEN);

				SelectPatternDialog d = new SelectPatternDialog(null, theNode);
				d.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						// reload the graph
						parent.openDiscussionDetails(theNode);
						
						parent.openDiscussionOverview(false);
					}
				});
				d.setVisible(true);
			}
		});
		Box infoPanel = UILib.getBox(new Component[] { title, submitAnswer, pattern, changePattern }, true, 10, 0);
		Box southPanel = UILib.getBox(
				new Component[] { infoPanel, tabbedPane }, false, 0, 0);
		return southPanel;
	}

	private Component buildEastPanel() {
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
			}
		});
		
		listMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				// clear selection
				listMenu.clearSelection();
		        m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).clear();	
				m_vis.run("repaint");
				TraceService.log(TraceService.EVENT_LIST_DETAILS_RELATED_MOUSEEXITED, "type=detailRelatedList" );
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

					TraceService.log(TraceService.EVENT_LIST_DETAILS_RELATED_MOUSEENTERED, "type=detailRelatedList, item=" + item.getString("id"));

				}
			}
			
			public void mouseClicked(MouseEvent evt) {
				
				int index = listMenu.locationToIndex(evt.getPoint());
				VisualItem item = (VisualItem) listMenu.getModel().getElementAt(index);

				TraceService.log(TraceService.EVENT_LIST_DETAILS_RELATED_ITEMCLICKED, "type=detailRelatedList, item=" + item.getString("id"));
				
				if ( evt.getClickCount() == 2 ) {
					// open thread details
					parent.openDiscussionDetails((Node)item);
				}
				
			}			
		});
		
		JScrollPane listScrollPane = new JScrollPane(listMenu);
		listScrollPane.setMinimumSize(new Dimension(450, 470));
		listScrollPane.setPreferredSize(listScrollPane.getMinimumSize());

		// define the answers list
		answersList = new JList(answersListMenuModel);
		answersList.setCellRenderer(new AnswersListRenderer());
		answersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		answersList.setVisibleRowCount(-1);
		JScrollPane answersListScrollPane = new JScrollPane(answersList);
		answersListScrollPane.setMinimumSize(new Dimension(450, 470));
		answersListScrollPane.setPreferredSize(answersListScrollPane.getMinimumSize());
		ListSelectionListener sl = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {

					VisualItem item = (VisualItem)answersList.getSelectedValue();

					// clear the list selections
					m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).clear();	
					m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).setTuple(item);
					m_vis.run("repaint");

					// open the details of the selected answer
					showSelectedAnswer(item, VisualDBConstants.ORIGIN_VIEW_DETAILS_ANSWER_LIST);
				}
			}
		};
		answersList.addListSelectionListener(sl);
		answersList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				TraceService.log(TraceService.EVENT_LIST_DETAILS_ANSWER_MOUSEENTERED, "type=detailAnswersList");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				TraceService.log(TraceService.EVENT_LIST_DETAILS_ANSWER_MOUSEEXITED, "type=detailAnswersList");
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = listMenu.locationToIndex(e.getPoint());
				VisualItem item = (VisualItem) listMenu.getModel().getElementAt(index);
				
				TraceService.log(TraceService.EVENT_LIST_DETAILS_ANSWER_ITEMCLICKED, "type=detailAnswersList, item=" + item.getString("id"));			}
		});
		
		// create the east panel tab
		eastPanelTabPane = new JTabbedPane();

		eastPanelTabPane.addTab("Answers", answersListScrollPane);			
		eastPanelTabPane.addTab("Related", listScrollPane);

		return eastPanelTabPane;
	}

	private void loadRecord(int threadid) {
		logger.info("id=" + threadid);
		
		// 11088
		// threadid = 123;
		Graph g = new Graph();
		g.getNodeTable().addColumns(GraphMLGenerator.NODE_SCHEMA);
		g.getEdgeTable().addColumns(GraphMLGenerator.EDGE_SCHEMA);

		Node n = GraphMLDataReader.getNodeById(
				GraphMLDataReader.getSourceGraph(), threadid);
		Node n1 = GraphUtil.createQuestionNode(g, n);

		n1.setInt("id", threadid);
		questionText.setText(n.getString("body"));

		// save the main node
		theNode = n;
		
		// add graph
		setGraph(g);

		// load answers
		loadAnswers(g, n1);

		// load related
		loadRelated(g, n1);


		logger.info("threadid: " + threadid + ", record: " + n);
	}

	private void setGraph(Graph g) {
		VisualGraph vg = (VisualGraph) m_vis.add(tree, g);
		m_vis.setInteractive(treeEdges, null, false);

		// add the first node, which is the one we loaded, to the main node
		// group
		VisualItem f = (VisualItem)vg.getNode(0);
		m_vis.addFocusGroup(mainnode, new DefaultTupleSet());
		m_vis.getGroup(mainnode).setTuple(f);
	}

	private void loadAnswers(Graph g, Node n) {
		ArrayList<Answer> answers = new ArrayList<Answer>();
		Iterator<Answer> iter = null;
		Node n0 = null;

		if ( VisualDBConfig.DATABASE_ENABLED) {
			answers = DBDataReader.getAnswers(n.getInt("id"), n.getInt("acceptedanswerid"));
		}

		iter = answers.iterator();

		while (iter.hasNext()) {

			if (n0 == null) {
				n0 = GraphUtil.createLabelNode(g, "Answers");

				// create edge from node to answer
				Edge e = GraphUtil.createEdge(g,
						VisualDBConstants.EDGE_TYPE_THREAD2ANSWER, n, n0);
				e.setDouble("similarity", 1.0);
			}
			Answer answer = iter.next();
			Node a = GraphUtil.createAnswerNode(g, answer);

			// create edge from node to answer
			Edge e = GraphUtil.createEdge(g, VisualDBConstants.EDGE_TYPE_THREAD2ANSWER,
					n0, a);
			e.setDouble("similarity", 1.0);
			// add node to the answers list
			answersListMenuModel.addElement(m_vis.getVisualItem(tree, a));
		}
		
		logger.info("Loaded " + answers.size() + " answers.");
	}

	/**
	 * Load discussions related to the selected node
	 * 
	 * @param g
	 *            the graph
	 * @param n
	 *            the selected node
	 */
	private void loadRelated(Graph g, Node n) {
		Iterator<Integer> iter = GraphMLDataReader.getRelatedItemIds(
				GraphMLDataReader.getSourceGraph(), n.getInt("id"));
		
		ArrayList<Integer> filteredRelated = new ArrayList<Integer>();
		while ( iter.hasNext()) {
			filteredRelated.add(iter.next());
		}
		
		Integer forFiltering [] = filteredRelated.toArray(new Integer[filteredRelated.size()]);
		
		Node patternsNode = null;
		Node relatedNode = null;
		int count = 0;
		for ( int  index = forFiltering.length; index > 0 && count < VisualDBConfig.DETAIL_VIEW_MAX_RELATED; index--) {
			//int nodeId = iter.next();
			int nodeId = forFiltering[index-1];
			Node n0 = GraphMLDataReader.getNodeById(
					GraphMLDataReader.getSourceGraph(), nodeId);
			Node n1 = null;

			if (n0.getInt("type") == VisualDBConstants.NODE_TYPE_DISCUSSION) {

				if (relatedNode == null) {
					relatedNode = GraphUtil.createLabelNode(g, "Related discussions");

					// create edge from node to related
					Edge e = GraphUtil.createEdge(g,
							VisualDBConstants.EDGE_TYPE_THREAD2THREAD, n,
							relatedNode);
					e.setDouble("similarity", 1.0);
				}

				n1 = GraphUtil.createQuestionNode(g, n0);

				// create edge from node to answer
				Edge e = GraphUtil.createEdge(g,
						VisualDBConstants.EDGE_TYPE_THREAD2THREAD, relatedNode,
						n1); 
				e.setDouble("similarity", 1.0);
				
				if ( theNode == null) {
					System.err.println("Node null" + theNode);
				}
				int id1 = theNode.getInt("id");
				int id2 = n0.getInt("id");
				
				Edge originalE = (Edge) GraphUtil.findEdge(GraphMLDataReader.getSourceGraph(), id1, id2);
				if ( originalE != null)
					e.setDouble("similarity", originalE.getDouble("similarity"));

				// add node the the reference list
				listMenuModel.addElement(m_vis.getVisualItem(tree, n1));
				
				// increment the total count of releated discussions
				count++;
			} else if (n0.getInt("type") == VisualDBConstants.NODE_TYPE_PATTERN) {

				if (patternsNode == null) {
					patternsNode = GraphUtil.createLabelNode(g, "pattern");

					// create edge from node to related
					GraphUtil.createEdge(g,
							VisualDBConstants.EDGE_TYPE_PATTERN2THREAD, n,
							patternsNode);
				}
				n1 = GraphUtil.createPatternNode(g, n0.getInt("id"), n0.getString("title"),
						n0.getString("body"), n0.getString("solution"));

				// create edge from node to answer
				GraphUtil.createEdge(g,
						VisualDBConstants.EDGE_TYPE_PATTERN2THREAD,
						patternsNode, n1);
			}
		}
	}

	/**
	 * Retrieves the body of the elmenent selected for display
	 * 
	 * @param item
	 *            the clicked answer node
	 */
	public void showSelectedAnswer(VisualItem item, int origin) {
		if ( item != null ) {
			logger.info("item=" + item.getInt("id") + ", type=" + item.getInt("type"));
			answerText.setText(item.getString("body"));
			tabbedPane.setSelectedIndex(1);
			eastPanelTabPane.setSelectedIndex(0);
			
			// scroll the answer to the top
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					answerScrollPane.getVerticalScrollBar().setValue(0);
				}
			});
			
			if ( origin == VisualDBConstants.ORIGIN_VIEW_DETAILS_DIAGRAM) {
				TraceService.log(TraceService.EVENT_ANSWER_OPEN_DETAILS_DIAGRAM, "item=" + item.getInt("id") + ", type=" + item.getInt("type"));				
			} else if ( origin == VisualDBConstants.ORIGIN_VIEW_DETAILS_ANSWER_LIST) {
				TraceService.log(TraceService.EVENT_ANSWER_OPEN_DETAILS_LIST_ANSWER, "item=" + item.getInt("id") + ", type=" + item.getInt("type"));
			} else {
				TraceService.log(TraceService.EVENT_ANSWER_OPEN_UNKNOWN, "item=" + item.getInt("id") + ", type=" + item.getInt("type"));
			}
		}
	}
	
	/**
	 * This method is called by the answer list listener.  As an item is selected
	 * by either the user from the list or from the control, it will call this method
	 * @param item
	 */
	public void setSelectedAnswer(VisualItem item) {
		
		answersList.setSelectedValue(item, true);
	}
	
	/**
	 * Switches to the question tab
	 * 
	 * @param threadid
	 * @return
	 */
	public void showSelectedQuestion(VisualItem item) {
		logger.info("item=" + item.getInt("id") + ", type=" + item.getInt("type"));		
		questionText.setText(item.getString("body"));
		tabbedPane.setSelectedIndex(0);
		eastPanelTabPane.setSelectedIndex(1);
		listMenu.setSelectedValue(item,  true);
	}

	/**
	 * Selects the item in the list
	 * 
	 * @param item the item to select
	 */
	public void selectItemFromList(VisualItem item) {
		if ( item != null ) {
	       listMenu.setSelectedValue(item, true);
	       eastPanelTabPane.setSelectedIndex(1);	    	   

		} else {
			listMenu.clearSelection();
			answersList.clearSelection();
		}
	}
	
	/**
	 * 
	 * @param parent
	 * @param threadid
	 * @return
	 */
	public static JPanel demo(MicroBrowserApplication parent, int threadid) {
		DiscussionDetailsPane pane = new DiscussionDetailsPane(parent, threadid);

		return pane;
	}

	// ------------------------------------------------------------------------

	/**
	 * Switch the root of the tree by requesting a new spanning tree at the
	 * desired root
	 */
	public static class TreeRootAction extends GroupAction {
		public TreeRootAction(String graphGroup) {
			super(graphGroup);
		}

		public void run(double frac) {
			TupleSet focus = m_vis.getGroup(Visualization.FOCUS_ITEMS);
			if (focus == null || focus.getTupleCount() == 0)
				return;

			Graph g = (Graph) m_vis.getGroup(m_group);
			Node f = null;
			Iterator tuples = focus.tuples();
			while (tuples.hasNext()
					&& !g.containsTuple(f = (Node) tuples.next())) {
				f = null;
			}
			if (f == null)
				return;
			g.getSpanningTree(f);
		}
	}

	/**
	 * Set node fill colors
	 */
	public static class NodeColorAction extends ColorAction {
		public NodeColorAction(String group) {
			super(group, VisualItem.FILLCOLOR, ColorLib.rgba(255, 255, 255, 0));
			add("_hover", ColorLib.gray(220, 230));
			add("ingroup('_search_')", ColorLib.rgb(255, 190, 190));
			add("ingroup('_focus_')", ColorLib.rgb(198, 229, 229));
		}

	} // end of inner class NodeColorAction

	/**
	 * Set node text colors
	 */
	public static class TextColorAction extends ColorAction {
		public TextColorAction(String group) {
			super(group, VisualItem.TEXTCOLOR, ColorLib.gray(0));
			add("_hover", ColorLib.rgb(255, 0, 0));
		}
	} // end of inner class TextColorAction
}
