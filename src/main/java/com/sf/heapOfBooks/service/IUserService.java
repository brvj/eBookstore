package com.sf.heapOfBooks.service;

import java.util.List;

import com.sf.heapOfBooks.model.User;

public interface IUserService {
	
	User findOne(Long id);
	
	User login(String userName, String userPassword);
	
	void createNewUser(User user);
	
	User update(User user);
	
	User delete(User user);

	List<User> findAll();
	
	void assignAdmin(Long id);
	
	void blockUser(Long id);
	
	void unblockUser(Long id);
}
