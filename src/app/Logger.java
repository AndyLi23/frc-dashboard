package app;

import util.Window;
import util.log.Display;
import util.log.TextDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Logger extends Window {
    private final Timer loopTimer;

    private HashMap<String, Display> panels = new HashMap<>();

    public Logger() {
        // TESTING
        super("Logger", false, new Dimension(500, 300), new Dimension(800, 500), new Color(200, 200, 200));

//        super("Logger", true, new Dimension(800, 400), new Dimension());

        this.setLayout(null);


        for(int i = 0; i < 10; i += 1) {
            panels.put("Test " + i, new TextDisplay("Test " + i, "", 0, i * 30));
        }

        for (Display d : panels.values()) {
            this.getContentPane().add(d);
            d.place();
        }

        showWindow();

        loopTimer = new Timer(50, action -> {
            loop();
        });
        loopTimer.setRepeats(true);
        loopTimer.start();
    }

    public void loop() {
        // TESTING
//        this.toFront();
//        System.out.println(d.getBounds());

        for (Display d : panels.values()) {
            d.updateValue(String.valueOf((int) (Math.random() * Math.pow(10, 5 + (int) (Math.random() * 5)))));
        }

        for (Component c : getComponents()) c.repaint();
    }

    @Override
    public void onCloseAction() {
        loopTimer.stop();
    }
}
