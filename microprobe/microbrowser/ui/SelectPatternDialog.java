package microbrowser.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import microbrowser.VisualDBConstants;
import microbrowser.data.io.GraphMLDataReader;
import microbrowser.data.io.GraphMLGenerator;
import microbrowser.util.GraphUtil;
import microbrowser.util.TraceService;
import prefuse.Visualization;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Tuple;

public class SelectPatternDialog extends JDialog implements ActionListener, ListSelectionListener, PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	
	private	JButton		selectPatternButton,
						cancelButton;
	
	private Node item;
	
	private Graph		graph;
	
	@SuppressWarnings("rawtypes")
	private JList 		patternList;
	
	@SuppressWarnings("rawtypes")
	private	DefaultListModel	patternListModel;
	
	private JEditorPane patternDescriptionText,
						patternSolutionText;

	private Visualization	m_vis;
	
	private final String CREATE_PATTERN_TITLE = "Create new pattern ...";
	
	public SelectPatternDialog(Dialog owner, Node item) {
		super(owner, "Select Pattern", true);
		this.item = item;
		//this.m_vis = this.item.getVisualization();
		//this.graph = (Graph) m_vis.getSourceData("tree");
		this.graph = GraphMLDataReader.getSourceGraph();
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		this.getContentPane().add(this.getTitlePanel());
		this.getContentPane().add(this.getPatternsPanel());
		this.getContentPane().add(this.getActionButtonsPanel());
		
		this.setPreferredSize(new Dimension(625, 400));
		this.setMinimumSize(this.getPreferredSize());
		this.setLocationRelativeTo(null);
	}
	
	private JPanel getTitlePanel() {
		JPanel content = new JPanel(new FlowLayout(FlowLayout.LEFT));		
		JLabel titleLabel = new JLabel(this.item.getString("title"));
		titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 14));
		content.add(titleLabel);

		return content;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JPanel getPatternsPanel() {
		JPanel content = new JPanel(new GridBagLayout());
		
		// build the patterns list
		JLabel selectPatternLabel = new JLabel("Select a Pattern");
		this.patternListModel = new DefaultListModel();		
		this.patternList = new JList(this.patternListModel);
		this.patternList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.patternList.setVisibleRowCount(-1);
		this.patternList.addListSelectionListener(this);
		JScrollPane patternListScrollPane = new JScrollPane(this.patternList);
		patternListScrollPane.setPreferredSize(new Dimension(100, 225));
		patternListScrollPane.setMinimumSize(patternListScrollPane.getPreferredSize());
		patternListScrollPane.setLocation(0, 0);
		
		// build the pattern contents
		JLabel patternDescriptionLabel	= new JLabel("Pattern Description");
		this.patternDescriptionText		= new JEditorPane();
		this.patternDescriptionText.setBorder(BorderFactory.createLoweredBevelBorder());
		this.patternDescriptionText.setPreferredSize(new Dimension(400, 100));
		this.patternDescriptionText.setMinimumSize(this.patternDescriptionText.getPreferredSize());
		
		JLabel patternSolutionLabel	= new JLabel("Pattern Solution");
		this.patternSolutionText	= new JEditorPane();
		this.patternSolutionText.setBorder(BorderFactory.createLoweredBevelBorder());
		this.patternSolutionText.setPreferredSize(new Dimension(400, 100));
		this.patternSolutionText.setMinimumSize(this.patternSolutionText.getPreferredSize());
	
		GridBagConstraints c = new GridBagConstraints();
		c.weightx	= 1;
		c.weighty	= 1;
		c.fill 		= GridBagConstraints.HORIZONTAL;
		c.anchor	= GridBagConstraints.FIRST_LINE_START;
		
		
		c.gridx = 0;
		c.gridy = 0;
		content.add(selectPatternLabel, c);
		c.gridy = 1;
		c.gridheight = GridBagConstraints.REMAINDER;
		
		content.add(patternListScrollPane, c);
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		content.add(patternDescriptionLabel, c);
		c.gridy = 1;
		content.add(patternDescriptionText, c);
		c.gridy = 2;
		content.add(patternSolutionLabel, c);
		c.gridy = 3;
		content.add(patternSolutionText, c);

		// load patterns
		this.loadPatterns();
		
		content.setPreferredSize(new Dimension(600, 250));
		content.setMinimumSize(content.getPreferredSize());
		content.setMaximumSize(content.getPreferredSize());
		
		return content;
	}
	
	private JPanel getActionButtonsPanel(){
		JPanel content = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		this.selectPatternButton = new JButton("Select");
		this.selectPatternButton.addActionListener(this);
		
		this.cancelButton = new JButton("Cancel");
		this.cancelButton.addActionListener(this);
		
		content.add(selectPatternButton);
		content.add(cancelButton);
		
		content.setPreferredSize(new Dimension(600, 50));
		content.setMinimumSize(content.getPreferredSize());
		content.setMaximumSize(content.getPreferredSize());
		
		return content;
	}
	
	@SuppressWarnings("unchecked")
	private void loadPatterns() { 
		
		// remove any previous patterns from the list
		this.patternListModel.removeAllElements();
		
		Iterator<?> patterns = GraphMLDataReader.getPatterns(this.graph);
		
		while ( patterns.hasNext()) {
			Tuple p = (Tuple) patterns.next();
			Node n 	= this.graph.getNode(p.getRow());
			
			// do not include the item that originally requested
			// the list (more than likely if the node is a pattern)
			if ( !n.getString("id").equals(item.getString("id"))) {
				this.patternListModel.addElement(n.getString("title"));				
			}
		}
		
		// add the "create new pattern..." item
		this.patternListModel.addElement(this.CREATE_PATTERN_TITLE);
	}
	
	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == this.cancelButton) {
			this.dispose();
			TraceService.log(TraceService.EVENT_PATTERN_SELECT_CANCEL);

		}
		else if ( e.getSource() == this.selectPatternButton && this.patternList.getSelectedValue() != null) {
				Node sourceNode = this.graph.getNode(this.item.getRow());
				Node targetNode = this.graph.getNode(GraphMLDataReader.getNodeByField(this.graph, "title", (String)this.patternList.getSelectedValue()).getRow());
				
				Node sourceNode2 = GraphMLDataReader.getNodeByField(GraphMLDataReader.getSourceGraph(), "title", sourceNode.getString("title"));
				Node targetNode2 = GraphMLDataReader.getNodeByField(GraphMLDataReader.getSourceGraph(), "title", targetNode.getString("title"));
						
				// add pattern edge
				if ( item.getInt("type") == VisualDBConstants.NODE_TYPE_DISCUSSION ) {
					//GraphUtil.createEdge(this.graph, VisualDBConstants.EDGE_TYPE_THREAD2THREAD, sourceNode, targetNode);					
					GraphUtil.createEdge(GraphMLDataReader.getSourceGraph(), VisualDBConstants.EDGE_TYPE_PATTERN2THREAD, sourceNode2, targetNode2);
				} else if ( item.getInt("type") == VisualDBConstants.NODE_TYPE_PATTERN ){
					//GraphUtil.createEdge(this.graph, VisualDBConstants.EDGE_TYPE_PATTERN2PATTERN, sourceNode, targetNode);
					GraphUtil.createEdge(GraphMLDataReader.getSourceGraph(), VisualDBConstants.EDGE_TYPE_PATTERN2PATTERN, sourceNode2, targetNode2);
				}
				
				// save the changes
				GraphMLGenerator.writeGraph(this.graph, VisualDBConstants.GRAPH_FILE_NAME);
				GraphMLGenerator.writeGraph(GraphMLDataReader.getSourceGraph(), VisualDBConstants.GRAPH_FILE_NAME_COMPLETE);
		
//				m_vis.run("draw");
//				m_vis.run("layout");
				
				TraceService.log(TraceService.EVENT_PATTERN_CHANGE, "pattern=" + targetNode.get("title") + ", target=" + sourceNode.getString("id"));
				this.dispose();
				
			
		} else {
			JOptionPane.showMessageDialog(this, "No pattern selected", "No Pattern Selected", JOptionPane.WARNING_MESSAGE);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if ( e.getSource() == this.patternList ) {
			
			this.patternDescriptionText.setText("");
			this.patternSolutionText.setText("");
			
			if ( this.CREATE_PATTERN_TITLE.equals(this.patternList.getSelectedValue())) {
				CreatePatternDialog diag = new CreatePatternDialog(this, this.m_vis, this.graph);
				
				diag.addPropertyChangeListener(this);

				diag.setVisible(true);
			} else {
				Node n = GraphMLDataReader.getNodeByField(this.graph, "title", (String)this.patternList.getSelectedValue());
				if ( n != null ) {					
					this.patternDescriptionText.setText(n.getString("body"));
					this.patternSolutionText.setText(n.getString("solution"));
					
					// ignore events that occur when the list is still adjusting
					if ( !e.getValueIsAdjusting()) {
						TraceService.log(TraceService.EVENT_PATTERN_SELECT_VIEW_PATTERN, "pattern=" + n.getString("title") + ", isAdjusting=" + e.getValueIsAdjusting());						
					}
				}				
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ( evt.getSource() instanceof CreatePatternDialog ) {
			if ( "patternName".equals(evt.getPropertyName()) ) {
				// a new pattern was created, proceed to update the list
				this.loadPatterns();
			}
		}
	}

}
