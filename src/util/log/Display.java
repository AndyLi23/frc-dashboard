package util.log;

import javax.swing.*;

public class Display extends JPanel {
    private String name, value;
    private int x, y;
    public Display(String name, String value) {
        this(name, value, 0, 0);
    }

    public Display(String name, String value, int x, int y) {
        this.name = name;
        this.value = value;
        this.x = x;
        this.y = y;
    }

    public void place() {
        this.setBounds(x, y, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
    }
}
