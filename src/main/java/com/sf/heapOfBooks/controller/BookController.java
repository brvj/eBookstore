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
			@RequestParam String genre, 
			@RequestParam BookTypeEnum bookType,
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
		List<Genre> genres = new ArrayList<Genre>();
		
		for(String s : auth) {
			authorsList.add(s);
		}
		String imageName = image.getOriginalFilename();
		
		Book book = new Book(name, publisher, authorsList, LocalDate.parse(releaseDate, formatterDate), shortDescription, 
				imageName, price, numberOfPages, bookType, letter, language, numberOfCopies);
		
		Genre g = genreService.findOne(Long.valueOf(genre));
		genres.add(g);
		book.setGenre(genres);
		book.setAverageRating(0);
			
		bookService.create(book);		
	}
	
	@GetMapping(value = "/Details")
	public ModelAndView details(@RequestParam Long id) {
		ModelAndView maw = new ModelAndView("book");
		
		Book book = bookService.findOne(id);
		maw.addObject("book", book);
		
		return maw;
	}
}
