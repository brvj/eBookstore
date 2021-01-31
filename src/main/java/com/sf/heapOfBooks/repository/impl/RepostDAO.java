package com.sf.heapOfBooks.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.sf.heapOfBooks.model.Book;
import com.sf.heapOfBooks.model.Shop;
import com.sf.heapOfBooks.model.ShoppingCart;
import com.sf.heapOfBooks.model.User;
import com.sf.heapOfBooks.model.enums.BookTypeEnum;
import com.sf.heapOfBooks.model.enums.LetterEnum;
import com.sf.heapOfBooks.model.enums.UserEnum;
import com.sf.heapOfBooks.repository.IReportDAO;

@Repository
public class RepostDAO implements IReportDAO{

	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private JdbcTemplate jdbcTemplate;	
	
	private class ShopCallBackHandler implements RowCallbackHandler{

		private Map<Long, Shop> shops = new LinkedHashMap<Long, Shop>();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			
			int i = 1;
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
						
			Long shopId = rs.getLong(i++);
			float shopPrice = rs.getFloat(i++);
			LocalDate shoppingDate = LocalDate.parse(rs.getString(i++), formatterDate);
			int bookSum = rs.getInt(i++);
			
			Shop shop = shops.get(shopId);
			if(shop == null) {
				shop = new Shop();
				shop.setId(shopId);
				shop.setShoppingDate(shoppingDate);			
				shops.put(shopId, shop);
			}
			
			Long cartId = rs.getLong(i++);
			int numberOfCopies = rs.getInt(i++);
			
			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setId(cartId);
			shoppingCart.setNumberOfCopies(numberOfCopies);
			

			
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
			
			shop.setPrice(rs.getInt(i++));
			shop.setBookSum(rs.getInt(i++));
			
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
	public List<Shop> findAll() {
		String sql= "SELECT  distinct b.*, s.*, sc.*, u.*, sum(s.price) over(partition by b.name), sum(s.bookSum) over(partition by b.name) FROM shop AS s"      
		+"	LEFT JOIN shopUserCart suc on suc.shopId = s.id"
		+"	LEFT JOIN shoppingCarts sc on suc.shoppingCartsId = sc.id"
		+"	LEFT JOIN shoppingCartUserBook cub ON cub.cartId = sc.id"
		+"	LEFT JOIN books b ON cub.bookId = b.id"
		+"	LEFT JOIN users u ON cub.userId = u.id"
		+ "	ORDER BY s.shoppingDate DESC";
		
		ShopCallBackHandler sccbh = new ShopCallBackHandler();
		jdbcTemplate.query(sql, sccbh);
		
		if(sccbh.getShop().isEmpty())
			return null;
		
		return sccbh.getShop();	
	}

	@Override
	public List<Shop> filter(String dateFrom, String dateTo, String priceAsc, String priceDesc, String numAsc,
			String numDesc) {
		
		String sql= "SELECT  distinct b.*, s.*, sc.*, u.*, sum(s.price) over(partition by b.name), sum(s.bookSum) over(partition by b.name) FROM shop AS s"      
		+"	LEFT JOIN shopUserCart suc on suc.shopId = s.id"
		+"	LEFT JOIN shoppingCarts sc on suc.shoppingCartsId = sc.id"
		+"	LEFT JOIN shoppingCartUserBook cub ON cub.cartId = sc.id"
		+"	LEFT JOIN books b ON cub.bookId = b.id"
		+"	LEFT JOIN users u ON cub.userId = u.id";
		
		StringBuffer whereClause = new StringBuffer("");
		boolean arg = false;
		
		if(dateFrom != "") {
			if(arg)
				whereClause.append("AND");
			
			whereClause.append(" WHERE s.shoppingDate >= '"+dateFrom.trim()+"'");
			arg = true;		
		}
		
		if(dateTo != "") {
			if(arg)
				whereClause.append("AND");
			
			if(dateFrom != "") 
				whereClause.append(" s.shoppingDate <= '"+dateTo.trim()+"'");
			else
				whereClause.append(" WHERE s.shoppingDate <= '"+dateTo.trim()+"'");
						
			arg = true;		
		}
		
		if(priceAsc != null) {

			
			whereClause.append(" ORDER BY s.price ASC ");
			arg = true;
		}
		
		if(priceDesc != null) {

			
			whereClause.append(" ORDER BY s.price DESC ");
			arg = true;
		}
		
		if(numAsc != null) {

			
			whereClause.append(" ORDER BY s.bookSum ASC ");
			arg = true;
		}
		
		if(numDesc != null) {

			
			whereClause.append(" ORDER BY s.bookSum DESC ");
			arg = true;
		}
					

		sql = sql + whereClause.toString();			

				
		ShopCallBackHandler bcbh = new ShopCallBackHandler();
		jdbcTemplate.query(sql, bcbh);
		
		if(bcbh.getShop().isEmpty())
			return null;
		
		return bcbh.getShop();
	}

}
