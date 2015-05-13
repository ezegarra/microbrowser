package microbrowser.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import microbrowser.VisualDBConstants;
import microbrowser.control.DiscussionDetailPaneControl;
import microbrowser.data.io.DBDataReader;
import microbrowser.data.io.GraphMLDataReader;
import microbrowser.data.io.GraphMLGenerator;
import microbrowser.data.model.Answer;
import microbrowser.render.VisualOverviewRenderer;
import microbrowser.util.GraphUtil;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.layout.CircleLayout;
import prefuse.action.layout.graph.BalloonTreeLayout;
import prefuse.activity.Activity;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.GraphLib;
import prefuse.util.PrefuseLib;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;

public class DiscussionDetailsPane_bak extends JPanel {
	private static final long serialVersionUID = 1L;
	private Visualization m_vis = null;
	private Display display;
	
    private static final String graph = "graph";
    private static final String nodes = "graph.nodes";
    private static final String edges = "graph.edges";
    
    private static final String tree = "tree";
    private static final String treeNodes = "tree.nodes";
    private static final String treeEdges = "tree.edges";
    
	private JTabbedPane tabbedPane = new JTabbedPane();		
    private JEditorPane questionText = new JEditorPane("text/html", "");
    private JEditorPane answerText = new JEditorPane("text/html", "");
    
    private int focusItemRow = 0;
    
	public DiscussionDetailsPane_bak(int threadid) {
		super(new BorderLayout());
		
		// create the visualization
		m_vis = new Visualization();
		
        this.setRenderers();
        
		display = new Display(m_vis);
        display.setSize(500,500);
        display.pan(250, 250);
        display.setForeground(Color.BLACK);
        display.setBackground(Color.WHITE);
        display.setHighQuality(true);
        
		// define layout
		this.add(buildCenterPanel()	, BorderLayout.CENTER);
		this.add(buildSouthPanel()	, BorderLayout.SOUTH);
        this.setActions();
        // adds graph to visualization and sets renderer label field
		// load data
		this.loadRecord(threadid);
        
		this.setControls();
		
		// run the action
        m_vis.run("draw");
	}

	private void setRenderers() {
		// --------------------------------------------------------------------
        // set up the renderers
        
        LabelRenderer tr = new LabelRenderer();
        DefaultRendererFactory drf = new DefaultRendererFactory(tr);
        tr.setRoundedCorner(8, 8);

        m_vis.setRendererFactory(drf);        
        drf.add("type = " + VisualDBConstants.NODE_TYPE_ANSWER, new VisualOverviewRenderer());
	}

	private void setControls() {
        // main display controls
//        display.addControlListener(new FocusControl(1));
//        display.addControlListener(new DragControl());
//        display.addControlListener(new PanControl());
//        display.addControlListener(new ZoomControl());
//        display.addControlListener(new WheelZoomControl());
//		display.addControlListener(new DetailViewControl(this));
        display.addControlListener(new ZoomToFitControl());
		
	}

	private void setActions() {
        ColorAction fill = new ColorAction(nodes, 
                VisualItem.FILLCOLOR, ColorLib.rgb(200,200,255));
        fill.add(VisualItem.FIXED, ColorLib.rgb(255,100,100));
        fill.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255,200,125));
        
        ActionList draw = new ActionList();
        draw.add(fill);
        draw.add(new ColorAction(nodes, VisualItem.STROKECOLOR, 0));
        draw.add(new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0)));
        draw.add(new ColorAction(edges, VisualItem.FILLCOLOR, ColorLib.gray(200)));
        draw.add(new ColorAction(edges, VisualItem.STROKECOLOR, ColorLib.gray(200)));
        draw.add(new CircleLayout(graph));
        draw.add(new RepaintAction());
        
        
        ActionList layout = new ActionList(Activity.INFINITY);
        //layout.add(new CircleLayout(nodes));
        BalloonTreeLayout glayout = new BalloonTreeLayout(nodes);
        
        layout.add(glayout);
        layout.add(new RepaintAction());
        
        // register the tasks with the visualization
        m_vis.putAction("draw", draw);
        m_vis.putAction("layout", layout);
        
        m_vis.runAfter("draw", "layout");
        
	}
	private Component buildCenterPanel() {
		return display;
	}
	
	private Component buildSouthPanel() {
		// set question text attributes
        questionText.setEditable(false);
        questionText.setOpaque(false);
        questionText.setAlignmentY(TOP_ALIGNMENT);
		
		//Put the question pane in a scroll pane.
		JScrollPane questionScrollPane = new JScrollPane(questionText);
		questionScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		questionScrollPane.setPreferredSize(new Dimension(250, 145));
		questionScrollPane.setMinimumSize(new Dimension(10, 10));
		
		// Put the answer text in a scroll pane
		JScrollPane answerScrollPane = new JScrollPane(answerText);
		answerScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		answerScrollPane.setPreferredSize(new Dimension(250, 145));
		answerScrollPane.setMinimumSize(new Dimension(10, 10));		
		
		// create the tabbed pane
		tabbedPane.add("Question", questionScrollPane);
		tabbedPane.add("Answers", answerScrollPane);
		return tabbedPane;
	}
	
	private void loadRecord(int threadid) {
		// 11088
		threadid = 123;
		Graph g = new Graph();
		g.getNodeTable().addColumns(GraphMLGenerator.NODE_SCHEMA);
		g.getEdgeTable().addColumns(GraphMLGenerator.EDGE_SCHEMA);
		
		Node n = GraphMLDataReader.getNodeById(GraphMLDataReader.getSourceGraph(), threadid);
		Node n1 = GraphUtil.createQuestionNode(g, n.getString("title"), n.getString("body"));

		n1.setInt("id", threadid);
		focusItemRow = n1.getRow();
		
		questionText.setText(n.getString("body"));
	
		
		// load answers
		loadAnswers(g, n1);
		
		// load related
		loadRelated(g, n1);
		
		// add graph
		setGraph(g, "title");
		
	}
	
    private void setGraph(Graph g, String label) {
        // update labeling
        DefaultRendererFactory drf = (DefaultRendererFactory)
                                                m_vis.getRendererFactory();
        ((LabelRenderer)drf.getDefaultRenderer()).setTextField(label);
        ((LabelRenderer)drf.getDefaultRenderer()).setMaxTextWidth(20);
        
        // update graph
        m_vis.removeGroup(graph);
       // VisualGraph vg = m_vis.addGraph(graph, g);
        VisualGraph vg = m_vis.addGraph(graph, GraphLib.getBalancedTree(4, 4));
        m_vis.setValue(edges, null, VisualItem.INTERACTIVE, Boolean.FALSE);
        VisualItem f = (VisualItem)vg.getNode(focusItemRow);
        m_vis.getGroup(Visualization.FOCUS_ITEMS).setTuple(f);
        f.setFixed(true);
        PrefuseLib.setX(f, null, 0);
        PrefuseLib.setY(f, null, 0);
    }    
    
    private void loadAnswers(Graph g, Node n) {
    	ArrayList<Answer> answers = null;
    	Iterator<Answer> iter = null;
    	
    	answers = DBDataReader.getAnswers(n.getInt("id"), n.getInt("acceptedanswerid"));
    	iter = answers.iterator();
    	
    	while ( iter.hasNext() ) {
    		Answer answer = iter.next();
    		Node a = GraphUtil.createAnswerNode(g, answer);
    		
    		// create edge from node to answer
    		GraphUtil.createEdge(g, VisualDBConstants.EDGE_TYPE_THREAD2ANSWER, n, a);
    		
    	}
    }
    
    private void loadRelated(Graph g, Node n) {
		Iterator<Integer> iter = GraphMLDataReader.getRelatedItemIds(GraphMLDataReader.getSourceGraph(), n.getInt("id"));
		while ( iter.hasNext() ) {
			int nodeId = iter.next();
			Node n0 = GraphMLDataReader.getNodeById(GraphMLDataReader.getSourceGraph(), nodeId);
			Node n1 = null;
			
			if ( n0.getInt("type") == VisualDBConstants.NODE_TYPE_DISCUSSION) {
				n1 = GraphUtil.createQuestionNode(g, n0.getString("title"), n0.getString("body"));				
	    		
				// create edge from node to answer
	    		GraphUtil.createEdge(g, VisualDBConstants.EDGE_TYPE_THREAD2THREAD, n, n1);
			}
			else if ( n0.getInt("type") == VisualDBConstants.NODE_TYPE_PATTERN) {
				n1 = GraphUtil.createPatternNode(g, n0.getString("title"), n0.getString("body"), n0.getString("solution"));

				// create edge from node to answer
	    		GraphUtil.createEdge(g, VisualDBConstants.EDGE_TYPE_PATTERN2THREAD, n, n1);
			}
			
			if ( n1 != null ) {
		    	n1.setInt("viewcount"			, n0.getInt("viewcount"));
		    	n1.setInt("answercount"			, n0.getInt("answercount"));
		    	n1.setInt("acceptedanswerid"	, n0.getInt("acceptedanswerid"));
		    	n1.setString("owner"			, n0.getString("owner"));
		    	n1.setLong("lasteditdate"		, n0.getLong("lasteditdate"));
		    	n1.setLong("lastactivitydate"	, n0.getLong("lastactivitydate"));
		    	
			}
		}
    }

	public void showSelectedAnswer(VisualItem item) {
		answerText.setText(item.getString("body"));
		tabbedPane.setSelectedIndex(1);
	}
}
