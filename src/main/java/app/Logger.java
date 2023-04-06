package app;

import core.Window;
import core.log.Display;
import core.log.GraphDisplay;
import core.log.TextDisplay;
import core.util.NTInstance;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Logger extends Window {
    private final Timer loopTimer;
    private final HashMap<String, Display> panels;
    private long startTime = -1;
    private long lastTime = -1;
    private final JPanel display, bar;
    private final int barHeight = 50;

    public enum DisplayType {
        kTextDisplay,
        kGraphDisplay
    }

    public static ArrayList<DisplayType> getTypes() {
        return new ArrayList<>(Arrays.asList(DisplayType.values()));
    }

    public Logger() {
        // TESTING
        super("Logger", false, new Dimension(500, 300), new Dimension(800, 500), new Color(200, 200, 200));

        ArrayList<String> loads = new ArrayList<>();
        File f = new File("res/stored/");
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                loads.add(c.getName().substring(0, c.getName().length() - 4));
            }
        }
        loads.add("None");

        Object[] pos = loads.toArray();
        String s = (String)JOptionPane.showInputDialog(
                this,
                "Load saved format: ",
                "Load Format",
                JOptionPane.PLAIN_MESSAGE,
                null,
                pos,
                pos[0]);

        this.setLayout(new BorderLayout());

        display = new JPanel();
        bar = new JPanel();

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 0, 0);
        

        display.setLayout(null);
        bar.setLayout(null);

        display.setBackground(new Color(200, 200, 200));

        this.add(display);
        this.add(bar, BorderLayout.SOUTH);

        bar.setPreferredSize(new Dimension(this.getWidth(), barHeight));

        this.pack();

        initiateNT();

        HashMap<String, Display> pn;

        if ((s != null) && (s.length() > 0) && !s.equals("None")) {
            System.out.println("Loading file " + s + ".txt");

            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream("res/stored/" + s + ".txt"));
                pn = (HashMap<String, Display>) in.readObject();
                in.close();
            } catch (Exception e) {
//                System.out.println("Loading failed: " + e.getMessage());

                pn = new HashMap<>();
            }
        } else {
            System.out.println("Not loading");

            pn = new HashMap<>();

//            for (int i = 0; i < 10; ++i) {
//                pn.put("Testing " + i, new TextDisplay("Testing " + i, 10, i * 30));
//                pn.get("Testing " + i).place();
//            }
        }

        for (Display d : pn.values()) {
            display.add(d);
            d.load();
            if(d.getStored().size() > 0) startTime = (startTime == -1 ? d.getStored().get(0).getKey() :
                    Math.min(startTime, d.getStored().get(0).getKey()));
        }
        panels = pn;

        addButtonGroup(new String[]{"Preload, Balance", "2 + Balance", "2 + Pickup"},
                new String[]{"preload", "balance", "pick"}, "auto", 10, 50, 2);

        addButtonGroup(new String[]{"LEFT FIELD (Blue)", "RIGHT FIELD (Red)"},
                new String[]{"left", "right"}, "fieldside", 10, 120, 0);

        addButtonGroup(new String[]{"Low", "High"},
                new String[]{"L", "H"}, "autoside", 200, 50, 0);

        showWindow();

        loopTimer = new Timer((int) (1000./60.), action -> {
            loop();
        });
        loopTimer.setRepeats(true);
        loopTimer.start();
    }

    public void loop() {
//        long time = System.currentTimeMillis();
//
        // TESTING
//        for (Display d : panels.values()) {
//            long microtime = System.nanoTime() / 1000L;
//            d.updateValue(microtime, ((int) (Math.random() * 3) - 1) * (int) (Math.random() * Math.pow(10, (int) (Math.random() * 5))));
//            lastTime = Math.max(lastTime, microtime);
//            if (startTime == -1) startTime = microtime;
//        }

//        System.out.println(startTime + " " + lastTime + " " + System.currentTimeMillis());

        for (Display d : panels.values()) d.update();
        for (Component c : getComponents()) c.repaint();

//        if(System.currentTimeMillis() - time > 1) System.out.println(System.currentTimeMillis() - time);
    }

    @Override
    public void remove(Component c) {
        display.remove(c);
        panels.remove(c.getName());
    }

    public void initiateNT() {
        NTInstance.getInstance().getDashboardTable().addEntryListener((tb, key, entry, value, flags) -> {
            updateValue(key, value);
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }

    public void addButtonGroup(String[] buttonNames, String[] buttonReturn, String entry, int x, int startingY, int starting) {
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < buttonNames.length; ++i) {
            JRadioButton f = new JRadioButton(buttonNames[i]);
            int finalI = i;
            f.addActionListener(e -> {
                if(f.isSelected()) NTInstance.getInstance().getDashboardTable().getEntry(entry).setString(buttonReturn[finalI]);
            });
            group.add(f);
            if (i == starting) {
                f.setSelected(true);
                NTInstance.getInstance().getDashboardTable().getEntry(entry).setString(buttonReturn[i]);
            }
            f.setBounds(x, startingY + 20 * i, 150, 20);
            display.add(f);
        }
    }

    public void updateValue(String key, NetworkTableValue value) {
        if (!panels.containsKey(key)) {
            panels.put(key, new TextDisplay(key, 0, 0));
            display.add(panels.get(key));
            panels.get(key).place();
        }
        panels.get(key).updateValue(value.getTime(), value.getValue());

        lastTime = Math.max(lastTime, value.getTime());
        if (startTime == -1) startTime = value.getTime();
    }

    public void replace(Component c, DisplayType type) {
        switch (type) {
            case kGraphDisplay: panels.replace(c.getName(), new GraphDisplay((TextDisplay) c)); break;
            case kTextDisplay: panels.replace(c.getName(), new TextDisplay((GraphDisplay) c)); break;
        }
        display.add(panels.get(c.getName()));
        display.remove(c);
        panels.get(c.getName()).moveToFront();
        panels.get(c.getName()).place();
        panels.get(c.getName()).bound();
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return lastTime;
    }

    @Override
    public void onCloseAction() {
        try {
            loopTimer.stop();


            final JOptionPane optionPane = new JOptionPane("Save format as: ",
                    JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

            optionPane.setWantsInput(true);
            optionPane.setSelectionValues(null);
            optionPane.setInitialSelectionValue("panel1");

            final JDialog dialog = new JDialog(this, "Save", true);
            dialog.setContentPane(optionPane);

            optionPane.addPropertyChangeListener(
                    e -> {
                        String prop = e.getPropertyName();

                        if (dialog.isVisible() && (e.getSource() == optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
                            if ((optionPane.getInputValue() != null && ((String) optionPane.getInputValue()).length() > 0)
                                    || optionPane.getValue().equals(JOptionPane.CANCEL_OPTION)) {
                                dialog.setVisible(false);
                            } else {
                                optionPane.setMessage("Save format as: \n(Invalid input)");
                                optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
                            }
                        }
                    });
            dialog.pack();
            dialog.setVisible(true);

            if (!optionPane.getValue().equals(JOptionPane.UNINITIALIZED_VALUE)) {
                int value = (Integer) optionPane.getValue();
                if (value == JOptionPane.OK_OPTION) {
                    String file = String.valueOf(optionPane.getInputValue());
                    System.out.println("Saving as " + file + ".txt");
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("res/stored/" + file + ".txt"));
                    out.writeObject(panels);
                    out.flush();
                    out.close();
                } else {
                    System.out.println("Saving cancelled");
                }
            } else {
                System.out.println("Saving cancelled");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
