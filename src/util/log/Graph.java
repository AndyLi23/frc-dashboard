package util.log;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class Graph extends JPanel {

    private int[] steps = {1, 2, 5, 10};
    private final int font = 11;
    private int sh, sw;
    private double wr, hr;
    private double[] horizontalRange, verticalRange;

    private DecimalFormat df = new DecimalFormat("0.####E0");


    public Graph() {
        super();

        setBackground(new Color(255, 255, 255));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ArrayList<AbstractMap.SimpleImmutableEntry<Long, Object>> data = ((GraphDisplay) getParent()).getStored();
        int linesH = ((GraphDisplay) getParent()).getApproxGridLinesH();
        int linesV = ((GraphDisplay) getParent()).getApproxGridLinesV();


        double tcur = data.get(data.size() - 1).getKey() / 1000.;

        double timeRange = Math.min(((GraphDisplay) getParent()).getT_max(),
                tcur - data.get(0).getKey() / 1000.);

        double tmax = 0;
        double tmin = -timeRange;

        double approxHRange = timeRange / linesH;

        horizontalRange = getRange(tmax, tmin, approxHRange);

//        System.out.println(Arrays.toString(horizontalRange));

        int left_index = search(data, (tcur + tmin) * 1000);
        int right_index = search(data, (tcur + tmax) * 1000);

        double[] vRange = getArrayRange(data, left_index, right_index);

//        System.out.println(Arrays.toString(vRange));

        double approxVRange = (vRange[1] - vRange[0]) / linesV;
        verticalRange = getRange(vRange[1], vRange[0], approxVRange);

//        System.out.println(Arrays.toString(horizontalRange) + "  |  " + Arrays.toString(verticalRange) + "  |  " + hLineN + ", " + vLineN);


//        System.out.println((tcur + tmin) * 1000 + " " + (tcur + tmax) * 1000);
//        System.out.println(data.get(left_index) + ", " + left_index + " ||| " + data.get(right_index) + ", " + right_index);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

//        g2d.drawLine(0, 0, 100, 100);
//        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, font));
//        g2d.drawString("Testing", 100, 100);

        int maxTextW = 0;
        int maxTextH = 0;

//        System.out.println(verticalRange[2]);

//        if(verticalRange[1] != 0) {
//            for (double l = verticalRange[0]; l <= verticalRange[2]; l += verticalRange[1])
//                maxTextW = Math.max(maxTextW, (int) Math.ceil(g2d.getFont().getStringBounds(String.valueOf(l), g2d.getFontRenderContext()).getWidth()));
//        }
////            maxTextW = Math.max(maxTextW, g2d.getFont().getStringBounds(String.valueOf(l), g2d.getFontRenderContext()).getWidth());
////
//        if(horizontalRange[1] != 0) {
//            for(double l = horizontalRange[0]; l <= horizontalRange[2]; l += horizontalRange[1])
//                maxTextH = Math.max(maxTextH, (int) Math.ceil(g2d.getFont().getStringBounds(String.valueOf(l), g2d.getFontRenderContext()).getHeight()));
//        }

        sw = getWidth();
        sh = getHeight() - 14;

        double cw = horizontalRange[2] - horizontalRange[0];
        double ch = verticalRange[2] - verticalRange[0];

        wr = sw / cw;
        hr = sh / ch;

        g2d.setColor(new Color(235, 235, 235));
        g2d.fillRect(0, sh, sw, getHeight());

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(1));

        if(horizontalRange[1] != 0) {
            for (double l = horizontalRange[0]; l <= horizontalRange[2]; l += horizontalRange[1]) {
                g2d.drawLine(getScreenX(l), 0, getScreenX(l), sh);
            }
        }

        if(verticalRange[1] != 0) {
            for (double l = verticalRange[0]; l <= verticalRange[2]; l += verticalRange[1]) {
                g2d.drawLine(0, getScreenY(l), sw, getScreenY(l));
            }
        }

        g2d.setColor(Color.RED);

        int prevx = -1;
        int prevy = -1;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);


        for (int ind = left_index; ind <= right_index; ++ind) {
            int datax = getScreenX(data.get(ind).getKey() / 1000. - tcur);
            int datay = getScreenY(Double.parseDouble(String.valueOf(data.get(ind).getValue())));
//            g2d.fillOval(datax - 1, datay - 1, 2, 2);
            if(prevx != -1) {
                g2d.drawLine(prevx, prevy, datax, datay);
            }
            prevx = datax;
            prevy = datay;
        }

        if(verticalRange[1] != 0) {
            g2d.setColor(Color.BLACK);

            for (double l = verticalRange[0]; l <= verticalRange[2]; l += verticalRange[1]) {
                if (getScreenY(l) == 0) g2d.drawString(String.valueOf(l), 3, 12);
                else if (getScreenY(l) == sh - 1) g2d.drawString(String.valueOf(l), 3, sh - 2);
                else g2d.drawString(String.valueOf(l), 3, getScreenY(l) + 4);
            }
        }

        if(horizontalRange[1] != 0) {
            for (double l = horizontalRange[0]; l <= horizontalRange[2]; l += horizontalRange[1]) {
                String pr = String.valueOf(l);
                if(pr.length() > 6) pr = String.format("%.2f", l);
                int tw = (int) Math.ceil(g2d.getFont().getStringBounds(pr, g2d.getFontRenderContext()).getWidth());
                if (getScreenX(l) == 0) g2d.drawString(pr, 2, getHeight() - 2);
                else if (getScreenX(l) == sw - 1) g2d.drawString(pr, sw - tw, getHeight() - 2);
                else g2d.drawString(pr, getScreenX(l) - tw/2, getHeight() - 2);
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

    public double[] getArrayRange(ArrayList<AbstractMap.SimpleImmutableEntry<Long, Object>> array, int minR, int highR) {
        double[] ans = new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        for(int i = minR; i <= highR; ++i) {
            ans[0] = Math.min(ans[0], Double.parseDouble(String.valueOf((array.get(i).getValue()))));
            ans[1] = Math.max(ans[1], Double.parseDouble(String.valueOf((array.get(i).getValue()))));
        }
        return ans;
    }

    public int search(ArrayList<AbstractMap.SimpleImmutableEntry<Long, Object>> array, double value) {
        int start = 0, end = array.size() - 1;

        int ans = -1;
        while (start <= end) {
            int mid = (start + end) / 2;

            if (array.get(mid).getKey() < value) {
                start = mid + 1;
            } else {
                ans = mid;
                end = mid - 1;
            }
        }
        return ans;
    }

}
