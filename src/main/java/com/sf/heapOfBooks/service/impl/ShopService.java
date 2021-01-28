package com.sf.heapOfBooks.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sf.heapOfBooks.model.Shop;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.repository.impl.ShopDAO;
import com.sf.heapOfBooks.service.IShopService;

@Service
public class ShopService implements IShopService{

	@Autowired
	private ShopDAO shopRepository;
	
	private Long newId(List<Long> ids) {
		Long id = null;
		if(ids.isEmpty()) {
			id = 1L;
		}else {
			id = Collections.max(ids) + 1L;
		}
		return id;
	}
	
	@Override
	public List<Shop> findAllForUser(User user) {
		return shopRepository.findAllForUser(user);
	}

	@Override
	public List<Shop> findAll() {
		return shopRepository.findAll();
	}

	@Override
	public void create(Shop shop) {
		List<Long> ids = new ArrayList<Long>();
		
		if(shopRepository.findAll() != null) {
			for(Shop s : shopRepository.findAll()) {
				ids.add(s.getId());
			}
		}
		shop.setId(newId(ids));
		
		shopRepository.create(shop);
	}

}
