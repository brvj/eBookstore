package com.sf.heapOfBooks.model;

public class ShoppingCart {

	private Long id;
	private Book book;
	private int numberOfCopies;
	private User user;
	
	public ShoppingCart() {}
	
	public ShoppingCart(Book book, User user) {
		this.book = book;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public int getNumberOfCopies() {
		return numberOfCopies;
	}

	public void setNumberOfCopies(int numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
