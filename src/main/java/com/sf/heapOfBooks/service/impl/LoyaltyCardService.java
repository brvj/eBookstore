package com.sf.heapOfBooks.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sf.heapOfBooks.model.LoyaltyCard;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.repository.impl.LoyaltyCardDAO;
import com.sf.heapOfBooks.service.ILoyaltyCardService;

@Service
public class LoyaltyCardService implements ILoyaltyCardService{

	@Autowired
	private LoyaltyCardDAO lcRepository;
	
	private Long newId(List<Long> ids) {
		Long id = null;
		
		if(ids.isEmpty())
			id = 1L;
		else
			id = Collections.max(ids) + 1L;
		
		return id;
	}
	
	@Override
	public LoyaltyCard findOne(Long id) {
		return lcRepository.findOne(id); 
	}

	@Override
	public LoyaltyCard findOneForUser(User user) {
		return lcRepository.findOneForUser(user);
	}

	@Override
	public List<LoyaltyCard> findAll() {
		return lcRepository.findAll();
	}

	@Override
	public List<LoyaltyCard> findAllWithStatusFalse() {
		return lcRepository.findAllWithStatusFalse();
	}

	@Override
	public void create(LoyaltyCard loyaltyCart) {
		List<Long> ids = new ArrayList<Long>();
		
		if(lcRepository.findAll() != null) {
			for(LoyaltyCard lc : lcRepository.findAll()) {
				ids.add(lc.getId());
			}
		}
		
		loyaltyCart.setId(newId(ids));
		
		lcRepository.create(loyaltyCart);
	}

	@Override
	public void accept(LoyaltyCard loyaltyCart) {
		lcRepository.accept(loyaltyCart);
	}

	@Override
	public void reject(LoyaltyCard loyaltyCart) {
		lcRepository.reject(loyaltyCart);
	}

	@Override
	public void updatePoints(LoyaltyCard loyaltyCart) {
		lcRepository.updatePoints(loyaltyCart);
	}

}
