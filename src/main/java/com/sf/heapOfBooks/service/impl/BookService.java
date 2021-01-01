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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void orderCopiesOfBook(Book book) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Book> searchByCopiesFrom(int numberOfCopies) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> searchByCopiesTo(int numberOfCopies) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> searchByCopiesFromTo(int numberOfCopiesFrom, int numberOfCopiesTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> searchByNameOrPublisher(String search) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> searchByAuthors(String author) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> serachByReleaseDate(LocalDate releaseDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> serachByPriceFrom(int price) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> searchByPriceTo(int price) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> searchByPriceFromTo(int priceFrom, int priceTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> searchByNumberOfPagesFrom(int pageNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> searchByNumberOfPagesTo(int pageNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> searchByNumberOfPagesFromTo(int pageNumberFrom, int pageNumberTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> searchByBookType(BookTypeEnum bookType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> seacrhByLetter(LetterEnum letter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> searchByLanguage(String language) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> searchByRatingFrom(float rating) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> searchByRatingTo(float rating) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> searchByRatingFromTo(float ratingFrom, float ratingTo) {
		// TODO Auto-generated method stub
		return null;
	}
}
