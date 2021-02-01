package com.sf.heapOfBooks.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.sf.heapOfBooks.model.Book;
import com.sf.heapOfBooks.model.SpecialDate;
import com.sf.heapOfBooks.model.enums.BookTypeEnum;
import com.sf.heapOfBooks.model.enums.LetterEnum;
import com.sf.heapOfBooks.repository.ISpecialDateDAO;

@Repository
public class SpecialDateDAO implements ISpecialDateDAO{

	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private class SpecialDateCallBackHandler implements RowCallbackHandler{

		Map<Long, SpecialDate> specialDates = new LinkedHashMap<Long, SpecialDate>();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			int i = 1;
			
			Long sdId = rs.getLong(i++);
			
			LocalDate date = null;
			String datedate = rs.getString(i++);
			if(datedate != null) {
				date = LocalDate.parse(datedate, formatterDate);
			}		
			int discount = rs.getInt(i++);
			Long idBook = rs.getLong(i++); 
			
			SpecialDate sDate = specialDates.get(sdId);
			if(sDate == null) {
				sDate = new SpecialDate();
				sDate.setId(sdId);
				sDate.setSpecialDate(date);
				sDate.setDiscount(discount);
				
				specialDates.put(sdId, sDate);
			}
			
			//int visak = rs.getInt(i++);
			
			List<String> authors = new ArrayList<String>();

			Long bookId = rs.getLong(i++);
			String bookName = rs.getString(i++);
			String bookPublisher = rs.getString(i++);
			String bookAuthorsLine = rs.getString(i++);
			String[] bookAuthors = bookAuthorsLine.split(",");
			for(String author : bookAuthors) {
				authors.add(author);
			}
			String bookDate = rs.getString(i++);
			String bookDescription = rs.getString(i++);
			String imagePath = rs.getString(i++);
			float bookPrice = rs.getFloat(i++);
			int bookNumberOfPages = rs.getInt(i++);
			BookTypeEnum bookType = BookTypeEnum.valueOf(rs.getString(i++));
			LetterEnum bookLetter = LetterEnum.valueOf(rs.getString(i++));
			String bookLanguage = rs.getString(i++);
			float bookAvgRating = rs.getFloat(i++);
			int bookCopies = rs.getInt(i++);
			
			Book book = new Book(bookName, bookPublisher, authors, LocalDate.parse(bookDate, formatterDate), bookDescription, 
					imagePath, bookPrice, bookNumberOfPages, bookType, bookLetter, bookLanguage, bookCopies);
			book.setISBN(bookId);
			book.setNumberOfCopies(bookCopies);
			book.setAverageRating(bookAvgRating);
			
			sDate.setBook(book);					
		}
		public List<SpecialDate> getSpecialDate(){
			return new ArrayList<SpecialDate>(specialDates.values());
		}		
	}
		
	@Override
	public List<SpecialDate> findAll() {
		String sql = "SELECT bs.*, b.* FROM books AS b"
				+ "	LEFT JOIN bookSpecialDate bs ON bs.bookId = b.id";
		
		SpecialDateCallBackHandler sdbh = new SpecialDateCallBackHandler();
		jdbcTemplate.query(sql, sdbh);
		
		if(sdbh.getSpecialDate().isEmpty()) 
			return null;
		
		return sdbh.getSpecialDate();		
	}

	@Override
	public int create(SpecialDate specialDate) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "INSERT INTO bookSpecialDate(id, specialDate, discount, bookId) VALUES (?, ?, ?, ?)";
				
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				
				int i = 1;
				ps.setLong(i++, specialDate.getId());
				ps.setString(i++,specialDate.getSpecialDate().toString());
				ps.setInt(i++, specialDate.getDiscount());
				ps.setLong(i++, specialDate.getBook().getISBN());
				
				return ps;
			}
		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		
		return uspeh?1:0;
	}

}
