package com.sf.heapOfBooks.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sf.heapOfBooks.model.Report;
import com.sf.heapOfBooks.model.Shop;
import com.sf.heapOfBooks.model.ShoppingCart;
import com.sf.heapOfBooks.service.impl.ReportService;

@Controller
@RequestMapping(value = "/Report")
public class ReportController {
	
	@Autowired
	private ReportService reposrtService;
	
	
	@GetMapping
	public ModelAndView init(
			@RequestParam(required = false) String dateFrom,
			@RequestParam(required = false) String dateTo,
			@RequestParam(required = false) String priceAsc,
			@RequestParam(required = false) String priceDesc,
			@RequestParam(required = false) String numAsc,
			@RequestParam(required = false) String numDesc) {
		ModelAndView maw = new ModelAndView("report");
		
		
		List<Shop> shop = new ArrayList<Shop>();
		
		if(dateFrom == null && dateTo == null && priceAsc == null && priceDesc == null
				&& numAsc == null && numDesc == null)
			shop = reposrtService.findAll();
		else
			shop = reposrtService.filter(dateFrom , dateTo, priceAsc, priceDesc, numAsc, numDesc);
		
		List<Report> reports = new ArrayList<Report>();
		
		for(Shop s : shop) {
			Report report = new Report();
			
			report.setBookSum(s.getBookSum());
			report.setPrice(s.getPrice());
			
			for(ShoppingCart sc : s.getBoughtBooks()) {
				report.setBook(sc.getBook());
			}
			
			reports.add(report);
		}
		
		int sumCopies = 0;
		float sumPrice = 0;
		
		List<Report> distinctReports = reports.stream().distinct().collect(Collectors.toList());
		
		for(Report r : distinctReports) {
			sumCopies += r.getBookSum();
			sumPrice += r.getPrice();
		}
		
		maw.addObject("report", distinctReports);
		maw.addObject("price", sumPrice);
		maw.addObject("copies", sumCopies);
		
		return maw;
	}

}
