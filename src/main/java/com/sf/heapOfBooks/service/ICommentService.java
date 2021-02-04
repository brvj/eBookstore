package com.sf.heapOfBooks.service;

import java.util.List;

import com.sf.heapOfBooks.model.Comment;

public interface ICommentService {

	 Comment findOne(Long id);
	
	 List<Comment> findAll();
	
	 List<Comment> findAllOnWaiting();
	
	 List<Comment> findAllAccepted(Long isbn);
	
	 void updateStatus(Comment comment);
	
	 void create(Comment comment);
}
