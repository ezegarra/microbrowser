package microbrowser.render;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import microbrowser.VisualDBConstants;
import prefuse.data.Node;
import prefuse.util.ui.UILib;

public class AnswersListRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList list,
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Node a = (Node)value;
		JPanel panel = new JPanel(new BorderLayout());
		boolean acceptedanswer = a.getInt("type") == VisualDBConstants.NODE_TYPE_ANSWER_ACCEPTED;
		
		final int MAX_BODY_LENGTH = 300;
		
		JLabel bodyLabel = new JLabel("<html>" + a.getString("body").substring(0, a.getString("body").length() > MAX_BODY_LENGTH? MAX_BODY_LENGTH: a.getString("body").length()) + "</html>");
		bodyLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		bodyLabel.setAutoscrolls(true);
		bodyLabel.setVerticalAlignment(JLabel.TOP);
		
		panel.add(UILib.getBox(new Component[]{bodyLabel}, false, 5, 5));
		panel.setOpaque(true);
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0), BorderFactory.createLineBorder(isSelected?Color.RED: Color.GRAY, acceptedanswer? 3: 1)));
		panel.setPreferredSize(new Dimension(200, 80));
		panel.setMaximumSize(panel.getPreferredSize());
		return panel;
	}
}
