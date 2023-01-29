package app;

import util.Window;
import util.log.Display;
import util.log.GraphDisplay;
import util.log.TextDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Logger extends Window {
    private final Timer loopTimer;

    private HashMap<String, Display> panels = new HashMap<>();

    public enum DisplayType {
        kTextDisplay,
        kGraphDisplay
    }

    public static ArrayList<DisplayType> getTypes() {
        return new ArrayList<>(Arrays.asList(DisplayType.values()));
    }

    public Logger() {
        // TESTING
        super("Logger", true, new Dimension(500, 300), new Dimension(800, 500), new Color(200, 200, 200));

//        super("Logger", true, new Dimension(500, 300), new Dimension());

        this.setLayout(null);


        for(int i = 0; i < 10; i += 1) {
            panels.put("Test " + i, new TextDisplay("Test " + i, 0, i * 30, this));
        }

        panels.put("Graph!", new GraphDisplay("Graph!", 200, 200, this));

        for (Display d : panels.values()) {
            this.add(d);
            d.place();
        }

        showWindow();

        loopTimer = new Timer((int) (1000./60.), action -> {
            loop();
        });
        loopTimer.setRepeats(true);
        loopTimer.start();
    }

    public void loop() {
        long time = System.currentTimeMillis();
        // TESTING
        for (Display d : panels.values()) {
            if(Math.random() <= 0.001) {
                d.updateValue("Oh no a string");
            } else {
                d.updateValue(((int) (Math.random() * 3) - 1) * (int) (Math.random() * Math.pow(10, 0 + (int) (Math.random() * 5))));
            }
        }

        for (Component c : getComponents()) c.repaint();

        if(System.currentTimeMillis() - time > 1) System.out.println(System.currentTimeMillis() - time);
    }

    @Override
    public void remove(Component c) {
        super.remove(c);
        panels.remove(c.getName());
    }

    public void replace(Component c, DisplayType type) {
        switch (type) {
            case kGraphDisplay -> panels.replace(c.getName(), new GraphDisplay((TextDisplay) c));
            case kTextDisplay -> panels.replace(c.getName(), new TextDisplay((GraphDisplay) c));
        }
        this.add(panels.get(c.getName()));
        super.remove(c);
        panels.get(c.getName()).moveToFront();
        panels.get(c.getName()).place();
    }

    @Override
    public void onCloseAction() {
        loopTimer.stop();

    }
}
