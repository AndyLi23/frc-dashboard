package util;

import app.DashboardManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window extends JFrame {

    public Window(String name, boolean maximized, Dimension minimumSize, Dimension size) {
        this(name, maximized, minimumSize, size, new Color(25, 25, 25));
    }

    public Window(String name, boolean maximized, Dimension minimumSize, Dimension size, Color background) {
        super(name);

        if(!(this instanceof DashboardManager)) {
            DashboardManager.openedWindow(this);
        }

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (maximized) this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setMinimumSize(minimumSize);
        if (!maximized) this.setSize(size);

        this.getContentPane().setBackground(background);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                onClose();
            }
        });
    }

    public void showWindow() {
        this.pack();
        this.setVisible(true);
    }

    public void onClose() {
        DashboardManager.closedWindow(this);
        onCloseAction();
    }

    public void onCloseAction() {
    }
}
