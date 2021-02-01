package com.sf.heapOfBooks.model;

import java.time.LocalDate;

public class SpecialDate {
	
	private Long id;
	private Book book;	
	private int discount;	
	private LocalDate specialDate;
	
	public SpecialDate (){}

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

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public LocalDate getSpecialDate() {
		return specialDate;
	}

	public void setSpecialDate(LocalDate specialDate) {
		this.specialDate = specialDate;
	}
	
	
}
