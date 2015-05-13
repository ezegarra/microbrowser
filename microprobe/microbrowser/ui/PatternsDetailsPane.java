package microbrowser.ui;

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

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import microbrowser.VisualDBApplication;
import microbrowser.data.io.GraphMLDataReader;
import microbrowser.render.AnswersListRenderer;
import microbrowser.render.VisualDBListRenderer;
import microbrowser.util.TraceService;
import prefuse.Visualization;
import prefuse.data.Node;
import prefuse.util.ui.JFastLabel;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualItem;

public class PatternsDetailsPane extends DiscussionDetailsPane {	
	private static final long serialVersionUID = 1L;
	
	private JEditorPane patternDescription = new JEditorPane("text/html", "");
	private JEditorPane patternSolution = new JEditorPane("text/html", "");
	
	public PatternsDetailsPane(VisualDBApplication parent, int threadid) {
		super(parent, threadid);		
	}

	public static JPanel demo(VisualDBApplication parent, int threadid) {
		PatternsDetailsPane pane = new PatternsDetailsPane(parent, threadid);

		return pane;
	}
	
	protected Component buildSouthPanel() {
		// set question text attributes
		patternDescription = new JEditorPane("text/html", "");
		patternDescription.setText(theNode.getString("body"));
		patternDescription.setEditable(false);
		patternDescription.setBackground(Color.white);
		patternDescription.setOpaque(true);
		patternDescription.setAlignmentY(TOP_ALIGNMENT);

		// Put the question pane in a scroll pane.
		final JScrollPane patternProblemScrollPane = new JScrollPane(patternDescription);
		patternProblemScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		patternProblemScrollPane.setPreferredSize(new Dimension(250, 200));
		patternProblemScrollPane
				.setMinimumSize(patternProblemScrollPane.getPreferredSize());

		// Put the pattern solution text in a scroll pane
		patternSolution = new JEditorPane("text/html", "");
		patternSolution.setText(theNode.getString("solution"));
		final JScrollPane patternSolutionScrollPane = new JScrollPane(patternSolution);
		patternSolutionScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		patternSolutionScrollPane.setPreferredSize(new Dimension(250, 145));
		patternSolutionScrollPane.setMinimumSize(new Dimension(10, 10));

		// create the tabbed pane
		tabbedPane.add("Description", patternProblemScrollPane);
		tabbedPane.add("Solution", patternSolutionScrollPane);

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				patternProblemScrollPane.getVerticalScrollBar().setValue(0);
				patternSolutionScrollPane.getVerticalScrollBar().setValue(0);
			}
		});

		// define the action buttons
		JFastLabel title = new JFastLabel("Asked by " + theNode.getString("owner") );
		title.setPreferredSize(new Dimension(100, 25));
		title.setMinimumSize(title.getPreferredSize());
		UILib.setFont(title, new Font(Font.MONOSPACED, Font.BOLD, 14));
		Node patternNode = GraphMLDataReader.getPatternForNode(GraphMLDataReader.getSourceGraph(), theNode);
		String t = patternNode == null? "No pattern selected" : patternNode.getString("title");
		JFastLabel pattern = new JFastLabel( t );
		pattern.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));

		JButton changePattern = new JButton("Change Pattern");
		changePattern.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				SelectPatternDialog d = new SelectPatternDialog(null, theNode);
				d.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						// reload the graph
						parent.openDiscussionDetails(theNode);
						
						parent.openDiscussionOverview(false);
					}
				});
				d.setVisible(true);

				TraceService.log(TraceService.EVENT_PATTERN_CHANGE_OPEN);
			}
		});
		Box infoPanel = UILib.getBox(new Component[] { title, pattern, changePattern }, true, 10, 0);
		
		UILib.addStrut(infoPanel, true, 200);
		
		Box southPanel = UILib.getBox(
				new Component[] { infoPanel, tabbedPane }, false, 0, 0);
		return southPanel;
	}

//	private Component buildEastPanel() {
//		JList listMenu = new JList(listMenuModel);
//		listMenu.setCellRenderer(new VisualDBListRenderer());
//		listMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		listMenu.setVisibleRowCount(-1);
//
//		listMenu.addMouseMotionListener(new MouseMotionAdapter() {
//			@Override
//			public void mouseMoved(MouseEvent e) {
//				// clear selection
//				listMenu.clearSelection();
//				
//				int index = listMenu.locationToIndex(e.getPoint());
//				if ( index > -1) {
//					listMenu.setSelectedIndex(index);
//					VisualItem item = (VisualItem) listMenu.getSelectedValue();
//					m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).setTuple(item);
//					m_vis.run("repaint");
//				}
//			}
//		});
//		
//		listMenu.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseExited(MouseEvent e) {
//				// clear selection
//				listMenu.clearSelection();
//		        m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).clear();	
//				m_vis.run("repaint");
//				TraceService.log(TraceService.EVENT_LIST_MOUSEEXITED, "type=detailRelatedList" );
//			}
//
//			@Override
//			public void mouseEntered(MouseEvent e) {
//				// clear selection
//				listMenu.clearSelection();
//
//				int index = listMenu.locationToIndex(e.getPoint());
//				
//				if ( index > -1 ) {
//					listMenu.setSelectedIndex(index);
//					VisualItem item = (VisualItem) listMenu.getSelectedValue();
//					m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).setTuple(item);		
//					m_vis.run("repaint");
//
//					TraceService.log(TraceService.EVENT_LIST_MOUSEENTERED, "type=detailRelatedList, item=" + item.getString("id"));
//
//				}
//			}
//			
//			public void mouseClicked(MouseEvent evt) {
//				
//				int index = listMenu.locationToIndex(evt.getPoint());
//				VisualItem item = (VisualItem) listMenu.getModel().getElementAt(index);
//
//				TraceService.log(TraceService.EVENT_LIST_ITEMCLICKED, "type=detailRelatedList, item=" + item.getString("id"));
//				
//				if ( evt.getClickCount() == 2 ) {
//					// open thread details
//					parent.openDiscussionDetails((Node)item);
//				}
//				
//			}			
//		});
//		
//		JScrollPane listScrollPane = new JScrollPane(listMenu);
//		listScrollPane.setMinimumSize(new Dimension(450, 470));
//		listScrollPane.setPreferredSize(listScrollPane.getMinimumSize());
//
//		// define the answers list
//		JList answersList = new JList(answersListMenuModel);
//		answersList.setCellRenderer(new AnswersListRenderer());
//		answersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		answersList.setVisibleRowCount(-1);
//		JScrollPane answersListScrollPane = new JScrollPane(answersList);
//		answersListScrollPane.setMinimumSize(new Dimension(450, 470));
//		answersListScrollPane.setPreferredSize(answersListScrollPane.getMinimumSize());
//		ListSelectionListener sl = new ListSelectionListener() {
//			public void valueChanged(ListSelectionEvent e) {
//				if (e.getValueIsAdjusting() == false) {
//
//					VisualItem item = (VisualItem)answersList.getSelectedValue();
//
//					// clear the list selections
//					m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).clear();	
//					m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).setTuple(item);
//					m_vis.run("repaint");
//
//					// open the details of the selected answer
//					showSelectedAnswer(item);
//				}
//			}
//		};
//		answersList.addListSelectionListener(sl);
//		answersList.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseEntered(MouseEvent e) {
//				TraceService.log(TraceService.EVENT_LIST_MOUSEENTERED, "type=detailAnswersList");
//			}
//			@Override
//			public void mouseExited(MouseEvent e) {
//				TraceService.log(TraceService.EVENT_LIST_MOUSEEXITED, "type=detailAnswersList");
//			}
//			
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				int index = listMenu.locationToIndex(e.getPoint());
//				VisualItem item = (VisualItem) listMenu.getModel().getElementAt(index);
//				
//				TraceService.log(TraceService.EVENT_LIST_ITEMCLICKED, "type=detailAnswersList, item=" + item.getString("id"));			}
//		});
//		
//		// create the east panel tab
//		eastPanelTabPane = new JTabbedPane();
//
//		eastPanelTabPane.addTab("Answers", answersListScrollPane);			
//		eastPanelTabPane.addTab("Related", listScrollPane);
//
//		return eastPanelTabPane;
//	}
}
