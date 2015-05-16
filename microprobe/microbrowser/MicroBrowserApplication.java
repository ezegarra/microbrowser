package microbrowser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import microbrowser.ui.AskQuestionDialog;
import microbrowser.ui.CreatePatternDialog;
import microbrowser.ui.DiscussionDetailsPane;
import microbrowser.ui.DiscussionOverviewByPatternPane;
import microbrowser.ui.DiscussionOverviewBySimilarityPane;
import microbrowser.ui.DiscussionOverviewPane;
import microbrowser.ui.LeaderBoardDialog;
import microbrowser.ui.LegendPanel;
import microbrowser.ui.PatternsDetailsPane;
import microbrowser.util.TraceService;
import prefuse.data.Node;
import prefuse.util.ui.UILib;

public class MicroBrowserApplication extends JPanel {
	private static final long serialVersionUID = -2310029621999732548L;

	private static Logger logger = Logger.getLogger(MicroBrowserApplication.class.getName());
	private static MicroBrowserApplication _instance = null;
	private static JFrame _frame = null;
	
	public JMenuBar menubar = new JMenuBar();
	public JTabbedPane tabbedPane = new JTabbedPane();
	public DiscussionOverviewPane discussionOverviewPane;	
	
	public MicroBrowserApplication() {
		super();
		_instance = this;
		Logger.getLogger("prefuse").setLevel(VisualDBConfig.LOGGING_LEVEL_PREFUSE);
		Logger.getLogger("visualdb").setLevel(VisualDBConfig.LOGGING_LEVEL_MICROPROBE);
	}
	
	public static void main(String[] argv)
	{
		// Prompt for options
		// 1 - prompt for experiment mode
		Object[] possibilities = {
				VisualDBConstants.EXPERIMENT_MODE_PATTERN_LEADERBOARD,
				VisualDBConstants.EXPERIMENT_MODE_PATTERN_ONLY,
				VisualDBConstants.EXPERIMENT_MODE_LEADERBOARD_ONLY,
				VisualDBConstants.EXPERIMENT_MODE_NO_CONDITION};
		
		// initialize the experiment mode
//		VisualDBConfig.EXPERIMENT_MODE = (String)JOptionPane.showInputDialog(
//		                    null,
//		                    "Select the experiment mode:",
//		                    "Experiment Mode Selection",
//		                    JOptionPane.PLAIN_MESSAGE,
//		                    null,
//		                    possibilities,
//		                    "ham");
		
		_frame = new JFrame("MicroBrowser - A visual browser of discussion threads");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final MicroBrowserApplication panel = createVisualization();
		//frame.setSize(new Dimension(VisualDBConfig.WINDOW_SIZE_WIDTH, VisualDBConfig.WINDOW_SIZE_HEIGHT));
		_frame.setContentPane(panel);
		_frame.setJMenuBar(panel.menubar);
		_frame.setResizable(true);
		_frame.addComponentListener(new ComponentAdapter() {
			  public void componentResized(java.awt.event.ComponentEvent evt){
				  JFrame f = (JFrame)evt.getSource();
				  logger.info("Window size=" + f.getSize());
				  VisualDBConfig.DISPLAY_SIZE_WIDTH = f.getWidth() > 480 ? f.getWidth() - 480 : 200;
				  VisualDBConfig.DISPLAY_SIZE_HEIGHT = f.getHeight() > 400 ? f.getHeight() - 175 : 50;
//				  VisualDBConfig.DISPLAY_SIZE_WIDTH = f.getWidth() > 480 ? f.getWidth() - 500 : 600;
//				  VisualDBConfig.DISPLAY_SIZE_HEIGHT = f.getHeight() > 400 ? f.getHeight() - 300 : 400;
			  }
		});

		_frame.pack();
		_frame.setVisible(true);
		_frame.setLocationRelativeTo(null);
		_frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
	}

	public void setupUI() {
						
		this.setLayout(new BorderLayout());
		
		// create center panel
		if ( VisualDBConfig.DISPLAY_MODE.equals(VisualDBConstants.DISPLAY_MODE_PATTERN)) {
			this.discussionOverviewPane =   DiscussionOverviewByPatternPane.demo(this);
		} else {
			this.discussionOverviewPane =   DiscussionOverviewBySimilarityPane.demo(this);			
		}
		
		tabbedPane.addTab("Overview", this.discussionOverviewPane);
		
		// create main window layout
		this.add(BorderLayout.NORTH, 	UILib.getBox(new Component[] { new LegendPanel()}, true, 0, 10, 25));
		this.add(BorderLayout.CENTER,	tabbedPane);	// place the visualization in the center
		
		JMenu fileMenu 		= new JMenu("File");
		JMenu createMenu 	= new JMenu("Create");
		JMenu viewMenu 		= new JMenu("View");
		
		JMenuItem fileMenu_exit = new JMenuItem("Exit");
		fileMenu_exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		fileMenu.add(fileMenu_exit);
		
		
		JMenuItem askQuestionActionButton = new JMenuItem("New Question");
		createMenu.add(askQuestionActionButton);
		
		final AskQuestionDialog askQuestionDialog  = new AskQuestionDialog((JFrame) this.getParent(), discussionOverviewPane.m_vis, discussionOverviewPane.m_graph);
		askQuestionDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDeactivated(WindowEvent e) {
				TraceService.log(TraceService.EVENT_QUESTION_CREATE_CLOSE);
			}
		});
		askQuestionActionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				askQuestionDialog.reset();
				askQuestionDialog.setVisible(true);
				TraceService.log(TraceService.EVENT_QUESTION_CREATE_OPEN);
				
				// reload the graph by reopening the panel
				//openDiscussionOverview();
				discussionOverviewPane.m_vis.run("filter");
				discussionOverviewPane.m_vis.run("updateList");
			}			
		});
		
		JMenuItem createPatternActionButton	= new JMenuItem("New Pattern");
		final CreatePatternDialog createPatternDialog = new CreatePatternDialog((JFrame) this.getParent(), discussionOverviewPane.m_vis, discussionOverviewPane.m_graph);
		createPatternDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDeactivated(WindowEvent e) {
				TraceService.log(TraceService.EVENT_PATTERN_CREATE_CLOSE);
			}
		});
		createPatternActionButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				createPatternDialog.reset();
				createPatternDialog.setVisible(true);
				TraceService.log(TraceService.EVENT_PATTERN_CREATE_OPEN);
				
				// reload the graph by reopening the panel
				openDiscussionOverview();	
			}
		});

		JMenuItem viewLeaderboardActionButton = new JMenuItem("Leaderboard");
		final LeaderBoardDialog leaderBoardDialog = new LeaderBoardDialog((JFrame) this.getParent());
		leaderBoardDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDeactivated(WindowEvent e) {
				TraceService.log(TraceService.EVENT_LEADERBOARD_CLOSE);
			}
		});
		viewLeaderboardActionButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				leaderBoardDialog.updateRankings(/*userid*/ 893);
				leaderBoardDialog.setVisible(true);
				TraceService.log(TraceService.EVENT_LEADERBOARD_OPEN);
			}
		});
		
		JMenuItem viewRadialLayoutActionButton = new JMenuItem("Discussions by pattern");
		
		viewRadialLayoutActionButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info("Display discussions by pattern");
				
				VisualDBConfig.DISPLAY_MODE = VisualDBConstants.DISPLAY_MODE_PATTERN;

				_instance.discussionOverviewPane = null;
				_instance.discussionOverviewPane =   DiscussionOverviewByPatternPane.demo(_instance);
				tabbedPane.setComponentAt(0, _instance.discussionOverviewPane);
				_frame.pack();
			}
		});
		
		JMenuItem viewSimilarityLayoutActionButton = new JMenuItem("Discussions by similarity");
		

		viewSimilarityLayoutActionButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info("Display discussions by similarity");

				VisualDBConfig.DISPLAY_MODE = VisualDBConstants.DISPLAY_MODE_SIMILARITY;

				_instance.discussionOverviewPane = null;
				_instance.discussionOverviewPane =   DiscussionOverviewBySimilarityPane.demo(_instance);
				tabbedPane.setComponentAt(0, _instance.discussionOverviewPane);
				_frame.pack();
			}
		});
		
		// build the menubar
		menubar.add(fileMenu);
		menubar.add(createMenu);
		
		if ( VisualDBConfig.EXPERIMENT_MODE == VisualDBConstants.EXPERIMENT_MODE_LEADERBOARD_ONLY || 
				VisualDBConfig.EXPERIMENT_MODE == VisualDBConstants.EXPERIMENT_MODE_PATTERN_LEADERBOARD) {			
			viewMenu.add(viewRadialLayoutActionButton);
			viewMenu.add(viewSimilarityLayoutActionButton);
			viewMenu.add(viewLeaderboardActionButton);
			menubar.add(viewMenu);
		}
				
		if ( VisualDBConfig.EXPERIMENT_MODE == VisualDBConstants.EXPERIMENT_MODE_PATTERN_ONLY || 
				VisualDBConfig.EXPERIMENT_MODE == VisualDBConstants.EXPERIMENT_MODE_PATTERN_LEADERBOARD) {
			createMenu.add(createPatternActionButton);
		}
	
		return;
	}

	public void openDiscussionDetails(Node item) {
		if ( tabbedPane.getTabCount() > 1 ) {
			tabbedPane.remove(1);
		}
		
		int type = item.getInt("type");
		if ( VisualDBConstants.NODE_TYPE_PATTERN == type ) {
			tabbedPane.addTab(item.getString("title"), PatternsDetailsPane.demo(this,item.getInt("id")));
		} else {			
			tabbedPane.addTab(item.getString("title"), DiscussionDetailsPane.demo(this,item.getInt("id")));
		}
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
		
		
		String event = "";
		switch ( type ) {
			case VisualDBConstants.NODE_TYPE_DISCUSSION:
				event = TraceService.EVENT_DISCUSSION_OPEN;
				break;
			case VisualDBConstants.NODE_TYPE_PATTERN:
				event = TraceService.EVENT_PATTERN_OPEN;
				break;
		}

		TraceService.log(event, item.getString("id"));
		
		return;
	}
	
	public void openDiscussionOverview() {
		openDiscussionOverview(true);
	}
	
	public void openDiscussionOverview(boolean showOverview) {
		tabbedPane.remove(0);
		
		if ( VisualDBConfig.DISPLAY_MODE.equals(VisualDBConstants.DISPLAY_MODE_PATTERN)) {
			this.discussionOverviewPane = DiscussionOverviewByPatternPane.demo(this);			
		} else {
			this.discussionOverviewPane = DiscussionOverviewBySimilarityPane.demo(this);
		}
		tabbedPane.insertTab("Overview", null, this.discussionOverviewPane , "", 0);
		if ( showOverview ) {
			tabbedPane.setSelectedIndex(0);			
		}
	}
	/**********************************************
	 * Initialize the demo so it can be tested
	 * from stand alone or embedded in Applet
	 * 
	 * @return VisualDBPanel object with visualization information
	 */
	public static MicroBrowserApplication createVisualization() {

		MicroBrowserApplication visualDBPanel = new MicroBrowserApplication();
		visualDBPanel.setupUI();

		return visualDBPanel;
	}

	public void run() {
//		this.setUpData();
//		this.setUpVisualization();
//		this.setUpRenderers();
//		this.setUpActions();
//		this.setUpDisplay();

		// launch the visualization -------------------------------------

		// The following is standard java.awt.
		// A JFrame is the basic window element in awt. 
		// It has a menu (minimize, maximize, close) and can hold
		// other gui elements. 

		// Create a new window to hold the visualization.  
		// We pass the text value to be displayed in the menubar to the constructor.
		JFrame frame = new JFrame("prefuse example");
		// Prepares the window.
		frame.pack();           

		// Shows the window.
		frame.setVisible(true); 
		
		// We have to start the ActionLists that we added to the visualization
//		this.m_vis.run("draw");
	}
}
