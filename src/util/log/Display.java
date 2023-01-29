package util.log;

import app.Logger;
import util.Pair;
import util.ResizablePanel;

import java.awt.*;
import java.util.ArrayList;

public class Display extends ResizablePanel {
    private String name;
    protected ArrayList<Logger.DisplayType> types;

    protected ArrayList<Pair> stored;
    protected final int maxStoredSize = 20000;
    protected Logger parentLogger;

    public Display(String name, int x, int y, Logger parent, ArrayList<Pair> stored) {
        this(name, x, y, Logger.getTypes(), parent, stored);
    }

    public Display(String name, int x, int y, Logger parent) {
        this(name, x, y, parent, null);
    }

    public Display(String name, int x, int y, ArrayList<Logger.DisplayType> types, Logger parent, ArrayList<Pair> stored) {
        super(x, y);
        this.name = name;
        this.types = types;
        this.parentLogger = parent;

        this.stored = stored == null ? new ArrayList<>() : stored;
    }

    public void updateValue(Object value) {
        stored.add(new Pair(System.currentTimeMillis(), String.valueOf(value)));
        if (stored.size() > maxStoredSize) stored.remove(0);
        checkType(value);
    }

    public void checkType(Object value) {
        try {
            Double.parseDouble(String.valueOf(value));
        } catch (Exception e) {
            if (types.contains(Logger.DisplayType.kGraphDisplay)) {
                disableGraph();
            }
        }
    }

    public void disableGraph() {
        types.remove(Logger.DisplayType.kGraphDisplay);
        if (this instanceof TextDisplay) {
            if(popup.getComponentCount() > 1) popup.remove(1);
        } else if (this instanceof GraphDisplay) {
            parentLogger.replace(this, Logger.DisplayType.kTextDisplay);
        }
    }

    public Logger getParentLogger() {
        return parentLogger;
    }

    public ArrayList<Pair> getStored() {
        return stored;
    }

    public void place() {
        this.setBounds(x, y, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
    }

    public void resize() {
        Rectangle r = this.getBounds();
        r.width = Math.max(r.width, (int) (getPreferredSize().getWidth()));
        r.height = Math.max(r.height, (int) getPreferredSize().getHeight());
        this.setBounds(r);
    }

    @Override
    public String getName() {
        return name;
    }

    public ArrayList<Logger.DisplayType> getTypes() {
        return types;
    }
}
