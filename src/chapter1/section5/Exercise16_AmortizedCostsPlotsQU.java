package chapter1.section5;

import util.GraphPanel;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 08/12/16.
 */
public class Exercise16_AmortizedCostsPlotsQU {

    private class QuickUnion {

        int[] id;
        int count;

        //Used for plotting amortized costs
        int operation;
        int currentCost;
        int totalCost;
        List<Integer> total;

        public QuickUnion(int size) {
            id = new int[size];
            count = size;

            operation = 1;
            currentCost = 0;
            totalCost = 0;
            total = new ArrayList<>();

            for(int i = 0; i < id.length; i++) {
                id[i] = i;
            }
        }

        public int count() {
            return count;
        }

        //O(1)
        public int find(int site) {
            currentCost++;

            while(site != id[site]) {
                currentCost++;

                site = id[site];
            }

            return site;
        }

        //O(1)
        public boolean connected(int site1, int site2) {
            boolean isConnected = find(site1) == find(site2);

            if (isConnected) {
                updateCostAnalysis();
            }

            return isConnected;
        }

        //O(n)
        public void union(int site1, int site2) {
            int leaderId1 = find(site1);
            int leaderId2 = find(site2);

            if (leaderId1 == leaderId2) {
                return;
            }

            id[leaderId1] = leaderId2;

            count--;

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

        int numberOfSites = 100;

        Exercise16_AmortizedCostsPlotsQU amortizedCostsPlots = new Exercise16_AmortizedCostsPlotsQU();
        QuickUnion quickUnion = amortizedCostsPlots.new QuickUnion(numberOfSites);

        for(int i = 0; i < 150; i++) {
            int randomSite1 = StdRandom.uniform(numberOfSites);
            int randomSite2 = StdRandom.uniform(numberOfSites);

            if (quickUnion.connected(randomSite1, randomSite2)) {
                continue;
            }

            quickUnion.union(randomSite1, randomSite2);
        }

        StdOut.println("Components: " + quickUnion.count);

        amortizedCostsPlots.draw(quickUnion);
    }

    private void draw(QuickUnion quickUnion) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GraphPanel graphPanel = new GraphPanel("QuickUnion", "Amortized Cost Plot",
                        "Number of Connections", "Number of Array Accesses", quickUnion.total);
                graphPanel.createAndShowGui();
            }
        });
    }

}
