package com.sf.heapOfBooks.service;

import java.util.List;

import com.sf.heapOfBooks.model.LoyaltyCard;
import com.sf.heapOfBooks.model.User;

public interface ILoyaltyCardService {

	 LoyaltyCard findOne(Long id);
	
	 LoyaltyCard findOneForUser(User user);
	
	 List<LoyaltyCard> findAll();
	
	 List<LoyaltyCard> findAllWithStatusFalse();
	
	 void create(LoyaltyCard loyaltyCart);
	
	 void accept(LoyaltyCard loyaltyCart);
	
	 void reject(LoyaltyCard loyaltyCart);
	
	 void updatePoints(LoyaltyCard loyaltyCart);	
}
