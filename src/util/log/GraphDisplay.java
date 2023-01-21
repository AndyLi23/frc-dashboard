package util.log;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphDisplay extends Display {

    private Graph graph;
    private final int padding = 5;
    private final int vHeight = 20;
    private final Dimension minimumSize = new Dimension(200, 200);
    private double t_left, t_right;

    public GraphDisplay(String name, String value) {
        this(name, value, 0, 0);
    }

    public GraphDisplay(String name, String value, int x, int y) {
        super(name, value, x, y);

        this.setLayout(null);

        graph = new Graph();
        this.add(graph);

        this.setBounds(x, y, getPreferredSize().width, getPreferredSize().height);

        graph.setBounds(new Rectangle(padding, padding + vHeight, getWidth() - 2 * padding, getHeight() - vHeight - 2 * padding));

        repaint();
    }

    @Override
    public void repaint() {
        if(graph != null) {
            graph.setBounds(new Rectangle(padding, padding + vHeight, getWidth() - 2 * padding, getHeight() - vHeight - 2 * padding));
            graph.repaint();
        }
    }

    public double getT_left() {
        return t_left;
    }

    public double getT_right() {
        return t_right;
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
