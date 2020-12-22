package com.sf.heapOfBooks.model;

import java.util.List;

import com.sf.heapOfBooks.model.enums.BookTypeEnum;
import com.sf.heapOfBooks.model.enums.LetterEnum;

public class Book {
	
	private Long ISBN;
	private String name;
	private String publisher;
	private List<String> authors;
	private String releaseDate;
	private String shortDescription;
	private String coverPicture;
	private float price;
	private int numberOfPages;
	private BookTypeEnum bookType;
	private LetterEnum letter;
	private String bookLanguage;
	private float averageRating;
	private List<Genre> genre;
	
	public Book() {}
	
	public Book(String name, String publisher, List<String> authors, String releaseDate, String shortDescription, String coverPicture,
					float price, int numberOfPages, BookTypeEnum bookType, LetterEnum letter, String bookLanguage, List<Genre> genre) {
		this.name = name;
		this.publisher = publisher;
		this.authors = authors;
		this.releaseDate = releaseDate;
		this.shortDescription = shortDescription;
		this.coverPicture = coverPicture;
		this.price = price;
		this.numberOfPages = numberOfPages;
		this.bookType = bookType;
		this.letter = letter;
		this.bookLanguage = bookLanguage;
		this.genre = genre;
	}

	public Long getISBN() {
		return ISBN;
	}

	public void setISBN(Long iSBN) {
		ISBN = iSBN;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getCoverPicture() {
		return coverPicture;
	}

	public void setCoverPicture(String coverPicture) {
		this.coverPicture = coverPicture;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public BookTypeEnum getBookType() {
		return bookType;
	}

	public void setBookType(BookTypeEnum bookType) {
		this.bookType = bookType;
	}

	public LetterEnum getLetter() {
		return letter;
	}

	public void setLetter(LetterEnum letter) {
		this.letter = letter;
	}

	public String getBookLanguage() {
		return bookLanguage;
	}

	public void setBookLanguage(String bookLanguage) {
		this.bookLanguage = bookLanguage;
	}

	public float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}

	public List<Genre> getGenre() {
		return genre;
	}

	public void setGenre(List<Genre> genre) {
		this.genre = genre;
	}
}
