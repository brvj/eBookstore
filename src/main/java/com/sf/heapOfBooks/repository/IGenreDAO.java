package com.sf.heapOfBooks.repository;

import java.util.List;

import com.sf.heapOfBooks.model.Genre;

public interface IGenreDAO {

	public List<Genre> findAll();
	
	public Genre findOne(Long id);
	
	public Genre findByName(String name);
	
	public int create(Genre genre);
	
	public int update(Genre genre);
	
	public int delete(Long id);
	
	public int logicDelete(Genre genre);
	
	public List<Genre> sortByNameAsc();
	
	public List<Genre> sortByNameDesc();	
}
