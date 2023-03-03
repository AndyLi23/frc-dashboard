package core.log;

import core.util.Pair;
import core.util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Graph extends JPanel {

    private int[] steps = {1, 2, 5, 10};
    private final int font = 11;
    private int sh, sw;
    private double wr, hr, tcur;
    private double[] horizontalRange, verticalRange;
    private final int bottom = 14;
    private final double timeRatio = 1000000;

    public Graph() {
        super();

        setBackground(new Color(255, 255, 255));

        sw = getWidth();
        sh = getHeight() - bottom;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        GraphDisplay parent = ((GraphDisplay) getParent());

        ArrayList<Pair> data = parent.getStored();
        int linesH = parent.getApproxGridLinesH();
        int linesV = parent.getApproxGridLinesV();

        sw = getWidth();
        sh = getHeight() - bottom;

        if(data.size() > 0) {

            double tmax, tmin, timeRange;

            double actualTime = parent.getEndTime();


            if (parent.getZoomed()) {
                tmax = parent.getT_right();
                tmin = parent.getT_left();
                timeRange = tmax - tmin;
                tcur = parent.getT_cur();
            } else {
                tcur = actualTime;

                timeRange = Math.min(parent.getT_max() * timeRatio,
                        tcur - parent.getStartTime());

                tmax = 0;
                tmin = -timeRange;
            }

            double approxHRange = timeRange / linesH;

            horizontalRange = getRange(tmax, tmin, approxHRange);

            int left_index, right_index;
            if (parent.getZoomed()) {
                left_index = Util.search(data, (tcur + horizontalRange[0]), false);
                right_index = Util.search(data, (tcur + horizontalRange[2]), true);
            } else {
                left_index = Util.search(data, (tcur + tmin), true);
                right_index = Util.search(data, (tcur + tmax), true);
            }

            double[] vRange;
            if (parent.getZoomed()) {
                vRange = new double[]{parent.getY_left(), parent.getY_right()};
            } else {
                vRange = getArrayRange(data, left_index, right_index);
            }

            double approxVRange = (vRange[1] - vRange[0]) / linesV;
            verticalRange = getRange(vRange[1], vRange[0], approxVRange);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            double cw = horizontalRange[2] - horizontalRange[0];
            double ch = verticalRange[2] - verticalRange[0];

            wr = sw / cw;
            hr = sh / ch;

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setStroke(new BasicStroke(1));

            if (horizontalRange[1] != 0) {
                for (double l = horizontalRange[0]; l <= horizontalRange[2] + (horizontalRange[1] / 2); l += horizontalRange[1]) {
                    g2d.drawLine(getScreenX(l), 0, getScreenX(l), sh);
                }
            }

            if (verticalRange[1] != 0) {
                for (double l = verticalRange[0]; l <= verticalRange[2] + (verticalRange[1] / 2); l += verticalRange[1]) {
                    g2d.drawLine(0, getScreenY(l), sw, getScreenY(l));
                }
            }

            g2d.setColor(Color.RED);

            int prevx = -1;
            int prevy = -1;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            if (left_index != -1 && right_index != -1) {

                for (int ind = left_index; ind < right_index; ++ind) {
                    int datax = getScreenX(data.get(ind).getKey() - tcur);
                    int datay = getScreenY(Double.parseDouble(data.get(ind).getValue()));
                    if (prevx != -1) {
                        g2d.drawLine(prevx, prevy, datax, datay);
                    }
                    prevx = datax;
                    prevy = datay;
                }
            }

            if (verticalRange[1] != 0) {
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, font));

                for (double l = verticalRange[0]; l <= verticalRange[2] + (verticalRange[1] / 2); l += verticalRange[1]) {
                    if (getScreenY(l) < 2) g2d.drawString(String.valueOf(l), 3, 11);
                    else if (getScreenY(l) == sh - 1) g2d.drawString(String.valueOf(l), 3, sh - 2);
                    else g2d.drawString(String.valueOf(l), 3, getScreenY(l) + 4);
                }
            }

            g2d.setColor(new Color(235, 235, 235));
            g2d.fillRect(0, sh, sw, bottom);

            if (horizontalRange[1] != 0) {
                g2d.setColor(Color.BLACK);

                for (double l = horizontalRange[0]; l <= horizontalRange[2] + (horizontalRange[1] / 2); l += horizontalRange[1]) {
                    String pr = String.valueOf((l - (actualTime - tcur)) / timeRatio);
                    if (pr.length() > 6) pr = String.format("%.2f", (l - (actualTime - tcur)) / timeRatio);
                    int tw = (int) Math.ceil(g2d.getFont().getStringBounds(pr, g2d.getFontRenderContext()).getWidth());
                    if (getScreenX(l) < 2) g2d.drawString(pr, 0, getHeight() - 2);
                    else if (getScreenX(l) == sw - 1) g2d.drawString(pr, sw - tw, getHeight() - 2);
                    else g2d.drawString(pr, getScreenX(l) - tw / 2, getHeight() - 2);
                }
            }

            if (parent.getClicked()) {
                Point start = screenToCanvas(parent.getStart());
                Point end = screenToCanvas(parent.getEnd());

                Graphics2D g2 = (Graphics2D) g;
                g2.setComposite(AlphaComposite.SrcOver.derive(0.2f));
                g2.setColor(Color.GRAY);

                Rectangle f = new Rectangle(Math.min(end.x, start.x), Math.min(end.y, start.y),
                        Math.abs(end.x - start.x), Math.abs(end.y - start.y));

                g2.fill(f);

                g2.setComposite(AlphaComposite.SrcOver);
                g2.setStroke(new BasicStroke(2));
                g2.draw(f);
            }
        }

    }

    public int getScreenX(double x) {
        double xt = x - horizontalRange[0];
        if((int) (xt * wr) == getWidth()) return getWidth() - 1;
        return (int) (xt * wr);
    }

    public int getScreenY(double y) {
        double yt = y - verticalRange[0];
        if((sh - (int) (yt * hr)) == sh) return sh - 1;
        return sh - (int) (yt * hr);
    }

    public double[] getRange(double tmax, double tmin, double approxRange) {

        int pow = (int) Math.floor(Math.log10(approxRange));

        double step = 0;
        double dist = Double.MAX_VALUE;

        for(int i : steps) {
            double j = i * Math.pow(10, pow);
            if(Math.abs(j - approxRange) < dist) {
                dist = Math.abs(j - approxRange);
                step = j;
            }
        }

        int lowR = (int) Math.floor(tmin / step);
        int highR = (int) Math.ceil(tmax / step);

        return new double[]{lowR * step, step, highR * step};
    }

    public double[] getArrayRange(ArrayList<Pair> array, int minR, int highR) {
        double[] ans = new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        for(int i = minR; i <= highR; ++i) {
            ans[0] = Math.min(ans[0], Double.parseDouble((array.get(i).getValue())));
            ans[1] = Math.max(ans[1], Double.parseDouble((array.get(i).getValue())));
        }
        return ans;
    }

    public Rectangle getGraphDim() {
        return new Rectangle(getX(), getY(), sw, sh);
    }

    public Point screenToCanvas(Point p) {
        return new Point(p.x - getX(), p.y - getY());
    }

    public double getGraphX(double x) {
        return (((x - getX()) / sw) * (horizontalRange[2] - horizontalRange[0]) + horizontalRange[0]);
    }

    public double getGraphY(double y) {
        return  (((sh - (y - getY())) / sh) * (verticalRange[2] - verticalRange[0]) + verticalRange[0]);
    }

    public double getT_cur() {
        return tcur;
    }

}
