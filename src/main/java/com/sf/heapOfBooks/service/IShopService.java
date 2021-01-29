package com.sf.heapOfBooks.service;

import java.util.List;

import com.sf.heapOfBooks.model.Shop;
import com.sf.heapOfBooks.model.User;

public interface IShopService {

	Shop findOne(Long id);
	
	List<Shop> findAllForUser(User user);
	
 	List<Shop> findAll();

 	void create(Shop shop);
 	
 	List<Shop> findAllForUserByDateDesc(User user);
}
