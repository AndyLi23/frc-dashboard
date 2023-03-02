package core.log;

import app.Logger;
import core.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class TextDisplay extends Display implements Serializable {
    private final JLabel nameLabel, valueLabel;
    private final int textSize = 14;
    private String longest;

    private final int paddingX = 8;
    private final int paddingY = 4;

    private final int minimumFont = 10;

    private int maxWidth;

    public TextDisplay(String name) {
        this(name, 0, 0, Logger.getTypes(), null);
    }

    public TextDisplay(GraphDisplay t) {
        this(t.getName(), t.getX(), t.getY(), t.getTypes(), t.getStored());
    }

    public TextDisplay(String name, int x, int y) {
        this(name, x, y, Logger.getTypes(), null);
    }

    public TextDisplay(String name, int x, int y, ArrayList<Logger.DisplayType> types, ArrayList<Pair> stored) {
        super(name, x, y, types, stored);

        nameLabel = new JLabel(name);
        valueLabel = new JLabel(stored == null ? "" : stored.get(stored.size() - 1).getValue());

        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, textSize));
        valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, textSize));

        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        nameLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY), BorderFactory.createEmptyBorder(paddingY, paddingX, paddingY, paddingX)));
        valueLabel.setBorder(BorderFactory.createEmptyBorder(paddingY, paddingX, paddingY, paddingX));

        nameLabel.setBackground(new Color(225, 225, 225));
        valueLabel.setBackground(new Color(255, 255 ,255));
        setBackground(new Color(255,  255, 255));

        nameLabel.setOpaque(true);
        valueLabel.setOpaque(true);

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(nameLabel);
        this.add(valueLabel);

        load();
    }

    @Override
    public void loadPopup() {
        super.loadPopup();

        if (types.contains(Logger.DisplayType.kGraphDisplay)) {
            JMenuItem graph = new JMenuItem("Change to Graph");
            graph.addActionListener(e -> ((Logger) getParent().getParent().getParent().getParent().getParent()).replace(
                    this, Logger.DisplayType.kGraphDisplay));
            popup.add(graph, 0);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if(valueLabel.getPreferredSize().getWidth() > maxWidth) {
            longest = valueLabel.getText();
            maxWidth = (int) valueLabel.getPreferredSize().getWidth();
        }

        return new Dimension((int) (nameLabel.getPreferredSize().getWidth() + maxWidth),
                (int) (Math.max(nameLabel.getPreferredSize().getHeight(),
                valueLabel.getPreferredSize().getHeight())));
    }

    @Override
    public void updateValue(Long time, Object value) {
        super.updateValue(time, value);
        if((String.valueOf(value)).contains("\n")) valueLabel.setText("<html>" + (String.valueOf(value)).replace("\n", "<br>") + "</html>");
        else valueLabel.setText(String.valueOf(value));
        resize();
        resizeLabels();
    }

    public Dimension getLabelSize(int font) {
        JLabel test1 = new JLabel(nameLabel.getText());
        JLabel test2 = new JLabel(longest);
        test1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, font));
        test2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, font));
        test1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY), BorderFactory.createEmptyBorder(paddingY, paddingX, paddingY, paddingX)));
        test2.setBorder(BorderFactory.createEmptyBorder(paddingY, paddingX, paddingY, paddingX));
        return new Dimension((int) (test1.getPreferredSize().getWidth() + test2.getPreferredSize().getWidth()),
                (int) Math.max(test1.getPreferredSize().getHeight(), test2.getPreferredSize().getHeight()));
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension ls = getLabelSize(minimumFont);
        return new Dimension(ls.width+4, ls.height);
    }

    public void resizeMaxFont(Rectangle size) {
        int font;
        for (font = minimumFont; true; font++) {
            if (getLabelSize(font).height > size.height || getLabelSize(font).width > size.width) break;
        }
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, font - 1));
        valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, font - 1));

        JLabel test2 = new JLabel(longest);
        test2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, font - 1));
        test2.setBorder(BorderFactory.createEmptyBorder(paddingY, paddingX, paddingY, paddingX));

        maxWidth = (int) test2.getPreferredSize().getWidth();
    }

    public void resizeLabels() {
        JLabel test = new JLabel(nameLabel.getText());
        test.setFont(new Font(Font.SANS_SERIF, Font.BOLD, nameLabel.getFont().getSize()));
        test.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY), BorderFactory.createEmptyBorder(paddingY, paddingX, paddingY, paddingX)));

        Dimension newDim = new Dimension((int) Math.ceil(test.getPreferredSize().getWidth()), getHeight());
        nameLabel.setMinimumSize(newDim);
        nameLabel.setPreferredSize(newDim);
        nameLabel.setMaximumSize(newDim);
        nameLabel.setSize(newDim);
        nameLabel.revalidate();

    }

    @Override
    public void resize() {
        Rectangle r = this.getBounds();
        r.width = Math.max(r.width, (int) (getPreferredSize().getWidth() + 2));
        r.height = Math.max(r.height, (int) getPreferredSize().getHeight());
        this.setBounds(r);
    }

    @Override
    public void place() {
        this.setBounds(x, y, (int) getPreferredSize().getWidth() + 2, (int) getPreferredSize().getHeight());
    }
}
