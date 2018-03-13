package chapter1.section2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento
 */
public class Exercise12 {
	
	private final int month;
	private final int day;
	private final int year;
	
	public Exercise12(int month, int day, int year) {
		
		if (!isDateValid(month, day, year)) {
			throw new RuntimeException("Invalid date!");
		}
		
		this.month = month;
		this.day = day;
		this.year = year;
	}
	
	public int month() {
		return month;
	}
	
	public int day() {
		return day;
	}
	
	public int year() {
		return year;
	}
	
	public String toString() {
		return month() + "/" + day() + "/" + year();
	}
	
	private boolean isDateValid(int month, int day, int year) {
		
		boolean valid = true;
		
		int[] maxNumberOfDaysPerMonth = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		
		if (year < 1 || month < 1 || month > 12 || day < 1 || day > maxNumberOfDaysPerMonth[month-1]) {
			valid = false;
		} 
		
		return valid;
	}
	
	private String dayOfTheWeek() {
		String[] days = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
		
		Calendar calendar = Calendar.getInstance();
		Date date;
		
		try {
			date = new SimpleDateFormat("MM/dd/yyyy").parse(this.toString());
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return days[dayOfWeek - 1];
	}
	
	public static void main(String[] args) {
		Exercise12 smartDate1 = new Exercise12(4, 18, 1989);
		StdOut.println(smartDate1.dayOfTheWeek() + " Expected: Tuesday");
		
		Exercise12 smartDate2 = new Exercise12(4, 19, 1989);
		StdOut.println(smartDate2.dayOfTheWeek() + " Expected: Wednesday");
		
		Exercise12 smartDate3 = new Exercise12(2, 28, 1996);
		StdOut.println(smartDate3.dayOfTheWeek() + " Expected: Wednesday");
	}

}
