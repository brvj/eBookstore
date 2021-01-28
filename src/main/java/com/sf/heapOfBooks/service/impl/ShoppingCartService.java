package com.sf.heapOfBooks.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sf.heapOfBooks.model.ShoppingCart;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.repository.impl.ShoppingCartDAO;
import com.sf.heapOfBooks.service.IShoppingCartService;

@Service
public class ShoppingCartService implements IShoppingCartService{

	@Autowired
	private ShoppingCartDAO shoppingCartRepository;
	
	private Long newId(List<Long> ids) {
		Long id = null;
		
		if(ids.isEmpty())
			id = 1L;
		else
			id = Collections.max(ids) + 1L;
		
		return id;
	}
	
	
	@Override
	public void create(ShoppingCart shoppingCart) {
		List<Long> ids = new ArrayList<Long>();
		
		if(shoppingCartRepository.findAlll() != null) {
			for(ShoppingCart sp : shoppingCartRepository.findAlll()) {
				ids.add(sp.getId());
			}
		}
		shoppingCart.setId(newId(ids));	
			
		shoppingCartRepository.create(shoppingCart);
	}

	@Override
	public void delete(ShoppingCart shoppingCart) {
		shoppingCartRepository.delete(shoppingCart);
	}

	@Override
	public List<ShoppingCart> findAll(User user) {
		return shoppingCartRepository.findAll(user);
	}
	@Override
	public List<ShoppingCart> findAlll() {
		return shoppingCartRepository.findAlll();
	}


	@Override
	public ShoppingCart findOne(Long id) {
		return shoppingCartRepository.findOne(id);
	}

}
