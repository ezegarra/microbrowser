package microbrowser.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import prefuse.render.ShapeRenderer;

public class LegendPanel extends JPanel {
	private static final long serialVersionUID = 1L;
    private Rectangle2D m_rect = new Rectangle2D.Double();
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    
	public LegendPanel() {
		this.setPreferredSize(new Dimension(650, 30));
		this.setMinimumSize( this.getPreferredSize());
	}
	protected void paintComponent(Graphics g ) {
		super.paintComponents(g);
		Graphics2D g2d = (Graphics2D) g;
				
		g2d.setFont(new Font("default", Font.BOLD, 13));
		g2d.drawString("Popularity:", 20, 22);
		
		g2d.setFont(new Font("default", Font.PLAIN, 12));
		m_rect.setRect(105, 10, 15, 15);
		g2d.setPaint(new Color(254, 232, 200));
	    g2d.fill(m_rect);
		g.setColor(Color.black);		
		g2d.drawString("< 5,000 views", 125, 23);
		
		g.setColor(Color.black);
		m_rect.setRect(210, 10, 15, 15);
		g2d.setPaint(new Color(253, 187, 132));
	    g2d.fill(m_rect);
		g.setColor(Color.black);		
		g2d.drawString("< 10,000 views", 230, 23);
		
		g.setColor(Color.black);
		m_rect.setRect(315, 10, 15, 15);
		g2d.setPaint(new Color(227, 74, 51));
	    g2d.fill(m_rect);
		g.setColor(Color.black);		
		g2d.drawString("> 10,000 views", 335, 23);
		
		g2d.setFont(new Font("default", Font.BOLD, 13));
		g2d.drawString("Shape type:", 425, 23);
		g2d.setFont(new Font("default", Font.PLAIN, 12));

		g2d.fill(shapeRenderer.hexagon(505, 10, 15));
		g2d.drawString("Answered", 525, 23);
		
		g2d.fill(shapeRenderer.diamond(585, 10, 15));
		g2d.drawString("Unanswered", 605, 23);
	}
}
