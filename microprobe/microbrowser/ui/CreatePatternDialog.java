package microbrowser.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import microbrowser.VisualDBConfig;
import microbrowser.VisualDBConstants;
import microbrowser.data.io.GraphMLDataReader;
import microbrowser.data.io.GraphMLGenerator;
import microbrowser.data.io.VDBConnector;
import microbrowser.util.GraphUtil;
import prefuse.Visualization;
import prefuse.data.Graph;
import prefuse.data.Node;

public class CreatePatternDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -4281522437195599893L;

	private JLabel	patternNameLabel,
					patternDescriptionLabel,
					patternSolutionLabel,
					relatedPatternsLabel;
	private	JTextField	patternNameText;
	private JEditorPane patternDescriptionText,
						patternSolutionText;
	
	private JButton createPatternButton,
					cancelButton;
	
	private Graph	graph;
	
	public CreatePatternDialog(JDialog owner, Visualization m_vis, Graph graph) {
		super(owner, "Create Patern", true);
		buildDialog(m_vis, graph);
	}
	
	public CreatePatternDialog(JFrame owner, Visualization m_vis, Graph graph) {
		super(owner, "Create Pattern", true);
		buildDialog(m_vis, graph);
	}

	private void buildDialog(Visualization m_vis, Graph graph) {
		this.setResizable(false);
		this.graph = graph;
		patternNameLabel = new JLabel("Pattern Name");
		patternNameLabel.setSize(200, 25);
		patternNameLabel.setBackground(Color.red);
		patternNameLabel.setPreferredSize(patternNameLabel.getSize());
		
		patternNameText		= new JTextField();
		patternNameText.setSize(200, 200);
		patternNameText.setPreferredSize(patternNameText.getSize());
		patternNameText.setMaximumSize(patternNameText.getSize());
		
		patternDescriptionLabel	= new JLabel("Pattern Description");
		patternDescriptionText	= new JEditorPane();
		patternDescriptionText.setBorder(BorderFactory.createLoweredBevelBorder());
		patternDescriptionText.setSize(300, 100);
		patternDescriptionText.setMinimumSize(patternDescriptionText.getSize());
		
		patternSolutionLabel	= new JLabel("Pattern Solution");
		patternSolutionText		= new JEditorPane();
		patternSolutionText.setBorder(BorderFactory.createLoweredBevelBorder());
		
		patternSolutionText.setSize(300, 100);
		patternSolutionText.setMinimumSize(patternSolutionText.getSize());
		
		relatedPatternsLabel	= new JLabel("Related Patterns");
		
		createPatternButton		= new JButton("Create Pattern");
		createPatternButton.addActionListener(this);
		
		cancelButton			= new JButton("Cancel");
		cancelButton.addActionListener(this);
		JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		actionButtonsPanel.add(createPatternButton);
		actionButtonsPanel.add(cancelButton);
		
		JPanel contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(2, 5, 2, 5);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 1;
		c.weighty = 1;
		
		// gridx = columns
		// gridy = rows
		c.gridx = 0;
		c.gridy = 0;
		contentPanel.add(patternNameLabel, c);
		c.gridy = 1;
		contentPanel.add(patternNameText, c);
		c.gridx = 1;
		c.gridy = 0;
		contentPanel.add(patternDescriptionLabel, c);
		c.gridy = 1;
		contentPanel.add(patternDescriptionText, c);
		c.gridx = 0;
		c.gridy = 2;
		contentPanel.add(relatedPatternsLabel, c);
		c.gridx = 1;
		c.gridy = 2;
		contentPanel.add(patternSolutionLabel, c);
		c.gridy = 3;
		contentPanel.add(patternSolutionText, c);
		
		contentPanel.setSize(500, 250);
		contentPanel.setPreferredSize(contentPanel.getSize());
		contentPanel.setMinimumSize(contentPanel.getSize());

		actionButtonsPanel.setSize(500, 50);
		actionButtonsPanel.setPreferredSize(actionButtonsPanel.getSize());
		actionButtonsPanel.setMinimumSize(actionButtonsPanel.getSize());
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.getContentPane().add(contentPanel);
		this.getContentPane().add(actionButtonsPanel);
		
		// set dimensions
		this.setPreferredSize(new Dimension(500, 320));
		this.setMinimumSize(this.getPreferredSize());
		this.setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == this.createPatternButton) {
		
			if ( this.validateForm() ) {
				Node pattern = GraphUtil.createPatternNode(this.graph, this.patternNameText.getText(), this.patternDescriptionText.getText(), this.patternSolutionText.getText());
				GraphUtil.createPatternNode(GraphMLDataReader.getSourceGraph(), this.patternNameText.getText(), this.patternDescriptionText.getText(), this.patternSolutionText.getText());
				
				GraphMLGenerator.writeGraph(this.graph, VisualDBConstants.GRAPH_FILE_NAME);
				GraphMLGenerator.writeGraph(GraphMLDataReader.getSourceGraph(), VisualDBConstants.GRAPH_FILE_NAME_COMPLETE);
				
				// fire an event to notify property listeners of the new pattern
				this.firePropertyChange("patternName", "", this.patternNameText.getText());
								
				this.dispose();
				
				// save to database
				if ( VisualDBConfig.DATABASE_ENABLED) {
					VDBConnector.createPattern(pattern.getInt("id"), this.patternNameText.getText(), this.patternDescriptionText.getText(), this.patternSolutionText.getText());					
				}

			} else {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(this, "Please provide valid values.", "Missing required fields", JOptionPane.ERROR_MESSAGE);
			}
		} else if ( e.getSource() == this.cancelButton ) {
			this.dispose();
		}		
	}
	
	private boolean validateForm() {
		
		if ( this.patternNameText.getText().isEmpty() || this.patternNameText.getText().length() < 5 ) {
			this.patternNameText.requestFocus();
			return false;
		}
		else if ( this.patternDescriptionText.getText().isEmpty() ) {
			this.patternDescriptionText.requestFocus();
			return false;
		}
		else if ( this.patternSolutionText.getText().isEmpty()) {
			this.patternSolutionText.requestFocus();
			return false;
		}
		
		return true;
	}
	
	public void reset() {
		this.patternNameText.setText("");
		this.patternDescriptionText.setText("");
		this.patternSolutionText.setText("");
	}
}
