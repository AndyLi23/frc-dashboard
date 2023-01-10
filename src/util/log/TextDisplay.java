package util.log;

import javax.swing.*;
import java.awt.*;

public class TextDisplay extends Display {
    private JLabel nameLabel, valueLabel;
    private int textSize = 14;

    private int paddingX = 8;
    private int paddingY = 4;

    public TextDisplay(String name, String value) {
        this(name, value, 0, 0);
    }

    public TextDisplay(String name, String value, int x, int y) {
        super(name, value, x, y);

        nameLabel = new JLabel(name);
        valueLabel = new JLabel(value);

        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, textSize));
        valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, textSize));

        nameLabel.setBorder(BorderFactory.createEmptyBorder(paddingY, paddingX, paddingY, paddingX));
        valueLabel.setBorder(BorderFactory.createEmptyBorder(paddingY, paddingX, paddingY, paddingX));

        nameLabel.setBackground(new Color(225, 225, 225));
        valueLabel.setBackground(new Color(255, 255 ,255));
        nameLabel.setOpaque(true);
        valueLabel.setOpaque(true);


        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(nameLabel);
        this.add(valueLabel);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int) (nameLabel.getPreferredSize().getWidth()
                + valueLabel.getPreferredSize().getWidth()),
                (int) (Math.max(nameLabel.getPreferredSize().getHeight(),
                valueLabel.getPreferredSize().getHeight())));
    }
}
