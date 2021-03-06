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
import org.springframework.transaction.annotation.Transactional;

import com.sf.heapOfBooks.model.Book;
import com.sf.heapOfBooks.model.Genre;
import com.sf.heapOfBooks.model.enums.BookTypeEnum;
import com.sf.heapOfBooks.model.enums.LetterEnum;
import com.sf.heapOfBooks.repository.IBookDAO;

@Repository
public class BookDAO implements IBookDAO{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private class BookRowCallBackHandler implements RowCallbackHandler{

		private Map<Long, Book> books = new LinkedHashMap<Long, Book>();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			List<String> authors = new ArrayList<String>();
			
			int i = 1;
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
			
			Book book = books.get(bookId);
			if(book == null) {
				book = new Book(bookName, bookPublisher, authors, LocalDate.parse(bookDate, formatterDate), bookDescription, 
						imagePath, bookPrice, bookNumberOfPages, bookType, bookLetter, bookLanguage, bookCopies);
				book.setISBN(bookId);
				book.setNumberOfCopies(bookCopies);
				book.setAverageRating(bookAvgRating);
				books.put(book.getISBN(), book);
			}
			
			Long genreId = rs.getLong(i++);
			String genreName = rs.getString(i++);
			String genreDescription = rs.getString(i++);
			boolean genreDeleted = Boolean.valueOf(rs.getString(i++));
			Genre genre = new Genre(genreName, genreDescription);
			genre.setId(genreId);
			genre.setDeleted(genreDeleted);

			book.getGenre().add(genre);
		}
		public List<Book> getBooks(){
			return new ArrayList<Book>(books.values());
		}		
	}
	
	@Override
	public List<Book> findAll() {
		String sql = "SELECT b.*,g.* FROM books b"
				+ " LEFT JOIN bookGenre bg ON bg.bookId = b.id"
				+ " LEFT JOIN genres g ON bg.genreId = g.id"
				+ " WHERE b.numberOfCopies > 0";
		BookRowCallBackHandler bcbh = new BookRowCallBackHandler();
		jdbcTemplate.query(sql, bcbh);
		
		return bcbh.getBooks();
	}

	@Override
	public Book findOne(Long id) {
		String sql = "SELECT b.*,g.* FROM books b"
				+ " LEFT JOIN bookGenre bg ON bg.bookId = b.id"
				+ " LEFT JOIN genres g ON bg.genreId = g.id"
				+ "	WHERE b.id = ?";
		BookRowCallBackHandler bcbh = new BookRowCallBackHandler();
		jdbcTemplate.query(sql, bcbh, id);
		
		if(bcbh.getBooks().isEmpty())
			return null;
		
		return bcbh.getBooks().get(0);
	}

	@Transactional
	@Override
	public int create(Book book) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO books (id, name, publicher,authors,releaseDate,description,imagePath,price,numberOfPages,bookType,letter,"
						+ "bookLanguage,avgRating,numberOfCopies) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int i = 1;
				ps.setLong(i++, book.getISBN());
				ps.setString(i++, book.getName());
				ps.setString(i++, book.getPublisher());
				String authors="";
				for(String s : book.getAuthors()) {
					authors += s + ",";
				}
				StringBuffer sb= new StringBuffer(authors);  
				sb.deleteCharAt(sb.length()-1);
				ps.setString(i++, sb.toString());
				ps.setString(i++, book.getReleaseDate().toString());
				ps.setString(i++, book.getShortDescription());
				ps.setString(i++, book.getCoverPicture());
				ps.setFloat(i++, book.getPrice());
				ps.setInt(i++, book.getNumberOfPages());
				ps.setString(i++, book.getBookType().toString());
				ps.setString(i++, book.getLetter().toString());
				ps.setString(i++, book.getBookLanguage());
				ps.setFloat(i++, book.getAverageRating());
				ps.setInt(i++, book.getNumberOfCopies());
				
				return ps;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		if (uspeh) {
			String sql = "INSERT INTO bookGenre (bookId, genreId) VALUES (?, ?)";
			for (Genre g: book.getGenre()) {	
				uspeh = uspeh && jdbcTemplate.update(sql, keyHolder.getKey(), g.getId()) == 1;
			}
		}
		return uspeh?1:0;
	}

	@Transactional
	@Override
	public int update(Book book) {
		String sql = "DELETE FROM bookGenre WHERE bookId = ?";
		jdbcTemplate.update(sql, book.getISBN());
		
		boolean uspeh = true;
		sql = "INSERT INTO bookGenre (bookId, genreId) VALUES (?, ?)";
		for(Genre g : book.getGenre()) {
			uspeh = uspeh && jdbcTemplate.update(sql, book.getISBN(), g.getId()) == 1;
		}
		
		sql = "UPDATE books SET name = ?, publicher = ?, authors = ?, releaseDate = ?, description = ?, price = ?, bookType = ?, letter = ?"
				+ " WHERE id = ?";
		
		String authors="";
		for(String s : book.getAuthors()) {
			authors += s + ",";
		}
		StringBuffer sb= new StringBuffer(authors);  
		sb.deleteCharAt(sb.length()-1);
		
		uspeh = uspeh && jdbcTemplate.update(sql,book.getName(),book.getPublisher(),sb,book.getReleaseDate().toString(),book.getShortDescription(),
				book.getPrice(),book.getBookType().toString(),book.getLetter().toString(),book.getISBN()) == 1;
		
		return uspeh?1:0;
	}

	@Transactional
	@Override
	public int delete(Long id) {
		String sql = "DELETE FROM bookGenre WHERE bookId = ?";
		jdbcTemplate.update(sql, id);
		
		sql = "DELETE FROM shoppingCartUserBook WHERE bookId = ?";
		jdbcTemplate.update(sql, id);
		
		sql = "DELETE FROM bookSpecialDate WHERE bookId = ?";
		jdbcTemplate.update(sql, id);
		
		sql = "DELETE FROM commentUserBook WHERE bookId =?";
		jdbcTemplate.update(sql, id);
		
		sql = "DELETE FROM books WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Transactional
	@Override
	public int orderCopiesOfBook(Book book) {
		boolean uspeh = true;
		String sql = "UPDATE books SET numberOfCopies = ? WHERE id = ?";
		
		uspeh = uspeh && jdbcTemplate.update(sql,book.getNumberOfCopies(),book.getISBN()) == 1;
		
		return uspeh?1:0;
	}
	
	@Override
	public List<Book> orderByNameASC() {
		String sql = "SELECT b.*,g.* FROM books b"
				+ " LEFT JOIN bookGenre bg ON bg.bookId = b.id"
				+ " LEFT JOIN genres g ON bg.genreId = g.id"
				+ "	WHERE b.numberOfCopies > 0"
				+ "	ORDER BY b.name ASC";
		BookRowCallBackHandler bcbh = new BookRowCallBackHandler();
		jdbcTemplate.query(sql, bcbh);
		
		return bcbh.getBooks();
	}

	@Override
	public List<Book> orderByNameDESC() {
		String sql = "SELECT b.*,g.* FROM books b"
				+ " LEFT JOIN bookGenre bg ON bg.bookId = b.id"
				+ " LEFT JOIN genres g ON bg.genreId = g.id"
				+ "	WHERE b.numberOfCopies > 0"
				+ "	ORDER BY b.name DESC";
		BookRowCallBackHandler bcbh = new BookRowCallBackHandler();
		jdbcTemplate.query(sql, bcbh);
		
		return bcbh.getBooks();
	}

	@Override
	public List<Book> orderByPriceASC() {
		String sql = "SELECT b.*,g.* FROM books b"
				+ " LEFT JOIN bookGenre bg ON bg.bookId = b.id"
				+ " LEFT JOIN genres g ON bg.genreId = g.id"
				+ "	WHERE b.numberOfCopies > 0"
				+ "	ORDER BY b.price ASC";
		BookRowCallBackHandler bcbh = new BookRowCallBackHandler();
		jdbcTemplate.query(sql, bcbh);
		
		return bcbh.getBooks();
	}

	@Override
	public List<Book> orderByPriceDESC() {
		String sql = "SELECT b.*,g.* FROM books b"
				+ " LEFT JOIN bookGenre bg ON bg.bookId = b.id"
				+ " LEFT JOIN genres g ON bg.genreId = g.id"
				+ "	WHERE b.numberOfCopies > 0"
				+ "	ORDER BY b.price DESC";
		BookRowCallBackHandler bcbh = new BookRowCallBackHandler();
		jdbcTemplate.query(sql, bcbh);
		
		return bcbh.getBooks();
	}

	@Override
	public List<Book> orderByLanguageASC() {
		String sql = "SELECT b.*,g.* FROM books b"
				+ " LEFT JOIN bookGenre bg ON bg.bookId = b.id"
				+ " LEFT JOIN genres g ON bg.genreId = g.id"
				+ "	WHERE b.numberOfCopies > 0"
				+ "	ORDER BY b.bookLanguage ASC";
		BookRowCallBackHandler bcbh = new BookRowCallBackHandler();
		jdbcTemplate.query(sql, bcbh);
		
		return bcbh.getBooks();
	}

	@Override
	public List<Book> orderByLanguageDESC() {
		String sql = "SELECT b.*,g.* FROM books b"
				+ " LEFT JOIN bookGenre bg ON bg.bookId = b.id"
				+ " LEFT JOIN genres g ON bg.genreId = g.id"
				+ "	WHERE b.numberOfCopies > 0"
				+ "	ORDER BY b.bookLanguage DESC";
		BookRowCallBackHandler bcbh = new BookRowCallBackHandler();
		jdbcTemplate.query(sql, bcbh);
		
		return bcbh.getBooks();
	}

	@Override
	public List<Book> orderByRatingASC() {
		String sql = "SELECT b.*,g.* FROM books b"
				+ " LEFT JOIN bookGenre bg ON bg.bookId = b.id"
				+ " LEFT JOIN genres g ON bg.genreId = g.id"
				+ "	WHERE b.numberOfCopies > 0"
				+ "	ORDER BY b.avgRating ASC";
		BookRowCallBackHandler bcbh = new BookRowCallBackHandler();
		jdbcTemplate.query(sql, bcbh);
		
		return bcbh.getBooks();
	}

	@Override
	public List<Book> orderByRatingDESC() {
		String sql = "SELECT b.*,g.* FROM books b"
				+ " LEFT JOIN bookGenre bg ON bg.bookId = b.id"
				+ " LEFT JOIN genres g ON bg.genreId = g.id"
				+ "	WHERE b.numberOfCopies > 0"
				+ "	ORDER BY b.avgRating DESC";
		BookRowCallBackHandler bcbh = new BookRowCallBackHandler();
		jdbcTemplate.query(sql, bcbh);
		
		return bcbh.getBooks();
	}

	@Override
	public Book searchByISBN(Long isbn) {
		String sql = "SELECT b.*,g.* FROM books b"
				+ " LEFT JOIN bookGenre bg ON bg.bookId = b.id"
				+ " LEFT JOIN genres g ON bg.genreId = g.id"
				+ "	WHERE b.id = ?";
		BookRowCallBackHandler bcbh = new BookRowCallBackHandler();
		jdbcTemplate.query(sql, bcbh, isbn);
		
		if(bcbh.getBooks().isEmpty())
			return null;
		
		return bcbh.getBooks().get(0);
	}

	@Override
	public List<Book> search(String search, Integer priceFrom, Integer priceTo, Long genreId) {
		
		String sql= "SELECT b.*,g.* FROM books b"
				+ " LEFT JOIN bookGenre bg ON bg.bookId = b.id"
				+ " LEFT JOIN genres g ON bg.genreId = g.id";
		
		StringBuffer whereClause = new StringBuffer(" WHERE b.numberOfCopies > 0 AND");
		boolean arg = false;
		
		if(search != null) {
			if(arg)
				whereClause.append("AND");
			
			whereClause.append(" (b.name LIKE '%" + search + "%' OR b.publicher LIKE '%" + search + "%' OR b.bookLanguage LIKE '%" + search + "%') ");
			arg = true;		
		}
		
		if(priceFrom != null) {
			if(arg)
				whereClause.append("AND");
			
			whereClause.append(" b.price >= "+ priceFrom +" ");
			arg = true;
		}
		
		if(priceTo != null) {
			if(arg)
				whereClause.append("AND");
			
			whereClause.append(" b.price <= "+ priceTo +" ");
			arg = true;
		}
		
		if(genreId != null) {
			if(arg)
				whereClause.append("AND");
			
			whereClause.append(" g.id = "+ genreId +" ");
			arg = true;
		}
					
		if(arg) 
			sql = sql + whereClause.toString() + ""
					+ "	ORDER BY b.id";			
		else 
			sql = sql + " WHERE b.numberOfCopies > 0"
					+ " ORDER BY b.id";
				
		BookRowCallBackHandler bcbh = new BookRowCallBackHandler();
		jdbcTemplate.query(sql, bcbh);
		
		if(bcbh.getBooks().isEmpty())
			return null;
		
		return bcbh.getBooks();		
	}

	@Transactional
	@Override
	public int updateRating(Book book) {
		boolean uspeh = true;
		String sql = "UPDATE books SET avgRating = ? WHERE id = ?";
		
		uspeh = uspeh && jdbcTemplate.update(sql,book.getAverageRating(),book.getISBN()) == 1;
		
		return uspeh?1:0;
	}
}
