package util.log;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap;
import java.util.ArrayList;

public class Graph extends JPanel {

    public Graph() {
        super();

        setBackground(new Color(255, 255, 255));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3f));

        g2d.drawLine(0, 0, 100, 100);

        double t_left = ((GraphDisplay) getParent()).getT_left();
        double t_right = ((GraphDisplay) getParent()).getT_right();
        ArrayList<AbstractMap.SimpleImmutableEntry<Long, Object>> data = ((GraphDisplay) getParent()).getStored();



    }

}
