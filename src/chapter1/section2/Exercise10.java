package chapter1.section2;

import edu.princeton.cs.algs4.StdDraw;

/**
 * Created by Rene Argento
 */
public class Exercise10 {
	
	private int n;
	private int max;
	
	private int totalOperations;
	private int counter;
	
	public Exercise10(int n, int max) {
		this.n = n;
		this.max = Math.abs(max);
		
		StdDraw.setCanvasSize(1024, 512);
		StdDraw.setPenRadius(.015);
		StdDraw.setXscale(0, n+1);
        StdDraw.setYscale(-max-3, max+3);
	}
	
	private void increment() {
		if (totalOperations < n && counter < max) {
			totalOperations++;
			counter++;
			
			plotCounter();
		}
	}

	private void decrement() {
		if (totalOperations < n && (Math.abs(counter) < max || counter == max)) {
			totalOperations++;
			counter--;
			
			plotCounter();
		}
	}
	
	private void plotCounter() {
		StdDraw.point(totalOperations, counter);
	}
	
	public static void main(String[] args) {
		Exercise10 visualCounter = new Exercise10(6, 4);
		
		visualCounter.increment();
		visualCounter.decrement();
		visualCounter.decrement();
		visualCounter.decrement();
		visualCounter.decrement();
		visualCounter.increment();
	}
	
}
