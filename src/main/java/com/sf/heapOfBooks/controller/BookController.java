package com.sf.heapOfBooks.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
	private static final String folder = "C:\\Users\\Boris\\Desktop\\Fakultet\\Semestar I\\Osnove WEB programiranja\\Projekat\\heapOfBooks\\src\\main\\resources\\static\\images\\";
	private static List<Genre> gList = new ArrayList<Genre>();
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private GenreService genreService;
	
	@GetMapping
	public ModelAndView init() {
		List<Book> books = bookService.findAll();
		
		ModelAndView returnBooks = new ModelAndView("books");
		returnBooks.addObject("books", books);
		
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
		Path path = Paths.get(folder + image.getOriginalFilename());
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
}
