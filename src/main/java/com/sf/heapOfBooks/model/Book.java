package com.sf.heapOfBooks.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.sf.heapOfBooks.model.enums.BookTypeEnum;
import com.sf.heapOfBooks.model.enums.LetterEnum;

public class Book {
	
	private Long ISBN;
	private String name;
	private String publisher;
	private List<String> authors;
	private LocalDate releaseDate;
	private String shortDescription;
	private String coverPicture;
	private float price;
	private int numberOfPages;
	private BookTypeEnum bookType;
	private LetterEnum letter;
	private String bookLanguage;
	private float averageRating;
	private List<Genre> genre = new ArrayList<Genre>();
	private int numberOfCopies;
	
	public Book() {}
	
	public Book(String name, String publisher, List<String> authors, LocalDate releaseDate, String shortDescription, String coverPicture,
					float price, int numberOfPages, BookTypeEnum bookType, LetterEnum letter, String bookLanguage, int numberOfCopies) {
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
		this.numberOfCopies = numberOfCopies;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime*result + ((ISBN == null) ? 0 : ISBN.hashCode());
		return 31 + ISBN.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (ISBN == null) {
			if (other.ISBN != null)
				return false;
		} else if (!ISBN.equals(other.ISBN))
			return false;
		return true;
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

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
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

	public int getNumberOfCopies() {
		return numberOfCopies;
	}

	public void setNumberOfCopies(int numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}
}
