package chapter1.section1;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento
 */
public class Exercise31_RandomConnections {

	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]); //5
		double p = Double.parseDouble(args[1]); //0.66

		draw(n, p);
	}
	
	private static void draw(int N, double p) {
		
		StdDraw.setCanvasSize(1024, 1024);
        StdDraw.setScale(-1.0, 1.0);
        StdDraw.setPenRadius(.015);
		
		double[][] d = new double[N][2];
        for (int i = 0; i < N; i++) {
            d[i][0] = Math.cos(2 * Math.PI * i / N);
            d[i][1] = Math.sin(2 * Math.PI * i / N);
            StdDraw.point(d[i][0], d[i][1]);
        }
        
        StdDraw.setPenRadius();
        
        for (int i = 0; i < N - 1; i++) {
        	for (int j = i + 1; j < N; j++) {
        		if (StdRandom.bernoulli(p)) {
        			StdDraw.line(d[i][0], d[i][1], d[j][0], d[j][1]);
        		}
        	}
        }
	}

}
