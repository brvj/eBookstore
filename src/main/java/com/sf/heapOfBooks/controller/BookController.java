package com.sf.heapOfBooks.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sf.heapOfBooks.model.Book;
import com.sf.heapOfBooks.model.Genre;
import com.sf.heapOfBooks.model.enums.BookTypeEnum;
import com.sf.heapOfBooks.model.enums.LetterEnum;
import com.sf.heapOfBooks.service.impl.BookService;
import com.sf.heapOfBooks.service.impl.GenreService;

@Controller
@RequestMapping(value = "/Books")
public class BookController {
	
	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static String folder = "";
	private static List<Genre> gList = new ArrayList<Genre>();
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private GenreService genreService;
	
	@PostConstruct
	private void imagesPath() {
		String path = "src/main/resources/static/images/";
		
		File file = new File(path);
		folder = file.getAbsolutePath();		
	}
	
	@GetMapping
	public ModelAndView init(
			@RequestParam(required = false) Integer numberOfCopiesFrom,
			@RequestParam(required = false) Integer numberOfCopiesTo,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String author,
			@RequestParam(required = false) String releaseDate,
			@RequestParam(required = false) Integer priceFrom,
			@RequestParam(required = false) Integer priceTo,
			@RequestParam(required = false) Integer pageNumFrom,
			@RequestParam(required = false) Integer pageNumTo,
			@RequestParam(required = false) BookTypeEnum bookType,
			@RequestParam(required = false) LetterEnum letter,
			@RequestParam(required = false) String language,
			@RequestParam(required = false) Integer ratingFrom,
			@RequestParam(required = false) Integer ratingTo,
			@RequestParam(required = false) Long id) {
		List<Book> books = bookService.findAll();	
		List<Genre> genres = genreService.findAll();
		
		ModelAndView returnBooks = new ModelAndView("books");
		returnBooks.addObject("genres", genres);
		List<Book> bookFilter = new ArrayList<Book>();
		
		if(numberOfCopiesFrom != null) {
			bookFilter.addAll(bookService.searchByCopiesFrom(numberOfCopiesFrom));
		}
		if(numberOfCopiesTo != null) {
			bookFilter.addAll(bookService.searchByCopiesTo(numberOfCopiesTo));
		}
		if(numberOfCopiesFrom != null && numberOfCopiesTo != null) {
			bookFilter.addAll(bookService.searchByCopiesFromTo(numberOfCopiesFrom,numberOfCopiesTo));
		}
		if(search != null) {
			bookFilter.addAll(bookService.searchByNameOrPublisher(search));
		}
		if(author != null) {
			bookFilter.addAll(bookService.searchByAuthors(author));
		}
		if(releaseDate != null && releaseDate != "") {
			bookFilter.addAll(bookService.serachByReleaseDate(LocalDate.parse(releaseDate, formatterDate)));
		}
		if(priceFrom != null) {
			bookFilter.addAll(bookService.serachByPriceFrom(priceFrom));
		}
		if(priceTo != null) {
			bookFilter.addAll(bookService.searchByPriceTo(priceTo));
		}
		if(priceTo != null && priceFrom != null) {
			bookFilter.addAll(bookService.searchByPriceFromTo(priceFrom, priceTo));
		}
		if(pageNumTo != null) {
			bookFilter.addAll(bookService.searchByNumberOfPagesTo(pageNumTo));
		}
		if(pageNumFrom != null) {
			bookFilter.addAll(bookService.searchByNumberOfPagesFrom(pageNumFrom));
		}
		if(pageNumTo != null && pageNumFrom != null) {
			bookFilter.addAll(bookService.searchByNumberOfPagesFromTo(pageNumFrom, pageNumTo));
		}
		if(bookType != null) {
			bookFilter.addAll(bookService.searchByBookType(bookType));
		}
		if(letter != null) {
			bookFilter.addAll(bookService.seacrhByLetter(letter));
		}
		if(language != null) {
			bookFilter.addAll(bookService.searchByLanguage(language));
		}
		if(ratingFrom != null) {
			bookFilter.addAll(bookService.searchByRatingFrom(ratingFrom));
		}
		if(ratingTo != null) {
			bookFilter.addAll(bookService.searchByRatingTo(ratingTo));
		}
		if(ratingFrom != null && ratingTo != null) {
			bookFilter.addAll(bookService.searchByRatingFromTo(ratingFrom,ratingTo));
		}
		if(id != null) {
			bookFilter.addAll(bookService.searchByGenre(id));
		}
		
				
		if(numberOfCopiesTo == null && numberOfCopiesFrom == null && search == null &&
				author == null && releaseDate == null && priceTo == null && priceFrom == null &&
				pageNumFrom == null && pageNumTo == null && bookType == null && letter == null &&
				language == null && ratingFrom == null && ratingTo == null && id == null)
			return returnBooks.addObject("books", books);
		
		List<Book> removeDuplicates = bookFilter.stream().distinct().collect(Collectors.toList());

		returnBooks.addObject("books", removeDuplicates);
		
		return returnBooks;		
	}
	
	@GetMapping(value = "/Create")
	public ModelAndView create() {
		ModelAndView maw = new ModelAndView("createBookForm");
		
		List<Genre> genres = genreService.findAll();

		maw.addObject("genres", genres);
				
		return maw;
	}
	
	@PostMapping(value = "/Create")
	public void create(@RequestParam String name, 
			@RequestParam String publisher, 
			@RequestParam String authors,
			@RequestParam String releaseDate,
			@RequestParam String shortDescription, 
			@RequestParam float price, 
			@RequestParam int numberOfPages, 
			@RequestParam BookTypeEnum bookType,
			@RequestParam String[] genre,
			@RequestParam LetterEnum letter, 
			@RequestParam String language, 
			@RequestParam int numberOfCopies, 
			@RequestParam MultipartFile image, 
			HttpServletResponse response) throws IOException {
				
		byte[] bytes = image.getBytes();
		Path path = Paths.get(folder + "\\" + image.getOriginalFilename());
		Files.write(path, bytes);
				
		String[] auth = authors.split(",");
		List<String> authorsList = new ArrayList<String>();
		
		for(String s : auth) {
			authorsList.add(s);
		}
		
		for(String s : genre) {
			gList.add(genreService.findOne(Long.valueOf(s)));
		}
		
		Book book = new Book(name, publisher, authorsList, LocalDate.parse(releaseDate, formatterDate), shortDescription, 
				image.getOriginalFilename(), price, numberOfPages, bookType, letter, language, numberOfCopies);
		
		book.setGenre(gList);
		book.setAverageRating(0);
			
		bookService.create(book);	
		gList.clear();
		response.sendRedirect("/HeapOfBooks/Books");
	}
	
	@GetMapping(value = "/Details")
	public ModelAndView details(@RequestParam Long id) {
		ModelAndView maw = new ModelAndView("book");
		
		Book book = bookService.findOne(id);
		maw.addObject("book", book);
		
		return maw;
	}
	
	@GetMapping(value = "/Update")
	public ModelAndView update(@RequestParam Long id) {
		ModelAndView maw = new ModelAndView("updateBookForm");
		
		Book book = bookService.findOne(id);
		List<Genre> genres = genreService.findAll();

		String authors="";
		for(String s : book.getAuthors()) {
			authors += s + ",";
		}
		StringBuffer sb= new StringBuffer(authors);
		
		maw.addObject("genres", genres);
		maw.addObject("book", book);
		maw.addObject("authors", sb.deleteCharAt(sb.length()-1));
		
		return maw;
	}
	
	@PostMapping(value = "/Update")
	public void update(@RequestParam String name, 
			@RequestParam String publisher, 
			@RequestParam String authors,
			@RequestParam String releaseDate,
			@RequestParam String shortDescription, 
			@RequestParam float price, 
			@RequestParam int numberOfPages, 
			@RequestParam BookTypeEnum bookType,
			@RequestParam String[] genre,
			@RequestParam LetterEnum letter, 
			@RequestParam String language, 
			@RequestParam Long id, 
			HttpServletResponse response) throws IOException {
		
		Book book = bookService.findOne(id);
		book.setName(name);
		book.setPublisher(publisher);
		List<String> authorsList = new ArrayList<String>();
		String[] auth = authors.split(",");
		for(String s : auth) {
			authorsList.add(s);
		}
		book.setAuthors(authorsList);
		book.setReleaseDate(LocalDate.parse(releaseDate, formatterDate));
		book.setShortDescription(shortDescription);
		book.setPrice(price);
		book.setNumberOfPages(numberOfPages);
		book.setBookType(bookType);
		for(String s : genre) {
			gList.add(genreService.findOne(Long.valueOf(s)));
		}
		book.setGenre(gList);
		book.setLetter(letter);
		book.setBookLanguage(language);
		
		bookService.update(book);
		gList.clear();
		
		response.sendRedirect("/HeapOfBooks/Books");
	}
	
	@GetMapping(value = "/Order")
	public ModelAndView order(@RequestParam Long id) {
		ModelAndView maw = new ModelAndView("order");
		
		maw.addObject("book", bookService.findOne(id));
		
		return maw;
	}
	
	@PostMapping(value = "/Order")
	public void order(@RequestParam Long id, @RequestParam int numberOfCopies, HttpServletResponse response) throws IOException {
		Book book = bookService.findOne(id);
		
		book.setNumberOfCopies(book.getNumberOfCopies() + numberOfCopies);
		bookService.orderCopiesOfBook(book);
		
		response.sendRedirect("/HeapOfBooks/Books");
	}
	
	@GetMapping(value = "/OrderByNameASC")
	public ModelAndView orderByNameAsc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByNameASC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByNameDESC")
	public ModelAndView orderByNameDesc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByNameDESC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByPublisherASC")
	public ModelAndView orderByPublisherAsc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByPublisherASC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByPublisherDESC")
	public ModelAndView orderByPublisherDesc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByPublisherDESC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByAuthorsASC")
	public ModelAndView orderByAuthorsAsc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByAuthorsASC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByAuthorsDESC")
	public ModelAndView orderByAuthorsDesc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByAuthorsDESC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByDateASC")
	public ModelAndView orderByDateAsc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByReleaseDateASC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByDateDESC")
	public ModelAndView orderByDateDesc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByReleaseDateDESC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByPriceASC")
	public ModelAndView orderByPriceAsc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByPriceASC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByPriceDESC")
	public ModelAndView orderByPriceDesc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByPriceDESC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByNumPagesASC")
	public ModelAndView orderByNumPagesAsc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByNumberOfPagesASC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByNumPagesDESC")
	public ModelAndView orderByNumPagesDesc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByNumberOfPagesDESC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByLanguageASC")
	public ModelAndView orderByLanguageAsc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByLanguageASC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByLanguageDESC")
	public ModelAndView orderByLanguageDesc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByLanguageDESC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByRatingASC")
	public ModelAndView orderByRatingAsc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByRatingASC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByRatingDESC")
	public ModelAndView orderByRatingDesc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByRatingDESC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByNumCopiesASC")
	public ModelAndView orderByNumCopiesAsc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByNumberOfCopiesASC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByNumCopiesDESC")
	public ModelAndView orderByNumCopiesDesc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByNumberOfCopiesDESC());
		
		return maw;
	}
}
