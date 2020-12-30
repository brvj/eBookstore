package com.sf.heapOfBooks.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

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
@RequestMapping(value = "/Users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ServletContext servletContext;
	private String baseURL;
	
	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@PostConstruct
	public void init() {
		baseURL = servletContext.getContextPath() + "/";
	}
	
	@GetMapping
	public ModelAndView index() {
		List<User> users = userService.findAll();
			
		ModelAndView returnUsers = new ModelAndView("users");
		returnUsers.addObject("users",users);
		
		return returnUsers;		
	}
	
	@GetMapping(value = "/Registration")
	public ModelAndView register() throws IOException {
		ModelAndView maw = new ModelAndView("registration");
		
		return maw;		
	}
	
	@PostMapping(value = "/Registration")
	public ModelAndView register(@RequestParam String userName, @RequestParam String userPassword,@RequestParam String
			eMail,@RequestParam String name,@RequestParam String surname,@RequestParam String dateOfBirth,
			@RequestParam String address,@RequestParam String phoneNumber, HttpServletResponse response) throws ParseException, IOException {
		User user = new User();
		user.setUserName(userName);
		user.setUserPassword(userPassword);
		user.seteMail(eMail);
		user.setName(name);
		user.setSurname(surname);		
		user.setDateOfBirth(LocalDate.parse(dateOfBirth, formatterDate));
		user.setAddress(address);
		user.setPhoneNumber(phoneNumber);
		
		String message = "";
		ModelAndView maw = new ModelAndView("message");
		for(User u : userService.findAll()) {
			if(u.getUserName().equals(userName)) {
				message = "Korisnik sa tim korisnickim imenom vec postoji!";
				return maw.addObject("message",message);
			}
		}
		
		userService.createNewUser(user);
		
		response.sendRedirect(baseURL+"Users");
		return null;
	}
}