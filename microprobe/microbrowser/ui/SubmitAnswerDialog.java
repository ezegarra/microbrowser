package microbrowser.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import microbrowser.data.io.VDBConnector;
import microbrowser.util.TraceService;
import prefuse.data.Node;

public class SubmitAnswerDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private Node item;
	
	private JLabel	answerLabel;
	
	private JEditorPane answerText;
	
	private JButton submitAnswerButton,
					cancelButton;
	
	private long startTime = System.currentTimeMillis();
	
	public SubmitAnswerDialog(JDialog owner, Node item ) {
		super(owner, "Submit Answer", true);
		this.item = item;
		
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		
		answerLabel = new JLabel("Enter your answer:");
		answerText 	= new JEditorPane();
		answerText.setBorder(BorderFactory.createLoweredBevelBorder());
		answerText.setPreferredSize(new Dimension(400, 200));
		answerText.setMinimumSize(answerText.getPreferredSize());
		

		JPanel contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		
		c.gridx = 0;
		c.gridy = 0;
		contentPanel.add(answerLabel, c);
		
		c.gridy = 1;
		contentPanel.add(answerText, c);
		
		JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		submitAnswerButton	= new JButton("Submit Answer");
		submitAnswerButton.addActionListener(this);
		cancelButton		= new JButton("Cancel");
		cancelButton.addActionListener(this);
		actionButtonPanel.add(submitAnswerButton);
		actionButtonPanel.add(cancelButton);

		this.getContentPane().add(contentPanel);
		this.getContentPane().add(actionButtonPanel);
		
		
		this.setPreferredSize(new Dimension(400, 300));
		this.setMinimumSize(this.getPreferredSize());
		
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == this.cancelButton) {
			this.dispose();
			TraceService.log(TraceService.EVENT_ANSWER_CREATE_CANCEL, (System.currentTimeMillis() - startTime)+"ms");
		}
		else if ( e.getSource() == this.submitAnswerButton ) {
			this.submitAnswer();
			
			TraceService.log(TraceService.EVENT_ANSWER_CREATE, (System.currentTimeMillis() - startTime)+"ms");
			this.dispose();
		}
	}
	
	private void submitAnswer() {
		if ( !this.answerText.getText().isEmpty()) {
			
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			Random random = new Random();
			try {
				int parentId = item.getInt("id");
				String body		= this.answerText.getText();
				
				conn = VDBConnector.getConnection();
				pstmt = conn.prepareStatement("INSERT INTO VISUAL.THREADS (ID, POSTTYPEID, PARENTID, BODY, CREATIONDATE, OWNERUSERID, OWNERDISPLAYNAME, SCORE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				pstmt.setInt(1, random.nextInt());
				pstmt.setInt(2, 2);
				pstmt.setInt(3, parentId);
				pstmt.setString(4, body);
				pstmt.setString(5, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis())));
				pstmt.setInt(6, 15452);
				pstmt.setString(7, "Vihung");
				pstmt.setInt(8, 0);
				pstmt.executeUpdate();
				
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if ( pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if ( conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
		}
	}
}
