package com.sf.heapOfBooks.model;

import java.time.LocalDate;
import java.util.List;

public class Shop {

	private Long id;
	private List<BoughtBook> boughtBooks;
	private float price;
	private LocalDate shoppingDate;
	private User user;
	private int bookSum;
	
	public Shop() {}
	
	public Shop(List<BoughtBook> boughtBooks, float price, LocalDate shoppingDate, User user, int bookSum) {
		this.boughtBooks = boughtBooks;
		this.price = price;
		this.shoppingDate = shoppingDate;
		this.user = user;
		this.bookSum = bookSum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<BoughtBook> getBoughtBooks() {
		return boughtBooks;
	}

	public void setBoughtBooks(List<BoughtBook> boughtBooks) {
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getBookSum() {
		return bookSum;
	}

	public void setBookSum(int bookSum) {
		this.bookSum = bookSum;
	}
}
