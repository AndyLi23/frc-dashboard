package app;

import util.Window;
import util.log.Display;
import util.log.TextDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Logger extends Window {
    private final Timer loopTimer;

    private HashMap<String, Display> panels = new HashMap<>();

    public Logger() {
        // TESTING
        super("Logger", false, new Dimension(800, 400), new Dimension(800, 600), new Color(200, 200, 200));

//        super("Logger", true, new Dimension(800, 400), new Dimension());

        this.setLayout(null);

        panels.put("Test", new TextDisplay("Test", "Testing Value", 0, 0));
        panels.put("Test 2", new TextDisplay("Test 2", "Testing Value 2!", 0, 100));

        for (Display d : panels.values()) {
            this.getContentPane().add(d);
            d.place();
        }

        showWindow();

        loopTimer = new Timer(20, action -> {
            loop();
        });
        loopTimer.setRepeats(true);
        loopTimer.start();
    }

    public void loop() {
        // TESTING
        this.toFront();
//        System.out.println(d.getBounds());
    }

    @Override
    public void onCloseAction() {
        loopTimer.stop();
    }
}
