package com.sf.heapOfBooks.repository;

import java.util.List;
import com.sf.heapOfBooks.model.Book;

public interface IBookDAO {

	public List<Book> findAll();
	
	public Book findOne(Long id);
	
	public int create(Book book);
	
	public int update(Book book);
	
	public int updateRating(Book book);
	
	public int delete(Long id);
	
	public int orderCopiesOfBook(Book book);
	
	public List<Book> search(String search, Integer priceFrom, Integer priceTo, Long genreId);
	
	public Book searchByISBN(Long isbn);
	
	public List<Book> orderByNameASC();
	
	public List<Book> orderByNameDESC();

	public List<Book> orderByPriceASC();
	
	public List<Book> orderByPriceDESC();

	public List<Book> orderByLanguageASC();
	
	public List<Book> orderByLanguageDESC();
	
	public List<Book> orderByRatingASC();
	
	public List<Book> orderByRatingDESC();
}
