package com.sf.heapOfBooks.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sf.heapOfBooks.model.Book;
import com.sf.heapOfBooks.service.impl.BookService;

@Controller
@RequestMapping(value = "/Books")
public class BookController {

	@Autowired
	private BookService bookService;
	
	@GetMapping
	public ModelAndView init() {
		List<Book> books = bookService.findAll();
		
		ModelAndView returnBooks = new ModelAndView("books");
		returnBooks.addObject("books", books);
		
		return returnBooks;		
	}
}
