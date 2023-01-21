package util.log;

import util.ResizablePanel;

import java.awt.*;
import java.util.AbstractMap;
import java.util.ArrayList;

public class Display extends ResizablePanel {
    String name;

    protected ArrayList<AbstractMap.SimpleImmutableEntry<Long, Object>> stored = new ArrayList<>();
    protected final int maxStoredSize = 20000;

    public Display(String name, Object value) {
        this(name, value, 0, 0);
    }

    public Display(String name, ArrayList<AbstractMap.SimpleImmutableEntry<Long, Object>> stored, int x, int y) {
        this(name, x, y);
        this.stored = stored;
    }

    public Display(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }

    public Display(String name, Object value, int x, int y) {
        super(x, y);
        this.name = name;
        stored.add(new AbstractMap.SimpleImmutableEntry<>(System.currentTimeMillis(), value));
    }

    public void updateValue(Object value) {
        stored.add(new AbstractMap.SimpleImmutableEntry<>(System.currentTimeMillis(), value));
        if(stored.size() > maxStoredSize) stored.remove(0);
    }

    public ArrayList<AbstractMap.SimpleImmutableEntry<Long, Object>> getStored() {
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
}
