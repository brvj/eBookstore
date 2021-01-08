package com.sf.heapOfBooks.repository;

import java.util.List;
import com.sf.heapOfBooks.model.Book;

public interface IBookDAO {

	public List<Book> findAll();
	
	public Book findOne(Long id);
	
	public int create(Book book);
	
	public int update(Book book);
	
	public int delete(Long id);
	
	public int orderCopiesOfBook(Book book);
	
	public List<Book> searchByNameOrPublisher(String search);
	
	public List<Book> serachByPriceFrom(int price);
	
	public List<Book> searchByPriceTo(int price);
	
	public List<Book> searchByPriceFromTo(int priceFrom, int priceTo);
	
	public List<Book> searchByRatingFrom(float rating);
	
	public List<Book> searchByRatingTo(float rating);
	
	public List<Book> searchByRatingFromTo(float ratingFrom, float ratingTo);	
	
	public List<Book> searchByGenre(Long genreId);
	
	public List<Book> orderByNameASC();
	
	public List<Book> orderByNameDESC();

	public List<Book> orderByPriceASC();
	
	public List<Book> orderByPriceDESC();

	public List<Book> orderByLanguageASC();
	
	public List<Book> orderByLanguageDESC();
	
	public List<Book> orderByRatingASC();
	
	public List<Book> orderByRatingDESC();
}
