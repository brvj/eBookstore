package com.sf.heapOfBooks.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sf.heapOfBooks.model.Shop;
import com.sf.heapOfBooks.repository.IReportDAO;
import com.sf.heapOfBooks.service.IRepostService;

@Service
public class ReportService implements IRepostService{

	@Autowired
	private IReportDAO reportRepository;
	
	@Override
	public List<Shop> findAll() {
		return reportRepository.findAll();
	}

	@Override
	public List<Shop> filter(String dateFrom, String dateTo, String priceAsc, String priceDesc, String numAsc,
			String numDesc) {
		return reportRepository.filter(dateFrom, dateTo, priceAsc, priceDesc, numAsc, numDesc);
	}

}
