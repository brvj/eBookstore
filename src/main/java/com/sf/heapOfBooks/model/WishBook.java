package com.sf.heapOfBooks.model;

public class WishBook {

	private User user;
	private Book book;
	
	public WishBook() {}
	
	public WishBook(User user, Book book) {
		this.user = user;
		this.book = book;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	
	
}
