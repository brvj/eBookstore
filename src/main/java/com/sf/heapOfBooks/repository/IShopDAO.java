package com.sf.heapOfBooks.repository;

import java.util.List;

import com.sf.heapOfBooks.model.Shop;
import com.sf.heapOfBooks.model.User;

public interface IShopDAO {

	List<Shop> findAllForUser(User user);
	
	List<Shop> findAll();
	
	int create(Shop shop);
	
}
