package com.sf.heapOfBooks.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
import com.sf.heapOfBooks.model.SpecialDate;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.model.enums.UserEnum;
import com.sf.heapOfBooks.service.impl.BookService;
import com.sf.heapOfBooks.service.impl.SpecialDateService;

@Controller
@RequestMapping(value = "/SpecialDate")
public class SpecialDateController {
	
	@Autowired
	private SpecialDateService sdService;
	
	@Autowired
	private BookService bookService;

	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@GetMapping
	public ModelAndView create() {
		ModelAndView maw = new ModelAndView("specialDate");
		
		maw.addObject("book", bookService.findAll());
		
		return maw;
	}
	
	@PostMapping
	public ModelAndView create(@RequestParam Long[] selectBook, @RequestParam String date, @RequestParam Integer discount, HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		String message = "";
		
		if(user == null || !user.getUserType().equals(UserEnum.Administrator)) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Nemate prava pristupa ovoj stranici!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}	
		
		for(Long isbn : selectBook) {
			Book book = bookService.findOne(isbn);
			SpecialDate sDate = new SpecialDate();
			
			sDate.setBook(book);
			sDate.setDiscount(discount);
			sDate.setSpecialDate(LocalDate.parse(date, formatterDate));
			
			sdService.create(sDate);
		}
		response.sendRedirect("/HeapOfBooks/Books");
		return null;
	}
	
}
