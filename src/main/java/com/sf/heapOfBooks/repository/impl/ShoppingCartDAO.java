package com.sf.heapOfBooks.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.sf.heapOfBooks.model.ShoppingCart;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.model.enums.BookTypeEnum;
import com.sf.heapOfBooks.model.enums.LetterEnum;
import com.sf.heapOfBooks.model.enums.UserEnum;
import com.sf.heapOfBooks.repository.IShoppingCartDAO;

@Repository
public class ShoppingCartDAO implements IShoppingCartDAO{

	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private class ShoppingCartCallBackHandler implements RowCallbackHandler{

		private Map<Long, ShoppingCart> shoppingCarts = new LinkedHashMap<Long, ShoppingCart>();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			int i = 1;
			Long cartId = rs.getLong(i++);
			int numberOfCopies = rs.getInt(i++);
			
			ShoppingCart cart = shoppingCarts.get(cartId);
			if(cart == null) {
				cart = new ShoppingCart(); 
				cart.setId(cartId);
				cart.setNumberOfCopies(numberOfCopies);
				
				shoppingCarts.put(cart.getId(), cart);
			}
			
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
			
			Long id = rs.getLong(i++);
			String userName = rs.getString(i++);
			String userPassword = rs.getString(i++);
			String eMail = rs.getString(i++);
			String name = rs.getString(i++);
			String surname = rs.getString(i++);
			String regDateOfBirth = rs.getString(i++);
			LocalDate dateOfBirth = LocalDate.parse(regDateOfBirth, formatterDate);
			String address = rs.getString(i++);
			String phoneNumber = rs.getString(i++);
			String regDate = rs.getString(i++); 
			LocalDateTime registrationDateAndTime = LocalDateTime.parse(regDate, formatterDateTime);
			String uType = rs.getString(i++);
			UserEnum userType = UserEnum.valueOf(uType);
			boolean userBlocked = rs.getBoolean(i++);
			
			User user = new User(userName, userPassword, eMail, name, surname, dateOfBirth, address, phoneNumber, registrationDateAndTime);
			user.setUserType(userType);
			user.setUserBlocked(userBlocked);
			user.setId(id);
			
			cart.setBook(book);
			cart.setUser(user);			
		}	
		public List<ShoppingCart> getShoppingCart(){
			return new ArrayList<ShoppingCart>(shoppingCarts.values());
		}
	}
	
	@Transactional
	@Override
	public int create(ShoppingCart shoppingCart) {
		PreparedStatementCreator prepraredStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "INSERT INTO shoppingCarts (id, numberOfCopies) VALUES (?, ?)";
				
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int i = 1;
				ps.setLong(i++, shoppingCart.getId());
				ps.setInt(i++, shoppingCart.getNumberOfCopies());
				
				return ps;
			}
		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(prepraredStatementCreator, keyHolder) == 1;
		if(uspeh) {
			String sql = "INSERT INTO shoppingCartUserBook (cartId, bookId, userId) VALUES (?, ?, ?)";
			uspeh = uspeh && jdbcTemplate.update(sql,keyHolder.getKey(),shoppingCart.getBook().getISBN(),shoppingCart.getUser().getId()) == 1;
		}
		return uspeh?1:0;
	}

	@Transactional
	@Override
	public int delete(ShoppingCart shoppingCart) {
		String sql = "DELETE FROM shoppingCarts WHERE id = ?";
		return jdbcTemplate.update(sql,shoppingCart.getId());
	}

	@Override
	public List<ShoppingCart> findAll(User user) {
		String sql = "SELECT c.*,b.*,u.* FROM shoppingCarts c"
				+ " LEFT JOIN shoppingCartUserBook cub ON cub.cartId = c.id"
				+ " LEFT JOIN books b ON cub.bookId = b.id"
				+ "	LEFT JOIN users u ON cub.userId = u.id"
				+ " WHERE b.numberOfCopies > 0 AND u.id = "+ user.getId() +" AND c.id NOT IN (SELECT shoppingCartsId from shopUserCart)";
		ShoppingCartCallBackHandler sccbh = new ShoppingCartCallBackHandler();
		jdbcTemplate.query(sql, sccbh);
		
		if(sccbh.getShoppingCart().isEmpty())
			return null;
		
		return sccbh.getShoppingCart();
	}

	@Override
	public List<ShoppingCart> findAlll() {
		String sql = "SELECT c.*,b.*,u.* FROM shoppingCarts c"
				+ " LEFT JOIN shoppingCartUserBook cub ON cub.cartId = c.id"
				+ " LEFT JOIN books b ON cub.bookId = b.id"
				+ "	LEFT JOIN users u ON cub.userId =  u.id"
				+ " WHERE b.numberOfCopies > 0";
		ShoppingCartCallBackHandler sccbh = new ShoppingCartCallBackHandler();
		jdbcTemplate.query(sql, sccbh);
		
		if(sccbh.getShoppingCart().isEmpty())
			return null;
		
		return sccbh.getShoppingCart();
	}

	@Override
	public ShoppingCart findOne(Long id) {
		String sql = "SELECT c.*,b.*,u.* FROM shoppingCarts c"
				+ " LEFT JOIN shoppingCartUserBook cub ON cub.cartId = c.id"
				+ " LEFT JOIN books b ON cub.bookId = b.id"
				+ "	LEFT JOIN users u ON cub.userId =  u.id"
				+ " WHERE b.numberOfCopies > 0 AND c.id = ?";
		ShoppingCartCallBackHandler sccbh = new ShoppingCartCallBackHandler();
		jdbcTemplate.query(sql, sccbh, id);
		
		if(sccbh.getShoppingCart().isEmpty())
			return null;
		
		return sccbh.getShoppingCart().get(0);		
	}

}
