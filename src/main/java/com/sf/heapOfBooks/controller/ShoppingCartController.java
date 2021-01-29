package com.sf.heapOfBooks.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sf.heapOfBooks.model.Book;
import com.sf.heapOfBooks.model.LoyaltyCard;
import com.sf.heapOfBooks.model.ShoppingCart;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.model.WishBook;
import com.sf.heapOfBooks.service.impl.BookService;
import com.sf.heapOfBooks.service.impl.LoyaltyCardService;
import com.sf.heapOfBooks.service.impl.ShoppingCartService;

@Controller
@RequestMapping(value = "/ShoppingCart")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService spService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private LoyaltyCardService lcService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	public static List<WishBook> bookWishList = new ArrayList<WishBook>();
	public static final String WISH_LIST_KEY = "wishList";
	
	@GetMapping
	public ModelAndView init(HttpServletRequest request) {
		String message = "";
		
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		List<ShoppingCart> spList = spService.findAll(user);
		
		if(spList == null) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Nemate nista u korpi!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		ModelAndView maw = new ModelAndView("shoppingCart");
		
		LoyaltyCard lc = lcService.findOneForUser(user);
		if(lc != null && lc.getPoints() > 0)
			maw.addObject("lc", lc);
		
		maw.addObject("cart", spList);
		
		return maw;
	}
	
	@PostMapping(value = "/Add")
	public void create(@RequestParam int quantity, @RequestParam Long isbn, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		Book book = bookService.findOne(Long.valueOf(isbn));
		
		ShoppingCart sp = new ShoppingCart(book, user);
		sp.setNumberOfCopies(quantity);
		
		spService.create(sp);
			
		@SuppressWarnings("unchecked")
		List<WishBook> wb = (List<WishBook>) request.getSession().getAttribute(WISH_LIST_KEY);
		
		List<WishBook> newList = new ArrayList<WishBook>();
		
		if(wb != null) {
			for(WishBook w : wb) {
				if(!w.getBook().getISBN().equals(Long.valueOf(isbn))) {
					newList.add(w);
				}
			}
		}
			
		session.removeAttribute(WISH_LIST_KEY);
		
		session.setAttribute(WISH_LIST_KEY, newList);
		
		response.sendRedirect("/HeapOfBooks/Books");
	}
	
	@GetMapping(value = "/RemoveWishItem")
	public void removeItem(@RequestParam Long id, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException{
		
		@SuppressWarnings("unchecked")
		List<WishBook> wb = (List<WishBook>) request.getSession().getAttribute(WISH_LIST_KEY);
		
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		List<WishBook> newList = new ArrayList<WishBook>();
			
		if(wb != null) {
			for(WishBook w : wb) {
				if(!w.getBook().getISBN().equals(id) && w.getUser().getId().equals(user.getId())) {
					newList.add(w);
				}
			}
		}
		
		session.setAttribute(WISH_LIST_KEY, newList);

		response.sendRedirect("/HeapOfBooks/Books");	
	}
	
	@GetMapping(value = "/AddToWishList")
	public void addToWishList(@RequestParam Long id, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		
		Book book = bookService.findOne(id);
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		WishBook wb = new WishBook(user, book);
		
		bookWishList.add(wb);
		
		session.setAttribute(WISH_LIST_KEY, bookWishList);
		
		response.sendRedirect("/HeapOfBooks/Books");		
	}
	
	@GetMapping(value = "/Remove")
	public void remove(@RequestParam Long id, HttpServletResponse response) throws IOException {
		ShoppingCart sc = shoppingCartService.findOne(id);
		
		shoppingCartService.delete(sc);
		
		response.sendRedirect("/HeapOfBooks/Books");
	}
}
