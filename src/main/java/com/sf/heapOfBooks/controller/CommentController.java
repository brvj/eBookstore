package com.sf.heapOfBooks.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sf.heapOfBooks.model.Book;
import com.sf.heapOfBooks.model.Comment;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.service.impl.BookService;
import com.sf.heapOfBooks.service.impl.CommentService;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping(value = "/Comments")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private BookService bookService;
	
	@GetMapping(value="/Accepted")
	@ResponseBody
	public Map<String, Object> commentListAccepted(@RequestParam Long isbn){
		
	
		
		List<Comment> comments = commentService.findAllAccepted(isbn);
		
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("response", "ok");
		response.put("comments", comments);
		
		return response;
	}
	
	@PostMapping(value = "/Create")
	public ModelAndView create(@RequestParam Integer rating, @RequestParam String comment, @RequestParam Long isbn, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		Book book = bookService.findOne(isbn);
		LocalDate commentDate = LocalDate.now();

		book.setAverageRating((book.getAverageRating() + rating) / 2);
		
		Comment comm = new Comment();
		
		comm.setBook(book);
		comm.setUser(user);
		comm.setRating(rating);
		comm.setCommentDate(commentDate);
		comm.setComment(comment);
		
		commentService.create(comm);
		bookService.updateRating(book);
		
		return null;
	}
}
