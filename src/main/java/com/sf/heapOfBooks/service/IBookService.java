package com.sf.heapOfBooks.service;

import java.util.List;

import com.sf.heapOfBooks.model.Book;

public interface IBookService {

	 List<Book> findAll();
	
	 Book findOne(Long id);
	
	 void create(Book book);
	
	 void update(Book book);
	
	 void delete(Long id);
	
	 void orderCopiesOfBook(Book book);
	
	 List<Book> searchByNameOrPublisher(String search);
	
	 List<Book> serachByPriceFrom(int price);
	
	 List<Book> searchByPriceTo(int price);
	
	 List<Book> searchByPriceFromTo(int priceFrom, int price);
	 
	 List<Book> searchByGenre(Long genreId);
	 
	 List<Book> orderByNameASC();
		
	 List<Book> orderByNameDESC();

	 List<Book> orderByPriceASC();
		
	 List<Book> orderByPriceDESC();

	 List<Book> orderByLanguageASC();
		
	 List<Book> orderByLanguageDESC();
		
	 List<Book> orderByRatingASC();
		
	 List<Book> orderByRatingDESC();
}
