package microbrowser.render;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import microbrowser.VisualDBConstants;
import microbrowser.util.DateLib;

import org.jsoup.Jsoup;

import prefuse.data.Node;
import prefuse.util.ColorLib;
import prefuse.util.ui.UILib;

public class VisualDBListRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	private HashMap<Integer, JPanel> items = new HashMap<Integer, JPanel>();

	@Override
	public Component getListCellRendererComponent(JList list,
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Node item = (Node) value;
		
		if ( item != null && item.isValid()) {
			JPanel p = new JPanel(new BorderLayout());
			Integer key = new Integer(item.getInt("id"));
			if ( items.containsKey(key)) {
				p = items.get(key);
				if ( isSelected ) {
					p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0), BorderFactory.createLineBorder(Color.RED, 3)));
				} else {
					p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0), BorderFactory.createLineBorder(Color.BLACK, 1)));			
				}
				return p;
			}
			int bodylimit = 200;

			String body = "";

			body = item.getString("body");					
			
			p.setOpaque(true);
			p.setBackground(Color.WHITE);
			if ( cellHasFocus) {
				p.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
			} else {
				p.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));			
			}
			
			String displaybody;
			
			if ( body.length() > bodylimit) {
				displaybody = body.substring(0, bodylimit) + " ...";
			} else {
				displaybody = body;
			}
			
			//JLabel bodylabel = new JLabel("<html>" + displaybody + "</html>");
			//JLabel bodylabel = new JLabel(Jsoup.parse(displaybody).text());
			JTextArea bodylabel = new JTextArea(Jsoup.parse(displaybody).text());
			bodylabel.setWrapStyleWord(true);
			//JXLabel bodylabel = new JXLabel();
			//bodylabel.setText(Jsoup.parse(displaybody).text());
			//bodylabel.setOpaque(true);
			bodylabel.setLineWrap(true);
			//bodylabel.setMinimumSize(new Dimension(50, 200));
			//bodylabel.setMaximumSize(bodylabel.getMinimumSize());
			bodylabel.setFont(new Font("Serif", Font.PLAIN, 12));
			bodylabel.setAutoscrolls(true);
//			bodylabel.setVerticalAlignment(JLabel.TOP);
			
			JLabel ownerLabel = new JLabel(item.getString("owner"));
			JLabel answeredLabel = new JLabel((0 == item.getInt("acceptedanswerid") ? "Unanswered": "Answered"));
			JLabel answerLabel = new JLabel(item.getInt("answercount") + " answer(s)");
			answeredLabel.setFont(new Font("Serif", Font.PLAIN, 9));
			answerLabel.setFont(new Font("Serif", Font.PLAIN, 9));
//			JLabel lastUpdatedLabel = new JLabel("<html><b>Last activity date:</b> " + DateLib.formatDate(item.getLong("lastactivitydate")) + " <b>Tags:</b> " +  item.getString("tags") + " <b>ID</b>: " + item.getInt("id") + "</html>");
			JLabel lastUpdatedLabel = new JLabel("Last activity date: " + DateLib.formatDate(item.getLong("lastactivitydate")));
			JLabel lastUpdatedLabel_tags = new JLabel(" Tags: " +  item.getString("tags"));
			JLabel lastUpdatedLabel_id = new JLabel(" ID: " + item.getInt("id"));
			lastUpdatedLabel.setFont(new Font("Serif", Font.PLAIN, 10));
			lastUpdatedLabel_tags.setFont(new Font("Serif", Font.PLAIN, 10));
			lastUpdatedLabel_id.setFont(new Font("Serif", Font.PLAIN, 10));
			lastUpdatedLabel.setHorizontalAlignment(JLabel.LEFT);
			
			JLabel titleLabel = new JLabel(item.getString("title"));
			titleLabel.setOpaque(true);
			if ( VisualDBConstants.NODE_TYPE_PATTERN == item.getInt("type") ) {
				titleLabel.setBackground(Color.GREEN.darker());
			} else {
				titleLabel.setBackground(ColorLib.getColor(43,140,190));
			}
			titleLabel.setForeground(Color.WHITE);
			titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
			p.add(titleLabel, BorderLayout.NORTH);
			if ( VisualDBConstants.NODE_TYPE_DISCUSSION == item.getInt("type")) {
				p.add(UILib.getBox(new Component[]{bodylabel}, true, 0, 0), BorderLayout.CENTER);
				p.add(UILib.getBox(new Component[]{
					UILib.getBox(new Component[]{ownerLabel,answeredLabel, answerLabel}, true, 0, 5),
					UILib.getBox(new Component[]{lastUpdatedLabel, lastUpdatedLabel_tags, lastUpdatedLabel_id}, true, 0, 5)
					}, false, 5, 5), BorderLayout.SOUTH);
			}
			else if ( VisualDBConstants.NODE_TYPE_PATTERN == item.getInt("type")) {
				p.add(bodylabel, BorderLayout.CENTER);
			}

			p.setPreferredSize(new Dimension(250, 100));
			p.setMinimumSize(p.getPreferredSize());
			p.setMaximumSize(p.getPreferredSize());
			
			if ( isSelected ) {
				p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0), BorderFactory.createLineBorder(Color.RED, 3)));
			} else {
				p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0), BorderFactory.createLineBorder(Color.BLACK, 1)));			
			}
			
			items.put(key, p);
			return p;
		}
		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	}

}
