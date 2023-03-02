package core.log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

public class MovablePanel extends JPanel implements Serializable {
    public final int[] locations = {
            SwingConstants.NORTH, SwingConstants.SOUTH, SwingConstants.WEST,
            SwingConstants.EAST, SwingConstants.NORTH_WEST,
            SwingConstants.NORTH_EAST, SwingConstants.SOUTH_WEST,
            SwingConstants.SOUTH_EAST
    };

    public final int[] cursors = {
            Cursor.N_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.W_RESIZE_CURSOR,
            Cursor.E_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
            Cursor.SW_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR
    };

    protected int x, y;
    private final MovablePanel self;
    protected int cornerDist, edgeDist;
    protected boolean rMenu = false;

    protected final JPopupMenu popup;

    public MovablePanel(int x, int y) {
        this.self = this;
        this.x = x;
        this.y = y;

        updateResizeBounds();

        popup = new JPopupMenu();
        JMenuItem remove = new JMenuItem("Remove");
        remove.addActionListener(e -> getParent().getParent().getParent().getParent().remove(this));
        popup.add(remove);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            private Point lastPoint;
            private Rectangle stored, bounds;
            private int cursor, offsetX, offsetY, windowWidth, windowHeight;

            @Override
            public void mousePressed(MouseEvent e) {
                moveToFront();

                if (e.getButton() == MouseEvent.BUTTON1) {
                    cursor = getCursor(e);
                    if (cursor != Cursor.CROSSHAIR_CURSOR) {
                        lastPoint = e.getLocationOnScreen();
                        stored = getBounds();
                        cursor = getCursor(e);
                    }
                }

                if (e.getButton() == MouseEvent.BUTTON3) {
                    initializeRightClick(e);
                } else {
                    cancelRightClick();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(getCursor(e)));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if(cursor != Cursor.CROSSHAIR_CURSOR && !rMenu) {
                    Point point = e.getLocationOnScreen();
                    offsetX = point.x - lastPoint.x;
                    offsetY = point.y - lastPoint.y;

                    windowWidth = getParent().getWidth();
                    windowHeight = getParent().getHeight();

                    bounds = getBounds();

                    updateResizeBounds();

                    switch (cursor) {
                        case Cursor.MOVE_CURSOR: {
                            stored.x += offsetX;
                            stored.y += offsetY;
                            bounds.x = Math.max(0, Math.min(stored.x, windowWidth - bounds.width));
                            bounds.y = Math.max(0, Math.min(stored.y, windowHeight - bounds.height));
                            break;
                        }
                        case Cursor.SE_RESIZE_CURSOR: {
                            southDragged();
                            eastDragged();
                            break;
                        }
                        case Cursor.SW_RESIZE_CURSOR: {
                            southDragged();
                            westDragged();
                            break;
                        }
                        case Cursor.NW_RESIZE_CURSOR: {
                            northDragged();
                            westDragged();
                            break;
                        }
                        case Cursor.NE_RESIZE_CURSOR: {
                            northDragged();
                            eastDragged();
                            break;
                        }
                        case Cursor.N_RESIZE_CURSOR: northDragged(); break;
                        case Cursor.S_RESIZE_CURSOR: southDragged(); break;
                        case Cursor.W_RESIZE_CURSOR: westDragged(); break;
                        case Cursor.E_RESIZE_CURSOR: eastDragged(); break;
                    }

                    lastPoint = point;

                    if (self instanceof TextDisplay) {
                        ((TextDisplay) self).resizeMaxFont(bounds);
                    }

                    setBounds(bounds);

                    setNewLoc(bounds.x, bounds.y);

                    if (self instanceof TextDisplay) {
                        ((TextDisplay) self).resizeLabels();
                    }
                    if (self instanceof GraphDisplay) {
                        self.repaint();
                    }

                    setCursor(Cursor.getPredefinedCursor(cursor));
                }
            }

            public void eastDragged() {
                stored.width = (stored.width + offsetX);
                bounds.width = Math.min(windowWidth - bounds.x, Math.max(stored.width, getMinimumSize().width));
            }

            public void southDragged() {
                stored.height = (stored.height + offsetY);
                bounds.height = Math.min(windowHeight - bounds.y, Math.max(stored.height, getMinimumSize().height));
            }

            public void westDragged() {
                stored.width -= offsetX;
                stored.x += offsetX;


                if (offsetX < 0) {
                    if(stored.x <= bounds.x) {
                        bounds.width = Math.min(stored.width, bounds.width + bounds.x);
                        bounds.x = Math.max(stored.x, 0);
                    }
                } else {
                    if(stored.width <= getMinimumSize().width) {
                        bounds.x += bounds.width - getMinimumSize().width;
                        bounds.width = getMinimumSize().width;
                    } else {
                        bounds.width = Math.min(stored.width, bounds.width + bounds.x);
                        bounds.x = Math.max(stored.x, 0);
                    }
                }
            }

            public void northDragged() {
                stored.height -= offsetY;
                stored.y += offsetY;

                if (offsetY < 0) {
                    if(stored.y <= bounds.y) {
                        bounds.height = Math.min(stored.height, bounds.height + bounds.y);
                        bounds.y = Math.max(stored.y, 0);
                    }
                } else {
                    if(stored.height <= getMinimumSize().height) {
                        bounds.y += bounds.height - getMinimumSize().height;
                        bounds.height = getMinimumSize().height;
                    } else {
                        bounds.height = Math.min(stored.height, bounds.height + bounds.y);
                        bounds.y = Math.max(stored.y, 0);
                    }
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public void setNewLoc(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void bound() {
        Rectangle bounds = getBounds();

        bounds.x = Math.max(0, Math.min(bounds.x, getParent().getWidth() - bounds.width));
        bounds.y = Math.max(0, Math.min(bounds.y, getParent().getHeight() - bounds.height));

        setBounds(bounds);
    }

    private Rectangle getRectangle(int w, int h, int location) {
        switch (location) {
            case SwingConstants.NORTH: return new Rectangle(cornerDist, 0, w - 2*cornerDist, edgeDist);
            case SwingConstants.SOUTH: return new Rectangle(cornerDist, h - edgeDist, w - 2*cornerDist, edgeDist);
            case SwingConstants.WEST: return new Rectangle(0, cornerDist, edgeDist, h - 2*cornerDist);
            case SwingConstants.EAST: return new Rectangle(w - edgeDist, cornerDist, edgeDist, h - 2*cornerDist);
            case SwingConstants.NORTH_WEST: return new Rectangle(0, 0, cornerDist, cornerDist);
            case SwingConstants.NORTH_EAST: return new Rectangle(w - cornerDist, 0, cornerDist, cornerDist);
            case SwingConstants.SOUTH_WEST: return new Rectangle(0, h - cornerDist, cornerDist, cornerDist);
            case SwingConstants.SOUTH_EAST: return new Rectangle(w - cornerDist, h - cornerDist, cornerDist, cornerDist);
            default: return new Rectangle();
        }
    }

    public int getCursor(MouseEvent me) {
        System.out.println(cornerDist + " " + edgeDist);
        for (int i = 0; i < locations.length; i++) {
            Rectangle rect = getRectangle(me.getComponent().getWidth(), me.getComponent().getHeight(), locations[i]);
            if (rect.contains(me.getPoint())) return cursors[i];
        }
        return Cursor.MOVE_CURSOR;
    }

    public Dimension getMinimumSize() {
        return new Dimension();
    }

    public void initializeRightClick(MouseEvent e) {
        rMenu = true;
        popup.show(e.getComponent(), e.getX(), e.getY());
    }

    public void cancelRightClick() {
        rMenu = false;
    }

    public void updateResizeBounds() {
        cornerDist = Math.max(6, Math.min(14, getBounds().height / 3));
        edgeDist = Math.max(4, Math.min(12, getBounds().height / 4));
    }

    public void moveToFront() {
        getParent().setComponentZOrder(self, 0);
    }
}
