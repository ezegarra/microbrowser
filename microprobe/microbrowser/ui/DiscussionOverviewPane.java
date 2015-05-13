package microbrowser.ui;

import javax.swing.JList;
import javax.swing.JPanel;

import microbrowser.VisualDBApplication;
import prefuse.Visualization;
import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.util.ui.JPrefuseTable;

public abstract class DiscussionOverviewPane extends JPanel {

	private static final long serialVersionUID = 1L;
	public VisualDBApplication parent;
	
	public Visualization m_vis = null;
	public Graph m_graph = null;

	public JList listMenu;
    public JPrefuseTable m_prefuseTable;
    private Table m_table;
    
	public DiscussionOverviewPane(VisualDBApplication parent) {
		this.parent = parent;
		
		m_table = new Table();
		m_table.addColumn("TAGS", String.class);
		m_prefuseTable = new JPrefuseTable(m_table);
		m_prefuseTable.setVisible(false);
	}
}
