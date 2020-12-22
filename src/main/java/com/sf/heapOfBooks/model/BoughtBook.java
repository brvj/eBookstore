package com.sf.heapOfBooks.model;

public class BoughtBook {

	private Long id;
	private Book book;
	private int numberOfBooks;
	private float price;
	
	public BoughtBook() {}
	
	public BoughtBook(Book book, int numberOfBooks) {
		this.book = book;
		this.numberOfBooks = numberOfBooks;
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

	public int getNumberOfBooks() {
		return numberOfBooks;
	}

	public void setNumberOfBooks(int numberOfBooks) {
		this.numberOfBooks = numberOfBooks;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
}
