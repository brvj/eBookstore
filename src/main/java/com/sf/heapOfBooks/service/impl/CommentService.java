package com.sf.heapOfBooks.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sf.heapOfBooks.model.Comment;
import com.sf.heapOfBooks.repository.impl.CommentDAO;
import com.sf.heapOfBooks.service.ICommentService;

@Service
public class CommentService implements ICommentService{

	@Autowired
	private CommentDAO commentRepository;
	
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
	public Comment findOne(Long id) {
		return commentRepository.findOne(id);
	}

	@Override
	public List<Comment> findAll() {
		return commentRepository.findAll();
	}

	@Override
	public List<Comment> findAllOnWaiting() {
		return commentRepository.findAllOnWaiting();
	}

	@Override
	public List<Comment> findAllAccepted(Long isbn) {
		return commentRepository.findAllAccepted(isbn);
	}

	@Override
	public void updateStatus(Comment comment) {
		commentRepository.updateStatus(comment);
		
	}

	@Override
	public void create(Comment comment) {
		List<Long> ids = new ArrayList<Long>();
				
		if(commentRepository.findAll() != null) {
			for(Comment c : commentRepository.findAll()) {
				ids.add(c.getId());
			}
		}		
		comment.setId(newId(ids));
		
		commentRepository.create(comment);		
	}
}
