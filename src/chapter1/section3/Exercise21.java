package chapter1.section3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
// Thanks to Aaron-He (https://github.com/Aaron-He) for correcting the find() method signature.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/202
public class Exercise21 {
	
	public boolean find(LinkedList<String> linkedList, String key) {
		for (String item : linkedList) {
			if (item.equals(key)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		Exercise21 exercise21 = new Exercise21();
		LinkedList<String> linkedList = new LinkedList<>();
		linkedList.insert("A");
		linkedList.insert("B");
		linkedList.insert("C");
		linkedList.insert("D");
		
		StdOut.println("Find B result: " + exercise21.find(linkedList, "B") + " Expected: true");
		StdOut.println("Find Z result: " + exercise21.find(linkedList, "Z") + " Expected: false");
	}
}
