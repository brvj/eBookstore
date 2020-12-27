package com.sf.heapOfBooks.service;

import java.util.List;

import com.sf.heapOfBooks.model.Genre;

public interface IGenreService {
	
	List<Genre> findAll();
	
	Genre findOne(Long id);
	
	Genre findByName(String name);
	
	Genre create(Genre genre);
	
	void update(Genre genre);
	
	void delete(Long id);
	
	void logicDelete(Genre genre);
	
	List<Genre> sortByNameAsc();
	
	List<Genre> sortByNameDesc();
}
