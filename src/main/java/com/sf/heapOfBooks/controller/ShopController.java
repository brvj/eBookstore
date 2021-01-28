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
import com.sf.heapOfBooks.model.Shop;
import com.sf.heapOfBooks.model.ShoppingCart;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.service.impl.BookService;
import com.sf.heapOfBooks.service.impl.ShopService;
import com.sf.heapOfBooks.service.impl.ShoppingCartService;

@Controller
@RequestMapping(value = "/Shop")
public class ShopController {

	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private BookService bookService;
	
	@GetMapping()
	public ModelAndView init(HttpServletRequest request) {
		ModelAndView maw = new ModelAndView("shop");
		
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		List<Shop> shop = shopService.findAllForUser(user);
		
		if(shop == null) {
			ModelAndView retMessage = new ModelAndView("message");
			
			String message = "Nemate ni jednu kupljenu knjigu!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		
		maw.addObject("shop", shop);
		
		return maw;
	}
	
	@PostMapping(value = "/Buy")
	public ModelAndView create(@RequestParam Long[] shoppingCartItemId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		float price = 0;
		LocalDate shoppingDate = LocalDate.now();
		int bookSum = 0;
				
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
	
}
