package com.sf.heapOfBooks.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sf.heapOfBooks.model.Genre;
import com.sf.heapOfBooks.repository.impl.GenreDAO;
import com.sf.heapOfBooks.service.IGenreService;

@Service
public class GenreService implements IGenreService{

	@Autowired
	private GenreDAO genreRepository;
	
	private Long newId(List<Long> ids) {
		Long maxId = Collections.max(ids);		
		return maxId + 1L;
	}

	@Override
	public List<Genre> findAll() {
		return genreRepository.findAll();
	}

	@Override
	public Genre findOne(Long id) {
		return genreRepository.findOne(id);
	}

	@Override
	public Genre findByName(String name) {
		return genreRepository.findByName(name);
	}

	@Override
	public Genre create(Genre genre) {
		List<Long> ids = new ArrayList<Long>();
		for(Genre g : findAll()) {
			ids.add(g.getId());
		}
		genre.setId(newId(ids));
		
		genreRepository.create(genre);
		
		return genre;
	}

	@Override
	public void update(Genre genre) {
		genreRepository.update(genre);	
	}

	@Override
	public void delete(Long id) {
		genreRepository.delete(id);
	}

	@Override
	public void logicDelete(Genre genre) {
		genreRepository.logicDelete(genre);
	}

	@Override
	public List<Genre> sortByNameAsc() {
		return genreRepository.sortByNameAsc();
	}

	@Override
	public List<Genre> sortByNameDesc() {
		return genreRepository.sortByNameDesc();
	}
}
