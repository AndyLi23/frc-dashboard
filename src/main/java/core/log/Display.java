package core.log;

import app.Logger;
import core.util.Pair;
import edu.wpi.first.networktables.NetworkTableValue;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Display extends MovablePanel implements Serializable {
    private String name;
    protected ArrayList<Logger.DisplayType> types;

    protected ArrayList<Pair> stored;
    protected final int maxStoredSize = 20000;

    public Display(String name, int x, int y, ArrayList<Pair> stored) {
        this(name, x, y, Logger.getTypes(), stored);
    }

    public Display(String name, int x, int y) {
        this(name, x, y, null);
    }

    public Display(String name, int x, int y, ArrayList<Logger.DisplayType> types, ArrayList<Pair> stored) {
        super(x, y);
        this.name = name;
        this.types = types;

        this.stored = stored == null ? new ArrayList<>() : stored;
    }

    public void updateValue(Long time, Object value) {
        stored.add(new Pair(time, String.valueOf(value)));
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
            getLoggerParent().replace(this, Logger.DisplayType.kTextDisplay);
        }
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
    
    public Logger getLoggerParent() {
//        System.out.println(getParent());
        return ((Logger) getParent().getParent().getParent().getParent().getParent());
    }

    @Override
    public String getName() {
        return name;
    }

    public ArrayList<Logger.DisplayType> getTypes() {
        return types;
    }

    public void update() {

    }
}
