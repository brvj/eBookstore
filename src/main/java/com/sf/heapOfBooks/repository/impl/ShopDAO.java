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
import com.sf.heapOfBooks.model.Shop;
import com.sf.heapOfBooks.model.ShoppingCart;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.model.enums.BookTypeEnum;
import com.sf.heapOfBooks.model.enums.LetterEnum;
import com.sf.heapOfBooks.model.enums.UserEnum;
import com.sf.heapOfBooks.repository.IShopDAO;

@Repository
public class ShopDAO implements IShopDAO{

	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private JdbcTemplate jdbcTemplate;	
	
	private class ShopCallBackHandler implements RowCallbackHandler{

		private Map<Long, Shop> shops = new LinkedHashMap<Long, Shop>();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			
			int i = 1;
			
			Long shopId = rs.getLong(i++);
			float shopPrice = rs.getFloat(i++);
			LocalDate shoppingDate = LocalDate.parse(rs.getString(i++), formatterDate);
			int bookSum = rs.getInt(i++);
			
			Shop shop = shops.get(shopId);
			if(shop == null) {
				shop = new Shop();
				shop.setId(shopId);
				shop.setPrice(shopPrice);
				shop.setShoppingDate(shoppingDate);
				shop.setBookSum(bookSum);
				
				shops.put(shopId, shop);
			}
			
			Long cartId = rs.getLong(i++);
			int numberOfCopies = rs.getInt(i++);
			
			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setId(cartId);
			shoppingCart.setNumberOfCopies(numberOfCopies);
			
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
			
			shoppingCart.setBook(book);
			shoppingCart.setUser(user);	
			
			
			shop.getBoughtBooks().add(shoppingCart);
		}
		public List<Shop> getShop(){
			return new ArrayList<Shop>(shops.values());
		}		
	}

	@Override
	public List<Shop> findAllForUser(User user) {
		String sql = "SELECT s.*, sc.*, b.*, u.* from shop as s"
				+ "	LEFT JOIN shopUserCart suc on suc.shopId = s.id"
				+ "	LEFT JOIN shoppingCarts sc on suc.shoppingCartsId = sc.id"
				+ "	LEFT JOIN shoppingCartUserBook cub ON cub.cartId = sc.id"
				+ " LEFT JOIN books b ON cub.bookId = b.id"
				+ "	LEFT JOIN users u ON cub.userId = u.id"
				+ " WHERE u.id = "+ user.getId() +"";

		ShopCallBackHandler sccbh = new ShopCallBackHandler();
		jdbcTemplate.query(sql, sccbh);
		
		if(sccbh.getShop().isEmpty())
			return null;
		
		return sccbh.getShop();	
	}

	@Override
	public List<Shop> findAll() {
		String sql = "SELECT s.*, sc.*, b.*, u.* from shop as s"
				+ "	LEFT JOIN shopUserCart suc on suc.shopId = s.id"
				+ "	LEFT JOIN shoppingCarts sc on suc.shoppingCartsId = sc.id"
				+ "	LEFT JOIN shoppingCartUserBook cub ON cub.cartId = sc.id"
				+ " LEFT JOIN books b ON cub.bookId = b.id"
				+ "	LEFT JOIN users u ON cub.userId = u.id";

		ShopCallBackHandler sccbh = new ShopCallBackHandler();
		jdbcTemplate.query(sql, sccbh);
		
		if(sccbh.getShop().isEmpty())
			return null;
		
		return sccbh.getShop();	
	}

	@Transactional
	@Override
	public int create(Shop shop) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "INSERT INTO shop(id, price, shoppingDate, bookSum) VALUES (?, ?, ?, ?)";
				
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int i = 1;
				ps.setLong(i++, shop.getId());
				ps.setFloat(i++, shop.getPrice());
				ps.setString(i++, shop.getShoppingDate().toString());
				ps.setInt(i++, shop.getBookSum());
				
				return ps;				
			}
		};	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		
		if(uspeh) {
			String sql = "INSERT INTO shopUserCart(shopId, shoppingCartsId) VALUES(?, ?)";
			for(ShoppingCart sc : shop.getBoughtBooks()) {
				uspeh = uspeh && jdbcTemplate.update(sql, keyHolder.getKey(), sc.getId()) == 1;
			}			
		}	
		return uspeh?1:0;
	}

	@Override
	public Shop findOne(Long id) {
		String sql = "SELECT s.*, sc.*, b.*, u.* from shop as s"
				+ "	LEFT JOIN shopUserCart suc on suc.shopId = s.id"
				+ "	LEFT JOIN shoppingCarts sc on suc.shoppingCartsId = sc.id"
				+ "	LEFT JOIN shoppingCartUserBook cub ON cub.cartId = sc.id"
				+ " LEFT JOIN books b ON cub.bookId = b.id"
				+ "	LEFT JOIN users u ON cub.userId = u.id"
				+ " WHERE b.numberOfCopies > 0 AND s.id = ?";

		ShopCallBackHandler sccbh = new ShopCallBackHandler();
		jdbcTemplate.query(sql, sccbh, id);
		
		if(sccbh.getShop().isEmpty())
			return null;
		
		return sccbh.getShop().get(0);	
	}

	@Override
	public List<Shop> findAllForUserByDateDesc(User user) {
		String sql = "SELECT s.*, sc.*, b.*, u.* from shop as s"
				+ "	LEFT JOIN shopUserCart suc on suc.shopId = s.id"
				+ "	LEFT JOIN shoppingCarts sc on suc.shoppingCartsId = sc.id"
				+ "	LEFT JOIN shoppingCartUserBook cub ON cub.cartId = sc.id"
				+ " LEFT JOIN books b ON cub.bookId = b.id"
				+ "	LEFT JOIN users u ON cub.userId = u.id"
				+ " WHERE b.numberOfCopies > 0 AND u.id = ?"
				+ "	ORDER BY s.shoppingDate DESC";

		ShopCallBackHandler sccbh = new ShopCallBackHandler();
		jdbcTemplate.query(sql, sccbh, user.getId());
		
		if(sccbh.getShop().isEmpty())
			return null;
		
		return sccbh.getShop();	
	}
}
