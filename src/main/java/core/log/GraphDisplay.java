package core.log;

import app.Logger;
import core.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

public class GraphDisplay extends Display implements Serializable {

    private Graph graph;
    private final int padding = 5;
    private final int vHeight = 25;
    private final int textSize = 14;
    private final Dimension minimumSize = new Dimension(400, 300);
    private double t_left = 0, t_right = 0, y_left = 0, y_right = 0, t_cur = 0;
    private final double t_max = 120;
    private final int approxGridLinesH = 4;
    private final int approxGridLinesV = 6;
    private boolean zoomed = false, clicked = false;
    private JLabel name;
    private Point start, end;

    public GraphDisplay(String name, Logger parent) {
        this(name, 0, 0, Logger.getTypes(), parent, null);
    }

    public GraphDisplay(TextDisplay t) {
        this(t.getName(), t.getX(), t.getY(), t.getTypes(), t.getParentLogger(), t.getStored());
    }

    public GraphDisplay(String name, int x, int y, Logger parent) {
        this(name, x, y, Logger.getTypes(), parent, null);
    }

    public GraphDisplay(String name, int x, int y, ArrayList<Logger.DisplayType> types, Logger parent, ArrayList<Pair> stored) {
        super(name, x, y, types, parent, stored);

        this.setLayout(null);

        JMenuItem zoom = new JMenuItem("Zoom Out");
        zoom.addActionListener(e -> zoomed = false);
        popup.add(zoom);

        JMenuItem text = new JMenuItem("Change to Text");
        text.addActionListener(e -> parentLogger.replace(this, Logger.DisplayType.kTextDisplay));
        popup.add(text);

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

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (getCursor(e) == Cursor.CROSSHAIR_CURSOR) {
                        clicked = true;
                        start = e.getPoint();
                        end = e.getPoint();
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                end = e.getPoint();
                requestFocus();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    if (getCursor(e) == Cursor.CROSSHAIR_CURSOR && !(start.x == end.x || start.y == end.y)) {
                        end = e.getPoint();

                        t_left = Math.min(graph.getGraphX(start.x), graph.getGraphX(end.x));
                        t_right = Math.max(graph.getGraphX(start.x), graph.getGraphX(end.x));
                        y_right = Math.max(graph.getGraphY(start.y), graph.getGraphY(end.y));
                        y_left = Math.min(graph.getGraphY(start.y), graph.getGraphY(end.y));
                        t_cur = graph.getT_cur();

                        zoomed = true;
                    }
                    clicked = false;
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    @Override
    public void repaint() {
        if(graph != null) {
            graph.setBounds(new Rectangle(padding, padding + vHeight, getWidth() - 2 * padding, getHeight() - vHeight - 2 * padding));
            graph.repaint();
            name.setBounds(new Rectangle(padding, padding, getWidth() - 2 * padding, vHeight - padding));
        }
    }

    public boolean getClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public double getT_left() {
        return t_left;
    }

    public double getT_right() {
        return t_right;
    }

    public double getY_left() {
        return y_left;
    }

    public double getY_right() {
        return y_right;
    }

    public boolean getZoomed() {
        return zoomed;
    }

    public double getT_cur() {
        return t_cur;
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

    public int getCursor(MouseEvent me) {
        Rectangle rect = graph.getGraphDim();
        if (rect != null && rect.contains(me.getPoint())) return Cursor.CROSSHAIR_CURSOR;
        return super.getCursor(me);
    }

    @Override
    public Dimension getMinimumSize() {
        return minimumSize;
    }

    @Override
    public void updateResizeBounds() {
        cornerDist = Math.max(6, Math.min(10, getBounds().height / 3));
        edgeDist = Math.max(4, Math.min(8, getBounds().height / 4));
    }
}
