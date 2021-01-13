package com.sf.heapOfBooks.repository;

import com.sf.heapOfBooks.model.User;

public interface IUserDAO {

	public User findOne(Long id);
	
	public User login(String userName, String userPassword);
	
	public int createNewUser(User user);
	
	public User update(User user);
	
	public User delete(User user);
	
	public int assignAdmin(Long id);
	
	public int blockUser(Long id);
	
	public int unblockUser(Long id);
}
