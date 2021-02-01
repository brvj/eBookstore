package com.sf.heapOfBooks.repository;

import java.util.List;

import com.sf.heapOfBooks.model.Shop;

public interface IReportDAO {

	public List<Shop> findAll();
	
	public List<Shop> filter(String dateFrom, String dateTo, String priceAsc, String priceDesc, String numAsc, String numDesc);
	
	
}
