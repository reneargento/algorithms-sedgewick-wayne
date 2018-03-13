package chapter1.section5;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rene Argento on 08/12/16.
 */
public class Exercise19_Animation {

    private class SiteLocation {
        double x;
        double y;

        SiteLocation(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private Map<Integer, SiteLocation> sitesLocation = new HashMap<>();

    public static void main(String[] args) {
        new Exercise19_Animation().animation();
    }

    private void animation() {
        int numberOfSites = 16;

        Exercise18_RandomGridGenerator randomGridGenerator = new Exercise18_RandomGridGenerator();
        Exercise18_RandomGridGenerator.Connection[] connections = randomGridGenerator.generate(numberOfSites);

        StdDraw.setCanvasSize(500, 500);
        StdDraw.setXscale(-1, numberOfSites);
        StdDraw.setYscale(-0.5, numberOfSites/2 + 1);

        StdDraw.setPenRadius(.015);

        drawSites(numberOfSites);

        StdDraw.setPenRadius();
        StdDraw.setPenColor(Color.red);

        UnionFind unionFind = new UnionFind(numberOfSites);

        for(int i = 0; i < 10; i++) {
            if (unionFind.connected(connections[i].p, connections[i].q)) {
                continue;
            }

            unionFind.union(connections[i].p, connections[i].q);
            drawConnection(connections[i].p, connections[i].q);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void drawSites(int numberOfSites) {

        int numberOfColumns = (int) Math.ceil(Math.sqrt(numberOfSites));

        int distanceX;
        int distanceY = 0;
        int distanceIncrement = 1;

        int sitesDrawn = 0;

        for(int i = 0; i < numberOfColumns; i++) {
            distanceX = 0;

            for(int j = 0; j < numberOfColumns; j++) {
                double x = j + distanceX;
                double y = i + distanceY;

                int siteValue = calculateSiteValue(i, j, numberOfSites);
                SiteLocation siteLocation = new SiteLocation(x, y);

                sitesLocation.put(siteValue, siteLocation);
                StdDraw.point(x, y);

                sitesDrawn++;
                if (sitesDrawn == numberOfSites) {
                    break;
                }

                distanceX += distanceIncrement;
            }

            if (sitesDrawn == numberOfSites) {
                break;
            }
            distanceY += distanceIncrement;
        }
    }

    private void drawConnection(int site1, int site2) {
        SiteLocation siteLocation1 = sitesLocation.get(site1);
        SiteLocation siteLocation2 = sitesLocation.get(site2);

        StdDraw.line(siteLocation1.x, siteLocation1.y, siteLocation2.x, siteLocation2.y);
    }

    private int calculateSiteValue(int indexI, int indexJ, int numberOfSites) {
        int rowSize = (int) Math.ceil(Math.sqrt(numberOfSites));

        return (indexI * rowSize) + indexJ;
    }
}
