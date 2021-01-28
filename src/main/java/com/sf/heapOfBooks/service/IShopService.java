package com.sf.heapOfBooks.service;

import java.util.List;

import com.sf.heapOfBooks.model.Shop;
import com.sf.heapOfBooks.model.User;

public interface IShopService {

	List<Shop> findAllForUser(User user);
	
 	List<Shop> findAll();

 	void create(Shop shop);
}
