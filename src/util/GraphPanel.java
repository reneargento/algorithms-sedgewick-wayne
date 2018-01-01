package util;

/**
 * Created by Rene Argento on 08/12/16.
 */
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GraphPanel extends JPanel {

    private Color lineColor = new Color(44, 102, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private static List<Double> values;
    private static final String ARIAL_FONT = "Arial";

    private static String xAxisLabel;
    private static String yAxisLabel;
    private static String windowTitle;
    private static String graphTitle;

    public GraphPanel(String graphTitle, String windowTitle, String xAxisLabel, String yAxisLabel, List<Integer> valuesInteger) {
        GraphPanel.graphTitle = graphTitle;
        GraphPanel.windowTitle = windowTitle;
        GraphPanel.xAxisLabel = xAxisLabel;
        GraphPanel.yAxisLabel = yAxisLabel;

        values = new ArrayList<>();

        for(Integer value : valuesInteger) {
            values.add((double) value);
        }
    }

    //extraUnusedParameter is only used to overload the constructor with a List<Double>
    public GraphPanel(String graphTitle, String windowTitle, String xAxisLabel, String yAxisLabel, List<Double> values, int extraUnusedParameter) {
        GraphPanel.graphTitle = graphTitle;
        GraphPanel.windowTitle = windowTitle;
        GraphPanel.xAxisLabel = xAxisLabel;
        GraphPanel.yAxisLabel = yAxisLabel;
        GraphPanel.values = values;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 25;
        int labelPadding = 25;
        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (values.size() - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - values.get(i)) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }

        // draw white background
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding,
                getHeight() - 2 * padding - labelPadding);
        graphics2D.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        int pointWidth = 4;
        int numberYDivisions = 10;
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight()
                    - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (values.size() > 0) {
                graphics2D.setColor(gridColor);
                graphics2D.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                graphics2D.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinScore()
                        + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = graphics2D.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                graphics2D.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            graphics2D.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < values.size(); i++) {
            if (values.size() > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (values.size() - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((values.size() / 20.0)) + 1)) == 0) {
                    graphics2D.setColor(gridColor);
                    graphics2D.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    graphics2D.setColor(Color.BLACK);
                    String xLabel = i + "";
                    FontMetrics metrics = graphics2D.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    graphics2D.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                graphics2D.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes
        graphics2D.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        graphics2D.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding,
                getHeight() - padding - labelPadding);

        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setColor(lineColor);
        graphics2D.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            graphics2D.drawLine(x1, y1, x2, y2);
        }

        graphics2D.setStroke(oldStroke);
        graphics2D.setColor(pointColor);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            graphics2D.fillOval(x, y, ovalW, ovalH);
        }
    }

    private double getMinScore() {
//        double minScore = Double.MAX_VALUE;
//        for (Integer score : values) {
//            minScore = Math.min(minScore, score);
//        }
        //Fixing minimum y value to zero
        return 0;
    }

    private double getMaxScore() {
        double maxScore = Double.MIN_VALUE;
        for (Double score : values) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    public void setValues(List<Double> values) {
        GraphPanel.values = values;
        invalidate();
        this.repaint();
    }

    public List<Double> getValues() {
        return values;
    }

    public void createAndShowGui() {
        MainPanel mainPanel = new MainPanel();
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame(windowTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    //Main changes underneath

    static class MainPanel extends JPanel {

        public MainPanel() {

            setLayout(new BorderLayout());

            JLabel title = new JLabel(graphTitle);
            title.setFont(new Font(ARIAL_FONT, Font.BOLD, 25));
            title.setHorizontalAlignment(JLabel.CENTER);

            JPanel graphPanel = new GraphPanel(graphTitle, windowTitle, xAxisLabel, yAxisLabel, values, 0);

            VerticalPanel verticalPanel = new VerticalPanel();

            HorizontalPanel horizontalPanel = new HorizontalPanel();

            add(title, BorderLayout.NORTH);
            add(horizontalPanel, BorderLayout.SOUTH);
            add(verticalPanel, BorderLayout.WEST);
            add(graphPanel, BorderLayout.CENTER);
        }

        class VerticalPanel extends JPanel {

            public VerticalPanel() {
                setPreferredSize(new Dimension(25, 0));
            }

            @Override
            public void paintComponent(Graphics graphics) {

                super.paintComponent(graphics);

                Graphics2D graphics2D = (Graphics2D) graphics;
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Font font = new Font(ARIAL_FONT, Font.PLAIN, 15);

                FontMetrics metrics = graphics.getFontMetrics(font);
                int width = metrics.stringWidth(yAxisLabel);

                graphics2D.setFont(font);

                drawRotate(graphics2D, getWidth(), (getHeight() + width) / 2, 270, yAxisLabel);
            }

            public void drawRotate(Graphics2D graphics2D, double x, double y, int angle, String text) {
                graphics2D.translate((float) x, (float) y);
                graphics2D.rotate(Math.toRadians(angle));
                graphics2D.drawString(text, 0, 0);
                graphics2D.rotate(-Math.toRadians(angle));
                graphics2D.translate(-(float) x, -(float) y);
            }
        }

        class HorizontalPanel extends JPanel {

            public HorizontalPanel() {
                setPreferredSize(new Dimension(0, 25));
            }

            @Override
            public void paintComponent(Graphics graphics) {

                super.paintComponent(graphics);

                Graphics2D graphics2D = (Graphics2D) graphics;
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Font font = new Font(ARIAL_FONT, Font.PLAIN, 15);

                FontMetrics metrics = graphics.getFontMetrics(font);
                int width = metrics.stringWidth(xAxisLabel);

                graphics2D.setFont(font);

                graphics2D.drawString(xAxisLabel, (getWidth() - width) / 2, 11);
            }
        }
    }
}