package com.sf.heapOfBooks.util;

public class PercentageUtil {

	public static float calculatePercentage(int points, float price) {
		int discount = points * 5;
		
		float percentage = price * discount / 100;
					
		return price - percentage;
	}	
	
	public static float claculatePercentage(int discount, float price) {
		float percentage = price * discount / 100;
		
		return price - percentage;
	}
}
