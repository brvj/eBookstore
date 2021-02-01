package com.sf.heapOfBooks.repository;

import java.util.List;

import com.sf.heapOfBooks.model.SpecialDate;

public interface ISpecialDateDAO {

	public List<SpecialDate> findAll();
	
	public int create(SpecialDate specialDate);	
}
