package app;

import core.Window;
import core.field.DrawPanel;
import core.util.NTInstance;
import edu.wpi.first.networktables.EntryListenerFlags;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Field extends Window {
    private double x, y, t;
    private boolean blue;

    public Field() {
        super("Field Map", true, new Dimension(800, 400), new Dimension());

        BufferedImage field = null;
        try {
            field = ImageIO.read(new File("./res/img/field.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        DrawPanel p = new DrawPanel(field);
        p.setBackground(Color.BLACK);
        p.setLayout(null);
        this.add(p);

        NTInstance.getInstance().getDashboardTable().addEntryListener((tb, key, entry, value, flags) -> {
            if(key.equals("Pose X")) x = value.getDouble();
            if(key.equals("Pose Y")) y = value.getDouble();
            if(key.equals("Heading")) t = value.getDouble();
            if(key.equals("fieldside")) blue = value.getString().equals("left");

            if(key.equals("Pose X") || key.equals("Pose Y") || key.equals("Heading") || key.equals("fieldside"))
                p.update(x, y, t, blue);

        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        p.update(72 + 100, 42 - 21 + 66, 0, true);

        showWindow();
    }


}
