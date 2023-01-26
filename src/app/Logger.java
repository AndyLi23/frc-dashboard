package app;

import util.Window;
import util.log.Display;
import util.log.GraphDisplay;
import util.log.TextDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Logger extends Window {
    private final Timer loopTimer;

    private HashMap<String, Display> panels = new HashMap<>();

    public Logger() {
        // TESTING
        super("Logger", true, new Dimension(500, 300), new Dimension(800, 500), new Color(200, 200, 200));

//        super("Logger", true, new Dimension(500, 300), new Dimension());

        this.setLayout(null);


        for(int i = 0; i < 10; i += 1) {
            panels.put("Test " + i, new TextDisplay("Test " + i, 0, 0, i * 30));
        }

        panels.put("Graph!", new GraphDisplay("Graph!", 0, 200, 200));

        for (Display d : panels.values()) {
            this.getContentPane().add(d);
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
            d.updateValue(((int) (Math.random() * 3) - 1) * (int) (Math.random() * Math.pow(10, 0 + (int) (Math.random() * 5))));
        }

        for (Component c : getComponents()) c.repaint();

//        System.out.println(System.currentTimeMillis() - time);
    }

    @Override
    public void onCloseAction() {
        loopTimer.stop();
    }
}
