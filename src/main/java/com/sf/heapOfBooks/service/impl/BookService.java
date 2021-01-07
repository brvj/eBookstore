package com.sf.heapOfBooks.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sf.heapOfBooks.model.Book;
import com.sf.heapOfBooks.model.enums.BookTypeEnum;
import com.sf.heapOfBooks.model.enums.LetterEnum;
import com.sf.heapOfBooks.repository.impl.BookDAO;
import com.sf.heapOfBooks.service.IBookService;

@Service
public class BookService implements IBookService{

	@Autowired
	private BookDAO bookRepository;
	
	private Long newId(List<Long> ids) {	
		Long id = null;
		if(ids.isEmpty()) {
			id = 1000000000001L;
		}else {
			id = Collections.max(ids) + 1L;
		}
		return id;
	}
	
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
		
		List<Long> ids = new ArrayList<Long>();
		for(Book id : bookRepository.findAll()) {
			ids.add(id.getISBN());
		}
		
		book.setISBN(newId(ids));
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
	public List<Book> searchByCopiesFrom(int numberOfCopies) {
		return bookRepository.searchByCopiesFrom(numberOfCopies);
	}

	@Override
	public List<Book> searchByCopiesTo(int numberOfCopies) {
		return bookRepository.searchByCopiesTo(numberOfCopies);
	}

	@Override
	public List<Book> searchByCopiesFromTo(int numberOfCopiesFrom, int numberOfCopiesTo) {
		return bookRepository.searchByCopiesFromTo(numberOfCopiesFrom, numberOfCopiesTo);
	}

	@Override
	public List<Book> searchByNameOrPublisher(String search) {
		return bookRepository.searchByNameOrPublisher(search);
	}

	@Override
	public List<Book> searchByAuthors(String author) {
		return bookRepository.searchByAuthors(author);
	}

	@Override
	public List<Book> serachByReleaseDate(LocalDate releaseDate) {
		return bookRepository.serachByReleaseDate(releaseDate);
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
	public List<Book> searchByNumberOfPagesFrom(int pageNumber) {
		return bookRepository.searchByNumberOfPagesFrom(pageNumber);
	}

	@Override
	public List<Book> searchByNumberOfPagesTo(int pageNumber) {
		return bookRepository.searchByNumberOfPagesTo(pageNumber);
	}

	@Override
	public List<Book> searchByNumberOfPagesFromTo(int pageNumberFrom, int pageNumberTo) {
		return bookRepository.searchByNumberOfPagesFromTo(pageNumberFrom, pageNumberTo);
	}

	@Override
	public List<Book> searchByBookType(BookTypeEnum bookType) {
		return bookRepository.searchByBookType(bookType);
	}

	@Override
	public List<Book> seacrhByLetter(LetterEnum letter) {
		return bookRepository.seacrhByLetter(letter);
	}

	@Override
	public List<Book> searchByLanguage(String language) {
		return bookRepository.searchByLanguage(language);
	}

	@Override
	public List<Book> searchByRatingFrom(float rating) {
		return bookRepository.searchByRatingFrom(rating);
	}

	@Override
	public List<Book> searchByRatingTo(float rating) {
		return bookRepository.searchByRatingTo(rating);
	}

	@Override
	public List<Book> searchByRatingFromTo(float ratingFrom, float ratingTo) {
		return bookRepository.searchByRatingFromTo(ratingFrom, ratingTo);
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
	public List<Book> orderByPublisherASC() {
		return bookRepository.orderByPublisherASC();
	}

	@Override
	public List<Book> orderByPublisherDESC() {
		return bookRepository.orderByPublisherDESC();
	}

	@Override
	public List<Book> orderByAuthorsASC() {
		return bookRepository.orderByAuthorsASC();
	}

	@Override
	public List<Book> orderByAuthorsDESC() {
		return bookRepository.orderByAuthorsDESC();
	}

	@Override
	public List<Book> orderByReleaseDateASC() {
		return bookRepository.orderByReleaseDateASC();
	}

	@Override
	public List<Book> orderByReleaseDateDESC() {
		return bookRepository.orderByReleaseDateDESC();
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
	public List<Book> orderByNumberOfPagesASC() {
		return bookRepository.orderByNumberOfPagesASC();
	}

	@Override
	public List<Book> orderByNumberOfPagesDESC() {
		return bookRepository.orderByNumberOfPagesDESC();
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
	public List<Book> orderByNumberOfCopiesASC() {
		return bookRepository.orderByNumberOfCopiesASC();
	}

	@Override
	public List<Book> orderByNumberOfCopiesDESC() {
		return bookRepository.orderByNumberOfCopiesDESC();
	}	
}
