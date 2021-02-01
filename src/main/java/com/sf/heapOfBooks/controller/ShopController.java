package com.sf.heapOfBooks.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import com.sf.heapOfBooks.model.LoyaltyCard;
import com.sf.heapOfBooks.model.Shop;
import com.sf.heapOfBooks.model.ShoppingCart;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.model.enums.UserEnum;
import com.sf.heapOfBooks.service.impl.BookService;
import com.sf.heapOfBooks.service.impl.LoyaltyCardService;
import com.sf.heapOfBooks.service.impl.ShopService;
import com.sf.heapOfBooks.service.impl.ShoppingCartService;
import com.sf.heapOfBooks.util.PercentageUtil;

@Controller
@RequestMapping(value = "/Shop")
public class ShopController {

	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private LoyaltyCardService lcService;
	
	@GetMapping()
	public ModelAndView init(HttpServletRequest request) {
		ModelAndView maw = new ModelAndView("shop");
		
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		List<Shop> shop = shopService.findAllForUser(user);
		
		String message = "";
		
		if(user == null) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Prijavite se da biste pritupili ovoj stranici!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		
		if(shop == null) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Nemate ni jednu kupljenu knjigu!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		
		maw.addObject("shop", shop);
		
		return maw;
	}
	
	@PostMapping(value = "/Buy")
	public ModelAndView create(@RequestParam Long[] shoppingCartItemId,@RequestParam(required = false) Integer lcPoints, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		float price = 0;
		LocalDate shoppingDate = LocalDate.now();
		int bookSum = 0;
		LoyaltyCard lc = lcService.findOneForUser(user);
				
		List<ShoppingCart> spList = new ArrayList<ShoppingCart>();
		
		for(Long ids : shoppingCartItemId) {
			ShoppingCart shoppingCart = shoppingCartService.findOne(ids);
			Book book = bookService.findOne(shoppingCart.getBook().getISBN());
					
			spList.add(shoppingCart);
			
			bookSum += shoppingCart.getNumberOfCopies();
			
			price += shoppingCart.getNumberOfCopies() * shoppingCart.getBook().getPrice();
			
			if((book.getNumberOfCopies() - bookSum) < 0) {
				ModelAndView retMessage = new ModelAndView("message");
				
				String message = "Nema toliko kopija knjiga!";
				
				retMessage.addObject("message",	message);
				
				return retMessage;
			}			
			book.setNumberOfCopies(book.getNumberOfCopies() - bookSum);
			
			bookService.orderCopiesOfBook(book);
		}
		
		if(lcPoints != null) {			
			if(lcPoints > 10) {
				ModelAndView retMessage = new ModelAndView("message");
				
				String message = "Ne mozete iskoristiti vise od 10 poena!";
				
				retMessage.addObject("message",	message);
				
				return retMessage;
			}			
			float	discountPrice = PercentageUtil.calculatePercentage(lcPoints, price);			
			price = discountPrice;				
			lc.setPoints(lc.getPoints() - lcPoints);
			lc.setDiscount(lc.getPoints()*5);
			lcService.updatePoints(lc);
						
		}	
		if(price > 1000 && lc != null) {
			lc.setPoints(lc.getPoints() + 1);
			lc.setDiscount(lc.getPoints()*5);
			lcService.updatePoints(lc);
		}
		
		Shop shop = new Shop(spList, price, shoppingDate, bookSum);
		
		shopService.create(shop);
		
		response.sendRedirect("/HeapOfBooks/Books");
		
		return null;
	}
	
	@PostMapping(value = "/Remove")
	public void remove(@RequestParam Long id, HttpServletResponse response) throws IOException {
		ShoppingCart sc = shoppingCartService.findOne(id);
		
		shoppingCartService.delete(sc);
		
		response.sendRedirect("/HeapOfBooks/Books");
	}
	
	@GetMapping(value = "/Details")
	public ModelAndView details(@RequestParam Long id, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		String message = "";
		
		if(user == null || !user.getUserType().equals(UserEnum.Administrator)) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Morate se prijaviti da biste pristupili ovoj stranici!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		
		ModelAndView maw = new ModelAndView("shop");
		
		Shop shop = shopService.findOne(id);
		
		maw.addObject("shop", shop);
		
		return maw;
	}
	
	@PostMapping(value = "/BuySpecialDate")
	public ModelAndView buy(@RequestParam Long isbn,@RequestParam(value = "quantity", required = true) Integer quantity,@RequestParam float price, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		Book book = bookService.findOne(isbn);
		ShoppingCart sCart = new ShoppingCart();
		
		LocalDate shoppingDate = LocalDate.now();
		int bookSum = quantity;
		
		sCart.setBook(book);
		sCart.setNumberOfCopies(quantity);
		sCart.setUser(user);
		
		List<Long> ids = new ArrayList<Long>();
		for(ShoppingCart sc : shoppingCartService.findAlll()) {
			ids.add(sc.getId());
		}
		
		sCart.setId(shoppingCartService.newId(ids));	
		System.out.println(sCart.getId());
		shoppingCartService.create(sCart);
									
		Shop shop = new Shop();
		shop.getBoughtBooks().add(sCart);
		shop.setBookSum(bookSum);
		shop.setPrice(price * bookSum);
		shop.setShoppingDate(shoppingDate);
		
		shopService.create(shop);
		
		
		book.setNumberOfCopies(book.getNumberOfCopies() - bookSum);			
		bookService.orderCopiesOfBook(book);
		
		response.sendRedirect("/HeapOfBooks/Books");
		
		return null;
	}
	
}
