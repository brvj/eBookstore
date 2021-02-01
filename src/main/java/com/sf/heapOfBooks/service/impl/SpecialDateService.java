package com.sf.heapOfBooks.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sf.heapOfBooks.model.SpecialDate;
import com.sf.heapOfBooks.repository.impl.SpecialDateDAO;
import com.sf.heapOfBooks.service.ISpecialDateService;

@Service
public class SpecialDateService implements ISpecialDateService{
	
	@Autowired
	private SpecialDateDAO specialDateRepository;
	
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
	public List<SpecialDate> findAll() {
		return specialDateRepository.findAll();
	}

	@Override
	public void create(SpecialDate specialDate) {
		List<Long> ids = new ArrayList<Long>();
		
		if(!specialDateRepository.findAll().isEmpty()) {
			for(SpecialDate sd : specialDateRepository.findAll()) {
				ids.add(sd.getId());
			}
		}
		
		specialDate.setId(newId(ids));
		
		specialDateRepository.create(specialDate);
	}
}
