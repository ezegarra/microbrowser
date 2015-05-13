package microbrowser.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import microbrowser.data.io.VDBConnector;

public class LeaderBoardDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JLabel row1Label,row2Label,row3Label,row4Label;
	
	public LeaderBoardDialog(JFrame owner) {
		super(owner, "Leaderboard", true);
		
		this.buildUI();
		
		// set dialog dimensions
		this.setPreferredSize(new Dimension(300, 300));
		this.setMinimumSize(this.getPreferredSize());
		this.setLocationRelativeTo(null);
	}
	
	private void buildUI() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel content = new JPanel();
		content.setPreferredSize(new Dimension(325, 200));
		content.setMinimumSize(content.getPreferredSize());
		
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		
		row1Label = new JLabel("You need 0 points to become the leader.");
		row1Label.setBorder(BorderFactory.createEmptyBorder(5, 2, 10, 2));
		
		row2Label = new JLabel("You need 0 points to move up by one position.");
		row2Label.setBorder(BorderFactory.createEmptyBorder(5, 2, 10, 2));
		
		row3Label = new JLabel("0   0      0 points");
		row3Label.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 2));
		
		row4Label = new JLabel("There are 0 users with less points than you.");
		row4Label.setBorder(BorderFactory.createEmptyBorder(5, 2, 10, 2));
		
		content.add(row1Label);
		content.add(row2Label);
		content.add(row3Label);
		content.add(row4Label);
		
		content.setAlignmentX(CENTER_ALIGNMENT);
	
		JPanel buttonPanel = new JPanel();
		JButton okButton = new JButton("Close");
		okButton.addActionListener(this);
		
		buttonPanel.add(okButton);
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		this.getContentPane().add(content);
		this.getContentPane().add(buttonPanel);
		
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// close the dialog
		this.dispose();
	}
	
	private String[] getRankingInfo(int userid) {
		String[] ranking = new String[6];
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String 	sql1 = "SELECT COUNT(*)+1 as rank from VISUAL.USERS where REPUTATION > (select REPUTATION from VISUAL.USERS where ID = ?)",
				sql2 = "SELECT * FROM VISUAL.USERS WHERE ID = ?",
				sql3 = "SELECT COUNT(*) + 1 AS CNT FROM VISUAL.USERS WHERE REPUTATION < (SELECT REPUTATION FROM VISUAL.USERS WHERE ID = ?)",
				sql4 = "SELECT MAX(REPUTATION) AS MAX_REPUTATION FROM VISUAL.USERS",
				sql5 = "SELECT * FROM (SELECT ROW_NUMBER() OVER () AS rownumber, T.* FROM (SELECT * FROM VISUAL.USERS ORDER BY REPUTATION DESC) T) AS U WHERE rownumber = ?";
		try {
			conn = VDBConnector.getConnection();

			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, userid);
			rs = pstmt.executeQuery();
			rs.next();
			ranking[0] = rs.getString("rank");
			
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1, userid);
			rs = pstmt.executeQuery();
			rs.next();
			ranking[1] = rs.getString("DISPLAYNAME");
			ranking[2] = String.valueOf(rs.getInt("REPUTATION"));
			
			pstmt = conn.prepareStatement(sql3);
			pstmt.setInt(1, userid);
			rs = pstmt.executeQuery();
			rs.next();
			ranking[3] = rs.getString("CNT");
			
			pstmt = conn.prepareStatement(sql4);
			rs = pstmt.executeQuery();
			rs.next();
			ranking[4] = String.valueOf( Integer.valueOf(rs.getString("MAX_REPUTATION")).intValue() - Integer.valueOf(ranking[2]).intValue() );
			
			if ( Integer.valueOf(ranking[0]).intValue() == 1 ) {
				ranking[5] = "0";
			} else {
				pstmt = conn.prepareStatement(sql5);
				pstmt.setInt(1, Integer.valueOf(ranking[0]).intValue() - 1);
				rs = pstmt.executeQuery();
				rs.next();
				ranking[5] = String.valueOf( rs.getInt("REPUTATION") - Integer.parseInt(ranking[2]) );				
			}
			
		} catch( Exception e) {
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
		return ranking;
		
	}

	public void updateRankings(int userid) {
		String[] ranking = getRankingInfo(userid);
		
		row1Label.setText("You need " + ranking[4] + " points to become the leader.");
		row2Label.setText("You need " + ranking[5] + " points to move up by one position.");
		row3Label.setText(ranking[0] + "   " + ranking[1] + "      " + ranking[2] + " points");
		row4Label.setText("There are " + ranking[3] + " users with less points than you.");
		
	}
}
