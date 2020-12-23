package com.sf.heapOfBooks.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.repository.impl.UserDAO;
import com.sf.heapOfBooks.service.IUserService;

@Service
public class UserService implements IUserService{

	@Autowired
	private UserDAO userRepository;
	
	private Long newId(List<Long> ids) {
		Long maxId = Collections.max(ids);		
		return maxId + 1L;
	}
	
	@Override
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	@Override
	public User findOne(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public User login(String userName, String userPassword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createNewUser(User user) {
		List<Long> ids = new ArrayList<Long>();
		for(User u : findAll()) {
			ids.add(u.getId());
		}
		user.setId(newId(ids));
		user.setRegistrationDateAndTime(LocalDateTime.now());
				
		userRepository.createNewUser(user);
	}

	@Override
	public User update(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User delete(User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
