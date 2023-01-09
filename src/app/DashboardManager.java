package app;

import util.RoundedButton;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class DashboardManager extends JFrame {
    public DashboardManager() throws IOException {
        super("Dashboard Manager");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.getContentPane().setBackground(new Color(25, 25, 25));

        String[] paths = new String[]{"docket", "radar", "camera", "settings"};
        String[] optionNames = new String[]{"Logger", "Field", "Cameras", "Settings"};
        RoundedButton[] options = new RoundedButton[4];

        for (int i = 0; i < paths.length; ++i) {
            BufferedImage img = ImageIO.read(new File("img/" + paths[i] + ".png"));
            JLabel image = new JLabel(new ImageIcon(img.getScaledInstance(i == 0 ? 50 : 60, i == 0 ? 50 : 60,
                    Image.SCALE_SMOOTH)));
            image.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            image.setBorder(BorderFactory.createEmptyBorder(i == 0 ? 18 : 13, 0, i == 0 ? 10 : 5, 0));

            options[i] = new RoundedButton(i);
            options[i].setLayout(new BoxLayout(options[i], BoxLayout.Y_AXIS));

            JLabel optionText = new JLabel(optionNames[i], JLabel.CENTER);
            optionText.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            optionText.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            optionText.setForeground(Color.WHITE);

            options[i].setBackground(new Color(40, 40, 40));
            options[i].setHoverColor(new Color(50, 50, 50));
            options[i].setClickColor(new Color(80, 80, 80));
            options[i].add(image);
            options[i].add(optionText);
            options[i].setPreferredSize(new Dimension(110, 110));
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(35, 0, 0, 0));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(25, 25, 25));

        JPanel optionsPanel = new JPanel();
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        optionsPanel.setBackground(new Color(25, 25, 25));

        JLabel title = new JLabel("Open Dashboards", JLabel.CENTER);
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        title.setForeground(Color.WHITE);

        for (JPanel option : options) optionsPanel.add(option);

//        optionsPanel.add(options[0]);

        mainPanel.add(title);
        mainPanel.add(optionsPanel);
        this.getContentPane().add(mainPanel);


        this.pack();
        this.setSize(600, 300);
        this.setVisible(true);
    }
}
