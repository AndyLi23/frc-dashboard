package core.field;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.HashMap;
import java.util.Properties;

public class DrawPanel extends JPanel {
    private BufferedImage field;
    private double fw, fh;
    private double x, y, t;
    private boolean blue;
    private double robotWidth = 36;
    private double robotHeight = 32;

    private double rfw = 651.22;
    private double rfh = 315.6;

    public DrawPanel(BufferedImage field) {
        this.field = field;
        this.fw = field.getWidth();
        this.fh = field.getHeight();

        x = 72;
        y = 21;
        t = 0;
        blue = true;
    }

    public void update(double x, double y, double t, boolean blue) {
        this.x = x;
        this.y = y;
        this.t = t;
        this.blue = blue;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        double maxWidth = this.getWidth();
        double maxHeight = this.getHeight();

        double ratio = Math.min(maxWidth / fw, maxHeight / fh);

        RenderingHints hints = new RenderingHints(new HashMap<>());
        hints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);
        hints.put(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        AffineTransform transform = AffineTransform.getScaleInstance(
                ratio,
                ratio);
        BufferedImageOp op = new AffineTransformOp(transform, hints);

        BufferedImage scaledImage = op.filter(field, null);

        g.drawImage(scaledImage, (int) (maxWidth - ratio * fw) / 2, (int) (maxHeight - ratio * fh) / 2, Color.WHITE, null);


        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.GREEN);

        int[] x_ = new int[]{(int) (x - Math.cos(t) * (robotWidth/2) + Math.sin(t) * (robotHeight/2)),
                (int) (x - Math.cos(t) * (robotWidth/2) - Math.sin(t) * (robotHeight/2)),
                (int) (x + Math.cos(t) * (robotWidth/2) - Math.sin(t) * (robotHeight/2)),
                (int) (x + Math.cos(t) * (robotWidth/2) + Math.sin(t) * (robotHeight/2))};
        int[] y_ = new int[]{(int) (y - Math.sin(t) * (robotWidth/2) - Math.cos(t) * (robotHeight/2)),
                (int) (y - Math.sin(t) * (robotWidth/2) + Math.cos(t) * (robotHeight/2)),
                (int) (y + Math.sin(t) * (robotWidth/2) + Math.cos(t) * (robotHeight/2)),
                (int) (y + Math.sin(t) * (robotWidth/2) - Math.cos(t) * (robotHeight/2))};

        for(int i = 0; i < 4; ++i) {
            x_[i] = (int) ((maxWidth - ratio * fw) / 2 + x_[i] * (fw / rfw * ratio));
            y_[i] = (int) (maxHeight - ((maxHeight - ratio * fh) / 2 + y_[i] * (fh / rfh * ratio)));
            System.out.println(x_[i] + " " + y_[i]);
        }

        g2.fillPolygon(
                x_, y_,
                4
        );

        g2.setStroke(new BasicStroke(2));
        g2.setColor(blue ? Color.BLUE : Color.RED);

        g2.drawPolygon(x_, y_, 4);
    }
}
