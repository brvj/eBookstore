package com.sf.heapOfBooks.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sf.heapOfBooks.model.Book;
import com.sf.heapOfBooks.model.Genre;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.model.enums.UserEnum;
import com.sf.heapOfBooks.service.impl.BookService;
import com.sf.heapOfBooks.service.impl.GenreService;

@Controller
@RequestMapping(value = "/Genres")
public class GenreController {

	@Autowired
	private GenreService genreService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private ServletContext servletContext;
	private String baseURL;
	
	@PostConstruct
	public void init() {
		baseURL = servletContext.getContextPath() + "/";
	}
	
	@GetMapping
	public ModelAndView index() {
		List<Genre> genres = genreService.findAll();
		
		ModelAndView returnGenres = new ModelAndView("genres");
		returnGenres.addObject("genres", genres);
		
		return returnGenres;
	}
	
	@GetMapping(value = "/Create")
	public ModelAndView createGenre(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		String message = "";
		
		if(user == null || !user.getUserType().equals(UserEnum.Administrator)) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Nemate prava pristupa ovoj stranici!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		ModelAndView maw = new ModelAndView("createGenreForm");
		
		return maw;
	}
	
	@PostMapping(value = "/Create")
	public ModelAndView createGenre(@RequestParam String name, @RequestParam String description, HttpServletResponse response) throws IOException {
		Genre genre = new Genre();
		genre.setName(name);
		genre.setDescription(description);
		
		String message = "";
		ModelAndView maw = new ModelAndView("message");
		for(Genre g : genreService.findAll()) {
			if(g.getName().equals(name)) {
				message = "Naziv tog zanra vec postoji!";
				return maw.addObject("message",message);
			}
		}
				
		genreService.create(genre);
		
		response.sendRedirect(baseURL + "Genres");
		return null;
	}
	
	@GetMapping(value = "/SortAsc")
	public ModelAndView sortAsc() {
		List<Genre> genres = genreService.sortByNameAsc();
		
		ModelAndView returnGenres = new ModelAndView("genres");
		returnGenres.addObject("genres", genres);
		
		return returnGenres;
	}
	
	@GetMapping(value = "/SortDesc")
	public ModelAndView sortDesc() {
		List<Genre> genres = genreService.sortByNameDesc();
		
		ModelAndView returnGenres = new ModelAndView("genres");
		returnGenres.addObject("genres", genres);
		
		return returnGenres;
	}
	
	@GetMapping(value = "/Search")
	public ModelAndView search(@RequestParam String search){
		Genre genre = genreService.findByName(search);
		List<Genre> genres = new ArrayList<Genre>();
		
		String message = "";
		
		ModelAndView maw = new ModelAndView("message");
		
		if(genre == null) {
			message = "Ne postoji zanr sa tim nazivom!";
			return maw.addObject("message",message);			
		}
		
		genres.add(genre);
		ModelAndView mawGenre = new ModelAndView("genres");
		mawGenre.addObject("genres", genres);
		
		return mawGenre;
	}
	
	@GetMapping(value = "/Details")
	public ModelAndView details(@RequestParam Long id, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		String message = "";
		
		if(user == null || !user.getUserType().equals(UserEnum.Administrator)) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Nemate prava pristupa ovoj stranici!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		Genre genre = genreService.findOne(id);
		
		ModelAndView maw = new ModelAndView("genre");
		maw.addObject("genre", genre);
		
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
		
		Genre genre = genreService.findOne(id);
		
		ModelAndView maw = new ModelAndView("updateGenreForm");
		maw.addObject("genre", genre);
		
		return maw;
	}
	
	@PostMapping(value = "/Update")
	public void update(@RequestParam Long id, @RequestParam String name, @RequestParam String description, HttpServletResponse response) throws IOException {
		Genre genre = new Genre();
		genre.setId(id);
		genre.setDescription(description);
		genre.setName(name);
		
		genreService.update(genre);
		
		response.sendRedirect(baseURL + "Genres");
	}
	
	@GetMapping(value = "/Delete")
	public ModelAndView delete(@RequestParam Long id, HttpServletResponse response, HttpServletRequest request) throws IOException {
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		String message = "";
		
		if(user == null || !user.getUserType().equals(UserEnum.Administrator)) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Nemate prava pristupa ovoj stranici!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		
		boolean found = false;
		for(Book b : bookService.findAll()) {
			for(Genre g : b.getGenre()) {
				if(g.getId().equals(id))
					found = true;
			}
		}
		
		if(found == true) {
			genreService.logicDelete(id);
		}else {
			genreService.delete(id);
		}	
		
		response.sendRedirect(baseURL + "Genres");
		
		return null;
	}
}
