package com.sf.heapOfBooks.service;

import java.util.List;

import com.sf.heapOfBooks.model.SpecialDate;

public interface ISpecialDateService {

	List<SpecialDate> findAll();
	
	void create(SpecialDate specialDate);	
}
