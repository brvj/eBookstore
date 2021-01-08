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
	public List<Book> searchByNameOrPublisher(String search) {
		return bookRepository.searchByNameOrPublisher(search);
	}

	@Override
	public List<Book> serachByPriceFrom(int price) {
		return bookRepository.serachByPriceFrom(price);
	}

	@Override
	public List<Book> searchByPriceTo(int price) {
		return bookRepository.searchByPriceTo(price);
	}

	@Override
	public List<Book> searchByPriceFromTo(int priceFrom, int priceTo) {
		return bookRepository.searchByPriceFromTo(priceFrom, priceTo);
	}

	@Override
	public List<Book> searchByGenre(Long id) {
		return bookRepository.searchByGenre(id);
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
}
