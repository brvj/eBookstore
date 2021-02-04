package com.sf.heapOfBooks.repository;

import java.util.List;

import com.sf.heapOfBooks.model.Comment;

public interface ICommentDAO {

	public Comment findOne(Long id);
	
	public List<Comment> findAll();
	
	public List<Comment> findAllOnWaiting();
	
	public List<Comment> findAllAccepted(Long isbn);
	
	public int updateStatus(Comment comment);
	
	public int create(Comment comment);
}
