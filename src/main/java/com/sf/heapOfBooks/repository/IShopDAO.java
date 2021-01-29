package com.sf.heapOfBooks.repository;

import java.util.List;

import com.sf.heapOfBooks.model.Shop;
import com.sf.heapOfBooks.model.User;

public interface IShopDAO {
	
	public Shop findOne(Long id);

	public List<Shop> findAllForUser(User user);
	
	public List<Shop> findAll();
	
	public int create(Shop shop);
	
	public List<Shop> findAllForUserByDateDesc(User user);
	
}
