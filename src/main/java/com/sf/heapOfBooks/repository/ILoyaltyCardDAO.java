package com.sf.heapOfBooks.repository;

import java.util.List;

import com.sf.heapOfBooks.model.LoyaltyCard;
import com.sf.heapOfBooks.model.User;

public interface ILoyaltyCardDAO {

	public LoyaltyCard findOne(Long id);
	
	public LoyaltyCard findOneForUser(User user);
	
	public List<LoyaltyCard> findAll();
	
	public List<LoyaltyCard> findAllWithStatusFalse();
	
	public int create(LoyaltyCard loyaltyCart);
	
	public int accept(LoyaltyCard loyaltyCart);
	
	public int reject(LoyaltyCard loyaltyCart);
	
	public int updatePoints(LoyaltyCard loyaltyCart);	
}
