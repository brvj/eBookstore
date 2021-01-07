package com.sf.heapOfBooks.service;

import java.time.LocalDate;
import java.util.List;

import com.sf.heapOfBooks.model.Book;
import com.sf.heapOfBooks.model.enums.BookTypeEnum;
import com.sf.heapOfBooks.model.enums.LetterEnum;

public interface IBookService {

	 List<Book> findAll();
	
	 Book findOne(Long id);
	
	 void create(Book book);
	
	 void update(Book book);
	
	 void delete(Long id);
	
	 void orderCopiesOfBook(Book book);
	
	 List<Book> searchByCopiesFrom(int numberOfCopies);
	
	 List<Book> searchByCopiesTo(int numberOfCopies);
	
	 List<Book> searchByCopiesFromTo(int numberOfCopiesFrom, int numberOfCopiesTo);
	
	 List<Book> searchByNameOrPublisher(String search);
	
	 List<Book> searchByAuthors(String author);
	
	 List<Book> serachByReleaseDate(LocalDate releaseDate);
	
	 List<Book> serachByPriceFrom(int price);
	
	 List<Book> searchByPriceTo(int price);
	
	 List<Book> searchByPriceFromTo(int priceFrom, int price);
	
	 List<Book> searchByNumberOfPagesFrom(int pageNumber);
	
	 List<Book> searchByNumberOfPagesTo(int pageNumber);
	
	 List<Book> searchByNumberOfPagesFromTo(int pageNumberFrom, int pageNumberTo);
	
	 List<Book> searchByBookType(BookTypeEnum bookType);
	
	 List<Book> seacrhByLetter(LetterEnum letter);
	
	 List<Book> searchByLanguage(String language);
	
	 List<Book> searchByRatingFrom(float rating);
	
	 List<Book> searchByRatingTo(float rating);
	
	 List<Book> searchByRatingFromTo(float ratingFrom, float ratingTo);
	 
	 List<Book> searchByGenre(Long genreId);
	 
	 List<Book> orderByNameASC();
		
	 List<Book> orderByNameDESC();
		
	 List<Book> orderByPublisherASC();
		
	 List<Book> orderByPublisherDESC();

	 List<Book> orderByAuthorsASC();
		
	 List<Book> orderByAuthorsDESC();
		
	 List<Book> orderByReleaseDateASC();
		
	 List<Book> orderByReleaseDateDESC();

	 List<Book> orderByPriceASC();
		
	 List<Book> orderByPriceDESC();
		
	 List<Book> orderByNumberOfPagesASC();
		
	 List<Book> orderByNumberOfPagesDESC();

	 List<Book> orderByLanguageASC();
		
	 List<Book> orderByLanguageDESC();
		
	 List<Book> orderByRatingASC();
		
	 List<Book> orderByRatingDESC();
		
	 List<Book> orderByNumberOfCopiesASC();
		
	 List<Book> orderByNumberOfCopiesDESC();
}
