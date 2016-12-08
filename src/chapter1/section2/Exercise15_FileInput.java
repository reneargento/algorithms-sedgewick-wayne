package chapter1.section2;

import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Exercise15_FileInput {
	
	public static void main(String...args) {
		String filePath = "/Users/rene/Desktop/Algorithms/Books/Algorithms, 4th ed. - Exercises/Data/intsFile.txt";
		int[] ints = readAllInts(filePath);
		
		for(int i : ints){
			StdOut.print(i + " ");
		}
	}
	
	public static int[] readAllInts(String fileName) {
		In in = new In(fileName);
		String input = in.readAll();
		
		String[] inputs = input.split("\\s+");
		List<Integer> intList = new LinkedList<>();
		
		for (int i=0; i< inputs.length; i++) {
			try {
				int number = Integer.parseInt(inputs[i]);
				intList.add(number);
			} catch (NumberFormatException e) {
				//We only care about ints
			}
		}
		
		int[] ints = new int[intList.size()];
		
		for (int i=0; i<intList.size(); i++){
			ints[i] = intList.get(i);
		}
		
		return ints;
	}

}
