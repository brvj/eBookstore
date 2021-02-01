package com.sf.heapOfBooks.service;

import java.util.List;

import com.sf.heapOfBooks.model.Shop;

public interface IRepostService {
	
	List<Shop> findAll();
	
	List<Shop> filter(String dateFrom, String dateTo, String priceAsc, String priceDesc, String numAsc, String numDesc);
}
