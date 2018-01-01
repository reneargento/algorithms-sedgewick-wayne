package chapter1.section1;

/**
 * Created by Rene Argento
 */
public class Exercise9 {
	
	public static void main(String[] args) {
		System.out.println(intToBinary(32));
	}
	
	private static String intToBinary(int n) {
		String result = "";
		
		while (n > 0) {
			result = n % 2 + result;
			
			n /= 2;
		}
		
		return result;
	}
	
}
