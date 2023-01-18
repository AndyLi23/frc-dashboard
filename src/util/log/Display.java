package util.log;

import util.ResizablePanel;

import java.awt.*;

public class Display extends ResizablePanel {
    String name;

    public Display(String name) {
        this(name, 0, 0);
    }

    public Display(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }

    public void updateValue(Object value) {}

    public void place() {
        this.setBounds(x, y, (int) getPreferredSize().getWidth() + 2, (int) getPreferredSize().getHeight());
    }

    public void resize() {
        Rectangle r = this.getBounds();
        r.width = Math.max(r.width, (int) (getPreferredSize().getWidth() + 2));
        r.height = Math.max(r.height, (int) getPreferredSize().getHeight());
        this.setBounds(r);
    }
}
