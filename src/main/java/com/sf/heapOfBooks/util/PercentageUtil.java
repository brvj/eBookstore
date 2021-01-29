package com.sf.heapOfBooks.util;

public class PercentageUtil {

	public static float calculatePercentage(int points, float price) {
		int discount = points * 5;
		
		float percentage = price * discount / 100;
					
		return price - percentage;
	}	
}
