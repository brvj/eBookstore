package com.sf.heapOfBooks.listeners;

import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

import com.sf.heapOfBooks.controller.BookController;

@Component
public class InitHttpSessionListener implements HttpSessionListener{

	public void sessionCreated(HttpSessionEvent arg0) {
		System.out.println("Creating session . . .");
		
		HttpSession session = arg0.getSession();
		session.setAttribute(BookController.LOCAL_KEY, Locale.forLanguageTag("sr"));
		
		System.out.println(". . . session created!");
	}
}
