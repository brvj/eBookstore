package com.sf.heapOfBooks.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sf.heapOfBooks.model.LoyaltyCard;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.model.enums.UserEnum;
import com.sf.heapOfBooks.service.impl.LoyaltyCardService;
import com.sf.heapOfBooks.service.impl.UserService;

@Controller
@RequestMapping(value = "/LoyaltyCard")
public class LoyaltyCardController {

	@Autowired
	private LoyaltyCardService lcService;
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping
	public ModelAndView init(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		String message = "";
		
		if(user == null || !user.getUserType().equals(UserEnum.Administrator)) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Nemate prava pristupa ovoj stranici!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		ModelAndView maw = new ModelAndView("loyaltyCards");
		
		List<LoyaltyCard> lc = lcService.findAllWithStatusFalse();
		
		
		
		if(lc == null) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Nema zahteva za loyalty karticu!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		
		return maw.addObject("lc", lc);
	}
	
	@GetMapping(value = "/Req")
	public ModelAndView request(@RequestParam Long[] userId, @RequestParam("btn") String btn, HttpServletResponse response) throws IOException {		
		if(btn.equals("Accept")) {			
			for(Long ids: userId) {
				lcService.accept(lcService.findOneForUser(userService.findOne(ids)));
				
			}					
			response.sendRedirect("/HeapOfBooks/Books");
			
		}else if(btn.equals("Decline")) {
			for(Long ids: userId) {
				lcService.reject(lcService.findOneForUser(userService.findOne(ids)));
			}
			response.sendRedirect("/HeapOfBooks/Books");
		}
		return null;
	}
	
	@GetMapping(value = "/Create")
	public void create(@RequestParam Long id, HttpServletResponse response) throws IOException {
		LoyaltyCard lc = new LoyaltyCard(userService.findOne(id));
		
		lcService.create(lc);
		
		response.sendRedirect("/HeapOfBooks/Books");
	}
	
	@GetMapping(value = "/Details")
	public ModelAndView details(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(LogingController.USER_KEY);
		
		String message = "";
		
		if(user == null || !user.getUserType().equals(UserEnum.Kupac)) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Nemate prava pristupa ovoj stranici!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		ModelAndView maw = new ModelAndView("loyaltyCardDetail");
		
		LoyaltyCard lc = lcService.findOneForUser(user);
		
		if(lc == null) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Nemate loyalty karticu!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		
		if(lc.isStatus())
			maw.addObject("lc", lc);
		else {
			ModelAndView retMessage = new ModelAndView("message");
		
			message = "Nemate karticu ili vam jos nije odobren zahtev!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		
		return maw;
	}	
}
