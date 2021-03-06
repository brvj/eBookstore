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
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sf.heapOfBooks.model.Book;
import com.sf.heapOfBooks.model.Comment;
import com.sf.heapOfBooks.model.Genre;
import com.sf.heapOfBooks.model.Shop;
import com.sf.heapOfBooks.model.ShoppingCart;
import com.sf.heapOfBooks.model.SpecialDate;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.model.enums.BookTypeEnum;
import com.sf.heapOfBooks.model.enums.LetterEnum;
import com.sf.heapOfBooks.model.enums.UserEnum;
import com.sf.heapOfBooks.service.impl.BookService;
import com.sf.heapOfBooks.service.impl.CommentService;
import com.sf.heapOfBooks.service.impl.GenreService;
import com.sf.heapOfBooks.service.impl.ShopService;
import com.sf.heapOfBooks.service.impl.SpecialDateService;
import com.sf.heapOfBooks.util.PercentageUtil;

@Controller
@RequestMapping(value = "/Books")
public class BookController {
	
	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static String folder = "";
	private static List<Genre> gList = new ArrayList<Genre>();
	public static final String LOCAL_KEY = "localisation";

	@Autowired
	private BookService bookService;
	
	@Autowired
	private GenreService genreService;
	
	@Autowired
	private SpecialDateService sdService;
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private CommentService commentService;
	
	@PostConstruct
	private void imagesPath() {
		String path = "src/main/resources/static/images/";
		
		File file = new File(path);
		folder = file.getAbsolutePath();		
	}
	
	@GetMapping
	public ModelAndView init(
			@RequestParam(required = false) String search,
			@RequestParam(required = false) Integer priceFrom,
			@RequestParam(required = false) Integer priceTo,
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) Long isbn,
			HttpSession session) {
		

		
		ModelAndView returnBooks = new ModelAndView("books");
		List<Book> books = new ArrayList<Book>();
		List<SpecialDate> specialDateBooks = new ArrayList<SpecialDate>();
		LocalDate dateNow = LocalDate.now();
		
		for(SpecialDate sd : sdService.findAll()) {
			if(sd.getSpecialDate() != null) {
				if(sd.getSpecialDate().equals(dateNow))
					specialDateBooks.add(sd);
			}
		}
		
		
		for(SpecialDate sd : specialDateBooks) {
			float originalPrice = sd.getBook().getPrice();			
			sd.getBook().setPrice(PercentageUtil.claculatePercentage(sd.getDiscount(), originalPrice));
		}
		
		String message = "";
		
		if(search != null && search.trim().equals(""))
			search = null;
		
		if(isbn != null) {
			Book b = bookService.searchByISBN(isbn);
			if(b == null) {
				ModelAndView retMessage = new ModelAndView("message");
				
				message = "Ne postoji knjiga sa tim kriterijumima!";
				
				retMessage.addObject("message",	message);
				
				return retMessage;
			}
			
			books.add(b);
		}else {
			if(bookService.search(search, priceFrom, priceTo, id) == null) {
				ModelAndView retMessage = new ModelAndView("message");
				
				message = "Ne postoji knjiga sa tim kriterijumima!";
				
				retMessage.addObject("message",	message);
				
				return retMessage;
			}
			else {
				books = new ArrayList<Book>(bookService.search(search, priceFrom, priceTo, id));
			}
		}
					
		List<Genre> genres = genreService.findAll();		
			
		returnBooks.addObject("genres", genres);
		returnBooks.addObject("books", books);
		returnBooks.addObject("bookSpecialDate", specialDateBooks);
				
		return returnBooks;		
	}
	
	@GetMapping(value = "/Create")
	public ModelAndView create(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		String message = "";
		
		if(user == null || !user.getUserType().equals(UserEnum.Administrator)) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Nemate prava pristupa ovoj stranici!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		
		
		ModelAndView maw = new ModelAndView("createBookForm");
		
		List<Genre> genres = genreService.findAll();

		maw.addObject("genres", genres);
				
		return maw;
	}
	
	@PostMapping(value = "/Create")
	public ModelAndView create(
			@RequestParam Long ISBN,
			@RequestParam String name, 
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
		
		Book b = bookService.findOne(ISBN);
		if(b != null) {
			ModelAndView maw = new ModelAndView("message");
			String message = "Knjiga sa tim ISBN vec postoji!";
			return maw.addObject("message",message);			
		}
		
		
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
		book.setISBN(ISBN);
		book.setAverageRating(0);
			
		bookService.create(book);	
		gList.clear();
		response.sendRedirect("/HeapOfBooks/Books");
		return null;
	}
	
	@GetMapping(value = "/Details")
	public ModelAndView details(@RequestParam Long id, HttpServletRequest request) {
		ModelAndView maw = new ModelAndView("book");
		
		Book book = bookService.findOne(id);
		maw.addObject("book", book);
		
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		if(user != null) {
			List<Shop> shop = shopService.findAllForUser(user);
			List<Comment> comment = commentService.findAll();
			
			boolean gotShop = false;
			boolean gotComment = true;
			
			if(shop != null) {
				for(Shop s : shop) {
					for(ShoppingCart sc : s.getBoughtBooks()) {
						if(sc.getBook().getISBN().equals(id))
							gotShop = true;
					}
				}
			}
			
			if(comment != null) {
				for(Comment c : comment) {
					if(c.getBook().getISBN().equals(id) && c.getUser().getId().equals(user.getId()))
						gotComment = false;
				}
			}
			
			if(gotShop && gotComment)
				maw.addObject("canComment", true);
			
		}		
		return maw;
	}
	
	@GetMapping(value = "/Update")
	public ModelAndView update(@RequestParam Long id, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		String message = "";
		
		if(user == null || !user.getUserType().equals(UserEnum.Administrator)) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Nemate prava pristupa ovoj stranici!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
			
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
	public void update(
			@RequestParam String name, 
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
			@RequestParam Long ISBN, 
			HttpServletResponse response) throws IOException {
		
		Book book = bookService.findOne(ISBN);
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
	
	@GetMapping(value = "/Delete")
	public void delete(@RequestParam Long id, HttpServletResponse response) throws IOException {
		bookService.delete(id);
		
		response.sendRedirect("/HeapOfBooks/Books");
	}
	
	@GetMapping(value = "/Order")
	public ModelAndView order(@RequestParam Long id, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		String message = "";
		
		if(user == null || !user.getUserType().equals(UserEnum.Administrator)) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Nemate prava pristupa ovoj stranici!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		
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

		List<Genre> genres = genreService.findAll();		
		
		maw.addObject("genres", genres);
		maw.addObject("books", bookService.orderByNameASC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByNameDESC")
	public ModelAndView orderByNameDesc() {
		ModelAndView maw = new ModelAndView("books");

		List<Genre> genres = genreService.findAll();		
		
		maw.addObject("genres", genres);
		maw.addObject("books", bookService.orderByNameDESC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByPriceASC")
	public ModelAndView orderByPriceAsc() {
		ModelAndView maw = new ModelAndView("books");

		List<Genre> genres = genreService.findAll();		
		
		maw.addObject("genres", genres);
		maw.addObject("books", bookService.orderByPriceASC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByPriceDESC")
	public ModelAndView orderByPriceDesc() {
		ModelAndView maw = new ModelAndView("books");

		List<Genre> genres = genreService.findAll();		
		
		maw.addObject("genres", genres);
		maw.addObject("books", bookService.orderByPriceDESC());
		
		return maw;
	}

	@GetMapping(value = "/OrderByRatingASC")
	public ModelAndView orderByRatingAsc() {
		ModelAndView maw = new ModelAndView("books");
		
		maw.addObject("books", bookService.orderByRatingASC());

		List<Genre> genres = genreService.findAll();		
		
		maw.addObject("genres", genres);
		return maw;
	}
	
	@GetMapping(value = "/OrderByRatingDESC")
	public ModelAndView orderByRatingDesc() {
		ModelAndView maw = new ModelAndView("books");

		List<Genre> genres = genreService.findAll();		
		
		maw.addObject("genres", genres);
		maw.addObject("books", bookService.orderByRatingDESC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByLanguageDESC")
	public ModelAndView orderByLanguageDesc() {
		ModelAndView maw = new ModelAndView("books");

		List<Genre> genres = genreService.findAll();		
		
		maw.addObject("genres", genres);
		maw.addObject("books", bookService.orderByLanguageDESC());
		
		return maw;
	}
	
	@GetMapping(value = "/OrderByLanguageASC")
	public ModelAndView orderByLanguageAsc() {
		ModelAndView maw = new ModelAndView("books");
		
		List<Genre> genres = genreService.findAll();		
		
		maw.addObject("genres", genres);
		maw.addObject("books", bookService.orderByLanguageASC());
		
		return maw;
	}
	
	@GetMapping(value = "/ChangeLanguage")
	public void change(@RequestParam String lang, HttpSession session, HttpServletResponse response) throws IOException {
		
		Locale localisation = (Locale) session.getAttribute(LOCAL_KEY);
		
		if(lang.equals("sr"))
			localisation = Locale.forLanguageTag("sr");
		else if(lang.equals("en"))
			localisation = Locale.ENGLISH;
		
		session.setAttribute(LOCAL_KEY, localisation);	
		//jresponse.sendRedirect("/HeapOfBooks/Books");
	}
}
