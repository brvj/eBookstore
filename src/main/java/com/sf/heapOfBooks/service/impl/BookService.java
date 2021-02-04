package com.sf.heapOfBooks.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sf.heapOfBooks.model.Book;
import com.sf.heapOfBooks.repository.impl.BookDAO;
import com.sf.heapOfBooks.service.IBookService;

@Service
public class BookService implements IBookService{

	@Autowired
	private BookDAO bookRepository;
	
	@Override
	public List<Book> findAll() {
		return bookRepository.findAll();
	}

	@Override
	public Book findOne(Long id) {
		return bookRepository.findOne(id);
	}

	@Override
	public void create(Book book) {
		bookRepository.create(book);
	}

	@Override
	public void update(Book book) {
		bookRepository.update(book);		
	}

	@Override
	public void delete(Long id) {
		bookRepository.delete(id);
	}

	@Override
	public void orderCopiesOfBook(Book book) {
		bookRepository.orderCopiesOfBook(book);
	}

	@Override
	public List<Book> orderByNameASC() {
		return bookRepository.orderByNameASC();
	}

	@Override
	public List<Book> orderByNameDESC() {
		return bookRepository.orderByNameDESC();
	}

	@Override
	public List<Book> orderByPriceASC() {
		return bookRepository.orderByPriceASC();
	}

	@Override
	public List<Book> orderByPriceDESC() {
		return bookRepository.orderByPriceDESC();
	}

	@Override
	public List<Book> orderByLanguageASC() {
		return bookRepository.orderByLanguageASC();
	}

	@Override
	public List<Book> orderByLanguageDESC() {
		return bookRepository.orderByLanguageDESC();
	}

	@Override
	public List<Book> orderByRatingASC() {
		return bookRepository.orderByRatingASC();
	}

	@Override
	public List<Book> orderByRatingDESC() {
		return bookRepository.orderByRatingDESC();
	}

	@Override
	public Book searchByISBN(Long isbn) {
		return bookRepository.searchByISBN(isbn);
	}

	@Override
	public List<Book> search(String search, Integer priceFrom, Integer priceTo, Long genreId) {
		return bookRepository.search(search, priceFrom, priceTo, genreId);
	}

	@Override
	public void updateRating(Book book) {
		bookRepository.updateRating(book);		
	}	
}
