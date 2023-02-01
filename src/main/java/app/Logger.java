package app;

import core.Window;
import core.log.Display;
import core.log.GraphDisplay;
import core.log.TextDisplay;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
//            if(Math.random() <= 0.001) {
//                d.updateValue("Oh no a string");
//            } else {n
                d.updateValue(((int) (Math.random() * 3) - 1) * (int) (Math.random() * Math.pow(10, 0 + (int) (Math.random() * 5))));
//            }
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
        try {
            //Creating the object
            //Creating stream and writing the object
            ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream("res/stored/panels.txt"));
            out.writeObject(panels);
            out.flush();
            //closing the stream
            out.close();
            System.out.println("success");

            ObjectInputStream in=new ObjectInputStream(new FileInputStream("res/stored/panels.txt"));
            HashMap<String, Display> r= (HashMap<String, Display>) in.readObject();
            //printing the data of the serialized object
            System.out.println(r);
            //closing the stream
            in.close();



        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}