package com.sf.heapOfBooks.service;

import java.util.List;

import com.sf.heapOfBooks.model.ShoppingCart;
import com.sf.heapOfBooks.model.User;

public interface IShoppingCartService {
	
	void create(ShoppingCart shoppingCart);
	
	void delete(ShoppingCart shoppingCart);
	
	List<ShoppingCart> findAll(User user);	
	
	List<ShoppingCart> findAlll();	
	
	ShoppingCart findOne(Long id);
}
