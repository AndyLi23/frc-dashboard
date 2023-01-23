package util.log;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphDisplay extends Display {

    private Graph graph;
    private final int padding = 5;
    private final int vHeight = 25;
    private final int textSize = 14;
    private final Dimension minimumSize = new Dimension(400, 300);
    private double t_left = 0, t_right = 0;
    private final double t_max = 120;
    private final int approxGridLinesH = 4;
    private final int approxGridLinesV = 6;
    private boolean zoomed = false;
    private JLabel name;

    public GraphDisplay(String name, Object value) {
        this(name, value, 0, 0);
    }

    public GraphDisplay(String name, Object value, int x, int y) {
        super(name, value, x, y);

        this.setLayout(null);

        graph = new Graph();
        this.name = new JLabel(name, SwingConstants.CENTER);
        this.add(graph);
        this.add(this.name);

        this.setBounds(x, y, getPreferredSize().width, getPreferredSize().height);

        graph.setBounds(new Rectangle(padding, padding + vHeight, getWidth() - 2 * padding, getHeight() - vHeight - 2 * padding));
        this.name.setBounds(new Rectangle(padding, padding, getWidth() - 2 * padding, vHeight - 2 * padding));

        this.name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, textSize));

        setBackground(new Color(235, 235, 235));

        updateResizeBounds();
    }

    @Override
    public void repaint() {
        if(graph != null) {
            graph.setBounds(new Rectangle(padding, padding + vHeight, getWidth() - 2 * padding, getHeight() - vHeight - 2 * padding));
            graph.repaint();
            name.setBounds(new Rectangle(padding, padding, getWidth() - 2 * padding, vHeight - padding));
        }
    }

    public double getT_left() {
        return t_left;
    }

    public double getT_right() {
        return t_right;
    }

    public double getT_max() {
        return t_max;
    }

    public int getApproxGridLinesH() {
        return approxGridLinesH;
    }

    public int getApproxGridLinesV() {
        return approxGridLinesV;
    }

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    @Override
    public void updateValue(Object value) {
        super.updateValue(value);
        resize();
//        repaint();

    }

    @Override
    public Dimension getMinimumSize() {
        return minimumSize;
    }
}
