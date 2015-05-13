package microbrowser.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.text.html.HTMLEditorKit;

import microbrowser.VisualDBConfig;
import microbrowser.VisualDBConstants;
import microbrowser.data.io.GraphMLDataReader;
import microbrowser.data.io.VDBConnector;
import microbrowser.render.VisualDBListRenderer;
import microbrowser.util.TraceService;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Tuple;
import prefuse.visual.VisualItem;

public class ViewDiscussionDetailsDialog extends JDialog implements ActionListener, WindowListener {
	private static final long serialVersionUID = 9105968381220966283L;

	private JLabel		discussionTitle,
						authorLabel,
						authorText,
						patternLabel,
						patternText,
						relatedDiscussionsTitle;
	
	private JEditorPane discussionBody;
	
	private JPanel		answersPanel,
						relatedPanel;
	
	private JButton		changePatternButton,
						submitAnswerButton;
	
	private Graph		graph;
	
	private VisualItem	item;
	
	@SuppressWarnings("rawtypes")
	private DefaultListModel relatedItemsModel = new DefaultListModel();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JList relatedItemsList = new JList(relatedItemsModel);
	
	public ViewDiscussionDetailsDialog(JFrame owner, Graph graph, VisualItem item ) {
		super(owner, item.getString("title"), false);
		System.out.println("id=" + item.getString("id") + ", title=" + item.getString("title"));
		this.graph 	= graph;
		this.item	= item;
		
		discussionTitle = new JLabel(item.getString("title"));
		discussionTitle.setFont(new Font(discussionTitle.getFont().getName(), Font.BOLD, 14));
		
		authorLabel		= new JLabel( "Asked by: ");
		authorText		= new JLabel( item.getString("owner") );
		patternLabel	= new JLabel( "Pattern: ");
		patternText		= new JLabel( "the pattern");
		changePatternButton = new JButton("Change Pattern");
		changePatternButton.addActionListener(this);
		submitAnswerButton	= new JButton("Submit Answer");
		submitAnswerButton.addActionListener(this);
		
		answersPanel	= this.loadAnswers(item);
		answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.Y_AXIS));
		
		relatedDiscussionsTitle = new JLabel("Related Discussions");
		relatedPanel	= this.loadRelatedDiscussions(item);
		
		this.setLayout(new BorderLayout());
		
		this.getContentPane().add(discussionTitle, BorderLayout.PAGE_START);
		

		discussionBody = new JEditorPane();
        HTMLEditorKit editorKit = new HTMLEditorKit();
		discussionBody.setEditable(false);
		discussionBody.setEditorKit(editorKit);
		discussionBody.setOpaque(false);
		//discussionBody.setPreferredSize(new Dimension(700, 300));
		//discussionBody.setMinimumSize(new Dimension(700, 200));
		discussionBody.setText(item.getString("body"));
				
		JPanel discussionInfoPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		discussionInfoPane.add(authorLabel);
		discussionInfoPane.add(authorText);
		
		if ( 
				VisualDBConfig.EXPERIMENT_MODE == VisualDBConstants.EXPERIMENT_MODE_PATTERN_ONLY || 
				VisualDBConfig.EXPERIMENT_MODE == VisualDBConstants.EXPERIMENT_MODE_PATTERN_LEADERBOARD ) {
			discussionInfoPane.add(patternLabel);
			discussionInfoPane.add(patternText);
			this.getPatternName();
			discussionInfoPane.add(changePatternButton);
		}
		
		discussionInfoPane.add(submitAnswerButton);
		discussionInfoPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		discussionInfoPane.setPreferredSize(new Dimension(700, 50));
		discussionInfoPane.setMinimumSize(discussionInfoPane.getPreferredSize());
		discussionInfoPane.setMaximumSize(discussionInfoPane.getPreferredSize());
		
		JPanel centerPane = new JPanel();
		centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.Y_AXIS));
		final JScrollPane discussionBodyScrollPane = new JScrollPane(discussionBody);
		discussionBodyScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		discussionBodyScrollPane.setPreferredSize(new Dimension(700, 300));
		discussionBodyScrollPane.setMinimumSize(new Dimension(700, 200));

		centerPane.add(discussionBodyScrollPane);
		centerPane.add(discussionInfoPane);
		centerPane.add(new JScrollPane(answersPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		centerPane.setPreferredSize(new Dimension(700, 750));
		centerPane.setMinimumSize(centerPane.getPreferredSize());
		this.getContentPane().add(centerPane, BorderLayout.CENTER);
		
		JPanel rightPane = new JPanel();
		rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
		rightPane.add(relatedDiscussionsTitle);
		rightPane.add(new JScrollPane(relatedPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		this.getContentPane().add(rightPane, BorderLayout.LINE_END);		
		
		// set dimensions
		this.setPreferredSize(new Dimension(950, 750));
		this.setMinimumSize(this.getPreferredSize());
		this.setLocationRelativeTo(null);

		
		this.pack();
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			   public void run() { 
				   discussionBodyScrollPane.getVerticalScrollBar().setValue(0);
				   discussionBodyScrollPane.getHorizontalScrollBar().setValue(0);
			   }
			});

	}

	@SuppressWarnings("unchecked")
	private JPanel loadRelatedDiscussions(VisualItem item) {
		// init the list model
		relatedItemsList.setCellRenderer(new VisualDBListRenderer());
		relatedItemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		relatedItemsList.setVisibleRowCount(-1);
		relatedItemsList.addMouseListener(new MouseAdapter() {

			
			@SuppressWarnings("rawtypes")
			public void mouseClicked(MouseEvent e) {
				
				if ( e.getClickCount() == 2 ) {
					JList list = (JList) e.getSource();
					int index = list.locationToIndex(e.getPoint());
					if ( index != -1 ) {
						VisualItem item = (VisualItem) list.getModel().getElementAt(index);
						
						// open thread details
						ViewDiscussionDetailsDialog threadDetailsDialog = new ViewDiscussionDetailsDialog(null, (Graph)item.getVisualization().getVisualGroup("graph"), item);
						threadDetailsDialog.setVisible(true);
						
						String event = "";
						switch ( item.getInt("type")) {
							case VisualDBConstants.NODE_TYPE_DISCUSSION:
								event = TraceService.EVENT_DISCUSSION_OPEN;
								break;
							case VisualDBConstants.NODE_TYPE_PATTERN:
								event = TraceService.EVENT_PATTERN_OPEN;
								break;
						}

						TraceService.log(event, item.getString("id"));
					}					
				}
			}
			
		});
		
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		
		GraphMLDataReader dataReader = new GraphMLDataReader(VisualDBConstants.GRAPH_FILE_NAME_COMPLETE);
		Iterator<Integer> iter = dataReader.getRelatedItemIds(this.graph, item.getInt("id"));
		while ( iter.hasNext() ) {

			Tuple t = GraphMLDataReader.getTupleById(this.graph, iter.next());
			VisualItem vi = item.getVisualization().getVisualItem("graph.nodes", t);
			
			relatedItemsModel.addElement(vi);
		}
		
		content.add(relatedItemsList);
		content.setPreferredSize(new Dimension(150, 800));
		content.setMinimumSize(content.getPreferredSize());
		
		return content;
	}

	private  JPanel loadAnswers(VisualItem item ) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM VISUAL.THREADS T WHERE POSTTYPEID = 2 AND PARENTID = ? FOR READ ONLY";
		
		JPanel content = new JPanel();

		try {
			 conn = VDBConnector.getConnection();
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setInt(1, item.getInt("id"));
			 rs = pstmt.executeQuery();
			 
			 while ( rs.next()) {
				JPanel p = new JPanel(new BorderLayout(5, 5));
				String creator = rs.getString("OWNERDISPLAYNAME");
				if ( creator == null || creator.trim().equals("")) {
					creator = rs.getString("OWNERUSERID");
				}
				
				JLabel score = new JLabel(rs.getString("SCORE"), SwingConstants.CENTER);
				// check if the current answer is the accepted answer for the discussion thread
				if ( item.getInt("acceptedanswerid") == rs.getInt("ID")) {
					score.setForeground(Color.RED);
				}
				score.setFont(new Font(score.getFont().getName(),Font.BOLD,30));
				score.setMinimumSize(new Dimension(200, 100));
				score.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
				p.add(score, BorderLayout.LINE_START);
				
				//Create an editor pane.
				final JScrollPane answerBodyScrollPane = new JScrollPane(new JEditorPane("text/html", rs.getString("BODY")));
				answerBodyScrollPane.setVerticalScrollBarPolicy(
		                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				answerBodyScrollPane.setPreferredSize(new Dimension(250, 150));
				answerBodyScrollPane.setMinimumSize(new Dimension(250, 100));
				p.add(answerBodyScrollPane, BorderLayout.CENTER);
				
				JPanel answerInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
				answerInfoPanel.add(new JLabel("Answer submitted on "));
				answerInfoPanel.add(new JLabel(rs.getString("CREATIONDATE")));
				answerInfoPanel.add(new JLabel(" by " + creator));
				p.add(answerInfoPanel, BorderLayout.PAGE_END);
				content.add(p);
				 
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
				   public void run() { 
					   answerBodyScrollPane.getVerticalScrollBar().setValue(0);
					   answerBodyScrollPane.getHorizontalScrollBar().setValue(0);
				   }
				});
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if ( conn != null ) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}	

		return content;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == this.changePatternButton) {
			SelectPatternDialog d = new SelectPatternDialog(this, (Node)this.item);
			d.addWindowListener(this);
			d.setVisible(true);

			TraceService.log(TraceService.EVENT_PATTERN_CHANGE_OPEN);
			
		}
		else if ( e.getSource() == this.submitAnswerButton) {
			SubmitAnswerDialog d = new SubmitAnswerDialog(this, (Node)this.item);
			d.addWindowListener(this);
			d.setVisible(true);
			
			TraceService.log(TraceService.EVENT_ANSWER_CREATE_OPEN);
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {		
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {

		// the dialog is closing
		if ( e.getSource() instanceof SelectPatternDialog ) {
			this.getPatternName();
		}
		else if ( e.getSource() instanceof SubmitAnswerDialog) {
			this.answersPanel = this.loadAnswers(this.item);
			this.pack();
		}
		
	}
	
	private void getPatternName() {
		Node n = GraphMLDataReader.getPatternForNode( this.graph, this.graph.getNode(this.item.getRow()));
		
		this.patternText.setText(n == null? "-none-" : n.getString("title"));
	}
}
