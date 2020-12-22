package com.sf.heapOfBooks.model;

import java.util.List;

public class ShoppingCart {

	private Long id;
	private List<BoughtBook> books;
	private User user;
	
	public ShoppingCart() {}
	
	public ShoppingCart(List<BoughtBook> books, User user) {
		this.books = books;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<BoughtBook> getBooks() {
		return books;
	}

	public void setBooks(List<BoughtBook> books) {
		this.books = books;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
