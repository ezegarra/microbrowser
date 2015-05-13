package microbrowser.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import microbrowser.VisualDBConstants;
import microbrowser.data.io.GraphMLDataReader;
import microbrowser.data.io.GraphMLGenerator;
import microbrowser.util.GraphUtil;
import prefuse.Visualization;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.search.PrefixSearchTupleSet;

public class AskQuestionDialog extends JDialog implements ActionListener {
	private static Logger logger = Logger.getLogger(AskQuestionDialog.class.getName());
	
	private JLabel 			questionTitleLabel,
							questionBodyLabel;
	private JTextField 		questionTitleText;
	private JEditorPane 	questionBodyText;
	private HTMLDocument 	doc 			= new HTMLDocument();
	private HTMLEditorKit  	editor		 	= new HTMLEditorKit();
	
	private JButton			createButton,
							cancelButton;

	private Graph			graph;
	
	private Visualization	m_vis;
	
	public AskQuestionDialog(JFrame owner, Visualization m_vis, Graph graph) {
		super(owner, "Ask a Question", true);
		// initialize graph utilities
		this.graph 			= graph;
		this.m_vis			= m_vis;
		
		questionTitleLabel = new JLabel("Question Title", SwingConstants.LEADING);
		questionTitleText	= new JTextField();
		questionTitleText.setSize(100, 25);
		questionTitleText.setPreferredSize(questionTitleText.getSize());
		questionTitleText.setMaximumSize(questionTitleText.getSize());

		
		questionBodyLabel	= new JLabel("Question Details", SwingConstants.LEADING);
		questionBodyText	= new JEditorPane();
		questionBodyText.setDocument(doc);
		questionBodyText.setEditorKit(editor);
		questionBodyText.setBorder(BorderFactory.createLoweredBevelBorder());
		questionBodyText.setSize(350, 150);
		questionBodyText.setPreferredSize(questionBodyText.getSize());
		questionBodyText.setMaximumSize(questionBodyText.getSize());
		questionBodyText.setMinimumSize(questionBodyText.getSize());

		createButton		= new JButton("Ask Question");
		createButton.addActionListener(this);
		cancelButton		= new JButton("Cancel");
		cancelButton.addActionListener(this);
		
		JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		actionButtonsPanel.add(createButton);
		actionButtonsPanel.add(cancelButton);
		
		JPanel contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 1;
		c.weighty = 1;
		
		c.gridx = 0;
		c.gridy = 0;
		contentPanel.add(questionTitleLabel, c);
		c.gridx = 1;
		contentPanel.add(questionTitleText, c);
		c.gridx = 0;
		c.gridy = 1;
		contentPanel.add(questionBodyLabel, c);
		c.gridx = 1;
		contentPanel.add(questionBodyText, c);
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.getContentPane().add(contentPanel);
		this.getContentPane().add(actionButtonsPanel);
		
		this.setPreferredSize(new Dimension(500, 300));
		this.setMinimumSize(this.getPreferredSize());
		this.setLocationRelativeTo(null);
			
		this.pack();
	}

	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent e) {

		if ( e.getSource() == createButton) {
			
			// validate form contents			
			if ( validateForm()) {
				
				logger.info("Creating question");
				
				// if valid, create form
				Node n = GraphUtil.createQuestionNode(this.graph, this.questionTitleText.getText(), this.questionBodyText.getText());
				GraphUtil.createQuestionNode(GraphMLDataReader.getSourceGraph(), this.questionTitleText.getText(), this.questionBodyText.getText());
				
				Node root = this.graph.getNode(1);
				
				GraphUtil.createEdge(this.graph, VisualDBConstants.EDGE_TYPE_PATTERN2THREAD, root, n);
				GraphMLGenerator.writeGraph(this.graph, VisualDBConstants.GRAPH_FILE_NAME);
				GraphMLGenerator.writeGraph(GraphMLDataReader.getSourceGraph(), VisualDBConstants.GRAPH_FILE_NAME_COMPLETE);
		
				PrefixSearchTupleSet st = (PrefixSearchTupleSet) m_vis.getFocusGroup(Visualization.SEARCH_ITEMS);
				st.index(n, "body");
				this.dispose();
				
			
			} else {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(this, "Enter a question title that consists of at least 5 characters");
			}
		} else if ( e.getSource() == cancelButton) {
			// dispose of dialog, do nothing
			this.dispose();
		}

	}

	private boolean validateForm() {
		if ( questionTitleText.getText().isEmpty() || questionTitleText.getText().length() < 5) {
			questionTitleText.requestFocus();
			return false;
		}
		return true;
	}

	public void reset() {
		this.questionBodyText.setText("");
		this.questionTitleText.setText("");
	}
}
