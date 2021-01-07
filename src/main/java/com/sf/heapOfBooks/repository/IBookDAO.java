package com.sf.heapOfBooks.repository;

import java.time.LocalDate;
import java.util.List;

import com.sf.heapOfBooks.model.Book;
import com.sf.heapOfBooks.model.enums.BookTypeEnum;
import com.sf.heapOfBooks.model.enums.LetterEnum;

public interface IBookDAO {

	public List<Book> findAll();
	
	public Book findOne(Long id);
	
	public int create(Book book);
	
	public int update(Book book);
	
	public int delete(Long id);
	
	public int orderCopiesOfBook(Book book);
	
	public List<Book> searchByCopiesFrom(int numberOfCopies);
	
	public List<Book> searchByCopiesTo(int numberOfCopies);
	
	public List<Book> searchByCopiesFromTo(int numberOfCopiesFrom, int numberOfCopiesTo);
	
	public List<Book> searchByNameOrPublisher(String search);
	
	public List<Book> searchByAuthors(String author);
	
	public List<Book> serachByReleaseDate(LocalDate releaseDate);
	
	public List<Book> serachByPriceFrom(int price);
	
	public List<Book> searchByPriceTo(int price);
	
	public List<Book> searchByPriceFromTo(int priceFrom, int priceTo);
	
	public List<Book> searchByNumberOfPagesFrom(int pageNumber);
	
	public List<Book> searchByNumberOfPagesTo(int pageNumber);
	
	public List<Book> searchByNumberOfPagesFromTo(int pageNumberFrom, int pageNumberTo);
	
	public List<Book> searchByBookType(BookTypeEnum bookType);
	
	public List<Book> seacrhByLetter(LetterEnum letter);
	
	public List<Book> searchByLanguage(String language);
	
	public List<Book> searchByRatingFrom(float rating);
	
	public List<Book> searchByRatingTo(float rating);
	
	public List<Book> searchByRatingFromTo(float ratingFrom, float ratingTo);	
	
	public List<Book> searchByGenre(Long genreId);
	
	public List<Book> orderByNameASC();
	
	public List<Book> orderByNameDESC();
	
	public List<Book> orderByPublisherASC();
	
	public List<Book> orderByPublisherDESC();

	public List<Book> orderByAuthorsASC();
	
	public List<Book> orderByAuthorsDESC();
	
	public List<Book> orderByReleaseDateASC();
	
	public List<Book> orderByReleaseDateDESC();

	public List<Book> orderByPriceASC();
	
	public List<Book> orderByPriceDESC();
	
	public List<Book> orderByNumberOfPagesASC();
	
	public List<Book> orderByNumberOfPagesDESC();

	public List<Book> orderByLanguageASC();
	
	public List<Book> orderByLanguageDESC();
	
	public List<Book> orderByRatingASC();
	
	public List<Book> orderByRatingDESC();
	
	public List<Book> orderByNumberOfCopiesASC();
	
	public List<Book> orderByNumberOfCopiesDESC();
}
