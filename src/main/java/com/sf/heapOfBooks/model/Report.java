package com.sf.heapOfBooks.model;

public class Report {
	
	private Book book;
	
	private float price;
	
	private int bookSum;
	
	public Report () {}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime*result + ((book == null) ? 0 : book.hashCode());
		return 31 + book.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Report other = (Report) obj;
		if (book == null) {
			if (other.book != null)
				return false;
		} else if (!book.equals(other.book))
			return false;
		return true;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float f) {
		this.price = f;
	}

	public int getBookSum() {
		return bookSum;
	}

	public void setBookSum(int bookSum) {
		this.bookSum = bookSum;
	}
	
	

}
