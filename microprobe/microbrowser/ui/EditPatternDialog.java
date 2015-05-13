package microbrowser.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import microbrowser.render.VisualDBListRenderer;
import microbrowser.util.GraphUtil;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualItem;

public class EditPatternDialog extends JDialog implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;

	private VisualItem m_item;
	private Graph m_graph;
	
	private final String ACTION_ADD_PATTERN = "Add Pattern";
	private final String ACTION_CLOSE		= "Close";
	private final String ACTION_REMOVE_PATTERN = "Remove";
	
	private JButton removePatternBtn;
	private JList	patternList;
	
	public EditPatternDialog(VisualItem item) {
	
		this.m_item = item;
		this.m_graph = (Graph) item.getVisualization().getSourceData("graph");
		
		// create UI elements
		this.createUI();
		
		// specify dialog sizes
		setPreferredSize(new Dimension(600,400));
		setMinimumSize(getPreferredSize());
		this.setLocationRelativeTo(null);
	}
	
	private void createUI() {
		this.setTitle("Edit Pattern Dialog - " + m_item.getString("title"));
		
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		
		JLabel titleLabel = new JLabel("Title", SwingConstants.LEFT);
		titleLabel.setPreferredSize(new Dimension(200, 15));
		titleLabel.setMinimumSize(titleLabel.getPreferredSize());
		
		JTextField title = new JTextField(m_item.getString("title"));
		title.setPreferredSize(new Dimension(240, 25));
		title.setMinimumSize(title.getPreferredSize());
		

		JTextArea context = new JTextArea(m_item.getString("body"));
		context.setPreferredSize(new Dimension(200, 90));
		context.setMinimumSize(context.getPreferredSize());

		JTextArea solution = new JTextArea(m_item.getString("solution"));
		solution.setPreferredSize(new Dimension(200, 90));
		solution.setMinimumSize(context.getPreferredSize());
		
		this.add(UILib.getBox(new Component[] {titleLabel, title, new JLabel("Description"), context, new JLabel("Solution"), solution}, false, 5, 5), c);
		
		
		// build the patterns list
		JLabel relatedPatternsLabel = new JLabel("Related Patterns", SwingConstants.LEFT);
		DefaultListModel patternListModel = new DefaultListModel();		
		patternList = new JList(patternListModel);
		patternList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		patternList.setVisibleRowCount(-1);
		patternList.setCellRenderer(new VisualDBListRenderer());
		patternList.addListSelectionListener(this);
		JScrollPane patternListScrollPane = new JScrollPane(patternList);
		patternListScrollPane.setPreferredSize(new Dimension(250, 250));
		patternListScrollPane.setMinimumSize(patternListScrollPane.getPreferredSize());
		patternListScrollPane.setLocation(0, 0);
		
		JButton addPatternBtn = new JButton(ACTION_ADD_PATTERN);
		addPatternBtn.addActionListener(this);
		removePatternBtn = new JButton(ACTION_REMOVE_PATTERN);
		removePatternBtn.setEnabled(false);
		removePatternBtn.addActionListener(this);
		
		c.gridx++;		
		Box patternActionBtn = UILib.getBox(new Component[] {addPatternBtn, removePatternBtn}, true, 1, 1);
		this.add(UILib.getBox(new Component[] {relatedPatternsLabel, patternListScrollPane, patternActionBtn}, false, 5, 5), c);
		
		// place action buttons
		c.gridy++;
		c.gridx--;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		
		JPanel buttonPanel = new JPanel();
		JButton closeBtn = new JButton(ACTION_CLOSE);
		closeBtn.addActionListener(this);
		
		buttonPanel.add(closeBtn);
		
		this.add(buttonPanel, c);
		
		this.loadPatterns(patternListModel);
	}
	
	private void loadPatterns(DefaultListModel list) {
		
		
		Iterator patterns = GraphUtil.getRelatedPatterns(m_graph, (Node) m_item);
		
		while ( patterns.hasNext()) {
			list.addElement(patterns.next());
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if ( event.getActionCommand().equals(ACTION_ADD_PATTERN)) {
			SelectPatternDialog dialog = new SelectPatternDialog(this, (Node)m_item);
			dialog.setVisible(true);
			dialog.pack();
		}
		else if ( event.getActionCommand().equals(ACTION_CLOSE)) {
			this.dispose();
		}
		else if ( event.getActionCommand().equals(ACTION_REMOVE_PATTERN)) {
			GraphUtil.removeEdge(m_graph, (Node)m_item, (Node)patternList.getSelectedValue());
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		
		if ( !event.getValueIsAdjusting() ) {
			Node n = (Node) ((JList)event.getSource()).getSelectedValue();
			
			// toggle remove pattern
			removePatternBtn.setEnabled(n == null? false : true);
		}
	}
}
