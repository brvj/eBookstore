package com.sf.heapOfBooks.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Shop {

	private Long id;
	private List<ShoppingCart> boughtBooks = new ArrayList<ShoppingCart>();
	private float price;
	private LocalDate shoppingDate;
	private int bookSum;
	
	public Shop() {}
	
	public Shop(List<ShoppingCart> boughtBooks, float price, LocalDate shoppingDate, int bookSum) {
		this.boughtBooks = boughtBooks;
		this.price = price;
		this.shoppingDate = shoppingDate;
		this.bookSum = bookSum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ShoppingCart> getBoughtBooks() {
		return boughtBooks;
	}

	public void setBoughtBooks(List<ShoppingCart> boughtBooks) {
		this.boughtBooks = boughtBooks;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public LocalDate getShoppingDate() {
		return shoppingDate;
	}

	public void setShoppingDate(LocalDate shoppingDate) {
		this.shoppingDate = shoppingDate;
	}

	public int getBookSum() {
		return bookSum;
	}

	public void setBookSum(int bookSum) {
		this.bookSum = bookSum;
	}
}
