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
    private double t_max = 30;
    private final int approxGridLinesH = 4;
    private final int approxGridLinesV = 6;
    private boolean zoomed = false, clicked = false;
    private final JLabel name;
    private JMenu resize;
    private Point start, end;
    private final int[] t_steps = new int[]{5, 10, 15, 20, 30, 45, 60, 120};

    public GraphDisplay(String name) {
        this(name, 0, 0, Logger.getTypes(), null);
    }

    public GraphDisplay(TextDisplay t) {
        this(t.getName(), t.getX(), t.getY(), t.getTypes(), t.getStored());
    }

    public GraphDisplay(String name, int x, int y) {
        this(name, x, y, Logger.getTypes(), null);
    }

    public GraphDisplay(String name, int x, int y, ArrayList<Logger.DisplayType> types, ArrayList<Pair> stored) {
        super(name, x, y, types, stored);

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

        load();
    }

    @Override
    public void loadPopup() {
        super.loadPopup();

        if (zoomed) {
            JMenuItem zoom = new JMenuItem("Zoom Out");
            zoom.addActionListener(a -> zoomOut());
            popup.add(zoom, 0);
        } else {
            resize = new JMenu("Set Max Range");
            for (int d : t_steps) {
                JMenuItem temp = new JMenuItem(String.valueOf(d));
                temp.addActionListener(e -> t_max = d);
                resize.add(temp);
            }
            popup.add(resize, 0);
        }

        JMenuItem text = new JMenuItem("Change to Text");
        text.addActionListener(e -> ((Logger) getParent().getParent().getParent().getParent().getParent()).replace(
                this, Logger.DisplayType.kTextDisplay));
        popup.add(text, 1);
    }

    @Override
    public void loadListeners() {
        super.loadListeners();

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
                end.x = Math.max(graph.getX(), Math.min(end.x, graph.getX() + graph.getWidth()));
                end.y = Math.max(graph.getY(), Math.min(end.y, graph.getY() + graph.getHeight() - 14));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(clicked) {
                    if (!(start.x == end.x || start.y == end.y)) {
                        end = e.getPoint();
                        end.x = Math.max(graph.getX(), Math.min(end.x, graph.getX() + graph.getWidth()));
                        end.y = Math.max(graph.getY(), Math.min(end.y, graph.getY() + graph.getHeight() - 14));

                        t_left = Math.min(graph.getGraphX(start.x), graph.getGraphX(end.x));
                        t_right = Math.max(graph.getGraphX(start.x), graph.getGraphX(end.x));
                        y_right = Math.max(graph.getGraphY(start.y), graph.getGraphY(end.y));
                        y_left = Math.min(graph.getGraphY(start.y), graph.getGraphY(end.y));
                        t_cur = graph.getT_cur();

                        if(!zoomed) {
                            zoomed = true;

                            popup.remove(resize);

                            JMenuItem zoom = new JMenuItem("Zoom Out");
                            zoom.addActionListener(a -> zoomOut());
                            popup.add(zoom, 0);
                        }
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

    public void zoomOut() {
        zoomed = false;
        popup.remove(0);
        popup.add(resize, 0);
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
    public void updateValue(Long time, Object value) {
        super.updateValue(time, value);
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
        edgeDist = Math.max(4, Math.min(6, getBounds().height / 4));
    }
}
