package chapter1.section5;

import edu.princeton.cs.algs4.StdRandom;
import util.GraphPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 22/12/16.
 */
public class Exercise26_AmortizedPlotErdosRenyi {

    private class UnionFind implements UF {

        private int[] leaders;
        private int[] ranks;

        private int components;

        //Used for plotting amortized costs
        int operation;
        int currentCost;
        int totalCost;
        List<Integer> total;

        public UnionFind(int size) {
            leaders = new int[size];
            ranks = new int[size];
            components = size;

            operation = 1;
            currentCost = 0;
            totalCost = 0;
            total = new ArrayList<>();

            for(int i = 0; i < size; i++) {
                leaders[i]  = i;
                ranks[i] = 0;
            }
        }

        public int count() {
            return components;
        }

        public boolean connected(int site1, int site2) {

            boolean isConnected = find(site1) == find(site2);

            if (isConnected) {
                updateCostAnalysis();
            }

            return isConnected;
        }

        //O(inverse Ackermann function)
        public int find(int site) {
            currentCost++;

            if (site == leaders[site]) {
                return site;
            }

            currentCost++;

            return leaders[site] = find(leaders[site]);
        }

        //O(inverse Ackermann function)
        public void union(int site1, int site2) {

            int leader1 = find(site1);
            int leader2 = find(site2);

            if (leader1 == leader2) {
                return;
            }

            if (ranks[leader1] < ranks[leader2]) {
                leaders[leader1] = leader2;
            } else if (ranks[leader2] < ranks[leader1]) {
                leaders[leader2] = leader1;
            } else {
                leaders[leader1] = leader2;
                ranks[leader2]++;
            }

            components--;

            currentCost++;
            updateCostAnalysis();
        }

        private void updateCostAnalysis() {

            totalCost += currentCost;

            total.add(totalCost / operation);

            currentCost = 0;
            operation++;
        }
    }

    public static void main(String[] args) {
        int numberOfSites = Integer.parseInt(args[0]);

        Exercise26_AmortizedPlotErdosRenyi amortizedPlotErdosRenyi = new Exercise26_AmortizedPlotErdosRenyi();
        Exercise26_AmortizedPlotErdosRenyi.UnionFind unionFind = amortizedPlotErdosRenyi.new UnionFind(numberOfSites);

        amortizedPlotErdosRenyi.erdosRenyi(numberOfSites, unionFind);
        amortizedPlotErdosRenyi.draw(unionFind);
    }

    private void erdosRenyi(int numberOfSites, UF unionFind) {

        while(unionFind.count() != 1) {

            int randomSite1 = StdRandom.uniform(numberOfSites);
            int randomSite2 = StdRandom.uniform(numberOfSites);

            if (!unionFind.connected(randomSite1, randomSite2)) {
                unionFind.union(randomSite1, randomSite2);
            }
        }
    }

    private void draw(UnionFind unionFind) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GraphPanel graphPanel = new GraphPanel("Union Find", "Amortized Cost Plot for Erdos-Renyi",
                        "Number of Connections", "Number of Array Accesses", unionFind.total);
                graphPanel.createAndShowGui();
            }
        });
    }

}
