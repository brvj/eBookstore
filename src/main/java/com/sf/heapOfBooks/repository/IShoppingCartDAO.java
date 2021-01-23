package com.sf.heapOfBooks.repository;

import java.util.List;

import com.sf.heapOfBooks.model.ShoppingCart;
import com.sf.heapOfBooks.model.User;

public interface IShoppingCartDAO {

	public int create(ShoppingCart shoppingCart);
	
	public int delete(ShoppingCart shoppingCart);
	
	public List<ShoppingCart> findAll(User user);	
	
	public List<ShoppingCart> findAlll();
}
