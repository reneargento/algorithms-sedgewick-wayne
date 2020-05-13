package chapter1.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by ektasingh151 on 5/14/20.
 */
public class Exer1_4_12 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] array1 = {-2, 1, 2, 2, 5, 6, 6, 8, 25};
        int[] array2 = {0, 1, 2, 2, 2, 3, 4, 5, 10, 20, 25};

        printElementsThatAppearInBothArrays(array1, array2);
	}

	static void printElementsThatAppearInBothArrays(int [] array1, int [] array2) {
		int array1Index = 0;
		int array2Index = 0;
		int recentMatchedValue = 0;
		int count=0;
		while(array1Index < array1.length && array2Index < array2.length) {
			if(array1[array1Index] < array2[array2Index])
				array1Index++;
			
			else if(array2[array2Index] < array1[array1Index])
				array2Index++;
			
			else {
				if(count == 0) {
					System.out.print(array1[array1Index] +" ");
					recentMatchedValue = array1[array1Index];
					count++;
				}
				else if(recentMatchedValue != array1[array1Index]) {
					System.out.print(array1[array1Index] +" ");
					recentMatchedValue = array1[array1Index];
				}
				
				
				array1Index++;
				array2Index++;
			}
		}
		
	}
}

}
