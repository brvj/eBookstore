package com.sf.heapOfBooks.service;

import java.util.List;

import com.sf.heapOfBooks.model.Book;

public interface IBookService {

	 List<Book> findAll();
	
	 Book findOne(Long id);
	
	 void create(Book book);
	
	 void update(Book book);
	 
	 void updateRating(Book book);
	
	 void delete(Long id);
	
	 void orderCopiesOfBook(Book book);
	
	 List<Book> search(String search, Integer priceFrom, Integer priceTo, Long genreId);
	 
	 Book searchByISBN(Long isbn);
	 
	 List<Book> orderByNameASC();
		
	 List<Book> orderByNameDESC();

	 List<Book> orderByPriceASC();
		
	 List<Book> orderByPriceDESC();

	 List<Book> orderByLanguageASC();
		
	 List<Book> orderByLanguageDESC();
		
	 List<Book> orderByRatingASC();
		
	 List<Book> orderByRatingDESC();
}
