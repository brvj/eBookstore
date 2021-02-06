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
import com.sf.heapOfBooks.model.enums.CommentStatus;
import com.sf.heapOfBooks.service.impl.BookService;
import com.sf.heapOfBooks.service.impl.CommentService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping(value = "/Comments")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private BookService bookService;
	
	
	@GetMapping
	public ModelAndView init() {
		ModelAndView maw = new ModelAndView("comments");
		return maw;
	}
	
	@GetMapping(value="/Accepted")
	@ResponseBody
	public Map<String, Object> commentListAccepted(@RequestParam Long isbn){
				
		List<Comment> comments = commentService.findAllAccepted(isbn);
		
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("response", "ok");
		response.put("comments", comments);
		
		return response;
	}
	
	@GetMapping(value="/Waiting")
	@ResponseBody
	public Map<String, Object> commentListWaiting(){
				
		List<Comment> comments = commentService.findAllOnWaiting();
		
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("response", "ok");
		response.put("comments", comments);
		
		return response;
	}
	
	@PostMapping(value = "/Create")
	public ModelAndView create(@RequestParam Integer rating, @RequestParam String comment, @RequestParam Long isbn,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		Book book = bookService.findOne(isbn);
		LocalDate commentDate = LocalDate.now();

		
		
		Comment comm = new Comment();
		
		comm.setBook(book);
		comm.setUser(user);
		comm.setRating(rating);
		comm.setCommentDate(commentDate);
		comm.setComment(comment);
		
		commentService.create(comm);
		
		response.sendRedirect("/HeapOfBooks/Books");
		return null;
	}
	
	@PostMapping(value = "/Accept")
	public void accept(@RequestParam Long[] id) throws IOException {
		for(Long l : id) {
			Comment comment = commentService.findOne(l);
			comment.setStatus(CommentStatus.Odobren);			
			commentService.updateStatus(comment);
			
			Book book = bookService.findOne(comment.getBook().getISBN());
			book.setAverageRating((book.getAverageRating() + comment.getRating()) / 2);
			bookService.updateRating(book);
		}		
	}
	
	@PostMapping(value = "/Decline")
	public void decline(@RequestParam Long[] id) throws IOException {
		for(Long l : id) {
			Comment comment = commentService.findOne(l);
			comment.setStatus(CommentStatus.Odbijen);			
			commentService.updateStatus(comment);
		}
	}
}
