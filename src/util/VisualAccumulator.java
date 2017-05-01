package util;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

/**
 * Created by rene on 01/05/17.
 */
public class VisualAccumulator {

    private static final String HELVETICA_FONT = "Helvetica";
    private double total;
    private int size;

    public VisualAccumulator(int originValue, double maxX, double maxY, String title, String xAxisLabel, String yAxisLabel) {
        StdDraw.setXscale(-(maxX * 0.05), maxX + (maxX * 0.05));
        StdDraw.setYscale(-(maxY * 0.05), maxY + (maxY * 0.05));
        StdDraw.setPenRadius(.005);

        drawLabels(originValue, maxX, maxY, title, xAxisLabel, yAxisLabel);
    }

    private void drawLabels(int originValue, double maxX, double maxY, String title, String xAxisLabel, String yAxisLabel) {
        Font font = new Font(HELVETICA_FONT, Font.BOLD, 12);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.RED);

        //X axis label
        double xAxisLabelHeight = -(maxY * 0.025);

        StdDraw.text(maxX / 2, xAxisLabelHeight, xAxisLabel);
        //Y axis label
        StdDraw.text(-(maxY * 0.025), maxY / 2, yAxisLabel, 90);

        StdDraw.setPenColor(StdDraw.BLACK);
        //Title
        StdDraw.text(maxX / 2, maxY - (maxY * 0.02), title);

        Font font2 = new Font(HELVETICA_FONT, Font.PLAIN, 12);
        StdDraw.setFont(font2);

        StdDraw.text(-(maxX * 0.01), xAxisLabelHeight, String.valueOf(originValue));
        //X axis label
        StdDraw.text(-(maxX * 0.005), maxY - (maxY * 0.07), String.valueOf((int) maxY));
        //Y axis label
        StdDraw.text(maxX - (maxX * 0.06), xAxisLabelHeight, String.valueOf((int) maxX));
    }

    public void addDataValue(double value) {
        size++;
        total += value;

        StdDraw.setPenColor(StdDraw.DARK_GRAY);
        StdDraw.point(size, value);

        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.point(size, mean());
    }

    private double mean() {
        return total / size;
    }

    public void writeFinalMean() {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(size + (size * 0.04), mean(), String.valueOf((int) mean()));
    }

    public String toString() {
        return "Mean (" + size + " values): " + String.format("%7.5f", mean());
    }

}
