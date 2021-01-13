package com.sf.heapOfBooks.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
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

import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.service.impl.UserService;

@Controller
@RequestMapping(value = "/Login")
public class LogingController {

	@Autowired
	private UserService userService;
	
	public static final String USER_KEY = "user";
	
	@Autowired
	private ServletContext servletContext;
	private String baseURL;
	
	@PostConstruct
	public void init() {
		baseURL = servletContext.getContextPath() + "/";
	}
	
	@GetMapping
	public ModelAndView login() {
		ModelAndView maw = new ModelAndView("login");
		
		return maw;
	}
	
	@PostMapping
	public ModelAndView login(@RequestParam String userName, @RequestParam String userPassword, 
			HttpServletResponse response, HttpSession session) throws IOException {
		
		User user = userService.login(userName, userPassword);
		
		String message = "";
		
		if(user == null) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Ne postoji korisnik sa tom sifrom ili korisnickim imenom!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		
		if(user.isLoggedIn() == true) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Korisnik je vec prijavljen na sistem!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}else if(session.getAttribute(LogingController.USER_KEY)!=null) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Korisnik je vec prijavljen na sistem!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}else if(user.isUserBlocked() == true) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Korisnik je blokiran!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		
		user.setLoggedIn(true);
		session.setAttribute(LogingController.USER_KEY, user);
		
		if(message.equals(""))
			response.sendRedirect(baseURL + "Books");
			
		return null;
	}
	
	@GetMapping(value = "/Logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = (User) request.getSession().getAttribute(USER_KEY);
		
		String message = "";
		
		if(user == null || user.isLoggedIn() == false) {
			ModelAndView retMessage = new ModelAndView("message");
			
			message = "Korisnik nije prijavljen na sistem!";
			
			retMessage.addObject("message",	message);
			
			return retMessage;
		}
		
		user.setLoggedIn(false);
		request.getSession().removeAttribute(USER_KEY);
		request.getSession().invalidate();
		
		response.sendRedirect(baseURL + "Books");
		
		return null;
	}
}
